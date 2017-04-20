package com.sd.vr.education;


import com.sd.vr.R;
import com.sd.vr.education.presenter.FilesManager;
import com.sd.vr.education.presenter.ServiceManager;
import com.sd.vr.education.presenter.ViewAction;
import com.sd.vr.education.utils.Utils;
import com.sd.vr.ctrl.netty.protobuf.MessageProto;
import com.sd.vr.education.vrplayer.VideoPlayerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VREducationMainActivity extends AppCompatActivity implements ViewAction {

    private static final String TAG = VREducationMainActivity.class.getName();

    ServiceManager serviceManager;
    Button sendConnectButton;
    Button sendRegisterButton;
    TextView process;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_vrmain);
        serviceManager = ServiceManager.getInstance();
        serviceManager.bindAction(this);
        sendConnectButton = (Button) findViewById(R.id.connect_send);
        sendConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //向服务端发送数据
//                MessageProto.ReConnectRequest reConnectRequest = MessageProto.ReConnectRequest.newBuilder().setEventId("REGISTER").setEquipmentId(Utils.getDeviceId(VREducationMainActivity.this)).build();
//                MessageProto.MessageRequest request = MessageProto.MessageRequest.newBuilder().setType(MessageProto.Types.RECONNECT).setReConnectRequest(reConnectRequest).build();
//                System.out.println("发送数据："+request.toString());
//                serviceManager.sendRequest(request);

//                serviceManager.requestProgress();
            }
        });

        sendRegisterButton = (Button) findViewById(R.id.register_send);
        sendRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务端发送数据
                MessageProto.RegisterRequest registerRequest = MessageProto.RegisterRequest.newBuilder().setEventId("RECONNECT").setEquipmentId(Utils.getDeviceId(VREducationMainActivity.this)).build();
                MessageProto.MessageRequest request = MessageProto.MessageRequest.newBuilder().setType(MessageProto.Types.REGISTER).setRegisterRequest(registerRequest).build();
                System.out.println("发送数据："+request.toString());
                serviceManager.sendRequest(request);
            }
        });

        process = (TextView) findViewById(R.id.process);

    }

    @Override
    public void stop() {
        System.out.println("收到服务端指令----->暂停");
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void start(String fileId) {
        String url = FilesManager.DIRECTORY+"/"+ fileId;
//        String url = Environment.getExternalStorageDirectory().getAbsolutePath()+"/F5fly.mp4";
        Log.e(TAG, "URL:"+url);
        Intent intent = new Intent(VREducationMainActivity.this, VideoPlayerActivity.class);
        intent.putExtra("START",url);
        startActivity(intent);
    }

    @Override
    public void updateprocess(final String process) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                VREducationMainActivity.this.process.setText(process);
            }
        });
    }
}
