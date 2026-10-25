# Movies Android App (Jetpack Compose)

Native Android host app that embeds the Flutter movies module.
Tap **Show List of Movies** to open the Flutter list; tap a movie and the
app returns to native and plays the trailer on a native screen.

## Repositories (clone side by side)

```
some_folder/
├── movies_flutter_module/   <- Flutter module repo
└── movies_android_app/      <- this repo
```

`settings.gradle.kts` points to `../movies_flutter_module`, so both repos must live in the same parent folder.

## Setup

Prerequisites: Android Studio, Flutter SDK, an emulator or device.

```bash
git clone https://github.com/sharmavipin11289/movies_flutter_module.git movies_flutter_module
git clone https://github.com/sharmavipin11289/movies_android_app.git movies_android_app

cd movies_flutter_module
# add your TMDB key, see the module README
flutter pub get
```

Open `movies_android_app` in Android Studio, let Gradle sync, and press Run.

Notes:
- `gradle.properties` sets `android.newDsl=false` and `android.builtInKotlin=false`, which the Flutter Gradle plugin currently needs with AGP 9.
- Min SDK 24, Java 17.

## Architecture

- `MoviesApplication`: pre-warms the cached Flutter engine, registers the `MethodChannel` listener, opens `TrailerActivity` and finishes the Flutter activity.
- `MainActivity` (Compose): home screen with the **Show List of Movies** button, launches `FlutterActivity` with the cached engine.
- `TrailerRequest`: parses channel arguments and builds the YouTube embed HTML (unit tested).
- `TrailerActivity` (Compose): native trailer screen, YouTube in a `WebView`, plus an **Open on YouTube** fallback.

## Channel contract

| | |
|---|---|
| Channel | `com.vipin.movies/bridge` |
| Method | `openTrailer` |
| Args | `movieId: Int`, `title: String`, `youtubeKey: String?` |

## Design decision

The Flutter module calls TMDB `/movie/{id}/videos` and sends the YouTube key
to native, so the native apps never need the TMDB API key.

## Known limitations

- Some studios disable embedding for their trailers; use **Open on YouTube**.
- On some emulators WebView video can render blank; a real device or a stable emulator image (API 34/35) works.

## Tests

```bash
./gradlew test
```

## Screenshots

(add: home, Flutter list, trailer screen)