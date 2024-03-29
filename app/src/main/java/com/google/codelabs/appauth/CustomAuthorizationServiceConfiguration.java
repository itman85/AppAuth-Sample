package com.google.codelabs.appauth;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;

public class CustomAuthorizationServiceConfiguration extends AuthorizationServiceConfiguration {

    public CustomAuthorizationServiceConfiguration instance(){
        return new CustomAuthorizationServiceConfiguration(
                Uri.parse("https://oidc-int.immoscout24.ch/connect/authorize") /* auth endpoint */,
                Uri.parse("https://oidc-int.immoscout24.ch/connect/token"), /* token endpoint */
                Uri.parse("https://oidc-int.immoscout24.ch/connect/endsession") /* end session endpoint */
        );
    }

    /**
     * The end session service's endpoint;
     */
    @Nullable
    public final Uri endSessionEndpoint;

    public CustomAuthorizationServiceConfiguration(@NonNull Uri authorizationEndpoint, @NonNull Uri tokenEndpoint, @Nullable Uri endSessionEndpoint) {
        super(authorizationEndpoint, tokenEndpoint);
        this.endSessionEndpoint = endSessionEndpoint;
    }

    public CustomAuthorizationServiceConfiguration(@NonNull Uri authorizationEndpoint, @NonNull Uri tokenEndpoint, @Nullable Uri registrationEndpoint, @Nullable Uri endSessionEndpoint) {
        super(authorizationEndpoint, tokenEndpoint, registrationEndpoint);
        this.endSessionEndpoint = endSessionEndpoint;
    }

    public CustomAuthorizationServiceConfiguration(@NonNull AuthorizationServiceDiscovery discoveryDoc, @Nullable Uri endSessionEndpoint) {
        super(discoveryDoc);
        this.endSessionEndpoint = endSessionEndpoint;
    }
}
