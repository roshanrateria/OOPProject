package com.example.my1stapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsernameField, loginPasswordField;
    private Button loginSubmitButton;

    private static final String USERS_FILE = "users.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginUsernameField = findViewById(R.id.loginUsernameField);
        loginPasswordField = findViewById(R.id.loginPasswordField);
        loginSubmitButton = findViewById(R.id.loginSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to perform login
                performLogin();
            }
        });
    }

    private void performLogin() {
        // Get the input values from EditText fields
        String username = loginUsernameField.getText().toString().trim();
        String password = loginPasswordField.getText().toString().trim();

        // Check if username and password match any user in the database
        if (isValidUser(username, password)) {
            // If valid, show a toast message indicating successful login
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Proceed to home screen or any other activity
            // For example:
             Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // If not valid, show a toast message indicating invalid credentials
            Toast.makeText(LoginActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
        }

        // Clear EditText fields after login attempt
        loginUsernameField.setText("");
        loginPasswordField.setText("");
    }

    private boolean isValidUser(String username, String password) {
        File file = new File(getFilesDir(), USERS_FILE);
        if (!file.exists()) {
            return false; // File doesn't exist, no users
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // Found matching username and password
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No matching username and password found
    }
}

