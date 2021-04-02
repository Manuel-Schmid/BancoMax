package Application.Data;

import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class CurrencyAPI {

    public static Double getExRate() throws Exception {
        StringBuilder res = new StringBuilder();
        URL url = new URL("http://api.exchangeratesapi.io/v1/latest?access_key=b11c07a5c6d7fde5ea8c69e1f0f8676f&symbols=CHF,EUR");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (var reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                res.append(line);
            }
        }
        String result = String.valueOf(res);
        JSONObject obj = new JSONObject(result);
        return obj.getJSONObject("rates").getDouble("CHF");
    }
}
