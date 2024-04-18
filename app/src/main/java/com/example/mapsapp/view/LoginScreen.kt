import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mapsapp.Routes
import com.example.mapsapp.model.UserPrefs
import com.example.mapsapp.ui.theme.CoolGray2
import com.example.mapsapp.ui.theme.Gunmetal
import com.example.mapsapp.ui.theme.Jasmine
import com.example.mapsapp.ui.theme.RichBlack
import com.example.mapsapp.viewmodel.MapsViewModel
import com.example.mapsapp.viewmodel.coolveticaRg
import com.example.mapsapp.viewmodel.coolveticaRgIt
import com.example.mapsapp.viewmodel.lemonMilkMediumItalic
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
            var showPassword by remember { mutableStateOf(false) }
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
                    .padding(horizontal = 30.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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
                        textStyle = TextStyle(fontFamily = coolveticaRg),
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
                        textStyle = TextStyle(fontFamily = coolveticaRg),
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
                        },
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Checkbox(
                            checked = rememberPassword,
                            onCheckedChange = { rememberPassword = it },
                        )
                        Text(
                            text = "Remember me?",
                            modifier = Modifier
                        )
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
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Gunmetal
                        )
                    ) {
                        Text(
                            text = "Log In",
                            style = TextStyle(fontFamily = lemonMilkMediumItalic)
                        )
                    }
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontFamily = coolveticaRgIt)) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Jasmine,
                                    fontFamily = coolveticaRgIt,
                                    fontWeight = FontWeight.Black,
                                )
                            ) {
                                append("Sign Up")
                            }
                        },
                        modifier = Modifier
                            .clickable { navigationController.navigate(Routes.RegisterScreen.routes) },
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

@Composable
private fun PasswordVisibility(
    showPassword: Boolean,
    onToggleClick: () -> Unit
) {
    val icon: ImageVector =
        if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
    val tint: androidx.compose.ui.graphics.Color = if (showPassword) CoolGray2 else CoolGray2

    IconButton(
        onClick = onToggleClick,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = "Toggle password visibility", tint = tint)
    }
}