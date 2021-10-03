import 'dart:async';
import 'dart:ui';
import 'package:flutter/services.dart';

class BackgroundService {
  static const MethodChannel _backgroundChannel = MethodChannel('br.com.brenohff.background', JSONMethodCodec());

  static final BackgroundService _instance = BackgroundService._internal().._setupBackgroundService();

  BackgroundService._internal();

  factory BackgroundService() => _instance;

  static Future<bool> initialize(Function onStart) async {
    final CallbackHandle? handle = PluginUtilities.getCallbackHandle(onStart);

    if (handle == null) {
      return false;
    }

    final service = BackgroundService();
    service._setupBackgroundService();

    return await _backgroundChannel.invokeMethod("startService", {
      "handle": handle.toRawHandle(),
    });
  }

  Future<bool> stopService() async {
    return await _backgroundChannel.invokeMethod("stopService");
  }

  void _setupBackgroundService() {
    _backgroundChannel.setMethodCallHandler(_handle);
  }

  Future<dynamic> _handle(MethodCall call) async {
    return true;
  }
}
