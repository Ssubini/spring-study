package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id;
    private int level;

    public TraceId(){
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        // UUID 이렇게 하면 너무 값이 길게나오니깐 첫 - 전까지 끊어서 쓸거임
        // ab99e16f-3cde-4d24-8241-256108c203a2 // 생성된 UUID -> 앞 8자리만 사용
        return UUID.randomUUID().toString().substring(0,8);
    }

    // 다음 TraceId를 편하게 만들기
    // 명세서 확인해보면 아이디 같을 때 레벨이 달라지는 부분 확인 가능한데 이거 표현해주는거임
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    // 첫 번째 레벨 여부를 편리하게 확인할 수 있는 메서드
    public boolean isFirstLevel(){
        return level == 0;
    }


    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
