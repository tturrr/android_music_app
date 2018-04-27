package com.example.user.nomo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Tag;

public class login_Activity extends AppCompatActivity {

    Button register_button;
    Button login_button;

    EditText id_edit;
    EditText pass_edit;

    private com.kakao.usermgmt.LoginButton btnKakao;
    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        register_button =  (Button)findViewById(R.id.register_button);
        login_button =  (Button)findViewById(R.id.login_button);

        id_edit = (EditText)findViewById(R.id.login_id_text);
        pass_edit = (EditText)findViewById(R.id.login_pass_text);

        callback = new SessionCallback(); //세션콜백을 부르고
        Session.getCurrentSession().addCallback(callback); // 추가시키면 끝입니다!!


        //로그인 버튼을 눌러서 메인 화면으로 이동하는 버튼.
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        //회원가입 버튼을 눌러서 회원가입 화면으로 이동하는 버튼.
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(login_Activity.this,register_Activity.class);
                startActivityForResult(intent,0);
            }
        });

    }




    //회원가입을 하게되었다면 id 텍스트에 있는 글이 그대로 로그인 텍스트에 넘겨진다.
    //카카오톡 로그인 api를 구현한다. success 부분에 로그인성공시에 구현할 것들을 적으면 된다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, intent)){
            return ;
        }
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if(resultCode == 100){
                String id = intent.getStringExtra("id");
                id_edit.setText(id);
            }

        }
    }


    public void request(){
        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("error", "Session Closed Error is " + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile result) {

                Intent intent = new Intent(login_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            request();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("error", "Session Fail Error is " + exception.getMessage().toString());
        }
    }





}
