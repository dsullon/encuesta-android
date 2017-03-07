package pe.dsullon.encuesta.activities;

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
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.dsullon.encuesta.R;
import pe.dsullon.encuesta.models.Area;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerArea;
    TextInputEditText editTextComentario;
    RatingBar rating;
    List<Area> areas;
    ArrayList<Area> areasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Revisar el código, entender y proponer posible refactoring

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AndroidNetworking.initialize(getApplicationContext());

        spinnerArea = (Spinner)findViewById(R.id.spinnerSkill);
        editTextComentario = (TextInputEditText) findViewById(R.id.etComentario);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        cargarListaAreas();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //TODO: Revisa, refactorizar, el mensaje debe ser acorde al resultado de grabación
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grabarEncuesta();

                String mensaje = " su valoración es: " + String.valueOf(rating.getRating()) + ".";

                mostrarMensaje(mensaje);

                limpiarEncuesta();
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

        //TODO: Falta implementar iniciar sesión
        if (id == R.id.action_sign_in) {
            mostrarMensaje("Falta implementar iniciar sesión");
        }


        return super.onOptionsItemSelected(item);
    }

    private void cargarListaAreas(){
        final String nombreMetodo = "cargarListaAreas";

        String urlListaAreas = "http://www.aislamontajes.com.pe/api/areas";

        AndroidNetworking.get(urlListaAreas)
                .setTag("areas")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String nombreEvento = "onResponse";
                        String tag = nombreMetodo + "-" + nombreEvento;

                        try {
                            if(response.getString("status").equalsIgnoreCase("ok")) {

                                areasAdapter = serializarAreas(response);
                                cargarListaAreas(areasAdapter);
                            }
                        } catch (JSONException e) {
                            Log.e(tag, e.getMessage());

                        } catch (Exception e) {
                            Log.e(tag, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        String nombreEvento = "onError";
                        String tag = nombreMetodo + "-" + nombreEvento;

                        Log.e(tag, error.getErrorBody());
                    }
                });
    }

    private void cargarListaAreas(List<Area> areas){
        spinnerArea.setAdapter(new ArrayAdapter<Area>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                areas));
    }

    private void grabarEncuesta() {
        final String nombreMetodo = "grabarEncuesta";
        String urlGrabarEncuesta = "http://www.aislamontajes.com.pe/api/eval";


        AndroidNetworking.post(urlGrabarEncuesta)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("lugar", spinnerArea.getSelectedItem().toString())
                .addBodyParameter("valoracion",String.valueOf(rating.getRating()))
                .addBodyParameter("comentario",  editTextComentario.getText().toString())
                .setTag("Evaluar")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String nombreEvento = "onResponse";
                        String tag = nombreMetodo + "-" + nombreEvento;

                        try {
                            if(response.getString("status").equalsIgnoreCase("ok")) {
                                //TODO: Aquí qué debe haber? Evaluar convertir en método que retorne resultado boolean
                                Log.i(tag, "La encuesta se grabó con exito!"); //Log temporal hasta resolver este caso de refactoring

                            }
                        } catch (JSONException e) {
                            Log.e(tag, e.getMessage());

                        } catch (Exception e) {
                            Log.e(tag, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        String nombreEvento = "onError";
                        String tag = nombreMetodo + "-" + nombreEvento;

                        Log.e(tag, anError.getMessage());
                    }
                });
    }

    private ArrayList<Area> serializarAreas(JSONObject response) throws JSONException {
        List<Area> jsonAreas = Area.build(response.getJSONArray("areas"));
        ArrayList<Area> listaAreas = new ArrayList<Area>();

        //TODO: Revisar como mapear directamente areasAdapet/areas, es posible?

        for(int i = 0; i < jsonAreas.size(); i++) {
            Area areafromService = jsonAreas.get(i);

            String nombreArea = areafromService.getNombre();
            String idArea = areafromService.getId();

            Area areaItem = new Area(idArea, nombreArea);
            listaAreas.add(areaItem);
        }

        return listaAreas;
    }

    private void limpiarEncuesta() {
        spinnerArea.setSelection(0, false);
        rating.setRating(0);
        editTextComentario.setText("");
    }

    private void mostrarMensaje(String mensaje) {

        View view = getWindow().getDecorView().findViewById(R.id.content_main);

        Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG)
                .setAction("Action",  null).show();
    }
}
