package com.zhy.http.okhttp.callback;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ppg on 2016/4/14.
 */
public abstract class OkHttpCallBack {
    /**
     * 解析为JsonBaseBean,回调给调用者,post方式,会打印参数
     */
    public static abstract class PostTaskCallBack extends Callback<JsonBaseBean> {

        Context mContent;
        private ProgressDialog progressDialog;
        private boolean isShowProgress = true;
        private String progressMsg;

        /**
         * @param Content 上下文
         * @param Msg     进度条的信息,没有则用""表示不开启进度条
         */
        public PostTaskCallBack(Context Content, String Msg) {
            this.mContent = Content;
            this.progressMsg = Msg;
            if (mContent != null)
                progressDialog = new ProgressDialog(mContent);
        }


        @Override
        public void onBefore(Request request, int id) {
            Log.d("okdebug","request:\n"+request.toString());
            super.onBefore(request, id);

            //开启进度条
            if (mContent != null) {
                this.isShowProgress = true;
                if (progressMsg.isEmpty())
                    isShowProgress = false;
                if (isShowProgress) {
                    progressStart(progressMsg);
                }
            }
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            progressClose();
        }

        @Override
        public void onError(Call call, Exception e, int id) {
                if (e!=null){
                    Log.d("okdebug",e.getMessage());
                }
        }

        @Override
        public JsonBaseBean parseNetworkResponse(Response response, int id) throws Exception {
            JsonBaseBean object = new JsonBaseBean();
            String content = response.body().string();
            Log.d("okdebug","response:\n"+content);
            object.analysisJson(content);
            return object;
        }
        /**
         * 简单的进度启动与关闭
         *
         * @param message
         */
        protected void progressStart(String message) {
            if (null == progressDialog)
                return;
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                //((Activity) progressDialog.getContext()).getWindow().setLayout(50, 50);
            }
        }

        protected void progressClose() {
            if (null == progressDialog)
                return;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    /**
     * 解析为JsonBaseBean,回调给调用者,post方式,会打印参数
     */
    public static abstract class PostCeachTaskCallBack extends Callback<JsonBaseBean> {

        Context mContent;
        private ProgressDialog progressDialog;
        private boolean isShowProgress = true;
        private String progressMsg;
        public String URL;

        /**
         * @param Content 上下文
         * @param Msg     进度条的信息,没有则用""表示不开启进度条
         */
        public PostCeachTaskCallBack(Context Content, String Msg) {
            this.mContent = Content;
            this.progressMsg = Msg;
            if (mContent != null)
                progressDialog = new ProgressDialog(mContent);
        }


        @Override
        public void onBefore(Request request, int id) {
            Log.d("okdebug","request:\n"+request.toString());
            super.onBefore(request, id);
            this.URL = request.url().toString();
            //开启进度条
            if (mContent != null) {
                this.isShowProgress = true;
                if (progressMsg.isEmpty())
                    isShowProgress = false;
                if (isShowProgress) {
                    progressStart(progressMsg);
                }
            }
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            progressClose();
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (e!=null){
                Log.d("okdebug",e.getMessage());
            }
        }

        @Override
        public JsonBaseBean parseNetworkResponse(Response response, int id) throws Exception {
            JsonBaseBean object = new JsonBaseBean();
            String content = response.body().string();
            Log.d("okdebug","response:\n"+content);
            object.analysisJson(content);
            String url = response.request().url().toString();
            String mkey = url;
            //以url+post的参数为KEY,把成功获取到的数据转成String存入share
            if (!StringUtil.isEmpty(content)) {
                JsonFileCache.storeData(mContent, mkey, content);
            }
            return object;
        }

        @Override
        public String getURL() {
            if (!StringUtil.isEmpty(URL)) {
                return URL;
            } else return null;
        }

        /**
         * 简单的进度启动与关闭
         *
         * @param message
         */
        protected void progressStart(String message) {
            if (null == progressDialog)
                return;
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                //((Activity) progressDialog.getContext()).getWindow().setLayout(50, 50);
            }
        }

        protected void progressClose() {
            if (null == progressDialog)
                return;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    /**
     * ONLY_CACHE //如果缓存存在,则使用缓存,不在则读取网络
     * ONLY_NETWORK://只读网络
     * CACHE_AND_NETWORK://如果缓存存在先读缓存,然后再读网络.
     */
    public enum CacheMode {
        ONLY_CACHE,
        CACHE_AND_NETWORK,
        ONLY_NETWORK,
        //  CYCLE_NETWORK;
    }

}
