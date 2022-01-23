package httpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import persistence.Asset;
import com.google.gson.Gson;
import persistence.Paste;

public class Client {
    /**
     * Passing in email address and receiving its JSON data pertaining to that address.
     * @param email_address
     * @param api_key
     * @throws IOException
     * @throws InterruptedException
     */
    public void fetchContent(String email_address, String api_key) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        String url = "https://haveibeenpwned.com/api/v3/pasteaccount/" + URLEncoder.encode(email_address, "UTF-8");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).setHeader("hibp-api-key", api_key)
                .GET()
                .timeout(Duration.ofMillis(5000))
                .build();

        try {
            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .get()
                    .body();
            Paste email_breaches = new Gson().fromJson(response, Paste.class);
            System.out.println(email_breaches); // Printing JSON object
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}