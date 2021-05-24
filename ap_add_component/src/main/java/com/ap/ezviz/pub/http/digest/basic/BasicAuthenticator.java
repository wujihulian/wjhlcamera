package com.ap.ezviz.pub.http.digest.basic;

import com.ap.ezviz.pub.http.digest.digest.CachingAuthenticator;
import com.ap.ezviz.pub.http.digest.digest.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.platform.Platform;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_PROXY_AUTH;

/**
 * Standard HTTP basic authenticator.
 */
public class BasicAuthenticator implements CachingAuthenticator {
    private final Credentials credentials;
    private boolean proxy;

    public BasicAuthenticator(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        final Request request = response.request();
        proxy = response.code() == HTTP_PROXY_AUTH;
        return authFromRequest(request);
    }

    private Request authFromRequest(Request request) {
        // prevent infinite loops when the password is wrong
        String header = proxy ? "Proxy-Authorization" : "Authorization";

        final String authorizationHeader = request.header(header);
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
            Platform.get().log(Platform.WARN, "previous basic authentication failed, returning null", null);
            return null;
        }
        String authValue = okhttp3.Credentials.basic(credentials.getUserName(), credentials.getPassword());
        return request.newBuilder()
                .header(header, authValue)
                .build();
    }

    @Override
    public Request authenticateWithState(Route route, Request request) throws IOException {
        return authFromRequest(request);
    }
}
