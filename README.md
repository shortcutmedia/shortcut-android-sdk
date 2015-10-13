# Shortcut Deep Linking SDK for Android

This SDK provides the following features:

- Support for [deferred deep linking](https://en.wikipedia.org/wiki/Deferred_deep_linking).
- Collection of additional statistics to build a user acquisition funnel and evaluate user activity.

There is also an [iOS version](https://github.com/shortcutmedia/shortcut-deeplink-sdk-ios) of this SDK.

## Requirements

The SDK works with Android API 10+.

## Installation 

1. Download the latest .AAR file from the [releases page](https://github.com/shortcutmedia/shortcut-deeplink-sdk-android/releases) and copy it to the project's libs directory (_typically `app/libs`_). 
2. Add a directory repository in `build.gradle` and add `compile 'sc.shortcut.sdk.android.deeplinking:sc-deeplinking:0.1.0@aar'` to the dependencies section of your application's `build.gradle` file.

```gradle
repositories {
  flatDir {
    dirs 'libs'
  }
}
```

```gradle
dependencies {
    compile 'sc.shortcut.sdk.android.deeplinking:sc-deeplinking:0.1.0@aar'
}
```

If for some reason this installion method does not work for you, check out [alternative installation methods](#alternative-installation-methods).


## Prerequisites

To make use of this SDK you need an Android app that supports deep linking. The section below [Add deep linking support to your app](#add-deep-linking-support-to-your-app) explains how to configure your app to support deep links. Please follow the instructions first.

## Integration into your app

### Enabling the SDK

There are 4 methods to enable the SDK inside your app. The preferred method is [Method 1: Register our Application class](#method-1-register-our-application-class). If your application needs to support pre-14 API, please use [Method 4: Manual session management](#method-4-manual-session-management-for-pre-14-support).

#### Method 1: Register our Application class

Simply register our application class in the `Manifest.xml` configuration file:

```xml
    <application
        android:name="sc.shortcut.sdk.android.deeplinking.SCDeepLinkingApp"
```

That's it! Your app supports now deferred deep linking and statistics are automatically gathered. 

#### Method 2: Extend from our Application class

If you already have an Application class then extend it with `SCDeepLinkingApp`.

```java
  public class YourApplication extends SCDeepLinkingApp
```

#### Method 3: Initialize the SDK yourself 

_We do not support this yet!_

~~If you do not want to/can extend from `SCDeepLinkingApp` for some 
reason you can initialize the SDK yourself in your
`Application#onCreate()` method or in the Activity receiving the 
deep link intent.~~ 

```java
  @Override
  public void onCreate() {
    super.onCreate();
    new SCDeepLinkingApp(this);
}
```

#### Method 4: Manual session management for pre-14 support

Unless you need to support API pre-14 we recommend using automatic session managment (methods 1 - 3). 

In your entry Activity add the following: 

```java
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      SCDeepLinking deepLinking = SCDeepLinking.getInstance(this);
      if (savedInstanceState == null) { // You wanna probably ignore device rotation
        deepLinking.startSession(getIntent());
      }
  }

```

### Retrieve the deep link

Usually your app should respond to a deep link with a corresponding view. You can retrieve the deep link either from the incoming intent or from the `SCDeepLinking` class. 

Note that you can retrieve the deep link at any time during the activity's lifecyle, but generally you want to do so in `onCreate()` or `onStart()`. 

The following example shows how to retrieve the deep link through the incoming intent. If the app was launched for the first time a possible deferred link is available through the intent:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...

    if (Intent.ACTION_VIEW.equals(getIntent.getAction()) {
        Uri deepLink = getIntent().getData();
        if (deepLink != null) {
            Log.d(TAG, "opened with deep link: " + deepLink);
            // TODO show content for deep link
        }
    }
}
```

And in the example below the deep link is retrieved from `SCDeepLinking`. 

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    
    Uri deepLink = SCDeepLinking.getInstance().getDeepLink()
    if (deepLink != null) {
        Log.d(TAG, "opened with deep link: " + deepLink);
        // TODO show content for deep link
    }
}

```

### What's next?

Use the [Shortcut Manager](http://manager.shortcutmedia.com) to create a short-url and set up a deep link to your app specified.

## Add deep linking support to your app

Android already has support for deep links baked in. The Shortcut Deep Linking SDK extends the basic built-in functionality with deferred deep links, statistics of app interactions through deep links and short-url generation (suited for sharing). 

In order to support deep links in your app add an intent filter to the `Activity` which you want to get opened when a short link is clicked. This is the entry point of your app. For details check out the [Android documentation](https://developers.google.com/app-indexing/android/app). The example below demonstrates how you would configure deep link support for the launcher activity in your app's `Manifest.xml`:

```XML
 <activity
      android:name=".MainActivity"
      android:label="@string/app_name" >
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>

      <!-- Add this intent filter below, and change the 'scheme' attribute to a unique -->
      <!-- custom scheme that identifies your app. The host and path attribute are     -->
      <!-- optional.                                                                   -->
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data
              android:host="shortcut.sc"
              android:scheme="scdemo"
              android:path="/demo"/>
      </intent-filter>
  </activity>
```

In order to test the deep link support the following command should launch your app:
```Shell
adb shell am start -a android.intent.action.VIEW -d "scdemo://shortcut.sc/demo"
```

## Alternative installation methods

### Add .AAR file as a module

1. Download the latest .AAR file from the [releases page](https://github.com/shortcutmedia/shortcut-deeplink-sdk-android/releases).
2. In Android Studio import the .AAR file (File -> New Module -> Import
   .JAR/.AAR).
3. Then in your app module `build.gradle` add the following dependency:
```Gradle
dependencies {
    compile project(':DeepLinkingSDK')
}
```

### Use jcenter or mavenCentral

_Support for jcenter and mavenCenteral is coming soon._

## License
This project is released under the MIT license. See included [LICENSE.txt](LICENSE.txt) file for details.
