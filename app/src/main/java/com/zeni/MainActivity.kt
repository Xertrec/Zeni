package com.zeni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.zeni.core.presentation.navigation.NavGraph
import com.zeni.core.presentation.navigation.ScreenInitial
import com.zeni.core.presentation.theme.ZeniTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ZeniTheme {
                NavGraph(
                    navController = rememberNavController(),
                    screenInitial = ScreenInitial::class // TODO: Conserve login state for future sessions
                )
            }
        }
    }
}