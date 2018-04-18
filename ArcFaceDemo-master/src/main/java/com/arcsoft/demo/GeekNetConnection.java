package com.arcsoft.demo;
import android.util.Base64;
import android.util.Log;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by win7 on 2018/1/27.
 */

public class GeekNetConnection {
    private static String geekURL = "http://geek-team.xin/FaceIn/";

    public static AFR_FSDKFace getFaceData (String userID) {
        AFR_FSDKFace face = null;
        try {
            URL url = new URL(geekURL+"SignStudent");
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            JSONObject params = new JSONObject();
            params.put("studentid", userID);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()));
            pw.print(params.toString());
            pw.flush();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String tempLine = reader.readLine();
            JSONObject netReturn = new JSONObject(tempLine);
            String result = netReturn.getString("result");
            Log.e(":::GeekNetConnection:::", "image");
            if (result.equals("true")) {
                String imgReturn = netReturn.getString("data");

                Log.e(":::GeekNetConnection:::", "imgReturn:::"+imgReturn);

                Log.e(":::GeekNetConnection:::", "imgReturn:::2:::"+FaceDB.data);
                byte[] imgBytes = Base64.decode(imgReturn, Base64.DEFAULT);
                Log.e(":::GeekNetConnection:::", "imgByes:::"+imgBytes);
                face = new AFR_FSDKFace(imgBytes);
                return face;
            } else {
                Log.e(":::GeekNetConnection:::", result.toString());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return face;

    }
}
