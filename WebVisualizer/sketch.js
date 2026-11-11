// Audio + CSV synced Top-5 categories (p5.js)
// Files expected:
//   data/data.csv  (semicolon-separated; first 2 columns: Capture location, Capture time)
//   data/audio.aac
//
// CSV time format example: 12/20/2025_19:58:2:557  (M/D/YYYY_HH:MM:S:ms)

let audioEl;           // HTMLAudioElement wrapped by p5 MediaElement
let csvLines = [];
let headers = [];
let rows = [];         // [{t: secondsFromStart, top5: [{name, score}, ...]}]
let ready = false;

// Map
let map;        // Leaflet map
let marker;     // moving marker
let pathLine;   // optional: polyline of the whole route

let annotations = [];               // [{t, lat, lng, note}]
let annFileLoaded = false;
let t0Millis = null;                // CSV start time in ms (same reference as Capture time)
let annLayer = null;                // Leaflet layer group for annotation markers


let csvFileLoaded = false;
let audioFileLoaded = false;


let specCanvas, specCtx, specW, specH;
let specImageData = null;
let specRendered = false;

// Spectrogram settings (grayscale)
const AXIS_W = 60;          // left margin for Y-axis labels
const SPEC_MIN_HZ = 50;     // low frequency bound for log scale
let specSampleRate = null;  // set after decoding audio
const SPEC_DB_MIN = -90;    // noise floor
const SPEC_DB_MAX = -25;    // ceiling

function initSpecCanvas() {
  specCanvas = document.getElementById("spec");
  if (!specCanvas) return false;
  specCtx = specCanvas.getContext("2d", { alpha: false });
  specW = specCanvas.width;
  specH = specCanvas.height;

  specCtx.fillStyle = "white";
  specCtx.fillRect(0, 0, specW, specH);
  return true;
}


function drawSpecYAxis() {
  if (!specCtx || !specSampleRate) return;

  const minHz = SPEC_MIN_HZ;
  const maxHz = specSampleRate / 2;

  specCtx.fillStyle = "white";
  specCtx.fillRect(0, 0, AXIS_W, specH);

  specCtx.fillStyle = "black";
  specCtx.font = "12px system-ui";
  specCtx.textAlign = "right";
  specCtx.textBaseline = "middle";

  const ticks = [100, 200, 330, 600, 1000, 2000, 4000, 6000, 7000];

  function hzToYLocal(hz) {
    const h = Math.max(minHz, Math.min(maxHz, hz));
    const frac = Math.log(h / minHz) / Math.log(maxHz / minHz);
    return specH - 1 - Math.round(frac * (specH - 1));
  }

  specCtx.strokeStyle = "rgba(0,0,0,0.25)";
  for (const hz of ticks) {
    if (hz > maxHz) continue;
    const y = hzToYLocal(hz);

    specCtx.beginPath();
    specCtx.moveTo(AXIS_W - 6, y);
    specCtx.lineTo(AXIS_W, y);
    specCtx.stroke();

    specCtx.fillText(String(hz), AXIS_W - 8, y);
  }

  specCtx.strokeStyle = "rgba(0,0,0,0.35)";
  specCtx.beginPath();
  specCtx.moveTo(AXIS_W + 0.5, 0);
  specCtx.lineTo(AXIS_W + 0.5, specH);
  specCtx.stroke();
}

