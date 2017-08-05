package com.kumar.shushil.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class contacts extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_contacts;
    private String url;
    private DatabaseReference mroot;
    private EditText uid;
    private Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.costom_titlebar);
        listView= (ListView) findViewById(R.id.lvChats);
        list_of_contacts=new ArrayList<>();
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_contacts);
        listView.setAdapter(arrayAdapter);
        url="messages/"+getIntent().getStringExtra("u_id");
        uid=(EditText) findViewById(R.id.uidname);
        add=(Button)findViewById(R.id.addbtn);
        mroot= FirebaseDatabase.getInstance().getReference(url);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference uroot=FirebaseDatabase.getInstance().getReference("users");
                uroot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(uid.getText().toString()))
                        {   if(uid.getText().toString().equals( getIntent().getStringExtra("u_id")))
                            Toast.makeText(contacts.this, "Can't message yourself!!", Toast.LENGTH_SHORT).show();
                            else
                            {
                            Map<String, Object> addmap = new HashMap<String, Object>();
                            addmap.put(uid.getText().toString(), "");
                            mroot.updateChildren(addmap);
                            }
                        }
                        else
                            Toast.makeText(contacts.this, "user doesn't exist", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(contacts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        mroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_contacts.clear();
                list_of_contacts.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(contacts.this,chat.class);
                intent.putExtra("c_name",((TextView)view).getText().toString());
                intent.putExtra("u_id",getIntent().getStringExtra("u_id"));
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
