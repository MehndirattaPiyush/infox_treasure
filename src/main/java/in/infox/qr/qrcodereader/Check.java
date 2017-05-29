package in.infox.qr.qrcodereader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Check extends AppCompatActivity {
EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ed = (EditText)findViewById(R.id.textView3);

        if(MainActivity.readFromPref(getApplicationContext(),"first_scan","").equals("lol")){
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            finish();
            startActivity(in);
        }


    }
    public void onClick(View view){
        String text = ed.getText().toString();
        if(text.equals("Poize2017")){
            MainActivity.saveToPref(getApplicationContext(),"first_scan","lol");
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            finish();
            startActivity(in);
        }
    }
}