async function renderFullSpectrogramFromFile(file) {
  if (!initSpecCanvas()) return;

  specCtx.fillStyle = "white";
  specCtx.fillRect(0, 0, specW, specH);
  specCtx.fillStyle = "black";
  specCtx.font = "14px system-ui";
  specCtx.fillText("Computing spectrogram…", 12, 20);

  const arrayBuffer = await file.arrayBuffer();
  const ctx = new (window.AudioContext || window.webkitAudioContext)();
  const audioBuffer = await ctx.decodeAudioData(arrayBuffer);

  specSampleRate = audioBuffer.sampleRate;

  const ch0 = audioBuffer.getChannelData(0);
  let mono = ch0;
  if (audioBuffer.numberOfChannels > 1) {
    const ch1 = audioBuffer.getChannelData(1);
    mono = new Float32Array(ch0.length);
    for (let i = 0; i < mono.length; i++) mono[i] = 0.5 * (ch0[i] + ch1[i]);
  }

  const fftSize = 4096;
  const hopSize = 256;
  const win = hannWindow(fftSize);

  const bins = fftSize / 2;
  const totalFrames = Math.max(1, Math.floor((mono.length - fftSize) / hopSize) + 1);

  const specAreaW = Math.max(1, specW - AXIS_W);
  const frameStride = Math.max(1, Math.floor(totalFrames / specAreaW));

  specImageData = specCtx.createImageData(specW, specH);
  const img = specImageData.data;

  // fill background white
  for (let i = 0; i < img.length; i += 4) {
    img[i] = 255; img[i+1] = 255; img[i+2] = 255; img[i+3] = 255;
  }

  const minHz = SPEC_MIN_HZ;
  const maxHz = specSampleRate / 2;

  function yToBin(y) {
    const frac = y / Math.max(1, (specH - 1));
    const hz = minHz * Math.pow(maxHz / minHz, frac);
    const bin = Math.floor((hz / maxHz) * bins);
    return Math.max(5, Math.min(bins - 1, bin));
  }

  let col = 0;
  for (let frame = 0; frame < totalFrames && col < specAreaW; frame += frameStride, col++) {
    const start = frame * hopSize;

    const re = new Float32Array(fftSize);
    const im = new Float32Array(fftSize);
    for (let i = 0; i < fftSize; i++) {
      re[i] = (mono[start + i] || 0) * win[i];
      im[i] = 0;
    }

    fftRadix2(re, im);

    const x = AXIS_W + col;

    for (let y = 0; y < specH; y++) {
      const yy = (specH - 1 - y);
      const bin = yToBin(y);

      const mag = Math.sqrt(re[bin] * re[bin] + im[bin] * im[bin]);
      const db = 20 * Math.log10(mag + 1e-10);
      const norm = (db - SPEC_DB_MIN) / (SPEC_DB_MAX - SPEC_DB_MIN);
      const t = Math.max(0, Math.min(1, norm));

      // Grayscale: louder energy -> brighter
      const gr = Math.round(t * 255);
      const idx = (yy * specW + x) * 4;

      img[idx] = gr;
      img[idx + 1] = gr;
      img[idx + 2] = gr;
      img[idx + 3] = 255;
    }
  }

  specCtx.putImageData(specImageData, 0, 0);
  drawSpecYAxis();
  specRendered = true;

  ctx.close();
}

// ---------- helpers ----------
function hannWindow(N) {
  const w = new Float32Array(N);
  for (let n = 0; n < N; n++) {
    w[n] = 0.5 * (1 - Math.cos((2 * Math.PI * n) / (N - 1)));
  }
  return w;
}

function clamp255(x) {
  if (x < 0) return 0;
  if (x > 255) return 255;
  return x | 0;
}

// Minimal radix-2 FFT (in-place)
function fftRadix2(re, im) {
  const n = re.length;
  const levels = Math.log2(n);
  if (Math.floor(levels) !== levels) throw new Error("fftSize must be power of 2");

  // Bit-reversal permutation
  for (let i = 0; i < n; i++) {
    const j = reverseBits(i, levels);
    if (j > i) {
      [re[i], re[j]] = [re[j], re[i]];
      [im[i], im[j]] = [im[j], im[i]];
    }
  }

  for (let size = 2; size <= n; size <<= 1) {
    const half = size >>> 1;
    const tableStep = (2 * Math.PI) / size;
    for (let i = 0; i < n; i += size) {
      for (let j = 0; j < half; j++) {
        const k = i + j;
        const l = k + half;

        const angle = j * tableStep;
        const wr = Math.cos(angle);
        const wi = -Math.sin(angle);

        const tr = wr * re[l] - wi * im[l];
        const ti = wr * im[l] + wi * re[l];

        re[l] = re[k] - tr;
        im[l] = im[k] - ti;
        re[k] = re[k] + tr;
        im[k] = im[k] + ti;
      }
    }
  }
}

function reverseBits(x, bits) {
  let y = 0;
  for (let i = 0; i < bits; i++) {
    y = (y << 1) | (x & 1);
    x >>>= 1;
  }
  return y;
}


function parseLatLng(s) {
  const nums = String(s).match(/-?\d+(?:\.\d+)?/g);
  if (!nums || nums.length < 2) return null;

  const lat = parseFloat(nums[0]);
  const lng = parseFloat(nums[1]);

  if (!Number.isFinite(lat) || !Number.isFinite(lng)) return null;
  return { lat, lng };
}

