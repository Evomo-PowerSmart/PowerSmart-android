package com.evomo.powersmart.ui.screen.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.evomo.powersmart.databinding.FragmentHomeBinding
import com.evomo.powersmart.ui.components.LargeDropdownMenu
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
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
            } else {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission denied!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

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
                                    title = {
                                        Text(text = "Power Smart")
                                    },
                                    actions = {
                                        Row(
                                            modifier = Modifier.padding(end = MaterialTheme.spacing.extraMedium),
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
                                    },
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(MaterialTheme.spacing.medium),
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
                                }


                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                LargeDropdownMenu(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = "Location",
                                    items = Location.entries.map { it.display },
                                    selectedIndex = state.selectedLocationIndex,
                                    onItemSelected = { index, _ ->
                                        viewModel.updateSelectedLocationAndIndex(index)
                                    },
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                RealtimeBox(
                                    isLoading = state.isRealtimeDataLoading,
                                    location = state.selectedLocation.display,
                                    lastUpdated = if (state.lastUpdateTime != null) "${state.lastUpdateTime} WIB" else "No data available yet",
                                    unitDetail = "Daya Per Hour",
                                    value = state.activeEnergyImport / 1000.0,
                                    unit = "kWh",
                                    status = state.status,
                                    energyIn = state.energyIn,
                                    addedValue = state.addedEnergy,
                                    previousValueUnit = "Wh",
                                    onClick = {
                                        val toMonitoringDetailFragment =
                                            HomeFragmentDirections.actionHomeFragmentToMonitoringDetailFragment()
                                        toMonitoringDetailFragment.location = state.selectedLocation
                                        findNavController().navigate(toMonitoringDetailFragment)
                                    }
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Latest Notification",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
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
                                    if (state.anomalies.isEmpty()) {
                                        item {
                                            Text(
                                                text = "No notifications available",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        items(count = 5, key = { it }) { index ->
                                            val anomaly = state.anomalies[index]
                                            val timestamp =
                                                anomaly.readingTime.toTimestamp()?.toDateString()
                                                    ?: "Unknown Time"
                                            Timber.d("Anomaly ID: ${anomaly.id}, Reading Time: ${anomaly.readingTime}, Converted: $timestamp")

                                            NotificationItem(
                                                timestamp = timestamp,
                                                location = Location.entries.find { it.location == anomaly.position }!!,
                                                onClick = {
                                                    Navigation.findNavController(view).navigate(
                                                        HomeFragmentDirections.actionHomeFragmentToAnomalyDetailFragment(
                                                            anomaly.id
                                                        )
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