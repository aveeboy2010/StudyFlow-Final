# StudyFlow — Android starter project

Created by Aveesha Shahindu.

## What is included in this starter
- Native Android Java/XML app skeleton (Android 8.0 / API 26 and newer)
- Forest green + cream visual theme, simple dark/light toggle, English/Sinhala menu toggle
- Five navigation destinations: Home, Tasks, Focus, My Plant, Progress
- Basic stopwatch display with screen-awake behavior while running
- GitHub Actions workflow that builds a debug APK in the cloud and uploads it as an artifact

## Important
This is an initial starter build, not the finished StudyFlow app. Task database/history, countdown and Pomodoro, audio playlist, plant growth persistence, calendar, charts, backups, PDF export, PIN lock and reminders still need implementation. The starter stopwatch is currently a UI prototype and does not yet persist recorded sessions as study history.

## Build with GitHub Actions
1. Create a new repository on GitHub named `StudyFlow`.
2. Upload the contents of this folder to the repository root (not the containing folder itself).
3. Open the repository's **Actions** tab and enable workflows if prompted.
4. Open **Build StudyFlow APK**, choose **Run workflow**, then wait for the green success check.
5. Open the completed run and download the `StudyFlow-debug-APK` artifact.
6. Extract the downloaded ZIP to get `app-debug.apk`.

Do not commit passwords, signing keys, or keystore files. A signed release APK can be set up later using GitHub repository secrets.