function parseCaptureTimeToMillis(s) {
  // "M/D/YYYY_HH:MM:S:ms"
  const parts = s.split("_");
  const datePart = parts[0];
  const timePart = parts[1];

  const [m, d, y] = datePart.split("/").map(v => parseInt(v, 10));
  const [hh, mm, ss, ms] = timePart.split(":").map(v => parseInt(v, 10));

  const dt = new Date(y, m - 1, d, hh, mm, ss, ms);
  return dt.getTime();
}

function topNFromScores(obj, n) {
  // obj: {header: numberScore, ...} already excludes non-score columns
  // returns sorted desc by score
  const arr = [];
  for (const [k, v] of Object.entries(obj)) {
    const num = Number(v);
    if (!Number.isFinite(num)) continue;
    arr.push({ name: k, score: num });
  }
  arr.sort((a, b) => b.score - a.score);
  return arr.slice(0, n);
}

function tryInitMap() {
  if (!csvFileLoaded || rows.length === 0) return;

  // Build route points
  const latlngs = rows.map(r => [r.lat, r.lng]);

  // Reset existing map if any
  if (map) {
    map.remove();
    map = null;
  }

  map = L.map("map");
  refreshAnnotationMarkers();

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors',
  }).addTo(map);

  // Draw the full movement path
  if (pathLine) pathLine.remove();
  pathLine = L.polyline(latlngs, { weight: 4, opacity: 0.7 }).addTo(map);

  // Fit view to the whole route (with padding)
  const bounds = pathLine.getBounds();
  map.fitBounds(bounds, { padding: [20, 20] });

  // Start marker at first point
  const first = latlngs[0];
  if (marker) marker.remove();
  marker = L.marker(first).addTo(map);
}
function markerLatLngAtAudioTime(tAudio) {
  if (!rows.length) return null;

  // clamp
  if (tAudio <= rows[0].t) return [rows[0].lat, rows[0].lng];
  if (tAudio >= rows[rows.length - 1].t) return [rows[rows.length - 1].lat, rows[rows.length - 1].lng];

  // Find segment [i, i+1] such that rows[i].t <= tAudio <= rows[i+1].t
  let lo = 0, hi = rows.length - 2;
  while (lo <= hi) {
    const mid = (lo + hi) >> 1;
    if (rows[mid + 1].t < tAudio) lo = mid + 1;
    else if (rows[mid].t > tAudio) hi = mid - 1;
    else { lo = mid; break; }
  }
  const i = Math.max(0, Math.min(rows.length - 2, lo));

  const a = rows[i];
  const b = rows[i + 1];

  const span = b.t - a.t;
  const alpha = span > 0 ? (tAudio - a.t) / span : 0; // 0..1

  const lat = a.lat + (b.lat - a.lat) * alpha;
  const lng = a.lng + (b.lng - a.lng) * alpha;

  return [lat, lng];
}

function latLngAtTime(t) {
  if (!rows.length) return null;
  if (t <= rows[0].t) return [rows[0].lat, rows[0].lng];
  if (t >= rows[rows.length - 1].t) return [rows[rows.length - 1].lat, rows[rows.length - 1].lng];

  // Find the segment [i, i+1] that contains t
  let lo = 0, hi = rows.length - 2;
  while (lo <= hi) {
    const mid = (lo + hi) >> 1;
    if (rows[mid + 1].t < t) lo = mid + 1;
    else if (rows[mid].t > t) hi = mid - 1;
    else { lo = mid; break; }
  }
  const i = Math.max(0, Math.min(rows.length - 2, lo));

  const a = rows[i];
  const b = rows[i + 1];

  const span = b.t - a.t;
  const alpha = span > 0 ? (t - a.t) / span : 0;

  const lat = a.lat + (b.lat - a.lat) * alpha;
  const lng = a.lng + (b.lng - a.lng) * alpha;

  return [lat, lng];
}

