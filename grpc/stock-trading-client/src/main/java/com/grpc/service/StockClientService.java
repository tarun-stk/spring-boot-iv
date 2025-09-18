package com.grpc.service;

import com.grpc.StockRequest;
import com.grpc.StockResponse;
import com.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")
//    below is used when working with unary rpc
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;
    @GrpcClient("stockService")
    //    below is used when working with server streaming rpc
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol("AML").build();
        return stockTradingServiceBlockingStub.getStockPrice(request);
    }

    public void subscribeStockPrice(String symbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(symbol)
                .build();
        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("Stock Price Update: " + stockResponse.getStockSymbol() +
                        " Price: " + stockResponse.getPrice() + " " +
                        " Time: " + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error : " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("stock price stream live update completed !");
            }
        });
    }

}
