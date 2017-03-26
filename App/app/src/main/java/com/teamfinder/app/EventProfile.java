package com.teamfinder.app;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EventProfile extends Activity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventprofile);
    }

    public void setEvent(final String eventname, final double latitude, final double longitude, String[] names){
        EditText event = (EditText) findViewById(R.id.eventProfileEvent);
        Button address = (Button) findViewById(R.id.eventProfileAddress);
        address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.google.com/maps/preview/@"+latitude+","+longitude+",8z");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        LinearLayout name = (LinearLayout) findViewById(R.id.eventProfileAttendeesContainer);
        event.setText(eventname.toString());

        for(int i=0;i<names.length; i++) {
           Button button = new Button(this);
           button.setText(names[i].toString());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                    startActivity(intent);
                }
            });
        }
    }

}
