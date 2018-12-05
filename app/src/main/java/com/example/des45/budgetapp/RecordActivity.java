package com.example.des45.budgetapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    //check for Standard mode
    private boolean isStandard;
    private boolean recordMode;//false for income, true for expense

    //Button variable
    private Button incomeMode;
    private Button expenseMode;
    private RadioGroup radioRecordMode;//for read and save the record.
    private TextView displayRecordMode;//display user the mode user want
    private EditText dateFill;
    private Spinner recordType;
    private Spinner recordCategory;
    private EditText mAmount;
    private EditText mContent;
    private Button mSave;
    private Button mCancel;
    private int databaseID;
    private String mUID;

    //Date variable
    private Calendar myCalendar = Calendar.getInstance();
    private String defaultDateFormat = "dd.MM.yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat,Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Variables
        incomeMode = findViewById(R.id.record_income);
        expenseMode =findViewById(R.id.record_expense);
        displayRecordMode = findViewById(R.id.record_mode);
        radioRecordMode = findViewById(R.id.radio_group_record);
        dateFill = findViewById(R.id.date_fill);
        recordType = findViewById(R.id.type_dropbox);
        recordCategory = findViewById(R.id.category_dropbox);
        mAmount = findViewById(R.id.amount_fill);
        mContent = findViewById(R.id.content_fill);
        mSave = findViewById(R.id.save_button);
        mCancel = findViewById(R.id.cancel_button);
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUID = mFirebaseUser.getUid();

        //set default date
        dateFill.setText(currentDate());

        //set default mode(expenses)
        recordMode = true;
        isStandard = true;
        setValue(1);//expenses
        displayRecordMode.setText(R.string.record_expense);
        expenseMode.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        incomeMode.setBackgroundColor(Color.WHITE);

        //Listener
        recordListener();
        radioRecordModeListener();
        datePickerListener();
        getDatabaseIDFromDatabase();
        //Handle the save button
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check the income
                if(recordMode)
                {
                    setRecord(isStandard);
                }
                else//expenses
                {
                    setRecord(isStandard);
                }

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    //Save the record when user press saved
    public void setRecord(Boolean isStandard)
    {
        double value;
        try {
            //Convert to string
            value = Double.parseDouble(mAmount.getText().toString());

            //Check all of the requirements is fulfilled
            if(value==0)
            {
                Toast.makeText(RecordActivity.this,R.string.toaster_provide_amount,Toast.LENGTH_SHORT).show();
            }

            if(recordMode)//expenses
            {
                setDatabaseValue(isStandard,value);
            }
            else//expenses
            {
                setDatabaseValue(isStandard,value);
            }
        }

        catch (NumberFormatException e)
        {
            Toast.makeText(RecordActivity.this,R.string.toaster_provide_amount,Toast.LENGTH_SHORT).show();
        }

    }

    //set currentDate as default
    public String currentDate ()
    {
        long currentDate = System.currentTimeMillis();
        return sdf.format(currentDate);
    }

    //update the date after pick
    public void updateDate()
    {
        dateFill.setText(sdf.format(myCalendar.getTime()));
    }

    //handle the picker
    public void datePickerListener()
    {
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
                        RecordActivity.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    //Handle radioGroup for standard or quick
    public void radioRecordModeListener()
    {
        radioRecordMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                //Check is standard mode
                switch (id)
                {
                    case R.id.radio_standard_mode:
                        isStandard=true;
                        setValue(3);
                        break;

                    case R.id.radio_quick_mode:
                        setValue(2);
                        isStandard=false;

                }
            }
        });
    }

    //Handle the income and expense button
    public void recordListener()
    {
        //Handle the income/expense
        incomeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            //display income
            public void onClick(View view) {
                recordMode = false;
                displayRecordMode.setText(R.string.record_income);
                //Change type value
                setValue(0);//income
                incomeMode.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));//Change text
                expenseMode.setBackgroundColor(Color.WHITE);

            }
        });
        expenseMode.setOnClickListener(new View.OnClickListener() {
            @Override
            //display income
            public void onClick(View view) {
                recordMode = true;
                displayRecordMode.setText(R.string.record_expense);
                setValue(1);//expenses
                expenseMode.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));//Change text
                incomeMode.setBackgroundColor(Color.WHITE);

            }
        });
    }

    //set value for the spinner
    //0 income
    //1 expenses
    //2 draft without value
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
        else if (mode==2)//reset the array for draft
        {
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.empty_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recordCategory.setAdapter(dataAdapter);
            recordType.setAdapter(dataAdapter);
        }
        else if(mode==3)
        {
            if (recordMode)//expenses
            {
                ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                        R.array.category_expenses_array, android.R.layout.simple_spinner_item);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                recordCategory.setAdapter(dataAdapter);
            }
            else//income
            {
                ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                        R.array.category_income_array, android.R.layout.simple_spinner_item);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                recordCategory.setAdapter(dataAdapter);
            }
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.type_array, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recordType.setAdapter(dataAdapter);
        }

    }

    public void setDatabaseValue(boolean isStandard, double value) {
        try {
            getDatabaseIDFromDatabase();
            int recordID = getDatabaseID();
            recordID++;
            Map<String, Object> record = setValueForDatabase(recordID, recordMode, isStandard, value);
            if(isStandard) {
                mFirestore.collection("users").document(mUID).collection("Record").document("" + recordID)
                        .set(record)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RecordActivity.this, R.string.toaster_saved, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RecordActivity.this, R.string.toaster_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                mFirestore.collection("users").document(mUID).collection("Record").document("" + recordID)
                        .set(record)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RecordActivity.this, R.string.toaster_saved_draft, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RecordActivity.this, R.string.toaster_failed, Toast.LENGTH_SHORT).show();
                            }
                        });

            }
            Map<String, Object> record2 = new HashMap<>();
            record2.put("id", recordID);
            mFirestore.collection("users").document(mUID).set(record2);
            finish();
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(RecordActivity.this,"error on getting recordID",Toast.LENGTH_SHORT).show();
        }
    }

    public Map<String,Object> setValueForDatabase(int recordID, boolean recordMode ,boolean isStandard, double amount)
    {
        Map<String, Object> record = new HashMap<>();
        if(isStandard) {
            record.put("id", recordID);
            record.put("recordType", recordMode);
            record.put("type", recordType.getSelectedItem().toString());
            record.put("category", recordCategory.getSelectedItem().toString());
            record.put("amount", amount);
            record.put("contents", mContent.getText().toString());
            record.put("draft", false);
            record.put("year", myCalendar.get(Calendar.YEAR));
            record.put("month", myCalendar.get(Calendar.MONTH) + 1);
            record.put("day", myCalendar.get(Calendar.DAY_OF_MONTH));
            record.put("hours", myCalendar.get(Calendar.HOUR_OF_DAY));
            record.put("minutes", myCalendar.get(Calendar.MINUTE));
        }
        else
        {
            record.put("id",recordID);
            record.put("recordType", recordMode);
            record.put("amount", amount);
            record.put("contents", mContent.getText().toString());
            record.put("draft", true);
            record.put("year", myCalendar.get(Calendar.YEAR));
            record.put("month", myCalendar.get(Calendar.MONTH)+1);
            record.put("day", myCalendar.get(Calendar.DAY_OF_MONTH));
            record.put("hours", myCalendar.get(Calendar.HOUR_OF_DAY));
            record.put("minutes",myCalendar.get(Calendar.MINUTE));
        }
        return record;
    }

    public int getDatabaseID()
    {
        return databaseID;
    }
    public void setDatabaseID(int value)
    {
        databaseID = value;
    }
    public void getDatabaseIDFromDatabase ()
    {
        DocumentReference documentReference = mFirestore.collection("users").document(mUID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    int value = document.getLong("id").intValue();
                    setDatabaseID(value);
                }
                else
                {
                    setDatabaseID(0);
                }
            }
        });
    }
}

