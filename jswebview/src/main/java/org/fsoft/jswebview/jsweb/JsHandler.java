package org.fsoft.jswebview.jsweb;

import org.fsoft.jswebview.bridge.CallBackFunction;

/**
 * @author lengyue
 * @create 2021-01-12 17:13
 * @organize fsoft
 * @describe Native调用JS处理
 * @update
 */
public interface JsHandler {

    void onJsHandler(String handlerName, String responseData, CallBackFunction function);
}
