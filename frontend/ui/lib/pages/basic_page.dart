import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

class BasicPage extends StatelessWidget {
  final Widget child;
  final Widget floatingActionButton;

  BasicPage({this.child, this.floatingActionButton});

  @override
  Widget build(BuildContext context) {
    // This uses spacers only if the page is expanded beyond a certain point.
    // hopefully this means it looks kinda good on both wide and small windows.
    return Scaffold(
      appBar: defaultAppBarWithNoExtras(context),
      body: LayoutBuilder(
        builder: (context, constraints) {
          if (constraints.maxWidth > 1000) {
            return Row(
              children: [
                Spacer(),
                SizedBox(
                  child: child,
                  width: 1000,
                ),
                Spacer(),
              ],
            );
          } else {
            return Row(
              children: [
                Expanded(child: child),
              ],
            );
          }
        },
      ),
      floatingActionButton: floatingActionButton,
    );
  }
}
