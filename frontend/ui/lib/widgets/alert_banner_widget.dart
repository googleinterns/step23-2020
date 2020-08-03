import 'dart:ui';
import 'package:flutter/material.dart';

class AlertBannerWidget extends StatelessWidget {
  String title;
  String content;
  VoidCallback continueCallBack;

  AlertBannerWidget(this.title, this.content, this.continueCallBack);

  @override
  Widget build(BuildContext context) {
    return BackdropFilter(
        filter: ImageFilter.blur(sigmaX: 6, sigmaY: 6),
        child: AlertDialog(
          title: Text(
            title,
          ),
          content: Text(
            content,
          ),
          actions: [
            FlatButton(
              child: Text("Continue"),
              onPressed: () {
                continueCallBack();
              },
            ),
            FlatButton(
              child: Text("Cancel"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        ));
  }
}

class AlertBannerWithDismissidget extends StatelessWidget {
  String title;
  String content;
  String buttonName;
  VoidCallback anyFunction;

  AlertBannerWithDismissidget(
      this.title, this.content, this.anyFunction, this.buttonName);

  @override
  Widget build(BuildContext context) {
    return BackdropFilter(
        filter: ImageFilter.blur(sigmaX: 6, sigmaY: 6),
        child: AlertDialog(
          title: Text(
            title,
          ),
          content: Text(
            content,
          ),
          actions: [
            FlatButton(
              child: Text(buttonName),
              onPressed: () {
                anyFunction();
              },
            ),
          ],
        ));
  }
}
