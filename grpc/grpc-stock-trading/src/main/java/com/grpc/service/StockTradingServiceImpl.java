package com.grpc.service;

import com.grpc.StockRequest;
import com.grpc.StockResponse;
import com.grpc.StockTradingServiceGrpc;
import com.grpc.entity.Stock;
import com.grpc.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*instead of using normal spring service, we've to use GrpcService below*/
@GrpcService
@RequiredArgsConstructor
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockRepository stockRepository;

    /*In the proto file we've only given one param StockRequest, other param of type StreamObserver is added by grpc
     * and it does the magic, you can observe even the method is void, void doesn't return any value, then how can we actually emit response
     * to client, StreamObserver has some useful methods, which does it */
    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
//        stockName -> Db -> map response -> return
        String stockSymbol = request.getStockSymbol();
//        Stock stockEntity = stockRepository.findByStockSymbol(stockSymbol);
        Stock stockEntity = new Stock();

        /*StockResponse stockResponse = StockResponse.newBuilder()
                .setStockSymbol(request.getStockSymbol())
                .setPrice(stockEntity.getPrice())
                .setTimestamp(stockEntity.getLastUpdated().toString()).build();*/

        StockResponse stockResponse = StockResponse.newBuilder()
                .setStockSymbol("request.getStockSymbol()")
                .setPrice(10.0)
                .setTimestamp("stockEntity.getLastUpdated().toString()").build();

//        setting response
        responseObserver.onNext(stockResponse);
//        telling that action is completed
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {

        String stockSymbol = request.getStockSymbol();
        try {
            /*simulating streaming by doing a loop for 10 times*/
            for (int i = 0; i < 10; i++) {
                StockResponse stockResponse = StockResponse.newBuilder()
                        .setStockSymbol(stockSymbol)
                        .setPrice(new Random().nextDouble())
                        .setTimestamp(Instant.now().toString())
                        .build();
                responseObserver.onNext(stockResponse);
                TimeUnit.SECONDS.sleep(1);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

}
