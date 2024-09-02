package com.example.sigmaenglish.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sigmaenglish.Database.DBType
import com.example.sigmaenglish.navigation.NavigationComponent
import com.example.sigmaenglish.navigation.convertWordsToJson
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.ui.theme.customText
import com.example.sigmaenglish.ui.theme.customTitle
import com.example.sigmaenglish.viewModel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


data class TestWord(
    val english: String,
    val russian: String,
    val description: String,
    var isCorrect: Boolean = false
)
data class TemplateWord(val original: String, var translation: String, val description: String)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SigmaEnglishTheme {
                /*val viewModel: ViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                )[ViewModel::class.java]*/
               // val viewModel: ViewModel = viewModel()
                val viewModel: ViewModel by viewModels()
                val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
                NavigationComponent(viewModel)
            }
        }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    SigmaEnglishTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primary),
            contentAlignment = Alignment.Center,

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Start Screen", style = typography.headlineMedium, color = colorScheme.secondary)
                Button(onClick = { navController.navigate("WordListScreen") }, colors = customButtonColors()) {
                    Text("Words List")
                }
                Button(onClick = { navController.navigate("trainingMenu") }, colors = customButtonColors()) {
                    Text("Training")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordListScreen(viewModel: ViewModel, navController: NavHostController) {
    val wordList by viewModel.words.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<DBType.Word?>(null) }
    var importFromNotesDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.secondary,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Words list", modifier = Modifier.padding(vertical = 10.dp))
                        Row {
                            IconButton(onClick = {
                                importFromNotesDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "import from notes")
                            }
                            IconButton(onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }) {
                                Icon(Icons.Default.Home, contentDescription = "Exit")
                            }
                        }
                    }
                }

            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                contentColor = colorScheme.secondary,
                containerColor = colorScheme.tertiary,
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
                    detectHorizontalDragGestures { _, dragAmount ->
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
                item {
                    Row(modifier = Modifier
                        .background(colorScheme.tertiary)
                        .fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                        TableCellHeader(text = "Original", weight = 1f)
                        TableCellHeader(text = "Translation", weight = 1f)
                    }
                }
                itemsIndexed(wordList) { _, word ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { isExpanded = !isExpanded },
                                onLongClick = { selectedWord = word }
                            ),
                        horizontalAlignment = Alignment.Start,
                        //verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TableCell(text = word.english, weight = 1f)
                            TableCell(text = word.russian, weight = 1f)
                        }
                        Crossfade(targetState = isExpanded, animationSpec = tween(durationMillis = 300)) { expanded ->
                            if (expanded) {
                                Row(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        modifier = Modifier.padding(horizontal = 0.dp)
                                    )
                                    Text(
                                        text = word.description,
                                        style = typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                    // Divider line between rows
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
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
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
    if (importFromNotesDialog) {
        ImportWordsDialog(
            onConfirm = { parsedList ->
                parsedList.forEach { word ->
                    val newWord = DBType.Word(
                        english = word.original,
                        russian = word.translation,
                        description = word.description,
                    )
                    viewModel.addWord(newWord)
                    showDialog = false
                }
            },
            onDismiss = { importFromNotesDialog = false }
        )
    }

    selectedWord?.let { word ->
        WordManagementDialog(
            word = word,
            onDelete = {
                viewModel.deleteWord(word)
                selectedWord = null
            },
            onUpdate = { updatedWord ->
                viewModel.updateWord(updatedWord)
                selectedWord = null
            },
            onDismiss = { selectedWord = null }
        )
    }

}

@Composable
fun TrainingMenu(viewModel: ViewModel, navController: NavHostController) {
    var selectedScreen by remember { mutableStateOf("") }
    SigmaEnglishTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Example cards for different modes
            Text("Select a mode:", style = typography.headlineMedium, color = colorScheme.secondary)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    ModeCard(
                        mode = "Classic  \uD83E\uDDD0",
                        selectedScreen = selectedScreen,
                        onSelect = { selectedScreen = "Classic" },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ModeCard(
                        mode = "Mistakes practise  ❌",
                        selectedScreen = selectedScreen,
                        onSelect = { selectedScreen = "Mistakes" },
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp).padding(vertical = 16.dp)
                    ModeCard(
                        mode = "Description  \uD83D\uDCDD",
                        selectedScreen = selectedScreen,
                        onSelect = { selectedScreen = "Description" },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ModeCard(
                        mode = "Zen  \uD83C\uDF43",
                        selectedScreen = selectedScreen,
                        onSelect = { selectedScreen = "Zen" },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Animated content based on selectedScreen
            AnimatedContent(
                targetState = selectedScreen,
                transitionSpec = {
                    slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                }
            ) { screen ->
                when (screen) {
                    "Classic" -> navController.navigate("settings/Classic")
                    "Mistakes" -> navController.navigate("settings/Mistakes")
                    "Description" -> navController.navigate("settings/Description")
                    "Zen" -> navController.navigate("WordTrainingScreenZen")
                    else -> { /* Handle default case or additional screens */
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: ViewModel, navController: NavHostController, trainingType: String) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedNumber by remember { mutableIntStateOf(10) }
    var selectedType by remember { mutableStateOf("All") }
    val numbers = listOf(10, 25, 50, 100) // List of numbers to display
    val types = listOf("Last 10", "Last 25", "All") // List of types to display
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.tertiary,
                    titleContentColor = colorScheme.secondary,
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
                                        .clickable {
                                            if (selectedType != "All" && selectedType != "") {
                                                showDialog = true
                                            } else {
                                                selectedNumber = number
                                            }
                                        }
                                        .padding(horizontal = 25.dp),
                                    color = if (selectedNumber == number) colorScheme.secondary else Color.Black,
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.tertiary,
                contentColor = colorScheme.secondary,
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
                                    .clickable {
                                        selectedType = type
                                        if (selectedType != "All") {
                                            if (selectedType == "Last 10") {
                                                selectedNumber = 10
                                            } else {
                                                selectedNumber = 25
                                            }
                                        }

                                    }
                                    .padding(horizontal = 25.dp),
                                //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                color = if (selectedType == type) colorScheme.secondary else Color.Black,
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
                    detectHorizontalDragGestures { _, dragAmount ->
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
                    modifier = Modifier.clickable {
                        val mockList : List<TestWord> = emptyList()
                        when (trainingType) {
                            "Classic" -> {
                                navController.navigate("WordTrainingScreen/$selectedNumber/$selectedType/$mockList/Classic")
                            }
                            "Mistakes" -> {
                                navController.navigate("WordTrainingScreen/$selectedNumber/$selectedType/$mockList/Mistakes")
                            }
                            "Description" -> {
                                navController.navigate("WordTrainingScreenDescription/$selectedNumber/$selectedType")
                            }
                        }

                    }
                )
            }

        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Alert") },
                text = { Text("Number-specific mode is selected, unable to change the number") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WordTrainingScreen(
    viewModel: ViewModel,
    navController: NavHostController,
    wordCount: Int,
    type: String,
    wordsRefresh: List<TestWord>? = null,
    WordSourse: String
) {
    val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
    val (isHintExpanded2, setHintExpanded2) = remember { mutableStateOf(false) }
    var currentWordIndex: Int by remember { mutableIntStateOf(0) }
    val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
    val wordListFailed by viewModel.wordsFailed.observeAsState(emptyList())
    var words by remember { mutableStateOf(emptyList<TestWord>()) }
    var isSourceEmpty by remember { mutableStateOf(false) }
    val startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = System.currentTimeMillis() - startTime
            delay(1000L)
        }
    }
    val wordLimit = when (type) {
        "last10" -> 10
        "last25" -> 25
        else -> wordCount
    }
    LaunchedEffect(Unit) {
        delay(500L)
        words = when {
            !wordsRefresh.isNullOrEmpty() && WordSourse == "Refresh" -> {
                wordsRefresh
            }
            WordSourse == "Classic" -> {
                val shuffledWords = wordList.map { TestWord(it.english, it.russian, it.description, true) }.shuffled()
                shuffledWords.takeLast(wordLimit)
            }
            else -> {
                val shuffledWords = wordListFailed.map { TestWord(it.english, it.russian, it.description, true) }.shuffled()
                shuffledWords.take(wordCount)
            }
        }

        Log.d("WordTraining", "Words initialized $words, words failed $wordListFailed")
    }

    val onClick: () -> Unit = {
        if ((currentWordIndex + 1) < words.size) {
            currentWordIndex++
        }
    }

    var isAlertDialogEnabled by remember { mutableStateOf(false) }

    var textfield by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val isKeyboardVisible = rememberKeyboardVisibilityObserver()
    val focusManager = LocalFocusManager.current

    val shake = remember { Animatable(0f) }
    var trigger by remember { mutableLongStateOf(0L) }
    LaunchedEffect(trigger) {
        if (trigger != 0L) {
            for (i in 0..10) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, spring(stiffness = 100_000f))
                    else -> shake.animateTo(-5f, spring(stiffness = 100_000f))
                }
            }
            shake.animateTo(0f)
        }
    }

    if (words.isEmpty()) {
        if(WordSourse == "Classic" && viewModel.isInitialized){
            isSourceEmpty = viewModel.words.value.isNullOrEmpty()
        }
        else if (WordSourse == "Mistakes" && viewModel.isInitialized){
            isSourceEmpty = viewModel.wordsFailed.value.isNullOrEmpty()
        }
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        Log.d("Loading screen\"", "Words didn't initalize, source is set to $WordSourse, isSourceEmpty is $isSourceEmpty")
    }
    else {
        SigmaEnglishTheme {
            Scaffold(
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            isAlertDialogEnabled = true
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = colorScheme.tertiary,
                        contentColor = colorScheme.secondary
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), // Adjust height as needed
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Skip",
                                modifier = Modifier.clickable(onClick = {
                                    if (currentWordIndex + 1 == words.size) {
                                        if (WordSourse == "failedTraining") {
                                            viewModel.decrementTraining(words[currentWordIndex].english)
                                        }
                                        words[currentWordIndex].isCorrect = false
                                        navController.navigate("ResultsScreen/${elapsedTime / 1000}/$type/${convertWordsToJson(words)}/Classic")
                                    } else {
                                        if (WordSourse == "failedTraining") {
                                            viewModel.decrementTraining(words[currentWordIndex].english)
                                        }
                                        setHintExpanded(false)
                                        words[currentWordIndex].isCorrect = false
                                        onClick()
                                    }
                                }),
                                color = colorScheme.secondary
                            )
                        }
                    }
                }
            ) {
                LaunchedEffect(isKeyboardVisible.value) {
                    if (!isKeyboardVisible.value) {
                        focusManager.clearFocus()
                    }
                }
                Text(
                    "${currentWordIndex + 1}/${words.size}",
                    fontSize = 50.sp,
                    modifier = Modifier.padding(26.dp),
                    color = colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        targetState = currentWordIndex,
                        transitionSpec = {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut()
                            )
                        }

                    ) { index ->
                        Text(words[index].english, fontSize = 50.sp, textAlign = TextAlign.Center, color = colorScheme.secondary,
                            modifier = Modifier.offset { IntOffset(shake.value.roundToInt(), y = 0) }
                        )
                    }

                        Column(Modifier.padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally){
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                shadowElevation = 1.dp,
                                color = colorScheme.surface,
                                modifier = Modifier
                                    .animateContentSize()
                                    .padding(all = 16.dp)
                            ){
                            Hint(
                            iconUsed = true,
                            initialText = "Description",
                            expandedText = words[currentWordIndex].description,
                            icon = Icons.Default.Info,
                            isExpanded = isHintExpanded,
                            onExpandChange = setHintExpanded
                            )
                            }
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                shadowElevation = 1.dp,
                                color = colorScheme.surface,
                                modifier = Modifier
                                    .animateContentSize()
                                    .padding(all = 2.dp)
                            ) {
                                Hint(
                                    iconUsed = false,
                                    initialText = "\uD83E\uDD37   See answer",
                                    expandedText = words[currentWordIndex].russian,
                                    icon = Icons.Default.Info,
                                    isExpanded = isHintExpanded2,
                                    onExpandChange = setHintExpanded2
                                )
                            }
                        }
                    TextField(
                        value = textfield,
                        onValueChange = { textfield = it },
                        label = { Text("Write your translation here", color = Color.White) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (checkAnswer(textfield, words[currentWordIndex].russian)) {
                                        if (WordSourse == "failedTraining") {
                                            viewModel.incrementTraining(words[currentWordIndex].english)
                                        }
                                        setHintExpanded(false)
                                        if (currentWordIndex + 1 == words.size) {
                                            navController.navigate("ResultsScreen/${elapsedTime / 1000}/$type/${convertWordsToJson(words)}/Classic")
                                        }
                                        textfield = ""
                                        onClick()
                                    } else {
                                        if (WordSourse == "failedTraining") {
                                            viewModel.decrementTraining(words[currentWordIndex].english)
                                        }
                                        trigger = System.currentTimeMillis()
                                        words[currentWordIndex].isCorrect = false
                                    }
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = colorScheme.secondary)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.White // Change cursor color to white
                        )
                    )
                }
            }
            if (isAlertDialogEnabled) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
                    containerColor = colorScheme.tertiary,
                    onDismissRequest = { isAlertDialogEnabled = false },
                    title = { Text("Are you sure?") },
                    text = {
                        Text("Are you sure you wanna close this window? Your progress will be lost.")
                    },
                    confirmButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                isAlertDialogEnabled = false
                            }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
    if (isSourceEmpty) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
            containerColor = colorScheme.tertiary,
            onDismissRequest = { navController.navigate("start") {
                popUpTo("start") { inclusive = true }
            }},
            title = { Text("No words to form training on!") },
            text = {
                if(WordSourse == "Classic"){
                    Text("To form a training list, you should first add some words.\n" +
                            " Would you like to be navigated to Word list screen to add some new words?")
                }
                else{
                    Text("You quite literally have no mistakes to correct, as of now.\n" +
                            " For now, keep up the good work!\nBut test to learn frequently" +
                            " failed words can't be generated," +
                            " for obvious reasons")
                }
            },
            confirmButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("WordListScreen") {
                            popUpTo("WordListScreen") { inclusive = true }
                        }
                    }) {
                    Text("Move to Word list screen")
                }
            }
            ,
            dismissButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }) {
                    Text("Move to start screen")
                }
            }
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WordTrainingScreenZen(
    viewModel: ViewModel,
    navController: NavHostController
) {
    val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
    val (isHintExpanded2, setHintExpanded2) = remember { mutableStateOf(false) }
    var currentWordIndex: Int by remember { mutableIntStateOf(0) }
    var earnedScore: Int by remember { mutableStateOf(0) }
    val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
    var resultList by remember { mutableStateOf(emptyList<TestWord>()) }
    var firstTry by remember { mutableStateOf(true) }
    var words by remember { mutableStateOf(emptyList<TestWord>()) }
    var isSourceEmpty by remember { mutableStateOf(false) }
    val startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = System.currentTimeMillis() - startTime
            delay(1000L)
        }
    }
    LaunchedEffect(Unit) {
        delay(500L)
        words =  wordList.map { TestWord(it.english, it.russian, it.description, true) }.shuffled()
        Log.d("WordTraining", "Words initialized $words")
    }

    val onClick: () -> Unit = {
        if ((currentWordIndex + 1) < words.size) {
            currentWordIndex++
        }
    }

    var isAlertDialogEnabled by remember { mutableStateOf(false) }
    val manager = viewModel.manager
    var textfield by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val isKeyboardVisible = rememberKeyboardVisibilityObserver()
    val focusManager = LocalFocusManager.current

    val shake = remember { Animatable(0f) }
    var trigger by remember { mutableLongStateOf(0L) }
    LaunchedEffect(trigger) {
        if (trigger != 0L) {
            for (i in 0..10) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, spring(stiffness = 100_000f))
                    else -> shake.animateTo(-5f, spring(stiffness = 100_000f))
                }
            }
            shake.animateTo(0f)
        }
    }

    if (words.isEmpty()) {
        if(viewModel.isInitialized){
            isSourceEmpty = viewModel.words.value.isNullOrEmpty()
        }
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        Log.d("Loading screen\"", "Words didn't initalize, isSourceEmpty is $isSourceEmpty")
    }
    else {
        SigmaEnglishTheme {
            Scaffold(
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            isAlertDialogEnabled = true
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = colorScheme.tertiary,
                        contentColor = colorScheme.secondary
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), // Adjust height as needed
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Skip",
                                    modifier = Modifier.clickable(onClick = {
                                        if (currentWordIndex + 1 == words.size) {
                                            words[currentWordIndex].isCorrect = false
                                            resultList = words.take(currentWordIndex + 1)
                                            Log.d("ZenTraining", "Result list of learned words: $resultList")
                                            navController.navigate("ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${convertWordsToJson(resultList)}")
                                        } else {
                                            if(firstTry){
                                                earnedScore--
                                            }
                                            else{
                                                firstTry = true
                                            }
                                            setHintExpanded(false)
                                            words[currentWordIndex].isCorrect = false
                                            onClick()
                                            textfield = ""
                                        }
                                    }),
                                    color = colorScheme.secondary
                                )
                                VerticalDivider(Modifier.size(4.dp))
                                Text(
                                    "Finish",
                                    modifier = Modifier.clickable(onClick =
                                    {
                                        words[currentWordIndex].isCorrect = false
                                        earnedScore--
                                        resultList = words.take(currentWordIndex + 1)
                                        Log.d("ZenTraining", "Result list of learned words: $resultList")
                                            navController.navigate("ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${convertWordsToJson(resultList)}")
                                    }),
                                    color = colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            ) {
                LaunchedEffect(isKeyboardVisible.value) {
                    if (!isKeyboardVisible.value) {
                        focusManager.clearFocus()
                    }
                }
                Text(
                    "Current score: ${earnedScore}\n" +
                            "Total count of words: ${words.size}",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(bottom = 26.dp)
                        .padding(horizontal = 26.dp)
                        .padding(top = 50.dp),
                    color = colorScheme.secondary,
                    lineHeight = 40.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        targetState = currentWordIndex,
                        transitionSpec = {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut()
                            )
                        }

                    ) { index ->
                        Text(words[index].english, fontSize = 50.sp, textAlign = TextAlign.Center, color = colorScheme.secondary,
                            modifier = Modifier.offset { IntOffset(shake.value.roundToInt(), y = 0) }
                        )
                    }

                    Column(Modifier.padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally){
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            shadowElevation = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier
                                .animateContentSize()
                                .padding(all = 16.dp)
                        ){
                            Hint(
                                iconUsed = true,
                                initialText = "Description",
                                expandedText = words[currentWordIndex].description,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
                        }
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            shadowElevation = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier
                                .animateContentSize()
                                .padding(all = 2.dp)
                        ) {
                            Hint(
                                iconUsed = false,
                                initialText = "\uD83E\uDD37   See answer",
                                expandedText = words[currentWordIndex].russian,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded2,
                                onExpandChange = setHintExpanded2
                            )
                        }
                    }
                    TextField(
                        value = textfield,
                        onValueChange = { textfield = it },
                        label = { Text("Write your translation here", color = Color.White) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (checkAnswer(textfield, words[currentWordIndex].russian)) {
                                        if(firstTry) {
                                            earnedScore++
                                        }
                                        setHintExpanded(false)
                                        if (currentWordIndex + 1 == words.size) {
                                            viewModel.checkForUpdatesHS(earnedScore)
                                            resultList = words.take(currentWordIndex + 1)
                                            Log.d("ZenTraining", "Result list of learned words: $resultList")
                                            navController.navigate("ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${convertWordsToJson(resultList)}")
                                        }
                                        textfield = ""
                                        onClick()
                                        firstTry = true
                                    } else {
                                        if(firstTry) {
                                            firstTry = false
                                            earnedScore--
                                        }
                                        trigger = System.currentTimeMillis()
                                        words[currentWordIndex].isCorrect = false
                                        textfield = ""
                                    }
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = colorScheme.secondary)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.White // Change cursor color to white
                        )
                    )
                }
            }
            if (isAlertDialogEnabled) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
                    containerColor = colorScheme.tertiary,
                    onDismissRequest = { isAlertDialogEnabled = false },
                    title = { Text("Are you sure?") },
                    text = {
                        Text("Are you sure you wanna close this window? Your progress will be lost.")
                    },
                    confirmButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                isAlertDialogEnabled = false
                            }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
    if (isSourceEmpty) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
            containerColor = colorScheme.tertiary,
            onDismissRequest = { navController.navigate("start") {
                popUpTo("start") { inclusive = true }
            }},
            title = { Text("No words to form training on!") },
            text = {
                    Text("To form a training list, you should first add some words.\n" +
                            " Would you like to be navigated to Word list screen to add some new words?")
            },
            confirmButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("WordListScreen") {
                            popUpTo("WordListScreen") { inclusive = true }
                        }
                    }) {
                    Text("Move to Word list screen")
                }
            }
            ,
            dismissButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }) {
                    Text("Move to start screen")
                }
            }
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WordTrainingScreenDescription(
    viewModel: ViewModel,
    navController: NavHostController,
    wordCount: Int,
    type: String
) {
    val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
    var currentWordIndex: Int by remember { mutableIntStateOf(0) }
    val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
    var words by remember { mutableStateOf(emptyList<TestWord>()) }
    var isSourceEmpty by remember { mutableStateOf(false) }
    val startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var isAlertDialogEnabled by remember { mutableStateOf(false) }

    var rightAnswer by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResults by remember { mutableStateOf<Boolean?>(false) }
    var options by remember { mutableStateOf<List<String>>(emptyList()) }
    var wrongAnswers by remember { mutableStateOf<Set<String>>(emptySet()) }
    fun resetAnswersState() {
        selectedAnswer = null
        showResults = false
        wrongAnswers = emptySet()
    }

    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = System.currentTimeMillis() - startTime
            delay(1000L)
        }
    }
    val wordLimit = when (type) {
        "last10" -> 10
        "last25" -> 25
        else -> wordCount
    }
    LaunchedEffect(Unit) {
        delay(500L)
        val shuffledWords = wordList.filter { it.description != "not provided" }.map { TestWord(it.english, it.russian, it.description, true) }.shuffled()
        words = shuffledWords.takeLast(wordLimit)

        Log.d("WordTraining", "Words initialized $words")
        if(words.size <4) {isSourceEmpty = true}
        rightAnswer = words[currentWordIndex].english
    }

    val goToNewWord: () -> Unit = {
        if ((currentWordIndex + 1) < words.size) {
            currentWordIndex++
            rightAnswer = words[currentWordIndex].english
            resetAnswersState()
        }
    }

    val shake = remember { Animatable(0f) }
    var trigger by remember { mutableLongStateOf(0L) }
    var triggerDelay by remember { mutableLongStateOf(0L) }
    LaunchedEffect(trigger) {
        if (trigger != 0L) {
            for (i in 0..10) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, spring(stiffness = 100_000f))
                    else -> shake.animateTo(-5f, spring(stiffness = 100_000f))
                }
            }
            shake.animateTo(0f)
        }
    }
    LaunchedEffect(triggerDelay) {
        Log.d("Delay", "Delay attempted, current trigger value = $triggerDelay")
        if (triggerDelay != 0L) {
            Log.d("Delay", "Delay started")
            delay(1500L)
            goToNewWord()
            Log.d("Delay", "Delay ended")
        }
    }
    if (words.isEmpty()) {
            isSourceEmpty = viewModel.words.value.isNullOrEmpty()
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        Log.d("Loading screen\"", "Words didn't initialize isSourceEmpty is $isSourceEmpty")
    }
    else {
        SigmaEnglishTheme {
            Scaffold(
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -50) { // Swipe right to left
                            isAlertDialogEnabled = true
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = colorScheme.tertiary,
                        contentColor = colorScheme.secondary
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), // Adjust height as needed
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Skip",
                                modifier = Modifier.clickable(onClick = {
                                    if (currentWordIndex + 1 == words.size) {
                                        words[currentWordIndex].isCorrect = false
                                        navController.navigate("ResultsScreen/${elapsedTime / 1000}/$type/${convertWordsToJson(words)}/Description")
                                    } else {
                                        setHintExpanded(false)
                                        words[currentWordIndex].isCorrect = false
                                        goToNewWord()
                                    }
                                }),
                                color = colorScheme.secondary
                            )
                        }
                    }
                }
            ) {
                Text(
                    "${currentWordIndex + 1}/${words.size}",
                    fontSize = 50.sp,
                    modifier = Modifier.padding(16.dp),
                    color = colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        targetState = currentWordIndex,
                        transitionSpec = {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut()
                            )
                        }

                    ) { index ->
                        Text(words[index].description, fontStyle = FontStyle.Italic, fontSize = 26.sp, textAlign = TextAlign.Center, color = colorScheme.secondary,
                            modifier = Modifier.offset { IntOffset(shake.value.roundToInt(), y = 0) }
                        )
                    }
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = colorScheme.surface,
                            modifier = Modifier
                                .animateContentSize()
                                .padding(all = 16.dp)
                        ){
                            Hint(
                                iconUsed = true,
                                initialText = "Translation",
                                expandedText = words[currentWordIndex].russian,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
                        }

                        if(words.size >=4) {


                            LaunchedEffect(currentWordIndex) {
                                val randomPosition = (0..3).random()
                                val remainingOptions =
                                    wordList.filter { it.english != rightAnswer }.shuffled().take(3)
                                        .map { it.english }
                                options = mutableListOf<String>().apply {
                                    addAll(remainingOptions)
                                    add(randomPosition, rightAnswer)
                                }
                            }
                            @Composable
                            fun getButtonColor(option: String): Color {
                                return when {
                                    showResults == true && option == rightAnswer -> Color.Green
                                    wrongAnswers.contains(option) -> Color.Red
                                    else -> colorScheme.secondary
                                }
                            }
                            val buttonColors = options.map { option ->
                                animateColorAsState(
                                    targetValue = getButtonColor(option),
                                    animationSpec = tween(durationMillis = 500)
                                ).value
                            }

                            val onClick: (String) -> Unit = { selectedOption ->
                                selectedAnswer = selectedOption
                                if (checkAnswer(selectedOption, words[currentWordIndex].english)) {
                                    showResults = true
                                    if (currentWordIndex + 1 == words.size) {
                                        navController.navigate(
                                            "ResultsScreen/${elapsedTime / 1000}/$type/${convertWordsToJson(words)}/Description"
                                        )
                                    }
                                    triggerDelay = System.currentTimeMillis()
                                    setHintExpanded(false)
                                } else {
                                    wrongAnswers = wrongAnswers + selectedOption
                                    trigger = System.currentTimeMillis()
                                    words[currentWordIndex].isCorrect = false
                                }

                            }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    options.forEachIndexed { index, option ->
                                        Button(
                                            onClick = { onClick(option) },
                                            colors = ButtonDefaults.buttonColors(contentColor = buttonColors[index]),
                                            modifier = Modifier.width(200.dp)
                                        ) {
                                            Text(
                                                option,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )

                                        }
                                    }

                        }
                    }

                }
            }
            if (isAlertDialogEnabled) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
                    containerColor = colorScheme.tertiary,
                    onDismissRequest = { isAlertDialogEnabled = false },
                    title = { Text("Are you sure?") },
                    text = {
                        Text("Are you sure you wanna close this window? Your progress will be lost.")
                    },
                    confirmButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                                isAlertDialogEnabled = false
                            }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
    if (isSourceEmpty) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
            containerColor = colorScheme.tertiary,
            onDismissRequest = { navController.navigate("start") {
                popUpTo("start") { inclusive = true }
            }},
            title = { Text("Not enough words to form training on!") },
            text = {
                    Text("To form a training list, you should first add some words.\n" +
                            " Would you like to be navigated to Word list screen to add some new words?")
            },
            confirmButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("WordListScreen") {
                            popUpTo("WordListScreen") { inclusive = true }
                        }
                    }) {
                    Text("Move to Word list screen")
                }
            }
            ,
            dismissButton = {
                Button(
                    colors = customButtonColors(),
                    onClick = {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }) {
                    Text("Move to start screen")
                }
            }
        )
    }
}

