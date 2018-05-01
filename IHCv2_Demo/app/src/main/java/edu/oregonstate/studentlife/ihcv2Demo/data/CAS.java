package edu.oregonstate.studentlife.ihcv2Demo.data;

import org.apache.http.client.HttpClient;

/**
 * Created by Omeed on 4/11/18.
 */

public class CAS {

    private static final String TAG = CAS.class.getSimpleName();
    private static final String CAS_LOGIN_URL_PART = "login";
    private static final String CAS_LOGOUT_URL_PART = "logout";
    private static final String CAS_SERVICE_VALIDATE_URL_PART = "serviceValidate";
    private static final String CAS_TICKET_BEGIN = "ticket=";
    private static final String CAS_LT_BEGIN = "name=\"lt\" value=\"";
    private static final String CAS_USER_BEGIN = "<cas:user>";
    private static final String CAS_USER_END = "</cas:user>";

    private HttpClient httpClient;
    private String baseURL;

    public CAS (HttpClient httpClient, String baseURL) {
        this.httpClient = httpClient;
        this.baseURL = baseURL;
    }

    /*public String authenticate (String serviceURL) {
        String serviceTicket = null;
        String lt = getLTFromAuthForm(serviceURL);
        if (lt == null) {
            Log.d(TAG, "Cannot retrieve LT from CAS. Aborting authentication.");
            return null;
        }
        else {
            Log.d(TAG, "POST: " + baseURL + CAS_LOGIN_URL_PART + "?service=" + serviceURL);
            String fullURL = baseURL + CAS_LOGIN_URL_PART + "?service=" + serviceURL;
            HttpPost httpPost = new HttpPost(fullURL);
            try {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("lt", lt));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));

                HttpR
            }
        }
    }*/
}
