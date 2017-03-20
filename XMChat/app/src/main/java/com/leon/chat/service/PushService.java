package com.leon.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.UIUtils;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by leon on 17/2/13.
 */

public class PushService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.sf("PushService onCreate");
        IMService.conn.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                if (message.getBody() != null && !message.getBody().equals("")){
                    UIUtils.showToast(message.getBody());
                }
            }
        },null);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
