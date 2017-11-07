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

## Pros and Cons


| Pros          | Cons| 
| ------------- |-------------|
| Very fast streaming secure 362,788 QPS on 8 cores client & server (6.11.2017) | Adoption by external customer?  | 
| Netty async non blocking IO  | HAProxy balancing is hacky (do you need it?) | 
| Bi-direction streaming with TCP multiplexing      | |
| Deadline propagation | | 
| Cancelation propagation | |
| Flow control || 
| Support distributed tracing: Open Tracing, Zipkin || 
| gRPC monitoring: prometheus ||
| SSL/TLS, token based auth with Google, auth API ||
| Build in strategy for service discovery & load balancing || 
| Support more than 10+ languages ||
| Build in testing support||
| Based on HTTP/2|| 
| Use protobuf by default||
| Interface definition language (IDL) || 
| Growing ecosystem ||


## How to run

### Observe Events

* **Start server** ```mvn exec:java@event-client```
* **Start client** ```mvn exec:java@event-client```


## References

* https://performance-dot-grpc-testing.appspot.com/explore?dashboard=5636470266134528
