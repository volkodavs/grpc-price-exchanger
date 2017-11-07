package com.sergeyvolkodav.grpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.sergeyvolkodav.grpc.proto.event.EventRequest;
import com.sergeyvolkodav.grpc.proto.event.EventResponse;
import com.sergeyvolkodav.grpc.proto.event.EventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

public class EventClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8090).usePlaintext(true).build();

        EventServiceGrpc.EventServiceStub stub = EventServiceGrpc.newStub(channel);

        Semaphore exitSemaphore = new Semaphore(0);

        StreamObserver<EventRequest> requestStream = stub.observeEvents(new StreamObserver<EventResponse>() {
            @Override
            public void onNext(EventResponse eventResponse) {
                System.out.printf("Async client onNext: \n %s \n", eventResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                exitSemaphore.release();
            }

            @Override
            public void onCompleted() {
                System.out.println("Call completed!");
                exitSemaphore.release();
            }
        });

        List<Long> sportIds = new ArrayList<>();
        sportIds.add(1L);
        sportIds.add(2L);
        sportIds.add(3L);

        EventRequest request = EventRequest.newBuilder()
                .setLimit(100)
                .addAllSportId(sportIds)
                .build();
        requestStream.onNext(request);

        exitSemaphore.acquire();
    }

}
