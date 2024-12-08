package com.evomo.powersmart.ui.screen.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evomo.powersmart.databinding.FragmentNotificationsBinding
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        binding.cvToolbar.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        TopAppBar(
                            colors = commonTopAppBarColor(),
                            title = {
                                Text(text = "Notifications")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    Navigation.findNavController(binding.root).popBackStack()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                }
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

        notificationsViewModel.anomalies.observe(viewLifecycleOwner) { anomalies ->
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
        }

        notificationsViewModel.fetchAnomalies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
