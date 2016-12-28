package su.psn.elegantlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import su.psn.arcelegant.arclayout.ArcLayout;
import su.psn.arcelegant.arclayout.ArcLayoutSettings;

public class MainActivity extends AppCompatActivity {

    private ArcLayout arcLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);


        ArcLayoutSettings settings = arcLayout.getSettings();
        settings.setDirectionBottom(true);
        settings.setIsCropConvex(true);
        arcLayout.setSettings(settings);

//        arcLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Here it is.", Toast.LENGTH_SHORT).show();
//            }
//        });

        findViewById(R.id.bitmapIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Here it is.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
