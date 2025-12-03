# CountDown Hour and Pomodoro - Simple & Clean UI

A minimalist Android countdown timer app with Pomodoro functionality, optimized for e-ink displays.
<br/>
<br/>

<img width="206" height="412" alt="Countdown Hour_20251130-194014" src="https://github.com/user-attachments/assets/e81ff049-59f6-4862-9f7b-8a2f99894f5f" />
<img width="206" height="412" alt="Countdown Hour_20251203-185152" src="https://github.com/user-attachments/assets/6f816236-5c13-4f93-af66-8d6393542305" />
<img width="206" height="412" alt="Countdown Hour_20251203-185030" src="https://github.com/user-attachments/assets/8588f728-0169-4c32-a0b3-3e7ec9c02f0a" />
<img width="206" height="412" alt="Countdown Hour_20251203-185044" src="https://github.com/user-attachments/assets/7fffa397-aade-40e0-a2dc-e6ae7dd10b57" />
<img width="206" height="412" alt="Countdown Hour_20251203-185118" src="https://github.com/user-attachments/assets/947cdd0a-4b6d-46dc-b361-374812bb3d87" />
<img width="206" height="412" alt="Countdown Hour_20251130-194153" src="https://github.com/user-attachments/assets/ad839f6e-38ca-4892-a561-bb17e3fa181e" />
<br/>

<img width="412" height="206" alt="Countdown Hour_20251203-185218" src="https://github.com/user-attachments/assets/e5d142d0-4c99-4792-ba2b-d9e67a39d94f" />
<img width="412" height="206" alt="Countdown Hour_20251203-185130" src="https://github.com/user-attachments/assets/a515da77-299a-455e-9cd7-20798eaff100" />



## Features

### Countdown Timer
- Set a target time (24h format) and countdown until you reach it
- Custom time picker with swipe gestures (swipe up/down to adjust)
- Tap on hour/minute to fine-tune by ±1
- Visual circular progress indicator
- Real-time summary showing NOW → DIFF → TARGET
- Pause/Resume/Stop controls
- Portrait and landscape layouts with large, readable typography

### Pomodoro Timer
- Classic Pomodoro technique implementation
- Customizable durations:
  - Focus session (default: 25 min)
  - Short break (default: 5 min)
  - Long break (default: 15 min)
  - Pomodoros until long break (default: 4)
- Visual progress tracking with cycle indicators
- Completed pomodoros counter
- Elapsed time display after session completion
- Extend time during session:
  - Tap +5 button to add 5 minutes
  - Long press +5 button to add 1 minute
  - Swipe up/down on timer to add/remove minutes

### Task Management
- Task pool to store up to 15 tasks
- Select up to 5 tasks for each focus session
- Tasks displayed in subtle card during focus
- Tap to mark tasks as completed (strikethrough)
- Completed tasks auto-unselected and moved to "DONE" section

### Gestures & Shortcuts
| Action | Gesture |
|--------|---------|
| Start focus without tasks | Long press Focus button |
| Add 5 minutes to session | Tap +5 button |
| Add 1 minute to session | Long press +5 button |
| Adjust time during session | Swipe up/down on timer |
| Fine-tune duration (±1 min) | Swipe up/down on duration text |
| Toggle task completion in pool | Long press on task |
| Clear all tasks | Double tap trash icon |
| Clear only completed tasks | Long press trash icon |

### Data Persistence
- Tasks, settings, and progress saved automatically
- Data survives app restarts and updates
- Task timestamps (created/completed) tracked

### E-Ink Optimized
- High contrast black & white theme
- Large monospace typography for excellent readability
- Minimal animations to reduce screen flickering
- Clean, distraction-free interface

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with ViewModel and StateFlow
- **Persistence**: DataStore Preferences + Kotlin Serialization
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)

### Background Support
- Timer continues running when app is in background
- Notification shows remaining time
- Screen stays awake while app is active

## Building

```bash
# Clone the repository
git clone https://github.com/oreglio/countdown-pomodoro.git

# Build the project
./gradlew build

# Install on connected device
./gradlew installDebug
```

## Project Structure

```
app/src/main/java/com/countdownhour/
├── MainActivity.kt
├── data/
│   ├── TimerState.kt
│   ├── PomodoroState.kt
│   └── PomodoroDataStore.kt
├── service/
│   └── TimerService.kt
├── viewmodel/
│   ├── TimerViewModel.kt
│   └── PomodoroViewModel.kt
└── ui/
    ├── components/
    │   ├── CircularProgress.kt
    │   ├── ControlButtons.kt
    │   ├── TimePickerDialog.kt
    │   ├── TimeSummaryCard.kt
    │   ├── PomodoroProgress.kt
    │   └── PomodoroSettingsDialog.kt
    ├── screens/
    │   ├── TimerScreen.kt
    │   ├── PomodoroScreen.kt
    │   └── MainScreen.kt
    └── theme/
        └── Theme.kt
```

## AI
The whole code is generated with Claude Code. I just needed to have those features in a simple and clean app.

## License

MIT License - feel free to use and modify as needed.
