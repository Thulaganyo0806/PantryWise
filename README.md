# PantryWise

PantryWise is a Java Android application that helps users manage the ingredients they have in their pantry and find recipes that can be prepared using those ingredients. The application is designed to help reduce food waste by suggesting recipes based on the ingredients currently available in the user's pantry.

## Features

- Add pantry ingredients
- View pantry ingredients
- Edit pantry ingredients
- Delete pantry ingredients
- Store pantry data using SQLite
- View suggested recipes
- Strictly match recipes with available pantry ingredients
- View recipe ingredients and preparation methods
- Settings screen
- Input validation
- Data remains available after closing and reopening the application

## Technologies Used

- Java
- Android Studio
- XML
- SQLite
- Android ListView
- Custom Adapters

## Database

PantryWise uses SQLite for local data storage. SQLite was chosen because it is built into Android and allows the application to store pantry ingredients and recipes directly on the device without requiring an internet connection or an external server.

## Recipe Matching

The Suggested Recipes feature uses strict recipe matching. A recipe is only displayed when all of its required ingredients are available in the user's pantry in the required quantity. Recipes that require missing ingredients are not displayed as suggested recipes.

## How to Run the Application

1. Download or clone the PantryWise repository.
2. Open the project in Android Studio.
3. Allow Android Studio to complete the Gradle sync.
4. Connect an Android device or start an Android emulator.
5. Run the application using Android Studio.
6. Add ingredients to the pantry and view the suggested recipes.

## Project Structure

The project contains the Android application source code, layouts, database implementation, recipe functionality, pantry management functionality, and application resources.

## Author

1. Name& Surname: Thulaganyo Motsei
2. Student NO: 402110758
3. Module Name: Mobile App Development 700
