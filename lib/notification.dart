import 'dart:async';

import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:flutter/material.dart';
import 'package:flutter_background/background_service.dart';
import 'package:notifications/notifications.dart';

Notifications? _notifications;
StreamSubscription<NotificationEvent>? _subscription;

List<String> ignorePackage = [
  'br.com.brenohff.flutter_background',
  'com.crunchyroll.crunchyroid',
  'com.spotify.music',
  'com.google.android.gms',
  'com.whatsapp',
];

Future<bool> start() async {
  return await BackgroundService.initialize(onStart);
}

Future<bool> stop() async {
  _subscription?.cancel();
  return await BackgroundService().stopService();
}

Future<String> status() async {
  return await BackgroundService().statusService();
}

void onStart() {
  WidgetsFlutterBinding.ensureInitialized();
  startListening();
}

void startListening() {
  _notifications = Notifications();
  try {
    _subscription = _notifications!.notificationStream!.listen(onData);
  } on NotificationException catch (exception) {
    debugPrint(exception.toString());
  }
}

void onData(NotificationEvent event) {
  if (!ignorePackage.contains(event.packageName)) {
    _showNotification(event);
  }
}

Future _showNotification(NotificationEvent event) async {
  AwesomeNotifications().createNotification(
    content: NotificationContent(
      id: 0,
      channelKey: 'basic_channel',
      title: '${event.title}',
      body: '${event.message} - ${event.packageName}',
      color: const Color(0xFF313031),
      backgroundColor: const Color(0xFF313031),
    ),
    actionButtons: [
      NotificationActionButton(
        key: 'ADD_EXPENSE',
        label: 'Despesa',
        buttonType: ActionButtonType.Default,
      ),
      NotificationActionButton(
        key: 'ADD_INCOMES',
        label: 'Receita',
        buttonType: ActionButtonType.Default,
      ),
    ],
  );
}
