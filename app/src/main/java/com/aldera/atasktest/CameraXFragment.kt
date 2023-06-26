package com.aldera.atasktest

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.aldera.atasktest.databinding.FragmentCameraXBinding
import com.aldera.atasktest.utils.Camera
import com.aldera.atasktest.utils.MLKit
import java.io.File

class CameraXFragment : Fragment() {
    private var _binding: FragmentCameraXBinding? = null
    private val binding get() = _binding!!
    private lateinit var outputDirectory: File
    private val CameraUtil = Camera()
    private val MlKitOCR = MLKit()
    private val red = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraXBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        outputDirectory = getOutputDirectory()
        CameraUtil.startCamera(requireContext(), binding.previewView, viewLifecycleOwner)

        binding.buttonCamera.setOnClickListener {
            CameraUtil.takePhoto(requireContext(), outputDirectory, binding.photoImageView)
            binding.CameraLL.visibility = View.GONE
            binding.ResultsLL.visibility = View.VISIBLE

        }
        binding.scanOCRButton.setOnClickListener {
            var photoBitmap : Bitmap = binding.photoImageView.drawable.toBitmap()
            MlKitOCR.TextRecognition(photoBitmap, requireContext(), binding.resultTextView)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }
}