package com.evomo.powersmart.ui.screen.monitoring_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evomo.powersmart.databinding.FragmentMonitoringDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonitoringDetailFragment : Fragment() {

    private var _binding: FragmentMonitoringDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMonitoringDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}