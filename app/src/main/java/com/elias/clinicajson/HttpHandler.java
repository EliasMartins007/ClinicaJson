package com.elias.clinicajson;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

//import javax.net.ssl.HttpsURLConnection;  // tenho que olhar pois utilizar https gera cath 27/08/2019

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();//olhar  para que serve 29/08/2019

    //construtor vazio 26/08/2019 elias
    public HttpHandler() {
    }


    //service Call
    //chamada de serviço
    public String makeServiceCall(String reqUrl) {

        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//estava dando catch nessa linha devido meu erro de colocar https !! 27/08/2019 elias corrigido
            conn.setRequestMethod("GET");// conn.setRequestMethod("GET");//pegar 26/08/2019

            //read the response
            //ler a resposta
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);//metodo criado logo abaixo por min 26/08/2019    // chega \n por isso erro  !!! 27/08/2019 elias


        } catch (MalformedURLException ex) {
            Log.e(TAG, "MalformedURLException :" + ex.getMessage());
        } catch (ProtocolException ex) {
            Log.e(TAG, "ProtocolException :" + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "IOException :" + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "Exception :" + ex.getMessage());
        }
        return response;
    }


    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));// traz \n por isso gera erro 27/08/2019
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }


}
