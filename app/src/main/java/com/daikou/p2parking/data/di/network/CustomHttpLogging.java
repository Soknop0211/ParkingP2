package com.daikou.p2parking.data.di.network;

import androidx.annotation.NonNull;
import androidx.viewbinding.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class CustomHttpLogging extends
        MyHttpLoggingInterceptor {

    private final HashMap<String, CountRespondServer> requestHashMap = new HashMap<>();
    private final boolean isDevelopment = true /*BuildConfig.IS_DEBUG*/;
    private Charset charset = UTF8;
    private String respondBody = "";

    public CustomHttpLogging(Logger logger) {
        this.logger = logger;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        /*################################ log body request #################################*/

        if (logHeaders) {
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (isDevelopment) {
                    logger.log("| token | " + name + ": " + headers.value(i));
//                    if ("Authorization".equalsIgnoreCase(name)) {
//                        logger.log("| token | " + name + ": " + headers.value(i));
//
//                    }
                }
            }
            String printRequestUrl = "";
            String printRequestBody = "";
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logger.log("");
                if (isPlaintext(buffer)) {
                    printRequestUrl = String.format("%s | %s", request.url(), request.method());
                    printRequestBody = buffer.readString(charset);
                    printRequestBody = parseNewGsonData(printRequestBody);
                    if (isDevelopment) {
                        logger.log(String.format(" ➡➡ %s | %s |  ↑ ", request.url(), request.method()));
                        logger.log("\nRequest: ");
                        logger.log(printRequestBody);
                    }
                }
            } else {
                printRequestUrl = String.format("%s | %s", request.url(), request.method());
                if (isDevelopment) {
                    logger.log(String.format(" ➡➡ %s | %s |  ↑ ", request.url(), request.method()));
                    logger.log("\nRequest: ");
                }
            }


            long tStart = System.currentTimeMillis();
            CountRespondServer countRespondServer = new CountRespondServer(printRequestBody, tStart);
            requestHashMap.put(printRequestUrl, countRespondServer);
        }

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }

        /*################################ log respond body #################################*/

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (logHeaders) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                logger.log("");
                return response;
            }

            if (contentLength != 0) {
                logger.log("");
                String respondUrl = String.format("%s | %s", request.url(), request.method());
                String headerRespond = String.format(" ➡➡ %s | %s | ↓ ", request.url(), request.method());
                respondBody = buffer.clone().readString(charset);

                if (isDevelopment) {
                    logger.log(headerRespond);
                    logger.log("Respond: ");
                    logger.log(respondBody);
                }
            }

        }
        return response;
    }


    private String formatPrettyJsonView(String jsonString) {
        int spacesToIndentEachLevel = 2;
        try {
            return new JSONObject(jsonString).toString(spacesToIndentEachLevel);
        } catch (JSONException ignored) {
        }
        return null;
    }

    private String parseNewGsonData(String data) {
        try {
            JsonElement respond = new Gson().fromJson(data, JsonElement.class);
            boolean hasRespond = respond != null;
            if (hasRespond) {
                //checking code first
                JsonObject rootObject = respond.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entryData = rootObject.entrySet();

                String keyIcon = "\uD83D\uDD12";
                String moneyIcon = "\uD83D\uDCB0";
                String naturalIcon = "\uD83D\uDDBC️";

                entryData.forEach(value -> {
                    if (value == null) return;
                    boolean hasMatchKey = keyDisable().contains(value.getKey());
                    if (hasMatchKey) {
                        if (value.getKey().contains("profile_img")) {
                            rootObject.addProperty(value.getKey(), naturalIcon);
                        } else if (value.getKey().contains("kyc_documents")) {
                            rootObject.addProperty(value.getKey(), naturalIcon);
                        } else if (value.getKey().contains("amount")) {
                            rootObject.addProperty(value.getKey(), moneyIcon);
                        } else {
                            rootObject.addProperty(value.getKey(), keyIcon);
                        }

                    }
                });

                JsonObject jsonObject = new JsonObject();
                entryData.forEach(value -> {
                    jsonObject.add(value.getKey(), value.getValue());
                });

                return jsonObject.toString();

            }
        } catch (JsonSyntaxException ignored) {
        }

        return "";
    }

    private List<String> keyDisable() {
        if (BuildConfig.DEBUG) {
            return new ArrayList<String>() {{
                add("kyc_documents");
                add("profile_img");
            }};
        }
        return new ArrayList<String>() {{
            add("profile_img");
            add("total_amount");
            add("id_card_img");
            add("vehicle_img");
            add("plate_number_img");
            add("confirm_pin");
            add("current_pin");
            add("debited_funds_amount");
            add("iban");
            add("fk_kuser");
            add("password");
            add("verify_pin");
            add("credited_funds_amount");
            add("owner_name");
            add("phone_number");
            add("data");
            add("token");
            add("body");
            add("security_code");
            add("pin");
            add("amount");
            add("tokae_token");
            add("kyc_documents");
            add("password_confirmation");
            add("otp_token");
        }};
    }

    private double elapsedSeconds(long tStart, long tEnd) {
        double milliseconds = tEnd - tStart;
        return (milliseconds / 1000);
    }

    private static class CountRespondServer {
        private String requestUrl;
        private long startTimer;


        public CountRespondServer(String requestUrl, long startTimer) {
            this.requestUrl = requestUrl;
            this.startTimer = startTimer;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public long getStartTimer() {
            return startTimer;
        }

        public void setStartTimer(long startTimer) {
            this.startTimer = startTimer;
        }
    }

}
