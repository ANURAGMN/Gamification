package com.anurag.eduai.uikit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.anurag.eduai.uikit.theme.EduAiTheme
import com.anurag.eduai.uikit.theme.EduThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleApp()
        }
    }
}
