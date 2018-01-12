package cn.xhl.client.manga.utils;

import android.support.annotation.Nullable;

import java.io.IOException;

import cn.xhl.client.manga.config.IConstants;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author Mike on 2017/9/5 0005.
 */
public final class UserAgentInterceptor implements Interceptor {
    private static final String TAG = "UserAgentInterceptor";
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private final String userAgentHeaderValue;

    public UserAgentInterceptor(String userAgentHeaderValue) {
        this.userAgentHeaderValue = userAgentHeaderValue;
    }

    @Override
    public Response intercept(@Nullable Chain chain) throws IOException {
        if (chain == null) {
            return null;
        }
        final Request originalRequest = chain.request();
        Request requestWithUserAgent;
        LogUtil.e(TAG, "url = " + originalRequest.url().toString());
        if (originalRequest.url().toString().contains("api.ebandwagon.tk")) {
            // 如果是自己的服务器则上传真正的user-agent，否则伪装
            requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .build();
        } else {
            requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, IConstants.USER_AGENT)
                    .build();
        }
        RequestBody requestBody = requestWithUserAgent.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            LogUtil.e(TAG, buffer.readUtf8());
        }
        return chain.proceed(requestWithUserAgent);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            LogUtil.e(TAG, "========bind line=======");
            LogUtil.e(TAG, "method : " + request.method());
            LogUtil.e(TAG, "url : " + url);
            if (headers != null && headers.size() > 0) {
                LogUtil.e(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogUtil.e(TAG, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtil.e(TAG, "requestBody's content : " + bodyToString(request));
                    } else {
                        LogUtil.e(TAG, "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            LogUtil.e(TAG, "========end line=======");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
