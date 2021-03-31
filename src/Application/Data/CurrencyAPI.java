package Application.Data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class CurrencyAPI {

    public static Double getExRate() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://api.exchangeratesapi.io/latest?symbols=CHF");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (var reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        String res = result.toString();
        final int mid = res.length() / 2;
        String part = res.substring(0, mid);
        res = part.replaceAll("[^0-9.]","");
        return Double.parseDouble(res);
    }
}
