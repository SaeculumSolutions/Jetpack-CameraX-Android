# Jetpack-CameraX-Android

[![](https://jitpack.io/v/SaeculumSolutions/Jetpack-CameraX-Android.svg)](https://jitpack.io/#SaeculumSolutions/Jetpack-CameraX-Android)

Jetpack-CameraX is simplest way to click and pick image.

### Jetpack-CameraX camera image picker

Simple implementation 

# Usage

### Dependencies

* Step 1. Add the JitPack repository to your build file
    
    Add it in your root build.gradle at the end of repositories:

    ```groovy
	    allprojects {
		    repositories {
			    ...
			    maven { url 'https://jitpack.io' }
		    }
	    }
    ``` 

* Step 2. Add the dependency
    
    Add it in your app module build.gradle:
    
    ```groovy
        dependencies {
            ...
            implementation 'com.github.SaeculumSolutions:Jetpack-CameraX-Android:1.0.0'
        }
    ``` 
    
 ### Implementation
 
 * Step 1. Add JetPackCameraX in to your activity class:
    
       
    ```kotlin
            val intent = JetPackCameraX(this).build()
                 /*
                 *  if you want open front camera as default use this
                 *  val intent = JetPackCameraX(this).setCameraLensFacingFront(true).build()
                 */
            startActivityForResult(intent, LAUNCH_CAMERA_X_ACTIVITY)
    ```


* Step 2. override onActivityResult function to get JetPackCameraX result.

    ```kotlin
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && data != null) {
                when (requestCode) {
                    LAUNCH_CAMERA_X_ACTIVITY -> {
                    if (data != null) {
                        val path: String = data.extras?.get(CameraXImage.GET_CLICKED_IMAGE_URI).toString()
                        Log.d(TAG, "onActivityResult: $path" )
                        // Do needful with your selectedMedia
                       }
                    }
                }
            }
        }
    ```
### Guideline to report an issue/feature request
It would be great for us if the reporter can share the below things to understand the root cause of the issue.

* Library version
* Code snippet
* Logs if applicable
* Device specification like (Manufacturer, OS version, etc)
* Screenshot/video with steps to reproduce the issue
    
### Requirements

* minSdkVersion >= 21
* Androidx

### Library used

* [Glide](https://github.com/bumptech/glide)
* CameraX:- 			implementation "androidx.camera:camera-core:1.0.0-rc01"
* CameraX Camera2 extensions:-	implementation "androidx.camera:camera-camera2:1.0.0-rc01"
* CameraX Lifecycle library:- 	implementation "androidx.camera:camera-lifecycle:1.0.0-rc01"
* CameraX View class:-        	implementation 'androidx.camera:camera-view:1.0.0-alpha20'

### ProGaurd rules

-dontwarn com.bumptech.glide.**

# LICENSE!

JetPack-CameraX is [Apache License 2.0](/LICENSE).

# Let us know!
We’d be really happy if you send us links to your projects where you use our component. Just send an email to info@saeculumsolutions.com And do let us know if you have any questions or suggestion regarding our work.