function setup() {

  // CSV upload
  const csvInput = document.getElementById("csvInput");
  csvInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      csvLines = reader.result.split(/\r?\n/);
      parseCSV();
      csvFileLoaded = true;
      tryInitMap();
    };
    reader.readAsText(file);
  });

  // Audio upload
  const audioInput = document.getElementById("audioInput");
  audioInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const url = URL.createObjectURL(file);

    if (audioEl) audioEl.remove();
    audioEl = createAudio(url);
    audioEl.parent("audio-holder");
    audioEl.attribute("controls", "");
    audioEl.style("width", "100%");
    audioEl.elt.preload = "auto";
    renderFullSpectrogramFromFile(file);   
     
    audioEl.elt.addEventListener("play", () => {
        if (audioEl.elt.currentTime > 0.001) return; // don't force reset if user resumes
        // keep as-is; first play starts at 0
      });
    audioFileLoaded = true;
  });
 

  // Annotations upload
  const annInput = document.getElementById("annInput");
  annInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      try {
        const obj = JSON.parse(reader.result);

        // Important: we need t0Millis from CSV to convert absolute time -> relative seconds
        if (t0Millis == null) {
          alert("Upload the CSV first (so we can align annotation times).");
          return;
        }

        annotations = parseAnnotationsJSON(obj);
        annFileLoaded = true;
        refreshAnnotationMarkers();
      } catch (err) {
        console.error(err);
        alert("Could not parse JSON annotations file.");
      }
    };
    reader.readAsText(file);
  });
  
  parseCSV();

  ready = rows.length > 0;
  
  if (ready) {
    const first = rows[0];

    map = L.map("map").setView([first.lat, first.lng], 16);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution: '&copy; OpenStreetMap contributors',
    }).addTo(map);

    marker = L.marker([first.lat, first.lng]).addTo(map);

    // Optional: show the whole route as a faint line
    const latlngs = rows.map(r => [r.lat, r.lng]);
    pathLine = L.polyline(latlngs, { weight: 3, opacity: 0.5 }).addTo(map);
    
  }

  // Canvas LAST (below map)
  const c = createCanvas(820, 360);
  c.parent("canvas-holder");

  textFont("system-ui");
  

}

function refreshAnnotationMarkers() {
  if (!map) return;

  if (annLayer) annLayer.remove();
  annLayer = L.layerGroup().addTo(map);

  for (const a of annotations) {
    const popupHtml =
      `<div style="max-width:260px;">
         <div style="font-weight:700; margin-bottom:6px;">Annotation</div>
         <div style="margin-bottom:8px;">${escapeHTML(a.note)}</div>
         <div style="font-size:12px; color:#666;">t = ${a.t.toFixed(2)}s</div>
       </div>`;

    L.marker([a.lat, a.lng]).addTo(annLayer).bindPopup(popupHtml);
  }
}

