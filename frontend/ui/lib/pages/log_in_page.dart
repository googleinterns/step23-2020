import 'package:tripmeout/widgets/log_in_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:flutter/material.dart';

class LogInPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(child: LogInWidget()),
    );
  }
}
