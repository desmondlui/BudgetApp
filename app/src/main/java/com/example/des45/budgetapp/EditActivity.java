package com.example.des45.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    //Button variable
    private EditText dateFill;
    private Spinner recordType;
    private Spinner recordCategory;
    private EditText mAmount;
    private EditText mContent;
    private Button mSave;
    private Button mCancel;
    private String mUID;

    //variable for the edit from previous
    private int mPreID;
    private boolean mRecordMode;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHours;
    private int mMinutes;
    private double mPreAmount;
    private String mPreContent;

    //Date variable
    private Calendar myCalendar = Calendar.getInstance();
    private String defaultDateFormat = "dd.MM.yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat,Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the value
        Intent intent = getIntent();
        mPreID = intent.getIntExtra("editid",0);
        mRecordMode = intent.getBooleanExtra("editrecordmode",true);
        mYear = intent.getIntExtra("edityear",2018);
        mMonth = intent.getIntExtra("editmonth",1);
        mDay = intent.getIntExtra("editday",1);
        mHours = intent.getIntExtra("edithours",12);
        mMinutes = intent.getIntExtra("editminutes",34);
        mPreAmount = intent.getDoubleExtra("editamount",0.0);
        mPreContent = intent.getStringExtra("editcontent");

        //set content view
        setContentView(R.layout.activity_edit);
        recordType = findViewById(R.id.edit_type_dropbox);
        recordCategory = findViewById(R.id.edit_category_dropbox);
        mAmount = findViewById(R.id.edit_amount_fill);
        mContent = findViewById(R.id.edit_content_fill);
        mSave = findViewById(R.id.edit_save_button);
        mCancel = findViewById(R.id.edit_cancel_button);
        dateFill = findViewById(R.id.edit_date_fill);
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUID = mFirebaseUser.getUid();

        //set date from previous data
        myCalendar.set(mYear,mMonth-1,mDay,mHours,mMinutes);
        dateFill.setText(sdf.format(myCalendar.getTime()));
        mContent.setText(mPreContent);
        mAmount.setText(mPreAmount+"");

        //set the Spinner value
        if(mRecordMode)//expenses
        {
            setValue(1);
        }
        else
        {
            setValue(0);
        }
        datePickerListener();
        buttonListener();
    }
    private void buttonListener()
    {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecord();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //update the date after pick
    public void updateDate()
    {
        dateFill.setText(sdf.format(myCalendar.getTime()));
    }

    //handle the picker
    public void datePickerListener() {
        final DatePickerDialog.OnDateSetListener date;
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateDate();
            }
        };
        dateFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(
                        EditActivity.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    //Save the record when user press saved
    public void setRecord()
    {
        double value;
        try {
            //Convert to string
            value = Double.parseDouble(mAmount.getText().toString());

            //Check all of the requirements is fulfilled
            if(value==0)
            {
                Toast.makeText(EditActivity.this,R.string.toaster_provide_amount,Toast.LENGTH_SHORT).show();
            }

            setDatabaseValue(value);
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(EditActivity.this,R.string.toaster_provide_amount,Toast.LENGTH_SHORT).show();
        }
    }

    public void setDatabaseValue(double value) {
        try {
            if (mRecordMode)//expenses
            {
                Map<String,Object> record = setValueForDatabase(value);
                mFirestore.collection("users").document(mUID).collection("Record").document(""+mPreID)
                        .set(record)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditActivity.this, R.string.toaster_saved, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditActivity.this, R.string.toaster_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
            else//income
            {
                //record all the value
                Map<String,Object> record = setValueForDatabase(value);
                mFirestore.collection("users").document(mUID).collection("Record").document(""+mPreID)
                        .set(record)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditActivity.this, R.string.toaster_saved, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditActivity.this, R.string.toaster_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            finish();
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(EditActivity.this,"error on getting recordID",Toast.LENGTH_SHORT).show();
        }
    }

    public Map<String,Object> setValueForDatabase(double amount)
    {
        Map<String, Object> record = new HashMap<>();
        record.put("id", mPreID);
        record.put("recordType", mRecordMode);
        record.put("type", recordType.getSelectedItem().toString());
        record.put("category", recordCategory.getSelectedItem().toString());
        record.put("amount", amount);
        record.put("contents", mContent.getText().toString());
        record.put("draft", false);
        record.put("pictureid", 0);
        record.put("year", myCalendar.get(Calendar.YEAR));
        record.put("month", myCalendar.get(Calendar.MONTH) + 1);
        record.put("day", myCalendar.get(Calendar.DAY_OF_MONTH));
        record.put("hours", myCalendar.get(Calendar.HOUR_OF_DAY));
        record.put("minutes", myCalendar.get(Calendar.MINUTE));
        return record;
    }

    //set value for the spinner
    //0 income
    //1 expenses
    public void setValue(int mode)
    {
        if(mode==0)//income
        {
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.category_income_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<CharSequence> dataAdapter2 = ArrayAdapter.createFromResource(this,
                    R.array.type_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            recordCategory.setAdapter(dataAdapter);
            recordType.setAdapter(dataAdapter2);

        }
        //expenses
        else if (mode==1){
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.category_expenses_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<CharSequence> dataAdapter2 = ArrayAdapter.createFromResource(this,
                    R.array.type_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recordCategory.setAdapter(dataAdapter);
            recordType.setAdapter(dataAdapter2);
        }
    }
}
