# Welcome

This is a sample project for IU Distance University's Mobile Software Engineering course. The
application is used to develop programming skills in Android apps.

The app provides a UI that connects to the Open311 (GeoReport V2) API of the city of Rostock. The
user of the app gets the ability to send and manage their own requests, as well as browse the
requests of other citizens.

The app does not claim to be complete or fully correct.

# Setup

In order to build the app successfully, you need to create a resource file with the following
content.

```xml

<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
        YourApiKey
    </string>
    <string name="open311_api_key" templateMergeStrategy="preserve" translatable="false">
        YourApiKey
    </string>
</resources>
```