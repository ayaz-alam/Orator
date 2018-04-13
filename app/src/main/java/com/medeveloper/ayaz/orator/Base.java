package com.medeveloper.ayaz.orator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Base extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setUpUserDetails();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.your_placeholder_screen, new Home()).commit();

    }
    FillerWords fwords;
    ArrayList<String> arrayList;
    public void ChangeFragment(final String child)
    {

        arrayList=null;
        fwords=null;
                FirebaseDatabase.getInstance().getReference("Session_Details").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child(child).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.child("FillerDetails").exists())
                                fwords=dataSnapshot.child("FillerDetails").getValue(FillerWords.class);
                            if(dataSnapshot.child("GrammarError").exists())
                                arrayList=(ArrayList<String>)dataSnapshot.child("GrammarError").getValue();
                            if(fwords!=null&&arrayList!=null)
                            {
                                Toast.makeText(getApplicationContext(),"Showing details of session held on "+child,Toast.LENGTH_SHORT).show();

                                Screen fragment1=new Screen();
                                fragment1.ScreenFillerWord=fwords;
                                fragment1.GrammerMistakes=arrayList;
                                // Insert the fragment by replacing any existing fragment
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.your_placeholder_screen, fragment1).commit();



                            }
                            else
                                Toast.makeText(getApplicationContext(),"Error Occured while Parsing Snapshot",Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void setUpUserDetails() {
        FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        ImageView imageView=headerLayout.findViewById(R.id.display_image);
        Uri uri = mUser.getPhotoUrl();
        if(imageView!=null)
        Picasso.with(this)
                .load(uri)
                .transform(new CircularTransform())
                .resize(150, 150)
                .centerCrop()
                .into(imageView);
        ((TextView)headerLayout.findViewById(R.id.display_name)).setText(mUser.getDisplayName());
        ((TextView)headerLayout.findViewById(R.id.display_email)).setText(mUser.getEmail());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_setter) {

            FirebaseDatabase.getInstance().getReference("admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isAdmin=false;


                    if(dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren())
                            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(d.getValue(String.class)))
                                isAdmin = true;
                        if (isAdmin)
                        startActivity(new Intent(getApplicationContext(),Setter.class));
                        else Toast.makeText(getApplicationContext(), "You are not Admin, Contact Admin for permission", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       Fragment fragment = null;
        Class fragmentClass=Home.class;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fragmentClass = Home.class;
            toolbar.setTitle(R.string.title_activity_base);

        } else if (id == R.id.nav_filler_cap) {
            //Toast.makeText(this,"Gallery Clicked",Toast.LENGTH_SHORT).show();
            fragmentClass = FillerCounter.class;
            toolbar.setTitle(R.string.fragment_name_filler);

        } else if (id == R.id.nav_grammarian) {
            fragmentClass=Grammarian.class;
            toolbar.setTitle(R.string.fragment_name_grammarian);

        } else if (id == R.id.nav_questioner) {
            fragmentClass=Questioner.class;
            toolbar.setTitle(R.string.fragment_name_questioner);

        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();

        } else if (id == R.id.nav_see_report) {
            fragmentClass=YourDetail.class;
            toolbar.setTitle(R.string.fragment_name_sessions);

        }
        try {
          //  Toast.makeText(this,"Came ",Toast.LENGTH_SHORT).show();
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.your_placeholder_screen, fragment).commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }
}
