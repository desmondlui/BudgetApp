package com.example.des45.budgetapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<Record> mRecordArrayList = new ArrayList<>();
    private ArrayList<Record> mDraftArrayList = new ArrayList<>();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestoreSettings databaseSetting = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home://Home
                    getRecordDraftFromDatabase();
                    return true;

                case R.id.navigation_stats://Stats
                    getRecordFromDatabase();
                    return true;

                case R.id.navigation_settings://Settings
                    Fragment fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void getRecordFromDatabase()
    {
        //Add database
        mRecordArrayList.clear();
        mFirestore.collection("users").document(mFirebaseUser.getUid()).collection("Record")
                .orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Record record = doc.toObject(Record.class);
                                if(record.getId() != 0) {
                                    mRecordArrayList.add(record);
                                }
                            }
                            //switch to stats fragment after finish
                            Bundle statBundle = new Bundle();
                            Fragment fragment = new StatsFragment();
                            statBundle.putParcelableArrayList("databaseList",mRecordArrayList);
                            fragment.setArguments(statBundle);
                            loadFragment(fragment);
                        }

                    }
                });
    }
    public void getRecordDraftFromDatabase()
    {
        //get the record
        mDraftArrayList.clear();
        mFirestore.collection("users").document(mFirebaseUser.getUid()).collection("Record")
                .orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Record record = doc.toObject(Record.class);
                                if(record.getId() != 0 && record.getDraft()==true) {
                                    mDraftArrayList.add(record);
                                    Log.d("draftArrayList",record.getRecordType()+"");
                                }
                            }
                            //Switch to home fragment after finish
                            Fragment fragment = new HomeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("draft",mDraftArrayList);
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore.setFirestoreSettings(databaseSetting);

        //set content view and get draft from database
        setContentView(R.layout.activity_dashboard);
        getRecordDraftFromDatabase();

        //navigation selection
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
