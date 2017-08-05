package com.kumar.shushil.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class signup extends AppCompatActivity {
    public EditText emailView;
    public EditText u_idView;
    public EditText pwdView;
    private TextView errorView;
    private Button signupbtn;
   // private String email;
    private String u_id;
   // private String pwd;
    private Button availability;
    private DatabaseReference uroot = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailView = (EditText) findViewById(R.id.email);
        emailView.requestFocus();
        u_idView = (EditText) findViewById(R.id.u_id);
        pwdView = (EditText) findViewById(R.id.pwd);
        signupbtn = (Button) findViewById(R.id.signupbutton);
        errorView = (TextView) findViewById(R.id.errView);
        availability=(Button) findViewById(R.id.checkbutton);
        setTitle("SignUp");
        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_id=u_idView.getText().toString();
                //errorView.setText(u_id);
                uroot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(u_id))
                        {
                            errorView.setText("username already exists");
                        }
                        else
                        {
                            errorView.setText("username available");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(signup.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_id=u_idView.getText().toString();
               // errorView.setText(u_id);
                uroot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(u_id))
                        {
                            errorView.setText("username already exists");
                        }
                        else
                        {
                           // errorView.setText("username already exists");
                            uroot.child(u_id).child("email").setValue(emailView.getText().toString());
                            uroot.child(u_id).child("password").setValue(pwdView.getText().toString());
                            Intent intent=new Intent(signup.this,success_signup.class);
                            intent.putExtra("email",emailView.getText().toString());
                            intent.putExtra("pwd",pwdView.getText().toString());
                            intent.putExtra("u_id",u_idView.getText().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(signup.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
