package com.saeculumsolutions.jetpackcamerax

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JetPackCameraXConfig(
    var cameraLensFacingFront: Boolean = false
) : Parcelable {
    companion object {
        private var cameraConfig = JetPackCameraXConfig()

        fun setConfig(JetPackCameraXConfig: JetPackCameraXConfig) {
            cameraConfig.apply {
                cameraLensFacingFront = JetPackCameraXConfig.cameraLensFacingFront
            }
        }

        fun getConfig(): JetPackCameraXConfig {
            return cameraConfig
        }
    }
}