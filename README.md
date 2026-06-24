# YouTube Video Search App (Assignment 2)

An advanced Android application built using **Java** and **Groovy DSL** that connects to the official **YouTube Data API v3** to search for and display real-time video results based on user queries.

---

## 🚀 Features Implemented
- **Dynamic Search Input:** Users can enter any search term to fetch video data directly from YouTube.
- **Asynchronous Networking:** Utilizes `AsyncTask` and `HttpURLConnection` to handle network operations off the main thread safely.
- **Live Data Fetching:** Fetches real-time structured data including:
  - Video Title
  - Description
  - Channel Title
  - Publish Time
  - Thumbnail Image URL
- **Efficient Image Loading:** Uses the **Glide** library for efficient image caching and rendering of video thumbnails in a custom `CardView` with `centerCrop` scaling.
- **UI & UX Feedback:** Integrated a dynamic `ProgressBar` for loading states and comprehensive error handling for empty inputs, network failures, or empty search results.

---

## 🛠️ Setup & Installation Steps

To run this project locally on your Android Studio, follow these steps:

### 1. Clone or Extract the Project
Extract the project ZIP file and open **Android Studio**. Go to `File -> Open` and select the `MyApplication1111` project folder.

### 2. Verify Internet Permissions
Ensure that the internet permission is included in your `AndroidManifest.xml` (already configured):
```xml
<uses-permission android:name="android.permission.INTERNET" />
