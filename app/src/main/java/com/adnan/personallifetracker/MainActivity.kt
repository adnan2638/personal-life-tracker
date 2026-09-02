package com.adnan.personallifetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adnan.personallifetracker.core.designsystem.theme.PersonalLifeTrackerTheme
import com.adnan.personallifetracker.navigation.PersonalLifeTrackerNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PersonalLifeTrackerTheme { PersonalLifeTrackerNavHost() } }
    }
}
