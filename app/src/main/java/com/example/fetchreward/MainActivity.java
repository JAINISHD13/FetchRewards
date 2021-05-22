package com.example.fetchreward;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Model> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView =  (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(readJSON());
            JSONArray sortedJsonArray = new JSONArray();

            List list = new ArrayList();

            // Setting up array list for Collections Sort
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }

            // Handles sorting of the listID and Name
            Collections.sort(list, new Comparator<JSONObject>() {

                String listId = "listId";
                String names = "name";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String lid1;
                    String lid2;
                    String name1 = "";
                    String name2 = "";

                    try {
                        lid1 = (String) a.get(listId).toString();
                        lid2 = (String) b.get(listId).toString();

                        int number = lid1.compareTo(lid2);

                        if (number != 0) {
                            return number;
                        }

                        name1 = (String) a.get(names).toString().replace("Item", "").trim();
                        name2 = (String) b.get(names).toString().replace("Item", "").trim();


                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        return Integer.valueOf(name1).compareTo(Integer.valueOf(name2));
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                    return name1.compareTo(name2);
                }
            });

            for (int i = 0; i < jsonArray.length(); i++) {
                sortedJsonArray.put(list.get(i));
            }


            // Handles displaying the data from JSON
            for (int i = 0; i < sortedJsonArray.length(); i++) {

                JSONObject jsonObject = sortedJsonArray.getJSONObject(i);
                String id = "ID: " + jsonObject.getString("id");
                String listId = "List Id: " + jsonObject.getString("listId");
                String name = "Name: " + jsonObject.getString("name");

                if (jsonObject.getString("name") == "null" || jsonObject.getString("name").isEmpty()) {
                    name = "";
                }

                Model model = new Model();
                model.setId(id);
                model.setName(name);

                // Handles filtering when name = (null or empty)
                if (model.getName().isEmpty()) {
                    continue;
                }

                model.setListId(listId);

                arrayList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainAdapter adapter = new MainAdapter(this, arrayList);
        listView.setAdapter((ListAdapter) adapter);
        
    }

    private String readJSON() {

        String json = null;
        try {
            // Opening hiring.json file
            InputStream inputStream = getAssets().open("hiring.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
