package com.example.my1stapp;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.AlertDialog;
class User {
    private String username;
    private String location;
    private String skills,phone,mail;

    public User(String username, String location, String skills,String phone,String mail) {
        this.username = username;
        this.location = location;
        this.skills = skills;
        this.phone=phone;
        this.mail=mail;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getSkills() {
        return skills;
    }
    public String getphone() {
        return phone;
    }
    public String getemail() {
        return mail;
    }

    @Override
    public String toString() {
        return username; // Return username for displaying in the ListView
    }
}



public class HomeActivity extends AppCompatActivity {

    private Spinner locationFilterSpinner, skillsFilterSpinner;
    private ListView userListView;

    private static final String USERS_FILE = "users.txt";

    // List to store user data
    private List<User> userList = new ArrayList<>();
    // Adapter for user list view
    private ArrayAdapter<User> userAdapter;

    // Set to store unique locations and skills
    private Set<String> locationSet = new HashSet<>();
    private Set<String> skillsSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        // Initialize UI components
        locationFilterSpinner = findViewById(R.id.locationFilterSpinner);
        skillsFilterSpinner = findViewById(R.id.skillsFilterSpinner);
        userListView = findViewById(R.id.userListView);

        // Set up filter spinners
        setupFilterSpinners();

        // Read user data from file and populate user list view
        readUsersFromFile();
    }

    private void setupFilterSpinners() {
        // Populate location and skills sets from user data
        readUsersFromFile();
        for (User user : userList) {
            locationSet.add(user.getLocation());
            skillsSet.add(user.getSkills());
        }

        // Convert sets to lists for spinner adapters
        List<String> locationList = new ArrayList<>(locationSet);
        List<String> skillsList = new ArrayList<>(skillsSet);

        // Add "All" option at the beginning of each list
        locationList.add(0, "All");
        skillsList.add(0, "All");

        // Set up adapters for spinners
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationFilterSpinner.setAdapter(locationAdapter);

        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, skillsList);
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skillsFilterSpinner.setAdapter(skillsAdapter);

        // Implement spinner item click listeners to apply filters
        locationFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(locationFilterSpinner.getSelectedItem().toString(), skillsFilterSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        skillsFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(locationFilterSpinner.getSelectedItem().toString(), skillsFilterSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void readUsersFromFile() {
        File file = new File(getFilesDir(), USERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Assuming each user entry has at least 6 parts
                    String username = parts[0];
                    String phone=parts[5];
                    String location = parts[2];
                    String skills = parts[3];
                    String mail=parts[4];
                    // Add user to list
                    userList.add(new User(username, location, skills,phone,mail));
                }
            }

            // Set up user list view after data is loaded
            setupUserListView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupUserListView() {
        // Create adapter for user list view
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(userAdapter);

        // Implement list item click listener for user details
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Display user details in a dialog
                User selectedUser = userList.get(position);
                showUserDetailsDialog(selectedUser);
            }
        });
    }

    private void applyFilters(String location, String skills) {
        // Clear user list before applying filters
        userList.clear();

        // Read users from file and add filtered users to the list
        File file = new File(getFilesDir(), USERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Assuming each user entry has at least 6 parts
                    String username = parts[0];
                    String phone=parts[5];
                    String mail=parts[4];
                    String userLocation = parts[2];
                    String userSkills = parts[3];
                    // Check if user matches filter criteria
                    if (("All".equals(location) || location.equals(userLocation)) &&
                            ("All".equals(skills) || skills.equals(userSkills))) {
                        // Add user to list
                        userList.add(new User(username, userLocation, userSkills,phone,mail));
                    }
                }
            }
            // Notify adapter that data set has changed
            userAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void showUserDetailsDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Details");
        builder.setMessage("Username: " + user.getUsername() + "\n" +
                "Location: " + user.getLocation() + "\n" +
                "Skills: " + user.getSkills()
                + "\n" +
                "Email: " + user.getphone()
                + "\n" +
                "Contact Number: " + user.getemail());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
