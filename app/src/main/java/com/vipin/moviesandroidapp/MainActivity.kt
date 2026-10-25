package com.vipin.moviesandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vipin.moviesandroidapp.ui.theme.MoviesAndroidAppTheme
import io.flutter.embedding.android.FlutterActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAndroidAppTheme {
                HomeScreen(onShowMovies = ::showFlutter)
            }
        }
    }

    private fun showFlutter() {
        startActivity(
            FlutterActivity
                .withCachedEngine(MoviesApplication.ENGINE_ID)
                .build(this)
        )
    }
}

@Composable
fun HomeScreen(onShowMovies: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Movies", style = MaterialTheme.typography.headlineLarge)
        Button(
            onClick = onShowMovies,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text("Show List of Movies")
        }
    }
}