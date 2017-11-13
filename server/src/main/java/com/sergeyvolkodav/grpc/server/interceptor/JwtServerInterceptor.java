package com.sergeyvolkodav.grpc.server.interceptor;

import java.util.Map;

import com.auth0.jwt.JWTVerifier;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;


public class JwtServerInterceptor implements ServerInterceptor {

    private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {
    };

    private final String secret;
    private final JWTVerifier verifier;
    private final Metadata.Key<String> jwt_metadata_key;
    private final Context.Key<Object> user_id_ctx_key;
    private final Context.Key<Object> jwt_ctx;

    public JwtServerInterceptor(String secret) {
        this.secret = secret;
        this.verifier = new JWTVerifier(secret);
        this.jwt_metadata_key = Metadata.Key.of("jwt", ASCII_STRING_MARSHALLER);
        this.user_id_ctx_key = Context.key("userId");
        this.jwt_ctx = Context.key("jwt");
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler) {

        String jwt = metadata.get(jwt_metadata_key);
        if (jwt == null) {
            serverCall.close(Status.UNAUTHENTICATED.withDescription("JWT Token is missing from Metadata"), metadata);
            return NOOP_LISTENER;
        }

        Context ctx;
        try {
            Map<String, Object> verified = verifier.verify(jwt);
            ctx = Context.current().withValue(user_id_ctx_key, verified.getOrDefault("sub", "anonymous").toString())
                    .withValue(jwt_ctx, jwt);
        } catch (Exception e) {
            System.out.println("Verification failed - Unauthenticated!");
            serverCall.close(Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e), metadata);
            return NOOP_LISTENER;
        }

        return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
    }
}
