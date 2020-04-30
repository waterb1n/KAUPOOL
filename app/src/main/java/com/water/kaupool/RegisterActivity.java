package com.water.kaupool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.water.kaupool.LoginActivity.db_member;

public class RegisterActivity extends AppCompatActivity {
    EditText id, name, pw, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = (EditText) findViewById(R.id.JoinId);
        pw = (EditText) findViewById(R.id.JoinPass);
        name = (EditText) findViewById(R.id.JoinName);
        phone = (EditText) findViewById(R.id.JoinPhone);
    }


    public void checkId(View view) { // ID 확인
        final String user_id = id.getText().toString();

        Query id_query = db_member.orderByChild("member_id").equalTo(user_id);
        id_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {//사용가능
                    Toast.makeText(RegisterActivity.this, "사용가능한 아이디입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkName(View view) { // Name 확인
        final String user_name = name.getText().toString();

        Query name_query = db_member.orderByChild("member_name").equalTo(user_name);
        name_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {//사용가능
                    Toast.makeText(RegisterActivity.this, "사용가능한 닉네임입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void RegisterToLogin(View view) {
        final String user_id = id.getText().toString();
        final String user_pw = pw.getText().toString();
        final String user_name = name.getText().toString();
        final String user_phone = phone.getText().toString();

        Query id_query = db_member.orderByChild("member_id").equalTo(user_id);
        id_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) { //사용가능
                    Query name_query = db_member.orderByChild("member_name").equalTo(user_name);

                    name_query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {//사용가능한 닉네임
                                member m = new member(user_id, user_pw, user_name, user_phone, "w");
                                db_member.child(user_id).setValue(m);

                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


