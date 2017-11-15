package com.sergeyvolkodav.grpc.server;

import java.io.IOException;

import com.sergeyvolkodav.grpc.server.services.AuthService;
import com.sergeyvolkodav.grpc.server.interceptor.JwtServerInterceptor;
import com.sergeyvolkodav.grpc.server.services.BetService;
import com.sergeyvolkodav.grpc.server.services.EventService;
import io.grpc.Server;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;

public class PriceExchangerServer {

    private static String JWT_SECRET = "password";

    public static void main(String[] args) throws InterruptedException, IOException {
        JwtServerInterceptor jwtInterceptor = new JwtServerInterceptor(JWT_SECRET);

        Server grpcServer = NettyServerBuilder.forPort(8090)
                .addService(ServerInterceptors.intercept(new BetService(), jwtInterceptor))
                .addService(new EventService())
                .addService(new AuthService(JWT_SECRET))
                .build()
                .start();

        System.out.println("Server started!");
        Runtime.getRuntime().addShutdownHook(new Thread(grpcServer::shutdown));
        grpcServer.awaitTermination();
    }
}
