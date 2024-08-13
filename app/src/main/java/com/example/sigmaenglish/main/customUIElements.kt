package com.example.sigmaenglish.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sigmaenglish.Database.DBType
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.ui.theme.lightgray
import com.example.sigmaenglish.viewModel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        focusedTextColor =  Color.Black,
        disabledTextColor = Color.Black,
        unfocusedPlaceholderColor = colorScheme.secondary,
        focusedPlaceholderColor = colorScheme.secondary

    )
}
@Composable
fun customButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = colorScheme.tertiary,
        contentColor = colorScheme.secondary,
        disabledContainerColor = colorScheme.tertiary.copy(alpha = 0.7f),
        disabledContentColor = colorScheme.secondary.copy(alpha = 0.3f)
    )
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
            .padding(8.dp),
    )
}
@Composable
fun RowScope.TableCellHeader(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.White)
            .weight(weight)
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
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
    var enableButton by remember { mutableStateOf(false) }
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
                    colors = customTextFieldColors(),
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
                colors = customButtonColors(),
                enabled = enableButton,
                onClick = {
                    if(translationDescription.isNotEmpty()){
                        onConfirm(englishWord, russianWord, translationDescription)
                        onDismiss()
                    }
                    else{
                        onConfirm(englishWord, russianWord, "Not provided")
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                colors = customButtonColors(),
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun Hint(
    iconUsed: Boolean,
    initialText: String,
    expandedText: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    val displayText = if (isExpanded) expandedText else initialText
    Modifier.padding(20.dp)
    Card(
        shape = RoundedCornerShape(16.dp), // Set the round shape here
        colors = CardDefaults.cardColors(lightgray), // Set the container color here
        modifier = Modifier
            .clickable(onClick = { onExpandChange(!isExpanded) })
            .padding(all = 8.dp)
            .border(BorderStroke(2.dp, colorScheme.secondary), shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp) // Padding inside the Card
        ) {
            if(iconUsed) {
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
            else {

                Text(
                    color = Color.White,
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }


        }
    }
}

@Composable
fun ModeCard(
    mode: String,
    selectedScreen: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier) {
    SigmaEnglishTheme {

        Card(
            modifier = modifier
                .clickable(onClick = onSelect),
            border = BorderStroke(
                2.dp,
                if (mode == selectedScreen) Color.Black else Color.Transparent
            ),
            elevation = CardDefaults.elevatedCardElevation(4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardColors(
                contentColor = colorScheme.secondary,
                containerColor = colorScheme.tertiary,
                disabledContentColor = Color.DarkGray,
                disabledContainerColor = Color.DarkGray
            ),
            content = {
                Text(
                    text = mode,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
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
                    colors = customTextFieldColors(),
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
                colors = customButtonColors(),
                onClick = {
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
            Button(
                colors = customButtonColors(),
                onClick = onDelete) {
                Text("Delete")
            }
        }
    )
}

@Composable
fun ImportWordsDialog(
    onConfirm: (list: List<TemplateWord>) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var newTranslation by remember { mutableStateOf("") }
    var enableButton by remember { mutableStateOf(false) }
    var showInitialButtons by remember { mutableStateOf(true) }
    var enableConfirmButton by remember { mutableStateOf(false) }
    var noWordsDialog by remember { mutableStateOf(false) }
    var showIssueResolver by remember { mutableStateOf(false) }
    var blankIds by remember { mutableStateOf(mutableListOf<Int>()) }
    var parsedList by remember { mutableStateOf(mutableListOf<TemplateWord>()) }
    var currentBlankIndex by remember { mutableIntStateOf(0) }

    fun validateInput(eng: String): Boolean {
        return eng.isNotEmpty()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        title = { Text("Manage Word") },
        text = {
            Column {
                if (showInitialButtons) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            enableConfirmButton = validateInput(text)
                        },
                        label = { Text("Insert your copied text here") }
                    )
                    Button(
                        colors = customButtonColors(),
                        enabled = enableConfirmButton,
                        onClick = {
                            parsedList = stringParser(text)
                            text = ""
                            enableConfirmButton = false

                            blankIds = checkForBlanks(parsedList).toMutableList()
                            if(parsedList.isNotEmpty()) {
                                if (blankIds.isEmpty()) {
                                    showInitialButtons = false
                                    enableButton = true
                                } else {
                                    showIssueResolver = true
                                    showInitialButtons = false
                                }
                            }
                            else {
                                noWordsDialog = true
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                    if (noWordsDialog) {
                        AlertDialog(
                            text = {
                                Column (){
                                    Text("Please enter words!", color = colorScheme.secondary, fontSize = 16.sp)
                                } },
                            onDismissRequest = { noWordsDialog = false },
                            confirmButton = { Button(
                                colors = customButtonColors(),
                                onClick = { noWordsDialog = false}){
                                Text("Okay")
                            }}
                        )
                    }
                }
                if (showIssueResolver) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.6f)
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text("Issues left: ${blankIds.size}")
                            Text("Word: ${parsedList[blankIds[currentBlankIndex]].original}")
                            Text("Translation: ${parsedList[blankIds[currentBlankIndex]].translation}")
                            Text("Description: ${parsedList[blankIds[currentBlankIndex]].description}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newTranslation,
                            onValueChange = { newTranslation = it },
                            label = { Text("Enter new translation") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                colors = customButtonColors(),
                                onClick = {
                                    blankIds.getOrNull(currentBlankIndex)?.let {
                                        parsedList.removeAt(currentBlankIndex)
                                        blankIds.removeAt(currentBlankIndex)
                                        if (blankIds.isEmpty()) {
                                            enableButton = true
                                            showIssueResolver = false
                                        }
                                        if (currentBlankIndex >= blankIds.size) {
                                            currentBlankIndex = blankIds.size - 1
                                        }
                                        currentBlankIndex = currentBlankIndex
                                    }
                                }
                            ) {
                                Text("Delete word", fontSize = 14.sp)
                            }
                            Button(
                                colors = customButtonColors(),
                                onClick = {
                                    blankIds.getOrNull(currentBlankIndex)?.let {
                                        parsedList[it].translation = newTranslation
                                        newTranslation = ""
                                        blankIds.removeAt(currentBlankIndex)
                                        if (blankIds.isEmpty()) {
                                            enableButton = true
                                            showIssueResolver = false
                                        }
                                        if (currentBlankIndex >= blankIds.size) {
                                            currentBlankIndex = blankIds.size - 1
                                        }
                                        currentBlankIndex = currentBlankIndex
                                    }
                                }
                            ) {
                                Text("Set translation")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            IconButton(
                                onClick = {
                                    if (currentBlankIndex > 0) currentBlankIndex--
                                },
                                enabled = currentBlankIndex > 0
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Go to previous issue"
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (currentBlankIndex < blankIds.size - 1) currentBlankIndex++
                                },
                                enabled = currentBlankIndex < blankIds.size - 1
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Go to next issue"
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                colors = customButtonColors(),
                enabled = enableButton,
                onClick = {
                    onConfirm(parsedList)
                    onDismiss()
                }
            ) {
                Text("Add new words")
            }
        },
        dismissButton = {
            Button(
                colors = customButtonColors(),
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
