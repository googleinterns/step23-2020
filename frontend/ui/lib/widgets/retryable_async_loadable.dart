import 'package:flutter/material.dart';

/// Provides loading and retrying functionality.
/// This should only be used with an idempotent async function.
class RetryableAsyncLoadable<T> extends StatefulWidget {
  final _OnLoad<T> onLoad;
  final _LoadFunction<T> loadFunction;
  final String errorMessage;

  RetryableAsyncLoadable({
    key,
    @required this.onLoad,
    @required this.loadFunction,
    @required this.errorMessage,
  }) : super(key: key);

  @override
  State createState() =>
      _RetryableAsyncLoadableState(onLoad, loadFunction, errorMessage);
}

typedef _LoadFunction<T> = Future<T> Function();
typedef _OnLoad<T> = Widget Function(T);

class _RetryableAsyncLoadableState<T> extends State<RetryableAsyncLoadable<T>> {
  final _OnLoad<T> onLoad;
  final _LoadFunction<T> loadFunction;
  final String errorMessage;
  bool loading = true;
  Exception error;
  T t;

  _RetryableAsyncLoadableState(
      this.onLoad, this.loadFunction, this.errorMessage) {
    startLoad();
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return Center(child: CircularProgressIndicator());
    }
    if (error != null) {
      print(error);
      return AlertDialog(
        title: Text(errorMessage),
        content: Text('Would you like to retry?'),
        actions: [
          FlatButton.icon(
            onPressed: () {
              startLoad();
            },
            icon: Icon(Icons.refresh),
            label: Text('Retry'),
          ),
        ],
      );
    }
    if (t != null) {
      return onLoad.call(t);
    }
    // TODO: do something smart here...
    return Container();
  }

  void startLoad() async {
    if (!loading) {
      setState(() {
        this.loading = true;
        this.error = null;
        this.t = null;
      });
    }
    try {
      var t = await loadFunction.call();
      setState(() {
        this.loading = false;
        this.error = null;
        this.t = t;
      });
    } catch (err) {
      setState(() {
        this.loading = false;
        this.error = err;
        this.t = null;
      });
    }
  }
}
