package fastcampus.aop.part2.afit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, Void, String> {

    String clientKey = "yw";;
    private String str, receiveMsg;
    private final String ID = "nyw";

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("http://3.37.56.9:8000/login/logout"); // 서버 URL

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", clientKey);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("결과", conn.getResponseCode() + "Error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}