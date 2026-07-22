package com.moazip.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.moazip.app.auth.FirebaseGoogleAuthClient

class MainActivity : ComponentActivity() {
    private val container = AppContainer()
    private val googleAuthClient by lazy {
        FirebaseGoogleAuthClient(
            activity = this,
            userRepository = container.userRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoaZipApp(
                container = container,
                googleAuthClient = googleAuthClient,
            )
        }
    }
}
