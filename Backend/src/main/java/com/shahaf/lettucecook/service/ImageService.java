package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.exceptions.ErrorOccurredException;
import com.shahaf.lettucecook.service.recipe.ElasticService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public byte[] getImageBytesFromUrl(String imageUrl) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            logger.info("Fetching bytes array from image URL {}.", imageUrl);
            HttpGet httpGet = new HttpGet(imageUrl);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                byte[] imageBytes = EntityUtils.toByteArray(response.getEntity());
                response.getEntity().getContent().close();
                return imageBytes;
            }
            String errorMessage = "Failed to fetch image from URL " + imageUrl;
            logger.error(errorMessage);
            throw new ErrorOccurredException(errorMessage);
        }
    }
}
