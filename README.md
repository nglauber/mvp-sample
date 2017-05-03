# mvp-sample
Demonstrates how to implement MVP (Model View Presenter) pattern using Kotlin, RXJava, Retrofit, Dagger and DataBinding

## Setup
I just cloned this project... how do I run this? Read on!

## Client
Right after importing the project you will notice that it fails to compile due to a missing `google-services.json` file error. This project uses Firebase authentication and that file is required.

### Why isn't `google-services.json` in github?
Quoting Sam [here](https://groups.google.com/forum/#!topic/firebase-talk/bamCgTDajkw):
> The general answer is yes, the google-services.json is safe to check in to your repo and is something that should be shared among engineers on your team.  The JSON file does not contain any super-sensitive information (like a server API key).  It does contain some information like your database URL, Android API key, and storage bucket.  These are not secrets, but if your security rules are not set up correctly an attacker could use them against you.  However since these values are also compiled into your APK as resources, the theoretical attacker would not need your JSON file to get these values anyway.

> For a library or open-source sample we do not include the JSON file because the intention is that users insert their own to point the code to their own backend.  That's why you won't see JSON files in most of our firebase repos on github.

### So how do I get a google-services.json file to use with this project?
You need to create your own firebase project, which is luckily very easy to do:
1. Create a new Firebase project in your Firebase console https://console.firebase.google.com/
1. Choose a name for your project like MVPSample
1. Then choose "Add Firebase to your Android app"
1. And complete with the following:
   - Android package name: br.com.nglauber.exemplolivro
   - App nickname (optional): MVPSample
   - Debug signing certificate SHA-1: The SHA-1 in your debug.keystore (see [here](https://www.youtube.com/watch?v=m_9tk7ME4ZU) if you don't know how to get this exactly)
1. Lastly click in the `Authentication` section `SIGN-IN METHOD` tab and make sure you at least enable Google as a Sign-in provider. You can also enable Facebook but that requires a little bit more setup so feel free to skip initially.

## Server
The client app can either store data in a local database or it can post it to a local server. A basic PHP server is provided under folder `server` that you can use to run this sample.
### Pre-requisites to launch the server 
- PHP Installed
- MySql Installed

Under **`mvp-sample/server`** run:
```sh
$ php -S {your_local_ip}:{your_port} 
```
**Do not use "localhost" for {your_local_ip}**
#### Example:
```sh
php -S 192.168.1.104:8000
```

Make sure  you can reach your server by opening this URL in your browser:
```
http://{your_local_ip}:{your_port}/webservice.php
```

#### Example:
```
http://192.168.1.104:8000/webservice.php
```

You should get a blank page with just a pair of square brackets: []

