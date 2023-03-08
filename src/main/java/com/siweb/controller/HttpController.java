package com.siweb.controller;

import com.siweb.App;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import java.util.Map;
import java.util.function.*;

/**
 * HttpController is a singleton to handle all http requests
 * All Http requests are asynchronous
 * Implemented with JSON Web Token (JWT) authentication:
 *   - Access token is automatically refreshed by using the Refresh token
 *   - When both tokens are expired, redirect to login page
 */
public class HttpController {
    private static final HttpController instance = new HttpController();
    private final HttpClient client = HttpClient.newHttpClient();
    private String accessToken = "";
    private String refreshToken = "";
    private HttpController(){}
    public static HttpController getInstance(){
        return instance;
    }


    // set tokens locally
    private void setTokens(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        System.err.println("accessToken updated");
        System.err.println("refreshToken updated");
        System.err.println();
    }

    // response handler for all http requests, except for refreshing token which is handled in refresh_token_and_retry()
    private void response_handler(HttpResponse<?> res, String reqMethod, String uri, Map<?, ?> data, Consumer<?> listener) {
        response_handler(res, reqMethod, uri, data, listener, "");
    }
    private void response_handler(HttpResponse<?> res, String reqMethod, String uri, Map<?, ?> data, Consumer<?> listener, String actionName) {

        // printing response for testing
        System.err.println(res);
        System.err.println(res.body());
        System.err.println();

        if(res.statusCode() >= 200 && res.statusCode() < 300)
        {
            // if the request is successful, parse the json and call the listener
            try
            {
                // check if it's a JSON with the structure "{...}"

                JSONObject resJSON;

                if(reqMethod.equals("DELETE")) { // this appears to be returning empty body from the API, we expect a JSON
                    resJSON = new JSONObject("{}");
                } else {
                    resJSON = new JSONObject(res.body().toString());
                }

                if(actionName.equals("login")) {
                    // login success, save the tokens locally for authentication
                    setTokens(resJSON.getString( "access" ), resJSON.getString( "refresh" ));
                } else if(actionName.equals("logout")) {
                    // logout success, remove the tokens locally
                    setTokens("", "");
                }
                ((Consumer<JSONObject>) listener).accept(resJSON);
            }
            catch (Exception e)
            {
                try
                {
                    // check if it's a JSON with the structure "[...]"
                    ((Consumer<JSONArray>) listener).accept(new JSONArray(res.body().toString()));
                }
                catch (Exception e2)
                {
                    // JSON not valid or listener is wrong
                    e.printStackTrace();
                }
            }
        }
        else if(res.statusCode() == 401 && !refreshToken.equals(""))
        {
            // access token not valid, automatically refresh the token and retry the request
            refresh_token_and_retry(reqMethod, uri, data, listener, actionName);
        }
        else {
            // error, show popup notifications

        }



    }

    public void post(String uri, Map<?, ?> data, Consumer<?> listener) {
        post(uri, data, listener, "");
    }
    // Sender for all post requests
    public void post(String uri, Map<?, ?> data, Consumer<?> listener, String actionName) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(App.API_URI + uri))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Authorization", "Bearer " + accessToken)
                .POST(BodyPublishers.ofString(new JSONObject(data).toString()))
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(res -> response_handler(res, "POST", uri, data, listener, actionName));

    }


    public void put(String uri, Map<?, ?> data, Consumer<?> listener) {
        put(uri, data, listener, "");
    }
    // Sender for all put requests
    public void put(String uri, Map<?, ?> data, Consumer<?> listener, String actionName) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(App.API_URI + uri))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Authorization", "Bearer " + accessToken)
                .PUT(BodyPublishers.ofString(new JSONObject(data).toString()))
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(res -> response_handler(res, "PUT", uri, data, listener, actionName));

    }


    // Sender for all get requests
    public void get(String uri, Consumer<?> listener) {
        get(uri, listener, "");
    }
    public void get(String uri, Consumer<?> listener, String actionName) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(App.API_URI + uri))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(res -> response_handler(res, "GET", uri, Map.of(), listener, actionName));

    }


    public void delete(String uri, Consumer<?> listener) {
        delete(uri, listener, "");
    }
    public void delete(String uri, Consumer<?> listener, String actionName) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(App.API_URI + uri))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Authorization", "Bearer " + accessToken)
                .DELETE()
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(res -> response_handler(res, "DELETE", uri, Map.of(), listener, actionName));

    }


    // refresh tokens and retry the failed request again
    private void refresh_token_and_retry(String reqMethod, String uri, Map<?, ?> data, Consumer<?> listener, String actionName) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(App.API_URI + "/auth/token_refresh/"))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Authorization", "Bearer " + accessToken)
                .POST(BodyPublishers.ofString(new JSONObject(Map.of("refresh", refreshToken)).toString()))
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(res -> {

                    if(res.statusCode() >= 200 && res.statusCode() < 300)
                    {
                        // We have successfully refreshed both tokens
                        JSONObject resJSON = new JSONObject(res.body().toString());

                        // set it locally
                        setTokens(resJSON.getString("access"), resJSON.getString("refresh"));

                        System.err.println("Tokens refreshed. Retrying the request...");
                        System.err.println();

                        // retry the previous failed request
                        if(reqMethod.equals("GET"))
                        {
                            get(uri, listener, actionName);
                        }
                        else if(reqMethod.equals("POST")){
                            post(uri, data, listener, actionName);
                        }
                        else if(reqMethod.equals("PUT")){
                            put(uri, data, listener, actionName);
                        }
                        else if(reqMethod.equals("DELETE")){
                            delete(uri, listener, actionName);
                        }

                    }
                    else if(res.statusCode() == 401)
                    {
                        // refresh token expired, returning to login page
                        try
                        {
                            System.err.println("refresh token expired, returning to login page...");
                            System.err.println();
                            accessToken = "";
                            refreshToken = "";
                            com.siweb.App.setRoot("login");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(res.statusCode() == 408 || res.statusCode() == 502 || res.statusCode() == 503) {
                        // Timeout, let user try again later.
                    }
                    else {
                        // Other http errors
                    }

                });

    }

    public void logout() {

        post("/auth/logout/", Map.of("refresh", refreshToken), (JSONObject res) -> {

            // It's important to wait for the main thread before logging out
            Platform.runLater(()-> {

                try {

                    // logout successfully, returning to login page...

                    accessToken = "";
                    refreshToken = "";

                    System.err.println("logout successfully, tokens cleared, returning to login page...");
                    System.err.println();

                    com.siweb.App.setRoot("login");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }, "logout");
    }


}
