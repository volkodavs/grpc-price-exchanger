package com.sergeyvolkodav.grpc.client;


import java.util.Iterator;

import com.sergeyvolkodav.grpc.proto.Event;
import com.sergeyvolkodav.grpc.proto.FindOneRequest;
import com.sergeyvolkodav.grpc.proto.PriceExchangeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PriceExchangerClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext(true)
                .build();

        PriceExchangeServiceGrpc.PriceExchangeServiceBlockingStub stub
                = PriceExchangeServiceGrpc.newBlockingStub(channel);

        Iterator<Event> price = stub.getPrice(FindOneRequest.newBuilder().setId(1L).build());

        while (price.hasNext()) {
            System.out.printf("Receive price from server: %s \n", price.next());
        }

    }
}
