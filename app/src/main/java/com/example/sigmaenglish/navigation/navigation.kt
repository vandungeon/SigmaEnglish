package com.example.sigmaenglish.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sigmaenglish.main.ResultsScreen
import com.example.sigmaenglish.main.ResultsScreenZen
import com.example.sigmaenglish.main.ScreenGuide
import com.example.sigmaenglish.main.SettingsScreen
import com.example.sigmaenglish.main.StartScreen
import com.example.sigmaenglish.main.TrainingMenu
import com.example.sigmaenglish.main.TestWord
import com.example.sigmaenglish.main.WordListScreen
import com.example.sigmaenglish.main.WordTrainingScreen
import com.example.sigmaenglish.main.WordTrainingScreenDescription
import com.example.sigmaenglish.main.WordTrainingScreenZen
import com.example.sigmaenglish.viewModel.ViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

fun convertWordsToJson(words: List<TestWord>): String {
    val gson = Gson()
    return gson.toJson(words)
}

fun convertJsonToWords(json: String): List<TestWord> {
    val gson = Gson()
    val type = object : TypeToken<List<TestWord>>() {}.type
    return gson.fromJson(json, type)
}

@Composable
fun NavigationComponent(viewModel: ViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("guide") { ScreenGuide(navController) }
        composable("WordListScreen") { WordListScreen(viewModel, navController) }
        composable("trainingMenu") { TrainingMenu(viewModel, navController) }
        composable(
            route = "settings/{selectedType}",
            arguments = listOf(
                navArgument("selectedType"){type = NavType.StringType}
            )
        ) { backStackEntry ->
            val selectedType = backStackEntry.arguments?.getString("selectedType")
            SettingsScreen(viewModel, navController, selectedType ?: "Classic")
        }
        composable(
            route = "WordTrainingScreen/{selectedNumber}/{selectedType}/{wordRefreshList}/{WordSourse}",
            arguments = listOf(
                navArgument("selectedNumber") { type = NavType.IntType },
                navArgument("selectedType") { type = NavType.StringType },
                navArgument("wordRefreshList") {type = NavType.StringType},
                navArgument("WordSourse") {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val selectedNumber = backStackEntry.arguments?.getInt("selectedNumber")
            val selectedType = backStackEntry.arguments?.getString("selectedType")
            val wordRefresh = backStackEntry.arguments?.getString("wordRefreshList")
            val WordSourse = backStackEntry.arguments?.getString("WordSourse")
            val wordRefreshList = wordRefresh?.let { convertJsonToWords(it) } ?: emptyList()
            WordTrainingScreen(viewModel, navController, selectedNumber ?: 10, selectedType ?: "All", wordRefreshList, WordSourse ?: "normal")
        }
        composable(
            route = "WordTrainingScreenDescription/{selectedNumber}/{selectedType}",
            arguments = listOf(
                navArgument("selectedNumber") { type = NavType.IntType },
                navArgument("selectedType") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val selectedNumber = backStackEntry.arguments?.getInt("selectedNumber")
            val selectedType = backStackEntry.arguments?.getString("selectedType")
            WordTrainingScreenDescription(viewModel, navController, selectedNumber ?: 10, selectedType ?: "All")
        }
        composable(
            route = "WordTrainingScreenZen"
        ) {
            WordTrainingScreenZen(viewModel, navController)
        }
        composable(route = "ResultsScreen/{timeSpent}/{selectedType}/{wordsLearned}/{selectedMode}",
            arguments = listOf(
                navArgument("timeSpent") { type = NavType.IntType },
                navArgument("selectedType") { type = NavType.StringType },
                navArgument("wordsLearned") { type = NavType.StringType},
                navArgument("selectedMode") {type = NavType.StringType}
            )) { backStackEntry ->
            val timeSpent = backStackEntry.arguments?.getInt("timeSpent")?: 0
            val selectedType = backStackEntry.arguments?.getString("selectedType") ?: "All"
            val wordsLearnedJson = backStackEntry.arguments?.getString("wordsLearned")
            val wordsLearned = wordsLearnedJson?.let { convertJsonToWords(it) } ?: emptyList()
            val selectedMode = backStackEntry.arguments?.getString("selectedMode") ?: "Classic"
            ResultsScreen(viewModel, navController, timeSpent, selectedType, wordsLearned, selectedMode)
        }
        composable(route = "ResultsScreenZen/{timeSpent}/{earnedScore}/{wordsLearned}",
            arguments = listOf(
                navArgument("timeSpent") { type = NavType.IntType },
                navArgument("earnedScore") { type = NavType.IntType },
                navArgument("wordsLearned") { type = NavType.StringType}
            )) { backStackEntry ->
            val timeSpent = backStackEntry.arguments?.getInt("timeSpent")?: 0
            val earnedScore = backStackEntry.arguments?.getInt("earnedScore")?: 1
            val wordsLearnedJson = backStackEntry.arguments?.getString("wordsLearned")
            val wordsLearned = wordsLearnedJson?.let { convertJsonToWords(it) } ?: emptyList()
            ResultsScreenZen(navController, timeSpent, earnedScore, wordsLearned, viewModel)
        }
    }
}