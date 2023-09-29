package com.shahaf.lettucecook.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {

    public byte[] getImageBytesFromUrl(String imageUrl) throws IOException {
        // Create an HttpClient
        HttpClient httpClient = HttpClients.createDefault();

        // Create an HTTP GET request to the image URL
        HttpGet httpGet = new HttpGet(imageUrl);

        // Execute the request and get the response
        HttpResponse response = httpClient.execute(httpGet);

        // Check if the response is successful (HTTP status code 200)
        if (response.getStatusLine().getStatusCode() == 200) {
            // Extract the image content as bytes from the response
            byte[] imageBytes = EntityUtils.toByteArray(response.getEntity());

            // Close the response
            response.getEntity().getContent().close();

            return imageBytes;
        } else {
            // Handle the case where the HTTP request was not successful
            throw new IOException("Failed to fetch image from URL. HTTP status code: " + response.getStatusLine().getStatusCode());
        }
    }
}
