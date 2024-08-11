import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1090);
            Calculator calculator = (Calculator) registry.lookup("Calculator");
            //Creating Random Unique Clientid
            byte[] str_arr = new byte[5];
            new Random().nextBytes(str_arr);
            String clientId = new String(str_arr);
            System.out.println("Client ID: " + clientId);

            // Creating a stack for this client(specific client)
            calculator.client_stack(clientId);

            // Testing the calculator
            System.out.println("Is empty: " + calculator.isEmpty(clientId));

            calculator.pushValue(clientId, 81);
            calculator.pushValue(clientId, 18);
            calculator.pushOperation(clientId, "min");
            System.out.println("Min: " + calculator.pop(clientId));

            calculator.pushValue(clientId, 89);
            calculator.pushValue(clientId, 98);
            calculator.pushOperation(clientId, "max");
            System.out.println("Max: " + calculator.pop(clientId));

            calculator.pushValue(clientId, 12);
            calculator.pushValue(clientId, 18);
            calculator.pushOperation(clientId, "lcm");
            System.out.println("LCM: " + calculator.pop(clientId));

            calculator.pushValue(clientId, 48);
            calculator.pushValue(clientId, 18);
            calculator.pushOperation(clientId, "gcd");
            System.out.println("GCD: " + calculator.pop(clientId));

            System.out.println("Delayed pop (3 seconds):");
            calculator.pushValue(clientId, 100);
            System.out.println("Result: " + calculator.delayPop(clientId, 3000));

            System.out.println("Is empty: " + calculator.isEmpty(clientId));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}