package com.example.sigmaenglish

sealed class ScreenList(val route: String) {
    data object TaskList: ScreenList("Task_list")
    data object FirstAppScreenList: ScreenList("First_screen")
}