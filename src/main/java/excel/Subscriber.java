package excel;

public interface Subscriber<T> {
    void onNext(T t);
}
