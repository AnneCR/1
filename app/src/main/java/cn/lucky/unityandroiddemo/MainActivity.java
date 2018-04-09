package cn.lucky.unityandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallListener;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {//1、注意修改集成的类，改成UnityPlayerActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstall();//个性化安装
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);//获取唤醒参数
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(final AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            //回调数据 控件对象名称，脚本方法，返回的数据
            UnityPlayer.UnitySendMessage("Main Camera","wakeup","data="+bindData+";channelCode="+channelCode);
        }
    };

    /**
     * 在APP需要个性化安装参数时（由web网页中传递过来的，如邀请码、游戏房间号等自定义参数）
     * 调用OpenInstall.getInstall方法，在回调中获取参数（可重复获取）
     * 适用于免填邀请码安装，自动加好友，渠道统计等安装来源追踪的解决方案
     */
    public void getInstall() {

        //获取OpenInstall数据，去掉外层的if判断就可以重复多次调用，推荐每次需要的时候调用，而不是自己保存数据
            OpenInstall.getInstall(new AppInstallListener() {
                @Override
                public void onInstallFinish(AppData appData, Error error) {
                    Log.d("OpenInstall ", "InstallFinish");
                    if (error == null) {
                        //根据自己的业务处理返回的数据
                        String data = appData.getData();
                        String channelCode = appData.getChannel();
                        //回调数据 对象名称，脚本方法，返回的数据
                        UnityPlayer.UnitySendMessage("Main Camera","install","data="+data+";channelCode="+channelCode);
                    } else {
                        Log.e("MainActivity", "errorMsg : " + error.toString());
                    }
                }
            });
    }

    //数据上报统计
    public void reportRegister(){
        OpenInstall.reportRegister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }

}
