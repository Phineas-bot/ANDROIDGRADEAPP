LOCAL_DEVELOPMENT_ENVIRONMENT.md

# Local Development Environment Setup

## Prerequisites Installation

### 1. Java Development Kit (JDK)
**Required**: JDK 11 or higher

#### Windows
```bash
# Download from: https://www.oracle.com/java/technologies/downloads/
# Or use chocolatey:
choco install jdk11

# Verify installation:
java -version
javac -version
```

#### macOS
```bash
brew install openjdk@11
# Or download from Oracle website
```

#### Linux
```bash
sudo apt-get install openjdk-11-jdk
# Or equivalent for your distro
```

### 2. Android Studio
**Required**: 2023.1 or later

1. Download from: https://developer.android.com/studio
2. Install following platform-specific instructions
3. Launch Android Studio
4. Complete initial setup wizard

### 3. Android SDK
**Required**: API 34 (target SDK)

In Android Studio:
1. Tools → SDK Manager
2. SDK Platforms tab
3. Check "Android 14.0 (API 34)" ✓
4. Click "Apply" and install

### 4. Android NDK (Optional)
Only needed if using native code. Grade Calculator doesn't need it.

### 5. Gradle
**Included**: Gradle 8.1.4 is bundled in the project

No separate installation needed.

---

## Project Setup

### Step 1: Clone/Import Project
```bash
# If cloning from git:
git clone <repository-url>
cd ANDROIDGRADEAPP

# If using zip:
# Extract ANDROIDGRADEAPP.zip
# Open folder in Android Studio
```

### Step 2: Open in Android Studio
1. File → Open
2. Select ANDROIDGRADEAPP folder
3. Wait for Gradle sync (first time: 2-3 minutes)

### Step 3: Configure SDK Path
If prompted for SDK location:
1. File → Project Structure
2. SDK Location section
3. Verify Android SDK path
4. Should point to: `C:\Users\<YourUser>\AppData\Local\Android\sdk` (Windows)

### Step 4: Create Virtual Device
For emulator testing:
1. Tools → Device Manager
2. Click "Create Virtual Device"
3. Select "Pixel 4" or similar
4. Select API 34 image
5. Finish creation

---

## IDE Configuration

### Android Studio Settings

#### Memory Settings
For better performance, increase IDE memory:
1. Help → Edit Custom VM Options
2. Uncomment and increase:
```properties
-Xmx4096m      # Max heap size (4GB)
-Xms1024m      # Initial heap size (1GB)
-XX:+UseG1GC   # Use G1 garbage collector
```

#### Gradle Settings
1. File → Settings → Build, Execution, Deployment → Gradle
2. Enable "Offline mode" if offline development needed
3. Set Gradle JVM to Java 11+

#### Code Style
1. File → Settings → Editor → Code Style
2. Scheme: IntelliJ IDEA Code Style
3. Hard wrap at: 120

---

## Building the Project

### Initial Build
```bash
# Clean and build
./gradlew clean build

# Or on Windows:
gradlew.bat clean build
```

### Debug Build
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

### Incremental Build
```bash
# Faster for small changes
./gradlew build
```

---

## Running the App

### On Android Emulator
1. Tools → Device Manager
2. Select virtual device
3. Click play button to start emulator
4. Click Run button in IDE (Shift+F10)
5. Select emulator as target

### On Physical Device
1. Enable Developer Mode (Settings → About → Tap Build 7x)
2. Enable USB Debugging (Developer options)
3. Connect via USB
4. Accept key fingerprint on device
5. Click Run button in IDE

### Via Command Line
```bash
# Build and install
./gradlew installDebug

# Launch app
adb shell am start -n com.example.gradecalculator/.ui.MainActivity
```

---

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test
```bash
./gradlew test --tests GradeCalculatorTest
```

### With Coverage Report
```bash
./gradlew testDebugUnitTestCoverage
```

---

## Troubleshooting Build Issues

### Issue: "Gradle sync failed"
**Solution**:
```bash
./gradlew clean
./gradlew --refresh-dependencies
./gradlew build
```

### Issue: "Cannot find Android SDK"
**Solution**:
1. File → Project Structure
2. Set Android SDK Location
3. Download SDK if needed via SDK Manager

### Issue: "Build cache is corrupted"
**Solution**:
```bash
./gradlew cleanBuildCache
./gradlew build
```

### Issue: "OutOfMemoryError during build"
**Solution**:
1. Increase Gradle memory in gradle.properties:
```properties
org.gradle.jvmargs=-Xmx4096m
```

### Issue: "Emulator too slow"
**Solution**:
- Use API 30+ image (better performance)
- Enable GPU acceleration in emulator settings
- Close other applications
- Use physical device for testing

---

## IDE Keyboard Shortcuts

### Useful Shortcuts
| Shortcut | Action |
|----------|--------|
| Ctrl+Alt+L | Format code |
| Ctrl+/ | Comment/uncomment |
| Ctrl+Shift+O | Optimize imports |
| Alt+Enter | Quick fix |
| Ctrl+B | Go to definition |
| Ctrl+Shift+A | Search action |
| Shift+F10 | Run app |
| Ctrl+F9 | Build project |
| Alt+F12 | Terminal |

---

## Version Control

### Git Setup
```bash
# Initialize repository
git init

# Add all files
git add .

# First commit
git commit -m "Initial commit: Grade Calculator App"

# Add remote (if using GitHub)
git remote add origin <repository-url>

# Push to remote
git push -u origin main
```

