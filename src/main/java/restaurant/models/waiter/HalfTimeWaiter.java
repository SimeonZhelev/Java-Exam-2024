package restaurant.models.waiter;

public class HalfTimeWaiter extends BaseWaiter {

    private static int EFFICIENCY = 4;

    public HalfTimeWaiter(String name) {
        super(name, EFFICIENCY);
    }

    @Override
    public void work() {
        EFFICIENCY = Math.max(0,EFFICIENCY -2);
    }
}
