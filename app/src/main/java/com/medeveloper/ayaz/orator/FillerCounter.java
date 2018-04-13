package com.medeveloper.ayaz.orator;

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
import android.widget.SeekBar;
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


public class FillerCounter extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    private static final int MEMBER_SELECT_CODE = 121;
    FirebaseAuth mAuth;
    DatabaseReference BaseRef;
    LinearLayout Long_Pause;
    RelativeLayout Filler_Word;
    LinearLayout Sentence_Break;
    Boolean TimerOn;
    TextView SelectMember;
    String MemberUid;
    Button Submit;
    private String SessionDate="Session Date";
    Chronometer myTimer;
    private long pauseOffset;
    private boolean running;
    boolean isRunning;
    int maxtime=120000;
    SeekBar timerSeekBar;
    TextView filler_word_count;//=rootView.findViewById(R.id.filler_word_count);
    TextView long_pause_count;//=rootView.findViewById(R.id.long_pause_count);
    TextView sentence_break_count;//=rootView.findViewById(R.id.sentence_break_count);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        final View rootView = inflater.inflate(R.layout.activity_filler_counter, parent, false);

        mAuth=FirebaseAuth.getInstance();
        Long_Pause=rootView.findViewById(R.id.long_pause_container);
        Filler_Word=rootView.findViewById(R.id.filler);
        Sentence_Break=rootView.findViewById(R.id.sentence_container);
        SelectMember=rootView.findViewById(R.id.member_select_text);
        filler_word_count=rootView.findViewById(R.id.filler_word_count);
        long_pause_count=rootView.findViewById(R.id.long_pause_count);
        sentence_break_count=rootView.findViewById(R.id.sentence_break_count);
        timerSeekBar=rootView.findViewById(R.id.seekBar);

        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("NextSession").child("date");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                SessionDate = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Submit=rootView.findViewById(R.id.submit);

        SelectMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),PersonSelector.class),MEMBER_SELECT_CODE);
            }
        });

        View.OnClickListener filler=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t=rootView.findViewById(R.id.filler_word_count);
                int x=Integer.parseInt(t.getText().toString());
                x++;
                t.setText(x+"");
            }
        };
        View.OnClickListener sentence_break_listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t=rootView.findViewById(R.id.sentence_break_count);
                int x=Integer.parseInt(t.getText().toString());
                x++;
                t.setText(x+"");
            }
        };
        View.OnClickListener long_pause_listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t=rootView.findViewById(R.id.long_pause_count);
                int x=Integer.parseInt(t.getText().toString());
                x++;
                t.setText(x+"");
            }
        };
        filler_word_count.setOnClickListener(filler);
        Filler_Word.setOnClickListener(filler);
        Sentence_Break.setOnClickListener(sentence_break_listener);
        sentence_break_count.setOnClickListener(sentence_break_listener);
        Long_Pause.setOnClickListener(long_pause_listener);
        long_pause_count.setOnClickListener(long_pause_listener);



        Submit.setEnabled(false);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Session_Details").child(MemberUid).child(SessionDate);

                FillerWords fillerWords=new FillerWords(filler_word_count.getText().toString(),long_pause_count.getText().toString(),sentence_break_count.getText().toString(),mAuth.getCurrentUser().getDisplayName());
                databaseReference.child("FillerDetails").setValue(fillerWords).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete())
                        {
                            Reset();
                            Toast.makeText(getActivity().getBaseContext(),"Successfully Submitted",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        isRunning=false;
        myTimer=rootView.findViewById(R.id.chronometer);
        myTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(),"Timer Clicked",Toast.LENGTH_SHORT).show();
                if(isRunning)
                {
                    pauseChronometer();
                }
                else
                {
                    startChronometer();
                }
                isRunning=!isRunning;

            }
        });
        timerSeekBar.setEnabled(false);

        myTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                long time=(SystemClock.elapsedRealtime() - chronometer.getBase());
                timerSeekBar.setMax(maxtime);
                timerSeekBar.setProgress((int)time);
                if ( time>= maxtime) {
                    //Do Something
                    myTimer.setTextColor(getResources().getColor(R.color.error_red));
                    timerSeekBar.setVisibility(View.INVISIBLE);
                }

            }
        });

        ((Button)rootView.findViewById(R.id.reset_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();


            }
        });

        return rootView;
    }

    public void Reset()
    {
        Submit.setEnabled(false);
        SelectMember.setEnabled(true);
        myTimer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        timerSeekBar.setProgress(0);
        filler_word_count.setText(0+"");
        long_pause_count.setText(0+"");
        sentence_break_count.setText(0+"");
        SelectMember.setText("Click to Select Member");
        resetChronometer();
        pauseChronometer();

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void startChronometer() {
        if (!running) {
            myTimer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            myTimer.start();
            running = true;
        }
    }

    public void pauseChronometer() {
        if (running) {
            myTimer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - myTimer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        myTimer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MEMBER_SELECT_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                MemberUid=data.getDataString();
                SelectMember.setText(data.getStringExtra("name")+"");
                Submit.setEnabled(true);
                SelectMember.setEnabled(false);
                resetChronometer();
                pauseChronometer();

            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),"Unable to select member try again",Toast.LENGTH_SHORT).show();
            }
        }

    }

}

class FillerWords
{
    String FillerWords;
    String LongPause;
    String SentenceBreak;
    String Judge;

    public FillerWords() {
    }

    public FillerWords(String fillerWords, String longPause, String sentenceBreak, String judge) {
        FillerWords = fillerWords;
        LongPause = longPause;
        SentenceBreak = sentenceBreak;
        Judge = judge;
    }
}
