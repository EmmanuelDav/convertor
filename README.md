## currency-converter with Jetpack Compose

`Currency converter` app helps you to convert one currency to another currency

📸 Screens01            |  📸 Screens02  |  📸 Screens03
:-------------------------:|:-------------------------:|:-------------------------:
 ![Screenshot_20240329_081136](https://github.com/oybekjon94/currentConverter-jetpackCompose/assets/91370134/0a649d6b-fd77-4beb-8c0c-f424aea7ed14) | ![Screenshot_20240329_073445](https://github.com/oybekjon94/currentConverter-jetpackCompose/assets/91370134/05de79bf-5fdb-4955-bd24-90e200872b14) | ![Screenshot_20240329_073542](https://github.com/oybekjon94/currentConverter-jetpackCompose/assets/91370134/0fadc3c8-a879-4bea-ba24-99e8a9da8c87)


### 🏗️ Project Structure: 

````
├── src
|    ├── composable
|    |    └── ConvertScreen
|    |  
|    ├── data
|    |    ├── models
|    |    |     ├── ExchangeResponse
|    |    |     ├── Info
|    |    |     └── Query
|    |    └── ConvertApi
|    |
|    ├── di
|    |    └── AppModule
|    |
|    ├── main
|    |    ├── MainRepository
|    |    ├── MainRepositoryImpl
|    |    └── MainViewModel
|    |
└──  ├── utils
     |    ├── ConvertEvent
     |    └── Resource
     |
     ├── MainActivity
     └── MyApp
````

### 🔨 This sample app showcases the following:
- Kotlin
- Clean and Simple UI with Jetpack Compose
- Coroutines
- MVVM(Model + View + ViewModel)
- ApiLayer
- Dagger Hilt for DI
- Retrofit

### Built With 🛠
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Jetpack Compose is Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) -
    - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
- [MVVM stands for Model, View, ViewModel](https://www.digitalocean.com/community/tutorials/android-mvvm-design-pattern) - 
   - `Model`: This holds the data of the application. It cannot directly talk to the View. Generally, it’s recommended to expose the data to the ViewModel through Observables.
   - `View`: It represents the UI of the application devoid of any Application Logic. It observes the ViewModel.
   - `ViewModel`: It acts as a link between the Model and the View. It’s responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to update the View. It’ll ask for the data from the Model.




## Contact

You can connect with me here [LinkedIn](https://www.linkedin.com/in/oybek-kholikov-b354b6258/)
