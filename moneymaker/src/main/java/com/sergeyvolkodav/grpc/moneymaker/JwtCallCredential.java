package com.sergeyvolkodav.grpc.moneymaker;

import java.util.concurrent.Executor;

import io.grpc.Attributes;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

public class JwtCallCredential implements CallCredentials {
  private final String jwt;

  public JwtCallCredential(String jwt) {
    this.jwt = jwt;
  }


  @Override
  public void thisUsesUnstableApi() {

  }

  @Override
  public void applyRequestMetadata(MethodDescriptor<?, ?> methodDescriptor, Attributes attributes, Executor executor, MetadataApplier metadataApplier) {
    String authority = attributes.get(ATTR_AUTHORITY);
    System.out.println(authority);
    executor.execute(() -> {
      try {
        Metadata headers = new Metadata();
        Metadata.Key<String> jwtKey = Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER);
        headers.put(jwtKey, jwt);
        metadataApplier.apply(headers);
      } catch (Throwable e) {
        metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
      }
    });
  }
}
