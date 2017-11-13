package com.sergeyvolkodav.grpc.moneymaker;

import static java.util.UUID.randomUUID;

import java.util.Random;
import java.util.stream.Stream;

import com.google.protobuf.Timestamp;
import com.sergeyvolkodav.grpc.proto.feed.AuthServiceGrpc;
import com.sergeyvolkodav.grpc.proto.feed.BetRequest;
import com.sergeyvolkodav.grpc.proto.feed.BetResponse;
import com.sergeyvolkodav.grpc.proto.feed.BetsServiceGrpc;
import com.sergeyvolkodav.grpc.proto.feed.Currency;
import com.sergeyvolkodav.grpc.proto.feed.JwtAccessToken;
import com.sergeyvolkodav.grpc.proto.feed.Stake;
import com.sergeyvolkodav.grpc.proto.feed.User;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

public class MoneyMakerClient {

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();

        ManagedChannel grpcChannel = NettyChannelBuilder
                .forAddress("localhost", 8090)
                .usePlaintext(true).build();


        AuthServiceGrpc.AuthServiceBlockingStub autStub = AuthServiceGrpc.newBlockingStub(grpcChannel);
        JwtAccessToken jwtAccessToken = autStub.signUp(User.newBuilder().setEmail("sergii@email.com").build());

        JwtCallCredential callCredential = new JwtCallCredential(jwtAccessToken.getAccessToken());
        BetsServiceGrpc.BetsServiceStub asyncStub = BetsServiceGrpc.newStub(grpcChannel)
                .withCallCredentials(callCredential);


        while (true) {
            Float stake = random.nextInt(100) + random.nextFloat();
            BetRequest request = BetRequest.newBuilder()
                    .setGuid(randomUUID().toString())
                    .setBetTime(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                    .setStake(Stake.newBuilder().setAmount(stake.toString()).setCurrency(Currency.EUR).build())
                    .setExternalBetId(random.nextLong())
                    .setExternalAccountId(random.nextInt(2))
                    .build();

            asyncStub.submitBet(request, new StreamObserver<BetResponse>() {
                @Override
                public void onNext(BetResponse betResponse) {
                    System.out.printf("Submit bet for %s: %s.%n", request, betResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    System.err.printf("Cannot submit bet for %s.%n", request);
                }

                @Override
                public void onCompleted() {
                    System.out.printf("Stream completed.%n");
                }
            });

            Thread.sleep(5000);
        }
    }
}
