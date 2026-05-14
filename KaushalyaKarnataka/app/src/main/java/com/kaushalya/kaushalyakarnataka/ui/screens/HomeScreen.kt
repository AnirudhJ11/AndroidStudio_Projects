package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel
import com.kaushalya.kaushalyakarnataka.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    vm: AppViewModel,
    onOpenWorker: (String) -> Unit
) {

    val homeViewModel: HomeViewModel = viewModel()

    val workers = homeViewModel.workers.collectAsState()

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

    var search by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    val categories =
        listOf("All") +
                workers.value
                    .map { it.category }
                    .distinct()

    val filtered =
        workers.value.filter { worker ->

            val matchCategory =
                selectedCategory == "All" ||
                        worker.category == selectedCategory

            val matchSearch =
                worker.name.contains(
                    search,
                    ignoreCase = true
                ) ||
                        worker.category.contains(
                            search,
                            ignoreCase = true
                        )

            matchCategory && matchSearch
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {

        // =========================================
        // HEADER
        // =========================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF2563EB),
                            Color(0xFF1D4ED8)
                        )
                    )
                )
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFBFDBFE),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Bengaluru, Karnataka",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kaushalya Karnataka",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Find skilled workers near you",
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 12.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    value = search,

                    onValueChange = {
                        search = it
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth(),

                    decorationBox = { inner ->

                        if (search.isBlank()) {

                            Text(
                                "Search electrician, plumber...",
                                color = Color(0xFF9CA3AF)
                            )
                        }

                        inner()
                    }
                )
            }
        }

        // =========================================
        // CATEGORY CHIPS
        // =========================================

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),

            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp
            )
        ) {

            items(categories) { category ->

                FilterChip(
                    selected =
                        selectedCategory == category,

                    onClick = {
                        selectedCategory = category
                    },

                    label = {
                        Text(category)
                    }
                )
            }
        }

        // =========================================
        // SECTION TITLE
        // =========================================

        Text(
            text =
                if (selectedCategory == "All")
                    "Top Rated Workers"
                else
                    "$selectedCategory Workers",

            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),

            fontWeight = FontWeight.Bold,

            style = MaterialTheme.typography.titleMedium,

            color = Color(0xFF111827)
        )

        // =========================================
        // EMPTY STATE
        // =========================================

        if (filtered.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.Engineering,
                    contentDescription = null,
                    tint = Color(0xFFD1D5DB),
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "No workers found",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Try another category or search term.",
                    color = Color(0xFF9CA3AF)
                )
            }

        } else {

            // =========================================
            // WORKER LIST
            // =========================================

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 10.dp,
                    end = 16.dp,
                    bottom = 110.dp
                ),

                verticalArrangement = Arrangement.spacedBy(14.dp),

                modifier = Modifier.fillMaxSize()
            ) {

                items(filtered) { worker ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onOpenWorker(worker.id)
                            },

                        shape = RoundedCornerShape(22.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                AsyncImage(
                                    model = worker.avatar,

                                    contentDescription = null,

                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = worker.name,

                                        fontWeight = FontWeight.Bold,

                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = worker.category,

                                        color = Color(0xFF2563EB),

                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text =
                                                if (worker.rating > 0)
                                                    worker.rating.toString()
                                                else
                                                    "New",

                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "(${worker.reviewCount} reviews)",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = worker.bio,

                                maxLines = 2,

                                color = Color(0xFF4B5563)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween,

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column {

                                    Text(
                                        text = "Starting at",

                                        color = Color(0xFF6B7280)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "₹${worker.startingPrice}",

                                        fontWeight = FontWeight.Bold,

                                        style =
                                            MaterialTheme.typography.titleLarge
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(14.dp)
                                        )
                                        .background(Color(0xFFEFF6FF))
                                        .padding(
                                            horizontal = 14.dp,
                                            vertical = 10.dp
                                        ),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "View Profile",

                                        color = Color(0xFF2563EB),

                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Color(0xFF2563EB)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}