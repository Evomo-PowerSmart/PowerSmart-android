package com.evomo.powersmart.ui.screen.register

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
import androidx.compose.material.icons.filled.Person
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
import com.evomo.powersmart.databinding.FragmentRegisterBinding
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
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvRegister.apply {
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
                                    RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                                )
                            }
                        }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                        ) { innerPadding ->
                            RegisterContent(
                                modifier = Modifier.padding(innerPadding),
                                fullNameValue = state.fullName,
                                onFullNameChange = viewModel::onFullNameChanged,
                                emailValue = state.email,
                                onEmailChange = viewModel::onEmailChanged,
                                passwordValue = state.password,
                                onPasswordChange = viewModel::onPasswordChanged,
                                onRegisterClick = viewModel::onRegisterClicked,
                                onContinueWithGoogleClick = {
                                    lifecycleScope.launch {
                                        getGoogleIdToken(requireContext())?.let {
                                            viewModel.onContinueWithGoogle(it)
                                        }
                                    }
                                },
                                onGoToLoginScreenClick = {
                                    Navigation.findNavController(view).navigate(
                                        RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
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
fun RegisterContent(
    modifier: Modifier = Modifier,
    fullNameValue: String,
    onFullNameChange: (String) -> Unit,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onContinueWithGoogleClick: () -> Unit,
    onGoToLoginScreenClick: () -> Unit,
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
                text = "Create an Account",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        CommonTextField(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Person,
            placeHolder = "Full Name",
            value = fullNameValue,
            singleLine = true,
            onValueChanged = onFullNameChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
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
            text = "Register",
            onClick = onRegisterClick
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
                text = "Already have an account?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    onGoToLoginScreenClick()
                },
                text = " Login",
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
private fun RegisterContentPreview() {
    PowerSmartTheme {
        Surface {
            RegisterContent(
                fullNameValue = "",
                onFullNameChange = {},
                emailValue = "",
                onEmailChange = {},
                passwordValue = "",
                onPasswordChange = {},
                onRegisterClick = {},
                onContinueWithGoogleClick = {},
                onGoToLoginScreenClick = {}
            )
        }
    }
}