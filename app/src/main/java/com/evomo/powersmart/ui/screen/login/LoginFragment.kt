package com.evomo.powersmart.ui.screen.login

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.evomo.powersmart.R
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.databinding.FragmentLoginBinding
import com.evomo.powersmart.ui.components.CommonButton
import com.evomo.powersmart.ui.components.CommonOutlinedButton
import com.evomo.powersmart.ui.components.CommonTextField
import com.evomo.powersmart.ui.components.FullscreenLoading
import com.evomo.powersmart.ui.components.PasswordTextField
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.spacing
import com.evomo.powersmart.ui.utils.getGoogleIdToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvLogin.apply {
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

                        LaunchedEffect(key1 = state.isSuccessful) {
                            if (state.isSuccessful) {
                                Navigation.findNavController(view).navigate(
                                    LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                )
                            }
                        }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                        ) { innerPadding ->
                            LoginContent(
                                modifier = Modifier.padding(innerPadding),
                                emailValue = state.email,
                                onEmailChange = viewModel::onEmailChanged,
                                passwordValue = state.password,
                                onPasswordChange = viewModel::onPasswordChanged,
                                onLoginClick = viewModel::onLoginClicked,
                                onContinueWithGoogleClick = {
                                    lifecycleScope.launch {
                                        when (val response = getGoogleIdToken(requireContext())) {
                                            is Resource.Error -> snackbarHostState.showSnackbar(
                                                message = response.message ?: "Something went wrong!"
                                            )
                                            is Resource.Loading -> {}
                                            is Resource.Success -> response.data?.let {
                                                viewModel.onContinueWithGoogle(it)
                                            }
                                        }
                                    }
                                },
                                onGoToRegisterScreenClick = {
                                    Navigation.findNavController(view).navigate(
                                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                                    )
                                }
                            )

                            if (state.isLoading) {
                                FullscreenLoading()
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

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onContinueWithGoogleClick: () -> Unit,
    onGoToRegisterScreenClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.extraMedium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_evomo),
                contentDescription = null,
                modifier = Modifier.size(130.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            Text(
                text = "Welcome Back!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        CommonTextField(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Mail,
            placeHolder = "Email",
            value = emailValue,
            singleLine = true,
            onValueChanged = onEmailChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChanged = onPasswordChange,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Login",
            onClick = onLoginClick
        )

        Row(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.extraMedium)
                .fillMaxWidth()
                .testTag("or_divider"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "or", fontSize = 12.sp, fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
        }

        CommonOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue with Google",
            icon = R.drawable.logo_google,
            onClick = onContinueWithGoogleClick
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account yet?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    onGoToRegisterScreenClick()
                },
                text = " Register",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginContentPreview() {
    PowerSmartTheme {
        Surface {
            LoginContent(
                emailValue = "",
                onEmailChange = {},
                passwordValue = "",
                onPasswordChange = {},
                onLoginClick = {},
                onContinueWithGoogleClick = {},
                onGoToRegisterScreenClick = {}
            )
        }
    }
}