package com.sergeyvolkodav.grpc.moneymaker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.protobuf.Timestamp;
import com.sergeyvolkodav.grpc.proto.Currency;
import com.sergeyvolkodav.grpc.proto.Event;
import com.sergeyvolkodav.grpc.proto.Market;
import com.sergeyvolkodav.grpc.proto.Price;
import com.sergeyvolkodav.grpc.proto.PriceExchangeServiceGrpc;
import com.sergeyvolkodav.grpc.proto.PriceSide;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PriceExchangeMoneyMaker {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext(true)
                .build();

        PriceExchangeServiceGrpc.PriceExchangeServiceBlockingStub stub
                = PriceExchangeServiceGrpc.newBlockingStub(channel);

        PriceExchangeMoneyMaker priceExchangeClient = new PriceExchangeMoneyMaker();

        while (true) {

            Event event = priceExchangeClient.buildEvent();
            stub.addPrice(event);

            System.out.println("Message successfully sent");
            Thread.sleep(ThreadLocalRandom.current().nextLong(5000, 5100));
        }
    }

    private Event buildEvent() {
        return Event.newBuilder().setId(1L)
                .setName("Wizards vs Rockets")
                .setTimestamp(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000))
                .addAllMarket(buildMarkets())
                .build();
    }

    private List<Market> buildMarkets() {
        List<Market> markets = new ArrayList<>();

        Market market = Market.newBuilder()
                .setId(1L)
                .setName("WIN")
                .addAllPrices(buildRandomPrice())
                .build();

        markets.add(market);
        return markets;
    }

    private List<Price> buildRandomPrice() {
        List<Price> prices = new ArrayList<>();
        Price price = Price.newBuilder()
                .setId(1L)
                .setPrice(ThreadLocalRandom.current().nextLong(0, 200_000))
                .setSide(PriceSide.BACK)
                .setCurrency(Currency.EUR)
                .build();
        prices.add(price);
        return prices;
    }
}
