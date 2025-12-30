/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.audio.fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.tensorflow.lite.examples.audio.AudioClassificationHelper
import org.tensorflow.lite.examples.audio.R
import org.tensorflow.lite.examples.audio.databinding.FragmentAudioBinding
import org.tensorflow.lite.examples.audio.ui.ProbabilitiesAdapter
import org.tensorflow.lite.support.label.Category
import java.io.IOException
import java.text.SimpleDateFormat
import java.io.OutputStream
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import android.net.Uri
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import java.io.FileOutputStream


interface AudioClassificationListener {
    fun onError(error: String)
    fun onResult(results: List<Category>, ordered: List<Category>, inferenceTime: Long)
    //fun saveCsvFile(filename: String)
}

class AudioFragment : Fragment() {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private var recording= false
    private var firstTime= true
    private var data: MutableList<MutableList<String>> = mutableListOf()
    private var categories: MutableList<String> = mutableListOf()

    private var _fragmentBinding: FragmentAudioBinding? = null
    private val fragmentAudioBinding get() = _fragmentBinding!!
    private val adapter by lazy { ProbabilitiesAdapter() }

    private lateinit var audioHelper: AudioClassificationHelper
    //Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //To save metadata
    private var currentMetadataUri: Uri? = null

    //To display Timer
    private var timerHandler: Handler? = null
    private var timerRunnable: Runnable? = null
    private var secondsElapsed: Int = 0
    private var timerTextView: TextView? = null

    private val audioClassificationListener = object : AudioClassificationListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResult(results: List<Category>, ordered: List<Category>, inferenceTime: Long) {
            requireActivity().runOnUiThread {
                if (recording)
                    stackData(ordered)
              //  saveCsvFile("my_data", data)
                adapter.categoryList = results
                adapter.notifyDataSetChanged()
                fragmentAudioBinding.bottomSheetLayout.inferenceTimeVal.text =
                    String.format("%d ms", inferenceTime)
            }
        }

        override fun onError(error: String) {
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                adapter.categoryList = emptyList()
                adapter.notifyDataSetChanged()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun stackData(results: List<Category>){
        // Cycle to add all the elements from results to a new List of String
                if(firstTime) {
                //First time
                    categories.add("Capture location")
                    categories.add("Capture time")
                    for (category in results) {
                        categories.add(category.label)
                    }
                    firstTime=false
                }
            var scores: MutableList<String> = mutableListOf()
            val currentMoment = LocalDateTime.now()
            saveCurrentLocationString { result ->
                val currentLocation = result ?: "Unknown location"
                scores.add(currentLocation)
                var now = ""+ currentMoment.monthValue.toString()+"/"+currentMoment.dayOfMonth.toString()+"/"+currentMoment.year.toString()+"_"+currentMoment.hour.toString()+":"+currentMoment.minute.toString()+":"+currentMoment.second.toString()+":"+(currentMoment.nano/ 1_000_000).toString()
                scores.add(now)
                for (category in results) {
                    scores.add(category.score.toString())
                }
                data.add(scores)
            }

        }

    }
    private fun startRecordingAndClassification() {
        recording = true
        categories.clear()
        data.clear()
        startTimer()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mediaRecorder = MediaRecorder(requireContext())
            } else {
                @Suppress("DEPRECATION")
                mediaRecorder = MediaRecorder()
            }

