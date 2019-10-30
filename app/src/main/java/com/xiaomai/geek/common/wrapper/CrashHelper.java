package com.xiaomai.geek.common.wrapper;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiaomai.geek.BuildConfig;

/**
 * Created by XiaoMai on 2017/4/20.
 */

public class CrashHelper {

    private static final String CHANNEL_NAME = "PuGongYing";

    private static final String APP_ID = "41bb6e6575";

    public static void init(Context context) {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppChannel(CHANNEL_NAME);
        // 之前使用过Bugly SDK，请将以下这句注释掉。
//        CrashReport.initCrashReport(context, APP_ID, BuildConfig.DEBUG, strategy);
        Bugly.init(context, APP_ID, BuildConfig.DEBUG, strategy);
    }
}