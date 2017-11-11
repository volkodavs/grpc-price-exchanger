package com.sergeyvolkodav.grpc.server;


import static java.lang.Math.abs;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sergeyvolkodav.grpc.proto.feed.Envelope;
import com.sergeyvolkodav.grpc.proto.feed.Event;
import com.sergeyvolkodav.grpc.proto.feed.EventRequest;
import com.sergeyvolkodav.grpc.proto.feed.EventServiceGrpc;
import com.sergeyvolkodav.grpc.proto.feed.MessageType;
import io.grpc.stub.StreamObserver;

public class EventService extends EventServiceGrpc.EventServiceImplBase {

    private ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);

    private static Set<StreamObserver<Envelope>> OBSERVERS =
            Collections.newSetFromMap(new ConcurrentHashMap<>());


    public EventService() {
        initFakeResponses();
    }

    @Override
    public void observeEvents(EventRequest request, StreamObserver<Envelope> responseObserver) {
        OBSERVERS.add(responseObserver);
        responseObserver.onNext(buildFakeEventResponse());
    }


    private Envelope buildFakeEventResponse() {
        Random random = new Random();
        Event masterEvent = Event.newBuilder()
                .setCompetition("HR")
                .setCountry("UK")
                .setId(abs(random.nextLong()))
                .setInRunning(false)
                .build();
        return Envelope.newBuilder()
                .setType(MessageType.MASTER)
                .setEvent(masterEvent)
                .build();
    }

    private void initFakeResponses() {
        scheduledExecutorService.scheduleAtFixedRate(this::notifyClient, 0, 1, TimeUnit.SECONDS);
    }

    private void notifyClient() {
        Envelope eventResponse = buildFakeEventResponse();
        for (StreamObserver<Envelope> observer : OBSERVERS) {
            observer.onNext(eventResponse);
        }
    }
}
