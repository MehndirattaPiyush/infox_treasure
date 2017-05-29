package in.infox.qr.qrcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Instruction extends AppCompatActivity {

    int click = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
    }

    public void onClick(View v){
        if(v.getId()==R.id.intt)
        {
           String cli = MainActivity.readFromPref(getApplicationContext(),"click","0");

            int cl = Integer.parseInt(cli);

            cl++;
            Log.d("asdfghj", "onClick: "+cl);
            if(cl>=7){
                MainActivity.saveToPref(getApplicationContext(),"admin","1");
                Toast.makeText(getApplicationContext(),"Admin mode",Toast.LENGTH_LONG).show();

                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
            MainActivity.saveToPref(getApplicationContext(),"click",""+cl);
        }
    }

}
