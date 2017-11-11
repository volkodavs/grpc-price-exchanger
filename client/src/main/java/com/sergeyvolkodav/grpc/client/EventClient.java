package com.sergeyvolkodav.grpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.sergeyvolkodav.grpc.proto.feed.Envelope;
import com.sergeyvolkodav.grpc.proto.feed.EventRequest;
import com.sergeyvolkodav.grpc.proto.feed.EventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

public class EventClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8090).usePlaintext(true).build();

        EventServiceGrpc.EventServiceStub stub = EventServiceGrpc.newStub(channel);

        Semaphore exitSemaphore = new Semaphore(0);

        List<Long> sportIds = new ArrayList<>();
        sportIds.add(1L);
        sportIds.add(2L);
        sportIds.add(3L);

        EventRequest request = EventRequest.newBuilder()
                .addAllSportId(sportIds)
                .build();

        stub.observeEvents(request, new StreamObserver<Envelope>() {
            @Override
            public void onNext(Envelope envelope) {
                System.out.printf("Async client onNext: \n %s \n", envelope);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
//        stub.observeEvents(new StreamObserver<>());
//        StreamObserver<Envelope> requestStream = stub.observeEvents(new StreamObserver<Envelope>() {
//            @Override
//            public void onNext(Envelope envelope) {
//                System.out.printf("Async client onNext: \n %s \n", envelope);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                throwable.printStackTrace();
//                exitSemaphore.release();
//            }
//
//            @Override
//            public void onCompleted() {
//                System.out.println("Call completed!");
//                exitSemaphore.release();
//            }
//        });



        exitSemaphore.acquire();
    }

}
