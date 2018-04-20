package com.appdirect.dbAccessPocComponent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.elastic.api.JSON;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final String DB_API_BASE_URL = "https://8d51bf78.ngrok.io";

    public static JsonObject getSingle(final String path,
                                    final JsonObject configuration) {
        return JSON.parseObject(get(path, configuration));
    }

    public static JsonArray getMany(final String path,
                                    final JsonObject configuration) {
        return JSON.parseArray(get(path, configuration));
    }

    private static String get(final String path,
                              final JsonObject configuration) {
        String aPath = path.startsWith("/") ? path : "/" + path;

        final String requestURI = DB_API_BASE_URL + aPath;

        final HttpGet httpGet = new HttpGet(requestURI);

        return sendRequest(httpGet, configuration);
    }

    public static JsonObject post(final String path,
                                  final JsonObject configuration,
                                  final JsonObject body) {
        String aPath = path.startsWith("/") ? path : "/" + path;

        final String requestURI = DB_API_BASE_URL + aPath;

        final HttpPost httpPost = new HttpPost(requestURI);
        try {
            httpPost.setEntity(new StringEntity(JSON.stringify(body)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final String response = sendRequest(httpPost, configuration);

        return JSON.parseObject(response);
    }


    private static final String sendRequest(final HttpRequestBase request,
                                            final JsonObject configuration) {

        request.addHeader(HTTP.CONTENT_TYPE, "application/json");

        // access the value of the authorization field defined in credentials section of component.json
      /*
        final JsonString apiKey = configuration.getJsonString("authorization");
        if (apiKey == null) {
            throw new IllegalStateException("authorization is required");
        }

        request.addHeader(new BasicHeader("Authorization", apiKey.getString()));
       */

        request.addHeader(new BasicHeader("Authorization", "Basic YWRtaW46YWRtaW4="));

        final CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            final CloseableHttpResponse response = httpClient.execute(request);
            final HttpEntity responseEntity = response.getEntity();
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            logger.info("Got {} response", statusCode);

            if (responseEntity == null) {
                throw new RuntimeException("Null response received");
            }

            final String result = EntityUtils.toString(responseEntity);

            if (statusCode > 202) {
                throw new RuntimeException(result);
            }

            EntityUtils.consume(responseEntity);

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Failed to close HttpClient", e);
            }
        }
    }
}
