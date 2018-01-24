package com.example.esatgozcu.jsonkullanimi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
    }

    // Getir butonuna tıklayınca..
    public void getData(View view){

        // Oluşturduğumuz DownloadData sınıfından bir nesne oluşturuyoruz.
        DownloadData downloadData = new DownloadData();

        try {

            // Kullanıcının verisini link ile birleştiriyoruz.
            String url = "http://api.fixer.io/latest?base=";
            String chosenBase = editText.getText().toString();

            downloadData.execute(url+chosenBase);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class DownloadData extends AsyncTask<String, Void, String> {

        // Arka planda gerçekleşecek işlemler..
        @Override
        protected String doInBackground(String... params) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                // Kullanıcının editText'te veri girmesi ile elde ettiğimiz url'deki bütün
                // verileri karakter karakter çekiyoruz

                url = new URL(params[0]);
                // Url ile bağlantı kuruyoruz.
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // Url'deki verileri çekebilmek için InputStream oluşturuyoruz.
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0) {

                    char character = (char) data;
                    result += character;

                    // Karakteri bir ileriye taşıyoruz.
                    data = inputStreamReader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Arka plandaki işlemler bittikten sonra buraya geliyoruz.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                // Elde ettiğimiz data'yı json formatına dönüştürüyoruz.

                JSONObject jsonObject = new JSONObject(s);
                String rates = jsonObject.getString("rates");

                JSONObject jsonObject1 = new JSONObject(rates);
                String tl = jsonObject1.getString("TRY");

                textView.setText("TRY: "+tl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
