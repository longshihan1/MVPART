package com.longshihan.mvpcomponent.http.base;


import com.longshihan.mvpcomponent.http.GlobalHttpHandler;
import com.longshihan.mvpcomponent.utils.AppService;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author longshihan
 * @time 2017/9/6 17:07
 * @des
 */

public class GlobalHttpHandlerImpl implements GlobalHttpHandler {
    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
        /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                       重新请求token,并重新执行请求 */

           /* 这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                    注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                    create a new request and modify it accordingly using the new token
                    Request newRequest = chain.request().newBuilder().header("token", newToken)
                                         .build();

                    retry the request

                    response.body().close();
                    如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可
                    如果不需要返回新的结果,则直接把response参数返回出去 */
        return response;
    }

    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
        try {
            Request.Builder builder = request.newBuilder();
            //从request中获取headers，通过给定的键url_name
            List<String> headerValues = request.headers(AppService.HEADER_KEY);
            if (headerValues != null && headerValues.size() > 0) {
                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader(AppService.HEADER_KEY);
                //匹配获得新的BaseUrl
                String headerValue = headerValues.get(0);
                HttpUrl newBaseUrl = null;
                if (AppService.baseurls.keySet().contains(headerValue)) {
                    newBaseUrl = HttpUrl.parse(AppService.baseurls.get(headerValue));
                } else {
                    newBaseUrl = request.url();
                }
                //从request中获取原有的HttpUrl实例HttpUrl
                HttpUrl oldHttpUrl = request.url();
                //重建新的HttpUrl，修改需要修改的url部分
                HttpUrl newFullUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(newBaseUrl.scheme())
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())
                        .build();
                // builder.addHeader("Connection", "close");
                //重建这个request，通过builder.url(newFullUrl).build()；
                //然后返回一个response至此结束修改
                request = builder.url(newFullUrl).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
