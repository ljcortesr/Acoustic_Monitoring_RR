# Audio Classification Android Demo extended from TensorFlow Lite 

### R&R Overview

Mobile application for Android that captures short segments of environmental audio and performs real-time sound-event classification. The app uses YAMNet, a TensorFlow model trained on AudioSet, to estimate the probability (ranging from 0 to 1) of 521 sound classes being present in each acoustic scene.

Key characteristics: 
- Real-time classification, performed every 0.975 seconds
- Temporal averaging, allowing the user to define how many classification frames are averaged per recorded entry
- Stereo audio recording
- Geolocation and timestamping of each recording
- User-generated annotations, which are also geolocated and time-stamped
- Storage of both the raw audio and the classification outputs for later analysis
- This system currently operates exclusively in real time, analyzing live microphone input on the device.


### TensorFlow Overview

This sample will record audio on a physical Android device and attempt to
classify those recordings. The supported classification models include
[YAMNET](https://tfhub.dev/google/lite-model/yamnet/classification/tflite/1) and
a custom speech command model trained using
[TensorFlow's Model Maker](https://www.tensorflow.org/lite/models/modify/model_maker/speech_recognition).
These instructions walk you through building and running the demo on an Android
device.

The model files are downloaded via Gradle scripts when you build and run the
app. You don't need to do any steps to download TFLite models into the project
explicitly.

This application should be run on a physical Android device.

## Build the demo using Android Studio

### Prerequisites

*   The **[Android Studio](https://developer.android.com/studio/index.html)**
    IDE. This sample has been tested on Android Studio Bumblebee.

*   A physical Android device with a minimum OS version of SDK 23 (Android 6.0)
    with developer mode enabled. The process of enabling developer mode may vary
    by device.

### Building

*   Open Android Studio. From the Welcome screen, select Open an existing
    Android Studio project.

*   From the Open File or Project window that appears, navigate to and select
    the `Mobile App` directory in this repository. Click OK.

*   If it asks you to do a Gradle Sync, click OK.

*   With your Android device connected to your computer and developer mode
    enabled, click on the green Run arrow in Android Studio.

### Models used

Downloading, extraction, and placing the models into the assets folder is
managed automatically by the download_model.gradle file.
