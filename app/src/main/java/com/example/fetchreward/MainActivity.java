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

                String KEY_NAME = "listId";
                String KEY_NAME2 = "name";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String str1;
                    String str2;
                    String str3 = "";
                    String str4 = "";

                    try {
                        str1 = (String) a.get(KEY_NAME).toString();
                        str2 = (String) b.get(KEY_NAME).toString();

                        int number = str1.compareTo(str2);

                        if (number != 0) {
                            return number;
                        }

                        str3 = (String) a.get(KEY_NAME2).toString().replace("Item", "").trim();
                        str4 = (String) b.get(KEY_NAME2).toString().replace("Item", "").trim();


                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        return Integer.valueOf(str3).compareTo(Integer.valueOf(str4));
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                    return str3.compareTo(str4);
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