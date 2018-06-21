package com.longshihan.module_gank.http;



import com.longshihan.module_gank.mvp.MainModule.model.entity.FunnyData;
import com.longshihan.module_gank.mvp.MainModule.model.entity.GanHuoData;
import com.longshihan.module_gank.mvp.MainModule.model.entity.GankData;
import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The Http Api of Gank
 *
 * @author Johnny Shieh
 * @version 1.0
 */
public interface GankService {


    // http://gank.io/api/data/数据类型/请求个数/第几页
    @GET(value = "data/福利/" + GankApi.MEIZI_SIZE + "/{page}")
    Observable<MeiziData> getMeiziData(@Path("page") String page);

    @GET("data/休息视频/" + GankApi.MEIZI_SIZE + "/{page}")
    Observable<FunnyData> getFunnyData(@Path("page") int page);

    //请求某天干货数据
    @GET("day/{year}/{month}/{day}")
    Observable<GankData> getDailyData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    //请求不同类型干货（通用）
    @GET("data/{type}/" + GankApi.GANK_SIZE + "/{page}")
    Observable<GanHuoData> getGanHuoData(@Path("type") String type, @Path("page") int page);

}
