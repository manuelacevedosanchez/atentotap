# AtentoTap

AtentoTap is a local 2-player reaction game built with Kotlin and Jetpack Compose.

## Gameplay

- The screen is split into two horizontal zones.
- Top zone belongs to Player 1 and bottom zone belongs to Player 2.
- Each zone shows 6 symbols.
- Exactly one symbol is shared between both zones.
- First player to tap the shared symbol in their own zone wins the round and gets 1 point.

## Modes

- **Score mode**: first player to reach the target score wins.
- **Time mode**: highest score when timer reaches zero wins.

## Tech

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow

## Localization

- Default language resources are in `app/src/main/res/values/strings.xml` (English).
- Spanish resources are in `app/src/main/res/values-es/strings.xml`.
- A quick EN/ES language switcher is available in **debug builds only** from the main menu.
- The selected debug language is persisted locally and restored on app start in debug builds.

## Project Structure

- `app/src/main/java/com/example/atentotap/domain` - Game models and use cases
- `app/src/main/java/com/example/atentotap/data` - Symbol pool and round generator
- `app/src/main/java/com/example/atentotap/presentation` - Navigation, screens, UI components, ViewModel

## Run

```zsh
cd "/home/manuelasan/AndroidStudioProjects/Atentotap"
./gradlew :app:assembleDebug
```

## Test

```zsh
cd "/home/manuelasan/AndroidStudioProjects/Atentotap"
./gradlew test
```

