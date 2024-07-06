package com.example.sigmaenglish

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SigmaEnglishTheme {
                val viewModel: ViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ViewModel::class.java]
                NavigationComponent(viewModel)
            }
        }
    }
}

@Composable
fun NavigationComponent(viewModel: ViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("screen1") { WordListScreen(viewModel, navController) }
        composable("screen2") { TrainingScreen(viewModel, navController) }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Start Screen", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { navController.navigate("screen1") }) {
                Text("Go to Screen 1")
            }
            Button(onClick = { navController.navigate("screen2") }) {
                Text("Go to Screen 2")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordListScreen(viewModel: ViewModel, navController: NavHostController) {
    val wordList by viewModel.words.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Word") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                onClick = { showDialog = true },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        }
                    }
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(wordList) { index, word ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = word.english)
                        Text(text = word.russian)
                    }
                }
            }
        }
    }
    if (showDialog) {
        AddWordDialog(
            viewModel = viewModel,
            onConfirm = { english, russian, description ->
                val word = DBType.Word(
                    english = english,
                    russian = russian,
                    description = description,
                )
                viewModel.addWord(word)
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun TrainingScreen(viewModel: ViewModel, navController: NavHostController) {
    var currentScreen by remember { mutableStateOf("settings") }
    var wordCount by remember { mutableStateOf(0) }

    when (currentScreen) {
        "settings" -> SettingsScreen(
            onStart = {
                    count -> wordCount = count
                currentScreen = "training"
            }, navController
        )
        "training" -> WordTrainingScreen(viewModel, wordCount)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onStart: (Int) -> Unit, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedNumber by remember { mutableIntStateOf(0) }
    var selectedType by remember { mutableStateOf("") }

    val numbers = listOf(10, 25, 50, 100) // List of numbers to display
    val types = listOf("Last 10", "Last 25", "All") // List of types to display
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary,
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            numbers.forEach { number ->
                                Text(
                                    text = number.toString(),
                                    modifier = Modifier

                                        .clickable { selectedNumber = number }
                                        .padding(horizontal = 25.dp),
                                    //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                    color = if (selectedNumber == number) Color.Green else Color.Black,
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Row {
                        types.forEach { type ->
                            Text(
                                text = type,
                                modifier = Modifier
                                    .clickable { selectedType = type }
                                    .padding(horizontal = 25.dp),
                                //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                color = if (selectedType == type) Color.Green else Color.Black,
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        }
                    }
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Start",
                    modifier = Modifier
                        .clickable { onStart(selectedNumber) }
                        .padding(horizontal = 25.dp),

                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
fun WordTrainingScreen(viewModel: ViewModel, wordCount: Int) {
    var isExpanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Description") }

    SigmaEnglishTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    contentColor = Color.LightGray, // Adjust as per your color scheme
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), // Adjust height as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Skip",
                            //modifier = Modifier.clickable(onClick = null)
                            color = Color.Black
                        )
                    }
                }
            }
        ) {
            Text(
                "0/x",
                fontSize = 50.sp,
                modifier = Modifier.padding(26.dp),
                color = colorScheme.primary
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(vertical = 250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text("Word", fontSize = 50.sp)
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    color = /*surfaceColor*/MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(all = 4.dp)
                )
                {
                    Hint(
                        initialText = "Description",
                        expandedText = "This is a test description",
                        icon = Icons.Default.Info
                    )
                }
            }

        }
    }
}

@Composable
fun AddWordDialog(
    viewModel: ViewModel,
    onConfirm: (english: String, russian: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var translationDescription by remember { mutableStateOf("") }
    var englishWord by remember { mutableStateOf("") }
    var russianWord by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Word") },
        text = {
            Column {
                TextField(
                    value = englishWord,
                    onValueChange = { englishWord = it },
                    label = { Text("English") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = russianWord,
                    onValueChange = { russianWord = it },
                    label = { Text("Russian") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = translationDescription,
                    onValueChange = { translationDescription = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(englishWord, russianWord, translationDescription)
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SigmaEnglishTheme {
        StartScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordListScreen() {
    val sampleWords = remember {
        listOf(
            DBType.Word("Hello", "Привет", 3, ""),
            DBType.Word("Goodbye", "До свидания", 2, ""),
            DBType.Word("Thank you", "Спасибо", 1, "")
        )
    }

    SigmaEnglishTheme {
        WordListScreenPreview(
            wordList = sampleWords,
            navController = rememberNavController()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordListScreenPreview(wordList: List<DBType.Word>, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Word") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                onClick = { showDialog = true },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        }
                    }
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(wordList) { index, word ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = word.english)
                        Text(text = word.russian)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewLearning() {
    SigmaEnglishTheme {


        var showDialog by remember { mutableStateOf(false) }
        var selectedNumber by remember { mutableStateOf(0) }
        var selectedType by remember { mutableStateOf("") }

        val numbers = listOf(10, 25, 50, 100) // List of numbers to display
        val types = listOf("Last 10", "Last 25", "All") // List of types to display
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primaryContainer,
                        titleContentColor = colorScheme.primary,
                    ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row {
                                numbers.forEach { number ->
                                    Text(
                                        text = number.toString(),
                                        modifier = Modifier

                                            .clickable { selectedNumber = number }
                                            .padding(horizontal = 25.dp),
                                        //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                        color = if (selectedNumber == number) Color.Green else Color.Black,
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.primary,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Row {
                            types.forEach { type ->
                                Text(
                                    text = type,
                                    modifier = Modifier
                                        .clickable { selectedType = type }
                                        .padding(horizontal = 25.dp),
                                    //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                    color = if (selectedType == type) Color.Green else Color.Black,
                                )
                            }
                        }
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount < -50) { // Swipe right to left
                            }
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Start",
                        modifier = Modifier
                            .clickable { }
                            .padding(horizontal = 25.dp),

                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WordTrainingScreenPreview() {
    var isExpanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Description") }
    var textfield by remember { mutableStateOf("Write your translation here")}
    SigmaEnglishTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    contentColor = Color.LightGray, // Adjust as per your color scheme
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), // Adjust height as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Skip",
                            //modifier = Modifier.clickable(onClick = null)
                            color = Color.Black
                        )
                    }
                }
            }
        ) {
            Text(
                "0/10",
                fontSize = 50.sp,
                modifier = Modifier.padding(26.dp),
                color = colorScheme.primary
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(vertical = 250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text("Word", fontSize = 50.sp)
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    color = /*surfaceColor*/MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(all = 16.dp)
                )
                {
                    Hint(
                        initialText = "Description",
                        expandedText = "This is a test description",
                        icon = Icons.Default.Info
                    )
                }
                HorizontalDivider(modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .padding(vertical = 20.dp))
                TextField(value = textfield, onValueChange = {textfield = it})
            }

        }
    }
}

@Composable
fun Hint(initialText: String, expandedText: String, icon: ImageVector) {
    // State to track expanded state and current text
    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
    val (displayText, setDisplayText) = remember { mutableStateOf(initialText) }

    // Toggle expanded state and change text accordingly
    val onClick: () -> Unit = {
        setExpanded(!isExpanded)
        setDisplayText(if (isExpanded) initialText else expandedText)
    }

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(all = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Info",
            modifier = Modifier.padding(horizontal = 0.dp)
        )
        Text(
            text = displayText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}