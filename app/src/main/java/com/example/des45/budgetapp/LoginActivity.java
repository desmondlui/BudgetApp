package com.example.des45.budgetapp;

import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mFirebaseAuth;
    private Button mRegister;
    private Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mRegister = findViewById(R.id.sign_up_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = mEmailView.getText().toString().trim();
                    String password = mPasswordView.getText().toString().trim();
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch(Exception e)
                {
                    Toast.makeText(getBaseContext(),"Error on sign in ",Toast.LENGTH_SHORT).show();
                }
            }

        });

        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = mEmailView.getText().toString().trim();
                    String password = mPasswordView.getText().toString().trim();
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getBaseContext(), "Register Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                                Map<String, Object> record = new HashMap<>();
                                record.put("id", 0);
                                mFirestore.collection("users").document(mFirebaseAuth.getUid()).set(record);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getBaseContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(),"Error on sign up ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

