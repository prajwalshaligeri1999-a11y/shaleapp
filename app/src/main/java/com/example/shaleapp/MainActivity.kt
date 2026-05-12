package com.example.shaleapp

import android.content.Context
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*

import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App(this) }
    }
}

// ---------------- APP ----------------
@Composable
fun App(context: Context) {

    var screen by remember { mutableStateOf("splash") }
    var isKannada by remember { mutableStateOf(false) }

    when (screen) {

        "splash" -> Splash { screen = "selection" }

        "selection" -> Selection(
            { screen = "student_login" },
            { screen = "parent_login" }
        )

        "student_login" -> LoginScreen(context, "Student Login",
            { screen = "selection" },
            { screen = "register_student" }) {
            screen = "dashboard"
        }

        "parent_login" -> LoginScreen(context, "Parent Login",
            { screen = "selection" },
            { screen = "register_parent" }) {
            screen = "dashboard"
        }

        "register_student" -> RegisterScreen(context, false,
            { screen = "student_login" },
            { screen = "selection" })

        "register_parent" -> RegisterScreen(context, true,
            { screen = "parent_login" },
            { screen = "selection" })

        "dashboard" -> DashboardScreen(
            isKannada,
            { isKannada = !isKannada },
            { screen = "profile" },
            { screen = "parent_meeting" },
            { screen = "labs" },
            { screen = "events" },
            { screen = "attendance" },
            { screen = "updates" },
            { screen = "gallery" }
        )

        "parent_meeting" -> ParentMeetingScreen { screen = "dashboard" }
        "labs" -> LabsScreen { screen = "dashboard" }
        "events" -> EventsScreen { screen = "dashboard" }
        "attendance" -> AttendanceScreen { screen = "dashboard" }
        "updates" -> UpdatesScreen { screen = "dashboard" }
        "gallery" -> GalleryScreen { screen = "dashboard" }
        "profile" -> ProfileScreen { screen = "selection" }
    }
}

// ---------------- COMMON ----------------
@Composable
fun InputBox(label: String, value: String, onChange: (String) -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(55.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
    ) {
        TextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun LinkText(text: String, onClick: () -> Unit) {
    Text(
        text,
        color = Color.White,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { onClick() }
    )
}

// ---------------- SPLASH ----------------
@Composable
fun Splash(next: () -> Unit) {

    var startAnim by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1.2f else 0.5f,
        animationSpec = tween(durationMillis = 800),
        label = ""
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnim = true
        delay(1800)
        next()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF0D47A1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            "🏫",
            fontSize = 120.sp,
            modifier = Modifier.scale(scale)
        )

        Spacer(Modifier.height(20.dp))


        Text(
            "Namma Shale",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alpha)
        )
    }
}

