package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

// 일반적으로 상품을 저장하는데 1초 정도 걸린다고 하고, 저장하는 레포지토리
// 상품 id가 'ex'로 넘오온다면 예외가 발생해서 터져나감

public class OrderRepositoryV1 {

    private final HelloTraceV1 trace;

    public void save(String itemId){

        TraceStatus status = null;

        try{
            status = trace.begin("OrderRepository.request()");
            // 저장로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외발생!");
            }
            sleep(1000);
            trace.end(status);
        } catch(Exception e) {
            trace.exception(status, e); // 예외를 여기서 먹어버림
            throw e; // 예외를 꼭 다시 던져주어야 한다.
        }


    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
