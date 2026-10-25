package com.vipin.moviesandroidapp

import android.app.Application
import android.content.Intent
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MoviesApplication : Application() {
    private var flutterActivity: android.app.Activity? = null
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(a: android.app.Activity, s: android.os.Bundle?) {
                if (a is io.flutter.embedding.android.FlutterActivity) flutterActivity = a
            }
            override fun onActivityDestroyed(a: android.app.Activity) {
                if (a === flutterActivity) flutterActivity = null
            }
            override fun onActivityStarted(a: android.app.Activity) {}
            override fun onActivityResumed(a: android.app.Activity) {}
            override fun onActivityPaused(a: android.app.Activity) {}
            override fun onActivityStopped(a: android.app.Activity) {}
            override fun onActivitySaveInstanceState(a: android.app.Activity, o: android.os.Bundle) {}
        })

        // Pre-warm the engine so the list opens instantly.
        val engine = FlutterEngine(this)
        engine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(ENGINE_ID, engine)

        MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL_NAME)
            .setMethodCallHandler { call, result ->
                if (call.method != "openTrailer") {
                    result.notImplemented()
                    return@setMethodCallHandler
                }
                val request = TrailerRequest.fromArguments(call.arguments)
                if (request == null) {
                    result.error(
                        "INVALID_ARGUMENTS",
                        "openTrailer needs movieId and title",
                        null,
                    )
                    return@setMethodCallHandler
                }
                result.success(null)
                openTrailer(request)
            }
    }

    private fun openTrailer(request: TrailerRequest) {
        val intent = Intent(this, TrailerActivity::class.java).apply {
            putExtra(TrailerActivity.EXTRA_MOVIE_ID, request.movieId)
            putExtra(TrailerActivity.EXTRA_TITLE, request.title)
            putExtra(TrailerActivity.EXTRA_YOUTUBE_KEY, request.youtubeKey)
            // Needed because we start it from the Application context.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        flutterActivity?.finish()
    }

    companion object {
        const val ENGINE_ID = "movies_engine"

        // Must be identical to NativeBridge.channelName in the Flutter module.
        const val CHANNEL_NAME = "com.vipin.movies/bridge"
    }
}