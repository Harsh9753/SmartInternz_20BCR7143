package com.example.bloodbankapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodbankapplication.ui.theme.BloodBankApplicationTheme
import java.util.*

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Schedule Ur Donation", fontSize = 20.sp, style = TextStyle(color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    background = Color.White,
                    shadow = Shadow(color = Color.Gray),
                    textAlign = TextAlign.Center)
                )

                Namefield()
                Agefield()
                Phonefield()
                sex()
                DOBDatepicking()
                Row() {
                    DonateDatepicking()
                    Spacer(modifier = Modifier.padding(10.dp))
                    Timepicking()
                }

                Bloodfield()
                Healthfield()
                submit()
            }
        }
    }
}

@Composable
fun Namefield() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var textInput by remember { mutableStateOf("") }
        OutlinedTextField(value = textInput, onValueChange = {textInput = it},
            label = { Text("Name")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray ,
                focusedLabelColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                leadingIconColor = Color.Red
            )
        )
    }
}

@Composable
fun Agefield() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var textInput by remember { mutableStateOf("")}
        OutlinedTextField(value = textInput, onValueChange = {textInput = it},
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {Icon(Icons.Outlined.Star, contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                leadingIconColor = Color.Red
            )
        )
    }
}

@Composable
fun Phonefield() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var textInput by remember { mutableStateOf("")}
        OutlinedTextField(value = textInput, onValueChange = {textInput = it},
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {Icon(Icons.Outlined.Phone, contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                leadingIconColor = Color.Red
            )
        )
    }
}

@Composable
fun sex() {
    val radioOptions = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        radioOptions.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    }
                )
                Text(
                    text = text
                )
            }
        }
    }

}

@Composable
fun DOBDatepicking(){
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth-${mMonth+1}-$mYear"
        }, mYear, mMonth, mDay
    )

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red) ) {
            Text(text = "Date of Birth", color = Color.White)
        }
        Text(text = "       ${mDate.value}", fontSize = 15.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun DonateDatepicking(){
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth-${mMonth+1}-$mYear"
        }, mYear, mMonth, mDay
    )

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red) ) {
            Text(text = "Date of Donation", color = Color.White)
        }
        Text(text = "${mDate.value}", fontSize = 15.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun Timepicking(){
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTime = remember { mutableStateOf("") }
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { mTimePickerDialog.show() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(
            0xFFFF0000
        )
        )) {
            Text(text = "Time of Donation", color = Color.White)
        }
        Text(text = "${mTime.value}", fontSize = 15.sp)
    }
}
@Composable
fun Bloodfield() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var textInput by remember { mutableStateOf("")}
        OutlinedTextField(value = textInput, onValueChange = {textInput = it},
            label = { Text("Blood Group") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {Icon(painter = painterResource(id = R.drawable.egg1), contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                leadingIconColor = Color.Red
            )
        )
    }
}

@Composable
fun Healthfield() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var textInput by remember { mutableStateOf("")}
        OutlinedTextField(value = textInput, onValueChange = {textInput = it},
            label = { Text("Health Description") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {Icon(painter = painterResource(id = R.drawable.ic_baseline_security_24), contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                leadingIconColor = Color.Red
            )
        )
    }
}

@Composable
fun submit() {
    val mContext = LocalContext.current
    Button(onClick = {
        Toast.makeText(mContext, "Scheduled Successfully!!", Toast.LENGTH_LONG).show()
        mContext.startActivity(Intent(mContext, Homepage::class.java))
    },  colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        shape = RoundedCornerShape(20.dp)) {
        Text(text = "Schedule", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
}