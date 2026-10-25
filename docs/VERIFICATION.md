# Verification results

Verified on 25 October 2026.

## Build

The final debug APK compiled successfully with Gradle 9.6.0, Android Gradle plugin 9.4.0 and JDK 21. Application source is Java with XML layouts. The minimum configured Android API is 23 and the target API is 35. Runtime verification used an Android 15 API 35 x86_64 emulator at 1080 by 1920 resolution. API 23 was not separately run on a device.

## Automated tests

The RecipeMatcher JVM suite passed 14 tests with zero failures and zero errors. The Android database suite passed 5 instrumented tests with zero failures and zero errors.

The matching suite checks missing ingredients, exact and insufficient quantities, common plurals, case and spaces, kg/g conversion, l/ml conversion, incompatible measurement groups, combined pantry rows, repeated requirements, empty inputs, invalid quantities, unrelated ingredients, tiny shortages and cooked bean aliases.

The database suite checks create, read, update and delete across helper reopenings, single seeding of 20 complete recipes, sort order, missing records, invalid quantities and recipe suggestions responding to database changes.

## Android static checks

Lint completed with 0 errors and 7 warnings. The remaining warnings concern newer available SDK or library versions, backup metadata and the course-style full adapter refresh. No lint errors were suppressed.

## Running interface checks

25 checks passed against the actual running app and its database.

1. Fresh pantry is empty.
2. Empty pantry has zero suggestions.
3. Blank name is rejected and the name field receives focus.
4. Zero quantity is rejected and the quantity field receives focus.
5. Create displays saved ingredient.
6. One missing ingredient excludes the recipe.
7. Insufficient converted quantity excludes recipe.
8. Edit loads stored values.
9. Update displays changed quantity.
10. Every ingredient with sufficient converted quantity matches.
11. Detail shows full ingredients and method.
12. Settings saves sort selection.
13. System Back refreshes pantry sort in onResume.
14. Pantry records and sorting persist after force stop.
15. Delete asks for confirmation.
16. Cancel preserves the ingredient.
17. Confirmed delete removes only selected ingredient.
18. Deleted ingredient immediately removes suggestion.
19. Rotation preserves unsaved form values.
20. Returning to portrait preserves form values.
21. Restored ingredient makes recipe available again.
22. Larger font keeps navigation reachable.
23. Database contains exactly twenty seeded recipes.
24. Persisted pantry contains only the two expected records.
25. Recipe foreign keys have no violations.

The screenshots folder contains the captured evidence, including validation errors, CRUD, recipe matching, settings, persistence, landscape form state and larger text.

## Source checks

Application Java, XML and Gradle files contain no authored code comments. Authored text files contain no double dashes. No secrets, local SDK paths, build directories or Gradle caches are committed.
