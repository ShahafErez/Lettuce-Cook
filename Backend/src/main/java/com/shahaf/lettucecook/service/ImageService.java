package com.shahaf.lettucecook.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {

    public byte[] getImageBytesFromUrl(String imageUrl) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(imageUrl);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                byte[] imageBytes = EntityUtils.toByteArray(response.getEntity());
                response.getEntity().getContent().close();
                return imageBytes;
            }
            throw new IOException("Failed to fetch image from URL " + imageUrl);
        }
    }
}
