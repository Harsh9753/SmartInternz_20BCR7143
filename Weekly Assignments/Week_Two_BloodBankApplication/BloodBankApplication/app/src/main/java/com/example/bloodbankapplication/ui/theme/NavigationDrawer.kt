package com.example.bloodbankapplication.ui.theme

import android.view.Menu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodbankapplication.MenuItem

class NavigationDrawer {

}
@Composable
fun DrawerHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center){
        Column() {
            Text(text = "UrBLOOD", fontSize = 30.sp, style = TextStyle(color = Color.Red,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                background = Color.White,
                shadow = Shadow(color = Color.Gray),
                textAlign = TextAlign.Center)
            )

            Text(text = "Be A Donar", fontSize = 20.sp, style = TextStyle(color = Color.Red,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                background = Color.White,
                shadow = Shadow(color = Color.Gray),
                textAlign = TextAlign.Center)
            )
        }
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
){
    LazyColumn(modifier){
        this.items(items){ item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(16.dp)
            ){
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title,style = itemTextStyle, modifier = Modifier.weight(1f))
            }

        }
    }
}