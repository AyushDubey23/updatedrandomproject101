package com.messfit.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.messfit.ai.navigation.NavRoutes
import com.messfit.ai.ui.screens.*
import com.messfit.ai.ui.theme.MessFitTheme
import com.messfit.ai.ui.theme.NeonGreen
import com.messfit.ai.ui.theme.TextSecondary
import com.messfit.ai.ui.viewmodel.MessFitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessFitTheme {
                MessFitApp()
            }
        }
    }
}

private data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun MessFitApp(viewModel: MessFitViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mainTabs = listOf(
        BottomNavItem(NavRoutes.DASHBOARD, "Home", Icons.Default.Home),
        BottomNavItem(NavRoutes.MENU_SCAN, "Menu", Icons.Default.Restaurant),
        BottomNavItem(NavRoutes.DIET_PLAN, "Diet", Icons.Default.RestaurantMenu),
        BottomNavItem(NavRoutes.WORKOUT, "Gym", Icons.Default.FitnessCenter),
        BottomNavItem(NavRoutes.PROFILE, "Profile", Icons.Default.Person)
    )

    val showBottomBar = currentRoute in mainTabs.map { it.route }

    Scaffold(
        containerColor = com.messfit.ai.ui.theme.Black,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = com.messfit.ai.ui.theme.DarkGrey) {
                    mainTabs.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonGreen,
                                selectedTextColor = NeonGreen,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = NeonGreen.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.SPLASH) {
                SplashScreen {
                    val dest = if (uiState.profile?.onboardingComplete == true) NavRoutes.DASHBOARD else NavRoutes.LOGIN
                    navController.navigate(dest) { popUpTo(NavRoutes.SPLASH) { inclusive = true } }
                }
            }
            composable(NavRoutes.LOGIN) {
                LoginScreen(
                    onLogin = {
                        val dest = if (uiState.profile?.onboardingComplete == true) NavRoutes.DASHBOARD else NavRoutes.ONBOARDING
                        navController.navigate(dest) { popUpTo(NavRoutes.LOGIN) { inclusive = true } }
                    },
                    onSignUp = {
                        navController.navigate(NavRoutes.ONBOARDING) { popUpTo(NavRoutes.LOGIN) { inclusive = true } }
                    }
                )
            }
            composable(NavRoutes.ONBOARDING) {
                OnboardingScreen { profile ->
                    viewModel.saveProfile(profile)
                    navController.navigate(NavRoutes.DASHBOARD) { popUpTo(NavRoutes.ONBOARDING) { inclusive = true } }
                }
            }
            composable(NavRoutes.DASHBOARD) {
                DashboardScreen(
                    state = uiState,
                    onNavigate = { navController.navigate(it) },
                    onCompleteDiet = { viewModel.completeTodayDiet() }
                )
            }
            composable(NavRoutes.MENU_SCAN) {
                MenuScanScreen(
                    state = uiState,
                    onBack = { navController.popBackStack() },
                    onScanText = { viewModel.scanMenuFromText(it) },
                    onScanImage = { viewModel.scanMenuFromImage(it) }
                )
            }
            composable(NavRoutes.DIET_PLAN) {
                DietPlanScreen(state = uiState, onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.WORKOUT) {
                WorkoutScreen(
                    state = uiState,
                    onBack = { navController.popBackStack() },
                    onSplitChange = { viewModel.setWorkoutSplit(it) }
                )
            }
            composable(NavRoutes.SHOPPING) {
                ShoppingScreen(state = uiState, onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.PROFILE) {
                ProfileScreen(state = uiState, onNavigate = { navController.navigate(it) })
            }
            composable(NavRoutes.ABOUT) {
                AboutScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.SETTINGS) {
                SettingsScreen(
                    state = uiState,
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.navigate(it) }
                )
            }
            composable(NavRoutes.BODY_ANALYSIS) {
                BodyAnalysisScreen(state = uiState, onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.GOAL_TIMELINE) {
                GoalTimelineScreen(state = uiState, onBack = { navController.popBackStack() })
            }
        }
    }
}
