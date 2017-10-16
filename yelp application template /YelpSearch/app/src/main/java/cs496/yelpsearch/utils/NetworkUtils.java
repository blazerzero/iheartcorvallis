package cs496.yelpsearch.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by benny on 5/31/2017.
 */

public class NetworkUtils {
    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHTTPGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer _PCTiW0srvjNxUr_MHaSVgozsSJOUDp_07CvI71Vk9XwZAnmsLB9KeLJiXObNGyIGNYMiaa5ZTT4jPWFgFRYY-Z0QRvk0siHq5QDCwS7sjyxXGd0hVACENOBv0E3WXYx")
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

}
