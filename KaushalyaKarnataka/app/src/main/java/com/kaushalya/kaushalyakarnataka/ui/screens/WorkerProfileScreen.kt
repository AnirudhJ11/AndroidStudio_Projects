package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog // ✅ ONLY this Dialog import
import coil.compose.AsyncImage
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.model.PriceType
import com.kaushalya.kaushalyakarnataka.ui.components.KKButton
import com.kaushalya.kaushalyakarnataka.ui.components.KKButtonVariant
import com.kaushalya.kaushalyakarnataka.ui.components.KKTextField
import kotlin.random.Random
import androidx.compose.runtime.collectAsState
import com.kaushalya.kaushalyakarnataka.model.WorkerProfile
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.navigationBarsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerProfileScreen(
    vm: AppViewModel,
    workerId: String,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onManageServices: () -> Unit,
    onLogout: () -> Unit
) {
    val state by vm.state.collectAsState()


    val worker =
        state.workers.find {
            it.id == workerId
        }

    if (worker == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }

        return
    }
    val user = state.currentUser
    val isSelf = user?.id == worker.id
    val isUnlocked = isSelf || (state.contactUnlockedFor[worker.id] == true)

    var hireSheet by remember { mutableStateOf(false) }
    var hireText by remember { mutableStateOf("") }
    var hireTime by remember { mutableStateOf("Today Evening") }

    var reviewSheet by remember { mutableStateOf(false) }
    var reviewRating by remember { mutableStateOf(5) } // ✅ more compatible than mutableIntStateOf
    var reviewText by remember { mutableStateOf("") }

    var photoViewerUrl by remember { mutableStateOf<String?>(null) }

    var editAvatarDialog by remember {
        mutableStateOf(false)
    }

    var avatarInput by remember {
        mutableStateOf(worker.avatar)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.92f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF374151))
            }
            Text("Profile", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Spacer(Modifier.width(48.dp))
        }

        // Main info panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = worker.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(999.dp))
                        .clickable {

                            if (isSelf) {

                                avatarInput = worker.avatar
                                editAvatarDialog = true
                            }
                        }
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(worker.name, fontWeight = FontWeight.Bold, color = Color(0xFF111827), fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.Shield, null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${worker.category} • ${worker.experience} yrs exp", color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (worker.rating > 0) worker.rating.toString() else "New", fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        Spacer(Modifier.width(6.dp))
                        Text("(${worker.reviewCount} reviews)", color = Color(0xFF6B7280))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Contact strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFDBEAFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Phone, null, tint = Color(0xFF2563EB))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Contact Number", color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(2.dp))
                        if (isUnlocked) {
                            Text(worker.phone, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        } else {
                            Text("Hidden until requested", fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563))
                        }
                    }
                }

                if (isUnlocked && !isSelf) {
                    Text(
                        "Call Now",
                        modifier = Modifier
                            .background(Color(0xFF2563EB), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(worker.bio, color = Color(0xFF4B5563))

            if (isSelf) {

                Spacer(Modifier.height(12.dp))

                KKButton(
                    text = "Edit Profile",
                    variant = KKButtonVariant.OUTLINE,
                    onClick = onEditProfile
                )

                Spacer(Modifier.height(10.dp))

                KKButton(
                    text = "Logout",
                    variant = KKButtonVariant.DANGER,
                    onClick = {
                        vm.logout()
                        onLogout()
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Portfolio
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Verified Work", fontWeight = FontWeight.Bold, color = Color(0xFF111827), fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    Text("Photos from recent tasks", color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall)
                }
                if (isSelf) {
                    IconButton(onClick = {
                        val randomImage =
                            "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?w=400&h=300&fit=crop&q=80&rand=${Random.nextInt(100000)}"
                        vm.addPortfolioPhoto(worker.id, randomImage)
                    }) {
                        Icon(Icons.Default.Add, null, tint = Color(0xFF2563EB))
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            if (worker.portfolio.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(16.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Image, null, tint = Color(0xFF9CA3AF))
                    Spacer(Modifier.width(8.dp))
                    Text("No photos yet", color = Color(0xFF9CA3AF))
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    userScrollEnabled = false,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.heightIn(min = 0.dp, max = 400.dp)
                ) {
                    items(worker.portfolio) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { photoViewerUrl = url }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Services
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Service Cards", fontWeight = FontWeight.Bold, color = Color(0xFF111827), fontSize = MaterialTheme.typography.titleMedium.fontSize)
                if (isSelf) {
                    Text("Manage", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onManageServices() })
                }
            }

            Spacer(Modifier.height(10.dp))

            if (worker.services.isEmpty()) {
                Text("No services listed yet.", color = Color(0xFF6B7280))
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    worker.services.forEach { srv ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            tonalElevation = 0.dp,
                            shadowElevation = 1.dp,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                        ) {
                            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(srv.name, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                    Spacer(Modifier.height(4.dp))
                                    Text(srv.description, color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall, maxLines = 2)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        if (srv.priceType == PriceType.STARTING) "STARTING AT" else "FIXED",
                                        color = Color(0xFF9CA3AF),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text("₹${srv.price}", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        if (!isSelf) {

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {

                KKButton(
                    text = "Request to Hire Me",
                    variant = KKButtonVariant.PRIMARY,
                    onClick = {
                        hireSheet = true
                    }
                )
            }
        }

        // Reviews preview
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Review Wall", fontWeight = FontWeight.Bold, color = Color(0xFF111827), fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text("See all", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(10.dp))

            if (worker.reviews.isEmpty()) {
                Text("No reviews yet.", color = Color(0xFF6B7280))
            } else {
                worker.reviews.take(2).forEach { r ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(r.authorName, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                            Text(r.date, color = Color(0xFF9CA3AF), style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            repeat(5) { idx ->
                                Icon(
                                    Icons.Default.Star,
                                    null,
                                    tint = if (idx < r.rating) Color(0xFFF59E0B) else Color(0xFFE5E7EB),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(r.text, color = Color(0xFF4B5563))
                        Spacer(Modifier.height(10.dp))
                        HorizontalDivider(
                            color = Color(0xFFF3F4F6)
                        )
                    }
                }
            }

            if (!isSelf) {
                Spacer(Modifier.height(12.dp))
                KKButton(
                    text = "Write a Review",
                    variant = KKButtonVariant.OUTLINE,
                    onClick = { reviewSheet = true }
                )
            }
        }
    }

    // Fixed bottom action for customer

    // Hire bottom sheet
    if (hireSheet) {
        ModalBottomSheet(onDismissRequest = { hireSheet = false }) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Request a Call", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
                Text("Send a request to ${worker.name}. They will see your requirement and contact you.", color = Color(0xFF6B7280))

                KKTextField(
                    label = "What do you need?",
                    value = hireText,
                    placeholder = "e.g. My ceiling fan is making a noise...",
                    singleLine = false,
                    onValueChange = { hireText = it }
                )
                KKTextField(
                    label = "Preferred Time",
                    value = hireTime,
                    onValueChange = { hireTime = it }
                )

                KKButton(
                    text = "Send Request",
                    enabled = hireText.isNotBlank(),
                    onClick = {
                        val cu = user ?: return@KKButton
                        vm.submitRequest(worker.id, cu.name, cu.phone, hireText, hireTime)
                        hireSheet = false
                        hireText = ""
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    // Review bottom sheet
    if (reviewSheet) {
        ModalBottomSheet(onDismissRequest = { reviewSheet = false }) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Write a Review", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    (1..5).forEach { s ->
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = if (s <= reviewRating) Color(0xFFF59E0B) else Color(0xFFD1D5DB),
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { reviewRating = s }
                                .padding(4.dp)
                        )
                    }
                }

                KKTextField(
                    label = "Your Feedback",
                    value = reviewText,
                    placeholder = "How was the service? (max 200 chars)",
                    singleLine = false,
                    onValueChange = { reviewText = it.take(200) }
                )
                Text("${reviewText.length}/200", modifier = Modifier.fillMaxWidth(), color = Color(0xFF9CA3AF))

                KKButton(
                    text = "Submit Review",
                    enabled = reviewText.isNotBlank(),
                    onClick = {
                        val cu = user ?: return@KKButton
                        vm.addReview(worker.id, cu.name, reviewRating, reviewText)
                        reviewSheet = false
                        reviewText = ""
                        reviewRating = 5
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    // Photo viewer overlay
    photoViewerUrl?.let { url ->
        Dialog(onDismissRequest = { photoViewerUrl = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { photoViewerUrl = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = url, contentDescription = null, modifier = Modifier.fillMaxWidth().padding(10.dp))
                Text(
                    "Tap anywhere to close",
                    color = Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp)
                )
            }
        }
    }

    if (editAvatarDialog) {

        AlertDialog(

            onDismissRequest = {
                editAvatarDialog = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        vm.updateWorkerProfile(worker.id) {

                            it.copy(
                                avatar = avatarInput
                            )
                        }

                        editAvatarDialog = false
                    }
                ) {

                    Text("Save")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        editAvatarDialog = false
                    }
                ) {

                    Text("Cancel")
                }
            },

            title = {

                Text("Profile Image URL")
            },

            text = {

                Column {

                    AsyncImage(
                        model = avatarInput,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(999.dp))
                    )

                    Spacer(Modifier.height(12.dp))

                    KKTextField(
                        label = "Image URL",
                        value = avatarInput,
                        placeholder = "Paste image URL",
                        onValueChange = {
                            avatarInput = it
                        }
                    )
                }
            }
        )
    }
}