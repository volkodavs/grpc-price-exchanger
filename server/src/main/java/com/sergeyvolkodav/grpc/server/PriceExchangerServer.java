package com.sergeyvolkodav.grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PriceExchangerServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder
                .forPort(9999)
                .addService(new PriceExchangeServiceImpl())
                .build();
        server.start();
        server.awaitTermination();
    }
}
