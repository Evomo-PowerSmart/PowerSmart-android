package com.evomo.powersmart.ui.screen.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evomo.powersmart.R
import com.evomo.powersmart.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.md_theme_primary)

        binding.toolbar.apply {
            title = "Notifications"
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adapter dengan callback onItemClick
        val adapter = NotificationsAdapter { anomaly ->
            // Saat item diklik, navigasi ke AnomalyDetailFragment dan kirim anomalyId
            val action = NotificationsFragmentDirections
                .actionNotificationsFragmentToAnomalyDetailFragment(anomaly.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        notificationsViewModel.anomalies.observe(viewLifecycleOwner, Observer { anomalies ->
            if (anomalies.isNullOrEmpty()) {
                binding.tvPlaceholder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.notificationCount.text = "0 Notifications"  // Update count to 0
            } else {
                binding.tvPlaceholder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(anomalies)
                binding.notificationCount.text = "${anomalies.size} Notifications"  // Update count dynamically
            }
        })

        notificationsViewModel.fetchAnomalies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
