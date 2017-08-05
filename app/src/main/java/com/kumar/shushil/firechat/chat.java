package com.kumar.shushil.firechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chat extends AppCompatActivity {
    private ListView msglist;
    //private ArrayList<String> msg;
    //private ArrayAdapter<String> arrayAdapter;
    private String urlsend,urlrec,temp_keysend,temp_keyrec,time,chatusername,chatmsg,chattime;
    private DatabaseReference mrootsend,mrootrec;
    private Button sendbtn;
    private TextView text;
    private EditText sendtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //name=(TextView)findViewById(R.id.chat);
        //name.setText(getIntent().getStringExtra("c_name"));
       // msglist=(ListView)findViewById(R.id.lv_chat);
        //msg=new ArrayList<>();
        //arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,msg);
        //msglist.setAdapter(arrayAdapter);
        sendtext=(EditText)findViewById(R.id.textmsgsend);
        urlsend="messages/"+getIntent().getStringExtra("u_id")+"/"+getIntent().getStringExtra("c_name");
        urlrec="messages/"+getIntent().getStringExtra("c_name")+"/"+getIntent().getStringExtra("u_id");
        mrootsend= FirebaseDatabase.getInstance().getReference(urlsend);
        mrootrec=FirebaseDatabase.getInstance().getReference(urlrec);
        sendbtn=(Button)findViewById(R.id.btnsend);
        text=(TextView) findViewById(R.id.textmsg);
        setTitle(getIntent().getStringExtra("c_name"));
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                if(c.get(Calendar.MINUTE)>10)
                    if((int)c.get(Calendar.AM_PM)==1)
                        time=Integer.toString(c.get(Calendar.HOUR))+":"+Integer.toString(c.get(Calendar.MINUTE))+" PM";
                    else
                        time=Integer.toString(c.get(Calendar.HOUR))+":"+Integer.toString(c.get(Calendar.MINUTE))+" AM";

                else
                    if((int)c.get(Calendar.AM_PM)==1)
                        time=Integer.toString(c.get(Calendar.HOUR))+":0"+Integer.toString(c.get(Calendar.MINUTE))+" PM";
                    else
                        time=Integer.toString(c.get(Calendar.HOUR))+":0"+Integer.toString(c.get(Calendar.MINUTE))+" AM";
                Map<String,Object> map=new HashMap<String, Object>();
                temp_keysend=mrootsend.push().getKey();
                temp_keyrec=mrootrec.push().getKey();
                DatabaseReference message_rootsend=mrootsend.child(temp_keysend);
                map.put("sentby",getIntent().getStringExtra("u_id"));
                map.put("message",sendtext.getText().toString());
                map.put("time",time);
                message_rootsend.updateChildren(map);
                DatabaseReference message_rootrec=mrootrec.child(temp_keyrec);
                message_rootrec.updateChildren(map);
                sendtext.setText("");
            }
        });
       /* mrootsend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                    Iterator m;
                }
                msg.clear();
                msg.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mrootsend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(chat.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void append_chat_conversation(DataSnapshot dataSnapshot)
    {
        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chatmsg=((DataSnapshot)i.next()).getValue().toString();
            chatusername=((DataSnapshot)i.next()).getValue().toString();
            chattime=((DataSnapshot)i.next()).getValue().toString();
            text.append("\n"+chatusername+":\n    "+chatmsg+"\n\t\t\t"+chattime+"\n");
        }

    }

}
