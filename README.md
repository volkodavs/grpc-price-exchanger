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
