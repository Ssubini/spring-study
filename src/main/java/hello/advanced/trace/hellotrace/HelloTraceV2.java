package hello.advanced.trace.hellotrace;
import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component // 싱글턴으로 등록해서 쓸 목적?? 이게 무슨말이징??
public class HelloTraceV2 {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";


    // 로그 시작할 때 호출 -> 처음에는 얘를 호출
    public TraceStatus begin(String message){
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();

        // 로그 출력
        // traceId , addSpace : 화살표 같은거(레벨 들어가면 더 많이 들어가고...), 메시지
        // ex> [796bccd9] | |-->OrderRepository.save()//트랜잭션ID:796bccd9, level:2
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2에서 추가 -> 두 번째 부터는 얘를 호출
    public TraceStatus beginSync(TraceId beforeTraceId, String message){
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);

        return new TraceStatus(nextId, startTimeMs, message);
    }

    // 로그 종료할 때 호출
    public void end(TraceStatus status){
        complete(status,null);
    }


    // 예외가 터져서 로그에 남을 때
    public void exception(TraceStatus status, Exception e){
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e){
        Long stopTimeMs = System.currentTimeMillis(); // 종료 시간
        long resultTimeMs = stopTimeMs - status.getStartTimeMs(); // 총 소요시간 = 종료시간 - 시작시간
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex = {}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }

    // addSpace 예시
    // level = 0 :
    // level = 1 :      |-->
    // level = 2 :      |   |-->
    // level = 2 ex :   |   |<X-
    // level = 1 ex :   |<X-
    private static String addSpace(String prefix, int level){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++){
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }


}
