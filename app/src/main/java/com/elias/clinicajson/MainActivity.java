package com.elias.clinicajson;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


//async mudarei de lugar no futuro  26/08/2019
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ListView lv; //lista do json 26/08/2019

    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instancio ArrayList 26/08/2019
        contactList = new ArrayList<>();

        //recuperar componentes da tela
        recuperarComponentes();

        new GetContacts().execute();
    }


    //region recuper componentes
    private void recuperarComponentes() {


        lv = (ListView) findViewById(R.id.list);

    }
    //endregion


    //region Async Task GetContacts  mudarei de lugar
    private class GetContacts extends AsyncTask<Void, Void, Void> {//responsavel por baixar o json da internet 26/08/2019

        //teste de arrayLista para lista de json array 18/10/2019
        ArrayList<HashMap<String, String>> studentList;


        //dialog
        ProgressDialog pDialog;


        // metodo da classe AsyncTask
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);//alterarei aki quando mudar asincTask de lugar 26/08/2019
            pDialog.setMessage("dowloading do Json ... ");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        //outro metodo da classe AsyncTask

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();//MEU HTTPHANDLER SERIA O JsonParser do outro exemplo ou WebRequest do exemplo jsonParsing app

            //Fazendo uma solicitação para URL e obtendo resposta
            //String url = "http://jmtecnologiabh.com.br/teste/webserviceclinica/view/Usuario/listarUsuario.php?usuarioNome=recepcionista&senha=recepcionista";// "http://jmtecnologiabh.com.br/teste/webserviceclinica/view/Usuario/listarUsuario.php?usuarioNome=gerente&senha=gerente" essa URL trouxe resultado 02/09/2019

            //teste nova hospedagem AZUL  APNET C#   seria REST FULL18/10/2019
            String url = "http://www.mrsilvatads.com.asp.hostazul.com.br/wsClinica/usuario";

            String jsonStr = sh.makeServiceCall(url);//chamo meu serviço passando a url 26/08/2019
            //jsonStr seria a string com json ?
            Log.e(TAG, "Resposta da url" + jsonStr);
            if (jsonStr != null) {//não esta ficando null mas tambem não traz o json traz \n ae gera erro
                try {


                    //criar o JSONArray
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    //pegar primeiro OBJ do ARRAY   tenho que ver como pegar todos os itens da lista 18/10/2019
                    JSONObject jsonObj = jsonArray.getJSONObject(0);//JSONObject jsonObj = new JSONObject(jsonStr);   esta dandp catch aki 18/10/2019
                    //agora jsonObj possui conteudo do primeiro elemento do json



                    String id = jsonObj.getString("IDUsuario");//original("idUsuario"); 18/10/2019
                    String name = jsonObj.getString("UsuarioNome");//original ("usuarioNome"); 18/10/2019
                    String senha = jsonObj.getString("senha");
                    String tipousuario = jsonObj.getString("TipoUsuario");// original ("tipoUsuario"); 18/10/2019
                    String token = jsonObj.getString("Token");// original ("token"); 18/10/2019  // estav errando na hora de passar o campo token na tela (tinha colocado toke) ae não trazia esse resultado


                    //tmp hashmapa para contato unico  (id unico)
                    HashMap<String, String> contact = new HashMap<>();

                    //adicionando cada nó filho á chave HashMap => value
                    contact.put("IDUsuario", id);
                    contact.put("UsuarioNome", name);
                    contact.put("senha", senha);
                    contact.put("TipoUsuario", tipousuario);
                    contact.put("Token", token);//token 09/10/2019


                    //idUsuario, usuarioNome, senha, tipoUsuario, token

                    //adicionando contato a lista de contatos
                    contactList.add(contact);


                } catch (final JSONException ex) {

                    Log.e(TAG, "Json parsing error :" + ex.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error : " + ex.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    {

                    }
                }
            } else {

                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Não foi possível obter o json do servidor. Verifique o LogCat para possíveis erros!", Toast.LENGTH_LONG).show();// Couldn't get json from server. Check LogCat for possible errors
                    }
                });
            }

            return null;//sempre tem de ter esse retono nesse app 18/10/2019
        }


        //outro metodo da classe AsyncTask
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //teste dialog
            pDialog.isShowing();
            pDialog.dismiss();

            //list olhar questão de exibir somente um registro na tela   (provavelmente seja questão de ser json simples ao inves de JsonArrayList)
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,// terei de alterar  quando mudar de lugar a asyncTasc
                    R.layout.list_item, new String[]{"IDUsuario", "UsuarioNome", "senha", "TipoUsuario", "Token"}, new int[]{R.id.idL, R.id.usuarioNomeL, R.id.senhaL, R.id.tipoUsuarioL, R.id.tokenL});//primeiro do json  segundo do meu layout  26/08/2019
            lv.setAdapter(adapter);

        }


    }


    //endregion


    //region menu app  auto gerado !!!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}
