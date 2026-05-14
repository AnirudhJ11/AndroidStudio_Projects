package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.model.Role
import com.kaushalya.kaushalyakarnataka.model.Tab
import com.kaushalya.kaushalyakarnataka.ui.components.clickableNoRipple

@Composable
fun MainTabsScreen(
    vm: AppViewModel,
    onOpenWorker: (String) -> Unit,
    onEditProfile: () -> Unit,
    onManageServices: () -> Unit,
    onLogout: () -> Unit
) {
    val state by vm.state.collectAsState()
    val user = state.currentUser

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...")
        }
        return
    }

    val tabs = if (user.role == Role.WORKER) {
        listOf(
            Tab.HOME to Icons.Default.Home,
            Tab.REQUESTS to Icons.Default.Notifications,
            Tab.PROFILE to Icons.Default.Person
        )
    } else {
        listOf(
            Tab.HOME to Icons.Default.Home,
            Tab.SERVICES to Icons.Default.Work,
            Tab.REVIEWS to Icons.Default.Star,
            Tab.PROFILE to Icons.Default.Person
        )
    }

    Box(Modifier.fillMaxSize().background(Color.White)) {

        when (state.currentTab) {
            Tab.HOME -> HomeScreen(vm = vm, onOpenWorker = onOpenWorker)
            Tab.REQUESTS -> RequestsTab(vm = vm)
            Tab.PROFILE -> {
                if (user.role == Role.WORKER) {
                    if (state.workers.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    } else {
                        WorkerProfileScreen(
                            vm = vm,
                            workerId = user.id,
                            onBack = {},
                            onEditProfile = onEditProfile,
                            onManageServices = onManageServices,
                            onLogout = onLogout
                        )
                    }
                } else {
                    CustomerProfileTab(vm = vm, onLogout = onLogout)
                }
            }
            Tab.SERVICES -> ServicesAndReviewsFallback(title = "Services Directory")
            Tab.REVIEWS -> ServicesAndReviewsFallback(title = "My Reviews")
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (tab, icon) ->
                val active = state.currentTab == tab
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .widthIn(min = 64.dp)
                        .clickableNoRipple{ vm.setTab(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (active) Color(0xFF2563EB) else Color(0xFF9CA3AF),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = when (tab) {
                            Tab.HOME -> if (user.role == Role.WORKER) "Discover" else "Home"
                            Tab.REQUESTS -> "Requests"
                            Tab.PROFILE -> if (user.role == Role.WORKER) "My Profile" else "Profile"
                            Tab.SERVICES -> "Services"
                            Tab.REVIEWS -> "Reviews"
                        },
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) Color(0xFF2563EB) else Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}