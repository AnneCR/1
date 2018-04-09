package cn.lucky.unityandroiddemo;

import android.app.Application;

import com.fm.openinstall.OpenInstall;

/**
 * Created by luck on 2018/3/7.
 */

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OpenInstall.init(this);
    }

}
