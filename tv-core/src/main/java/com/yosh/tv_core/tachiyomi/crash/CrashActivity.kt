package com.yosh.tv_core.tachiyomi.crash

import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.yosh.tv_core.presentation.crash.CrashScreen
import com.yosh.tv_core.tachiyomi.ui.base.activity.BaseActivity
import com.yosh.tv_core.tachiyomi.ui.main.MainActivity
import com.yosh.tv_core.tachiyomi.util.view.setComposeContent

class CrashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val exception = GlobalExceptionHandler.getThrowableFromIntent(intent)
        setComposeContent {
            CrashScreen(
                exception = exception,
                onRestartClick = {
                    finishAffinity()
                    startActivity(Intent(this@CrashActivity, MainActivity::class.java))
                },
            )
        }
    }
}
