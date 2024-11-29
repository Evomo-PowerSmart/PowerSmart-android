package com.evomo.powersmart.ui.screen.monitoring_detail

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.Navigation
import com.evomo.powersmart.R
import com.evomo.powersmart.databinding.FragmentMonitoringDetailBinding
import com.evomo.powersmart.ui.screen.monitoring_detail.components.FilterDateChip
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import com.evomo.powersmart.ui.theme.spacing
import com.evomo.powersmart.ui.utils.formatToTwoDecimal
import com.evomo.powersmart.ui.utils.getStringDateFromEpochMilli
import com.evomo.powersmart.ui.utils.getTimeStringFromTimeInMillis
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.ZoneOffset
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

        val location = MonitoringDetailFragmentArgs.fromBundle(requireArguments()).location

        if (savedInstanceState == null) {
            viewModel.fetchHistoricalData(
                location = location.location,
                startDate = viewModel.state.value.startDate,
                endDate = viewModel.state.value.endDate,
                startTime = viewModel.state.value.startTime,
                endTime = viewModel.state.value.endTime
            )
        }

        viewModel.usageData.observe(viewLifecycleOwner) { usages ->
            binding.lineChart.clear()
            updateLineChart(binding.lineChart, usages)
        }

        binding.cvMonitoringDetail.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TopAppBar(
                                colors = commonTopAppBarColor(),
                                title = {
                                    Column {
                                        Text(text = "Monitoring Detail")
                                        Text(
                                            modifier = Modifier.alpha(0.75f),
                                            text = location.display,
                                            style = MaterialTheme.typography.titleSmall,
                                        )
                                    }
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

                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                            FilterDateChip(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                                startDateValue = getStringDateFromEpochMilli(state.startDate),
                                endDateValue = getStringDateFromEpochMilli(state.endDate),
                                startTimeValue = getTimeStringFromTimeInMillis(state.startTime),
                                endTimeValue = getTimeStringFromTimeInMillis(state.endTime),
                                onStartDateValueChange = viewModel::updateStartDate,
                                onEndDateValueChange = viewModel::updateEndDate,
                                onStartTimeValueChange = viewModel::updateStartTime,
                                onEndTimeValueChange = viewModel::updateEndTime,
                                onApplyClick = {
                                    viewModel.fetchHistoricalData(
                                        location = location.location,
                                        startDate = state.startDate,
                                        endDate = state.endDate,
                                        startTime = state.startTime,
                                        endTime = state.endTime
                                    )
                                }
                            )
                        }

                        LaunchedEffect(key1 = true) {
                            viewModel.errorMessage.collectLatest { message ->
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    private fun updateLineChart(lineChart: LineChart, usageDatas: List<UsageData>) {
        val entries = usageDatas.map { data ->
            Entry(
                data.readingTime.toEpochSecond(ZoneOffset.UTC).toFloat(),
                data.activeEnergyImport.toFloat()
            )
        }

        val primaryColor = MaterialColors.getColor(
            requireContext(),
            com.google.android.material.R.attr.colorPrimary,
            Color.BLACK
        )
        val onSurfaceColor = MaterialColors.getColor(
            requireContext(),
            com.google.android.material.R.attr.colorOnSurface,
            Color.BLACK
        )

        val dataSet = LineDataSet(entries, "Active Energy Import").apply {
            color = primaryColor
            valueTextColor = onSurfaceColor
            lineWidth = 2f
            setDrawCircles(true)
            setDrawCircleHole(false)
            setCircleColor(primaryColor)
            setDrawValues(false)
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }

        val lineData = LineData(dataSet)

        lineChart.apply {
            data = lineData
            description.text = "Energy Usage Over Time"
            description.textColor = onSurfaceColor

            axisRight.isEnabled = false

            axisLeft.apply {
                setDrawGridLines(true)
                textColor = onSurfaceColor
            }

            legend.apply {
                isEnabled = true
                textSize = 12f
                textColor = onSurfaceColor
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            setXAxisRenderer(
                CustomXAxisRenderer(
                    viewPortHandler,
                    xAxis,
                    getTransformer(YAxis.AxisDependency.LEFT)
                )
            )

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setLabelCount(5, false)
                valueFormatter = DateValueFormatter()
                textColor = onSurfaceColor
            }

            setExtraOffsets(0f, 20f, 0f, 20f)

            marker = object : MarkerView(context, R.layout.marker_custom) {
                private val tvContent: TextView = findViewById(R.id.title_tv)
                private val tvTime: TextView = findViewById(R.id.time_tv)

                @SuppressLint("SetTextI18n")
                override fun refreshContent(entry: Entry?, highlight: Highlight?) {
                    entry?.let {
                        tvContent.text = "${(entry.y / 1000.0).formatToTwoDecimal()} kWh"

                        val localDateTime = Instant.ofEpochSecond(entry.x.toLong())
                            .atZone(ZoneOffset.UTC)
                            .toLocalDateTime()

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                        tvTime.text = localDateTime.format(formatter)
                    }
                    super.refreshContent(entry, highlight)
                }

                private val arrowSize = 35
                private val arrowCircleOffset = 0f

                override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
                    val offset = offset
                    val width = width.toFloat()
                    val height = height.toFloat()

                    if (posY <= height + arrowSize) {
                        offset.y = arrowSize.toFloat()
                    } else {
                        offset.y = -height - arrowSize
                    }

                    if (posX > this@apply.width - width) {
                        offset.x = -width
                    } else {
                        offset.x = 0f
                        if (posX > width / 2) {
                            offset.x = -(width / 2)
                        }
                    }

                    return offset
                }

                override fun draw(canvas: Canvas, posX: Float, posY: Float) {
                    val paint = Paint().apply {
                        style = Paint.Style.FILL
                        strokeJoin = Paint.Join.ROUND
                        color = ContextCompat.getColor(context, R.color.md_theme_secondaryContainer)
                    }

                    val width = width.toFloat()
                    val height = height.toFloat()
                    val offset = getOffsetForDrawingAtPoint(posX, posY)
                    val saveId: Int = canvas.save()
                    val path = Path()

                    if (posY < height + arrowSize) {
                        if (posX > this@apply.width - width) {
                            path.moveTo(width - (2 * arrowSize), 2f)
                            path.lineTo(width, -arrowSize + arrowCircleOffset)
                            path.lineTo(width - arrowSize, 2f)
                        } else {
                            if (posX > width / 2) {
                                path.moveTo(width / 2 - arrowSize / 2, 2f)
                                path.lineTo(width / 2, -arrowSize + arrowCircleOffset)
                                path.lineTo(width / 2 + arrowSize / 2, 2f)
                            } else {
                                path.moveTo(0f, -arrowSize + arrowCircleOffset)
                                path.lineTo(0f + arrowSize, 2f)
                                path.lineTo(0f, 2f)
                                path.lineTo(0f, -arrowSize + arrowCircleOffset)
                            }
                        }
                        path.offset(posX + offset.x, posY + offset.y)
                    } else {
                        if (posX > this@apply.width - width) {
                            path.moveTo(width, (height - 2) + arrowSize - arrowCircleOffset)
                            path.lineTo(width - arrowSize, height - 2)
                            path.lineTo(width - (2 * arrowSize), height - 2)
                        } else {
                            if (posX > width / 2) {
                                path.moveTo(width / 2 + arrowSize / 2, height - 2)
                                path.lineTo(width / 2, (height - 2) + arrowSize - arrowCircleOffset)
                                path.lineTo(width / 2 - arrowSize / 2, height - 2)
                                path.lineTo(0f, height - 2)
                            } else {
                                path.moveTo(0f + (arrowSize * 2), height - 2)
                                path.lineTo(0f + arrowSize, (height - 2) + arrowSize - arrowCircleOffset)
                                path.lineTo(0f + arrowSize, height - 2)
                            }
                        }
                        path.offset(posX + offset.x, posY + offset.y)
                    }

                    canvas.drawPath(path, paint)
                    canvas.translate(posX + offset.x, posY + offset.y)
                    draw(canvas)
                    canvas.restoreToCount(saveId)
                }
            }

            invalidate()
        }
    }
}