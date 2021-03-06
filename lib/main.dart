import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'notification.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  AwesomeNotifications().initialize(
    'resource://drawable/logo',
    [
      NotificationChannel(
        channelKey: 'basic_channel',
        channelName: 'Basic notifications',
        channelDescription: 'Notification channel for basic tests',
      ),
    ],
  );

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String serviceStatus = "";
  String serviceDestroyedInfo = "";

  Future<void> getStatus() async {
    String statusResult = await status();

    List<String> statusSplit = statusResult.split(" - ");

    setState(() {
      serviceStatus = statusSplit[0];
      serviceDestroyedInfo = statusSplit[1];
    });
  }

  @override
  Widget build(BuildContext context) {
    getStatus();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Flutter Background Service'),
      ),
      body: Container(
        padding: const EdgeInsets.all(20),
        color: Colors.white,
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Text(
                serviceStatus,
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontSize: 20,
                  color: Colors.black,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 25, top: 10),
                child: Text(
                  'Service destroyed at:\n$serviceDestroyedInfo',
                  textAlign: TextAlign.center,
                  style: const TextStyle(
                    fontSize: 16,
                    color: Colors.black,
                  ),
                ),
              ),
              ElevatedButton(
                child: const Text('Start Service'),
                onPressed: () {
                  start();
                },
              ),
              ElevatedButton(
                child: const Text('Stop Service'),
                onPressed: () {
                  stop();
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
