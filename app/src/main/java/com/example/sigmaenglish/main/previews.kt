package com.example.sigmaenglish.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sigmaenglish.Database.DBType
import com.example.sigmaenglish.navigation.convertWordsToJson
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

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
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true),
        Word(english = "book", russian = "книга", description = "A written or printed work", false),
        Word(english = "cat", russian = "кот", description = "A small domesticated carnivorous mammal", true),
        Word(english = "dog", russian = "собака", description = "A domesticated carnivorous mammal", true),
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true) ,
        Word(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
        Word(english = "book", russian = "книга", description = "A written or printed work", false),
        Word(english = "cat", russian = "кот", description = "A small domesticated carnivorous mammal", true),
        Word(english = "dog", russian = "собака", description = "A domesticated carnivorous mammal", true),
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true),
        Word(english = "book", russian = "книга", description = "A written or printed work", false),
        Word(english = "cat", russian = "кот", description = "A small domesticated carnivorous mammal", true),
        Word(english = "dog", russian = "собака", description = "A domesticated carnivorous mammal", true),
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true) ,
        Word(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
        Word(english = "book", russian = "книга", description = "A written or printed work", false),
        Word(english = "cat", russian = "кот", description = "A small domesticated carnivorous mammal", true),
        Word(english = "dog", russian = "собака", description = "A domesticated carnivorous mammal", true),
        Word(english = "elephant", russian = "слон", description = "A large mammal with a trunk", true)
    )
}

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WordTrainingScreenDescription() {
    val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
    val (isHintExpanded2, setHintExpanded2) = remember { mutableStateOf(false) }
    var currentWordIndex: Int by remember { mutableIntStateOf(0) }
    var isSourceEmpty by remember { mutableStateOf(false) }
    val startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var elapsedTime by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = System.currentTimeMillis() - startTime
            delay(1000L)
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
                    "${currentWordIndex + 1}/10",
                    fontSize = 50.sp,
                    modifier = Modifier.padding(26.dp),
                    color = colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("words[index].description", fontStyle = FontStyle.Italic, fontSize = 26.sp, textAlign = TextAlign.Center, color = colorScheme.secondary,
                            modifier = Modifier.offset { IntOffset(shake.value.roundToInt(), y = 0) }
                    )
                    Column(Modifier.padding(0.dp), horizontalAlignment = Alignment.CenterHorizontally){
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            shadowElevation = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier
                                .animateContentSize()
                                .padding(top = 16.dp)
                        ){
                            Hint(
                                iconUsed = true,
                                initialText = "Translation",
                                expandedText = "russian",
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
                        }
                    }
                    SigmaEnglishTheme {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val buttonWidth = 200.dp // Set this to the desired width
                                Button(
                                    onClick = { },
                                    modifier = Modifier.width(buttonWidth),
                                    colors = customButtonColors()
                                ) {
                                    Text(
                                        "Option A: Short word",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                    )
                                }
                                Button(
                                    onClick = { },
                                    modifier = Modifier.width(buttonWidth),
                                    colors = customButtonColors()
                                ) {
                                    Text(
                                        "Option B: Loooooong",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Button(
                                    onClick = { },
                                    modifier = Modifier.width(buttonWidth),
                                    colors = customButtonColors()
                                ) {
                                    Text("Option C: test",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Button(
                                    onClick = { },
                                    modifier = Modifier.width(buttonWidth),
                                    colors = customButtonColors()
                                ) {
                                    Text("Option D: dfhdfgdfgdfg",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }
                    }

//                    TextField(
//                        value = textfield,
//                        onValueChange = { textfield = it },
//                        label = { Text("Write your translation here", color = Color.White) },
//                        trailingIcon = {
//                            IconButton(
//                                onClick = {
//                                    if (checkAnswer(textfield, words[currentWordIndex].russian)) {
//                                        setHintExpanded(false)
//                                        if (currentWordIndex + 1 == words.size) {
//                                            navController.navigate("ResultsScreen/${elapsedTime / 1000}/$type/${convertWordsToJson(words)}")
//                                        }
//                                        textfield = ""
//                                        onClick()
//                                    } else {
//                                        trigger = System.currentTimeMillis()
//                                        words[currentWordIndex].isCorrect = false
//                                    }
//                                }
//                            ) {
//                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = colorScheme.secondary)
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .focusRequester(focusRequester),
//                        colors = TextFieldDefaults.colors(
//                            cursorColor = Color.White // Change cursor color to white
//                        )
//                    )
                }
            }
        }
}