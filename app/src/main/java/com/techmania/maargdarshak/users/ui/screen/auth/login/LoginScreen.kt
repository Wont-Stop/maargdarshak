package com.techmania.maargdarshak.users.ui.screen.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techmania.maargdarshak.R // IMPORTANT: Add your social media icons to res/drawable
import com.techmania.maargdarshak.users.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                // UPDATED: Listen for the correct event
                is LoginViewModel.NavigationEvent.NavigateToPermission -> {
                    // UPDATED: Navigate to the permission screen
                    navController.navigate(Screen.Permission.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                is LoginViewModel.NavigationEvent.NavigateToSignup -> {
                    navController.navigate(Screen.Signup.route)
                }
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignInClicked = viewModel::onSignInClicked,
        onForgotPasswordClicked = viewModel::onForgotPasswordClicked,
        onGoogleSignInClicked = viewModel::onGoogleSignInClicked,
        onAppleSignInClicked = viewModel::onAppleSignInClicked,
        onFacebookSignInClicked = viewModel::onFacebookSignInClicked,
        onCreateAccountClicked = viewModel::onCreateAccountClicked
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignInClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onGoogleSignInClicked: () -> Unit,
    onAppleSignInClicked: () -> Unit,
    onFacebookSignInClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
) {
    val purpleColor = Color(0xFF6A00FF)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = purpleColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access your trips and transport info in seconds.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.generalError != null) {
                Text(
                    text = uiState.generalError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Email Field
            TextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Email") },
                isError = uiState.emailError != null,
                supportingText = { if (uiState.emailError != null) Text(uiState.emailError) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            TextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter password") },
                isError = uiState.passwordError != null,
                supportingText = { if (uiState.passwordError != null) Text(uiState.passwordError) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.rememberMe,
                        onCheckedChange = onRememberMeChange
                    )
                    Text("Remember Me", style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = onForgotPasswordClicked) {
                    Text("Forgot password?", color = purpleColor, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignInClicked,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Sign In", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("Or", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialLoginButton(iconRes = R.drawable.googlelogo, onClick = onGoogleSignInClicked)
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginButton(iconRes = R.drawable.applelogo, onClick = onAppleSignInClicked)
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginButton(iconRes = R.drawable.facebook, onClick = onFacebookSignInClicked)
            }

            Spacer(modifier = Modifier.weight(1f))

            CreateAccountText(onCreateAccountClicked)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SocialLoginButton(iconRes: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Social Login",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CreateAccountText(onCreateAccount: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("Don't have an account? ")
        pushStringAnnotation(tag = "CREATE", annotation = "create")
        withStyle(style = SpanStyle(color = Color(0xFF6A00FF), fontWeight = FontWeight.Bold)) {
            append("Create Account")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "CREATE", start = offset, end = offset)
                .firstOrNull()?.let {
                    onCreateAccount()
                }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        uiState = LoginUiState(emailError = "Invalid email format"),
        onEmailChange = {},
        onPasswordChange = {},
        onRememberMeChange = {},
        onTogglePasswordVisibility = {},
        onSignInClicked = {},
        onForgotPasswordClicked = {},
        onGoogleSignInClicked = {},
        onAppleSignInClicked = {},
        onFacebookSignInClicked = {},
        onCreateAccountClicked = {}
    )
}