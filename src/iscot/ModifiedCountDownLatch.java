package iscot;

public class ModifiedCountDownLatch {

    private final int MAX;
    private int count;
    
    public ModifiedCountDownLatch(int count) {
        if(count <= 0) throw new IllegalArgumentException("[ModifiedCountDownLatch]: 'count' must be greater than 0");
        this.MAX = count;
        this.count = count;
    }

    public synchronized void countdown(){ 
        count--;
        if(count == 0){
            notifyAll();
        }
        
    }

    public synchronized void await(int waitPeriod) throws InterruptedException {
        while(count > 0){
            wait(waitPeriod);
        }    
        
    }
}
