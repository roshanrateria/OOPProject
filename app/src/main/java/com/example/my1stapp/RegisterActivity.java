package com.example.my1stapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsernameField, regPasswordField, regLocationField, regSkillsField, regContactNumberField, regEmailField;
    private Button registerSubmitButton;

    private static final String USERS_FILE = "users.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        regUsernameField = findViewById(R.id.regUsernameField);
        regPasswordField = findViewById(R.id.regPasswordField);
        regLocationField = findViewById(R.id.regLocationField);
        regSkillsField = findViewById(R.id.regSkillsField);
        regContactNumberField = findViewById(R.id.regContactNumberField);
        regEmailField = findViewById(R.id.regEmailField);
        registerSubmitButton = findViewById(R.id.registerSubmitButton);

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to perform registration
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        // Get the input values from EditText fields
        String username = regUsernameField.getText().toString().trim();
        String password = regPasswordField.getText().toString().trim();
        String location = regLocationField.getText().toString().trim();
        String skills = regSkillsField.getText().toString().trim();
        String contactNumber = regContactNumberField.getText().toString().trim();
        String email = regEmailField.getText().toString().trim();

        // Perform validation checks
        if (username.isEmpty() || password.isEmpty() || location.isEmpty() || skills.isEmpty() || contactNumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate username
        if (isUsernameDuplicate(username)) {
            Toast.makeText(RegisterActivity.this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user data to database (text file)
        saveUserData(username, password, location, skills, contactNumber, email);

        // Display registration success message
        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

        // Clear EditText fields after successful registration
        regUsernameField.setText("");
        regPasswordField.setText("");
        regLocationField.setText("");
        regSkillsField.setText("");
        regContactNumberField.setText("");
        regEmailField.setText("");

        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);    }

    private boolean isUsernameDuplicate(String username) {
        File file = new File(getFilesDir(), USERS_FILE);
        if (!file.exists()) {
            return false; // File doesn't exist, no duplicate usernames
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true; // Found duplicate username
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No duplicate username found
    }

    private void saveUserData(String username, String password, String location, String skills, String contactNumber, String email) {
        File file = new File(getFilesDir(), USERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + "," + password + "," + location + "," + skills + "," + contactNumber + "," + email + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
