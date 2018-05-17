package com.gpetuhov.android.samplehandler;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  // By default Handler uses Looper from the thread, from which it is created,
  // so here Handler is using Looper from the UI thread.
  private Handler handler = new Handler();

  private MyHandlerThread myHandlerThread;

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textView = findViewById(R.id.text);
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Create background thread
    Thread myThread = new Thread(new Runnable() {
      @Override
      public void run() {
        Utils.sleep(5);

        Runnable showToast = new Runnable() {
          @Override
          public void run() {
            Toast.makeText(MainActivity.this, "Message from background thread", Toast.LENGTH_SHORT).show();
          }
        };

        // We cannot show Toast message from background thread directly, so we use our handler
        handler.post(showToast);

        Utils.sleep(5);

        // Or we can create new handler and pass looper from the main thread into it
        new Handler(Looper.getMainLooper()).post(showToast);
      }
    });

    myThread.start();

    Runnable anotherTask = new Runnable() {
      @Override
      public void run() {
        Utils.sleep(3);

        handler.post(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(MainActivity.this, "Message from HandlerThread", Toast.LENGTH_SHORT).show();
          }
        });
      }
    };

    // myThread cannot be reused, it can only be recreated. To post multiple tasks we should use HandlerThread
    myHandlerThread = new MyHandlerThread("myHandlerThread");
    myHandlerThread.start();
    myHandlerThread.prepareHandler();
    myHandlerThread.postTask(anotherTask);
    // Second task will be enqueued and will run only after the first task has been executed
    myHandlerThread.postTask(anotherTask);

    myHandlerThread.postCustomMessage(textView);
    myHandlerThread.postCustomMessage(textView);

    // postTask() and postCustomMessage() use different handlers, but message queue is the same,
    // so all of the above four messages will be handled sequentially
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Don't forget to stop HandlerThread
    myHandlerThread.quit();
  }
}
