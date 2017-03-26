package com.teamfinder.app;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.*;


public class Dashboard extends Activity {

    final Random rnd = new Random();

    @Override
    protected void onCreate(
            final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        Button newEvent = (Button) findViewById(R.id.dashboardNewEvent);
        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewEvent.class);
                startActivity(intent);
            }
        });

//        final ImageView img = (ImageView) findViewById(R.id.);
        // I have 3 images named img_0 to img_2, so...
        final String str = "img_" + rnd.nextInt(2);
//        img.setImageDrawable
//                (
//                        getResources().getDrawable(getResourceID(str, "drawable",
//                                getApplicationContext()))
//                );
    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx){
        final int ResourceID = ctx.getResources().getIdentifier(resName, resType,
                ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    ("No resource string found with name " + resName);
        }
        else
        {
            return ResourceID;
        }
    }

    public void displayNewCard(final double distance, final String time, final String event) {
        final Button btn = new Button(this);
        btn.setText(String.valueOf(distance) + time.toString() + event.toString());
    }

}
