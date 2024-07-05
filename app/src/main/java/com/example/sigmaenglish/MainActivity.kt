package com.example.sigmaenglish

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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
        composable("screen2") { TrainingScreen(navController) }
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
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
fun TrainingScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (dragAmount < -50) { // Swipe right to left
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Screen 2", style = MaterialTheme.typography.headlineMedium)
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
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
@Preview(showBackground = true)
@Composable
fun PreviewLearning() {
    SigmaEnglishTheme {
        TrainingScreen(navController = rememberNavController())
    }
}
