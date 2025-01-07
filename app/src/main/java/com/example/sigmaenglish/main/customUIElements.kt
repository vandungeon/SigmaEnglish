package com.example.sigmaenglish.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sigmaenglish.database.DBType
import com.example.sigmaenglish.R
import com.example.sigmaenglish.ui.theme.GoldSchemeWhite
import com.example.sigmaenglish.ui.theme.SigmaEnglishTheme
import com.example.sigmaenglish.viewModel.ViewModel
import com.example.sigmaenglish.ui.theme.customText
import com.example.sigmaenglish.ui.theme.customTitle
import com.example.sigmaenglish.ui.theme.dialogMain
import com.example.sigmaenglish.ui.theme.hintText
import com.example.sigmaenglish.ui.theme.interFontFamily
import com.example.sigmaenglish.ui.theme.montserratFontFamily
import com.example.sigmaenglish.ui.theme.standartText
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import com.example.sigmaenglish.ui.theme.GoldSchemeBlack
import com.example.sigmaenglish.ui.theme.GoldSchemeYellow

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "My PNG Image",
        contentScale = ContentScale.Fit, // (Crop, Fit, FillBounds)
        modifier = Modifier.size(150.dp)
    )
}
@Composable
fun GuideImage(painter: Painter) {
    HorizontalDivider(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(top = 8.dp), color = colorScheme.primaryContainer)
    Image(
        painter = painter,
        contentDescription = "My PNG Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(300.dp)
    )
    HorizontalDivider(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 8.dp), color = colorScheme.primaryContainer)
}


data class ExpandableItem(
    val title: String,
    val content: @Composable (() -> Unit)? = null,
    val children: List<ExpandableItem> = emptyList()
)

@Composable
fun ExpandableItemComposable(item: ExpandableItem, level: Int = 0) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Column(
        modifier = Modifier
            .animateContentSize()
            .padding(start = (level * 16).dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Center the icon and text
            modifier = Modifier
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = item.title,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f) // Makes text take up remaining space
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.rotate(rotationAngle), // Rotate the icon
                tint = Color.Black
            )
        }

            if (expanded) {
                item.content?.let { content ->
                    content()
                }
                item.children.forEach {
                    ExpandableItemComposable(it, level + 1)
                }
            }
    }
}

@Composable
fun ExpandableList(items: List<ExpandableItem>) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        items(items) { item ->
            ExpandableItemComposable(item)
        }
    }
}

val styleHeader = customTitle.toSpanStyle()
val styleText = customText.toSpanStyle()

val dialogHeader = TextStyle(
    fontFamily = montserratFontFamily,
    color =  GoldSchemeWhite,
    fontWeight = FontWeight.W400,
    fontSize = 24.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
)
@Composable
fun customButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = colorScheme.secondary,
        contentColor = colorScheme.primary,
        disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
        disabledContentColor = Color.White.copy(alpha = 0.1f)
    )
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = customButtonColors(),
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        Text(text, fontFamily = montserratFontFamily)
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {

    Text(
        text = text,
        Modifier
            .border(
                BorderStroke(1.dp, colorScheme.secondary),
                shape = RectangleShape
            )
            .weight(weight)
            .padding(8.dp),
        color = colorScheme.primaryContainer,
        style = standartText
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
            .border(1.dp, colorScheme.secondary)
            .weight(weight)
            .padding(8.dp),
        textAlign = TextAlign.Center,
        color = colorScheme.primary,
        style = TextStyle(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.W700,
            fontStyle = FontStyle.Normal,
            fontSize = 18.sp
        )
    )
}

