package nmct.howest.be.desproject.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.desproject.Kotzone;

public class KotzonesLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static List<String[]> listKotzones;
    private String urlJSON = "http://datatank.gent.be/Onderwijs&Opvoeding/Kotzones.json";

    public KotzonesLoader(Context context) {
        super(context);
    }

    // Getter: Haalt de lijst op van Kotzones
    public static List<String[]> getListKotzones() {
        return listKotzones;
    }

    // Array van kolomnamen
    private final String[] mColumnNames = new String[] {
            BaseColumns._ID,
            Contract.KotzonesColumns.COLUMN_KOTZONES_COORDS,
            Contract.KotzonesColumns.COLUMN_KOTZONES_NAAM,
    };

    private static Object lock = new Object();

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }

        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if (mCursor == null) {
            loadCursor();
        }
        return mCursor;
    }

    private void loadCursor() {
        synchronized (lock) {
            if (mCursor != null) return;

            // Creëer een MatrixCursor
            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream input = null;
            JsonReader reader = null;
            listKotzones = new ArrayList<String[]>();

            // Haal data op van JSON URL
            try {
                input = new URL(urlJSON).openStream();
                reader = new JsonReader(new InputStreamReader(input, "UTF-8"));

                int id = 1;
                reader.beginObject();

                while (reader.hasNext()) {
                    //reader.beginArray();

                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("Kotzones")) {

                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();

                                String coordinaten = "";
                                String kotzoneNaam = "";

                                // Zolang er een volgende lijn is -> Lees in
                                while (reader.hasNext()) {

                                    // Zoek op Key
                                    String name2 = reader.nextName();
                                    if (name2.equals("coords")) {
                                        if (reader.peek().equals(JsonToken.NULL)) {
                                            reader.skipValue();
                                        }
                                        else if (reader.peek().equals(JsonToken.STRING)) {
                                            coordinaten = reader.nextString();
                                        }
                                        else {
                                            reader.skipValue();
                                        }
                                    }
                                    else if (name2.equals("kotzone_na")) {
                                        kotzoneNaam = reader.nextString();
                                    }
                                    // Indien geen van bovenstaande Keys
                                    else {
                                        reader.skipValue();
                                    }
                                    // Controleer of de 3 waarden ingevuld zijn -> Valid object
                                    if (id != 0 && !coordinaten.equals("") && !kotzoneNaam.equals("") && !coordinaten.equals(null) && !kotzoneNaam.equals(null)) {
                                        // Voeg de properties toe aan een object 'Kotzone' -> Voeg object toe aan lijst
                                        String[] kotzone = new String[] {
                                                String.valueOf(id), coordinaten, kotzoneNaam
                                        };
                                        listKotzones.add(kotzone);
                                    }
                                }


                                // Maak matrixcursor rijen en vul deze op met properties van JSON data
                                MatrixCursor.RowBuilder row = cursor.newRow();
                                row.add(id);
                                row.add(coordinaten);
                                row.add(kotzoneNaam);
                                id++;

                                reader.endObject();
                            }
                        }
                        reader.endArray();
                    }

                } // End While
                reader.endObject();
                mCursor = cursor;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
