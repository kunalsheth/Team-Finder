package com.teamfinder.app;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class UserProfile extends Activity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
    }

    public void setAll(final int ages, final long phonenum, final String emails, final String names, final com.teamfinder.app.utils.Activity first, final com.teamfinder.app.utils.Activity second, final com.teamfinder.app.utils.Activity third) {
        EditText choice1 = (EditText) findViewById(R.id.UserProfileChoice1);
        EditText choice2 = (EditText) findViewById(R.id.UserProfileChoice2);
        EditText choice3 = (EditText) findViewById(R.id.UserProfileChoice3);
        EditText name = (EditText) findViewById(R.id.userProfileName);
        EditText email = (EditText) findViewById(R.id.UserProfileEmail);
        EditText phone = (EditText) findViewById(R.id.UserProfilePhone);
        EditText age = (EditText) findViewById(R.id.UserProfileAge);

        choice1.setText(first.toString());
        choice2.setText(second.toString());
        choice3.setText(third.toString());
        name.setText(names.toString());
        email.setText(emails.toString());
        phone.setText(String.valueOf(phonenum));
        age.setText(String.valueOf(ages));
    }

}
