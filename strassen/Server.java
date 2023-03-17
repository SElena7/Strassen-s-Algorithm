package mpjExpressTesting;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import mpjExpressTesting.Worker;
import mpjExpressTesting.Strassen;

public class Server implements Worker {

    public Server() {}

    public int[][] multiply(int[][] A, int[][] B)
    {
        //use the parallel code distributively  
        Strassen s = new Strassen();

        return s.multiply(A, B);
    }
    //we make connections on the server, host 127.0.0.1 and port 3000
    public static void main(String args[])
    {
        try {
            int port = Integer.parseInt(args[0]);
            System.err.println("Creating a new RMI registry at port: " + args[0]);
            Registry registry = LocateRegistry.createRegistry(port);

            Server obj = new Server();
            Worker stub = (Worker) UnicastRemoteObject.exportObject(obj, 0);

            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            registry.bind("Multiplier", stub);
            System.err.println("Server ready...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}