// ---------------- SELECTION (FIXED FONT) ----------------
@Composable
fun Selection(onStudent: () -> Unit, onParent: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Color(0xFF0D47A1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            Modifier.fillMaxWidth(0.85f).height(90.dp)
                .border(2.dp, Color.Black)
                .background(Color.Black, RoundedCornerShape(10.dp))
                .clickable { onStudent() },
            contentAlignment = Alignment.Center
        ) {
            Text("Students", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(25.dp))

        Box(
            Modifier.fillMaxWidth(0.85f).height(90.dp)
                .border(2.dp, Color.Black)
                .background(Color(0xFF2E7D32), RoundedCornerShape(10.dp))
                .clickable { onParent() },
            contentAlignment = Alignment.Center
        ) {
            Text("Parents", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}


// ---------------- LOGIN ----------------
@Composable
fun LoginScreen(
    context: Context,
    title: String,
    onBack: () -> Unit,
    onCreate: () -> Unit,
    onSuccess: () -> Unit
) {
    var sats by remember { mutableStateOf("") }
    var roll by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val isParent = title == "Parent Login"

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF0D47A1))
    ) {

        // 🔙 Back Button
        Text(
            "Back →",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { onBack() }
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🏷 Title
            Text(
                title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))


            InputBox("SATS Number", sats) {
                sats = it
            }

            Spacer(Modifier.height(10.dp))


            InputBox("Roll Number", roll) {
                roll = it
            }


            if (isParent) {

                Spacer(Modifier.height(10.dp))

                InputBox("Password", password) {
                    password = it
                }
            }

            Spacer(Modifier.height(20.dp))


            LinkText("Submit") {


                if (sats.isBlank() || roll.isBlank()) {
                    error = "Please fill all details"
                    return@LinkText
                }

                if (isParent && password.isBlank()) {
                    error = "Enter Password"
                    return@LinkText
                }

                val pref = context.getSharedPreferences(
                    "user",
                    Context.MODE_PRIVATE
                )

                val s = pref.getString("sats", "")
                val r = pref.getString("roll", "")
                val p = pref.getString("password", "")


                if (isParent) {

                    if (sats == s &&
                        roll == r &&
                        password == p
                    ) {
                        onSuccess()
                    } else {
                        error = "Wrong Password or Details"
                    }

                } else {


                    if (sats == s &&
                        roll == r
                    ) {
                        onSuccess()
                    } else {
                        error = "Invalid Student Details"
                    }
                }
            }

            Spacer(Modifier.height(10.dp))


            Text(
                error,
                color = Color.Red
            )

            Spacer(Modifier.height(10.dp))


            LinkText("Create Account") {
                onCreate()
            }
        }
    }
}
// ---------------- REGISTER ----------------
@Composable
fun RegisterScreen(
    context: Context,
    isParent: Boolean,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    var sats by remember { mutableStateOf("") }
    var roll by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize().background(Color(0xFF0D47A1))) {


        Text(
            "Back →",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { onBack() }
        )

        Column(
            Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InputBox("SATS Number", sats) { sats = it }
            Spacer(Modifier.height(10.dp))
            InputBox("Roll Number", roll) { roll = it }

            if (isParent) {
                Spacer(Modifier.height(10.dp))
                InputBox("New Password", pass) { pass = it }

                Spacer(Modifier.height(10.dp))
                InputBox("Confirm Password", confirm) { confirm = it }
            }

            Spacer(Modifier.height(20.dp))

            LinkText("Register") {
                if (isParent && pass != confirm) {
                    error = "Password mismatch"
                } else {
                    val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                    pref.edit()
                        .putString("sats", sats)
                        .putString("roll", roll)
                        .putString("password", pass)
                        .apply()

                    onDone()
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(error, color = Color.Red)
        }
    }
}

// ---------------- DASHBOARD ----------------
@Composable
fun DashboardScreen(
    isKannada: Boolean,
    toggle: () -> Unit,
    onProfile: () -> Unit,
    onParentMeeting: () -> Unit,
    onLabs: () -> Unit,
    onEvents: () -> Unit,
    onAttendance: () -> Unit,
    onUpdates: () -> Unit,
    onGallery: () -> Unit
) {


    var startAnim by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(700), label = ""
    )

    val offsetY by animateDpAsState(
        targetValue = if (startAnim) 0.dp else 50.dp,
        animationSpec = tween(700), label = ""
    )

    LaunchedEffect(Unit) {
        startAnim = true
    }

    var search by remember { mutableStateOf("") }

    val items = if (isKannada)
        listOf("ಪೋಷಕರ ಸಭೆ","ಪ್ರಯೋಗಾಲಯ","ಕಾರ್ಯಕ್ರಮ","ಗ್ಯಾಲರಿ","ಹಾಜರಾತಿ","ನವೀಕರಣ")
    else
        listOf("Parent Meeting","Labs & Library","Events","Gallery","Attendance","Updates")

    val colors = listOf(
        Color.Yellow, Color(0xFF9C27B0),
        Color.Red, Color.Blue,
        Color.Green, Color.White
    )

    val filtered = if (search.length >= 2)
        items.filter { it.contains(search, true) }
    else items

    Box(Modifier.fillMaxSize().background(Color(0xFF0D47A1))) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .alpha(alphaAnim)
                .offset(y = offsetY)
        ) {

            TextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Box(
                Modifier.align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(50.dp))
                    .clickable { onProfile() },
                contentAlignment = Alignment.Center
            ) {
                Text("👨‍🎓", fontSize = 40.sp)
            }

            Spacer(Modifier.height(10.dp))

            filtered.chunked(2).forEach { row ->
                Row {
                    row.forEach { item ->

                        val index = items.indexOf(item)


                        var pressed by remember { mutableStateOf(false) }

                        val scaleClick by animateFloatAsState(
                            targetValue = if (pressed) 0.9f else 1f,
                            animationSpec = tween(150), label = ""
                        )

                        Box(
                            Modifier.weight(1f)
                                .aspectRatio(1f)
                                .padding(6.dp)
                                .border(2.dp, Color.Black)
                                .background(colors[index])
                                .scale(scaleClick)
                                .clickable {
                                    pressed = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(item, fontWeight = FontWeight.Bold)
                        }


                        if (pressed) {
                            LaunchedEffect(Unit) {
                                delay(100)
                                pressed = false

                                when (item) {
                                    "Parent Meeting","ಪೋಷಕರ ಸಭೆ" -> onParentMeeting()
                                    "Labs & Library","ಪ್ರಯೋಗಾಲಯ" -> onLabs()
                                    "Events","ಕಾರ್ಯಕ್ರಮ" -> onEvents()
                                    "Attendance","ಹಾಜರಾತಿ" -> onAttendance()
                                    "Updates","ನವೀಕರಣ" -> onUpdates()
                                    "Gallery","ಗ್ಯಾಲರಿ" -> onGallery()
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            Modifier.align(Alignment.BottomStart)
                .padding(12.dp)
                .background(Color.White)
                .clickable { toggle() }
                .padding(10.dp)
        ) {
            Text(if (isKannada) "E" else "ಕ")
        }
    }
}

// ---------------- SIMPLE LIST ----------------
@Composable
fun SimpleListScreen(title: String, list: List<String>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF0D47A1)).padding(16.dp)) {
        Text("Back", color = Color.White, modifier = Modifier.clickable { onBack() })
        Spacer(Modifier.height(20.dp))
        Text(title, color = Color.White)

        Spacer(Modifier.height(20.dp))

        list.forEach {
            Box(
                Modifier.fillMaxWidth().padding(8.dp)
                    .background(Color.White)
                    .padding(12.dp)
            ) { Text(it) }
        }
    }
}

// ---------------- OTHER SCREENS ----------------
@Composable fun LabsScreen(onBack: () -> Unit) =
    SimpleListScreen("Labs", listOf("Computer Lab : 10:20AM","Physics Lab : 2:30PM","Library : Will update"), onBack)

@Composable fun EventsScreen(onBack: () -> Unit) =
    SimpleListScreen("Events", listOf("Annual Day : dec 17","Sports Day : june 13"), onBack)

@Composable fun AttendanceScreen(onBack: () -> Unit) =
    SimpleListScreen("Attendance", listOf("Present : 110","Absent : 10","Total : 91%"), onBack)

@Composable fun ParentMeetingScreen(onBack: () -> Unit) =
    SimpleListScreen("Parent Meeting", listOf("05 Aug : meeting room","07 Aug : canceled","10 Aug : Auditorium"), onBack)

// ---------------- UPDATES ----------------
@Composable
fun UpdatesScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF0D47A1)).padding(16.dp)) {

        Text("Back", color = Color.White, modifier = Modifier.clickable { onBack() })

        Spacer(Modifier.height(20.dp))

        Text("Mid-Day Meal Updates", color = Color.White)

        listOf("Monday → Rice + Egg","Tuesday → Roti + Dal").forEach {
            Box(
                Modifier.fillMaxWidth().padding(6.dp)
                    .background(Color.White)
                    .padding(10.dp)
            ) { Text(it) }
        }

        Spacer(Modifier.height(20.dp))

        Text("Student Feedback", color = Color.White)

        listOf("Rahul: food ir really good","Anjali: Improved form the paste days").forEach {
            Box(
                Modifier.fillMaxWidth().padding(6.dp)
                    .background(Color.White)
                    .padding(10.dp)
            ) { Text(it) }
        }
    }
}
@Composable
fun GalleryScreen(onBack: () -> Unit) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF0D47A1))
            .padding(12.dp)
    ) {

        Text(
            "Back",
            color = Color.White,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.lab1),
            contentDescription = "Lab Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(3.dp, Color.Black)
        )


        Box(
            Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .background(Color.White)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "washroom",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.lab2),
            contentDescription = "Lab Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(3.dp, Color.Black)
        )


        Box(
            Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .background(Color.White)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Library",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.lab3),
            contentDescription = "Lab Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(3.dp, Color.Black)
        )


        Box(
            Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .background(Color.White)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "computer lab",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------------- PROFILE ----------------
@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Color(0xFF0D47A1)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(40.dp))

        Text("👨‍🎓", fontSize = 50.sp)

        Spacer(Modifier.height(20.dp))

        ProfileBox("Name: PRAJWAL S H")
        ProfileBox("Roll: 1VJ22EC006")
        ProfileBox("SATS: 202617")

        Spacer(Modifier.height(20.dp))

        Box(
            Modifier.background(Color.Black)
                .clickable { onLogout() }
                .padding(15.dp)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun ProfileBox(text: String) {
    Box(
        Modifier.fillMaxWidth(0.8f)
            .padding(6.dp)
            .background(Color.White)
            .padding(10.dp)
    ) {
        Text(text)
    }
}