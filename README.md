# Smart Pantry Manager

A Java Android application for the Mobile App Development 700 assignment. It tracks pantry ingredients and suggests recipes only when every required ingredient is available in sufficient quantity.

The app uses Java Activities, XML layouts, SQLiteOpenHelper, a custom RecyclerView adapter, Intents and SharedPreferences. SQLite keeps pantry data on the device, works offline and follows the database approach taught in the module.

## Run the project

1. Open this folder in Android Studio.
2. Install Android SDK Platform 35 and SDK Build Tools 36.0.0 through SDK Manager.
3. Use JDK 17 or newer and allow Gradle to sync.
4. Select an emulator or Android device running Android 6.0 or newer.
5. Run the app module.

The Gradle wrapper uses Gradle 9.6.0 and Android Gradle plugin 9.4.0.

## Required features

1. Pantry screen with a database-backed custom RecyclerView adapter and confirmed deletion.
2. Add and edit screen with required names, numeric quantity validation and unit selection.
3. Suggested recipes screen showing only complete matches from 20 stored recipes.
4. Recipe detail screen showing every required ingredient and the preparation method.
5. Settings screen saving alphabetical pantry sorting with SharedPreferences.

The pantry starts empty. Recipe records are inserted into SQLite once when the database is first created. Closing and reopening the app does not reset pantry data or duplicate recipes.

## Matching rules

Ingredient names ignore case and extra spaces. Common plurals include tomatoes, potatoes, eggs, onions, carrots, mushrooms, peppers, beans, cooked beans, bananas, apples and peas.

Compatible quantities are converted between g and kg, and between ml and l. Counted items use pcs. Mass, volume and count are never converted into one another. Separate pantry entries for the same normalised ingredient and compatible unit are added together. Every required ingredient must have enough quantity. There are no partial matches in the suggestions.

For a quick demonstration, add banana with quantity 1 and unit pcs. No recipe matches yet. Add milk with quantity 0.2 and unit l. Banana milk is still excluded. Edit milk to 0.25 l and Banana milk appears. Remove the milk and the recipe disappears. These steps check missing ingredients, insufficient quantities, unit conversion and deletion.

## Project files

The five screen classes are PantryActivity, IngredientActivity, RecipesActivity, RecipeDetailActivity and SettingsActivity. BaseActivity shares the database, preferences, navigation and screen padding. Ingredient and Recipe hold data. PantryDatabase creates tables, seeds recipes and performs CRUD. RecipeMatcher checks quantities. IngredientAdapter and RecipeAdapter display list rows.

The XML screen layouts are in app/src/main/res/layout. Shared labels and appearance are in app/src/main/res/values. Recipe data is written as Java arrays inside PantryDatabase.

## Build and tests

Run `.\gradlew.bat :app:assembleDebug` on Windows or `./gradlew :app:assembleDebug` on macOS and Linux.

Run `.\gradlew.bat :app:testDebugUnitTest :app:lintDebug` for matching tests and Android static checks. With an emulator or device connected, run `.\gradlew.bat :app:connectedDebugAndroidTest` for database persistence and integration tests.

The installable development APK is generated at app/build/outputs/apk/debug/app-debug.apk. It is signed for local testing and does not require Play Store publication.

## Assignment documents

The docs folder contains the implementation report, actual screenshots, requirement mapping, test results and the demonstration outline. The report uses the supplied cover page, Times New Roman size 12 and 1.5 line spacing for the report body.

The repository is private as requested. Add the lecturer or marker as a collaborator before submission. The final submission needs the full project, the report and a single narrated 5 to 7 minute video in one ZIP of at most 50 MB. The student must record the narration and complete the cover declaration and signature before submitting.

## Assignment ownership

Prepared for Linda Mhlangu, student number 402412302. The student must review, understand and acknowledge assistance and consulted sources according to institutional requirements. Git commits record actual implementation steps with their real timestamps.
