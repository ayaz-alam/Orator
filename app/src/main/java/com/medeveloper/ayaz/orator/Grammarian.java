package com.medeveloper.ayaz.orator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Intent;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;



import java.util.ArrayList;

public class Grammarian extends android.support.v4.app.Fragment {

    LinearLayout Containr;
    ArrayList<String> mistakeList;
    TextView SelectMember;
    Button SubmitButton;
    final int MEMBER_SELECT_CODE=141;
    String SessionDate="SessionDate";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        final View rootView = inflater.inflate(R.layout.activity_grammarian, parent, false);

        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("NextSession").child("date");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    SessionDate = dataSnapshot.getValue(String.class);
              //  Toast.makeText(getActivity().getApplicationContext(),"Date :"+SessionDate,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Containr=rootView.findViewById(R.id.grammar_display_scroll);
        SelectMember=rootView.findViewById(R.id.member_select_text);

        mistakeList =new ArrayList<>();
        final EditText mistakeBox=rootView.findViewById(R.id.mistake_edittext);
        SubmitButton=rootView.findViewById(R.id.submit_grammarian);
        TextView Add=rootView.findViewById(R.id.add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.lang.String Mistake=mistakeBox.getText().toString();
                if(Mistake.equals(""))
                    mistakeBox.setError("Please Enter Any Mistake");
                else {
                    mistakeList.add(Mistake);

                    AddTextView(Mistake);
                    mistakeBox.setText(null);
                }

            }
        });
        SelectMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),PersonSelector.class),MEMBER_SELECT_CODE);

                }


        });
        SubmitButton.setEnabled(false);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              PushData();

            }
        });






        return rootView;
    }

    private void PushData() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Session_Details").child(MemberUid).child(SessionDate);

        databaseReference.child("GrammarError").setValue(mistakeList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                {
                    SubmitButton.setEnabled(false);
                    mistakeList.clear();
                    if(Containr.getChildCount() > 0)
                        Containr.removeAllViews();
                    SelectMember.setText("Click to Select Member");
                    SelectMember.setEnabled(true);
                    Toast.makeText(getActivity().getBaseContext(),"Successfully Submitted",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void AddTextView(String newItem)
    {


        TextView Ed=new TextView(getActivity().getApplicationContext());
        //Creating Layout Parameter for Text View and Adding it
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(12,6,12,6);
        Ed.setLayoutParams(p);
        Ed.setTextSize(18);
        //Ed.setBackground(getDrawable(R.drawable.layout_with_border));
        Ed.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Ed.setPadding(24,5,5,5);
        Ed.setBackground(getResources().getDrawable(R.drawable.round_));

        Ed.setText(newItem);
        //Adding it to layout
        Containr.addView(Ed);
        Ed.requestFocus();


    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    String MemberUid="Null";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MEMBER_SELECT_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                MemberUid=data.getDataString();
                SelectMember.setText(data.getStringExtra("name")+"");
                SubmitButton.setEnabled(true);
                SelectMember.setEnabled(false);

            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),"Unable to select member try again",Toast.LENGTH_SHORT).show();
            }
        }

    }

}
