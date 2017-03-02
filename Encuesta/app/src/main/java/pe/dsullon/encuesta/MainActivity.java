package pe.dsullon.encuesta;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextInputEditText editTextLugar;
    TextInputEditText editTextComentario;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextLugar = (TextInputEditText)findViewById(R.id.etLugar);
        editTextComentario = (TextInputEditText) findViewById(R.id.etComentario);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluar();
                String mensaje = "Lugar: " + editTextLugar.getText().toString() + ".";
                mensaje += " su valoración es: " + String.valueOf(rating.getRating()) + ".";
                Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

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

    private void evaluar() {
        AndroidNetworking.post("http://www.aislamontajes.com.pe/api/eval")
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("lugar", editTextLugar.getText().toString())
                .addBodyParameter("valoracion",String.valueOf(rating.getRating()))
                .addBodyParameter("comentario",  editTextComentario.getText().toString())
                .setTag("Evaluar")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equalsIgnoreCase("ok")) {

                            }
                        } catch (JSONException e) {
                            Log.d("Login", "Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Login", "Error: " + anError.getErrorBody());

                    }
                });
    }
}
