package com.kumar.shushil.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class success_signup extends AppCompatActivity {
    private TextView email;
    private TextView user;
    private TextView pass;
    private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_signup);
        email=(TextView)success_signup.this.findViewById(R.id.emailview);
        user=(TextView)success_signup.this.findViewById(R.id.usernameview);
        pass=(TextView)success_signup.this.findViewById(R.id.passwordview);
        loginbtn=(Button)success_signup.this.findViewById(R.id.finish);
        email.setText("Email: "+getIntent().getStringExtra("email"));
        user.setText("Username: "+getIntent().getStringExtra("u_id"));
        pass.setText("password: "+getIntent().getStringExtra("pwd"));
        setTitle("Details");
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(success_signup.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
