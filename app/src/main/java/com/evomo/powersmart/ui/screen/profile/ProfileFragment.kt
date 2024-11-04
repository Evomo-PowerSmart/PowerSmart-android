package com.evomo.powersmart.ui.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.evomo.powersmart.databinding.FragmentProfileBinding
import com.evomo.powersmart.ui.components.FullscreenLoading
import com.evomo.powersmart.ui.screen.profile.components.ProfileTop
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.commonTopAppBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvProfile.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                PowerSmartTheme {
                    Surface {
                        val state by viewModel.state.collectAsState()

                        val snackbarHostState = remember { SnackbarHostState() }

                        LaunchedEffect(key1 = Unit) {
                            viewModel.errorMessage.collectLatest {
                                snackbarHostState.showSnackbar(message = it)
                            }
                        }

                        LaunchedEffect(key1 = state.isLogoutSuccess) {
                            if (state.isLogoutSuccess) {
                                Navigation.findNavController(binding.root).navigate(
                                    ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                                )
                            }
                        }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                            topBar = {
                                TopAppBar(
                                    colors = commonTopAppBarColor(),
                                    title = {
                                        Text(text = "Profile")
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
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                ProfileTop(
                                    userData = state.userData,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                OutlinedButton(
                                    modifier = Modifier.padding(16.dp),
                                    onClick = viewModel::onSignOut,
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                ) {
                                    Text(text = "Logout")
                                }
                            }
                        }
                        if (state.isLoading) {
                            FullscreenLoading()
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