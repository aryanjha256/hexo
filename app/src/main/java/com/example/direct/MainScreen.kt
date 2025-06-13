
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.direct.Screen

@Composable
fun MainScreen() {

    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Insights,
        Screen.Settings
    )
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar  {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination == screen.route,
                        onClick = {
                            if (currentDestination != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost (
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Insights.route) { InsightsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
fun DashboardScreen() {
    Text("📊 Dashboard Screen")
}

@Composable
fun InsightsScreen() {
    Text("📈 Insights Screen")
}

@Composable
fun SettingsScreen() {
    Text("⚙️ Settings Screen")
}

