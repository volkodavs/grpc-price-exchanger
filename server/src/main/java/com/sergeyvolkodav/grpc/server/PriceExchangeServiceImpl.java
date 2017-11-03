package com.sergeyvolkodav.grpc.server;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.sergeyvolkodav.grpc.proto.AddResponse;
import com.sergeyvolkodav.grpc.proto.Event;
import com.sergeyvolkodav.grpc.proto.FindOneRequest;
import com.sergeyvolkodav.grpc.proto.PriceExchangeServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PriceExchangeServiceImpl extends PriceExchangeServiceGrpc.PriceExchangeServiceImplBase {

    private static BlockingQueue<Event> queue = new ArrayBlockingQueue<>(1024);

    private static Set<StreamObserver<Event>> observers =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void addPrice(Event request, StreamObserver<AddResponse> responseObserver) {
        System.out.println(request);
        queue.add(request);
        AddResponse response = AddResponse.newBuilder().setId(1L).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPrice(FindOneRequest request, StreamObserver<Event> responseObserver) {
        observers.add(responseObserver);
        while (true) {
            try {
                Event event = queue.take();
                for (StreamObserver<Event> observer : observers) {
                    if (event.getId() == request.getId()) {
                        observer.onNext(event);
                    }
                }
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
        }
    }
}
