package com.example.sigmaenglish.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.example.sigmaenglish.database.DBType
import com.example.sigmaenglish.navigation.GuideChapters
import com.example.sigmaenglish.navigation.GuideChapters.MainScreenGuide
import com.example.sigmaenglish.navigation.NavigationComponent
import com.example.sigmaenglish.ui.theme.GoldSchemeBrown
import com.example.sigmaenglish.ui.theme.GoldSchemeWhite
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.ui.theme.dialogMain
import com.example.sigmaenglish.ui.theme.montserratFontFamily
import com.example.sigmaenglish.ui.theme.standartText
import com.example.sigmaenglish.viewModel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.window.Dialog
import com.example.sigmaenglish.ui.theme.GoldSchemeYellow


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
                val viewModel: ViewModel by viewModels()
                val wordList: List<DBType.Word> by viewModel.words.observeAsState(emptyList())
                NavigationComponent(viewModel)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun StartScreen(navController: NavHostController) {
    val activity = LocalContext.current as? Activity ?: return
    val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
    insetsController.isAppearanceLightStatusBars = true
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
                LogoImage()
                CustomButton(onClick = { navController.navigate("WordListScreen") }, text = "Words list")
                CustomButton(onClick = { navController.navigate("trainingMenu") }, text = "Training")
                CustomButton(onClick = { navController.navigate("guide") }, text = "How to use")
                CustomButton(onClick = { navController.navigate("Tags") }, text = "Tags folders")
            }
        }
}
@Composable
fun ScreenGuide(navController: NavHostController) {
    BackHandler {
        navController.navigate("start")
    }
    val items = listOf(
        ExpandableItem(
            title = "1. Main screen",
            content = {
                MainScreenGuide()
            }
        ),
        ExpandableItem(
            title = "2. Word List screen",
            content = {
                GuideChapters.WordListScreenGuide()
            },
            children = listOf(
                ExpandableItem(
                    title = "2.1 Adding words",
                    content = {
                        GuideChapters.WordListScreenGuide_Add()
                    }
                ),
                ExpandableItem(
                    title = "2.2 Import words from notes", content = { GuideChapters.WordListScreenGuide_Import() })
            )
        ),
        ExpandableItem(
            title = "3. Word training guide",
            content = {
                GuideChapters.WordTrainingGuide()
            },
            children = listOf(
                ExpandableItem(
                    title = "3.1 Classic mode",
                    content = {
                        GuideChapters.ClassicTrainingGuide()
                    }
                ),
                ExpandableItem(
                    title = "3.2 Mistakes practice",
                    content = {
                        GuideChapters.MistakesTrainingGuide()
                    }
                ),
                ExpandableItem(
                    title = "3.3 Description mode",
                    content = {
                        GuideChapters.DescriptionTrainingGuide()
                    }
                ),
                ExpandableItem(
                    title = "3.4 Zen mode",
                    content = {
                        GuideChapters.ZenTrainingGuide()
                    }
                ),
            )
        ),
        ExpandableItem(
            title = "4. Results",
            content = {
                GuideChapters.ResultsScreen()
            }
        )
    )
    Column(modifier = Modifier
        .background(colorScheme.primary),) {
        Text(
            fontSize = 40.sp,
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            textAlign = TextAlign.Center,
            text = "How to use",
            color = colorScheme.secondary,
            style = standartText
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primary),
            contentAlignment = Alignment.TopStart,
        ) {
            ExpandableList(items)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordListScreen(viewModel: ViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val isKeyboardVisible = rememberKeyboardVisibilityObserver()
    val focusManager = LocalFocusManager.current
    val wordList by viewModel.words.observeAsState(emptyList())
    var addWordDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<DBType.Word?>(null) }
    var importFromNotesDialog by remember { mutableStateOf(false) }
    var exportWordsDialog by remember { mutableStateOf(false) }
    var resetMistakesListDialog by remember { mutableStateOf(false)}
    var isSortingDialogEnabled by remember { mutableStateOf(false) }
    val sortingOptions = listOf("Alphabetical ascending", "Alphabetical descending", "Newest", "Oldest", "Favorites")
    var selectedOption by remember { mutableStateOf<String>("Newest") }
    var searchText by remember {
        mutableStateOf("")
    }
    val filteredList = wordList.filter{
        it.english.contains(searchText, ignoreCase = true) or it.russian.contains(searchText, ignoreCase = true)
    }
    val sortedList = when (selectedOption) {
        "Alphabetical ascending" -> filteredList.sortedBy { it.english }
        "Alphabetical descending" -> filteredList.sortedByDescending { it.english }
        "Newest" -> filteredList.sortedByDescending { it.id }
        "Oldest" -> filteredList.sortedBy { it.id }
        "Favorites" -> filteredList.filter { it.favorite }
        else -> filteredList
    }
    BackHandler {
        navController.navigate("start")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary,
                ),
                title = {
                    Column(modifier = Modifier
                        .height(150.dp)
                        .padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Words list", modifier = Modifier.padding(vertical = 10.dp), fontFamily = montserratFontFamily, fontWeight = FontWeight.SemiBold)
                            Row {
                                IconButton(onClick = {
                                    resetMistakesListDialog  = true
                                }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Exit")
                                }
                                IconButton(onClick = {
                                    importFromNotesDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "import from notes")
                                }
                                IconButton(onClick = {
                                    exportWordsDialog = true
                                }) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "import from notes")
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(colorScheme.secondary),
                                modifier = Modifier
                                    .clickable(onClick = { })
                                    .padding(all = 0.dp)
                                    .border(
                                        BorderStroke(2.dp, color = colorScheme.tertiary),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(all = 0.dp)
                                        .width(260.dp)
                                        .height(58.dp)
                                ) {
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Icon(Icons.Default.Search, contentDescription = "Search", tint = GoldSchemeWhite, modifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp))
                                    TextField(
                                        value = searchText,
                                        onValueChange = { newText -> searchText = newText },
                                        placeholder = {
                                            Text(
                                                color = colorScheme.tertiary,
                                                text = "Search",
                                                style = TextStyle(
                                                    fontFamily = montserratFontFamily,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontStyle = FontStyle.Normal,
                                                    fontSize = 14.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 0.dp)
                                            )
                                        },
                                        textStyle = TextStyle(
                                            fontFamily = montserratFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontStyle = FontStyle.Normal,
                                            fontSize = 14.sp
                                        ),
                                        modifier = Modifier
                                            .padding(horizontal = 0.dp)
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .focusRequester(focusRequester),

                                        colors = TextFieldDefaults.colors(
                                            focusedTextColor = GoldSchemeWhite,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent

                                        ),


                                    )
                                }
                            }
                            IconButton(onClick = { isSortingDialogEnabled = true
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Exit", tint = GoldSchemeWhite)
                            }
                        }
                    }

                }, modifier = Modifier.fillMaxHeight(
                    if(viewModel.isTablet()){0.16f
                    }
                    else {
                        0.23f
                    })

            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                contentColor = colorScheme.primary,
                containerColor = colorScheme.secondary,
                text = { Text("Add Word", fontFamily = montserratFontFamily) },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                onClick = { addWordDialog = true },
            )
        }
    ) { innerPadding ->
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
                            TableCellHeader(text = "Original", weight = 1f)
                            TableCellHeader(text = "Translation", weight = 1f)
                        }
                    }
                    itemsIndexed(sortedList) { index, word ->
                        var isExpanded by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { isExpanded = !isExpanded },
                                    onLongClick = { selectedWord = word }
                                ),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableCell(text = word.english, weight = 1f, null)
                                TableCell(text = word.russian, weight = 1f, null)
                            }
                            if(index == sortedList.size - 1 )HorizontalDivider(thickness = 1.dp, color = colorScheme.secondary)
                            Crossfade(
                                targetState = isExpanded,
                                animationSpec = tween(durationMillis = 300)
                            ) { expanded ->
                                if (expanded) {
                                    var isFavorite = word.favorite
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(){
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                tint = Color.Black,
                                                contentDescription = "Info",
                                                modifier = Modifier.padding(horizontal = 0.dp)
                                            )
                                            Text(
                                                color = colorScheme.primaryContainer,
                                                style = standartText,
                                                text = word.description,
                                                modifier = Modifier.padding(horizontal = 32.dp)
                                            )
                                        }

                                        IconButton(onClick = {
                                            isFavorite = !isFavorite

                                            viewModel.updateWord(word.copy(
                                                favorite = isFavorite
                                            ))
                                        }) {
                                            Box(contentAlignment = Alignment.Center) {
                                                // Larger star for the outline
                                                Icon(
                                                    imageVector = Icons.Filled.Star,
                                                    contentDescription = null,
                                                    tint = GoldSchemeBrown,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                                // Smaller star for the main icon
                                                Icon(
                                                    imageVector = Icons.Filled.Star,
                                                    contentDescription = if (isFavorite) "Unmark as favorite" else "Mark as favorite",
                                                    tint = if (isFavorite) GoldSchemeBrown else GoldSchemeWhite,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }
    LaunchedEffect(isKeyboardVisible.value) {
        if (!isKeyboardVisible.value) {
            focusManager.clearFocus()
        }
    }
    if (addWordDialog) {
        AddWordDialog(
            onConfirm = { english, russian, description ->
                val word = DBType.Word(
                    english = english.trimEnd(),
                    russian = russian.trimEnd(),
                    description = description,
                    favorite = false
                )
                viewModel.addWord(word)
                addWordDialog = false
            },
            onDismiss = { addWordDialog = false }
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
                        favorite = false
                    )
                    viewModel.addWord(newWord)
                }
            },
            onDismiss = { importFromNotesDialog = false }
        )
    }
    if (exportWordsDialog) {
        ExportWordsDialog(
            onDismiss = { exportWordsDialog = false }, context = context, wordsFormated = viewModel.getWordsExportString()
        )
    }
            selectedWord?.let { word ->
                val coroutineScope = rememberCoroutineScope()

                WordManagementDialog(
                    word = word,
                    onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteWord(word)
                            viewModel.deleteMistakenWord(word.english)
                            selectedWord = null
                        }
                    },
                    onUpdate = { updatedWord ->
                        coroutineScope.launch {
                            viewModel.updateWord(updatedWord)

                            val wordInFailedDb = viewModel.getWordIdIfExists(selectedWord!!.english)

                            if (wordInFailedDb != null) {
                                viewModel.updateWordFailed(
                                    DBType.WordsFailed(
                                        english = updatedWord.english,
                                        russian = updatedWord.russian,
                                        description = updatedWord.description,
                                        id = wordInFailedDb,
                                        timesPractised = 0
                                    )
                                )
                            }

                            selectedWord = null
                        }
                    },
                    onDismiss = { selectedWord = null }
                )
            }

    if (isSortingDialogEnabled){
        OptionDialog(options = sortingOptions, onOptionSelected = {
            option -> selectedOption = option
            isSortingDialogEnabled = false
        },
            onDismiss = {
                isSortingDialogEnabled = false
            }, selectedOption = selectedOption)
    }
    if (resetMistakesListDialog) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            containerColor = colorScheme.primaryContainer,
            onDismissRequest = { resetMistakesListDialog = false },
            title = { Text("Mistakes reset", style = dialogHeader) },
            text = {
                Text("Are you sure you wanna reset mistakes words list?", style = dialogMain)
            },
            confirmButton = {
                CustomButton(
                    onClick = {
                        viewModel.deleteAllMistakenWords()
                        resetMistakesListDialog = false
                    },
                    text = "Yes")
            },
            dismissButton = {
                CustomButton(
                    onClick = {
                        resetMistakesListDialog = false
                    },
                    text = "No")
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TagsLibrary(viewModel: ViewModel, navController: NavHostController){
    val tagsList by viewModel.tags.observeAsState(emptyList())
    var selectedTag by remember { mutableStateOf<DBType.Tag?>(null) }
    var toggleAddTagDialog by remember { mutableStateOf(false) }
    BackHandler {
        navController.navigate("start")
    }
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
                            "Tags library",

                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                            IconButton(onClick = {
                                toggleAddTagDialog = true
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add new tag folder")
                            }
/*                            IconButton(onClick = {
                            }) {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Delete tag folders"
                                )
                            }*/

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
                                    navController.navigate("SelectedTagFolder")
                                },
                                onLongClick = { selectedTag = tag }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            TableCell(text = tag.name, weight = 1f, null)
                        }
                    }
                }
            }
            }
        }
    selectedTag?.let{ tag ->
        val coroutineScope = rememberCoroutineScope()
/*        AlertDialog(
            onDismissRequest = {selectedTag = null},
            title = { Text("Delete tag", style = dialogHeader) },
            text = {
                Text("Do you wish to delete this tag folder?", style = standartText)
            },
            confirmButton = {
                CustomButton(
                    onClick = {
                   viewModel.deleteTag(tag)
                        selectedTag = null
                    }, text = "Delete")

            },
            dismissButton = {
                CustomButton(
                    onClick = {selectedTag = null}, text = "Cancel")
            }
        )*/
        TagFolderManagementDialog(
            tag = tag,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteTag(tag)
                    selectedTag = null
                }
            },
            onUpdate = { updatedTag ->
                coroutineScope.launch {
                    viewModel.updateTag(updatedTag)
                    }
                    selectedTag = null

            },
            onDismiss = { selectedTag = null }
        )
    }
    if (toggleAddTagDialog){
        var enableButton by remember { mutableStateOf(false)}
        var tagName by remember { mutableStateOf("") }
        fun validateInput( name: String): Boolean {
            return name.isNotEmpty()
        }
        AlertDialog(
            onDismissRequest = {toggleAddTagDialog = false},
            title = { Text("Add new tag folder", style = dialogHeader) },
            text = {
                TextField(
                    value = tagName,
                    onValueChange = { tagName = it
                        if(validateInput(tagName)){enableButton = true}},
                    label = { Text("English") }
                )
            },
            confirmButton = {
                CustomButton(
                    enabled = enableButton,
                    onClick = {
                            viewModel.addTag(DBType.Tag(name = tagName, numbers = emptyList()))
                            toggleAddTagDialog = false

                    }, text = "Update")

            },
            dismissButton = {
                CustomButton(
                    onClick = {toggleAddTagDialog = false}, text = "Cancel")
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SelectedTagScreen(viewModel: ViewModel, navController: NavHostController) {

    val selectedTag by viewModel.selectedTag.observeAsState(null)
    val tags by viewModel.tags.observeAsState(emptyList())
    val words by viewModel.words.observeAsState(emptyList())
    val folderValue by remember(words, selectedTag) {
        derivedStateOf {
            words.filter { it.id in (selectedTag?.numbers ?: emptyList()) }
        }
    }

    val checkedIndices = mutableSetOf<Int>()
    var updateWordsTrigger by remember { mutableLongStateOf(0L) }
    var toggleSelection by remember {
        mutableStateOf(false)
    }
    var toggleAddition by remember {
        mutableStateOf(false)
    }
    BackHandler {
        navController.navigate("Tags")
    }
/*    LaunchedEffect(selectedTag, Unit, tags, updateWordsTrigger) {
        Log.d("SelectedTagScreen", "foldervalue is ${folderValue}, selected tag is $selectedTag")
        tags.find { it.id == selectedTag?.id }?.let { viewModel.setSelectedTag(it) }
        folderValue = words.filter{ it.id in (selectedTag?.numbers ?: emptyList()) }.map { it }
        Log.d("SelectedTagScreen", "foldervalue is ${folderValue}, selected tag is $selectedTag")
    }*/
    var selectedWord by remember { mutableStateOf<DBType.Word?>(null) }
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
                            "${selectedTag?.name}",
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row (modifier = Modifier.animateContentSize()) {
                            IconButton(onClick = {
                                toggleAddition = !toggleAddition
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add new words to the folder")
                            }
                            IconButton(onClick = {
                                toggleSelection = !toggleSelection
                            })
                            {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Delete words from folder"
                                )
                            }
                            if(toggleSelection) {
                                IconButton(onClick = {
                                        selectedTag?.let { viewModel.updateTag(it.copy(numbers = (selectedTag!!.numbers - checkedIndices))) }
                                })
                                {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete words from folder",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
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
                    .padding(innerPadding)
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
                    itemsIndexed(folderValue){ _, word ->
                        var currentState by remember { mutableStateOf(word.id in checkedIndices) }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { },
                                onLongClick = {selectedWord = word })
                            .height(35.dp).animateContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            TableCell(text = word.english, weight = 1f, null)
                            TableCell(text = word.russian, weight = 1f, null)
                            if (toggleSelection) {
                                CheckboxCell(
                                    checked = currentState,
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            checkedIndices.add(word.id)
                                        } else {
                                            checkedIndices.remove(word.id)
                                        }
                                        currentState = (word.id in checkedIndices)
                                    },
                                    weight = 0.2f
                                )
                            }
                        }

                    }
                }
            }
        }
    }
    selectedWord?.let { word ->
        val coroutineScope = rememberCoroutineScope()

        WordManagementDialog(
            word = word,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteWord(word)
                    viewModel.deleteMistakenWord(word.english)
                    selectedWord = null
                }
            },
            onUpdate = { updatedWord ->
                coroutineScope.launch {
                    viewModel.updateWord(updatedWord)

                    val wordInFailedDb = viewModel.getWordIdIfExists(selectedWord!!.english)

                    if (wordInFailedDb != null) {
                        viewModel.updateWordFailed(
                            DBType.WordsFailed(
                                english = updatedWord.english,
                                russian = updatedWord.russian,
                                description = updatedWord.description,
                                id = wordInFailedDb,
                                timesPractised = 0
                            )
                        )
                    }
                    updateWordsTrigger = System.currentTimeMillis()
                    selectedWord = null
                }
            },
            onDismiss = { selectedWord = null }
        )
    }
    if (toggleAddition){
        val checkedIds by remember { mutableStateOf(mutableListOf<Int>()) }
        val availableWords = words.filter { it.id !in selectedTag?.numbers.orEmpty() }
        var buttonEnabled by remember { mutableStateOf(false) }
            Dialog(onDismissRequest = {toggleAddition = !toggleAddition},) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Words to Add",
                            style = dialogHeader
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(availableWords) { word ->
                                var currentState by remember { mutableStateOf(word.id in checkedIds) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = word.english, modifier = Modifier.weight(1f), style = standartText, color = GoldSchemeWhite)
                                    Checkbox(
                                        checked = currentState,
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) {
                                                checkedIds.add(word.id)
                                                currentState = word.id in checkedIds
                                                buttonEnabled = checkedIds.isNotEmpty()
                                                Log.d("SelectedTagScreen", "$checkedIds, word.id in checkedIds = ${word.id in checkedIds}")
                                            } else {
                                                checkedIds.remove(word.id)
                                                currentState = word.id in checkedIds
                                                buttonEnabled = checkedIds.isNotEmpty()
                                                Log.d("SelectedTagScreen", "$checkedIds, word.id in checkedIds = ${word.id in checkedIds}")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomButton(
                            "Add words",
                            onClick = {
                                if (checkedIds.isNotEmpty()) {
                                    val temp = (selectedTag?.numbers ?: emptyList()) + checkedIds.toList()
                                    selectedTag?.let { viewModel.updateTag(it.copy(numbers = temp)) }
                                    toggleAddition = !toggleAddition
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = buttonEnabled)
                    }
                }
        }
    }
}



