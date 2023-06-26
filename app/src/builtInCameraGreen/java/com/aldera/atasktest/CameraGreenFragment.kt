package com.aldera.atasktest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aldera.atasktest.databinding.FragmentCameraXBinding

class CameraGreenFragment : Fragment() {
    private var _binding: FragmentCameraXBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        Log.d("CameraGreenFragment", "onCreateView")
        _binding = FragmentCameraXBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCameraXBinding.bind(view)
//        binding.cameraView.bindToLifecycle(viewLifecycleOwner)
        Log.d("CameraGreenFragment", "onViewCreated")
    }
}