package com.groupx.simplenote.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
import com.groupx.simplenote.R;
import com.groupx.simplenote.dao.AccountDao;
import com.groupx.simplenote.database.NoteDatabase;
import com.groupx.simplenote.entity.Account;
import com.groupx.simplenote.entity.Note;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etMail, etConfirmPw, etAvatar, etFullName;
    //private EditText etDob;
    private Button btnRegister;
    private TextView tv_Login;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.edtUsername);
        etPassword = findViewById(R.id.edtPassword);
        etMail = findViewById(R.id.edtEmail);
        etConfirmPw = findViewById(R.id.edtConfirmPassword);

        //etAvatar = findViewById(R.id.etAvatar);
        //etFullName = findViewById(R.id.etFullName);
        //etDob = findViewById(R.id.etDob);
        btnRegister = findViewById(R.id.btnSignUp);
        //btnLogin = findViewById(R.id.btnLogin);
        tv_Login = findViewById(R.id.tvSignIn);

        /*
         * Date picker
         * */
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

//        etDob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        month = month + 1;
//                        String date = year + "-" + month + "-" + day;
//                        etDob.setText(date);
//                    }
//                }, year, month, day);
//                dialog.show();
//            }
//        });

        /*
         * Register
         * */

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account accountEntity = new Account();
                accountEntity.setUsername(etUserName.getText().toString());
                accountEntity.setPassword(etPassword.getText().toString());
                accountEntity.setAvatarImagePath(etAvatar.getText().toString());
//                try {
//                    accountEntity.setDob(format.parse(etDob.getText().toString()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                accountEntity.setFullName(etFullName.getText().toString());
                if(validateInput(accountEntity)){
                    /*final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(etUserName.getText().toString(), etPassword.getText().toString()).addOnCompleteListener((task) -> {
                       if(task.isSuccessful()){
                           firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email id", Toast.LENGTH_SHORT).show();
                                   }else{
                                       Toast.makeText(RegisterActivity.this, "can not send", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                       }else{
                           Toast.makeText(RegisterActivity.this, "can not create", Toast.LENGTH_SHORT).show();
                       }
                    });*/

                    //insert to database
                    NoteDatabase noteDatabase = NoteDatabase.getSNoteDatabase(getApplicationContext());
                    AccountDao accountDao = noteDatabase.accountDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            accountDao.registerAccount(accountEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private Boolean validateInput(Account accountEntity){
        if(accountEntity.getUsername().isEmpty()
                || accountEntity.getPassword().isEmpty()
                || accountEntity.getAvatarImagePath().isEmpty()
                || accountEntity.getDob() ==  null
                || accountEntity.getFullName().isEmpty()){
            return false;
        }
        return true;
    }


}










