package com.evomo.powersmart.ui.screen.anomaly_detail

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
import androidx.navigation.Navigation
import com.evomo.powersmart.databinding.FragmentAnomalyDetailBinding
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnomalyDetailFragment : Fragment() {

    private var _binding: FragmentAnomalyDetailBinding? = null
    private val binding get() = _binding!!

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

        // TODO: Implement the rest of the fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}