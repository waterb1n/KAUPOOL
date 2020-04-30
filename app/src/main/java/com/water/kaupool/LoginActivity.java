package com.water.kaupool;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    static DatabaseReference dbTable, db_member, db_manager;
    static String loginName, loginId;
    EditText id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    /**
     * https://blog.naver.com/ajh794/221011436910
     */

    private void init() {
        dbTable = FirebaseDatabase.getInstance().getReference("kaupool"); //db 참조 객체 선언
        db_member = dbTable.child("member_info");

        id = (EditText) findViewById(R.id.loginId);
        password = (EditText) findViewById(R.id.loginPasswd);
    }

    public void goto_Register(View view) {
        Intent i_register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i_register);
    }

    public void goto_Main(View view) {
        final String user_id = id.getText().toString();
        final String user_pw = password.getText().toString();

        Query query = db_member.orderByKey().equalTo(user_id); //id와 같은 user의 정보를 가져옴

        query.addListenerForSingleValueEvent(new ValueEventListener() { //핸들러로 쿼리 내용 확인
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    member m = data.getValue(member.class);

                    if (user_pw.equals(m.getMember_pw())) {
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        loginName = m.getMember_name();
                        loginId = m.getMember_id();
                        Toast.makeText(LoginActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                        startActivity(loginIntent);
                    } else {
                        id.setText("");
                        password.setText("");
                        Toast.makeText(LoginActivity.this, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
