package com.deneme.oguz.sanaldovizbrosu;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static float edit;
    int a=0;
    TextView dolarT,euroT,poundT,baslikT,tryT;
    Button guncelleB;
    EditText editT;
    RadioButton radio_TRY,radio_USD,radio_EUR,radio_GBP;
    RadioGroup radiogroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dolarT=findViewById(R.id.dolarT);
        euroT=findViewById(R.id.euroT);
        poundT=findViewById(R.id.poundT);
        guncelleB=findViewById(R.id.guncelleB);
        baslikT=findViewById(R.id.baslikT);
        tryT=findViewById(R.id.tryT);
        editT=findViewById(R.id.editT);
        radio_EUR=findViewById(R.id.radio_EUR);
        radio_GBP=findViewById(R.id.radio_GBP);
        radio_TRY=findViewById(R.id.radio_TRY);
        radio_USD=findViewById(R.id.radio_USD);
        radiogroup=findViewById(R.id.radiogroup);

        guncelleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Innerclass().execute("https://api.exchangeratesapi.io/latest?base=TRY");
                if(radio_EUR.isChecked()){                       //
                    a = 1;                                       //
                }else if(radio_GBP.isChecked()){                 //
                    a=2;                                         //  HANGİ RADİOBUTTON
                }else if(radio_TRY.isChecked()){                 //  SEÇİLİ DİYE BAKIYORUZ
                    a=0;                                         //  ONA GÖRE a DEĞERİMİZİ DEĞİŞTİRİYORUZ
                }else if(radio_USD.isChecked()){                 //
                    a=3;                                         //
                }
                String temp = editT.getText().toString();        //  EDITTEXT'E GİRİLEN DEĞERİ
                edit = Float.valueOf(temp);                      //  FLOAT'A ÇEVİRİP SAKLIYORUZ
            }
        });
    }


    class Innerclass extends AsyncTask<String,Void,String> {
        protected String doInBackground(String ... params){
            HttpURLConnection connection;
            BufferedReader br;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is=connection.getInputStream();
                br=new BufferedReader(new InputStreamReader(is));

                String satir,result="";

                while((satir=br.readLine())!=null){
                    result += satir;
                }
                return result;

            }catch(Exception e){
                euroT.setText("BAS");
                return "Hata";
            }
        }

        protected void onPostExecute(String s){

            super.onPostExecute(s);
            try{
                JSONObject json = new JSONObject(s);
                String asd = json.getString("rates");
                JSONObject json2=new JSONObject(asd);

                String us= json2.getString("USD");
                String eu = json2.getString("EUR");
                String gb=json2.getString("GBP");
                String tr=json2.getString("TRY");

                float usf=Float.valueOf(us);
                float euf=Float.valueOf(eu);
                float tryf=Float.valueOf(tr);
                float gbpf=Float.valueOf(gb);

                if(a==1){                                    //
                    usf = (edit*usf)/euf;                    //
                    gbpf = (edit*gbpf)/euf;                  //
                    tryf = (edit*tryf)/euf;                  //
                    euf=edit;                                //
                }else if(a==2){                              //
                    usf = (edit*usf)/gbpf;                   //
                    euf = (edit*euf)/gbpf;                   //  BU SATIRLAR
                    tryf = (edit*tryf)/gbpf;                 //  EDITTEXT'E GİRİLEN
                    gbpf=edit;                               //  DEĞERE GÖRE
                }else if(a==3){                              //  KURLARI ÇEVİRİYOR
                    gbpf = (edit*gbpf)/usf;                  //
                    euf = (edit*euf)/usf;                    //
                    tryf = (edit*tryf)/usf;                  //
                    usf=edit;                                //
                }else if(a==0){                              //
                    usf = (edit*usf)/tryf;                   //
                    euf = (edit*euf)/tryf;                   //
                    gbpf = (edit*gbpf)/tryf;                 //
                    tryf = edit;                             //
                }

                poundT.setText("Pound ="+gbpf);
                euroT.setText("Euro ="+euf);
                tryT.setText("Turk Lirasi ="+tryf);
                dolarT.setText("Dolar ="+usf);

            }catch(Exception e){
                dolarT.setText("A");
            }

        }
    }
}
