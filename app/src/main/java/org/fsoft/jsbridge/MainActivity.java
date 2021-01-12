package org.fsoft.jsbridge;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.widget.Toast;

import org.fsoft.jswebview.bridge.CallBackFunction;
import org.fsoft.jswebview.client.CustomWebViewClient;
import org.fsoft.jswebview.jsweb.JavaCallHandler;
import org.fsoft.jswebview.jsweb.JsHandler;
import org.fsoft.jswebview.view.ProgressBarWebView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author lengyue
 * @create 2021-01-12 15:20
 * @organize fsoft
 * @describe 利用JsBridge和WebView（系统的WebView内核）进行通信，可实现js和Native方法互调
 * Demo目的：因为最近项目中需要实现网页按钮点击跳转到App本地页面的功能，找了好久，
 * 但是大多数不符合需求，或者无法编译使用，忙了一天，终于实现了。
 * 所以将项目的测试Demo简化抽象出来，便于以后的开发者使用或者参考，减少踩坑经历。
 * @update
 */
public class MainActivity extends AppCompatActivity {
    private final static int RESULT_CODE = 0x11;

    private ProgressBarWebView mProgressBarWebView;
    private ArrayList<String> mHandlers = new ArrayList<>();

    ValueCallback<Uri> mUploadMessage;
    private CallBackFunction mFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mProgressBarWebView = (ProgressBarWebView) findViewById(R.id.login_progress_webview);
        mProgressBarWebView.setWebViewClient(new CustomWebViewClient(mProgressBarWebView.getWebView()) {
            @Override
            public String onPageError(String url) {
                //如果网页跳转失败，则跳转到此设置的错误页面
                return "file:///android_asset/error.html";
            }

            @Override
            public Map<String, String> onPageHeaders(String url) {

                // 可以加入header
                return null;
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }
        });

        // 打开页面，也可以支持网络url
        mProgressBarWebView.loadUrl("file:///android_asset/demo.html");
        //mProgressBarWebView.loadUrl("http://178.zbg.com/dot");

        mHandlers.add("jumpToLocal");
        mHandlers.add("callNative");
        mHandlers.add("callJs");
        mHandlers.add("open");

        //回调js的方法
        mProgressBarWebView.registerHandlers(mHandlers, new JsHandler() {
            @Override
            public void onJsHandler(String handlerName, String responseData, CallBackFunction function) {

                if (handlerName.equals("jumpToLocal")) {
                    Toast.makeText(MainActivity.this, responseData, Toast.LENGTH_SHORT).show();
                } else if (handlerName.equals("callNative")) {
                    Toast.makeText(MainActivity.this, responseData, Toast.LENGTH_SHORT).show();
                    // 调用你的方法
                    function.onCallBack("深圳，多☁️转晴");
                } else if (handlerName.equals("callJs")) {
                    Toast.makeText(MainActivity.this, responseData, Toast.LENGTH_SHORT).show();
                    // 调用你的方法，自定义
                    function.onCallBack("测试调用自定义方法");
                }else if (handlerName.equals("open")) {
                    mFunction = function;
                    pickFile();
                }
            }
        });

        // 调用js
        mProgressBarWebView.callHandler("callNative", "Hello, 我是Java方法", new JavaCallHandler() {
            @Override
            public void onNativeHandler(String handlerName, String jsResponseData) {
                Toast.makeText(MainActivity.this, "H5返回的数据：" + jsResponseData, Toast.LENGTH_SHORT).show();
            }
        });
        //发送消息给js
        mProgressBarWebView.send("巨笑哈哈", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

            mFunction.onCallBack(intent.getData().toString());
        }
    }

    @Override
    public void onDestroy() {
        if(mProgressBarWebView != null){
            mProgressBarWebView.getWebView().destroy();
            mProgressBarWebView.removeAllViews();
            mProgressBarWebView = null;
        }
        super.onDestroy();
    }
}
