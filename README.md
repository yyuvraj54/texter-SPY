# texter-SPY

texter is a sophisticated spyware application designed to monitor the target device's surroundings and call activity while running in the background. This Android Studio project is compatible with devices running API levels 26 to 28 (Android 9 and 10). Upon installation, the app hides itself from the main menu (except on Android 10) and operates discreetly in the background using Android's Service framework.
it a spy app that will hide after installation (after first open but not in android 10) and there is a remort app for this to control this app 
Remort app link (https://github.com/yyuvraj54/Remort-App-spy)

## Features
- Real-time Monitoring: Tracks and records the surroundings and call activity of the host device.
- Stealth Operation: Disappears from the main menu after the first launch, running covertly in the background.
- Call Monitoring: Uses broadcast receivers to detect call events and phone restarts.
- Remote Control: Comes with a remote app to manage and control the host application.
- Cloud Storage: Uploads recorded files to Firebase Cloud Storage, ensuring no trace on the host device.
- Device Status: Provides information on the host device's online status and call activity.

## Getting Started
- Install the App: Install texter on the target (host) device to begin monitoring.
- Stealth Mode Activation: After the first launch, the app hides from the main menu and continues to run in the background.
- Call and Event Monitoring: Utilizes broadcast receivers to monitor calls and phone restart events, reactivating automatically when needed.


## Remote App Functionality
- Start and Stop Recordings: Initiate and halt recordings of calls and surroundings on the host device.
- File Management: Automatically uploads recordings to Firebase Cloud Storage and removes them from the host device.
- Online Status: Check if the host device is online.
- Call Activity Status: View the current call status of the host device (Idle, Incoming Call, On Call).
- Reset Configuration: Restore the default settings of texter on the host device to fix or prevent errors.


## How to Use the App
texter is an app that needs to be installed on the host phone to monitor its surroundings and call activity. Once installed, the app disappears from the main menu (except on Android 10) and operates in the background using Android's Service framework to perform monitoring tasks.

Monitoring and Recording
- Broadcast Receiver: Monitors calls and phone restart events to ensure continuous operation.
- Remote Control: The remote app allows you to control the host application. Start and stop recordings remotely, with files uploaded to Firebase Cloud Storage and removed from the host device.
- Online Status and Call Activity: The remote app shows the host device's online status and current call activity, including Idle, Incoming Call, and On Call states.


Remote App
The remote app provides comprehensive control over the host device's texter application:

- Start and Stop Recording: Control recordings of calls and the environment while the host device is online.
- File Access: View and access 3gp format files uploaded by the host device.
- Device Status: Check the online status and current call activity of the host device.
- Reset Configuration: Restore default settings on the host application to ensure smooth operation.


## Developers✨
**Design and Developed by Yuvraj Singh Yadav**
- Email: yyuvraj54@gmail.com
- GitHub: [yyuvraj54](https://github.com/yyuvraj54)
