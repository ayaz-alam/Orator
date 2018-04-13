package com.medeveloper.ayaz.orator;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Questioner extends android.support.v4.app.Fragment {
    Button SubmitButton;
    LinearLayout Containr;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View rootView = inflater.inflate(R.layout.activity_questioner, parent, false);
        Containr=rootView.findViewById(R.id.grammar_display_scroll);
        SubmitButton=rootView.findViewById(R.id.submit_grammarian);
        final EditText editText=rootView.findViewById(R.id.mistake_edittext);
        TextView Add=rootView.findViewById(R.id.add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mistake=editText.getText().toString();
                if(Mistake.equals(""))
                    editText.setError("Please Enter Any Question");
                else {


                    AddTextView(Mistake);
                    editText.setText(null);
                }

            }
        });

        return rootView;
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
        Ed.setBackground(getResources().getDrawable(R.drawable.round_));
        Ed.setPadding(15,5,5,5);

        Ed.setText(newItem);
        //Adding it to layout
        Containr.addView(Ed);


    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
