# gRPC Price Exchanger

## Purpose 

Create gRPC application where services will interact with each other. 

## Architecture

![grpc-arm](https://user-images.githubusercontent.com/4140597/32380618-6967a49c-c0a8-11e7-8897-e1851aba038d.png)

## Modules 

* **Money Maker** - submit prices on server 
  * submit price in random fashion from 1 to 5 sec
  * using blocking stub for simplicity
* **Server** - consume new prices and notify clients with a new prices
  * register the clients in Set  
* **Client** - consume prices and print it to output
  * using blocking stub for simplicity
* **Proto** - profobuf definition


## Proto IDL

```
syntax = "proto3";

import "google/protobuf/timestamp.proto";

option java_multiple_files = true;
package com.sergeyvolkodav.grpc.proto;

enum PriceSide {
    BACK = 0;
    LAY = 1;
}

enum Currency {
    US = 0;
    EUR = 1;
    GBR = 2;
}

message Event {
    int64 id = 1;
    string name = 2;
    repeated Market market = 3;
    google.protobuf.Timestamp timestamp = 4;
}

message Market {
    int64 id = 1;
    string name = 2;
    repeated Price prices = 3;
}

message Price {
    int64 id = 1;
    int64 price = 2;
    PriceSide side = 3;
    Currency currency = 4;
}

message AddResponse {
    int64 id = 1;
}

message FindOneRequest {
    int64 id = 1;
}

service PriceExchangeService {
    rpc addPrice (stream Event) returns (AddResponse);
    rpc getPrice (FindOneRequest) returns (stream Event);
}


```
