package com.kaushalya.kaushalyakarnataka.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.ui.components.KKIconCircle
import com.kaushalya.kaushalyakarnataka.ui.components.KKToastCard
import com.kaushalya.kaushalyakarnataka.ui.screens.*
import com.kaushalya.kaushalyakarnataka.firebase.FirebaseModule

private object Routes {
    const val Splash = "splash"
    const val RoleSelect = "role_select"
    const val SignIn = "sign_in"
    const val Main = "main"
    const val WorkerProfile = "worker_profile/{workerId}"
    const val EditProfile = "edit_profile"
    const val ManageServices = "services_manage"
}

@Composable
fun KaushalyaApp(vm: AppViewModel) {
    val nav = rememberNavController()
    val state by vm.state.collectAsState()

    Box(Modifier.fillMaxSize()) {

        NavHost(navController = nav, startDestination = Routes.Splash) {

            composable(Routes.Splash) {

                SplashScreen(

                    onDone = {

                        if (FirebaseModule.auth.currentUser != null) {

                            nav.navigate(Routes.Main) {

                                popUpTo(Routes.Splash) {
                                    inclusive = true
                                }
                            }

                        } else {

                            nav.navigate(Routes.RoleSelect) {

                                popUpTo(Routes.Splash) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }

            composable(Routes.RoleSelect) {
                RoleSelectScreen(
                    onBack = { nav.popBackStack() },
                    onContinue = { role ->
                        vm.setSetupRole(role)
                        nav.navigate(Routes.SignIn)
                    }
                )
            }

            composable(Routes.SignIn) {
                SignInScreen(
                    initialRole = state.setupRole,
                    onBack = { nav.popBackStack() },
                    onSubmit = { user ->
                        vm.login(user)
                        nav.navigate(Routes.Main) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.Main) {
                MainTabsScreen(
                    vm = vm,
                    onOpenWorker = { workerId ->

                        nav.navigate(
                            "worker_profile/$workerId"
                        )
                    },
                    onEditProfile = { nav.navigate(Routes.EditProfile) },
                    onManageServices = { nav.navigate(Routes.ManageServices) },
                    onLogout = {
                        vm.logout()
                        nav.navigate(Routes.RoleSelect) { popUpTo(Routes.Main) { inclusive = true } }
                    }
                )
            }

            composable(
                route = Routes.WorkerProfile
            ) { backStackEntry ->

                val workerId =
                    backStackEntry
                        .arguments
                        ?.getString("workerId")
                        ?: ""

                WorkerProfileScreen(
                    vm = vm,
                    workerId = workerId,
                    onBack = {
                        nav.popBackStack()
                    },
                    onEditProfile = {
                        nav.navigate(
                            Routes.EditProfile
                        )
                    },
                    onManageServices = {
                        nav.navigate(
                            Routes.ManageServices
                        )
                    },
                    onLogout = {
                        nav.navigate(Routes.RoleSelect) {
                            popUpTo(0)
                        }
                    }
                )
            }

            composable(Routes.EditProfile) {
                EditProfileScreen(vm = vm, onBack = { nav.popBackStack() })
            }

            composable(Routes.ManageServices) {
                ManageServicesScreen(vm = vm, onBack = { nav.popBackStack() })
            }
        }

        // Toast overlay (top)
        AnimatedVisibility(
            visible = state.toastMessage != null,
            enter = fadeIn(animationSpec = tween(180)),
            exit = fadeOut(animationSpec = tween(180)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            val msg = state.toastMessage ?: ""
            KKToastCard(
                icon = { KKIconCircle(icon = Icons.Default.Notifications) },
                text = msg,
                onClose = { vm.clearToast() }
            )
        }
    }
}