function escapeHTML(s) {
  return String(s)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function parseCSV() {
  if (!csvLines || csvLines.length < 2) return;

  headers = csvLines[0].split(";").map(h => h.trim());

  const idxTime = headers.indexOf("Capture time");
  const idxLoc  = headers.indexOf("Capture location");
  if (idxTime < 0 || idxLoc < 0) return;

  // Parse all capture times first to compute t0 (only for rows with valid lat/lng)
  const times = [];
  for (let i = 1; i < csvLines.length; i++) {
    const cols = csvLines[i].split(";");
    if (cols.length !== headers.length) continue;

    const loc = parseLatLng(cols[idxLoc]);
    if (!loc) continue;

    times.push(parseCaptureTimeToMillis(cols[idxTime]));
  }
  if (times.length === 0) return;

  //const t0 = Math.min(...times);
  t0Millis = Math.min(...times);

  // Build rows with precomputed top-5 + lat/lng
  rows = [];
  for (let i = 1; i < csvLines.length; i++) {
    const cols = csvLines[i].split(";");
    if (cols.length !== headers.length) continue;

    const loc = parseLatLng(cols[idxLoc]);
    if (!loc) continue;

    const tMillis = parseCaptureTimeToMillis(cols[idxTime]);
    //const t = (tMillis - t0) / 1000.0;
    const t = (tMillis - t0Millis) / 1000.0;

    const scoreObj = {};
    for (let j = 0; j < headers.length; j++) {
      const h = headers[j];
      if (h === "Capture location" || h === "Capture time") continue;
      scoreObj[h] = cols[j];
    }

    const top5 = topNFromScores(scoreObj, 5);
    rows.push({ t, top5, lat: loc.lat, lng: loc.lng });
  }
  
  rows.sort((a, b) => a.t - b.t);
}

function parseAnnotationsJSON(obj) {
  if (!obj || !Array.isArray(obj.annotations)) return [];

  const out = [];
  for (const a of obj.annotations) {
    const loc = parseLatLng(a.location);
    if (!loc) continue;

    const ms = parseCaptureTimeToMillis(a.time);
    if (!Number.isFinite(ms)) continue;

    // Convert absolute annotation time -> seconds from CSV start
    // (requires t0Millis already computed from CSV)
    if (t0Millis == null) continue;
    const t = (ms - t0Millis) / 1000.0;

    out.push({
      t,
      lat: loc.lat,
      lng: loc.lng,
      note: String(a.note || "").trim(),
    });
  }

  out.sort((x, y) => x.t - y.t);
  return out;
}

function findRowForTime(t) {
  // returns the last row with row.t <= t (binary search)
  if (!rows.length) return null;
  if (t <= rows[0].t) return rows[0];
  if (t >= rows[rows.length - 1].t) return rows[rows.length - 1];

  let lo = 0, hi = rows.length - 1;
  while (lo <= hi) {
    const mid = (lo + hi) >> 1;
    if (rows[mid].t <= t) lo = mid + 1;
    else hi = mid - 1;
  }
  return rows[Math.max(0, hi)];
}

function fmtTime(sec) {
  const s = Math.max(0, sec);
  const m = Math.floor(s / 60);
  const r = Math.floor(s - m * 60);
  return `${m}:${String(r).padStart(2, "0")}`;
}

function drawPlayheadOnSpectrogram() {
  if (!specRendered || !specCtx || !audioEl?.elt || !specImageData) return;

  const dur = audioEl.elt.duration;
  if (!Number.isFinite(dur) || dur <= 0) return;

  // Redraw base spectrogram + axis
  specCtx.putImageData(specImageData, 0, 0);
  drawSpecYAxis();

  // Playhead in spectrogram area (exclude axis margin)
  const specAreaW = Math.max(1, specW - AXIS_W);
  const xInArea = Math.floor((audioEl.elt.currentTime / dur) * specAreaW);
  const x = AXIS_W + Math.max(0, Math.min(specAreaW - 1, xInArea));

  specCtx.fillStyle = "rgba(36, 36, 36, 0.7)";
  specCtx.fillRect(x, 0, 2, specH);
}

function draw() {  
  if (!csvFileLoaded || !audioFileLoaded) {
    background(255);
    fill(0);
    textSize(14);
    text("Upload a CSV and an audio file to begin.", 22, 40);
    return;
  }
  background(255);
  //updateSpectrogram();
  drawPlayheadOnSpectrogram();
  // Header
  fill(0);
  textSize(18);
  text("Top 5 categories at current time", 22, 30);

  textSize(14);
  fill(80);


  const t = audioEl?.elt?.currentTime ?? 0;
  const activeNotes = annotations
  .filter(a => t >= a.t && t < a.t + 10)
  .map(a => a.note);
  
  const row = findRowForTime(t);

  
  if (map && marker) {
    const ll = markerLatLngAtAudioTime(t);
    if (ll) marker.setLatLng(ll);
  }
 
  text(`Audio time: ${fmtTime(t)} / ${fmtTime(audioEl.elt.duration || 0)}`, 22, 56);

  // Display list (underlined, like your mockups)
  const startY = 110;
  const lineH = 44;

  if (!row) {
    fill(0);
    textSize(14);
    text("No data for this time.", 22, startY);
    return;
  }

  for (let i = 0; i < row.top5.length; i++) {
    const item = row.top5[i];
    const y = startY + i * lineH;

    // Category label
    fill(0);
    textStyle(BOLD);
    textSize(18);
    const label = item.name;

    // Underline by drawing a line under the text width
    const x = 80;
    text(label, x, y);
    const w = textWidth(label);
    

    // score (optional; comment out if you only want the names)
    noStroke();
    fill(110);
    textStyle(NORMAL);
    textSize(14);
    text(nf(item.score, 0, 3), x + w + 14, y);
  }
  // Right-side annotation box
  if (activeNotes.length > 0) {
    const boxX = 470;
    const boxY = 90;
    const boxW = 320;
    const boxH = 240;

    noStroke();
    fill(250);
    rect(boxX, boxY, boxW, boxH, 12);

    fill(0);
    textStyle(BOLD);
    textSize(14);
    text("Annotations (10s)", boxX + 12, boxY + 24);

    textStyle(NORMAL);
    textSize(12);
    let yy = boxY + 46;

    for (const note of activeNotes.slice(0, 3)) { // show up to 3 at once
      text(note, boxX + 12, yy, boxW - 24, 80);
      yy += 86;
    }
  }
}
