package com.sergeyvolkodav.grpc.server.services;

import com.google.protobuf.Timestamp;
import com.sergeyvolkodav.grpc.proto.feed.BetRequest;
import com.sergeyvolkodav.grpc.proto.feed.BetResponse;
import com.sergeyvolkodav.grpc.proto.feed.BetsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BetService extends BetsServiceGrpc.BetsServiceImplBase {

    @Override
    public void submitBet(BetRequest request, StreamObserver<BetResponse> responseObserver) {

        BetResponse response = BetResponse.newBuilder()
                .setBetTime(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                .setStake(request.getStake())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
