package code.guanajuato.gob.mx.activatecode.connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;


public class ClienteHttp {
    //public static final String SERVER_IP = "192.168.1.73";
    //public static final String SERVER_IP = "10.0.7.42";
    public static final String SERVER_IP = "app.codegto.gob.mx";

    public String hacerRequestHttp(String url, HashMap<String, String> params){
        StringBuilder sbParams = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String charset = "ISO-8859-1";
        HttpURLConnection conn = null;
        URL urlObj = null;
        DataOutputStream wr = null;



        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        try {
            urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();

            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept-Charset", charset);

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");


            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            conn.connect();

            String paramsString = sbParams.toString();

            wr = new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(paramsString);
            writer.close();
            wr.flush();
            wr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


}


