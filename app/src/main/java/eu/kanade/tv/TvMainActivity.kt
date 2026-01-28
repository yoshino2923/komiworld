package eu.kanade.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import eu.kanade.tv.presentation.TvApp
import eu.kanade.tv.presentation.theme.KomiworldTVTheme


class TvMainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)


        setContent {
            KomiworldTVTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        TvApp (
                            onBackPressed = onBackPressedDispatcher::onBackPressed,
                        )
                    }
                }
            }
        }
    }
}