### .gitignore Already Configured
Included in project - covers:
- Build outputs
- IDE files
- Local configuration
- Gradle cache

---

## Dependency Management

### View Dependencies
```bash
./gradlew dependencies
```

### Check for Updates
```bash
./gradlew dependencyUpdates
```

### Update Dependencies
```gradle
// In app/build.gradle.kts, update version numbers:
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")  // New version
```

---

## Continuous Integration (Optional)

### GitHub Actions Example
Create `.github/workflows/build.yml`:
```yaml
name: Build and Test

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: ./gradlew build
      - run: ./gradlew test
```

---

## Performance Profiling

### CPU Profiler
1. Run app
2. View → Tool Windows → Profiler
3. Click CPU tab
4. Interact with app
5. Analyze call stack

### Memory Profiler
1. View → Tool Windows → Profiler
2. Click Memory tab
3. Monitor heap usage
4. Check for memory leaks

### Database Inspector
1. Run app
2. View → Tool Windows → Database Inspector
3. Open databases
4. Query and inspect data

---

## Debugging

### Debug App
1. Click Run → Debug App (or Shift+F9)
2. Set breakpoints (click line number)
3. Step through code (F10 next, F11 step in)
4. View variables in Variables panel
5. Use Evaluate Expression (Alt+F9)

### Logcat
1. View → Tool Windows → Logcat
2. Filter by process: "gradecalculator"
3. Set log level: Info/Verbose
4. Search for errors/warnings

### Debugger Features
- Breakpoints (conditional, temporary)
- Watches (monitor variables)
- Evaluation (run code snippets)
- Stack trace (understand call flow)

---

## File Locations

### Important Directories

**Windows**:
```
C:\Users\<YourUser>\AndroidStudioProjects\ANDROIDGRADEAPP\
├── app\                          # App module
│   ├── src\
│   │   ├── main\java\            # Source code
│   │   └── test\java\            # Unit tests
│   └── build\                    # Output directory
├── .gradle\                       # Gradle cache
└── build.gradle.kts              # Build script
```

**macOS/Linux**:
```
~/AndroidStudioProjects/ANDROIDGRADEAPP/
├── app/                           # App module
│   ├── src/
│   │   ├── main/java/             # Source code
│   │   └── test/java/             # Unit tests
│   └── build/                     # Output directory
├── .gradle/                       # Gradle cache
└── build.gradle.kts               # Build script
```

### Generated Files (can be deleted safely)
- `build/` - Build outputs (rebuilt on build)
- `.gradle/` - Gradle cache (rebuilt on sync)
- `.idea/` - IDE configuration (safe to delete, recreated)

---

## Environment Variables (Optional)

### ANDROID_HOME
```bash
# Windows (in System Environment Variables):
ANDROID_HOME=C:\Users\<User>\AppData\Local\Android\sdk

# macOS/Linux (in ~/.zshrc or ~/.bash_profile):
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### JAVA_HOME
```bash
# Windows (find JDK location):
JAVA_HOME=C:\Program Files\Java\jdk-11.0.x

# macOS:
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

---

## Useful Commands Reference

```bash
# Build commands
./gradlew build              # Full build
./gradlew clean              # Clean build outputs
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK

# Testing
./gradlew test               # Run unit tests
./gradlew test --tests Class # Run specific test

# Running
./gradlew installDebug       # Install debug APK
./gradlew installRelease     # Install release APK

# Other
./gradlew tasks              # List available tasks
./gradlew --help             # Show help
./gradlew --version          # Show version
```

---

## IDE Plugins (Optional but Useful)

Recommended plugins in Android Studio:
1. **Kotlin Linter** - Code style checking
2. **Github Copilot** - AI code assistance
3. **Git Toolbox** - Enhanced Git integration
4. **JSON Parser** - JSON file handling
5. **Rainbow Brackets** - Bracket highlighting

Install via: Settings → Plugins → Marketplace

---

## Documentation Access

Quick access to documentation:
- QUICK_START.md - 5-minute setup
- ARCHITECTURE_GUIDE.md - Technical details
- README.md - Full documentation
- FAQ.md - Common questions

---

## Performance Tips

### Faster Builds
1. Increase Gradle memory: `org.gradle.jvmargs=-Xmx4096m`
2. Use offline Gradle if stable
3. Disable unused modules
4. Use SSD for development

### Faster Emulation
1. Use API 30+ images
2. Enable GPU acceleration
3. Allocate sufficient RAM (4GB+ to emulator)
4. Use physical device when possible

### Faster IDE
1. Disable unused inspections
2. Increase IDE memory (Xmx4096m)
3. Disable real-time analysis
4. Use SSD

---

## Getting Help

1. **Gradle Errors**: Run `./gradlew build --stacktrace`
2. **Compilation Errors**: Check logcat, press Alt+F1
3. **Runtime Crashes**: Check Logcat with proper filters
4. **Performance**: Use Profiler tool
5. **Code Issues**: Use Code Analyzer (Ctrl+Alt+Shift+I)

---

## Next Steps

1. ✅ Install prerequisites
2. ✅ Import project in Android Studio
3. ✅ Wait for Gradle sync
4. ✅ Run on emulator or device
5. ✅ Read QUICK_START.md
6. ✅ Study ARCHITECTURE_GUIDE.md
7. ✅ Modify and experiment!

---

**Ready to develop?** You're all set! 🚀

For issues, check FAQ.md or the detailed documentation guides.

Happy coding! 🎓

