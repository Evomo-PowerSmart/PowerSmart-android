package com.evomo.powersmart.ui.screen.anomaly_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.evomo.powersmart.databinding.FragmentAnomalyDetailBinding
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import com.evomo.powersmart.ui.utils.toDateString
import com.evomo.powersmart.ui.utils.toTimestamp
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnomalyDetailFragment : Fragment() {

    private var _binding: FragmentAnomalyDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnomalyDetailViewModel by viewModels()

    private val args: AnomalyDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnomalyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                                Text(text = "Anomaly Detail")
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

        val anomalyId = args.anomalyId

        if (anomalyId > 0) {
            Timber.d("Fetching anomaly detail with ID: $anomalyId")
            viewModel.fetchAnomalyDetail(anomalyId)
        } else {
            Timber.e("Invalid anomaly ID: $anomalyId")
            Toast.makeText(requireContext(), "Invalid anomaly ID", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.anomalyDetail.collect { anomalyDetail ->
                anomalyDetail?.firstOrNull()?.let { item ->
                    binding.apply {
                        tvReadingTime.text = item.readingTime?.toTimestamp()?.toDateString("dd MMM yyyy, HH:mm:ss") ?: "N/A"
                        tvMeterSerialNumber.text = item.meterSerialNumber.toString()
                        tvMeterType.text = item.meterType
                        tvDataType.text = item.anomalyType
                        tvActiveEnergyImport.text = item.activeEnergyImport.toString()
                        tvActiveEnergyExport.text = item.activeEnergyExport.toString()
                        tvReactiveEnergyImport.text = item.reactiveEnergyImport.toString()
                        tvReactiveEnergyExport.text = item.reactiveEnergyExport.toString()
                        tvApparentEnergyImport.text = item.apparentEnergyImport.toString()
                        tvApparentEnergyExport.text = item.apparentEnergyExport.toString()
                        tvPredictedEnergy.text = item.predictedEnergy.toString()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loading.collect { isLoading ->
                if (isLoading) {
                    Toast.makeText(requireContext(), "Loading anomaly details...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
