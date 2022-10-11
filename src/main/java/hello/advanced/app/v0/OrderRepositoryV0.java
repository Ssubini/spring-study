package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

// 일반적으로 상품을 저장하는데 1초 정도 걸린다고 하고, 저장하는 레포지토리
// 상품 id가 'ex'로 넘오온다면 예외가 발생해서 터져나감

public class OrderRepositoryV0 {
    public void save(String itemId){
        // 저장로직
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외발생!");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
