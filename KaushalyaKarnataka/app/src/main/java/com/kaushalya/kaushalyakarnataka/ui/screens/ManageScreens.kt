package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.data.MockData
import com.kaushalya.kaushalyakarnataka.model.PriceType
import com.kaushalya.kaushalyakarnataka.model.Service
import com.kaushalya.kaushalyakarnataka.ui.components.KKButton
import com.kaushalya.kaushalyakarnataka.ui.components.KKButtonVariant
import com.kaushalya.kaushalyakarnataka.ui.components.KKCard
import com.kaushalya.kaushalyakarnataka.ui.components.KKDropdown
import com.kaushalya.kaushalyakarnataka.ui.components.KKTextField
import java.util.UUID

@Composable
fun EditProfileScreen(vm: AppViewModel, onBack: () -> Unit) {
    val state by vm.state.collectAsState()
    val user = state.currentUser ?: return
    val worker = state.workers.find { it.id == user.id } ?: return

    var name by remember { mutableStateOf(worker.name) }
    var category by remember { mutableStateOf(worker.category) }
    var phone by remember { mutableStateOf(worker.phone) }
    var exp by remember { mutableStateOf(worker.experience.toString()) }
    var bio by remember { mutableStateOf(worker.bio) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF9FAFB))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF374151)) }
            Text("Edit Profile", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Spacer(Modifier.width(48.dp))
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ){
            KKTextField("Full Name", name, onValueChange = { name = it })
            KKDropdown("Category", category, MockData.categories.filter { it != "All" }, onChange = { category = it })
            KKTextField("Phone Number", phone, onValueChange = { phone = it })
            KKTextField("Years of Experience", exp, onValueChange = { exp = it })
            KKTextField("Short Bio", bio, singleLine = false, onValueChange = { bio = it })
        }

        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KKButton("Cancel", modifier = Modifier.weight(1f), variant = KKButtonVariant.GHOST, onClick = onBack)
            KKButton("Save Profile", modifier = Modifier.weight(1f), variant = KKButtonVariant.PRIMARY, onClick = {
                vm.updateWorkerProfile(worker.id) { w ->
                    w.copy(
                        name = name,
                        category = category,
                        phone = phone,
                        bio = bio,
                        experience = exp.toIntOrNull() ?: 0
                    )
                }
                onBack()
            })
        }
    }
}

@Composable
fun ManageServicesScreen(vm: AppViewModel, onBack: () -> Unit) {
    val state by vm.state.collectAsState()
    val user = state.currentUser ?: return
    val worker = state.workers.find { it.id == user.id } ?: return

    var adding by remember { mutableStateOf(false) }

    if (adding) {
        AddServiceScreen(
            onBack = { adding = false },
            onSave = { name, desc, type, price ->
                val service = Service(
                    id = UUID.randomUUID().toString().take(8),
                    name = name,
                    description = desc,
                    priceType = type,
                    price = price
                )
                vm.addService(worker.id, service)
                adding = false
            }
        )
        return
    }

    Column(Modifier.fillMaxSize().background(Color(0xFFF9FAFB))) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF374151)) }
            Text("My Services", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            IconButton(onClick = { adding = true }) { Icon(Icons.Default.Add, null, tint = Color(0xFF2563EB)) }
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (worker.services.isEmpty()) {
                Column(
                    Modifier.fillMaxWidth().padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No service cards yet.", color = Color(0xFF6B7280))
                    Spacer(Modifier.height(12.dp))
                    KKButton("Add Your First Service", onClick = { adding = true })
                }
            } else {
                worker.services.forEach { srv ->
                    KKCard(modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.weight(1f)) {
                                Text(srv.name, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                Spacer(Modifier.height(4.dp))
                                Text(srv.description, color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall, maxLines = 2)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = (if (srv.priceType == PriceType.STARTING) "Starting at " else "") + "₹${srv.price}",
                                    color = Color(0xFF2563EB),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            IconButton(onClick = { vm.deleteService(worker.id, srv.id) }) {
                                Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddServiceScreen(
    onBack: () -> Unit,
    onSave: (name: String, desc: String, type: PriceType, price: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(PriceType.FIXED) }
    var price by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(Color(0xFFF9FAFB))) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF374151)) }
            Text("Add Service", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Spacer(Modifier.width(48.dp))
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            KKTextField("Service Name", name, required = true, placeholder = "e.g. Fan Repair", onValueChange = { name = it })
            KKTextField("Description", desc, placeholder = "Short details...", singleLine = false, onValueChange = { desc = it })

            Text("Pricing Type", fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SegBtn(text = "Fixed Price", active = type == PriceType.FIXED, onClick = { type = PriceType.FIXED }, modifier = Modifier.weight(1f))
                SegBtn(text = "Starting At", active = type == PriceType.STARTING, onClick = { type = PriceType.STARTING }, modifier = Modifier.weight(1f))
            }

            KKTextField("Price (₹)", price, required = true, onValueChange = { price = it })
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                KKButton("Cancel", modifier = Modifier.weight(1f), variant = KKButtonVariant.OUTLINE, onClick = onBack)
                KKButton(
                    "Save Service",
                    modifier = Modifier.weight(1f),
                    enabled = name.isNotBlank() && price.isNotBlank(),
                    onClick = { onSave(name, desc, type, price.toIntOrNull() ?: 0) }
                )
            }
        }
    }
}

@Composable
private fun SegBtn(text: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .background(if (active) Color.White else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp)
            .clickable { onClick() },
        color = if (active) Color(0xFF2563EB) else Color(0xFF6B7280),
        fontWeight = FontWeight.Bold
    )
}