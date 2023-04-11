package org.example;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class Main
{

    public JsonArray getPlayerData()
    {
        OkHttpClient client = new OkHttpClient();

        Request getRequest = new Request.Builder()
                .url("https://api.sportsdata.io/v3/nba/scores/json/Players/DAL?key=58957574730c4ee1b809da2f53525997")
                .build();
        String responseBody = "";
        try {
            Response response = client.newCall(getRequest).execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

        return jsonArray;
    }
}
