package com.example.bloodbankapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.LocusId
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodbankapplication.ui.theme.BloodBankApplicationTheme
import com.example.bloodbankapplication.ui.theme.DrawerBody
import com.example.bloodbankapplication.ui.theme.DrawerHeader
import kotlinx.coroutines.launch

class Homepage : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            Scaffold (
                scaffoldState = scaffoldState,
                topBar = {
                    AppBar (
                        onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    )
                },
                drawerContent = {
                    DrawerHeader()
                    DrawerBody(items = listOf(
                        MenuItem(
                            id = "home",
                            title = "Home",
                            contentDescription = "Go to homescreen",
                            icon = Icons.Default.Home
                        ),
                        MenuItem(
                            id = "profile",
                            title = "Profile",
                            contentDescription = "Go to profile",
                            icon = Icons.Default.Person
                        ),
                        MenuItem(
                            id = "settings",
                            title = "Settings",
                            contentDescription = "Go to settings",
                            icon = Icons.Default.Settings
                        ),
                        MenuItem(
                            id = "info",
                            title = "Info",
                            contentDescription = "Go to info",
                            icon = Icons.Default.Info
                        )
                    ), onItemClick = {
                        println("Clicked on ${it.title}")
                    })
                },
                floatingActionButton = {
                    fab()
                }
            ){

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)){
                    for (index in 0 until 10) {
                        item {
                            ImageCard(painter = painterResource(id = R.drawable.news), contentDescription = "When a disaster strikes, blood is needed immediately, there is no time to wait for donations. Donate today, to prepare for tomorrow. Find where you can donate blood through the Armed Services Blood Program. One donation can save up to three lives.", title = "")
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))
                            ImageCard(painter = painterResource(id = R.drawable.newss), contentDescription = "FDA issues new rules, makes it easier for gay, bisexual men to donate blood", title = "")
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))
                            ImageCard(painter = painterResource(id = R.drawable.newsss), contentDescription = "Nowadays, a large number of people are suffering from diabetes. Due to our lifestyle and stress, the sugar levels in the blood increase. But can a diabetic patient donate blood? Well, let’s find out. According to the Healthline report, a diabetic patient can also donate blood.", title = "")
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        }
                    }
                }
            }





        }
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = 5.dp
    ){
        Box(modifier = Modifier.height(200.dp)) {
            Image(painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.FillWidth, modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ){
                    //Text(title, style = TextStyle(color = Color.White, fontSize = 16.sp))
                    Text(contentDescription, style = TextStyle(color = Color.White, fontSize = 16.sp))
                }
            }

        }
    }
}

@Composable
fun fab(){
    val context = LocalContext.current
    ExtendedFloatingActionButton(
        text = {
            Text(text = "Donate Ur Blood", color = Color.White)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_egg_24),
                contentDescription = "Navigate FAB",
                tint = Color.White,
            )
        },
        onClick = {
            context.startActivity(Intent(context, Profile::class.java))
        },
        backgroundColor = Color.Red
    )
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {

}