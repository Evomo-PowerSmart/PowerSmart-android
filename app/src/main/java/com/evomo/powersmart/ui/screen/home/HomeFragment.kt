package com.evomo.powersmart.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.evomo.powersmart.databinding.FragmentHomeBinding
import com.evomo.powersmart.ui.screen.home.components.NotificationItem
import com.evomo.powersmart.ui.screen.home.components.RealtimeBox
import com.evomo.powersmart.ui.screen.home.components.SmallProfileIcon
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import com.evomo.powersmart.ui.theme.spacing
import com.evomo.powersmart.ui.utils.toDateString
import com.evomo.powersmart.ui.utils.toTimestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvHome.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        val state by viewModel.state.collectAsState()

                        val snackbarHomeState = remember { SnackbarHostState() }

                        LaunchedEffect(key1 = true) {
                            viewModel.messageFlow.collectLatest { message ->
                                snackbarHomeState.showSnackbar(message)
                            }
                        }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHomeState) },
                            topBar = {
                                TopAppBar(
                                    colors = commonTopAppBarColor(),
                                    title = { Text(text = "Power Smart") },
                                    actions = {
                                        Row(
                                            modifier = Modifier.padding(end = MaterialTheme.spacing.extraMedium)
                                        ) {
                                            SmallProfileIcon(
                                                photoUrl = state.profilePictureUrl,
                                                onClick = {
                                                    Navigation.findNavController(view).navigate(
                                                        HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                                                    )
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(MaterialTheme.spacing.medium)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Realtime Monitoring",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(onClick = {
                                        Navigation.findNavController(view).navigate(
                                            HomeFragmentDirections.actionHomeFragmentToMonitoringDetailFragment()
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                RealtimeBox(
                                    location = "Lantai 2",
                                    lastUpdated = state.lastUpdateTime ?: "No data available yet",
                                    unitDetail = "Daya Per Hour",
                                    value = state.activeEnergyImport / 1000.0,
                                    unit = "kWh",
                                    status = state.status,
                                    energyIn = state.energyIn,
                                    addedValue = state.addedEnergy,
                                    previousValueUnit = "Wh"
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Latest Notification",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(onClick = {
                                        Navigation.findNavController(view).navigate(
                                            HomeFragmentDirections.actionHomeFragmentToNotificationsFragment()
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (state.anomalies.isNullOrEmpty()) {
                                        item {
                                            Text(
                                                text = "No notifications available",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        items(state.anomalies, key = { it.id }) { anomaly ->
                                            val timestamp = anomaly.readingTime.toTimestamp()?.toDateString() ?: "Unknown Time"
                                            println("Anomaly ID: ${anomaly.id}, Reading Time: ${anomaly.readingTime}, Converted: $timestamp")

                                            NotificationItem(
                                                timestamp = timestamp,
                                                onClick = {
                                                    Navigation.findNavController(view).navigate(
                                                        HomeFragmentDirections.actionHomeFragmentToAnomalyDetailFragment(anomaly.id)
                                                    )
                                                }
                                            )
                                            Spacer(modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
