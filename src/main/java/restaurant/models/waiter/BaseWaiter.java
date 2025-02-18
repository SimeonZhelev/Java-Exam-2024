package restaurant.models.waiter;

import restaurant.common.ExceptionMessages;
import restaurant.models.orders.TakenOrders;
import restaurant.models.orders.TakenOrdersImpl;

public abstract class BaseWaiter implements Waiter{

    private String name;
    private int efficiency;
    private TakenOrders takenOrders;

    protected BaseWaiter(String name, int efficiency) {
        setName(name);
        setEfficiency(efficiency);
        takenOrders = new TakenOrdersImpl();
    }

    private void setName(String name) {
        if(name == null || name.trim().isEmpty()){
            throw new NullPointerException(ExceptionMessages.WAITER_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    private void setEfficiency(int efficiency) {
        if (efficiency < 0){
            throw new IllegalArgumentException(ExceptionMessages.WAITER_EFFICIENCY_LESS_THAN_ZERO);
        }
        this.efficiency = efficiency;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getEfficiency() {
        return this.efficiency;
    }

    @Override
    public boolean canWork() {
        return this.efficiency > 0;
    }

    @Override
    public TakenOrders takenOrders() {
        return takenOrders;
    }

    @Override
    public void work() {
        this.efficiency = Math.max(0, efficiency - 1);
    }
}
