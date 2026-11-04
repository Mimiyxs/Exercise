# Exercises of different classes 

### Important implementation and import : 
* For be able to access the link : 
```
<uses-permission android:name="android.permission.INTERNET" />
```



* In plugin : 
```
id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
```

* Retrofit & Serialization :
```
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
```

* ViewModel : 
```
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
```

* Log : 
```
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
```

* Access to different default icons : 
```
    implementation("androidx.compose.material:material-icons-extended")
```

* Navigation :
```
    implementation("androidx.navigation:navigation-compose:2.9.4")

    IF NavHost doesnt work, add this to the top of the mainActivity or wtv : 
    import androidx.navigation.compose.NavHost
```

* Composer (icons)
```
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt:coil-compose:2.7.0")
```
