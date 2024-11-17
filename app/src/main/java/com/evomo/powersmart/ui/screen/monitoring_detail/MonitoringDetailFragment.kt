package com.evomo.powersmart.ui.screen.monitoring_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.evomo.powersmart.databinding.FragmentMonitoringDetailBinding
import com.evomo.powersmart.ui.screen.monitoring_detail.components.FilterDateChip
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import com.evomo.powersmart.ui.theme.spacing
import com.evomo.powersmart.ui.utils.toDateString
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MonitoringDetailFragment : Fragment() {

    private var _binding: FragmentMonitoringDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MonitoringDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMonitoringDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvMonitoringDetail.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        val usageData by viewModel.filteredUsageData.collectAsState()
                        val startDate by viewModel.startDate.collectAsState()
                        val endDate by viewModel.endDate.collectAsState()

                        // Define a key to store mapping between x-values and LocalDate values
                        val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()

                        // Create data mapping for chart
                        val data = usageData.associate {
                            val date = Instant.ofEpochMilli(it.timestampMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            date to it.usage.toFloat()
                        }

                        // Map x-values (floats) to LocalDates for proportional date spacing
                        val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }

                        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                        val chart = rememberCartesianChart(
                            rememberLineCartesianLayer(),
                            startAxis = VerticalAxis.rememberStart(),
                            bottomAxis = HorizontalAxis.rememberBottom(
//                                valueFormatter = { context, x, _ ->
//                                    val dateMap = context.model.extraStore[xToDateMapKey]
//                                    val date =
//                                        dateMap[x.toFloat()] ?: LocalDate.ofEpochDay(x.toLong())
//                                    date.format(dateTimeFormatter)
//                                }
                            ),
                            marker = rememberDefaultCartesianMarker(
                                label = TextComponent(),
                            ),
                        )
                        val modelProducer = remember { CartesianChartModelProducer() }

                        // Set up the chart model
                        LaunchedEffect(key1 = usageData) {
                            modelProducer.runTransaction {
                                lineSeries {
                                    series(xToDates.keys, data.values)
                                }
                                extras { it[xToDateMapKey] = xToDates }
                            }
                        }

                        val scrollState = rememberVicoScrollState()
                        val zoomState = rememberVicoZoomState()

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                TopAppBar(
                                    colors = commonTopAppBarColor(),
                                    title = {
                                        Text(text = "Monitoring Detail")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            Navigation.findNavController(binding.root)
                                                .popBackStack()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBackIosNew,
                                                contentDescription = null
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
                                    .padding(
                                        MaterialTheme.spacing.medium
                                    ),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                            ) {
                                FilterDateChip(
                                    startDateValue = if (startDate == 0L) null else startDate.toDateString(),
                                    endDateValue = if (endDate == 0L) null else endDate.toDateString(),
                                    onStartDateValueChange = viewModel::updateStartDate,
                                    onEndDateValueChange = viewModel::updateEndDate,
                                )
                                OutlinedCard {
                                    CartesianChartHost(
                                        chart = chart,
                                        modelProducer = modelProducer,
                                        scrollState = scrollState,
                                        zoomState = zoomState,
                                    )
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