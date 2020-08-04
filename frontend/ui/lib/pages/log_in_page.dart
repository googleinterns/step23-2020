import 'package:tripmeout/services/login_service.dart';
import 'package:tripmeout/widgets/log_in_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:flutter/material.dart';

class LogInPageArguments {
  final String redirectUrl;
  LogInPageArguments(this.redirectUrl);
}

class LogInPage extends StatelessWidget {
  final LogInService logInService;
  final LogInPageArguments arguments;
  LogInPage(this.logInService, this.arguments);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(
          child: LogInWidget(logInService, arguments.redirectUrl ?? '/trips')),
    );
  }
}
