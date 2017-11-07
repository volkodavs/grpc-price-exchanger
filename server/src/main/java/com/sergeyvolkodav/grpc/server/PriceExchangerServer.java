package com.sergeyvolkodav.grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

public class PriceExchangerServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server grpcServer = NettyServerBuilder.forPort(8090)
                .addService(new BetService())
                .addService(new EventService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(grpcServer::shutdown));
        grpcServer.awaitTermination();
    }
}
