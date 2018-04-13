package com.medeveloper.ayaz.orator;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class YourDetail extends Fragment {
        // The onCreateView method is called when Fragment should create its View object hierarchy,
        // either dynamically or via XML layout inflation.

        ArrayList<String> list;
        mArrayAdapter adapter;
        ListView listView;
        DatabaseReference databaseReference;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            // Defines the xml file for the fragment
            View rootView = inflater.inflate(R.layout.fragment_your_detail, parent, false);
            list=new ArrayList<>();
            list.add("Loading Please Wait");
            listView=rootView.findViewById(R.id.listview);
            adapter=new mArrayAdapter(getActivity(),list);
            listView.setAdapter(adapter);
            databaseReference = FirebaseDatabase.getInstance().
                    getReference("Session_Details").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                list.clear();
                                for(DataSnapshot d:dataSnapshot.getChildren())
                                    list.add(d.getKey());
                                if(list!=null)
                                    prepareAdapter(list);
                            }
                            else Toast.makeText(getActivity().getApplicationContext(),"Error DataSnapshot doesn't Exist",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

          //  Toast.makeText(getActivity().getApplicationContext(),""+listView.getChildCount(),Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return rootView;

        }

    private void prepareAdapter(ArrayList<String> mList) {
        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String child=list.get(position);
                ((Base)getActivity()).ChangeFragment(child);
            }
        });
    }

}


class mArrayAdapter extends ArrayAdapter<String>
{

    //Resource id for background color of list

    public mArrayAdapter(Activity context, ArrayList<String> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check if the current view is reused else inflate the view
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }

        //get the object located at position
        String word_item = getItem(position);

        //find the textview in list_item with id default_text_view
        TextView defaultTextView = listItemView.findViewById(R.id.session_date);
        //gets the default Translation and set it to the text of this textView;
        defaultTextView.setText(word_item);

        return listItemView;
    }

}