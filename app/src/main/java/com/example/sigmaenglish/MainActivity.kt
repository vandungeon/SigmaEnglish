package com.example.sigmaenglish

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Bundle
import android.provider.UserDictionary.Words
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.BottomSheetDefaults.ContainerColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.ui.theme.lightgray
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

data class Word(
    val english: String,
    val russian: String,
    val description: String,
    var isCorrect: Boolean = false
)

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
fun convertWordsToJson(words: List<Word>): String {
    val gson = Gson()
    return gson.toJson(words)
}

fun convertJsonToWords(json: String): List<Word> {
    val gson = Gson()
    val type = object : TypeToken<List<Word>>() {}.type
    return gson.fromJson(json, type)
}
@Composable
fun NavigationComponent(viewModel: ViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("WordListScreen") { WordListScreen(viewModel, navController) }
        composable("trainingMenu") { TrainingMenu(viewModel, navController) }
        composable("settings") { SettingsScreen(viewModel, navController) }
        composable(
            route = "WordTrainingMenu/{selectedNumber}/{selectedType}/{wordRefreshList}",
            arguments = listOf(
                navArgument("selectedNumber") { type = NavType.IntType },
                navArgument("selectedType") { type = NavType.StringType },
                navArgument("wordRefreshList") {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val selectedNumber = backStackEntry.arguments?.getInt("selectedNumber")
            val selectedType = backStackEntry.arguments?.getString("selectedType")
            val wordRefresh = backStackEntry.arguments?.getString("wordRefreshList")
            val wordRefreshList = wordRefresh?.let { convertJsonToWords(it) } ?: emptyList()
            WordTrainingMenu(viewModel, navController, selectedNumber ?: 10, selectedType ?: "All", wordRefreshList)
        }
        composable(route = "ResultsScreen/{timeSpent}/{selectedType}/{wordsLearned}",
            arguments = listOf(
                navArgument("timeSpent") { type = NavType.IntType },
                navArgument("selectedType") { type = NavType.StringType },
                navArgument("wordsLearned") { type = NavType.StringType}
            )) { backStackEntry ->
            val timeSpent = backStackEntry.arguments?.getInt("timeSpent")?: 0
            val selectedType = backStackEntry.arguments?.getString("selectedType") ?: "All"
            val wordsLearnedJson = backStackEntry.arguments?.getString("wordsLearned")
            val wordsLearned = wordsLearnedJson?.let { convertJsonToWords(it) } ?: emptyList()
            ResultsScreen(viewModel, navController, timeSpent, selectedType, wordsLearned)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        focusedTextColor =  Color.Black,
        disabledTextColor = Color.Black,
        unfocusedPlaceholderColor = colorScheme.secondary,
        focusedPlaceholderColor = colorScheme.secondary

    )
}
@Composable
fun CustomButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = colorScheme.tertiary,
        contentColor = colorScheme.secondary,
        disabledContainerColor = colorScheme.tertiary.copy(alpha = 0.3f),
        disabledContentColor = colorScheme.onTertiary.copy(alpha = 0.3f)
    )
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
                Text("Start Screen", style = MaterialTheme.typography.headlineMedium, color = colorScheme.secondary)
                Button(onClick = { navController.navigate("WordListScreen") }, colors = CustomButtonColors()) {
                    Text("Words List")
                }
                Button(onClick = { navController.navigate("trainingMenu") }, colors = CustomButtonColors()) {
                    Text("Training")
                }
            }
        }
    }
}
@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.White)
            .weight(weight)
            .padding(8.dp)
    )
}
@Composable
fun WordManagementDialog(
    word: DBType.Word,
    onDelete: () -> Unit,
    onUpdate: (DBType.Word) -> Unit,
    onDismiss: () -> Unit
) {
    var translationDescription by remember { mutableStateOf(word.description) }
    var englishWord by remember { mutableStateOf(word.english) }
    var russianWord by remember { mutableStateOf(word.russian) }
    var enableButton by remember { mutableStateOf(false)}
    fun validateInput(eng: String, rus: String): Boolean {
        return eng.isNotEmpty() and rus.isNotEmpty()
    }
    // Dialog UI to manage the word (delete or update)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Word") },
        text = {
            Column {
                TextField(
                    value = englishWord,
                    onValueChange = { englishWord = it
                        if(validateInput(englishWord, russianWord)){enableButton =true}},
                    label = { Text("English") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    colors = CustomTextFieldColors(),
                    value = russianWord,
                    onValueChange = { russianWord = it
                        if(validateInput(englishWord, russianWord)){enableButton =true}},
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
            Button(onClick = {
                // Handle word update
                onUpdate(
                    word.copy(
                        english = englishWord,
                        russian = russianWord,
                        description = translationDescription
                    )
                )
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordListScreen(viewModel: ViewModel, navController: NavHostController) {
    val wordList by viewModel.words.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<DBType.Word?>(null) }

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
                        IconButton(onClick = {navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }}) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit")
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
                item {
                    Row(Modifier.background(Color.Transparent)) {
                        TableCell(text = "Original", weight = 1f)
                        TableCell(text = "Translation", weight = 1f)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    }
                }
                itemsIndexed(wordList) { index, word ->
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
                                        style = MaterialTheme.typography.bodyMedium,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordListScreenPreview(wordList: List<DBType.Word>, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<DBType.Word?>(null) }

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
                        IconButton(onClick = {navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }}) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit")
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
                item {
                    Row(Modifier.background(Color.Transparent)) {
                        TableCell(text = "Original", weight = 1f)
                        TableCell(text = "Translation", weight = 1f)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    }
                }
                itemsIndexed(wordList) { index, word ->
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
                                        style = MaterialTheme.typography.bodyMedium,
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
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TrainingMenu(viewModel: ViewModel, navController: NavHostController) {
    var selectedScreen by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Example cards for different modes
        Text("Select a mode:")
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ModeCard(mode = "settings", selectedScreen, onSelect = { selectedScreen = "settings" })
            ModeCard(mode = "otherScreen", selectedScreen, onSelect = { selectedScreen = "otherScreen" })
            // Add more ModeCard for additional screens
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
                "settings" -> navController.navigate("settings")
                //"otherScreen" -> OtherScreen(viewModel = viewModel, navController = navController)
                // Add more cases for additional screens
                else -> { /* Handle default case or additional screens */ }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: ViewModel, navController: NavHostController) {
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
                                    //color = if (selectedNumber == number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
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
                    modifier = Modifier.clickable {
                        val mockList : List<Word> = emptyList()
                        navController.navigate("WordTrainingMenu/$selectedNumber/$selectedType/$mockList")
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

@Composable
fun rememberKeyboardVisibilityObserver(): State<Boolean> {
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val rootView = LocalView.current
    DisposableEffect(rootView) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            isKeyboardVisible.value = keypadHeight > screenHeight * 0.15
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return isKeyboardVisible
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WordTrainingMenu(
    viewModel: ViewModel,
    navController: NavHostController,
    wordCount: Int,
    type: String,
    wordsRefresh: List<Word>? = null
) {
    var time by remember { mutableStateOf(0) }
    var currentWordIndex: Int by remember { mutableIntStateOf(0) }
    val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())

    var words by remember { mutableStateOf(emptyList<Word>()) }

    LaunchedEffect(wordsRefresh, wordList) {
        words = if (!wordsRefresh.isNullOrEmpty()) {
            wordsRefresh
        } else {
            val shuffledWords = wordList.map { Word(it.english, it.russian, it.description, true) }.shuffled()
            shuffledWords.take(wordCount)
        }
        Log.d("WordTraining", "Words initialized $words")
    }

    Log.d("WordTraining", "Words initialized $words")
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
    var trigger by remember { mutableStateOf(0L) }
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
        Log.d("Loading screen", "${words}")
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )

    } else {
        SigmaEnglishTheme {
            Scaffold(
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
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
                                        navController.navigate("ResultsScreen/$time/$type/${convertWordsToJson(words)}")
                                    } else {
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
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .animateContentSize()
                            .padding(all = 16.dp)
                    ) {
                        Hint(
                            initialText = "Description",
                            expandedText = words[currentWordIndex].description,
                            icon = Icons.Default.Info
                        )
                    }
                    TextField(
                        value = textfield,
                        onValueChange = { textfield = it },
                        label = { Text("Write your translation here", color = Color.White) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (checkAnswer(textfield, words[currentWordIndex].russian)) {
                                        if (currentWordIndex + 1 == words.size) {
                                            navController.navigate("ResultsScreen/$time/$type/${convertWordsToJson(words)}")
                                        }
                                        textfield = ""
                                        onClick()
                                    } else {
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
                    title = { Text("Manage Word") },
                    text = {
                        Text("Are you sure you wanna close this window? Your progress will be lost.")
                    },
                    confirmButton = {
                        Button(
                            colors = CustomButtonColors(),
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
                            colors = CustomButtonColors(),
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
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(viewModel: ViewModel? = null, navController: NavHostController, timeSpent: Int, selectedType: String, learnedWords: List<Word>) {
    val wordCount: Int = learnedWords.size
    val words: String = buildString {
        learnedWords.forEachIndexed { index, word ->
            if (index < learnedWords.size - 1) {
                append("${word.english} - ${word.russian}, ")
            }
            else if (index < learnedWords.size ) {
                append("${word.english} - ${word.russian}")
            }
        }
    }
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

    val wordsplaceholder: List<Word> = emptyList()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {navController.navigate("WordTrainingMenu/$wordCount/$selectedType/${convertWordsToJson(learnedWords)}")}) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { navController.navigate("WordTrainingMenu/$wordCount/$selectedType/${convertWordsToJson(wordsplaceholder)}")}) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Warning, contentDescription = "Mistakes")
                    }
                    IconButton(onClick = {navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }}) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                fontSize = 40.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp),
                textAlign = TextAlign.Center,
                text = "Results:\n",
            )
            Text(
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left,
                text = "Words: ${words}"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = innerPadding.calculateBottomPadding() + 70.dp) // Adjust as needed
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount < -50) { // Swipe right to left
                                //   navController.navigate("start") {
                                //    popUpTo("start") { inclusive = true }
                                // }
                            }
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // First Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            text = "words\n${wordCount}",
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            text = "Accuracy\n${accuracy}",
                        )
                    }

                    // Second Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            text = "test type\n${selectedType}",
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            text = "time\n${timeSpent}s",
                        )
                    }
                }

            }
        }
    }
}

