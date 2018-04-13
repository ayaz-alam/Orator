package com.medeveloper.ayaz.orator;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    public FillerWords ScreenFillerWord;
    SessionDetail sessionDetail;
    DatabaseReference baseRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        final View rootView = inflater.inflate(R.layout.activity_home, parent, false);
        baseRef= FirebaseDatabase.getInstance().getReference("NextSession");

        baseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    try{
                        sessionDetail=dataSnapshot.getValue(SessionDetail.class);
                        if(sessionDetail!=null)
                            populateFields(rootView);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });



        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    private void populateFields(View rootView) {
        ((TextView)rootView.findViewById(R.id.next_session_date)).setText(sessionDetail.date+" | "+sessionDetail.time);

        ((TextView)rootView.findViewById(R.id.next_session_topic)).setText(sessionDetail.topic);

        ((TextView)rootView.findViewById(R.id.speaker_of_the_day)).setText(sessionDetail.speaker);

        ((TextView)rootView.findViewById(R.id.word_of_the_day)).setText(sessionDetail.word);
        ((TextView)rootView.findViewById(R.id.word_of_the_day_explanation)).setText(sessionDetail.meaning);
    }

}

