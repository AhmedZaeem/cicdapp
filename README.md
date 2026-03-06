# ShopApp - Android CI/CD Pipeline

## What this is

Simple Android shopping app built with Jetpack Compose. Has a product list, product details, and a cart. Set up a full CI/CD pipeline using GitHub Actions for automated testing and builds.

## How to run it

Clone the repo and open in Android Studio. Hit run. Thats it.

Or from terminal:
```
./gradlew assembleDebug
```

## CI Pipeline Structure

The pipeline has 4 jobs that run on every push to main/develop and on PRs:

1. **Code Quality** - runs Android Lint and Detekt static analysis
2. **Unit Tests** - runs all unit tests, generates JaCoCo coverage report, uploads to Codecov
3. **Instrumented Tests** - runs UI tests on emulators (API 28, 30, 33) using macos runner
4. **Build Debug** - assembles the debug APK and uploads it as an artifact

Jobs run in order: code-quality first, then unit-tests and instumented-tests in parallel, then build-debug after unit tests pass.

## Issues and Fixes

### Detekt Setup
Had to configure detekt with a custom yml file becuase the default rules were too strict for Compose code. Disabled `MagicNumber`, `WildcardImport`, `TooManyFunctions`, and `NewLineAtEndOfFile` since Compose files naturally have a lot of functions (one per composable). Changed `FunctionNaming` pattern to `[a-zA-Z][a-zA-Z0-9]*` because Compose composables start with uppercase which violates the default kotlin naming convention. Also bumped `LongMethod` threshold to 150 since composable functions tend to be longer with all the UI nesting. Set max line length to 140 instead of the default 120. First run had 13 issues, after tweaking the config it passed clean.

### JaCoCo Configuration
JaCoCo needed explicit class directory paths pointing to `tmp/kotlin-classes/debug` instead of the standard java classes directory. Also had to exclude generated Compose files (`ComposableSingletons`, `_Impl` classes) from coverage reports otherwise you get inflated numbers.

### String.format Locale Warning
Lint flagged `String.format("%.2f", price)` calls because using default locale can cause issues with decimal separators in different regions. Fixed by passing `Locale.US` explicitly.

### compileSdk Configuration
Project uses AGP 9.1.0 with the new `compileSdk` block syntax (`release(36)`) instead of the old integer format. This is the newer way of specifing SDK versions.

### Unit Tests - returnDefaultValues
Had to set `unitTests.isReturnDefaultValues = true` in testOptions because some Android framework classes return default values in unit test envrionment.

### Branch Rename
Renamed `master` to `main` to match the workflow trigger branches. Had to update the remote tracking after renaming.

### Instrumented Tests Runner
Instrumented tests use `macos-latest` runner because Android emulator needs hardware acceleration (KVM). This costs more GitHub Actions minutes but its the only reliable way to run emulator tests in CI.

## Test Coverage

Unit tests cover:
- CartViewModel (add/remove/update items, totals, item count, clear cart)
- ProductRepository (product list, unique IDs, valid prices, lookup by ID)

Instrumented tests cover:
- Product list displays items correctly
- Cart button is visible
- Add to cart updates the count
- Empty cart shows message
- Cart displays added items

## Tools Used

- **Detekt** v1.23.7 - Kotlin static analysis
- **JaCoCo** v0.8.12 - Code coverage
- **Android Lint** - Built-in Android static analysis
- **Codecov** - Coverage reporting (via GitHub Action)


