package com.google.codelabs.appauth;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthorizationManagementActivity;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.internal.UriUtil;

import org.json.JSONException;

import java.util.Map;


public class CustomAuthorizationService extends AuthorizationService {

    static final String PARAM_CLIENT_ID = "client_id";
    static final String PARAM_CODE_CHALLENGE = "code_challenge";
    static final String PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method";
    static final String PARAM_DISPLAY = "display";
    static final String PARAM_LOGIN_HINT = "login_hint";
    static final String PARAM_ID_TOKEN_HINT = "id_token_hint";
    static final String PARAM_PROMPT = "prompt";
    static final String PARAM_REDIRECT_URI = "post_logout_redirect_uri";
    static final String PARAM_RESPONSE_MODE = "response_mode";
    static final String PARAM_RESPONSE_TYPE = "response_type";
    static final String PARAM_SCOPE = "scope";
    static final String PARAM_STATE = "state";


    Context mContext;

    public CustomAuthorizationService(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public CustomAuthorizationService(@NonNull Context context, @NonNull AppAuthConfiguration clientConfiguration) {
        super(context, clientConfiguration);
        mContext = context;
    }




    private Uri buildLogoutUri(AuthorizationRequest request,String tokenID) throws JSONException {
        String url = request.configuration.toJson().getJSONObject("discoveryDoc").getString("end_session_endpoint");

        Uri.Builder uriBuilder = Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAM_REDIRECT_URI, request.redirectUri.toString())
                .appendQueryParameter(PARAM_CLIENT_ID, request.clientId)
                .appendQueryParameter(PARAM_RESPONSE_TYPE, request.responseType);

        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_ID_TOKEN_HINT, tokenID);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_DISPLAY, request.display);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_LOGIN_HINT, request.loginHint);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_PROMPT, request.prompt);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_STATE, request.state);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_SCOPE, request.scope);
        UriUtil.appendQueryParameterIfNotNull(uriBuilder, PARAM_RESPONSE_MODE, request.responseMode);

        if (request.codeVerifier != null) {
            uriBuilder.appendQueryParameter(PARAM_CODE_CHALLENGE, request.codeVerifierChallenge)
                    .appendQueryParameter(PARAM_CODE_CHALLENGE_METHOD, request.codeVerifierChallengeMethod);
        }

        for (Map.Entry<String,String> entry :  request.additionalParameters.entrySet()) {
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return uriBuilder.build();
    }

    private Intent prepareLogoutIntent(AuthorizationRequest request, CustomTabsIntent customTabsIntent,String tokenID) throws JSONException,ActivityNotFoundException {
        if (getBrowserDescriptor() == null) {
            throw new ActivityNotFoundException();
        }

        Uri requestUri = buildLogoutUri(request,tokenID);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(getBrowserDescriptor().useCustomTab)
            intent = customTabsIntent.intent;

        intent.setPackage(getBrowserDescriptor().packageName);
        intent.setData(requestUri);

        Log.d("TEST",String.format("Using %s as browser for auth, custom tab = %s",
                intent.getPackage(),
                getBrowserDescriptor().useCustomTab.toString()));

        Log.d("TEST",String.format("Initiating authorization request to %s",
                request.configuration.authorizationEndpoint));

        return intent;
    }

    public Intent getLogoutIntent(AuthorizationRequest request,String tokenID){
        Intent logoutIntent = null;
        try {
            logoutIntent = prepareLogoutIntent(request, createCustomTabsIntentBuilder().build(),tokenID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AuthorizationManagementActivity.createStartForResultIntent(
                mContext,
                request,
                logoutIntent
        );
    }

}
