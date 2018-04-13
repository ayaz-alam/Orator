package com.medeveloper.ayaz.orator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.ArrayList;

public class PersonSelector extends AppCompatActivity {
    ArrayList<Person> Users;
    PersonListAdapter Adapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_selector);
        Users=new ArrayList<>();
        mListView=findViewById(R.id.person_list_view);
        prepareList();


    }

    private void prepareList() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot d:dataSnapshot.getChildren())
                    {
                        try {
                            Users.add(d.getValue(Person.class));
                        }catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"Firebases Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                    setAdapter(Users);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(final ArrayList<Person> users) {
        Adapter=new PersonListAdapter(this,users);
        mListView.setAdapter(Adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                Person text =users.get(position);
                data.putExtra("name",text.getmName());
//---set the data to pass back---
                data.setData(Uri.parse(text.getmUid()));
                setResult(RESULT_OK, data);
//---close the activity---
                finish();
            }
        });

    }
}

class Person implements Serializable
{
    private String mName,mUid;

    public Person() {
    }

    public Person(String mName, String mUid) {
        this.mName = mName;
        this.mUid = mUid;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}

class PersonListAdapter extends ArrayAdapter<Person>
{

    public PersonListAdapter(Context context, ArrayList<Person> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Person user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.name);

        // Populate the data into the template view using the data object
        tvName.setText(user.getmName());
        // Return the completed view to render on screen
        return convertView;
    }
}

