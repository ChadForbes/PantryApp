package com.mobilesoftware.pantryapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getPlace(JSONObject googleGroceryJson) {
        HashMap<String, String> googleGroceryMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if (!googleGroceryJson.isNull("name")){
                placeName = googleGroceryJson.getString("name");
            }
            if (!googleGroceryJson.isNull("vicinity"))
            {
                vicinity = googleGroceryJson.getString("vicinity");
            }
            latitude = googleGroceryJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googleGroceryJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googleGroceryJson.getString("reference");

            googleGroceryMap.put("place_name", placeName);
            googleGroceryMap.put("vicinity", vicinity);
            googleGroceryMap.put("lat", latitude);
            googleGroceryMap.put("lng", longitude);
            googleGroceryMap.put("reference", reference);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return googleGroceryMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < count; i++){
            try{
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return placesList;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("result");
        }catch (JSONException e){
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

}
