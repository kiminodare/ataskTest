package com.aldera.atasktest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.aldera.atasktest.databinding.FragmentFirstBinding
import com.aldera.atasktest.model.MathResults
import com.aldera.atasktest.utils.Camera
import com.aldera.atasktest.utils.MLKit
import com.aldera.atasktest.utils.OperatorMath
import com.aldera.atasktest.utils.validationNumber
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.File
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private val ocrExecutor = Executors.newSingleThreadExecutor()
    private lateinit var imageAnalysis: ImageAnalysis
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private val PICK_IMAGE = 1
    private val validationNumber = validationNumber()
    private val OperatorMathRegex = OperatorMath()
    private val CameraUtil = Camera()
    private val MlKitOCR = MLKit()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_CameraXFragment)
        }

        binding.buttonGallery.setOnClickListener {
            pickPictureGallery()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickPictureGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUri = data.data
                val imageBitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                binding?.photoImageView?.setImageBitmap(imageBitmap)
                MlKitOCR.TextRecognition(imageBitmap, requireContext(), binding.resultMathTextView)
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}