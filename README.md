# HiddenCamera
A library that allows you to take picture secretly, in the background without alerting users.
[![Build Status](https://travis-ci.org/CottaCush/HiddenCam.svg?branch=master)](https://travis-ci.org/CottaCush/HiddenCam)

## Gradle Dependency

Add the dependency to your app's `build.gradle`:

```groovy
implementation 'com.cottacush:HiddenCamera:0.0.1'
```

## Usage

It is very easy to get started with Hidden cam. 
First, initialize the camera engine with a `Context`, A `File` that 
represents the base storage folder where the captured images will be saved to, 
and an `OnImageCapturedListener` to get capture results:

```kotlin
val hiddenCam = HiddenCam(context, baseStorageFolder, captureListener)
```
Then prepare the camera for capturing by calling the `start()` method:
 
```kotlin
hiddenCam.start()
```
You can now start capturing images with:
```kotlin
hiddenCam.captureImage()
```
When you are no longer actively capturing images, stop the camera engine to free the camera hardware by calling:

```kotlin
hiddenCam.stop()
```
Finally, to clean up call

```kotlin
hiddenCam.destroy()
```

That's all for basic setup. The captured images should be save at the storage folder provided.




##  License

    Copyright (c) 2019 Cotta & Cush Limited.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
