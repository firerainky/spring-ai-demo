package spring.ai.example.spring_ai_demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SimpleRequestExecutor {

    public String executeGetRequest(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String executePostRequest(String urlString, Map<String, String> headers, String jsonBody) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static void main(String[] args) {
        SimpleRequestExecutor executor = new SimpleRequestExecutor();
        
        // GET 请求示例
        System.out.println("--- Testing GET Request ---");
        String getResponse = executor.executeGetRequest("https://api.github.com");
        System.out.println("GET Response: " + getResponse);

        System.out.println("\n--- Testing POST Request ---");
        // POST 请求示例
        // 示例 Header
        Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
        // 示例 JSON body
        String jsonBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
        // 示例 URL
        String postResponse = executor.executePostRequest("https://jsonplaceholder.typicode.com/posts", headers, jsonBody);
        System.out.println("POST Response: " + postResponse);
    }
}