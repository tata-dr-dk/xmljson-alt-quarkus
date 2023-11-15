package dk.dr.drip.adapters.cloud.wocache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import java.util.Base64;

@ApplicationScoped
public class RequestAuthorizationHeaderFactory implements ClientHeadersFactory {

    private final String username;
    private final String password;

    RequestAuthorizationHeaderFactory(@ConfigProperty(name = "wocache_user") String username, @ConfigProperty(name = "wocache_pw") String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", getAuthorization());
        return result;
    }

    private String getAuthorization() {
        return "Basic " + Base64.getEncoder().encodeToString((username+":"+password).getBytes());
    }

}