@Composable
fun ResultsScreen(
    viewModel: ViewModel,
    navController: NavHostController,
    timeSpent: Int,
    selectedType: String,
    learnedWords: List<TestWord>,
    selectedMode: String
) {
    LaunchedEffect(Unit) {
        learnedWords.forEach { TestWord ->
            if (!TestWord.isCorrect) {
                if (!viewModel.isWordInFailedDatabase(TestWord.english)) {
                    val wordFailed = DBType.WordsFailed(
                        english = TestWord.english,
                        russian = TestWord.russian,
                        description = TestWord.description,
                        timesPractised = 0
                    )
                    viewModel.addWordFailed(word = wordFailed)
                }
            }
        }
        viewModel.checkForDeletion()
    }

    val wordCount: Int = learnedWords.size
    val accuracy: String = buildString {
        var correctScore = 0
        learnedWords.forEach { word ->
            if (word.isCorrect) {
                correctScore++
            }
        }
        val percentage = if (learnedWords.isNotEmpty()) {
            (correctScore.toDouble() / learnedWords.size * 100).toInt()
        } else {
            0
        }
        append("$percentage%")
    }

    val scrollState = rememberLazyListState()
    val wordsPlaceholder: List<TestWord> = emptyList()
    val calculatedAlpha by remember { derivedStateOf { calculateGradientAlpha(scrollState) } }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(selectedMode != "Description") {
                        IconButton(onClick = {
                            navController.navigate(
                                "WordTrainingScreen/$wordCount/$selectedType/${
                                    convertWordsToJson(learnedWords)
                                }/Refresh"
                            )
                        }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                    IconButton(onClick = {
                        if(selectedMode == "Description") {
                            navController.navigate(
                               "WordTrainingScreenDescription/$wordCount/$selectedType"
                            )
                        }
                        else {
                            navController.navigate(
                                "WordTrainingScreen/$wordCount/$selectedType/${
                                    convertWordsToJson(wordsPlaceholder)
                                }/Classic"
                            )
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(
                            "settings/Mistakes"
                        )
                    }) {
                        Icon(Icons.Default.Warning, contentDescription = "Mistakes")
                    }
                    IconButton(onClick = {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Exit")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                fontSize = 40.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                text = "Results:\n",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(17.dp))
                    .border(
                        BorderStroke(3.dp, colorScheme.secondary),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    state = scrollState
                ) {
                    items(learnedWords) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                fontSize = 20.sp,
                                text = "${word.english} - ${word.russian}"
                            )
                            if (word.isCorrect) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "success",
                                    tint = Color.Green,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "failure",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(vertical = 0.dp, horizontal = 2.dp)
                        .fillMaxWidth()
                        .height(20.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.5f)),
                                startY = 0f,
                                endY = 100f
                            ),
                            alpha = calculatedAlpha
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val styleHeader = customTitle.toSpanStyle()
                    val styleText = customText.toSpanStyle()
                    // First Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                text = buildAnnotatedString {
                                    withStyle(style = styleHeader) {
                                        append("Words\n")
                                    }

                                    withStyle(style = styleText) {
                                        append("$wordCount")
                                    }
                                }
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                text = buildAnnotatedString {
                                    withStyle(style = styleHeader) {
                                        append("Accuracy\n")
                                    }
                                    withStyle(style = styleText) {
                                        append(accuracy)
                                    }
                                }
                            )
                    }
                    // Second Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Test Type\n")
                                }
                                withStyle(style = styleText) {
                                    append("$selectedType Words\n$selectedMode")
                                }
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Time\n")
                                }
                                withStyle(style = styleText) {
                                    append("${timeSpent}s")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ResultsScreenZen(
    navController: NavHostController,
    timeSpent: Int,
    earnedScore: Int,
    learnedWords: List<TestWord>,
    viewModel: ViewModel
) {
    Log.d("ResultsScreenZen", "Learned words: $learnedWords")
    val wordCount: Int = learnedWords.size
    val accuracy: String = buildString {
        var correctScore = 0
        learnedWords.forEach { word ->
            if (word.isCorrect) {
                correctScore++
            }
        }
        val percentage = if (learnedWords.isNotEmpty()) {
            (correctScore.toDouble() / learnedWords.size * 100).toInt()
        } else {
            0
        }
        append("$percentage%")
    }

    val scrollState = rememberLazyListState()
    val calculatedAlpha by remember { derivedStateOf { calculateGradientAlpha(scrollState) } }
    val highestScore by viewModel.highestScore.observeAsState(0)
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                            navController.navigate("WordTrainingScreenZen")
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(
                            "settings/Mistakes"
                        )
                    }) {
                        Icon(Icons.Default.Warning, contentDescription = "Mistakes")
                    }
                    IconButton(onClick = {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Exit")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                fontSize = 40.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                text = "Results:\n",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(17.dp))
                    .border(
                        BorderStroke(3.dp, colorScheme.secondary),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    state = scrollState
                ) {
                    items(learnedWords) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                fontSize = 20.sp,
                                text = "${word.english} - ${word.russian}"
                            )
                            if (word.isCorrect) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "success",
                                    tint = Color.Green,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "failure",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(vertical = 0.dp, horizontal = 2.dp)
                        .fillMaxWidth()
                        .height(20.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.5f)),
                                startY = 0f,
                                endY = 100f
                            ),
                            alpha = calculatedAlpha
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val styleHeader = customTitle.toSpanStyle()
                    val styleText = customText.toSpanStyle()
                    // First Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Words\n")
                                }

                                withStyle(style = styleText) {
                                    append("$wordCount")
                                }
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Accuracy\n")
                                }
                                withStyle(style = styleText) {
                                    append(accuracy)
                                }
                            }
                        )
                    }
                    // Second Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Test Type\n")
                                }
                                withStyle(style = styleText) {
                                    append("Zen")
                                }
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Time\n")
                                }
                                withStyle(style = styleText) {
                                    append("${timeSpent}s")
                                }
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Score\n")
                                }
                                withStyle(style = styleText) {
                                    append("$earnedScore points")
                                }
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 10.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            text = buildAnnotatedString {
                                withStyle(style = styleHeader) {
                                    append("Highest Score\n")
                                }
                                withStyle(style = styleText) {
                                    append("${highestScore} points")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}