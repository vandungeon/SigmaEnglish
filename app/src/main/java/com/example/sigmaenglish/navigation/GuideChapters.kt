package com.example.sigmaenglish.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sigmaenglish.main.GuideImage
import com.example.sigmaenglish.R
import com.example.sigmaenglish.ui.theme.standartText

object GuideChapters {

    @Composable
    fun CustomText(text: String){
        Text(text = text, color = colorScheme.primaryContainer,
            style = standartText)
    }

    @Composable
    fun MainScreenGuide() {
        CustomText(text = "Main screen serves only 2 purposes except for guide you are currently reading." +
                "It enables you to go either Word List screen where you can manage your words, or Word Training where training process actually happens"
        )
        GuideImage(painter = painterResource(id = R.drawable.main_screen))

    }
    @Composable
    fun WordListScreenGuide() {
        CustomText(text = "Word List screen allows you to add, update words you wish to learn, or import them from notes."
        )
        GuideImage(painter = painterResource(id = R.drawable.word_list))
    }
    @Composable
    fun WordListScreenGuide_Add() {
        CustomText(text = "Icon at the bottom, \"Add word\", will show menu on screen, where you can fill information about word" +
                ", that means it's original form, translation and description. Notice that translation should be 1 word long, but description is not mandatory."

        )
        GuideImage(painter = painterResource(id = R.drawable.add_new_word))
    }
    @Composable
    fun WordListScreenGuide_Import() {
        CustomText(text = "Icon at the top shows menu on screen, that allows you to copy words from your notes, as long as they are in specified format shown on screen." +
                " If format doesn't match, your words will not be added."
        )
        GuideImage(painter = painterResource(id = R.drawable.import_from_notes))
        CustomText(text =  "\nIf you have entered two words in translation and otherwise everything else is right, your translation will move into description and new menu will appear, where you will be prompted to either set new translation(pencil icon) or remove word with issues (trash can icon)")
        GuideImage(painter = painterResource(id = R.drawable.import_issue))
    }
    @Composable
    fun WordTrainingGuide() {
        CustomText(text = "This the screen that leads to all training modes. There you just have to choose one that you need." +
                "\nThere are 4 total practice types - Classic, Mistakes, Description and zen. You can find type-specific information below."
        )
        GuideImage(painter = painterResource(id = R.drawable.select_mode))
        CustomText(text = "\nBut for now, let's talk about your options for almost every type." +
                " You will be prompted to choose amount of words you want to practise and from what pool - either Last 10 of words you added, Last 25, or All.")
        GuideImage(painter = painterResource(id = R.drawable.settings_screen))
    }
    @Composable
    fun ClassicTrainingGuide() {
        CustomText(text = "During Classic training you will be shown words that you have to translate and current progress at the top left corner." +
                " Once you decide with the translation, you must write it in Text Field and press button at the end of it to submit your answer." +
                "\nIf your answer is correct - good job! Otherwise, test won't move on." +
                " You can still try to guess, look at the hint(this will show word's description) or word translation, obviously by doing this you will cheat on yourself, but if you need it - go on." +
                "\nIf you can't answer, you can press button at the bottom to skip current word."
        )
        GuideImage(painter = painterResource(id = R.drawable.classic_training))
    }
    @Composable
    fun MistakesTrainingGuide() {
        CustomText(text = "Mistakes training is almost the same as Classic. Only difference being is source pool of words." +
                " When you practice in other modes, if your answer is wrong, word will be marked as one that you failed to translate." +
                "\nIt will be put in an invisible list that you cannot access. The only way to remove word from mistakes list is to answer properly 5 times in a row." +
                " if you fail once, your progress will be reset." +
                "\nSo in this mode you do a Classic type of training, but on a words you made mistakes until you no longer make them."
        )
    }
    @Composable
    fun DescriptionTrainingGuide() {
        CustomText(text = "During Description training you will be shown descriptions of words." +
                " By looking at this description you must figure out what word(in it's original form) fits this description." +
                "\nYou can still look at hint, which will be translation of a word, and skip answer, but be aware that unlike other types you need 4 or more words with proper descriptions to start test, for obvious reasons. "
        )
        GuideImage(painter = painterResource(id = R.drawable.description_training))
    }
    @Composable
    fun ZenTrainingGuide() {
        CustomText(text = "Zen training allows you to train words as much as you want in Classic style, until there are no more left. Unlike other modes you can finish Zen training when you want due to it's nature." +
                "\nAlso, your score during this training will be calculated." +
                " Each correct answer on first try will grant you 1 point, while wrong answer or skip will take 1 from you."
        )
        GuideImage(painter = painterResource(id = R.drawable.zen_training))
    }

    @Composable
    fun ResultsScreen(){
        CustomText(text = "Results screen provides you information on how you performed during the test." +
                "You can see list of words you trained here and were your answers correct, as well as other metrics" +
                " like time spent, type of training, amount of words, accuracy rate." +
                "\nThis screen is almost the same for all modes, except for slight changes in Result screen of Zen - there you will be also shown your score and your all-time high score.")
    GuideImage(painter = painterResource(id = R.drawable.results_screen))
        CustomText(text = "\nButtons at the bottom serve purpose of navigating you to screens you need after test is over." +
                " First will allow you to do the same training with same words again, second will generate same type of training but with new selection of words, third lead you to Mistakes practice and fourth to Main screen.")
    Spacer(modifier = Modifier.height(15.dp))
    }
}