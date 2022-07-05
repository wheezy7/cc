public class test8 {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1,5);

        new Thread(() -> {
            waitNotify.print("a",1,2);

        },"a线程").start();

        new Thread(() -> {
            waitNotify.print("b",2,3);

        },"b线程").start();

        new Thread(() -> {
            waitNotify.print("c",3,1);

        },"c线程").start();
    }
}

class WaitNotify {
    private int flag;
    private int loopNumber;

    /*打印*/
    public void print(String str ,int waitFlag , int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (waitFlag != this.flag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                this.flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getLoopNumber() {
        return loopNumber;
    }

    public void setLoopNumber(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}
