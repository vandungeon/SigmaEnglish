package com.example.sigmaenglish.main.appScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sigmaenglish.database.DBType
import com.example.sigmaenglish.main.CustomButton
import com.example.sigmaenglish.main.Hint
import com.example.sigmaenglish.main.ModeCard
import com.example.sigmaenglish.main.TableCell
import com.example.sigmaenglish.main.TableCellHeader
import com.example.sigmaenglish.main.TestWord
import com.example.sigmaenglish.main.calculateGradientAlpha
import com.example.sigmaenglish.main.checkAnswer
import com.example.sigmaenglish.main.dialogHeader
import com.example.sigmaenglish.main.rememberKeyboardVisibilityObserver
import com.example.sigmaenglish.main.styleHeader
import com.example.sigmaenglish.main.styleText
import com.example.sigmaenglish.navigation.convertWordsToJson
import com.example.sigmaenglish.ui.theme.PastelGreen
import com.example.sigmaenglish.ui.theme.WrongRed
import com.example.sigmaenglish.ui.theme.dialogMain
import com.example.sigmaenglish.ui.theme.interFontFamily
import com.example.sigmaenglish.ui.theme.montserratFontFamily
import com.example.sigmaenglish.ui.theme.standartText
import com.example.sigmaenglish.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun TrainingMenu(navController: NavHostController) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            BackHandler {
                navController.navigate("start")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.primary)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Select a mode:", style = typography.titleLarge, color = colorScheme.secondary)
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        ModeCard(
                            mode = "Classic  \uD83E\uDDD0",
                            onClick = { navController.navigate("settings/Classic") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ModeCard(
                            mode = "Mistakes practise  ❌",
                            onClick = { navController.navigate("settings/Mistakes") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ModeCard(
                            mode = "Description  \uD83D\uDCDD",
                            onClick = { navController.navigate("settings/Description") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ModeCard(
                            mode = "Zen  \uD83C\uDF43",
                            onClick = { navController.navigate("WordTrainingScreenZen") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ModeCard(
                            mode = "Favorites  ⭐",
                            onClick = { navController.navigate("settings/Favorites") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ModeCard(
                            mode = "Tags  \uD83D\uDCC1",
                            onClick = { navController.navigate("TrainingTagSelector") },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsScreen(navController: NavHostController, trainingType: String) {
        var showDialog by remember { mutableStateOf(false) }
        var selectedNumber by remember { mutableIntStateOf(10) }
        var selectedType by remember { mutableStateOf("All") }
        val numbers = listOf(10, 25, 50, 100) // List of numbers to display
        val types = listOf("Last 10", "Last 25", "All") // List of types to display
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primaryContainer,
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
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (selectedType != "All" && selectedType != "") {
                                                    showDialog = true
                                                } else {
                                                    selectedNumber = number
                                                }
                                            }
                                            .padding(horizontal = 25.dp),
                                        color = if (selectedNumber == number) colorScheme.secondary else colorScheme.primary,
                                        style = TextStyle(
                                            fontFamily = interFontFamily,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
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
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
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
                                    color = if (selectedType == type) colorScheme.secondary else colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(colorScheme.primary)
                    .padding(innerPadding),
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
                        style = typography.titleLarge,
                        color = colorScheme.secondary,
                        modifier = Modifier.clickable {
                            val mockList: List<TestWord> = emptyList()
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
                                "Favorites" -> {
                                    navController.navigate("WordTrainingScreen/$selectedNumber/$selectedType/$mockList/Favorites")
                                }
                                "Tags" -> {
                                    navController.navigate("WordTrainingScreen/$selectedNumber/$selectedType/$mockList/Tags")
                                }
                            }
                        }
                    )
                }

            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Alert", style = dialogHeader) },
                    text = {
                        Text(
                            "Number-specific mode is selected, unable to change the number",
                            style = dialogMain
                        )
                    },
                    confirmButton = {
                        CustomButton(
                            onClick = { showDialog = false }, text = "Ok"
                        )
                    }
                )
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    fun TrainingTagSelector(viewModel: ViewModel, navController: NavHostController) {
        val tagsList by viewModel.tags.observeAsState(emptyList())
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primaryContainer,
                        titleContentColor = colorScheme.primary,
                    ),
                    title = {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(1f), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Select tag for training",

                                fontFamily = montserratFontFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                )
            }
        ){ innerPadding ->
            Box(modifier = Modifier
                .fillMaxSize(1f)
                .background(colorScheme.primary)) {
                Column(
                    modifier = Modifier
                        .background(colorScheme.primary)
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .background(colorScheme.secondary)
                                    .fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableCellHeader(text = "Tag folder name", weight = 1f)
                            }
                        }
                        itemsIndexed(tagsList){ index, tag ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        viewModel.setSelectedTag(tag)
                                        navController.navigate("settings/Tags")
                                    },
                                    onLongClick = { }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                TableCell(text = tag.name, weight = 1f)
                            }
                        }
                    }
                }
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
        wordSourse: String
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
        var dataAssignmentTrigger by remember { mutableLongStateOf(0L) }
        val selectedTag by viewModel.selectedTag.observeAsState(null)
        val wordLimit = when (type) {
            "Last 10" -> 10
            "Last 25" -> 25
            else -> wordCount
        }
        LaunchedEffect(dataAssignmentTrigger) {
            if (viewModel.isInitialized) {
                words = when {
                    !wordsRefresh.isNullOrEmpty() && (wordSourse == "Refresh" || wordSourse == "RefreshMistakes") -> {
                        wordsRefresh
                    }

                    wordSourse == "Classic" -> {
                        if (type == "Last 10" || type == "Last 25") {
                            val takenWords = wordList.map {
                                TestWord(
                                    it.english,
                                    it.russian,
                                    it.description,
                                    true
                                )
                            }.takeLast(wordLimit)
                            takenWords.shuffled()
                        } else {
                            val takenWords = wordList.map {
                                TestWord(
                                    it.english,
                                    it.russian,
                                    it.description,
                                    true
                                )
                            }.shuffled()
                            takenWords.takeLast(wordLimit)
                        }
                    }

                    wordSourse == "Favorites" -> {
                        if (type == "Last 10" || type == "Last 25") {
                            val takenWords = wordList
                                .filter { it.favorite }
                                .map { TestWord(it.english, it.russian, it.description, true) }
                            takenWords.takeLast(wordLimit).shuffled()
                        } else {
                            val takenWords = wordList
                                .filter { it.favorite }
                                .map { TestWord(it.english, it.russian, it.description, true) }

                            takenWords.shuffled().takeLast(wordLimit)
                        }
                    }
                    wordSourse == "Tags" -> {
                        if (type == "Last 10" || type == "Last 25") {
                            val takenWords = wordList.filter { it.id in (selectedTag?.numbers ?: emptyList()) }.map {
                                TestWord(
                                    it.english,
                                    it.russian,
                                    it.description,
                                    true
                                )
                            }.takeLast(wordLimit)
                            takenWords.shuffled()
                        } else {
                            val takenWords = wordList.filter { it.id in (selectedTag?.numbers ?: emptyList()) }.map {
                                TestWord(
                                    it.english,
                                    it.russian,
                                    it.description,
                                    true
                                )
                            }.shuffled()
                            takenWords.takeLast(wordLimit)
                        }
                    }
                    else -> {
                        val takenWords = wordListFailed.map {
                            TestWord(
                                it.english,
                                it.russian,
                                it.description,
                                true
                            )
                        }.takeLast(wordLimit)
                        takenWords.shuffled()
                    }
                }

                Log.d("WordTraining", "Words initialized $words, words failed $wordListFailed")
            } else {
                Log.d("WordTraining", "ViewModel not initialized yet")
            }
        }
        LaunchedEffect(Unit) {
            while (true) {
                elapsedTime = System.currentTimeMillis() - startTime
                delay(1000L)
            }
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
        BackHandler {
            isAlertDialogEnabled = true
        }
        if (words.isEmpty()) {
            dataAssignmentTrigger = System.currentTimeMillis()
            if (viewModel.isInitialized) {
                when (wordSourse) {
                    "Classic" -> {
                        isSourceEmpty = viewModel.words.value.isNullOrEmpty()
                    }
                    "Mistakes" -> {
                        isSourceEmpty = viewModel.wordsFailed.value.isNullOrEmpty()
                    }
                    "Favorites" -> {
                        isSourceEmpty = viewModel.words.value?.none { it.favorite } ?: true
                    }
                    "Tags" -> {
                        isSourceEmpty = viewModel.words.value.isNullOrEmpty() || (selectedTag?.numbers?.isEmpty()
                            ?: false)
                    }
                }
            }
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            Log.d(
                "Loading screen",
                "Words didn't initialize, source is set to $wordSourse, isSourceEmpty is $isSourceEmpty"
            )
        } else {
            Scaffold(
                modifier = Modifier
                    .background(colorScheme.primary),
                bottomBar = {
                    Column {
                        TextField(
                            value = textfield,
                            onValueChange = { textfield = it },
                            label = { Text("Write your translation here", color = Color.White) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (checkAnswer(
                                                textfield,
                                                words[currentWordIndex].russian
                                            )
                                        ) {
                                            setHintExpanded(false)
                                            setHintExpanded2(false)
                                            if (currentWordIndex + 1 == words.size) {
                                                navController.navigate(
                                                    "ResultsScreen/${elapsedTime / 1000}/$type/${
                                                        convertWordsToJson(
                                                            words
                                                        )
                                                    }/Classic"
                                                )
                                            }
                                            textfield = ""
                                            onClick()
                                        } else {
                                            trigger = System.currentTimeMillis()
                                            words[currentWordIndex].isCorrect = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = colorScheme.tertiary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.colors(
                                cursorColor = colorScheme.primary
                            )
                        )
                        BottomAppBar(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Skip",
                                    modifier = Modifier.clickable(onClick = {
                                        if (currentWordIndex + 1 == words.size) {
                                            words[currentWordIndex].isCorrect = false
                                            navController.navigate(
                                                "ResultsScreen/${elapsedTime / 1000}/$type/${
                                                    convertWordsToJson(
                                                        words
                                                    )
                                                }/Classic"
                                            )
                                        } else {
                                            setHintExpanded(false)
                                            setHintExpanded2(false)
                                            words[currentWordIndex].isCorrect = false
                                            onClick()
                                        }
                                    }),
                                    color = colorScheme.primary
                                )
                            }
                        }
                    }
                }
            ) {
                Column() {
                    Column(modifier = Modifier.height(150.dp)) {
                        LaunchedEffect(isKeyboardVisible.value) {
                            if (!isKeyboardVisible.value) {
                                focusManager.clearFocus()
                            }
                        }
                        Text(
                            "${currentWordIndex + 1}/${words.size}",
                            fontSize = 50.sp,
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .padding(horizontal = 26.dp),
                            style = TextStyle(fontFamily = interFontFamily),
                            color = colorScheme.secondary
                        )
                        if (isKeyboardVisible.value) {
                            /*if (viewModel.isTablet()) {
                                Spacer(modifier = Modifier.height(800.dp))
                            } else {*/
                            Spacer(modifier = Modifier.height(200.dp))
                            /* }*/
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
                            Text(words[index].english,
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                color = colorScheme.secondary,
                                modifier = Modifier.offset {
                                    IntOffset(
                                        shake.value.roundToInt(),
                                        y = 0
                                    )
                                }
                            )
                        }
                        Column(
                            Modifier.padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Hint(
                                iconUsed = true,
                                initialText = "Description",
                                expandedText = words[currentWordIndex].description,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
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
                }
            }
            if (isAlertDialogEnabled) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    containerColor = colorScheme.primaryContainer,
                    onDismissRequest = { isAlertDialogEnabled = false },
                    title = { Text("Are you sure?", style = dialogHeader) },
                    text = {
                        Text(
                            "Are you sure you wanna close this window? Your progress will be lost.",
                            style = dialogMain
                        )
                    },
                    confirmButton = {
                        CustomButton(
                            onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            },
                            text = "Yes"
                        )
                    },
                    dismissButton = {
                        CustomButton(
                            onClick = {
                                isAlertDialogEnabled = false
                            },
                            text = "No"
                        )
                    }
                )
            }

        }
        if (isSourceEmpty) {
            AlertDialog(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                containerColor = colorScheme.primaryContainer,
                onDismissRequest = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                title = { Text("No words to form training on!", style = dialogHeader) },
                text = {
                    when (wordSourse) {
                        "Mistakes" -> {
                            Text(
                                "You quite literally have no mistakes to correct, as of now.\n" +
                                        "For now, keep up the good work!\nBut test to learn frequently" +
                                        " failed words can't be generated," +
                                        " for obvious reasons.", style = dialogMain
                            )
                        }
                        "Tags" -> {
                            Text(
                                "Your tag doesn't contain enough words to form a training.\n" +
                                        " Would you like to be navigated to tag folders screen?",
                                style = dialogMain
                            )
                        }
                        else -> {
                            Text(
                                "To form a training list, you should first add some words.\n" +
                                        " Would you like to be navigated to Word list screen to add some new words?",
                                style = dialogMain
                            )
                        }
                    }
                },
                confirmButton = {
                    CustomButton(
                        onClick = {
                            if (wordSourse == "Tags"){
                                navController.navigate("WordListScreen") {
                                    popUpTo("Tags") { inclusive = true }
                                }
                            }
                            else {
                                navController.navigate("WordListScreen") {
                                    popUpTo("WordListScreen") { inclusive = true }
                                }
                            }

                        },
                        text = "Yes, take me there"
                    )
                },
                dismissButton = {
                    CustomButton(
                        onClick = {
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        },
                        text = "Move to start screen"
                    )
                }
            )
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun WordTrainingScreenZen(
        viewModel: ViewModel, navController: NavHostController
    ) {
        val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
        val (isHintExpanded2, setHintExpanded2) = remember { mutableStateOf(false) }
        var currentWordIndex: Int by remember { mutableIntStateOf(0) }
        var earnedScore: Int by remember { mutableIntStateOf(0) }
        val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
        var resultList by remember { mutableStateOf(emptyList<TestWord>()) }
        var firstTry by remember { mutableStateOf(true) }
        var words by remember { mutableStateOf(emptyList<TestWord>()) }
        var isSourceEmpty by remember { mutableStateOf(false) }
        val startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
        var elapsedTime by remember { mutableLongStateOf(0L) }
        var dataAssignmentTrigger by remember { mutableLongStateOf(0L) }
        LaunchedEffect(dataAssignmentTrigger) {
            if (viewModel.isInitialized) {
                words = wordList.map { TestWord(it.english, it.russian, it.description, true) }
                    .shuffled()
                isSourceEmpty = viewModel.words.value.isNullOrEmpty()
                Log.d("WordTraining", "Words initialized $words")
            } else {

                Log.d("WordTraining", "ViewModel not initialized yet")
            }
        }
        LaunchedEffect(Unit) {
            while (true) {
                elapsedTime = System.currentTimeMillis() - startTime
                delay(1000L)
            }
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
        BackHandler {
            isAlertDialogEnabled = true
        }
        if (words.isEmpty()) {
            dataAssignmentTrigger = System.currentTimeMillis()

            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            Log.d("Loading screen", "Words didn't initialize, isSourceEmpty is $isSourceEmpty")
        } else {
            Scaffold(
                bottomBar = {
                    Column {
                        TextField(
                            value = textfield,
                            onValueChange = { textfield = it },
                            label = { Text("Write your translation here", color = Color.White) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (checkAnswer(
                                                textfield,
                                                words[currentWordIndex].russian
                                            )
                                        ) {
                                            if (firstTry) {
                                                earnedScore++
                                            }
                                            setHintExpanded(false)
                                            setHintExpanded2(false)
                                            if (currentWordIndex + 1 == words.size) {
                                                viewModel.checkForUpdatesHS(earnedScore)
                                                resultList = words.take(currentWordIndex + 1)
                                                Log.d(
                                                    "ZenTraining",
                                                    "Result list of learned words: $resultList"
                                                )
                                                navController.navigate(
                                                    "ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${
                                                        convertWordsToJson(
                                                            resultList
                                                        )
                                                    }"
                                                )
                                            }
                                            textfield = ""
                                            onClick()
                                            firstTry = true
                                        } else {
                                            if (firstTry) {
                                                firstTry = false
                                                earnedScore--
                                            }
                                            trigger = System.currentTimeMillis()
                                            words[currentWordIndex].isCorrect = false
                                            textfield = ""
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = colorScheme.tertiary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.colors(
                                cursorColor = colorScheme.primary
                            )
                        )
                        BottomAppBar(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.primary
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Skip",
                                        modifier = Modifier.clickable(onClick = {
                                            if (currentWordIndex + 1 == words.size) {
                                                words[currentWordIndex].isCorrect = false
                                                resultList = words.take(currentWordIndex + 1)
                                                Log.d(
                                                    "ZenTraining",
                                                    "Result list of learned words: $resultList"
                                                )
                                                navController.navigate(
                                                    "ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${
                                                        convertWordsToJson(
                                                            resultList
                                                        )
                                                    }"
                                                )
                                            } else {
                                                if (firstTry) {
                                                    earnedScore--
                                                } else {
                                                    firstTry = true
                                                }
                                                setHintExpanded(false)
                                                setHintExpanded2(false)
                                                words[currentWordIndex].isCorrect = false
                                                onClick()
                                                textfield = ""
                                            }
                                        })
                                    )
                                    VerticalDivider(
                                        color = colorScheme.secondary,
                                        thickness = 3.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Text(
                                        "Finish",
                                        modifier = Modifier.clickable(onClick =
                                        {
                                            words[currentWordIndex].isCorrect = false
                                            earnedScore--
                                            resultList = words.take(currentWordIndex + 1)
                                            Log.d(
                                                "ZenTraining",
                                                "Result list of learned words: $resultList"
                                            )
                                            navController.navigate(
                                                "ResultsScreenZen/${elapsedTime / 1000}/$earnedScore/${
                                                    convertWordsToJson(
                                                        resultList
                                                    )
                                                }"
                                            )
                                        })
                                    )
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                LaunchedEffect(isKeyboardVisible.value) {
                    if (!isKeyboardVisible.value) {
                        focusManager.clearFocus()
                    }
                }
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    Column() {
                        Text(
                            "Score: ${earnedScore}\n" +
                                    "Words count: ${words.size}",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(horizontal = 26.dp)
                                .padding(top = 50.dp),
                            color = colorScheme.secondary,
                            lineHeight = 40.sp,
                            fontFamily = interFontFamily
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
                            Text(words[index].english,
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                color = colorScheme.secondary,
                                modifier = Modifier.offset {
                                    IntOffset(
                                        shake.value.roundToInt(),
                                        y = 0
                                    )
                                }
                            )
                        }
                        Column(
                            Modifier.padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Hint(
                                iconUsed = true,
                                initialText = "Description",
                                expandedText = words[currentWordIndex].description,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
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
                }
            }
            if (isAlertDialogEnabled) {
                AlertDialog(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    containerColor = colorScheme.primaryContainer,
                    onDismissRequest = { isAlertDialogEnabled = false },
                    title = { Text("Are you sure?", style = dialogHeader) },
                    text = {
                        Text(
                            "Are you sure you wanna close this window? Your progress will be lost.",
                            style = dialogMain
                        )
                    },
                    confirmButton = {
                        CustomButton(
                            onClick = {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }, text = "Yes"
                        )
                    },
                    dismissButton = {
                        CustomButton(
                            onClick = {
                                isAlertDialogEnabled = false
                            }, text = "No"
                        )
                    }
                )
            }
        }
        if (isSourceEmpty) {
            AlertDialog(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                containerColor = colorScheme.primaryContainer,
                onDismissRequest = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                title = { Text("No words to form training on!", style = dialogHeader) },
                text = {
                    Text(
                        "To form a training list, you should first add some words.\n" +
                                " Would you like to be navigated to Word list screen to add some new words?",
                        style = dialogMain
                    )
                },
                confirmButton = {
                    CustomButton(
                        onClick = {
                            navController.navigate("WordListScreen") {
                                popUpTo("WordListScreen") { inclusive = true }
                            }
                        }, text = "Move to Word list screen"
                    )

                },
                dismissButton = {
                    CustomButton(
                        onClick = {
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        }, text = "Move to start screen"
                    )
                }
            )
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun WordTrainingScreenDescription(
        viewModel: ViewModel, navController: NavHostController, wordCount: Int, type: String
    ) {
        val (isHintExpanded, setHintExpanded) = remember { mutableStateOf(false) }
        var currentWordIndex: Int by remember { mutableIntStateOf(0) }
        val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
        var words by remember { mutableStateOf(emptyList<TestWord>()) }
        var isSourceEmpty by remember { mutableStateOf(false) }
        val startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
        var elapsedTime by remember { mutableLongStateOf(0L) }
        var isAlertDialogEnabled by remember { mutableStateOf(false) }
        val isKeyboardVisible = rememberKeyboardVisibilityObserver()
        var rightAnswer by remember { mutableStateOf("") }
        var selectedAnswer by remember { mutableStateOf<String?>(null) }
        var showResults by remember { mutableStateOf<Boolean?>(false) }
        var options by remember { mutableStateOf<List<String>>(emptyList()) }
        var wrongAnswers by remember { mutableStateOf<Set<String>>(emptySet()) }
        var dataAssignmentTrigger by remember { mutableLongStateOf(0L) }
        fun resetAnswersState() {
            selectedAnswer = null
            showResults = false
            wrongAnswers = emptySet()
        }

        val wordLimit = when (type) {
            "Last 10" -> 10
            "Last 25" -> 25
            else -> wordCount
        }
        LaunchedEffect(dataAssignmentTrigger) {
            if (viewModel.isInitialized) {
                if (wordList.size >= 4) {
                    isSourceEmpty = false
                    words = when {
                        type == "Last 10" || type == "Last 25" -> {
                            wordList.filter { it.description != "not provided" && it.description != "Not provided" }
                                .map { TestWord(it.english, it.russian, it.description, true) }
                                .takeLast(wordLimit).shuffled()
                        }

                        else -> {
                            wordList.filter { it.description != "not provided" && it.description != "Not provided" }
                                .map { TestWord(it.english, it.russian, it.description, true) }
                                .shuffled().takeLast(wordLimit)
                        }
                    }
                    rightAnswer = words[currentWordIndex].english
                } else {
                    isSourceEmpty = true
                }
            } else {
                Log.d(
                    "WordTraining",
                    "ViewModel not initialized yet (${viewModel.isInitialized}) //"
                )
            }
        }
        LaunchedEffect(Unit) {
            dataAssignmentTrigger = System.currentTimeMillis()
            while (true) {
                elapsedTime = System.currentTimeMillis() - startTime
                delay(1000L)
            }
        }

        val goToNewWord: () -> Unit = {
            if ((currentWordIndex + 1) < words.size) {
                currentWordIndex++
                rightAnswer = words[currentWordIndex].english
                resetAnswersState()
            }
        }
        BackHandler {
            isAlertDialogEnabled = true
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
            if (viewModel.isInitialized) {
                dataAssignmentTrigger = System.currentTimeMillis()
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
                Log.d(
                    "Loading screen",
                    "Words didn't initialize isSourceEmpty is $isSourceEmpty isInitialized is ${viewModel.isInitialized}"
                )
            }

        } else {
            if (words.size >= 4) {
                Scaffold(
                    bottomBar = {
                        BottomAppBar(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Skip",
                                    modifier = Modifier.clickable(onClick = {
                                        if (currentWordIndex + 1 == words.size) {
                                            words[currentWordIndex].isCorrect = false
                                            navController.navigate(
                                                "ResultsScreen/${elapsedTime / 1000}/$type/${
                                                    convertWordsToJson(
                                                        words
                                                    )
                                                }/Description"
                                            )
                                        } else {
                                            setHintExpanded(false)
                                            words[currentWordIndex].isCorrect = false
                                            goToNewWord()
                                        }
                                    }),
                                    color = colorScheme.primary
                                )
                            }
                        }
                    }
                ) {
                    Column() {
                        Column() {
                            Text(
                                "${currentWordIndex + 1}/${words.size}",
                                fontSize = 50.sp,
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .padding(horizontal = 16.dp),
                                color = colorScheme.secondary,
                                style = TextStyle(fontFamily = interFontFamily)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 25.dp)
                                .fillMaxSize(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (isKeyboardVisible.value) {
                                Spacer(modifier = Modifier.height(200.dp))
                            }
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
                                Text(words[index].description,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 26.sp,
                                    textAlign = TextAlign.Center,
                                    color = colorScheme.secondary,
                                    modifier = Modifier.offset {
                                        IntOffset(
                                            shake.value.roundToInt(),
                                            y = 0
                                        )
                                    }
                                )
                            }
                            Hint(
                                iconUsed = true,
                                initialText = "Translation",
                                expandedText = words[currentWordIndex].russian,
                                icon = Icons.Default.Info,
                                isExpanded = isHintExpanded,
                                onExpandChange = setHintExpanded
                            )
                            if (words.size >= 4) {
                                LaunchedEffect(currentWordIndex) {
                                    val randomPosition = (0..3).random()
                                    val remainingOptions =
                                        wordList.filter { it.english != rightAnswer }.shuffled()
                                            .take(3)
                                            .map { it.english }
                                    options = mutableListOf<String>().apply {
                                        addAll(remainingOptions)
                                        add(randomPosition, rightAnswer)
                                    }
                                }
                                @Composable
                                fun getButtonColor(option: String): Color {
                                    return when {
                                        showResults == true && option == rightAnswer -> PastelGreen
                                        wrongAnswers.contains(option) -> WrongRed
                                        else -> colorScheme.primaryContainer
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
                                    if (checkAnswer(
                                            selectedOption,
                                            words[currentWordIndex].english
                                        )
                                    ) {
                                        showResults = true
                                        if (currentWordIndex + 1 == words.size) {
                                            navController.navigate(
                                                "ResultsScreen/${elapsedTime / 1000}/$type/${
                                                    convertWordsToJson(
                                                        words
                                                    )
                                                }/Description"
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
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 48.dp)
                                ) {
                                    options.forEachIndexed { index, option ->
                                        Button(
                                            onClick = { onClick(option) },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorScheme.tertiary.copy(
                                                    alpha = 0.5f
                                                ), contentColor = buttonColors[index]
                                            ),
                                            modifier = Modifier
                                                .width(200.dp)
                                        ) {
                                            Text(
                                                option,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(3.dp),
                                                textAlign = TextAlign.Center,
                                                style = TextStyle(
                                                    fontFamily = interFontFamily,
                                                    fontWeight = FontWeight.Normal,
                                                    fontStyle = FontStyle.Normal,
                                                    fontSize = 18.sp
                                                )
                                            )

                                        }
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
                        containerColor = colorScheme.primaryContainer,
                        onDismissRequest = { isAlertDialogEnabled = false },
                        title = { Text("Are you sure?", style = dialogHeader) },
                        text = {
                            Text(
                                "Are you sure you wanna close this window? Your progress will be lost.",
                                style = dialogMain
                            )
                        },
                        confirmButton = {
                            CustomButton(
                                onClick = {
                                    navController.navigate("start") {
                                        popUpTo("start") { inclusive = true }
                                    }
                                }, text = "Yes"
                            )

                        },
                        dismissButton = {
                            CustomButton(
                                onClick = {
                                    isAlertDialogEnabled = false
                                }, text = "No"
                            )
                        }
                    )
                }
            }
        }
        if (isSourceEmpty) {
            AlertDialog(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                containerColor = colorScheme.primaryContainer,
                onDismissRequest = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                title = { Text("Not enough words to form training on!", style = dialogHeader) },
                text = {
                    Text(
                        "To form a training list, you should first add some words.\n" +
                                " Would you like to be navigated to Word list screen to add some new words?",
                        style = dialogMain
                    )
                },
                confirmButton = {
                    CustomButton(
                        onClick = {
                            navController.navigate("WordListScreen") {
                                popUpTo("WordListScreen") { inclusive = true }
                            }
                        }, text = "Move to Word list screen"
                    )
                },
                dismissButton = {
                    CustomButton(
                        onClick = {
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        }, text = "Move to start screen"
                    )
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
            learnedWords.forEach { testWord ->
                if (!testWord.isCorrect) {
                    if (!viewModel.isWordInFailedDatabase(testWord.english)) {
                        val wordFailed = DBType.WordsFailed(
                            english = testWord.english,
                            russian = testWord.russian,
                            description = testWord.description,
                            timesPractised = 0
                        )
                        viewModel.addWordFailed(word = wordFailed)
                    } else {
                        viewModel.decrementTraining(testWord.english)
                    }
                } else {
                    if (viewModel.isWordInFailedDatabase(testWord.english)) {
                        viewModel.incrementTraining(testWord.english)
                    }
                }
            }
            viewModel.checkForDeletion()
        }
        BackHandler {
            navController.navigate("start")
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
                            .padding(vertical = 8.dp, horizontal = 48.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (selectedMode != "Description") {
                            IconButton(onClick = {
                                if (selectedType != "Mistakes") {
                                    navController.navigate(
                                        "WordTrainingScreen/$wordCount/$selectedType/${
                                            convertWordsToJson(learnedWords)
                                        }/Refresh"
                                    )
                                } else {
                                    navController.navigate(
                                        "WordTrainingScreen/$wordCount/$selectedType/${
                                            convertWordsToJson(learnedWords)
                                        }/RefreshMistakes"
                                    )
                                }
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                            }
                        }
                        IconButton(onClick = {
                            when (selectedMode){
                                "Description" -> {
                                    navController.navigate(
                                        "WordTrainingScreenDescription/$wordCount/$selectedType"
                                    )
                                }
                                else -> {
                                    navController.navigate(
                                        "WordTrainingScreen/$wordCount/$selectedType/${
                                            convertWordsToJson(wordsPlaceholder)
                                        }/{$selectedMode}"
                                    )
                                }
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
                    fontFamily = montserratFontFamily,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Results:\n",
                    color = colorScheme.secondary,
                    style = standartText
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(3.dp, colorScheme.secondary),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        state = scrollState
                    ) {
                        items(learnedWords) { word ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 2.dp
                                    ),
                                    text = "${word.english} - ${word.russian}",
                                    color = colorScheme.primaryContainer,
                                    style = standartText,
                                    fontSize = 20.sp
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
                                    colors = listOf(
                                        Color.Transparent,
                                        colorScheme.secondary.copy(alpha = 0.5f)
                                    ),
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
                        // First Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
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
                                    .padding(bottom = 16.dp)
                                    .padding(horizontal = 16.dp)
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
                        // Second Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                text = buildAnnotatedString {
                                    withStyle(style = styleHeader) {
                                        append("Test type\n")
                                    }
                                    withStyle(style = styleText) {
                                        append("$selectedType Words\n$selectedMode")
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
        BackHandler {
            navController.navigate("start")
        }
        LaunchedEffect(Unit) {
            Log.d("ResultsScreenZen", "Learned words: $learnedWords")
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
                    color = colorScheme.secondary
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(3.dp, colorScheme.secondary),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
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
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 2.dp
                                    ),
                                    fontSize = 20.sp,
                                    text = "${word.english} - ${word.russian}",
                                    color = colorScheme.primaryContainer
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
                                    colors = listOf(
                                        Color.Transparent,
                                        colorScheme.secondary.copy(alpha = 0.5f)
                                    ),
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
                        // First Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .padding(horizontal = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                text = buildAnnotatedString {
                                    withStyle(style = styleHeader) {
                                        append("Test type\n")
                                    }
                                    withStyle(style = styleText) {
                                        append("Zen")
                                    }
                                }
                            )
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
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
                                    .padding(bottom = 16.dp)
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
                                    .padding(bottom = 16.dp)
                                    .padding(horizontal = 16.dp)
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
                                    .padding(horizontal = 16.dp)
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
                                    .padding(horizontal = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                text = buildAnnotatedString {
                                    withStyle(style = styleHeader) {
                                        append("Highest score\n")
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
