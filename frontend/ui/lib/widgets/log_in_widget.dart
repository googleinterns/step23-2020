import 'package:flutter/material.dart';
import 'package:tripmeout/services/login_service.dart';
import 'package:url_launcher/url_launcher.dart';

class LogInWidget extends StatelessWidget {
  final LogInService service;
  final String redirectUrl;

  LogInWidget(this.service, this.redirectUrl, {Key key}) : super(key: key);

  void _redirectToLogInUrl() async {
    String url = await service.getLogInLink(redirectUrl);
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      print('Failed to launch $url.');
    }
  }

  @override
  Widget build(BuildContext context) {
    final loginButon = Material(
      elevation: 5.0,
      borderRadius: BorderRadius.circular(10.0),
      child: MaterialButton(
        padding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
        onPressed: _redirectToLogInUrl,
        child: Text(
          "Login",
          textAlign: TextAlign.center,
        ),
      ),
    );

    return Container(
      child: Padding(
        padding: const EdgeInsets.all(36.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              height: 155.0,
              width: 300.0,
              child: Icon(Icons.airplanemode_active),
            ),
            SizedBox(
              height: 155.0,
              child: Text("Welcome to Trip Me Out."),
            ),
            loginButon,
            SizedBox(
              height: 15.0,
            ),
          ],
        ),
      ),
    );
  }
}
