package restaurant.models.waiter;

public class FullTimeWaiter extends BaseWaiter {

    private final static int EFFICIENCY = 8;

    public FullTimeWaiter(String name) {
        super(name, EFFICIENCY);
    }
}
