package restaurant.core;

import restaurant.common.ConstantMessages;
import restaurant.common.ExceptionMessages;
import restaurant.models.client.Client;
import restaurant.models.client.ClientImpl;
import restaurant.models.waiter.FullTimeWaiter;
import restaurant.models.waiter.HalfTimeWaiter;
import restaurant.models.waiter.Waiter;
import restaurant.models.working.Working;
import restaurant.models.working.WorkingImpl;
import restaurant.repositories.ClientRepository;
import restaurant.repositories.WaiterRepository;

import java.util.ArrayList;
import java.util.List;

public class ControllerImpl implements Controller{

    private WaiterRepository waiterRepository;
    private ClientRepository clientRepository;
    private int servedClients;

    public ControllerImpl() {
        waiterRepository = new WaiterRepository();
        clientRepository = new ClientRepository();
        servedClients = 0;
    }

    @Override
    public String addWaiter(String type, String waiterName) {
        Waiter waiter;
        switch (type){
            case "FullTimeWaiter":
                waiter = new FullTimeWaiter(waiterName);
                break;
            case "HalfTimeWaiter":
                waiter = new HalfTimeWaiter(waiterName);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.WAITER_INVALID_TYPE);
        }
        waiterRepository.add(waiter);
        return String.format(ConstantMessages.WAITER_ADDED,type,waiterName);
    }

    @Override
    public String addClient(String clientName, String... orders) {
        Client client = new ClientImpl(clientName);
        for (String order : orders) {
            client.getClientOrders().add(order);
        }
        clientRepository.add(client);
        return String.format(ConstantMessages.CLIENT_ADDED, clientName);
    }

    @Override
    public String removeWaiter(String waiterName) {
        Waiter waiterToRemove = waiterRepository.byName(waiterName);
        if (waiterToRemove == null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.WAITER_DOES_NOT_EXIST,waiterName));
        }
        waiterRepository.remove(waiterToRemove);
        return String.format(ConstantMessages.WAITER_REMOVE,waiterName);
    }

    @Override
    public String removeClient(String clientName) {
        Client clientToRemove = clientRepository.byName(clientName);
        if (clientToRemove == null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.CLIENT_DOES_NOT_EXIST,clientName));
        }
        clientRepository.remove(clientToRemove);
        return String.format(ConstantMessages.CLIENT_REMOVE,clientName);
    }

    @Override
    public String startWorking(String clientName) {
        List<Waiter> waiters = new ArrayList<>(waiterRepository.getCollection());

        if (waiters.isEmpty()){
            throw new IllegalArgumentException(ExceptionMessages.THERE_ARE_NO_WAITERS);
        }

        Client client = new ClientImpl(clientName);
        Working working = new WorkingImpl();
        working.takingOrders(client,waiters);
        servedClients++;
        return String.format(ConstantMessages.ORDERS_SERVING,clientName);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(ConstantMessages.FINAL_CLIENTS_COUNT,servedClients));
        sb.append(System.lineSeparator());
        sb.append(ConstantMessages.FINAL_WAITERS_STATISTICS);
        sb.append(System.lineSeparator());

        for (Waiter waiter : waiterRepository.getCollection()) {
            sb.append(String.format(ConstantMessages.FINAL_WAITER_NAME, waiter.getName())).append(System.lineSeparator());
            sb.append(String.format(ConstantMessages.FINAL_WAITER_EFFICIENCY, waiter.getEfficiency())).append(System.lineSeparator());
            if (waiter.takenOrders().getOrdersList().isEmpty()) {
                sb.append(String.format(ConstantMessages.FINAL_WAITER_ORDERS, "None")).append(System.lineSeparator());
            } else {
                sb.append(String.format(ConstantMessages.FINAL_WAITER_ORDERS, String.join(ConstantMessages.FINAL_WAITER_ORDERS_DELIMITER, waiter.takenOrders().getOrdersList()))).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}