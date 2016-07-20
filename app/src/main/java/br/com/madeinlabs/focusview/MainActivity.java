package br.com.madeinlabs.focusview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgUpload = (ImageView) findViewById(R.id.img_upload);

        FocusView focusView = (FocusView) findViewById(R.id.focusView);
        focusView.setCircleAncor(imgUpload);

        focusView.addLayoutBelow(R.layout.layout_focus_below);
        focusView.addLayoutRight(R.layout.layout_focus_below);
    }
}
