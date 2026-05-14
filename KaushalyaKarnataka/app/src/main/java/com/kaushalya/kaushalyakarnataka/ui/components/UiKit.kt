package com.kaushalya.kaushalyakarnataka.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class KKButtonVariant { PRIMARY, SECONDARY, OUTLINE, GHOST, DANGER }

@Composable
fun KKButton(
    text: String,
    modifier: Modifier = Modifier,
    variant: KKButtonVariant = KKButtonVariant.PRIMARY,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    when (variant) {
        KKButtonVariant.PRIMARY -> Button(
            modifier = modifier.fillMaxWidth().height(52.dp),
            enabled = enabled,
            shape = shape,
            onClick = onClick
        ) { Text(text, fontWeight = FontWeight.SemiBold) }

        KKButtonVariant.SECONDARY -> Button(
            modifier = modifier.fillMaxWidth().height(52.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
            onClick = onClick
        ) { Text(text, fontWeight = FontWeight.SemiBold) }

        KKButtonVariant.OUTLINE -> OutlinedButton(
            modifier = modifier.fillMaxWidth().height(52.dp),
            enabled = enabled,
            shape = shape,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            onClick = onClick
        ) { Text(text, fontWeight = FontWeight.SemiBold) }

        KKButtonVariant.GHOST -> Button(
            modifier = modifier.fillMaxWidth().height(52.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6), contentColor = Color(0xFF374151)),
            onClick = onClick
        ) { Text(text, fontWeight = FontWeight.SemiBold) }

        KKButtonVariant.DANGER -> Button(
            modifier = modifier.fillMaxWidth().height(52.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2), contentColor = Color(0xFFDC2626)),
            onClick = onClick
        ) { Text(text, fontWeight = FontWeight.SemiBold) }
    }
}

@Composable
fun KKCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Surface(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() } else Modifier
        ),
        shape = shape,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun KKChip(text: String, active: Boolean, onClick: () -> Unit) {
    val bg = if (active) MaterialTheme.colorScheme.primary else Color(0xFFEFF6FF)
    val fg = if (active) Color.White else Color(0xFF1D4ED8)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = fg, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun KKLabel(text: String, required: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = Color(0xFF374151), fontWeight = FontWeight.SemiBold)
        if (required) Text(" *", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun KKTextField(
    label: String,
    value: String,
    required: Boolean = false,
    placeholder: String = "",
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        KKLabel(label, required)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { if (placeholder.isNotBlank()) Text(placeholder) },
            singleLine = singleLine,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFF9FAFB)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KKDropdown(
    label: String,
    value: String,
    options: List<String>,
    required: Boolean = false,
    onChange: (String) -> Unit
) {
    var expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        KKLabel(label, required)
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
            OutlinedTextField(
                readOnly = true,
                value = value,
                onValueChange = {},
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Select $label") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFF9FAFB)
                )
            )
            ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            onChange(opt)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun KKToastCard(
    icon: @Composable () -> Unit,
    text: String,
    onClose: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        color = Color(0xFF059669),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            icon()
            Text(text = text, color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Text(
                "×",
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClose() }
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun KKIconCircle(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}