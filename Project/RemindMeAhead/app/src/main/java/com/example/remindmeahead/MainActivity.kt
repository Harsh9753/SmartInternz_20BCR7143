package com.example.remindmeahead

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remindmeahead.NotifyWork.Companion.NOTIFICATION_WORK
import com.example.remindmeahead.database.Event
import com.example.remindmeahead.database.MainViewModel
import com.example.remindmeahead.ui.theme.AppTheme
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var checkNotificationPermission: ActivityResultLauncher<String>
    private var isPermission = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val mainViewModel: MainViewModel by viewModels()


        setContent {
            AppTheme {
                val getPermision = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){
                        isGenerate->
                    if(isGenerate){
                    }else{
                    }
                }
                SideEffect {
                    getPermision.launch(Manifest.permission.SEND_SMS)
                    //getPermision.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    HomeScaffold(mainViewModel)
                    var data=mainViewModel.allData.collectAsState(initial = listOf())
                    val dates1=convertStateToList(data)
                    callWorkManager(dates1)
                    checkPermission()
                }
            }
        }
    }
    fun convertStateToList(state: State<List<Event>>): List<Event> {
        return state.value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun changeYear(newYear :String, dateString :String):String{
        val newDateString = newYear + dateString.substring(4)

        return newDateString
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun callWorkManager(dates:List<Event>){
        val currentYear = LocalDate.now().year.toString()
        for (date in dates){
            if(date.toRemind != "null"){
                val newDate=changeYear(currentYear,date.toRemind)

                scheduleNotification(strdate = newDate, number = date.number, notesToSent = date.notesToSend, cat = date.category, sent = date.sent, fname = date.fname)
            }
            else{
                val newDate=changeYear(currentYear,date.date)

                scheduleNotification(strdate = newDate, number = date.number, notesToSent = date.notesToSend, cat = date.category, sent = date.sent, fname = date.fname)
            }
            }
    }
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                isPermission = true
            } else {
                isPermission = false

                checkNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            isPermission = true
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotification(strdate:String, number:String, notesToSent:String, sent:Boolean, cat:String, fname:String) {
        val currentTime = Date()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = format.parse(strdate)
        val targetTime=Date(date.year,date.month,date.date,8,0,0)
        val data = Data.Builder()
            .putString("date", format.format(date))
            .putString("number", number)
            .putString("notesToSent", notesToSent)
            .putString("category",cat)
            .putString("fname",fname)
            .putBoolean("sent",sent)
            .build()
        val delay=targetTime.time-currentTime.time

        var delay1:Long
        if(delay>0){
            delay1=delay
        }
        else{
            val newTargetTime=Date(date.year+1,date.month,date.date,8,0,0)
            delay1=newTargetTime.time-currentTime.time
        }

        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay1, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }
}

enum class Screens {
    Home,
    Add,
    Edit
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScaffold(mainViewModel: MainViewModel) {

    val navController = rememberNavController()
    var state by remember {
        mutableStateOf(true)
    }
    Scaffold(floatingActionButton = {
        if (state) {
            FloatingActionButton(onClick = {
                state = !state
                navController.navigate(route = Screens.Add.name)
            }) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }) { padding ->
        NavHost(navController = navController, startDestination = Screens.Home.name) {
            composable(route = Screens.Home.name) {
                state = true
                LandingScreen(padding, mainViewModel, navController)
            }
            composable(route = Screens.Add.name) {
                AddScreen(padding, LocalContext.current, mainViewModel, navController)
                state = false
            }
            composable(route = Screens.Edit.name + "/{eId}") {
                val eId = it.arguments?.getString("eId")
                if (eId != null) {
                    EditScreen(padding, mainViewModel, navController, eId)
                }
                state = false
            }
        }
    }
}

@Composable
fun LandingScreen(
    padding: PaddingValues, mainViewModel: MainViewModel, navController: NavHostController
) {
    var category by remember {
        mutableStateOf("")
    }
    var eventList = mainViewModel.allData.collectAsState(listOf())
    if(category.isNotBlank()){
        eventList=mainViewModel.allCat(category).collectAsState(initial = listOf())
    }
    ConstraintLayout(Modifier.padding(padding)) {
        val (radio, list) = createRefs()
        val options = listOf("Birthday", "Wedding", "MemorialDay", "OtherEvents")
        LazyRow(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .constrainAs(radio) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            items(count = 4) {
                if (category == options[it]) {
                    Button(
                        onClick = {
                            category = if (category == options[it]) {
                                ""
                            } else {
                                options[it]
                            }
                        }, modifier = Modifier
                            .height(50.dp)
                            .width(150.dp)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = options[it])
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            category = options[it]
                        }, modifier = Modifier
                            .height(50.dp)
                            .width(150.dp)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = options[it])
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier
            .padding(vertical = 30.dp)
            .constrainAs(list) {
                top.linkTo(radio.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 10.dp)
            }) {

            items(eventList.value.size) { index ->
                val event = eventList.value[index]

                Content(event, mainViewModel, navController)

            }
        }
    }
}

@Composable
fun Content(event: Event, mainViewModel: MainViewModel, navController: NavHostController) {
    var deleteConfirm by remember {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .padding(10.dp)
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (categ, fn, dt, nt, icns) = createRefs()

            Row(Modifier.constrainAs(categ) {
                top.linkTo(parent.top, margin = 15.dp)
                start.linkTo(parent.start, margin = 10.dp)
            }) {
                Text(
                    text = event.category,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
            Row(Modifier.constrainAs(fn) {
                top.linkTo(categ.bottom, margin = 5.dp)
                start.linkTo(parent.start, margin = 10.dp)
            }) {
                Text(text = "Name: ", color = MaterialTheme.colorScheme.primary)
                Text(text = event.fname + " ")
                Text(text = event.lname)
            }
            Row(Modifier.constrainAs(dt) {
                top.linkTo(fn.bottom, margin = 5.dp)
                start.linkTo(parent.start, margin = 10.dp)
            }) {
                Text(text = "Date of event: ", color = MaterialTheme.colorScheme.primary)
                Text(text = event.date)
            }
            Text(
                text = if (event.note.length > 35) {
                    event.note.substring(0, 30) + "..."
                } else {
                    event.note
                },
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                modifier = Modifier
                    .constrainAs(nt) {
                        top.linkTo(dt.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 15.dp)
                        end.linkTo(icns.start, margin = 15.dp)
                        width = Dimension.fillToConstraints
                    })
            Column(Modifier.constrainAs(icns) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 5.dp)
            }) {
                IconButton(onClick = {
                    deleteConfirm = true

                }) {
                    Icon(Icons.Default.Delete, "delete")
                }
                IconButton(onClick = {
                    val eId = event.eid
                    navController.navigate(route = Screens.Edit.name + "/$eId")
                }) {
                    Icon(Icons.Default.Edit, "edit")
                }
            }
        }
        if (deleteConfirm) {
            AlertDialog(onDismissRequest = { deleteConfirm = false }, confirmButton = {
                Button(onClick = {
                    deleteConfirm = false
                    mainViewModel.deleteEvent(event)
                }, shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Delete")
                }
            }, dismissButton = {
                TextButton(onClick = { deleteConfirm = false }) {
                    Text(text = "Cancel")
                }
            }, title = {
                Text(text = "Delete Entry?")
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    padding: PaddingValues,
    context: Context,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        val (type, fnm, lnm, cal, note, buttons) = createRefs()
        val options = listOf("Birthday", "Wedding", "MemorialDay", "OtherEvents")
        val times = listOf("1 day", "2 days", "3 days", "1 week", "2 weeks")
        val placeHolders = listOf(
            "Happy Birthday",
            "Happy wedding day",
            "My Deepest Condolences",
            "Remind the event"
        )
        val vChain =
            createVerticalChain(type, fnm, lnm, cal, note, buttons, chainStyle = ChainStyle.Spread)

        var ddm by remember {
            mutableStateOf(false)
        }
        var dddm by remember {
            mutableStateOf(false)
        }
        var eventCategory by remember {
            mutableStateOf("")
        }
        var firstName by remember {
            mutableStateOf("")
        }
        var lastName by remember {
            mutableStateOf("")
        }
        var eventNote by remember {
            mutableStateOf("")
        }
        var phNumber by remember {
            mutableStateOf("")
        }

        var notesToSend by remember {
            mutableStateOf("")
        }
        var remindSelect by remember {
            mutableStateOf("")
        }
        var moreOptions by remember {
            mutableStateOf(false)
        }
        var sent by remember {
            mutableStateOf(false)
        }
        var isDialogShown: Boolean by rememberSaveable {
            mutableStateOf(false)
        }
        var remindMeAt: LocalDate? by remember {
            mutableStateOf(null)
        }
        var date: LocalDate? by remember {
            mutableStateOf(null)
        }

        if (isDialogShown) {
            DatePickerDialog(
                onDismissRequest = { isDialogShown = false },
                onDateChange = {
                    date = it
                    isDialogShown = false
                },

                title = { Text(text = "Select date") }
            )
        }

        ExposedDropdownMenuBox(expanded = ddm,
            onExpandedChange = { ddm = !ddm },
            modifier = Modifier
                .constrainAs(type) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = eventCategory,
                onValueChange = { eventCategory = it },
                label = { Text(text = "Event Type") },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            if (ddm) {
                                Icons.Filled.KeyboardArrowUp
                            } else {
                                Icons.Filled.KeyboardArrowDown
                            }, contentDescription = ""
                        )
                    }
                })
            ExposedDropdownMenu(expanded = ddm, onDismissRequest = { ddm = false }) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(text = { Text(text = selectionOption) }, onClick = {
                        eventCategory = selectionOption
                        ddm = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            singleLine = true,
            label = { Text(text = "First name") },
            modifier = Modifier.constrainAs(fnm) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            singleLine = true,
            label = { Text(text = "Last name") },
            modifier = Modifier.constrainAs(lnm) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Button(onClick = {
            isDialogShown = true
        }, shape = RoundedCornerShape(10.dp), modifier = Modifier
            .width(175.dp)
            .constrainAs(cal) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Text(text = "Set Date")
        }

        OutlinedTextField(value = eventNote,
            onValueChange = { eventNote = it },
            label = { Text(text = "Additional Note") },
            modifier = Modifier
                .height(225.dp)
                .constrainAs(note) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(buttons) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            val (more, confirm) = createRefs()
            val hChain = createHorizontalChain(more, confirm, chainStyle = ChainStyle.SpreadInside)
            OutlinedButton(
                onClick = { moreOptions = true }, modifier = Modifier
                    .padding(start = 35.dp)
                    .constrainAs(more) {}, shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "More Options")
            }
            Button(
                onClick = {
                    if(date!=null){mainViewModel.addEvent(
                        Event(
                            fname = firstName,
                            lname = lastName,
                            note = eventNote,
                            category = eventCategory,
                            date = date.toString(), toRemind = date.toString(),
                        )
                    )}
                    else{
                        Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show()
                    }
                    navController.navigate(route = Screens.Home.name)
                }, modifier = Modifier
                    .padding(end = 35.dp)
                    .constrainAs(confirm) {}, shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Confirm")
            }
        }

        if (moreOptions) {
            AlertDialog(onDismissRequest = {
                moreOptions = false
                phNumber = ""
                remindMeAt = null
            }, confirmButton = {
                Button(onClick = {
                    moreOptions = false
                    if (phNumber != "" && phNumber.length == 10) {

                        sent = true
                        mainViewModel.addEvent(
                            Event(
                                fname = firstName,
                                lname = lastName,
                                note = eventNote,
                                category = eventCategory,
                                date = date.toString(), toRemind = remindMeAt.toString(),
                                sent = sent,
                                number = phNumber,
                                notesToSend = notesToSend
                            )
                        )
                        navController.navigate(route = Screens.Home.name)
                    } else {
                        Toast.makeText(context, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
                    }
                }, shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Send SMS")
                }
            }, dismissButton = {
                TextButton(onClick = {
                    moreOptions = false
                    phNumber = ""
                    if(date!=null){mainViewModel.addEvent(
                        Event(
                            fname = firstName,
                            lname = lastName,
                            note = eventNote,
                            category = eventCategory,
                            date = date.toString(), toRemind = remindMeAt.toString(),
                        )
                    )}
                    else{

                        Toast.makeText(context, "Invalid date", Toast.LENGTH_SHORT).show()
                    }
                    navController.navigate(route = Screens.Home.name)
                }) {
                    Text(text = "Remind Only")
                }
            }, title = {
                Text(text = "Add details")
            }, text = {
                ConstraintLayout {
                    val (num, tim, tim2) = createRefs()
                    val mvChain =
                        createVerticalChain(num, tim, tim2, chainStyle = ChainStyle.SpreadInside)
                    ExposedDropdownMenuBox(expanded = dddm,
                        onExpandedChange = { dddm = !dddm }, modifier = Modifier
                            .padding(10.dp)
                            .constrainAs(num) {}) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            value = remindSelect,
                            onValueChange = { remindSelect = it },
                            label = { Text(text = "Remind Me Ahead") },
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        if (dddm) {
                                            Icons.Filled.KeyboardArrowUp
                                        } else {
                                            Icons.Filled.KeyboardArrowDown
                                        }, contentDescription = ""
                                    )
                                }
                            })
                        ExposedDropdownMenu(expanded = dddm, onDismissRequest = { dddm = false }) {
                            times.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(text = selectionOption) },
                                    onClick = {
                                        if (date != null) {
                                            when (selectionOption) {
                                                times[0] -> {
                                                    remindSelect = times[0]
                                                    remindMeAt = (date!!.minusDays(1))
                                                }
                                                times[1] -> {
                                                    remindSelect = times[1]
                                                    remindMeAt = (date!!.minusDays(2))
                                                }
                                                times[2] -> {
                                                    remindSelect = times[2]
                                                    remindMeAt = (date!!.minusDays(3))
                                                }
                                                times[3] -> {
                                                    remindSelect = times[3]
                                                    remindMeAt = (date!!.minusDays(7))
                                                }
                                                times[4] -> {
                                                    remindSelect = times[4]
                                                    remindMeAt = (date!!.minusDays(14))
                                                }
                                            }
                                            dddm = false
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Date not Selected",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }
                        }
                    }

                    OutlinedTextField(
                        value = phNumber,
                        onValueChange = { phNumber = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Mobile Number") },
                        placeholder = {
                            Text(
                                text = "Optional"
                            )
                        }, modifier = Modifier
                            .padding(10.dp)
                            .constrainAs(tim) {})
                    OutlinedTextField(
                        value = notesToSend,
                        onValueChange = { notesToSend = it },
                        label = { Text("Notes To Send") },
                        placeholder = {
                            when (eventCategory) {
                                "Birthday" -> {
                                    notesToSend = placeHolders[0]
                                }
                                "Wedding" -> {
                                    notesToSend = placeHolders[1]
                                }
                                "MemorialDay" -> {
                                    notesToSend = placeHolders[2]
                                }
                                "OtherEvents" -> {
                                    notesToSend = placeHolders[3]
                                }
                            }
                            Text(
                                text = notesToSend
                            )
                        }, modifier = Modifier
                            .padding(10.dp)
                            .constrainAs(tim2) {})
                }
            })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    padding: PaddingValues,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    eId: String
) {
    var x = 0
    val eValue = eId.toInt()
    val eventList = mainViewModel.allData.collectAsState(initial = listOf())
    var eCateg by remember {
        mutableStateOf("")
    }
    var eFName by remember {
        mutableStateOf("")
    }
    var eLName by remember {
        mutableStateOf("")
    }
    var eNote by remember {
        mutableStateOf("")
    }
    var eDate by remember {
        mutableStateOf("")
    }
    var eRemindDate by remember {
        mutableStateOf("")
    }
    var eNumber by remember {
        mutableStateOf("")
    }
    var eNNote by remember {
        mutableStateOf("")
    }
    var eMessage by remember {
        mutableStateOf(false)
    }
    while (x < eventList.value.size) {
        if (eventList.value[x].eid == eValue) {
            eCateg = (eventList.value[x].category)
            eFName = (eventList.value[x].fname)
            eLName = (eventList.value[x].lname)
            eNote = (eventList.value[x].note)
            eDate = (eventList.value[x].date)
            eRemindDate = (eventList.value[x].toRemind)
            eMessage=(eventList.value[x].sent)
            eNNote=(eventList.value[x].notesToSend)
            eNumber=(eventList.value[x].number)
        }
        x++
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val (type, fnm, lnm, cal, rem, note, nnote, num, buttons) = createRefs()
        val options = listOf("Birthday", "Wedding", "MemorialDay", "OtherEvents")
        var isDialogShown: Boolean by rememberSaveable {
            mutableStateOf(false)
        }
        var isDialog2Shown: Boolean by rememberSaveable {
            mutableStateOf(false)
        }
        var ddm by remember {
            mutableStateOf(false)
        }
        val evChain =
            createVerticalChain(
                type,
                fnm,
                lnm,
                cal,
                rem,
                note,
                num,
                nnote,
                buttons,
                chainStyle = ChainStyle.Spread
            )

        if (isDialogShown) {
            DatePickerDialog(
                onDismissRequest = { isDialogShown = false },
                onDateChange = {
                    eDate = it.toString()
                    isDialogShown = false
                },
                title = { Text(text = "Select the date of the event") }
            )
        }
        if (isDialog2Shown) {
            DatePickerDialog(
                onDismissRequest = { isDialog2Shown = false },
                onDateChange = {
                    eRemindDate = it.toString()
                    isDialog2Shown = false
                },
                title = { Text(text = "Select a date for the reminder") }
            )
        }

        ExposedDropdownMenuBox(expanded = ddm,
            onExpandedChange = { ddm = !ddm },
            modifier = Modifier
                .constrainAs(type) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = eCateg,
                onValueChange = { eCateg = it },
                label = { Text(text = "Event Type") },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            if (ddm) {
                                Icons.Filled.KeyboardArrowUp
                            } else {
                                Icons.Filled.KeyboardArrowDown
                            }, contentDescription = ""
                        )
                    }
                })
            ExposedDropdownMenu(expanded = ddm, onDismissRequest = { ddm = false }) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(text = { Text(text = selectionOption) }, onClick = {
                        eCateg = selectionOption
                        ddm = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = eFName,
            onValueChange = { eFName = it },
            singleLine = true,
            label = { Text(text = "First name") },
            modifier = Modifier.constrainAs(fnm) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        OutlinedTextField(
            value = eLName,
            onValueChange = { eLName = it },
            singleLine = true,
            label = { Text(text = "Last name") },
            modifier = Modifier.constrainAs(lnm) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .wrapContentHeight()
                .constrainAs(cal) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            val (date, remind) = createRefs()
            val ehChain =
                createHorizontalChain(date, remind, chainStyle = ChainStyle.SpreadInside)
            Button(onClick = {
                isDialogShown = true
            }, shape = RoundedCornerShape(10.dp), modifier = Modifier
                .width(140.dp)
                .constrainAs(date) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Text(text = "Event Date")
            }
            Button(onClick = {
                isDialog2Shown = true
            }, shape = RoundedCornerShape(10.dp), modifier = Modifier
                .width(140.dp)
                .constrainAs(remind) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Text(text = "Reminder Date")
            }
        }

        OutlinedTextField(value = eNumber,
            onValueChange = { eNumber = it },
            label = { Text(text = "Phone Number") },
            singleLine = true,
            trailingIcon = {
                Row {
                    Icon(Icons.Filled.Email, contentDescription = "", modifier = Modifier.align(CenterVertically))
                    RadioButton(selected = eMessage, onClick = { eMessage = !eMessage })
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .constrainAs(note) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        OutlinedTextField(value = eNNote,
            onValueChange = { eNNote = it },
            label = { Text(text = "SMS Note") },
            modifier = Modifier
                .constrainAs(num) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        OutlinedTextField(value = eNote,
            onValueChange = { eNote = it },
            label = { Text(text = "In-App Note") },
            modifier = Modifier
                .constrainAs(nnote) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        Button(onClick = {
            mainViewModel.updateEvent(
                event = Event(
                    eid = eValue,
                    category = eCateg,
                    fname = eFName,
                    lname = eLName,
                    note = eNote,
                    date = eDate,
                    toRemind = eRemindDate,
                    number = eNumber,
                    notesToSend = eNNote,
                    sent = eMessage
                )
            )

            navController.navigate(route = Screens.Home.name)
        }, shape = RoundedCornerShape(10.dp), modifier = Modifier
            .width(175.dp)
            .constrainAs(buttons) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Text(text = "Submit")
        }
    }
}