            val outputDir = requireContext().cacheDir
            audioFile = File.createTempFile("recording_", ".mp3", outputDir)

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioChannels(2)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
        } catch (e: IOException) {
            Log.e("AudioFragment", "MediaRecorder setup failed", e)
            Toast.makeText(context, "Audio recording failed to start.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecordingAndClassification() {
        // 1. Stop audio classification data stacking
        recording = false
        firstTime = true // Reset for the next CSV
        stopTimer()
        // 2. Stop and release MediaRecorder
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: IllegalStateException) {
            // This can happen if stop() is called before start() or after stop()
            Log.e("AudioFragment", "MediaRecorder stop failed: ${e.message}")
        }
        mediaRecorder = null

        // 3. Save the recorded audio file and the CSV data
        val currentDateTime = LocalDateTime.now()
        val baseFilename = "RRAA_" + currentDateTime.year.toString() +
                currentDateTime.month.toString().padStart(2, '0') +
                currentDateTime.dayOfMonth.toString().padStart(2, '0') + "_" +
                currentDateTime.hour.toString().padStart(2, '0') +
                currentDateTime.minute.toString().padStart(2, '0')

        saveAudioFile(baseFilename)
        saveCsvFile(baseFilename) // Your existing function
    }
    private fun startTimer() {
        secondsElapsed = 0
        timerTextView?.visibility = View.VISIBLE
        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                secondsElapsed++
                updateTimerDisplay()
                timerHandler?.postDelayed(this, 1000) // Run again after 1 second
            }
        }
        timerHandler?.post(timerRunnable!!)
    }

    private fun stopTimer() {
        timerHandler?.removeCallbacks(timerRunnable!!)
        timerRunnable = null
        timerHandler = null
        timerTextView?.visibility = View.INVISIBLE
    }

    private fun updateTimerDisplay() {
        val hours = secondsElapsed / 3600
        val minutes = (secondsElapsed % 3600) / 60
        val seconds = secondsElapsed % 60

        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView?.text = timeString
    }
    private fun saveAudioFile(filename: String) {
        if (audioFile == null || !audioFile!!.exists()) {
            Toast.makeText(context, "No audio recording to save.", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.aac")
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/aac")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }

            //val resolver = requireContext().contentResolver
            //val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            val resolver = requireContext().contentResolver
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            uri?.let {
                try {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        audioFile!!.inputStream().copyTo(outputStream)
                    }
                    Toast.makeText(context, "Audio saved to Documents folder", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error saving audio file", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // For older versions, you might need different logic or to request WRITE_EXTERNAL_STORAGE
            Toast.makeText(context, "Saving audio requires Android 10+", Toast.LENGTH_SHORT).show()
        }

        // Clean up the temporary file
        audioFile?.delete()
        audioFile = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View {

        _fragmentBinding = FragmentAudioBinding.inflate(inflater, container, false)
        val start: Button = _fragmentBinding!!.startButton
        val stop: Button = _fragmentBinding!!.stopButton
        val note: Button = _fragmentBinding!!.addComment

        timerTextView = _fragmentBinding!!.timerText

        start.setOnClickListener {
            stop.visibility=View.VISIBLE
            start.visibility=View.INVISIBLE
            startRecordingAndClassification()
           // recording=true
        }
        stop.setOnClickListener {
            start.visibility=View.VISIBLE
            stop.visibility=View.INVISIBLE
            stopRecordingAndClassification()
            //recording=false
            //firstTime=true
            //val currentDateTime = LocalDateTime.now()
            //var fileN = "RRAA_"+ currentDateTime.year.toString()+currentDateTime.month.toString()+currentDateTime.dayOfMonth.toString()+"_"+currentDateTime.hour.toString()+currentDateTime.minute.toString()
            //saveCsvFile(fileN)
        }
        note.setOnClickListener {
            showInputDialog()
        }
        return fragmentAudioBinding.root
    }
    private fun showInputDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.annotations, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextInput)
        val saveButton = dialogView.findViewById<Button>(R.id.buttonSaveAnnotation)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        saveButton.setOnClickListener {
            val inputText = editText.text.toString()
            saveAnnotation(inputText)
            dialog.dismiss()
        }
        dialog.show()
    }
    fun saveCurrentLocationString(onResult: (String?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            onResult(null)
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Location found, format it and return via the callback
                    val locationString = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    onResult(locationString)
                } else {
                    // Location is null, which can happen. Fallback to last known location.
                    // This is a good safety net.
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                        if (lastLocation != null) {
                            val locationString = "Lat: ${lastLocation.latitude}, Lon: ${lastLocation.longitude}"
                            onResult(locationString)
                        } else {
                            onResult("Unknown location")
                        }
                    }
                }
            }
            .addOnFailureListener {
                // Handle failure to get location
                onResult("Location update failed")
            }
    }

    fun saveCsvFile(filename: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.csv")
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }

            val contentResolver = requireContext().contentResolver
            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            if (uri != null) {
                try {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                    outputStream?.use {
                        // Write CSV header (Categories)
                        it.write(categories.joinToString(separator = ";").toByteArray())
                        it.write("\n".toByteArray())
                        // Write data rows
                        data.forEach { row ->
                            it.write(row.joinToString(separator = ";").toByteArray())
                            it.write("\n".toByteArray())
                        }

                        Toast.makeText(context, "CSV file saved in Documents folder", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error saving CSV file", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Error accessing Documents folder", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case for older Android versions (API level < 29)
            Toast.makeText(context, "Requires API level 29 or higher", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveAnnotation(annotation: String){
        val currentMoment = LocalDateTime.now()
        saveCurrentLocationString { result ->
            val currentLocation = result ?: "Unknown location"
            var now = ""+ currentMoment.monthValue.toString()+"/"+currentMoment.dayOfMonth.toString()+"/"+currentMoment.year.toString()+"_"+currentMoment.hour.toString()+":"+currentMoment.minute.toString()+":"+currentMoment.second.toString()+":"+(currentMoment.nano/ 1_000_000).toString()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                Toast.makeText(context, "Requires Android 10 (API 29) or higher", Toast.LENGTH_SHORT).show()
                return@saveCurrentLocationString
            }
            val contentResolver = requireContext().contentResolver

            if (currentMetadataUri == null) {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val filename = "Annotations_$timestamp.json"

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }


                val resolver = requireContext().contentResolver
                currentMetadataUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

                // Initialize file with empty annotations array
                currentMetadataUri?.let { uri ->
                    val initJson = JSONObject().apply {
                        put("annotations", JSONArray())
                    }
                    contentResolver.openOutputStream(uri, "wt")?.use { output ->
                        output.write(initJson.toString(4).toByteArray())
                    }
                }
            }
            val uri = currentMetadataUri
            if (uri == null) {
                Toast.makeText(context, "Error creating metadata file", Toast.LENGTH_SHORT).show()
                return@saveCurrentLocationString
            }
            try {
                // Load current JSON
                val jsonObject = try {
                    contentResolver.openInputStream(uri)?.use { input ->
                        val text = input.bufferedReader().readText()
                        if (text.isNotEmpty()) JSONObject(text) else JSONObject()
                    } ?: JSONObject()
                } catch (e: Exception) {
                    JSONObject()
                }

                // Ensure annotations array exists
                val annotationsArray = if (jsonObject.has("annotations")) {
                    jsonObject.getJSONArray("annotations")
                } else {
                    val newArray = JSONArray()
                    jsonObject.put("annotations", newArray)
                    newArray
                }

                // Append new annotation
                val newAnnotation = JSONObject().apply {
                    put("note", annotation)
                    put("time", now)  // Add timestamp
                    put("location", currentLocation)  // Add GPS later if needed
                }
                annotationsArray.put(newAnnotation)

                // Write updated JSON back
                contentResolver.openOutputStream(uri, "wt")?.use { output ->
                    output.write(jsonObject.toString(4).toByteArray())
                }

                Toast.makeText(context, "Annotation saved", Toast.LENGTH_SHORT).show()

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Error saving annotation", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(requireContext(), "Annotation saved", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAudioBinding.recyclerView.adapter = adapter
        audioHelper = AudioClassificationHelper(
            requireContext(),
            audioClassificationListener
        )
        //Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        // Allow the user to select between multiple supported audio models.
        // The original location and documentation for these models is listed in
        // the `download_model.gradle` file within this sample. You can also create your own
        // audio model by following the documentation here:
        // https://www.tensorflow.org/lite/models/modify/model_maker/speech_recognition
        fragmentAudioBinding.bottomSheetLayout.modelSelector.setOnCheckedChangeListener(
            object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.yamnet -> {
                        audioHelper.stopAudioClassification()
                        audioHelper.currentModel = AudioClassificationHelper.YAMNET_MODEL
                        audioHelper.initClassifier()
                    }
                    R.id.speech_command -> {
                        audioHelper.stopAudioClassification()
                        audioHelper.currentModel = AudioClassificationHelper.SPEECH_COMMAND_MODEL
                        audioHelper.initClassifier()
                    }
                }
            }
        })

        // Allow the user to change the amount of overlap used in classification. More overlap
        // can lead to more accurate resolves in classification.
        fragmentAudioBinding.bottomSheetLayout.spinnerOverlap.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                  parent: AdapterView<*>?,
                  view: View?,
                  position: Int,
                  id: Long
                ) {
                    audioHelper.stopAudioClassification()
                    audioHelper.overlap = 0.25f * position
                    audioHelper.startAudioClassification()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // no op
                }
            }

        // Allow the user to change the max number of results returned by the audio classifier.
        // Currently allows between 1 and 5 results, but can be edited here.
        fragmentAudioBinding.bottomSheetLayout.resultsMinus.setOnClickListener {
            /*if (audioHelper.numOfResults > 1) {
                audioHelper.numOfResults--
                audioHelper.stopAudioClassification()
                audioHelper.initClassifier()
                fragmentAudioBinding.bottomSheetLayout.resultsValue.text =
                    audioHelper.numOfResults.toString()
            }*/
            if (audioHelper.numSamples > 1) {
                audioHelper.numSamples--
                audioHelper.stopAudioClassification()
                audioHelper.initClassifier()
                fragmentAudioBinding.bottomSheetLayout.resultsValue.text =
                    audioHelper.numSamples.toString()
            }
        }

        fragmentAudioBinding.bottomSheetLayout.resultsPlus.setOnClickListener {
            //if (audioHelper.numSamples < 5) {
                audioHelper.numSamples++
                audioHelper.stopAudioClassification()
                audioHelper.initClassifier()
                fragmentAudioBinding.bottomSheetLayout.resultsValue.text =
                    audioHelper.numSamples.toString()
            //}
        }

        // Allow the user to change the confidence threshold required for the classifier to return
        // a result. Increments in steps of 10%.
        fragmentAudioBinding.bottomSheetLayout.thresholdMinus.setOnClickListener {
            if (audioHelper.classificationThreshold >= 0.2) {
                audioHelper.stopAudioClassification()
                audioHelper.classificationThreshold -= 0.1f
                audioHelper.initClassifier()
                fragmentAudioBinding.bottomSheetLayout.thresholdValue.text =
                    String.format("%.2f", audioHelper.classificationThreshold)
            }
        }

        fragmentAudioBinding.bottomSheetLayout.thresholdPlus.setOnClickListener {
            if (audioHelper.classificationThreshold <= 0.8) {
                audioHelper.stopAudioClassification()
                audioHelper.classificationThreshold += 0.1f
                audioHelper.initClassifier()
                fragmentAudioBinding.bottomSheetLayout.thresholdValue.text =
                    String.format("%.2f", audioHelper.classificationThreshold)
            }
        }

        // Allow the user to change the number of threads used for classification
        fragmentAudioBinding.bottomSheetLayout.threadsMinus.setOnClickListener {
            if (audioHelper.numThreads > 1) {
                audioHelper.stopAudioClassification()
                audioHelper.numThreads--
                fragmentAudioBinding.bottomSheetLayout.threadsValue.text = audioHelper
                    .numThreads
                    .toString()
                audioHelper.initClassifier()
            }
        }

        fragmentAudioBinding.bottomSheetLayout.threadsPlus.setOnClickListener {
            if (audioHelper.numThreads < 4) {
                audioHelper.stopAudioClassification()
                audioHelper.numThreads++
                fragmentAudioBinding.bottomSheetLayout.threadsValue.text = audioHelper
                    .numThreads
                    .toString()
                audioHelper.initClassifier()
            }
        }

        // When clicked, change the underlying hardware used for inference. Current options are CPU
        // and NNAPI. GPU is another available option, but when using this option you will need
        // to initialize the classifier on the thread that does the classifying. This requires a
        // different app structure than is used in this sample.
        fragmentAudioBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                  parent: AdapterView<*>?,
                  view: View?,
                  position: Int,
                  id: Long
                ) {
                    audioHelper.stopAudioClassification()
                    audioHelper.currentDelegate = position
                    audioHelper.initClassifier()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }

        fragmentAudioBinding.bottomSheetLayout.spinnerOverlap.setSelection(
            2,
            false
        )
        fragmentAudioBinding.bottomSheetLayout.spinnerDelegate.setSelection(
            0,
            false
        )
    }

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(AudioFragmentDirections.actionAudioToPermissions())
        }

        if (::audioHelper.isInitialized ) {
            audioHelper.startAudioClassification()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::audioHelper.isInitialized ) {
            audioHelper.stopAudioClassification()
        }
    }

    override fun onDestroyView() {
        _fragmentBinding = null
        super.onDestroyView()
    }
}
