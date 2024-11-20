package com.evomo.powersmart.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

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

        binding.cvHome.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        val state by viewModel.state.collectAsState()
//                        val activeEnergyImport by viewModel.activeEnergyImport.collectAsState()
//                        val status by viewModel.status.collectAsState()
//                        val energyIn by viewModel.energyIn.collectAsState()
//                        val lastUpdateTime by viewModel.lastUpdateTime.collectAsState()
//                        val addedEnergy by viewModel.addedEnergy.collectAsState()

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
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .weight(1f),
//                                    horizontalAlignment = Alignment.CenterHorizontally,
//                                    verticalArrangement = Arrangement.Center
//                                ) {
//                                    Icon(
//                                        modifier = Modifier
//                                            .size(72.dp)
//                                            .alpha(0.7f),
//                                        imageVector = Icons.Outlined.WorkHistory,
//                                        contentDescription = null
//                                    )
//                                    Spacer(modifier = Modifier.size(8.dp))
//                                    Text(
//                                        modifier = Modifier.alpha(0.7f),
//                                        text = "No new notification!",
//                                        fontSize = 16.sp,
//                                        textAlign = TextAlign.Center
//                                    )
//                                }

                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    items(count = 3, key = { UUID.randomUUID() }) {
                                        NotificationItem(
                                            timestamp = Timestamp.now(),
                                            onClick = {
                                                Navigation.findNavController(view).navigate(
                                                    HomeFragmentDirections.actionHomeFragmentToAnomalyDetailFragment()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}