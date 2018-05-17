package com.gpetuhov.android.samplehandler;

import android.os.Handler;
import android.os.HandlerThread;

// HandlerThread has Looper and can be reused.
// HandlerThread must be started by start() to become usable
// and stopped by calling quit() to free resources, when it is no longer needed.
public class MyHandlerThread extends HandlerThread {

  private Handler handler;

  public MyHandlerThread(String name) {
    super(name);
  }

  public void postTask(Runnable task) {
    handler.post(task);
  }

  public void prepareHandler() {
    // This handler uses looper from this thread
    handler = new Handler(getLooper());
  }
}
