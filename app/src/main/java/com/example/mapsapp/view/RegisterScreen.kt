package com.example.mapsapp.view

import PasswordVisibility
import UpperText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mapsapp.Routes
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Gunmetal
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.RichBlack
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.coolveticaRgIt
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.example.mapsapp.viewmodel.lemonMilkRegularItalic

fun RegisterScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@Composable
fun ScaffoldRegisterScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {
    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .background(RichBlack)

        ) {
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var showPassword by rememberSaveable { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    UpperText("Sign up", "Please sing up to continue")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Email",
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        style = TextStyle(
                            fontFamily = lemonMilkMediumItalic
                        ),
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        textStyle = TextStyle(fontFamily = lemonMilkRegularItalic),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Jasmine,
                            focusedBorderColor = CoolGray2,
                            unfocusedBorderColor = CoolGray2,
                            cursorColor = Jasmine
                        )
                    )
                    Text(
                        text = "Password",
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 10.dp),
                        style = TextStyle(
                            fontFamily = lemonMilkMediumItalic
                        ),
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        textStyle = TextStyle(fontFamily = lemonMilkRegularItalic),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Jasmine,
                            focusedBorderColor = CoolGray2,
                            unfocusedBorderColor = CoolGray2,
                            cursorColor = Jasmine
                        ),
                        trailingIcon = {
                            PasswordVisibility(
                                showPassword = showPassword,
                                onToggleClick = {
                                    showPassword = !showPassword
                                }
                            )
                        }
                    )
                    Button(
                        onClick = {
                            myViewModel.register(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Gunmetal
                        )
                    ) {
                        Text(
                            text = "Sign Up",
                            style = TextStyle(fontFamily = lemonMilkMediumItalic)
                        )
                    }
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontFamily = coolveticaRgIt)) {
                                append("Have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Jasmine,
                                    fontFamily = coolveticaRgIt,
                                    fontWeight = FontWeight.Black
                                )
                            ) {
                                append("Log In")
                            }
                        },
                        modifier = Modifier
                            .clickable { navigationController.navigate(Routes.LoginScreen.routes) }
                    )
                }
            }
        }
    }
}