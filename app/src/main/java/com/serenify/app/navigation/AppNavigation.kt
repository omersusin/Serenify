package com.serenify.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.serenify.app.ui.theme.*
import com.serenify.app.ui.screens.HomeScreen
import com.serenify.app.ui.screens.TimerScreen
import com.serenify.app.ui.screens.HabitsScreen
import com.serenify.app.ui.screens.ProfileScreen

sealed class Screen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Timer : Screen("timer", "Focus", Icons.Filled.Timer, Icons.Outlined.Timer)
    data object Habits : Screen("habits", "Habits", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

val screens = listOf(Screen.Home, Screen.Timer, Screen.Habits, Screen.Profile)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, MidnightBlue, DarkNavy)
                )
            )
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { 30 }
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Timer.route) { TimerScreen() }
            composable(Screen.Habits.route) { HabitsScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }

        BottomNavBar(
            currentRoute = currentRoute,
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            GlassWhite.copy(alpha = 0.12f),
                            GlassWhite.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.label,
                                modifier = Modifier.size(24.dp)
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(VividPurple)
                                )
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VividPurple,
                        unselectedIconColor = TextMuted,
                        indicatorColor = VividPurple.copy(alpha = 0.15f)
                    )
                )
            }
        }
    }
}
