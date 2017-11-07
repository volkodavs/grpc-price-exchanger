package com.sergeyvolkodav.grpc.server;

import com.sergeyvolkodav.grpc.proto.bets.BetRequest;
import com.sergeyvolkodav.grpc.proto.bets.BetResponse;
import com.sergeyvolkodav.grpc.proto.bets.BetsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BetService extends BetsServiceGrpc.BetsServiceImplBase {

    @Override
    public void submitBet(BetRequest request, StreamObserver<BetResponse> responseObserver) {

        BetResponse response = BetResponse.newBuilder()
                .setCurrency(request.getCurrency())
                .setEventId(request.getEventId())
                .setMarketId(request.getMarketId())
                .setCurrencyValue(request.getCurrencyValue())
                .setOfferTime(request.getOfferTime())
                .setOddsType(request.getOddsType())
                .setStake(request.getStake())
                .setExternalBetId(request.getExternalBetId())
                .setExternalAccountId(request.getExternalAccountId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
