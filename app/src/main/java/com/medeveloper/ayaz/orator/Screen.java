package com.medeveloper.ayaz.orator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class Screen extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    public FillerWords ScreenFillerWord;
    public ArrayList<String> GrammerMistakes;
    private LinearLayout Containr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Defines the xml file for the fragment
        View rootView = inflater.inflate(R.layout.fragment_screen, parent, false);

         Toast.makeText(getContext(),"Came in screen",Toast.LENGTH_SHORT).show();

         if(ScreenFillerWord!=null) {


             ((TextView) rootView.findViewById(R.id.name_screen)).setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
             ((TextView) rootView.findViewById(R.id.fillers_screen)).setText("" + ScreenFillerWord.FillerWords);
             ((TextView) rootView.findViewById(R.id.long_screen)).setText("" + ScreenFillerWord.LongPause);
             ((TextView) rootView.findViewById(R.id.sentence_screen)).setText("" + ScreenFillerWord.SentenceBreak);

         }
        Containr=rootView.findViewById(R.id.my_scroll_view);
         if(GrammerMistakes!=null)
         for(int i=0;i<GrammerMistakes.size();i++)
        {

            TextView Ed=new TextView(getActivity().getApplicationContext());
            //Creating Layout Parameter for Text View and Adding it
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          //  p.setMargins(30,6,12,6);
            Ed.setLayoutParams(p);
            Ed.setTextSize(18);
           // Ed.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            Ed.setPadding(15,5,5,5);

            Ed.setText(GrammerMistakes.get(i));
            //Adding it to layout
            Containr.addView(Ed);

        }




    return rootView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}

