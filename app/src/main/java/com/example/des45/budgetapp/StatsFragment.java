package com.example.des45.budgetapp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class StatsFragment extends Fragment {


    private Spinner mYear;
    private Spinner mMonth;
    private Spinner mType;
    private Button mButtonList;
    private Button mButtonPie;
    private Button mButtonIncome;
    private Button mButtonExpense;
    private boolean isPie;
    private boolean recordMode;//true for expense, false for income

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace) {

        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        mYear = v.findViewById(R.id.type_year);
        mMonth = v.findViewById(R.id.type_month);
        mButtonList = v.findViewById(R.id.button_stats_list);
        mButtonPie = v.findViewById(R.id.button_stats_pie);
        mButtonExpense = v.findViewById(R.id.fragment_stats_expense);
        mButtonIncome = v.findViewById(R.id.fragment_stats_income);

        //init set
        isPie=false;
        recordMode=true;
        mButtonExpense.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        mButtonIncome.setBackgroundColor(Color.WHITE);
        ArrayList<Record> mRecordArrayList;
        Bundle bundle = getArguments();
        mRecordArrayList = bundle.getParcelableArrayList("databaseList");
        buttonListener(mRecordArrayList);
        return v;
    }

    //listen to button and spinner
    private void buttonListener(final ArrayList<Record> mRecordArrayList)
    {

        mButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPie = false;
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPie",isPie);
                bundle.putString("year",mYear.getSelectedItem().toString());
                bundle.putString("month",mMonth.getSelectedItem().toString());
                bundle.putBoolean("recordMode",recordMode);
                bundle.putParcelableArrayList("databaseList",mRecordArrayList);
                sendDataToFragments(bundle,new StatsDisplay(),R.id.display_list_or_pie);
                mButtonList.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));//Change text
                mButtonPie.setBackgroundColor(Color.WHITE);

            }
        });
        mButtonPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPie = true;
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPie",isPie);
                bundle.putString("year",mYear.getSelectedItem().toString());
                bundle.putString("month",mMonth.getSelectedItem().toString());
                bundle.putBoolean("recordMode",recordMode);
                bundle.putParcelableArrayList("databaseList",mRecordArrayList);
                sendDataToFragments(bundle,new StatsDisplay(),R.id.display_list_or_pie);
                mButtonPie.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));//Change text
                mButtonList.setBackgroundColor(Color.WHITE);
            }
        });

        mButtonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordMode = true;
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPie",isPie);
                bundle.putString("year",mYear.getSelectedItem().toString());
                bundle.putString("month",mMonth.getSelectedItem().toString());
                bundle.putBoolean("recordMode",recordMode);
                bundle.putParcelableArrayList("databaseList",mRecordArrayList);
                sendDataToFragments(bundle,new StatsDisplay(),R.id.display_list_or_pie);
                mButtonExpense.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));//Change text
                mButtonIncome.setBackgroundColor(Color.WHITE);
            }
        });

        mButtonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordMode = false;
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPie",isPie);
                bundle.putString("year",mYear.getSelectedItem().toString());
                bundle.putString("month",mMonth.getSelectedItem().toString());
                bundle.putParcelableArrayList("databaseList",mRecordArrayList);
                bundle.putBoolean("recordMode",recordMode);
                sendDataToFragments(bundle,new StatsDisplay(),R.id.display_list_or_pie);
                mButtonIncome.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));//Change text
                mButtonExpense.setBackgroundColor(Color.WHITE);
            }
        });

    }

    //send data to fragment respectively
    private void sendDataToFragments(Bundle value, Fragment target,int viewID)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        target.setArguments(value);
        fragmentTransaction.replace(viewID,target);
        fragmentTransaction.commit();
    }
}