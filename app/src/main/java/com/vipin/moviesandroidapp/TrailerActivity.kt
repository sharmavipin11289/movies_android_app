package com.vipin.moviesandroidapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
        topBar = {
            TopAppBar(
                title = { Text(request.title) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
            )
        },
    ) { padding ->
        val html = request.embedHtml()
        if (html != null) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                YouTubePlayer(html = html, modifier = Modifier.fillMaxWidth().weight(1f))
                OpenOnYouTube(youtubeKey = request.youtubeKey!!)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Trailer not available", style = MaterialTheme.typography.titleLarge)
                Text(
                    "TMDB has no trailer for this movie.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayer(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                )
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                // YouTube needs an https origin, otherwise it shows an error.
                loadDataWithBaseURL(
                    "https://www.youtube-nocookie.com",
                    html,
                    "text/html",
                    "utf-8",
                    null,
                )
            }
        },
        onRelease = { it.destroy() },
    )
}

@Composable
fun OpenOnYouTube(youtubeKey: String) {
    val context = LocalContext.current
    TextButton(
        onClick = {
            val url = "https://www.youtube.com/watch?v=$youtubeKey"
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        },
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Text("Open on YouTube")
    }
}