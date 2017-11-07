package com.sergeyvolkodav.grpc.server;

import static java.lang.Math.abs;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sergeyvolkodav.grpc.proto.event.EventRequest;
import com.sergeyvolkodav.grpc.proto.event.EventResponse;
import com.sergeyvolkodav.grpc.proto.event.EventServiceGrpc;
import io.grpc.stub.StreamObserver;

public class EventService extends EventServiceGrpc.EventServiceImplBase {

    private ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);

    private static Set<StreamObserver<EventResponse>> OBSERVERS =
            Collections.newSetFromMap(new ConcurrentHashMap<>());


    public EventService() {
        initFakeResponses();
    }

    @Override
    public StreamObserver<EventRequest> observeEvents(StreamObserver<EventResponse> responseObserver) {
        OBSERVERS.add(responseObserver);

        return new StreamObserver<EventRequest>() {
            @Override
            public void onNext(EventRequest eventRequest) {
                responseObserver.onNext(buildFakeEventResponse());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Communication completed");
                OBSERVERS.remove(responseObserver);
            }
        };
    }

    private EventResponse buildFakeEventResponse() {
        Random random = new Random();
        return EventResponse.newBuilder()
                .setCountry("UK")
                .setInRunning(true)
                .setId(abs(random.nextLong()))
                .build();
    }

    private void initFakeResponses() {
        scheduledExecutorService.scheduleAtFixedRate(this::notifyClient, 0, 1, TimeUnit.SECONDS);
    }

    private void notifyClient() {
        EventResponse eventResponse = buildFakeEventResponse();
        for (StreamObserver<EventResponse> observer : OBSERVERS) {
            observer.onNext(eventResponse);
        }
    }
}
