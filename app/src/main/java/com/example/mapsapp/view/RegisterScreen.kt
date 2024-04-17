package com.example.mapsapp.view

import UpperText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mapsapp.Routes
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.RichBlack
import com.example.mapsapp.viewmodel.MapsViewModel
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
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(fontFamily = lemonMilkRegularItalic),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Jasmine,
                            focusedBorderColor = CoolGray2,
                            unfocusedBorderColor = CoolGray2,
                            cursorColor = Jasmine
                        )
                    )

                    Button(
                        onClick = {
                            myViewModel.register(email, password)
                        }) {
                        Text(text = "Sign Up")
                    }

                    Text(
                        text = "You already have an account? Log in",
                        modifier = Modifier
                            .clickable { navigationController.navigate(Routes.LoginScreen.routes) }
                    )

                }
            }
        }
    }

}