@Composable
fun AddWordDialog(
    onConfirm: (english: String, russian: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var translationDescription by remember { mutableStateOf("") }
    var englishWord by remember { mutableStateOf("") }
    var russianWord by remember { mutableStateOf("") }
    var enableButton by remember { mutableStateOf(false) }
    fun validateInput(eng: String, rus: String): Boolean {
        return eng.isNotEmpty() and rus.isNotEmpty() && !rus.contains(Regex(" \\S+")) && !eng.contains(Regex(" \\S+"))

    }
    AlertDialog(
        containerColor = colorScheme.primaryContainer,
        onDismissRequest = onDismiss,
        title = { Text("Add new word", style = dialogHeader) },
        text = {
            Column {
                TextField(
                    value = englishWord,
                    onValueChange = { englishWord = it
                        enableButton = validateInput(englishWord, russianWord)
                    },
                    label = { Text("English") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = russianWord,
                    onValueChange = { russianWord = it
                        enableButton = validateInput(englishWord, russianWord)
                    },
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
            CustomButton(
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
                },
                text = "Add")
        },
        dismissButton = {
            CustomButton(
                onClick = onDismiss
            , text = "Cancel")
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
    Surface(
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        modifier = Modifier
            .animateContentSize()
            .padding(all = 8.dp)
    ) {
        Modifier.padding(20.dp)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(colorScheme.secondary),
            modifier = Modifier
                .clickable(onClick = { onExpandChange(!isExpanded) })
                .padding(all = 0.dp)
                .border(
                    BorderStroke(2.dp, colorScheme.tertiary),
                    shape = RoundedCornerShape(16.dp)
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp), // Padding inside the Card
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (iconUsed) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Info",
                        modifier = Modifier.padding(horizontal = 0.dp),
                        tint = colorScheme.tertiary
                    )
                    Text(
                        color = colorScheme.tertiary,
                        text = displayText,
                        style = hintText,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                } else {
                    Text(
                        color = colorScheme.tertiary,
                        text = displayText,
                        style = hintText,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ModeCard(
    mode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    SigmaEnglishTheme {
        Card(
            modifier = modifier
                .clickable(onClick = onClick),
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
                        fontFamily = montserratFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Word", style = dialogHeader) },
        text = {
            Column {
                TextField(
                    value = englishWord,
                    onValueChange = { englishWord = it
                        if(validateInput(englishWord, russianWord)){enableButton = true}},
                    label = { Text("English") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = russianWord,
                    onValueChange = { russianWord = it
                        if(validateInput(englishWord, russianWord)){enableButton = true}},
                    label = { Text("Russian") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = translationDescription,
                    onValueChange = { translationDescription = it
                        if(validateInput(englishWord, russianWord)){enableButton = true}},
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            CustomButton(
                enabled = enableButton,
                onClick = {
                // Handle word update
                onUpdate(
                    word.copy(
                        english = englishWord.trimEnd(),
                        russian = russianWord.trimEnd(),
                        description = translationDescription
                    )
                )
            }, text = "Update")

        },
        dismissButton = {
            CustomButton(
                onClick = onDelete, text = "Delete")
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
    val code = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.primaryContainer,
        title = {
            Column {
                // Header text
                Text(
                    "Import from notes",
                    style = dialogHeader
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Main body text
                Text(
                    "Enter your data in format shown below. Translation should be 1 word long, description from 0 to how much needed:",
                    style = dialogMain
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Code-style text
                Text(
                    "Word - Translation (Description)",
                    style = code
                )
            }

        },
        text = {
            Column {
                if (showInitialButtons) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            enableConfirmButton = validateInput(text)
                        },
                        label = { Text("Insert copied text here") },
                        trailingIcon = {
                            if (enableConfirmButton) {
                                IconButton(
                                    onClick = {
                                        parsedList = stringParser(text)
                                        text = ""
                                        enableConfirmButton = false

                                        blankIds = checkForBlanks(parsedList).toMutableList()
                                        if (parsedList.isNotEmpty()) {
                                            if (blankIds.isEmpty()) {
                                                showInitialButtons = false
                                                enableButton = true
                                            } else {
                                                showIssueResolver = true
                                                showInitialButtons = false
                                            }
                                        } else {
                                            noWordsDialog = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Confirm",
                                        tint = colorScheme.primary
                                    )
                                }
                            }
                        }
                    )


                    if (noWordsDialog) {
                        AlertDialog(
                            text = {
                                Column {
                                    Text("Data you entered is not proper", style = dialogMain)
                                } },
                            onDismissRequest = { noWordsDialog = false },
                            confirmButton = { CustomButton(
                                onClick = { noWordsDialog = false},
                                text = "Okay")
                            }
                        )
                    }
                }
                if (showIssueResolver) {
                    var issuesLeft by remember { mutableIntStateOf(0) }
                    var issueWord by remember { mutableStateOf("") }
                    var issueTranslation by remember { mutableStateOf("") }
                    var issueDescription by remember { mutableStateOf("") }
                    issueWord = parsedList[blankIds[currentBlankIndex]].original
                    issueTranslation = parsedList[blankIds[currentBlankIndex]].translation
                    issueDescription = parsedList[blankIds[currentBlankIndex]].description
                    issuesLeft = blankIds.size
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text("Issues left: $issuesLeft ")
                            Text("Word: $issueWord")
                            Text("Translation: $issueTranslation")
                            Text("Description: $issueDescription")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newTranslation,
                            onValueChange = { newTranslation = it },
                            label = { Text("Enter new translation") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        blankIds.getOrNull(currentBlankIndex)?.let {
                                            val isValidTranslation = newTranslation.trim().split(Regex("""\s+""")).let {
                                                it.size == 1 || (it.size == 2 && it[0].contains('-'))
                                            }
                                            if(isValidTranslation) {
                                                parsedList[blankIds[currentBlankIndex]].translation = newTranslation
                                                newTranslation = ""
                                                blankIds.removeAt(currentBlankIndex)
                                                if (blankIds.isEmpty()) {
                                                    enableButton = true
                                                    showIssueResolver = false
                                                }
                                                else {
                                                    if (currentBlankIndex >= blankIds.size) {
                                                        currentBlankIndex--
                                                    }
                                                    issueWord = parsedList[blankIds[currentBlankIndex]].original
                                                    issueTranslation = parsedList[blankIds[currentBlankIndex]].translation
                                                    issueDescription = parsedList[blankIds[currentBlankIndex]].description
                                                    issuesLeft = blankIds.size
                                                }
                                            }
                                            else{
                                                noWordsDialog = true
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Create,
                                        contentDescription = "Send",
                                        tint = colorScheme.primary
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            IconButton(
                                onClick = {
                                    blankIds.getOrNull(currentBlankIndex)?.let {
                                        Log.d("Delete word", "Parsed list: $parsedList, blankIds: $blankIds, current index: $currentBlankIndex")
                                        parsedList.removeAt(
                                            blankIds[currentBlankIndex]
                                        )
                                        if (parsedList.isEmpty()) {
                                            enableButton = true
                                            showIssueResolver = false
                                        }
                                        val temp = blankIds.removeAt(currentBlankIndex)
                                        blankIds = blankIds.map { if (it > temp) it - 1 else it }.toMutableList()
                                        if (blankIds.isEmpty()) {
                                            enableButton = true
                                            showIssueResolver = false
                                        }
                                        else {
                                            if (currentBlankIndex >= blankIds.size) {
                                                currentBlankIndex -= 1

                                            }
                                            if (currentBlankIndex < 0) currentBlankIndex = 0

                                            issueWord = parsedList[blankIds[currentBlankIndex]].original
                                            issueTranslation = parsedList[blankIds[currentBlankIndex]].translation
                                            issueDescription = parsedList[blankIds[currentBlankIndex]].description
                                            issuesLeft = blankIds.size
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete word"
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (currentBlankIndex > 0) currentBlankIndex--
                                    issueWord = parsedList[blankIds[currentBlankIndex]].original
                                    issueTranslation = parsedList[blankIds[currentBlankIndex]].translation
                                    issueDescription = parsedList[blankIds[currentBlankIndex]].description
                                    issuesLeft = blankIds.size
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
                                    issueWord = parsedList[blankIds[currentBlankIndex]].original
                                    issueTranslation = parsedList[blankIds[currentBlankIndex]].translation
                                    issueDescription = parsedList[blankIds[currentBlankIndex]].description
                                    issuesLeft = blankIds.size
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
            CustomButton(
                enabled = enableButton,
                onClick = {
                    onConfirm(parsedList)
                    onDismiss()
                },text ="Add new words")
        },
        dismissButton = {
            CustomButton(
                onClick = onDismiss
            ,text ="Cancel")
        }
    )
}
@Composable
fun ExportWordsDialog(
    onDismiss: () -> Unit,
    wordsFormated: String,
    context: Context
) {
    val code = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    val clipboardText = wordsFormated
    var enableButton by remember { mutableStateOf(wordsFormated.isNotEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.primaryContainer,
        title = {
            Column {
                if(enableButton) {
                    Text(
                        "Export words to clipboard",
                        style = dialogHeader
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Your words will be copied to clipboard in format shown below:",
                        style = dialogMain
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Word - Translation (Description)",
                        style = code
                    )
                }
                else {
                    Text(
                        "Export words to clipboard",
                        style = dialogHeader
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "You are missing the words to export them, consider adding them.",
                        style = dialogMain
                    )
                }

            }

        },
        text = {},
        confirmButton = {
            CustomButton(
                enabled = enableButton,
                onClick = {
                    copyTextToClipboard(context, "Exported words", clipboardText);
                    onDismiss()
                },text ="Copy words")
        },
        dismissButton = {
            CustomButton(
                onClick = onDismiss
                ,text ="Cancel")
        }
    )

}
@Composable
fun OptionDialog(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    var _selectedOption by remember { mutableStateOf<String>(selectedOption) }

    AlertDialog(
        containerColor = colorScheme.primaryContainer,
        onDismissRequest = { onDismiss() },
        title = { Text("Choose how to sort words", style = dialogHeader) },
        text = {
            LazyColumn {
                items(options) { option ->
                    Text(
                        text = "•   $option",
                        style = if (option == _selectedOption) {TextStyle(
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = GoldSchemeYellow,
                            fontSize = 18.sp
                        )} else {
                            standartText
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                _selectedOption = option
                                onOptionSelected(option)
                                onDismiss()
                            }
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }, colors = customButtonColors()) {
                Text("Cancel")
            }
        }
    )
}

