package pe.dsullon.encuesta.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsullon on 02/03/2017.
 */

public class Area {
    private String id;
    private String nombre;

    public Area() {
    }

    public Area(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public Area setId(String id) {
        this.id = id;
        return  this;
    }

    public String getNombre() {
        return nombre;
    }

    public Area setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static Area build(JSONObject jsonSource) {
        Area vehicle = new Area();
        try {
            vehicle.setId(jsonSource.getString("id"))
                    .setNombre(jsonSource.getString("nombre"));
            return vehicle;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Area> build(JSONArray jsonSources) {
        int areasCount = jsonSources.length();
        List<Area> areas = new ArrayList<>();
        for(int i = 0; i < areasCount; i++) {
            try {
                JSONObject jsonSource = (JSONObject) jsonSources.get(i);
                Area area = Area.build(jsonSource);
                areas.add(area);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return areas;
    }
}
