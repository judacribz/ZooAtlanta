# Zoo Atlanta (Unofficial)

A modern Android application that surfaces the latest animal information from Zoo Atlanta. The app is built with Jetpack Compose, follows a Clean Architecture layout, and uses an MVI presentation layer powered by a unidirectional data flow.

## Tech Stack
- **UI**: Jetpack Compose with Material 3 design system
- **Architecture**: Clean Architecture + MVI + coroutines
- **DI**: Dagger 2 with Square Anvil code generation
- **Data**: Jsoup-based HTML scraping with Room caching for offline resilience
- **Language**: Kotlin (1.9)

## Features
- Real-time scraping of the Zoo Atlanta animals catalogue with resilient HTML parsing
- Category-based browsing with persistent caching provided by Room
- Pull-to-refresh and offline-first experience thanks to reactive Flows

## Building
The project targets Android Studio Iguana (or newer) and requires JDK 17.

```bash
./gradlew assembleDebug
```

> **Note**: The build downloads the Gradle 8.7 wrapper and modern Android dependencies. Ensure network access is available when running the first build.
