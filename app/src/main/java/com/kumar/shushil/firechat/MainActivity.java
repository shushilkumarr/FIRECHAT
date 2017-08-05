package com.kumar.shushil.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.kumar.shushil.firechat.R.id.pwd;
import static com.kumar.shushil.firechat.R.id.u_id;

public class MainActivity extends AppCompatActivity {
    private EditText u_idview;
    private EditText pwdview;
    private Button login;
    private Button register;
    private String u_idValue;
    private String pwdValue;
    private TextView errView;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference uroot= database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errView=(TextView)MainActivity.this.findViewById(R.id.textView4);
        u_idview=(EditText)MainActivity.this.findViewById(u_id);
        pwdview=(EditText)MainActivity.this.findViewById(pwd);
        login=(Button)MainActivity.this.findViewById(R.id.login);
        register =(Button)MainActivity.this.findViewById(R.id.signup);
        setTitle("login");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_idValue = u_idview.getText().toString();
                if(u_idValue.equals(""))
                    Toast.makeText(MainActivity.this, "username empty.", Toast.LENGTH_SHORT).show();
                else
                {
                pwdValue=pwdview.getText().toString();
                Log.v("EVALUE","Data:"+u_idValue);
                uroot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(u_idValue))
                        {
                            String url="users/".concat(u_idValue).concat("/password");
                            //urlView.setText(url);
                            DatabaseReference proot=database.getReference(url);
                            proot.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pass=dataSnapshot.getValue(String.class);
                                    //Log.v("E_VALUE","Data:"+dataSnapshot.getValue());
                                    if(pass.equals(pwdValue))
                                    {   Intent intent=new Intent(MainActivity.this,contacts.class);
                                        intent.putExtra("u_id",u_idValue);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        errView.setText("wrong password");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else
                        {
                            errView.setText("username not registered");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }}
        });
    }

}
