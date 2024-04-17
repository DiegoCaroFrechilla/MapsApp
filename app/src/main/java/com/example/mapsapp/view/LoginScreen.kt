import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mapsapp.Routes
import com.example.mapsapp.model.UserPrefs
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.RichBlack
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.coolveticaRgIt
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
import com.example.mapsapp.viewmodel.lemonMilkRegularItalic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScaffoldLoginScreen(navigationController: NavHostController, myViewModel: MapsViewModel) {
    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .background(RichBlack)

        ) {
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            val goToNext by myViewModel.goToNext.observeAsState()
            var rememberPassword by rememberSaveable { mutableStateOf(false) }
            val context = LocalContext.current
            val userPrefs = UserPrefs(context)
            val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
            if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != "" && storedUserData.value[1] != "" && storedUserData.value[2] != "") {
                email = storedUserData.value[0]
                password = storedUserData.value[1]
                //myViewModel.modifyProcessing()
                myViewModel.login(storedUserData.value[0], storedUserData.value[1])
                if (storedUserData.value[2] == "true") {
                    myViewModel.login(storedUserData.value[0], storedUserData.value[1])
                }
                if (goToNext == true) {
                    navigationController.navigate(Routes.MapScreen.routes)
                }
            }

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
                    UpperText("LOGIN", "Please log in to continue")
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
                    Row() {
                        Checkbox(
                            checked = rememberPassword,
                            onCheckedChange = { rememberPassword = it })
                    }
                    Button(
                        {
                            myViewModel.login(email, password)
                            if (rememberPassword) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    userPrefs.saveUserData(email, password, "true")
                                }
                            } else {
                                CoroutineScope(Dispatchers.IO).launch {
                                    userPrefs.saveUserData("", "", "")
                                }
                            }
                            if (myViewModel.goToNext.value == true) {
                                navigationController.navigate(Routes.MapScreen.routes)
                            }
                        }
                    ) {
                        Text(text = "Log In")
                    }

                    Text(
                        text = "Don't have an account? Sign up",
                        modifier = Modifier
                            .clickable { navigationController.navigate(Routes.RegisterScreen.routes) }
                    )

                }
            }
        }
    }
}

@Composable
fun UpperText(title: String, subtitle: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        style = TextStyle(
            fontFamily = lemonMilkMediumItalic
        ),
    )

    Text(
        text = subtitle,
        fontSize = 14.sp,
        style = TextStyle(
            fontFamily = coolveticaRgIt
        )
    )

}

