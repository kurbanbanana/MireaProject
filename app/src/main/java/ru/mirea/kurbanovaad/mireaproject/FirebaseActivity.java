package ru.mirea.kurbanovaad.mireaproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.kurbanovaad.mireaproject.R;

public class FirebaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FirebaseFragment())
                    .commit();
        }
    }
}