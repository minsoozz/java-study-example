package excel;

import java.util.ArrayList;
import java.util.List;

public class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {
    private int value = 0;
    private String name;
    private List<Subscriber> subscribers = new ArrayList<>();

    public SimpleCell(String name) {
        this.name = name;
    }

    /**
     * 새로운 값이 있음을 모든 구독자에게 알리는 메서드
     */
    private void notifyAllSubscribers() {
        subscribers.forEach(subscriber -> subscriber.onNext(value));
    }

    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void onNext(Integer item) {
        this.value = item;
        System.out.println(this.name + ":" + this.value); // 값을 콘솔로 출력하지만 실제로는 UI의 셀을 갱신할 수 있음
        notifyAllSubscribers();
    }
}
