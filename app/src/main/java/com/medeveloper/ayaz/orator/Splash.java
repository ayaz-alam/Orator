package com.medeveloper.ayaz.orator;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class Splash extends AppCompatActivity {
    private static final int RC_SIGN_IN = 121;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animation1= AnimationUtils.makeInChildBottomAnimation(this);
        animation1.setDuration(1000);
        TextView t=findViewById(R.id.splash_text);
        TextView t2=findViewById(R.id.splash_text_2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pacifico.ttf");
        t.setTypeface(custom_font);
        t.setAnimation(animation1);
        t2.setTypeface(custom_font);
        t2.setAnimation(animation1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Authenticate();


            }
        },1500);
    }



    private void Authenticate() {
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(this,Base.class));
            Toast.makeText(this,"Welcome : "+mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    mAuth=FirebaseAuth.getInstance();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Members");
                    Person person = new Person("" + mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getUid());
                    databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            startActivity(new Intent(getApplicationContext(), Base.class));
                            finish();
                        }
                    });

                }

            }
    }
    }
}
