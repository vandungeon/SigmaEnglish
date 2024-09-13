package com.example.sigmaenglish.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sigmaenglish.database.DBType
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.ui.theme.interFontFamily
import com.example.sigmaenglish.ui.theme.montserratFontFamily

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
                        Text("Words list", modifier = Modifier.padding(vertical = 10.dp), fontFamily = montserratFontFamily, fontWeight = FontWeight.SemiBold)
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
                .background(color = colorScheme.primary)
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
            Surface(
                shadowElevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .animateContentSize()
                    .padding(all = 8.dp)
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
                        .padding(all = 8.dp)
                ) {
                        Text(
                            color = colorScheme.tertiary,
                            text = "display Text",
                            style = TextStyle(
                                fontFamily = montserratFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Normal,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }

                }
            }
            Button(
                onClick = { /* Handle click */ },

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.tertiary.copy(alpha = 0.5f),
                    contentColor = colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .width(200.dp)
                    .border(
                        BorderStroke(2.dp, color = colorScheme.tertiary),
                        shape = RoundedCornerShape(220.dp)
                    )
            ) {
                Text(
                    "option",
                    modifier = Modifier.fillMaxWidth().padding(3.dp),
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



    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        SigmaEnglishTheme {
            StartScreen(navController = rememberNavController())
        }
    }
@Preview(showBackground = true)
@Composable
fun PreviewCard() {
    SigmaEnglishTheme {

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
@Preview(showBackground = true)
fun ResultsPreview(){
    SigmaEnglishTheme {


        val timeSpent = 10
        val selectedType = "Standart"
        val sampleWords: List<TestWord> = listOf(
            TestWord(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
            TestWord(
                english = "book",
                russian = "книга",
                description = "A written or printed work",
                false
            ),
            TestWord(
                english = "cat",
                russian = "кот",
                description = "A small domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "dog",
                russian = "собака",
                description = "A domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "elephant",
                russian = "слон",
                description = "A large mammal with a trunk",
                true
            ),
            TestWord(
                english = "book",
                russian = "книга",
                description = "A written or printed work",
                false
            ),
            TestWord(
                english = "cat",
                russian = "кот",
                description = "A small domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "dog",
                russian = "собака",
                description = "A domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "elephant",
                russian = "слон",
                description = "A large mammal with a trunk",
                true
            ),
            TestWord(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
            TestWord(
                english = "book",
                russian = "книга",
                description = "A written or printed work",
                false
            ),
            TestWord(
                english = "cat",
                russian = "кот",
                description = "A small domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "dog",
                russian = "собака",
                description = "A domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "elephant",
                russian = "слон",
                description = "A large mammal with a trunk",
                true
            ),
            TestWord(
                english = "book",
                russian = "книга",
                description = "A written or printed work",
                false
            ),
            TestWord(
                english = "cat",
                russian = "кот",
                description = "A small domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "dog",
                russian = "собака",
                description = "A domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "elephant",
                russian = "слон",
                description = "A large mammal with a trunk",
                true
            ),
            TestWord(english = "apple", russian = "яблоко", description = "A sweet fruit", true),
            TestWord(
                english = "book",
                russian = "книга",
                description = "A written or printed work",
                false
            ),
            TestWord(
                english = "cat",
                russian = "кот",
                description = "A small domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "dog",
                russian = "собака",
                description = "A domesticated carnivorous mammal",
                true
            ),
            TestWord(
                english = "elephant",
                russian = "слон",
                description = "A large mammal with a trunk",
                true
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(17.dp))
                    .border(
                        BorderStroke(3.dp, colorScheme.secondary),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(sampleWords) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                fontSize = 20.sp,
                                text = "${word.english} - ${word.russian}",
                                color =
                                colorScheme.primaryContainer
                            //Color.Black
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
                            alpha = 1f
                        )
                )
            }
        }
    }
}