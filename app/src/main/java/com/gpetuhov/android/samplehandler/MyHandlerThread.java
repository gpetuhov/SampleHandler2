package com.gpetuhov.android.samplehandler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

// HandlerThread has Looper and can be reused.
// HandlerThread must be started by start() to become usable
// and stopped by calling quit() to free resources, when it is no longer needed.
public class MyHandlerThread extends HandlerThread {

  private Handler handler;
  private Handler customHandler;

  MyHandlerThread(String name) {
    super(name);
  }

  public void postTask(Runnable task) {
    // Here we send message that contains only Runnable,
    // which is executed, when the message is handled.
    handler.post(task);
  }

  public void prepareHandler() {
    // This handler uses looper from this thread
    handler = new Handler(getLooper());

    // When the message, that does not contain Runnable, is delivered to the handler,
    // handler's handleMessage() method is executed. By default this method is empty,
    // but we can implement it by providing our Handler.Callback into the handler's constructor.
    customHandler = new Handler(getLooper(), new Handler.Callback() {
      @Override
      public boolean handleMessage(Message msg) {
        Utils.sleep(2);

        // Get TextView from the message
        final TextView textView = (TextView) msg.obj;

        // Here we use looper from the main thread,
        // because views can only be modified on the main thread.
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            if (textView != null) {
              // Change TextView contents
              String receiveTime = String.valueOf(SystemClock.elapsedRealtime()/1000);
              textView.setText(String.format("Custom message handling finished at %s", receiveTime));
            }
          }
        });

        return true;
      }
    });
  }

  public void postCustomMessage(TextView textView) {
    // Message can contain any object. Here we pass TextView into message.
    // We obtain message from customHandler and then send it to target - to customHandler.
    // "what" field is not used in this example.
    customHandler.obtainMessage(1, textView).sendToTarget();
  }
}
