package org.linphone.classes;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class MyAsyncTask  extends AsyncTask<String, Void, BalanceResponse> {
    private TextView textView;

    public MyAsyncTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected BalanceResponse doInBackground(String... strings) {
        String urlString = strings[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), BalanceResponse.class);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(BalanceResponse balanceResponse) {
        if (balanceResponse != null) {
            // JSON parsing successful, update TextView with the parsed data
            String result = "Result: " + balanceResponse.getResult() + "\n";
            String error = "Error: " + balanceResponse.getError() + "\n";
            String username = "Username: " + balanceResponse.getUsername() + "\n";
            String credit = "Credit: " + balanceResponse.getCredit() + "\n";

            String displayText = result + error + username + credit;
            textView.setText("BDT " + balanceResponse.getCredit());
        } else {
            // Handle null response
            textView.setText("Error");
        }
    }
}
