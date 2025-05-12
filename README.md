# 📋 Share to Clipboard

An Android “Share” target that captures text and/or files from other apps and copies them into the system clipboard.

## ✨ Features

- **📝 Single Text**  
  Accepts shared text and copies it as plain text to the clipboard.

- **📁 Single File**  
  Accepts a single file URI (e.g. image, PDF) and copies it as a URI clip.

- **📝 + 📁 Text & File**  
  When both text and a single file are shared, creates a combined clip (text + file).

- **📂 Multiple Files (with optional text)**  
  Accepts multiple files (and optional text) and copies them all into the clipboard in one action.

- **🤖 Android 7.0+**  
  Supports from API level 24 up through the latest Android releases.

- **🚀 No UI**  
  Runs invisibly (no layout) and shows only a Toast to confirm successful copy.

## 🛠️ Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer  
- Android SDK 35 (compile & target SDK 35)  
- Kotlin 1.6+  

### 📥 Installation

Grab the latest APK and install it on your device:

[⬇️ Download the latest APK here](https://github.com/lleans/simple-clipboard-android/raw/refs/heads/main/app/release/app-release.apk)

> **Tip:** Replace the URL above with your hosted APK link whenever you publish a new version.

Alternatively, clone the repo and build in Android Studio:

```bash
git clone https://github.com/yourusername/share-to-clipboard.git
cd share-to-clipboard
./gradlew installDebug
