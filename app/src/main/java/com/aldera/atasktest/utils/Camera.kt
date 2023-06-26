package com.aldera.atasktest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.aldera.atasktest.FirstFragment
import java.io.File
import kotlin.math.max
import kotlin.math.min

class Camera {
    private var imageCapture: ImageCapture? = null

    fun startCamera(
        context: Context,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        val processCameraProvider = ProcessCameraProvider.getInstance(context)

        processCameraProvider.addListener({
            val cameraProvider: ProcessCameraProvider = processCameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        previewView.surfaceProvider
                    )
                }
            //set Preview FullScreen
            previewView.scaleType = PreviewView.ScaleType.FIT_CENTER

            imageCapture = ImageCapture.Builder()
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(context, "Use case binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(
        context: Context,
        outputDirectory: File,
        photoImageView: ImageView,
    ) {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            "IMG_${System.currentTimeMillis()}.jpg"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    val resizedBitmap = resizeBitmap(photoFile, 500, 500)
                    photoImageView?.setImageBitmap(resizedBitmap)
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Photo capture failed ${exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    fun resizeBitmap(file: File, targetWidth: Int, targetHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, this)
            val scaleFactor = calculateScaleFactor(outWidth, outHeight, targetWidth, targetHeight)
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        return BitmapFactory.decodeFile(file.path, options)
    }

    fun calculateScaleFactor(
        actualWidth: Int,
        actualHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        return max(1, min(actualWidth / targetWidth, actualHeight / targetHeight))
    }
}