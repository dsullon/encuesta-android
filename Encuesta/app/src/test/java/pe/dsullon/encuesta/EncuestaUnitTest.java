package pe.dsullon.encuesta;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EncuestaUnitTest {

    @Test
    public void connectionToAreas_isCorrect() throws Exception{
        String urlAreasServices = "http://www.aislamontajes.com.pe/api/areas";

        AndroidNetworking.get(urlAreasServices)
                .setTag("areas")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        assertNotNull(response);

                        try {
                            assertTrue(response.getString("status").equalsIgnoreCase("ok"));

                            if (response.getString("status").equalsIgnoreCase("ok")) {

                            }
                        } catch (JSONException e) {
                            Log.d("ListarArea", "Error:" + e.getMessage());
                        }
                    }


                    @Override
                    public void onError(ANError error) {
                        Log.d("Areas", "Error: " + error.getErrorBody());
                    }
                });



    }
}