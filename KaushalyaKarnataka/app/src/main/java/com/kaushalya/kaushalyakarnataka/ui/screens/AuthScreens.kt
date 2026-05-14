package com.kaushalya.kaushalyakarnataka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.imePadding
import com.google.firebase.auth.FirebaseAuth
import com.kaushalya.kaushalyakarnataka.data.MockData
import com.kaushalya.kaushalyakarnataka.data.repository.WorkerRepository
import com.kaushalya.kaushalyakarnataka.model.Role
import com.kaushalya.kaushalyakarnataka.model.User
import com.kaushalya.kaushalyakarnataka.ui.components.KKButton
import com.kaushalya.kaushalyakarnataka.ui.components.KKButtonVariant
import com.kaushalya.kaushalyakarnataka.ui.components.KKCard
import com.kaushalya.kaushalyakarnataka.ui.components.KKDropdown
import com.kaushalya.kaushalyakarnataka.ui.components.KKTextField
import com.kaushalya.kaushalyakarnataka.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onDone: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(1500)
        onDone()
    }

    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFF1D4ED8),
            Color(0xFF2563EB),
            Color(0xFF10B981)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Kaushalya\nKarnataka",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Local Skill Showcase",
                color = Color(0xFFD1FAE5),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(42.dp))

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }
}

@Composable
fun RoleSelectScreen(
    onBack: () -> Unit,
    onContinue: (Role) -> Unit
) {

    var selected by remember {
        mutableStateOf<Role?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Choose your role to get started.",
                color = Color(0xFF6B7280)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            KKCard(
                onClick = {
                    selected = Role.WORKER
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (selected == Role.WORKER) {
                            Modifier.border(
                                2.dp,
                                Color(0xFF2563EB),
                                RoundedCornerShape(20.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (selected == Role.WORKER)
                                    Color(0xFF2563EB)
                                else
                                    Color(0xFFDBEAFE)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.Engineering,
                            contentDescription = null,
                            tint =
                                if (selected == Role.WORKER)
                                    Color.White
                                else
                                    Color(0xFF2563EB),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "I am a Worker",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Create profile, list services, find clients.",
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }

            KKCard(
                onClick = {
                    selected = Role.CUSTOMER
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (selected == Role.CUSTOMER) {
                            Modifier.border(
                                2.dp,
                                Color(0xFF10B981),
                                RoundedCornerShape(20.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (selected == Role.CUSTOMER)
                                    Color(0xFF10B981)
                                else
                                    Color(0xFFD1FAE5)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint =
                                if (selected == Role.CUSTOMER)
                                    Color.White
                                else
                                    Color(0xFF10B981),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "I am a Customer",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Find and hire verified local skills.",
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        KKButton(
            text = "Continue",
            enabled = selected != null,
            variant =
                if (selected == Role.CUSTOMER)
                    KKButtonVariant.SECONDARY
                else
                    KKButtonVariant.PRIMARY,
            onClick = {
                selected?.let(onContinue)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SignInScreen(
    initialRole: Role?,
    onBack: () -> Unit,
    onSubmit: (User) -> Unit
) {

    val authViewModel: AuthViewModel = viewModel()

    val workerRepository = remember {
        WorkerRepository()
    }

    val scope = rememberCoroutineScope()

    var isLoginMode by remember {
        mutableStateOf(false)
    }

    var role by remember {
        mutableStateOf(initialRole ?: Role.CUSTOMER)
    }

    var name by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            Text(
                text =
                    if (isLoginMode)
                        "Welcome Back"
                    else
                        "Create Account",

                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text =
                    if (isLoginMode)
                        "Login to continue"
                    else
                        "Join Kaushalya Karnataka",

                color = Color(0xFF6B7280)
            )

            Spacer(Modifier.height(24.dp))

            if (!isLoginMode) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Button(
                        onClick = {
                            role = Role.CUSTOMER
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Customer")
                    }

                    Button(
                        onClick = {
                            role = Role.WORKER
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Worker")
                    }
                }

                Spacer(Modifier.height(18.dp))
            }

            if (!isLoginMode) {

                KKTextField(
                    label = "Full Name",
                    value = name,
                    required = true,
                    placeholder = "Enter your full name",
                    onValueChange = {
                        name = it
                    }
                )

                Spacer(Modifier.height(14.dp))

                KKTextField(
                    label = "Phone Number",
                    value = phone,
                    required = true,
                    placeholder = "10 digit mobile number",
                    onValueChange = {
                        phone = it
                    }
                )

                Spacer(Modifier.height(14.dp))
            }

            KKTextField(
                label = "Email",
                value = email,
                required = true,
                placeholder = "example@gmail.com",
                onValueChange = {
                    email = it
                }
            )

            Spacer(Modifier.height(14.dp))

            KKTextField(
                label = "Password",
                value = password,
                required = true,
                placeholder = "Minimum 6 characters",
                onValueChange = {
                    password = it
                }
            )

            if (!isLoginMode && role == Role.WORKER) {

                Spacer(Modifier.height(14.dp))

                KKDropdown(
                    label = "Trade Category",
                    value = category,
                    options = MockData.categories.filter {
                        it != "All"
                    },
                    required = true,
                    onChange = {
                        category = it
                    }
                )
            }

            Spacer(Modifier.height(18.dp))

            error?.let {

                Text(
                    text = it,
                    color = Color.Red
                )

                Spacer(Modifier.height(12.dp))
            }

            KKButton(

                text =
                    if (loading)
                        "Please wait..."
                    else if (isLoginMode)
                        "Login"
                    else
                        "Create Account",

                enabled = !loading,

                variant =
                    if (role == Role.WORKER)
                        KKButtonVariant.PRIMARY
                    else
                        KKButtonVariant.SECONDARY,

                onClick = {

                    loading = true
                    error = null

                    if (isLoginMode) {

                        authViewModel.login(

                            email = email.trim(),

                            password = password.trim(),

                            onSuccess = {

                                val uid =
                                    FirebaseAuth
                                        .getInstance()
                                        .currentUser
                                        ?.uid ?: ""

                                scope.launch {

                                    val worker =
                                        workerRepository
                                            .getWorkerById(uid)

                                    loading = false

                                    if (worker != null) {

                                        onSubmit(
                                            User(
                                                id = worker.id,
                                                name = worker.name,
                                                phone = worker.phone,
                                                role = Role.WORKER,
                                                category = worker.category
                                            )
                                        )

                                    } else {

                                        onSubmit(
                                            User(
                                                id = uid,
                                                name = email.substringBefore("@"),
                                                phone = "",
                                                role = Role.CUSTOMER
                                            )
                                        )
                                    }
                                }
                            },

                            onError = {

                                loading = false
                                error = it
                            }
                        )

                    } else {

                        authViewModel.register(

                            email = email.trim(),

                            password = password.trim(),

                            onSuccess = {

                                loading = false

                                val uid =
                                    FirebaseAuth
                                        .getInstance()
                                        .currentUser
                                        ?.uid ?: ""

                                onSubmit(
                                    User(
                                        id = uid,
                                        name = name.trim(),
                                        phone = phone.trim(),
                                        role = role,
                                        category =
                                            if (role == Role.WORKER)
                                                category
                                            else
                                                null
                                    )
                                )
                            },

                            onError = {

                                loading = false
                                error = it
                            }
                        )
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = {

                    isLoginMode = !isLoginMode
                    error = null
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(

                    text =
                        if (isLoginMode)
                            "Don't have an account? Register"
                        else
                            "Already have an account? Login",

                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}