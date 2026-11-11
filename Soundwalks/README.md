# Soundwalking the Machine: A Guide

This guide oriented the soundwalks of our critical technical practice of environmental machine listening (Appendix B of the paper reproduces it). It is meant to guide walks, not to script them: choose your route, pace, and company, and take up, adapt, or ignore its prompts. You are welcome to adapt it to other listening machines.

## About the activity

*Soundwalking the machine* is a way of examining machine listening systems that "listen to" environmental sounds from the experience of using them, not only as designers but as listeners. A soundwalk is, in Hildegard Westerkamp's words, "any excursion whose main purpose is listening to the environment" (Westerkamp 1974): no matter where, no matter when, as long as listening is at the core of the experience. Here, you walk (or cycle, ride, or sail) with a listening machine alongside you.

The activity has three moments:

1. **Preparation**, to get to know the equipment and imagine the soundwalk.
2. **Soundwalking**, to listen attentively while the machine listens too.
3. **Reflection**, as soon as possible after the walk and while revisiting its recordings.

Keep in mind:

- There is no correct walk and no single correct way of listening. The prompts below are invitations: take those that speak to you and leave the rest.
- Your listening comes first; the app listens alongside you. Look at it when you are curious—not all the time, or not at all if you prefer.
- Notice the moments when the app and your listening disagree or surprise you.
- Your notes and recordings are stored locally, on your device, and you decide what of them, and of your walk, you share.

## The app and the equipment

By machine listening we mean the use of signal processing and machine learning to extract useful information from sound (Ellis 2010). The app runs YAMNet, a classification model trained on the AudioSet dataset, on the phone itself, so no audio leaves the device. YAMNet is a multi-label classifier: as it listens, it gives each of its 521 classes a score between 0 and 1, representing the likelihood that the class is present, and the screen shows the classes ranked by score.

- **Recording.** Starting a recording saves the audio in stereo (AAC, 44.1 kHz, 320 kbps, in an MPEG-4 `.m4a` file) and logs the scores of all 521 classes to a CSV file, each entry with its timestamp and geolocation. Save the recording when you are done, or pause and restart it as you wish.
- **Notes.** With or without a recording running, you can write a note at any moment through "Add note." Each note is saved with its time and location to a JSON file.
- **Binaural recording (optional).** If you carry binaural microphones and can plug them into the phone, they record what reached your ears with an immersive quality, which enriches listening back to the recordings later with headphones.

## Care

- Recording in public places captures passers-by. Avoid recording identifiable people up close or conversations you are not part of; if someone asks what you are doing, explain, and stop recording if they prefer.
- Some places, practices, and conversations are not ours to record or share. What you keep out of the research stays with you.
- Manage your files on your own devices, and share them only if you wish to and it is ethically appropriate.
- When cycling or riding, stop before looking at the app or writing a note, and keep your attention on traffic and terrain. Let your listening accompany the journey in the background, and reflect on it afterward.

## 1. Preparation

- Charge the phone, check that there is free storage, and allow the app to use the microphone and location.
- Plan the walk loosely. A plan can emerge from an acoustic question, the wish to walk common or uncommon places with attentive ears, a usual path repeated, or a search for hidden sounds. You can go alone, with someone else, or in a small group; conversations are welcome when they are part of your listening (see [Care](#care) on recording conversations).
- Imagine the sounds you might find and, if you want, register your thoughts by writing, drawing, recording your voice, or however you prefer:
  - How do you remember and imagine the sounds of this place?
  - Have you walked there before? Do you remember a soundmark of that place, and do you think you will find it again, at this time of year, in this weather, at this time of day?
  - If you have not been there before, is there a "cliché" sound you expect?
  - What do you expect the app to hear there?

## 2. Soundwalking

Start the recording when you set off. When something catches your attention, add a note: a few words are enough, because the note keeps its time and place and lets you return to that moment later. Some prompts and provocations:

- **Ways of listening.** You can focus your auditory attention on sonic textures, sources, qualities, or affects; let yourself be distracted by the noise; feel low vibrations with your body; use artifacts to produce, amplify, silence, or modify sounds; or even read and imagine sounds. Now and then, you might stop and close your eyes. Joella Bitter's "Ten Ways to Listen" (Bitter 2022) may be a wonderful inspiration.
- **What to listen for.** The closest sound and the furthest; the most ephemeral; your favorite and the most disturbing; those made by humans, animals, or objects; the quietest moment.
- **Your own body.** Can you listen to your own body? What sounds do you produce? Can you move and be quiet? Does the app recognize any of those sounds? What does it say?
- **The unheard and the unknown.** Is there a sound you know is there but cannot hear? Can the app hear it? Have you heard a sound whose source you cannot recognize? What do you think it is, and what does the app say?
- **The machine's "mistakes."** Did the app make a mistake? Can you imagine the reason for the confusion? Can you try to trick it?

## 3. Reflection

As soon as possible after the walk, write down your reflections in whatever form suits you: a field note, a drawing, an embroidery, a recording, a conversation. Then revisit the recording, if possible in the companion web tool, which shows the app's log and your notes alongside it, and return to the moments you marked. If you walked with others, share and compare what each of you heard. Some questions to guide your notes or a group discussion:

- Was there a sound that surprised you or stood out during the walk?
- Were any of the app's categories confusing, unexpected, or inconsistent with what you heard? Why do you think the app named them as it did?
- To what extent did the app capture or represent your acoustic experience? What did it leave out?
- Did the app make you pay attention to sounds you might otherwise ignore? How did using it change the way you listened, for better or worse?
- How would you describe the differences between how you identify sounds and how the app does? Which sounds are difficult to label because their meaning or source is ambiguous? Are you, or the app, better at distinguishing certain kinds of sounds?

## References

- Bitter, Joella. 2022. "Ten Ways to Listen." *Anthropology News*, February 17, 2022. <https://anthropology-news.org/articles/ten-ways-to-listen>
- Ellis, Dan. 2010. *A History and Overview of Machine Listening.* Columbia University, New York.
- Westerkamp, Hildegard. 1974. "Soundwalking." *Sound Heritage* 3 (4).
