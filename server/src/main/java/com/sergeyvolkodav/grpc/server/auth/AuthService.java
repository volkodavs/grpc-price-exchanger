package com.sergeyvolkodav.grpc.server.auth;

import java.util.HashMap;

import com.auth0.jwt.JWTSigner;
import com.sergeyvolkodav.grpc.proto.feed.AuthServiceGrpc;
import com.sergeyvolkodav.grpc.proto.feed.JwtAccessToken;
import com.sergeyvolkodav.grpc.proto.feed.User;
import io.grpc.stub.StreamObserver;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final String secret;

    public AuthService(String secret) {
        this.secret = secret;
    }

    @Override
    public void signUp(User request, StreamObserver<JwtAccessToken> responseObserver) {

        String jwtToken = createJwt(request.getEmail());
        JwtAccessToken jwtAccessToken = JwtAccessToken.newBuilder()
                .setType("Bearer")
                .setAccessToken(jwtToken)
                .build();
        responseObserver.onNext(jwtAccessToken);
        responseObserver.onCompleted();
    }
    
    private String createJwt(String subject) {
        final long iat = System.currentTimeMillis() / 1000l;
        final long exp = iat + 3600L;

        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<>();
        claims.put("iss", "PriceExchanger");
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("sub", subject);

        return signer.sign(claims);
    }
}
