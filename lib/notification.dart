import 'dart:async';

import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:flutter/material.dart';
import 'package:flutter_background/background_service.dart';
import 'package:notifications/notifications.dart';

Notifications? _notifications;
StreamSubscription<NotificationEvent>? _subscription;

Future<bool> start() async {
  return await BackgroundService.initialize(onStart);
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
    print(exception);
  }
}

void onData(NotificationEvent event) {
  if (event.packageName != 'br.com.brenohff.flutter_background' && event.packageName != 'com.crunchyroll.crunchyroid' && event.packageName != 'com.spotify.music') {
    _showNotification(event);
    print(event.toString());
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
