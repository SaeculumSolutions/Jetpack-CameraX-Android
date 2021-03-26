package com.saeculumsolutions.camerax

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.saeculumsolutions.jetpackcamerax.CameraXImage
import com.saeculumsolutions.jetpackcamerax.JetPackCameraX
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    companion object {
        const val LAUNCH_CAMERA_X_ACTIVITY = 131
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCameraX = findViewById<Button>(R.id.btnCameraX)
        btnCameraX.setOnClickListener {
            //init intent
            val intent = JetPackCameraX(this).build()
            startActivityForResult(intent, LAUNCH_CAMERA_X_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_CAMERA_X_ACTIVITY && resultCode == RESULT_OK) {
            if (data != null) {
                val path: String =
                    data.extras?.get(CameraXImage.GET_CLICKED_IMAGE_URI).toString()
                val photoFile = File(path)
                Log.d(TAG, "onActivityResult photoFile: ${photoFile.getFileSize()}")
                val imageFile = imageFileRotate(photoFile)
                val imageView = findViewById<ImageView>(R.id.imageView)
                val tvFilePath = findViewById<TextView>(R.id.tvFilePath)
                tvFilePath.text = "Image File Path: ${path}"
                Glide.with(imageView).load(imageFile).into(imageView as ImageView)
                Log.d(TAG, "onActivityResult: ${imageFile.getFileSize()}" )
            }
        }
    }

    /*
    * In This method rotate image according to ExifInterface properties and give you
    * true position when you upload image
    *
    * */
    private fun imageFileRotate(imageFile: File): File {
        var bitmapF: Bitmap? = null
        try {
            val exif = ExifInterface(imageFile.absolutePath)
            val matrix = Matrix()
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> Unit
                ExifInterface.ORIENTATION_UNDEFINED -> Unit
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1F, 1F)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1F, -1F)
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(270F)
                }
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(90F)
                }

                // Error out if the EXIF orientation is invalid
                else -> Log.e("TAG", "Invalid orientation: $orientation")
            }

            Log.v("TAG", "Exif orientation: $orientation")
            val rotattedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            bitmapF = Bitmap.createBitmap(
                rotattedBitmap,
                0,
                0,
                rotattedBitmap.width,
                rotattedBitmap.height,
                matrix,
                true
            )
            val bos = ByteArrayOutputStream()
            bitmapF.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(imageFile)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageFile
    }

    /*
    * return file size
    * */
    fun File.getFileSize(): String {
        val sizeInKb = this.length() / 1024
        if (sizeInKb < 1024) {
            return "$sizeInKb KB"
        }

        val sizeInMb = sizeInKb / 1024
        if (sizeInMb < 1024) {
            return "$sizeInMb MB"
        }

        val sizeInGb = sizeInMb / 1024
        return "$sizeInGb GB"
    }
}