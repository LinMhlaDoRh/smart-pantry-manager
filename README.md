# Smart Pantry Manager

A Java Android app that stores pantry ingredients and suggests recipes only when every required ingredient is available in enough quantity.

The app has five screens: Pantry, Add/Edit Ingredient, Suggested Recipes, Recipe Detail and Settings. It includes 20 stored recipes and saves the pantry sort order.

## Database

SQLite stores pantry items, recipes and recipe ingredients on the device. It works offline and follows the SQLiteOpenHelper approach covered in the study guide.

## Run

1. Open the SmartPantryManager folder in Android Studio.
2. Install Android SDK Platform 35 and SDK Build Tools 36.0.0 through SDK Manager if needed.
3. Select JDK 21 for Gradle and let the project sync.
4. Select an emulator or device running Android 6.0 or newer.
5. Select the app configuration and click Run.

The pantry starts empty. Add banana as 1 pcs and milk as 0.25 l to make Banana milk available. Reducing milk to 0.2 l removes that suggestion.

Give the marker access to this private repository before submission.
