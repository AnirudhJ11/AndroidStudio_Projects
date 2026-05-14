package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.model.RequestStatus
import com.kaushalya.kaushalyakarnataka.ui.components.KKButton
import com.kaushalya.kaushalyakarnataka.ui.components.KKButtonVariant
import com.kaushalya.kaushalyakarnataka.ui.components.KKCard
import com.kaushalya.kaushalyakarnataka.ui.components.clickableNoRipple
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaushalya.kaushalyakarnataka.viewmodel.AuthViewModel

@Composable
fun CustomerProfileTab(vm: AppViewModel, onLogout: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val state by vm.state.collectAsState()
    val user = state.currentUser
    if (user == null) {
        Text("Loading...")
        return
    }
    val myRequests = state.requests.filter { it.customerName == user.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(bottom = 90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2563EB), RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
                .padding(horizontal = 24.dp)
                .padding(top = 34.dp, bottom = 22.dp)
        ) {
            Text(user.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(Modifier.height(6.dp))
            Text(user.phone, color = Color(0xFFBFDBFE))
            Spacer(Modifier.height(12.dp))
            Text(
                "${user.role.name.lowercase()} Account",
                color = Color.White,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(Modifier.padding(16.dp)) {
            Text("My Requests", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(Modifier.height(12.dp))

            if (myRequests.isEmpty()) {
                KKCard(modifier = Modifier.fillMaxWidth()) {
                    Text("No requests yet.", color = Color(0xFF6B7280))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    myRequests.forEach { req ->
                        KKCard(modifier = Modifier.fillMaxWidth()) {
                            Text(req.requestText, fontWeight = FontWeight.Medium, color = Color(0xFF1F2937), maxLines = 1)
                            Spacer(Modifier.height(6.dp))
                            Text("To worker ${req.workerId} • ${req.timestamp}", color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall)

                            Spacer(Modifier.height(10.dp))
                            RowChip(
                                text = req.status.name.lowercase(),
                                icon = Icons.Default.AccessTime,
                                bg = Color(0xFFFFFBEB),
                                fg = Color(0xFFB45309)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("Settings", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(Modifier.height(10.dp))

            KKCard(modifier = Modifier.fillMaxWidth()) {
                RowAction(icon = Icons.Default.Settings, text = "Account Settings", tint = Color(0xFF9CA3AF), onClick = {})
                Spacer(Modifier.height(6.dp))
                RowAction(
                    icon = Icons.Default.Logout,
                    text = "Log Out",
                    tint = Color(0xFFEF4444),
                    textColor = Color(0xFFDC2626),

                    onClick = {

                        authViewModel.logout()

                        onLogout()
                    }
                )
            }
        }
    }
}

@Composable
fun RequestsTab(vm: AppViewModel) {
    val state by vm.state.collectAsState()
    val user = state.currentUser ?: return
    val workerRequests = state.requests.filter { it.workerId == user.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(bottom = 90.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Incoming Requests", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(Modifier.padding(16.dp)) {
            if (workerRequests.isEmpty()) {
                Text("No requests yet.", color = Color(0xFF6B7280), modifier = Modifier.padding(top = 40.dp))
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    workerRequests.forEach { req ->
                        KKCard(modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(req.customerName, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                Text(req.timestamp, color = Color(0xFF9CA3AF), style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(req.customerPhone, color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(req.requestText, color = Color(0xFF374151))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Prefers: ${req.preferredTime}", color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(Modifier.height(12.dp))
                            if (req.status == RequestStatus.PENDING) {
                                KKButton(
                                    text = "Mark as Contacted",
                                    variant = KKButtonVariant.OUTLINE,
                                    onClick = { vm.markRequestContacted(req.id) }
                                )
                            } else {
                                RowChip(text = "Contacted", icon = Icons.Default.CheckCircle, bg = Color(0xFFECFDF5), fg = Color(0xFF059669))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServicesAndReviewsFallback(title: String) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF9FAFB)).padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(Modifier.height(8.dp))
        Text("This section is primarily managed via Profile for Workers.", color = Color(0xFF6B7280))
    }
}

@Composable
private fun RowAction(
    icon: ImageVector,
    text: String,
    tint: Color,
    textColor: Color = Color(0xFF374151),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickableNoRipple { onClick() },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null, tint = tint)
        Text(text, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun RowChip(text: String, icon: ImageVector, bg: Color, fg: Color) {
    Row(
        modifier = Modifier
            .background(bg, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, null, tint = fg, modifier = Modifier.size(14.dp))
        Text(text, color = fg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
    }
}