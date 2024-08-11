import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private Map<String, Stack<Integer>> clientStacks;

    public CalculatorImplementation() throws RemoteException {
        super();
        clientStacks = new HashMap<>();
    }

    @Override
    public void client_stack(String clientId) throws RemoteException {
        clientStacks.putIfAbsent(clientId, new Stack<>());
    }

    @Override
    public void pushValue(String clientId, int val) throws RemoteException {
        ensureClientStackExists(clientId);
        clientStacks.get(clientId).push(val);
    }

    @Override
    public void pushOperation(String clientId, String operator) throws RemoteException {
        ensureClientStackExists(clientId);
        Stack<Integer> stack = clientStacks.get(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty for client " + clientId);
        }

        Stack<Integer> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            tempStack.push(stack.pop());
        }

        int result = tempStack.pop();
        while (!tempStack.isEmpty()) {
            int value = tempStack.pop();
            switch (operator) {
                case "min":
                    result = Math.min(result, value);
                    break;
                case "max":
                    result = Math.max(result, value);
                    break;
                case "lcm":
                    result = lcm(result, value);
                    break;
                case "gcd":
                    result = gcd(result, value);
                    break;
                default:
                    throw new RemoteException("Invalid operator");
            }
        }
        stack.push(result);
    }

    @Override
    public int pop(String clientId) throws RemoteException {
        ensureClientStackExists(clientId);
        Stack<Integer> stack = clientStacks.get(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty for client " + clientId);
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty(String clientId) throws RemoteException {
        ensureClientStackExists(clientId);
        return clientStacks.get(clientId).isEmpty();
    }

    @Override
    public int delayPop(String clientId, int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RemoteException("Interrupted during delay", e);
        }
        return pop(clientId);
    }
    //Check if there is a seprates stack for each client 
    private void ensureClientStackExists(String clientId) throws RemoteException {
        if (!clientStacks.containsKey(clientId)) {
            throw new RemoteException("Client stack does not exist for " + clientId);
        }
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }
}