# Video demonstration outline

Record one video lasting 5 to 7 minutes. Speak in your own words and point to the actual app and code. Keep the screen readable. Your voice is required throughout; the video is not complete without narration.

## 0 minutes to 50 seconds

Open the private GitHub repository and show the commit history on codex/smart-pantry. Explain the real implementation order: project setup, matching, SQLite, layouts, pantry CRUD, forms, recipes, details, settings and verification. Do not claim the commits were spread across days.

## 50 seconds to 3 minutes 10 seconds

Show the empty pantry and add banana with quantity 1 and unit pcs. Open Recipes and show that no recipe matches. Add milk with quantity 0.2 and unit l. Show that this is still insufficient. Edit the milk to 0.25 l and show Banana milk appearing. Open the detail screen and show the required quantities and method.

Return to the pantry, change the sort order in Settings, then close and reopen the app to show persistence. Delete the milk and show that Banana milk disappears from suggestions. This demonstrates Create, Read, Update, Delete, validation, strict matching, unit conversion and persistent storage. Show a blank or zero quantity error during the form demonstration.

## 3 minutes 10 seconds to 5 minutes 45 seconds

Explain three concepts while showing the relevant Java files.

1. Database: PantryDatabase creates three tables, inserts 20 recipes on first creation, stores pantry records through ContentValues, reads through Cursor and uses the record ID for update and delete.
2. Intents: PantryActivity opens IngredientActivity and passes ingredient_id when editing. RecipesActivity passes recipe_id to RecipeDetailActivity. The destination reads the ID and loads its database record.
3. Matching: RecipeMatcher normalises common ingredient names, keeps mass, volume and count separate, converts compatible units and compares available quantity with total required quantity. One missing ingredient or insufficient quantity excludes the whole recipe.

If asked about the adapter, show onCreateViewHolder, onBindViewHolder and getItemCount. If asked about lifecycle, explain why onResume refreshes records and sorting when returning to a screen. Review these explanations until you can explain each line without reading a script.

## 5 minutes 45 seconds to 6 minutes 20 seconds

Explain why SQLite fits this app: the data is local, the app works offline, the recipe collection is small, no account or server is needed, and SQLiteOpenHelper follows the module examples.

## Before submitting

Export an MP4 with readable text and audible narration. Add the actual video file to the ZIP beside the report and source project. Check the entire ZIP is no larger than 50 MB. Give the marker access to the private repository and complete the report cover and declaration.
