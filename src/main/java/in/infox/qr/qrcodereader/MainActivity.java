package in.infox.qr.qrcodereader;

import android.Manifest;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.database.Cursor;

import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    ListView listView;
    private List<Hint> hints = new ArrayList<Hint>();
    private ZXingScannerView mScannerView;
    Button scanbtn;
    Button skipbtn;
    String scannedKey = "";
    String set = "";
    String output;
    String TAG = "asdf";
    String last_key = "";
    int total ;
    String to_be_scan;
    String total_points = "";
    TextView tv;
    TextView tv4;
    String wrong_attempemts ;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String  PREF_FILE = "file";

    private String databaseStored = "false";
    private ProgressBar progressBar;
    private Typeface tf;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        tv =(TextView)findViewById(R.id.textView);
        scanbtn=(Button)findViewById(R.id.btnScan);
        skipbtn=(Button)findViewById(R.id.btnSkip);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        tv2 = (TextView)findViewById(R.id.textView2);

        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());

        if(readFromPref(getApplicationContext(),"admin","0").equals("0")){
            tv.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
        } else if(readFromPref(getApplicationContext(),"admin","0").equals("1"))
        {
            tv.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
        }

        if(databaseStored == "false"){

            try {
                dbAdapter.open(); //First time Db
            } catch (SQLException e) {
                e.printStackTrace();
            }

            databaseStored= "true";

            saveToPref(getApplicationContext(),"DataBaseStored",databaseStored);

            //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            finish();
            //startActivity(intent);
        }

        String first = readFromPref(getApplicationContext(),"first_scan","false");
        String first_riddle = readFromPref(getApplicationContext(),"first_riddle","true");

        if(first.equals("true")){
            Intent in = new Intent(getApplicationContext(),Check.class);
            finish();
            startActivity(in);
        }

        if(first_riddle.equals("true")){
            saveToPref(getApplicationContext(), "last_key", "A");

            try {
                dbAdapter.openSetA();
                saveToPref(getApplicationContext(),"to_be_scan","one");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            output = dbAdapter.getHint("A", "A");
            saveToPref(getApplicationContext(), "set", "A"); //uncrypt
            //           key_to_scan();

            Log.d(TAG, "onCreate: "+output);

            dbAdapter.updateHint("A", output);

            Cursor data = dbAdapter.executeQuery();
            if(data.moveToNext()){
                do{
                    String hint = data.getString(1);
                    //String set = data.getString(2);
                    //saveToPref(getApplicationContext(),"set",set);
                    populateHint(hint);
                }while (data.moveToNext());

                populateListView();
            }
            saveToPref(getApplicationContext(),"first_riddle","lol");

        }

        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(10000, 1000);
        myCountDownTimer.start();
        
        wrong_attempemts= readFromPref(getApplicationContext(),"invalid_attempts","0");
        tv4 = (TextView)findViewById(R.id.textView4);
        if(wrong_attempemts.equals("3")){
            tv4.setVisibility(View.VISIBLE);
            scanbtn.setVisibility(View.GONE);
            skipbtn.setVisibility(View.GONE);

        }

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Quicksand-Regular.otf");

        tv2.setTypeface(tf);
        tv.setTypeface(tf);

        System.currentTimeMillis();

        tv.setText(readFromPref(getApplicationContext(),"last_scan","00:00:00"));



        set = readFromPref(getApplicationContext(),"set","E");

        last_key = readFromPref(getApplicationContext(),"last_key","");

        to_be_scan = readFromPref(getApplicationContext(),"to_be_scan","");
        if(to_be_scan.equals("DONE")){
//            scanbtn.setVisibility(View.GONE);
//            skipbtn.setVisibility(View.GONE);
        }

        Log.d(TAG, "onCreate: set : "+set +" key : "+last_key +" to_be scan :"+to_be_scan);

        databaseStored = readFromPref(getApplicationContext(), "DataBaseStored", "false");

//        DBAdapter dbAdapter = new DBAdapter(this);
        Cursor data = dbAdapter.executeQuery(); //TABLE FOUND
        if(data.moveToNext()){
            do{
               String hint = data.getString(1);
                populateHint(hint);
            }while (data.moveToNext());
            populateListView();
        }




        if(set.equals("0")) {
            skipbtn.setVisibility(View.INVISIBLE);
        }

       // populateListView();

        }


    public void  showData(String buffer){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void handleResult(Result result) {

        /* INCREASE TOTAL SCORE IF CODE IS SCANNED
            INCREMENT EQUALS TO THE MARKS OF PREV KEY
        */


        DBAdapter dbAdapter = new DBAdapter(this);

        scannedKey = result.getText();


        if(set.equals("0")) { //INNITIAL SCAN
            output = dbAdapter.getHint(scannedKey, set);
            saveToPref(getApplicationContext(), "set", scannedKey); //uncrypt
 //           key_to_scan();

            dbAdapter.updateHint(scannedKey, output);
            skipbtn.setVisibility(View.VISIBLE);
            switch (scannedKey) {
                case "A":
                    saveToPref(getApplicationContext(), "last_key", "A");
                    try {
                        dbAdapter.openSetA();
                        saveToPref(getApplicationContext(),"to_be_scan","one");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "B":
                    saveToPref(getApplicationContext(), "last_key", "B");
                    try {
                        dbAdapter.openSetB();
                        saveToPref(getApplicationContext(),"to_be_scan","1");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "C":
                    saveToPref(getApplicationContext(), "last_key", "C");
                    try {
                        dbAdapter.openSetC();
                        saveToPref(getApplicationContext(),"to_be_scan","3");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "D":
                    saveToPref(getApplicationContext(), "last_key", "D");
                    try {
                        dbAdapter.openSetD();
                        saveToPref(getApplicationContext(),"to_be_scan","4");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            Intent in = new Intent(getApplicationContext(),Check.class);
            saveToPref(getApplicationContext(),"first_scan","true");
            finish();
            startActivity(in);

        } else {
            if(scannedKey.equals(to_be_scan)){
                //NEXT HINT
                output = dbAdapter.getHint(scannedKey,set);
                dbAdapter.updateHint(scannedKey,output);

//                String point = dbAdapter.getHintPoint(last_key,set);
                to_be_scan = dbAdapter.getToBeScanned(scannedKey,set);
                saveToPref(getApplicationContext(), "last_key", scannedKey);

                String pre_point = readFromPref(getApplicationContext(),"total_point","0");

                int pre_total=Integer.parseInt(pre_point);
//                total = Integer.parseInt(point);
//                total = total+pre_total;
//                saveToPref(getApplicationContext(),"total_point",total+"");
                saveToPref(getApplicationContext(), "to_be_scan",to_be_scan);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                String test = sdf.format(cal.getTime());
                Log.d("TEST", test);
                saveToPref(getApplicationContext(), "last_scan",test);
                tv.setText(test);


                showData(output);

            } else{
                //WRONG CODE
                int at = Integer.parseInt(wrong_attempemts);
                at++;
                saveToPref(getApplicationContext(),"invalid_attempts",at+"");
                Toast.makeText(getApplicationContext(),"INVALID QR CODE",Toast.LENGTH_SHORT).show();
                showData(output);
            }
        }
        Cursor data = dbAdapter.executeQuery();
        if(data.moveToNext()){
            do{
                String hint = data.getString(1);
                //String set = data.getString(2);
                //saveToPref(getApplicationContext(),"set",set);
                populateHint(hint);
            }while (data.moveToNext());
            populateListView();
        }

      setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String test = sdf.format(cal.getTime());
        Log.d("TEST", test);
        tv.setText(test);
    }


    public void onClick(View v){
        if(v.getId()==R.id.btnScan){
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
        else if (v.getId()==R.id.btnSkip){
            // SHOW NEXT CODE
            DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
            output = dbAdapter.getHint(to_be_scan,set);
            dbAdapter.updateHint(to_be_scan,output);
            to_be_scan = dbAdapter.getToBeScanned(to_be_scan,set);
            saveToPref(getApplicationContext(),"to_be_scan",to_be_scan);
            showData(output);
        } else if(v.getId() == R.id.btnInstruction){
            Intent in = new Intent(getApplicationContext(),Instruction.class);
            startActivity(in);
        } else if(v.getId() == R.id.btnPerform){
            Intent in = new Intent(getApplicationContext(),Perform.class);
            startActivity(in);
        }

    }

    @Override
    public void onBackPressed() {


        if(mScannerView.VISIBLE==1){
            mScannerView.setVisibility(View.INVISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // setContentView(R.layout.activity_main);

        try {
            populateListView();
            mScannerView.stopCamera();
           // setContentView(R.layout.activity_main);

        }catch (Exception e){
           // onBackPressed();
          //  setContentView(R.layout.activity_main);
        }
    }

    private void populateListView() {
        ArrayAdapter<Hint> adapter = new MyListAdapter();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String test = sdf.format(cal.getTime());
        Log.e("TEST", test);

//        tv.setText(test);

        listView.setAdapter(adapter);
    }

    private void populateHint( String hint)
    {
        hints.add(new Hint(hint));
    }
    private class MyListAdapter extends ArrayAdapter<Hint>{

        private MyListAdapter() {
            super(MainActivity.this, R.layout.item_view, hints);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.item_view,parent,false);
            }
            Hint currentHint = hints.get(position);

            TextView nm = (TextView)itemView.findViewById(R.id.name);
            nm.setText(Html.fromHtml(currentHint.getHint()));
            nm.setTypeface(tf);

            return itemView;
        }
    }

    public static void saveToPref(Context context , String prefName ,String prefValue){

        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName,prefValue);
        editor.apply();
    }
    public static String readFromPref(Context context , String prefName ,String defaultValue){

        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
       return sharedPref.getString(prefName,defaultValue);
    }



    public void key_to_scan(){
        Log.d(TAG, "key_to_scan: ");
        switch (set){
            case "A" :
                Log.d(TAG, "key_to_scan: A");
                switch (to_be_scan) {
                    case "" :
                        saveToPref(getApplicationContext(),"to_be_scan","4");
                        Log.d(TAG, "key_to_scan: 4");
                        break;
                    case "4" :
                        saveToPref(getApplicationContext(),"to_be_scan","2");
                        break;
                    case "2" :
                        saveToPref(getApplicationContext(),"to_be_scan","3");
                        break;
                    case "3" :
                        saveToPref(getApplicationContext(),"to_be_scan","1");
                        break;
                    case "1" :
                        saveToPref(getApplicationContext(),"to_be_scan","DONE");
                        break;
                }
                break;
            case "B" :
                switch (to_be_scan) {
                    case "" :
                        saveToPref(getApplicationContext(),"to_be_scan","1");
                        break;
                    case "1" :
                        saveToPref(getApplicationContext(),"to_be_scan","3");
                        break;
                    case "3" :
                        saveToPref(getApplicationContext(),"to_be_scan","4");
                        break;
                    case "4" :
                        saveToPref(getApplicationContext(),"to_be_scan","2");
                        break;
                    case "2" :
                        saveToPref(getApplicationContext(),"to_be_scan","DONE");
                        break;
                }
                break;
            case "C" :
                switch (to_be_scan) {
                    case "" :
                        saveToPref(getApplicationContext(),"to_be_scan","3");
                        break;
                    case "3" :
                        saveToPref(getApplicationContext(),"to_be_scan","2");
                        break;
                    case "2" :
                        saveToPref(getApplicationContext(),"to_be_scan","4");
                        break;
                    case "4" :
                        saveToPref(getApplicationContext(),"to_be_scan","1");
                        break;
                    case "1" :
                        saveToPref(getApplicationContext(),"to_be_scan","DONE");
                        break;
                }
                break;
            case "D" :
                switch (to_be_scan) {
                    case "" :
                        saveToPref(getApplicationContext(),"to_be_scan","4");
                        break;
                    case "4" :
                        saveToPref(getApplicationContext(),"to_be_scan","1");
                        break;
                    case "1" :
                        saveToPref(getApplicationContext(),"to_be_scan","2");
                        break;
                    case "2" :
                        saveToPref(getApplicationContext(),"to_be_scan","3");
                        break;
                    case "3" :
                        saveToPref(getApplicationContext(),"to_be_scan","DONE");
                        break;
                }
                break;
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);

            progressBar.setProgress(progressBar.getMax()-progress);
        }

        @Override
        public void onFinish() {
            //finish();
        }
    }
}



