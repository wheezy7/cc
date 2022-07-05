import java.util.ArrayList;

public class test2 {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;
    public static void main(String[] args) {
        ThreadSafeSubClass test = new ThreadSafeSubClass();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + i).start();
        }
    }
}
class ThreadUnsafe {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
// { 临界区, 会产生竞态条件
            method2(list);
            method3(list);
// } 临界区
        }
    }
    public void method2( ArrayList<String> list) {
        list.add("1");
    }
    public void method3( ArrayList<String> list) {
        list.remove(0);
    }
}
class ThreadSafeSubClass extends ThreadUnsafe {
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}