
## SDK Integration

### Install the SDK files

#### ~~From jcenter or mavenCentral (comming soon)~~
~~If you use gradle just add `compile
'sc.shortcut.sdk.android:deeplinking:0.0.1-beta'` to the dependencies
section of your `build.gradle` file.~~

#### Download aar file

You can also download the [latest release](https://github.com/shortcutmedia/shortcut-deeplink-sdk-android/releases) 
as aar file and add it manually to your project.

1. Download latest .AAR file
2. In Android Studio import the .AAR file (File -> New Module -> Import
   .JAR/.AAR).
3. Then in your app module build.gradle at the dependency 
```Android
  dependencies {
      compile project(':DeepLinkingSDK-0.1.0-beta')
  }
```

### Manifest configuration

#### Configure for deep linking

Add an intent filter to the `Activity` you want to open up when a link
is clicked. This is the entry point to your app. 

```Android
 <activity
      android:name=".MainActivity"
      android:label="@string/app_name" >
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category
android:name="android.intent.category.LAUNCHER" />
      </intent-filter>

      <!-- Add this intent filter below, and change the data tag
accordingly  -->
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data
              android:host="your.host"
              android:scheme="scdemo"
              android:path="<optional path>"/>
      </intent-filter>
  </activity>
```

### Subclass the SCDeepLinkingApp

There are 4 methods how you can use the SDK inside your app. If your
app supports Android API 14 or higher check out methods #1-3. If
you need to support pre-14 API go with method #4.

#### Method 1: Register our Application class

Simply register our Application class in the `Manifest.xml`
configuration file:

```xml
    <application
        android:name="sc.shortcut.sdk.android.deeplinking.SCDeepLinkingApp"
```

#### Method 2: Extend from our Application class

If you already have an Application class then extend it with
SCDeepLinkingApp.

```java
  public class YourApplication extends SCDeepLinkingApp
```

#### ~~~Method 3: Initialize the SDK yourself~~~ _coming soon_

If you do not want to/can extend from `SCDeepLinkingApp` for some 
reason you can initialize the SDK yourself in your
`Application#onCreate()` method or in the Activity receiving the 
deep link intent.

```android
  @Override
  public void onCreate() {
    super.onCreate();
    SCDeepLinkingApp(this);
}
```

#### Method 4: Manual session management for pre-14 support

We do not recommend using this method unless you have to support pre-14.

##### Init Session

```Android
  @Override
  protected void onStart() {
      super.onStart();
      SCDeepLinkingApp(this).startSession();
  }
```

##### Close Session
```Android
  @Override
  protected void onStop() {
      super.onStop();
      branch.closeSession();
  }
```

