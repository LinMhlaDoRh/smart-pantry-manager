# Assignment requirement mapping

## Pantry management

PantryActivity reads SQLite records and displays them through IngredientAdapter. IngredientActivity creates and updates records. The Delete action asks for confirmation before removing a record. Data is stored in pantry.db and survives app restarts.

## Twenty stored recipes

PantryDatabase creates the recipes and recipe_ingredients tables and inserts 20 recipes on first database creation. Each recipe has a name, a full ingredient list with quantities and units, and preparation steps. The pantry is never automatically populated with demonstration data.

## Strict suggestions

RecipesActivity loads the current pantry and stored recipe collection in onResume. RecipeMatcher accepts a recipe only when every required ingredient exists in sufficient quantity. It handles common plurals, case, spaces, g/kg and ml/l. Partial matches are excluded. An empty result shows a descriptive message.

## Screens and navigation

The app has five Activities: pantry list, add/edit ingredient, suggested recipes, recipe details and settings. Pantry, Recipes and Settings have a shared bottom navigation bar. Explicit Intents navigate between Activities. Ingredient and recipe IDs are passed as long extras and queried from SQLite at the destination.

## Java and XML

All authored application source is Java. Screen layouts use XML LinearLayouts, ScrollViews and RecyclerViews. Resources hold labels, colours and styles. No Kotlin source, mapping SDK, location permission, login, payment integration, external recipe API or cloud backend is included.

## Custom adapters

IngredientAdapter and RecipeAdapter extend RecyclerView.Adapter. Their ViewHolders cache row widgets, onCreateViewHolder inflates the row XML, onBindViewHolder displays current data, and getItemCount reports list size.

## Validation and feedback

Names must contain non-space text and are limited to 60 characters. Quantities must be finite numbers greater than zero and no larger than 1,000,000,000. The unit selector restricts entry to supported units. Form errors appear at the relevant field. Save and delete operations check database outcomes before displaying success.

## Settings

SettingsActivity saves A to Z or Z to A pantry sorting through SharedPreferences. PantryActivity reads the saved preference in onResume, including when returning through the system Back button.

## Version control

The private GitHub repository is https://github.com/LinMhlaDoRh/smart-pantry-manager. The codex/smart-pantry branch contains the actual incremental implementation commits. No commit timestamps were changed. Marker access must be granted before submission.

## Report and video

The implementation report contains the cover page, contents, introduction, screen flow, data model, actual screenshots, five short code extracts, implementation challenges, conclusion and references. It identifies assistance used. The student must review the report and complete the declaration.

DEMONSTRATION.md provides a recording outline. The student must record one video with their own clear narration, showing Git history, working CRUD, strict matching, persistence and three concepts from the actual code. Include the video file directly in the final ZIP.

## Submission

Use the filename 402412302_Mhlangu_MobileAppDev700_Assignment.zip for the final submission. Include the complete Android Studio source project, the report and the narrated video. Exclude build folders and the local Gradle cache. Keep the ZIP at or below 50 MB.
