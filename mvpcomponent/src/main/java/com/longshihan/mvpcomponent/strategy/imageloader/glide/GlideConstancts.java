package com.longshihan.mvpcomponent.strategy.imageloader.glide;

/**
 * @author longshihan
 * @time 2017/9/7 10:53
 * @des
 */

public class GlideConstancts {
    public static final int Glide_ALL = 0x0000;//远程数据同时缓存Data和Resource，本地数据仅缓存Resource
    public static final int Glide_NONE = 0x0001;//不缓存任何数据
    public static final int Glide_RESOURCE = 0x0010;//解码之前直接将数据写入硬盘
    public static final int Glide_DATA = 0x0011;//解码之后写入硬盘
    public static final int Glide_AUTOMATIC = 0x0100;//默认策略，根据DataFetcher以及ResourceEncoder的编码策略（EncodeStrategy）智能选择
}
