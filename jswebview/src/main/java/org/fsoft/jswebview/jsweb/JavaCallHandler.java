package org.fsoft.jswebview.jsweb;

/**
 * @author lengyue
 * @create 2021-01-12 19:31
 * @organize fsoft
 * @describe JS调用Native方法处理
 * @update
 */
public interface JavaCallHandler {

    void onNativeHandler(String handlerName, String jsResponseData);
}
