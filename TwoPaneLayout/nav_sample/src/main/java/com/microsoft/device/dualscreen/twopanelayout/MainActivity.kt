package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.composable
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.blue

val pink = Color(0xFFF17C98)
val lightGreen = Color(0xFF63D6B2)
val mediumGreen = Color(0xFF269573)
val darkGreen = Color(0xFF286753)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwoPaneLayoutTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {
    val navController = rememberNavController()

    TwoPaneLayoutNav(
        navController = navController,
        singlePaneStartDestination = "list",
        pane1StartDestination = "list",
        pane2StartDestination = "profile?name={name}",
    ) {
        composable("list") {
            ListScreen(
                navToDetail = { id, name -> navController.navigateTo("detail/$id/$name", Screen.Pane2) },
                navToProfile = { navController.navigateTo("profile", Screen.Pane2) }
            )
        }
        composable(
            route = "detail/{id}/{name}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            val name = backStackEntry.arguments?.getString("name")
            DetailScreen(id, name) { navController.navigateTo("profile?name=$it", Screen.Pane2) }
        }
        composable(
            route = "profile?name={name}",
            arguments = listOf(navArgument("name") { defaultValue = "guest" })
        ) {
            val name = it.arguments?.getString("name")
            ProfileScreen(name)
        }
    }
}

@Composable
fun ListScreen(navToDetail: (Int?, String?) -> Unit, navToProfile: () -> Unit) {
    var id: Int? by remember { mutableStateOf(null) }
    var name: String? by remember { mutableStateOf(null) }

    val textFieldColors = TextFieldDefaults.textFieldColors(
        cursorColor = pink,
        focusedIndicatorColor = pink,
        focusedLabelColor = pink
    )

    Screen(color = lightGreen) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "List Screen",
            style = MaterialTheme.typography.h4
        )
        TextField(
            modifier = Modifier.padding(vertical = 7.dp),
            value = id?.toString() ?: "",
            onValueChange = { id = it.toIntOrNull() },
            label = { Text("Id") },
            colors = textFieldColors,
        )
        TextField(
            value = name ?: "",
            onValueChange = { name = it },
            label = { Text("Name") },
            colors = textFieldColors
        )
        AppButton(text = "Go to detail", action = { navToDetail(id, name) })
        AppButton(text = "Go to profile", action = navToProfile)
    }
}

@Composable
fun DetailScreen(id: Int?, name: String?, navToProfile: (String?) -> Unit) {
    Screen(color = mediumGreen) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "Detail Screen",
            style = MaterialTheme.typography.h4
        )
        Text(
            modifier = Modifier.padding(vertical = 7.dp),
            text = "Id: $id",
            style = MaterialTheme.typography.body1
        )
        Text(text = "Name: $name", style = MaterialTheme.typography.body1)
        AppButton(text = "Go to profile", action = { navToProfile(name) })
    }
}

@Composable
fun ProfileScreen(name: String?) {
    Screen(color = darkGreen) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "Profile Screen",
            color = Color.White,
            style = MaterialTheme.typography.h4
        )
        Text(
            modifier = Modifier.padding(vertical = 7.dp),
            text = "Profile for: $name",
            color = Color.White,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun AppButton(text: String, action: () -> Unit) {
    Button(
        modifier = Modifier.padding(top = 20.dp),
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            backgroundColor = pink,
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.h5)
    }
}

@Composable
fun Screen(color: Color, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(30.dp),
    ) {
        content()
    }
}

@Composable
fun TopAppBar(paneAnnotation: String) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name) + paneAnnotation, color = Color.White) },
        backgroundColor = blue
    )
}
