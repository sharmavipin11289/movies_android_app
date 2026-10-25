package com.vipin.moviesandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vipin.moviesandroidapp.ui.theme.MoviesAndroidAppTheme

class TrailerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val request = TrailerRequest(
            movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1),
            title = intent.getStringExtra(EXTRA_TITLE).orEmpty(),
            youtubeKey = intent.getStringExtra(EXTRA_YOUTUBE_KEY),
        )
        setContent {
            MoviesAndroidAppTheme {
                TrailerScreen(request = request, onBack = ::finish)
            }
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movieId"
        const val EXTRA_TITLE = "title"
        const val EXTRA_YOUTUBE_KEY = "youtubeKey"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailerScreen(request: TrailerRequest, onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(request.title) }) },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(request.youtubeKey ?: "Trailer not available")
        }
    }
}