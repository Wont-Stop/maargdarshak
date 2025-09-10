package com.techmania.maargdarshak.users.ui.screen.auth.signup

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
import com.techmania.maargdarshak.R
import com.techmania.maargdarshak.users.navigation.Screen

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                SignupViewModel.NavigationEvent.NavigateToPermission -> {
                    // UPDATED: Navigate to the permission screen
                    navController.navigate(Screen.Permission.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                SignupViewModel.NavigationEvent.NavigateToLogin -> {
                    navController.popBackStack()
                }
            }
        }
    }


    SignupContent(
        uiState = uiState,
        onFullNameChange = viewModel::onFullNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignUpClicked = viewModel::onSignUpClicked,
        onLoginClicked = viewModel::onLoginClicked
    )
}

@Composable
private fun SignupContent(
    uiState: SignupUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginClicked: () -> Unit
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
                text = "Create an Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = purpleColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join to plan, track, and ride smarter",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            TextField(
                value = uiState.fullName,
                onValueChange = onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Full Name") },
                isError = uiState.fullNameError != null,
                supportingText = { if (uiState.fullNameError != null) Text(uiState.fullNameError) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSignUpClicked,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                    Text("Sign Up", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text(" Or ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Login Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                SocialLoginButton(iconRes = R.drawable.googlelogo, onClick = { /* TODO */ })
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginButton(iconRes = R.drawable.applelogo, onClick = { /* TODO */ })
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginButton(iconRes = R.drawable.facebook, onClick = { /* TODO */ })
            }

            Spacer(modifier = Modifier.weight(1f))

            LoginAccountText(onLoginClicked)

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
fun LoginAccountText(onLogin: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation(tag = "LOGIN", annotation = "login")
        withStyle(style = SpanStyle(color = Color(0xFF6A00FF), fontWeight = FontWeight.Bold)) {
            append("Login")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "LOGIN", start = offset, end = offset)
                .firstOrNull()?.let { onLogin() }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupContent(
        uiState = SignupUiState(passwordError = "Password is too short"),
        onFullNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onSignUpClicked = {},
        onLoginClicked = {}
    )
}