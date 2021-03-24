package com.saeculumsolutions.jetpackcamerax

import android.content.Context
import android.content.Intent

class JetPackCameraX(private val context: Context) {
    private var cameraXConfig = JetPackCameraXConfig()

    /**
     * Set camera lens facing
     */
    fun setCameraLensFacingFront(value: Boolean): JetPackCameraX {
        cameraXConfig.cameraLensFacingFront = value
        return this
    }

    /**
     * Create intent for LassiMediaPickerActivity with config
     */
    fun build(): Intent {
        JetPackCameraXConfig.setConfig(cameraXConfig)
        return Intent(context, CameraXActivity::class.java)
    }
}