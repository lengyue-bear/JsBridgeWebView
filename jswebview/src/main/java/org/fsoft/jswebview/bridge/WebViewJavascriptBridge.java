package org.fsoft.jswebview.bridge;

public interface WebViewJavascriptBridge {

    void send(String data);

    void send(String data, CallBackFunction responseCallback);
}
