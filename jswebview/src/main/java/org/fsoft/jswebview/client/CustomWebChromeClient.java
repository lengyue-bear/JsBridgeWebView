package org.fsoft.jswebview.client;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * @author lengyue
 * @create 2021-01-12 19:34
 * @organize fsoft
 * @describe 通过WebChromeClient回调处理进度条
 * @update
 */
public class CustomWebChromeClient extends WebChromeClient {
    private final static int MAX_HIDE_PROGRESS = 95;

    private ProgressBar mProgressBar;

    public CustomWebChromeClient(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress >= MAX_HIDE_PROGRESS) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            if (mProgressBar.getVisibility() == View.GONE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            mProgressBar.setProgress(newProgress);
        }
        //mProgressBar.setVisibility(View.GONE);
        super.onProgressChanged(view, newProgress);
    }
}
