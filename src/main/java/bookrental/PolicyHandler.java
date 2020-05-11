package bookrental;

import bookrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    StockRepository stockRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequested_Checkstock(@Payload Requested requested){

        if(requested.getEventType().equals("requested")) {
            System.out.println("=============================");
            System.out.println("requested");
            Stock stock = new Stock();
            long qty = stock.getQty();

            if(qty > 1){
                stockRepository.save(stock);
                stock.setBookid(requested.getBookid());
                stock.setQty(qty-1);
                System.out.println("set Stock -1");
            } else {
                System.out.println("stock-out");
            }
            System.out.println("=============================");
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceled_Cancelstock(@Payload Canceled canceled){

        if(canceled.getEventType().equals("canceled")) {
            System.out.println("=============================");
            System.out.println("canceled");
            Stock stock = new Stock();
            long qty = stock.getQty();

            stock.setQty(qty+1);
            System.out.println("set Stock +1");
            System.out.println("=============================");
        }
    }
}