fun checkAnswer(userAnswer: String, correctAnswer: String): Boolean {
    return userAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)
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
    var enableButton by remember { mutableStateOf(false)}
    fun validateInput(eng: String, rus: String): Boolean {
        return eng.isNotEmpty() and rus.isNotEmpty()
    }
    AlertDialog(
        containerColor = colorScheme.primary,
        onDismissRequest = onDismiss,
        title = { Text("Add New Word") },
        text = {
            Column {
                TextField(
                    value = englishWord,
                    onValueChange = { englishWord = it
                        if(validateInput(englishWord, russianWord)){enableButton =true}},
                    label = { Text("English") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    colors = CustomTextFieldColors(),
                    value = russianWord,
                    onValueChange = { russianWord = it
                                    if(validateInput(englishWord, russianWord)){enableButton =true}},
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
                colors = CustomButtonColors(),
                enabled = enableButton,
                onClick = {
                        if(translationDescription.isNotEmpty()){
                            onConfirm(englishWord, russianWord, translationDescription)
                            onDismiss()
                        }
                        else{
                            onConfirm(englishWord, russianWord, "No description provided")
                            onDismiss()
                        }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                colors = CustomButtonColors(),
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun Hint(initialText: String, expandedText: String, icon: ImageVector) {
    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
    val (displayText, setDisplayText) = remember { mutableStateOf(initialText) }

    // Toggle expanded state and change text accordingly
    val onClick: () -> Unit = {
        setExpanded(!isExpanded)
        setDisplayText(if (isExpanded) initialText else expandedText)
    }

    Card(
        shape = RoundedCornerShape(16.dp), // Set the round shape here
        colors = CardDefaults.cardColors(lightgray), // Set the container color here

        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(all = 8.dp)
            .border(BorderStroke(2.dp, colorScheme.secondary),shape = RoundedCornerShape(16.dp),),
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp) // Padding inside the Card
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Info",
                modifier = Modifier.padding(horizontal = 0.dp),
                tint = Color.White
            )
            Text(
                color = Color.White,
                text = displayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}
@Composable
fun ModeCard(mode: String, selectedScreen: String, onSelect: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onSelect),
        border = BorderStroke(2.dp, if (mode == selectedScreen) Color.Black else Color.Transparent),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(contentColor = Color.LightGray, containerColor = Color.White, disabledContentColor = Color.DarkGray, disabledContainerColor = Color.DarkGray),
        content = {
            Text(
                text = mode,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
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

@Composable
@Preview
fun ResultsPreview(){
    val timeSpent = 10
    val selectedType = "Standart"
    val sampleWords: List<Word> = listOf(
        Word(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
        Word(english = "book", russian = "книга", description = "A written or printed work", false),
        Word(english = "cat", russian = "кот", description = "A small domesticated carnivorous mammal", true),
        Word(english = "dog", russian = "собака", description = "A domesticated carnivorous mammal", true),
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true)
    )
    ResultsScreen(null, navController = rememberNavController(), timeSpent , selectedType, sampleWords)
}


