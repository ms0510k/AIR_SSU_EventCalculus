import py4j.GatewayServer;

public class StackEntryPoint {

    private MainDriver reteEngine;

    public StackEntryPoint() {

        reteEngine = new MainDriver();
    }

    public MainDriver getreteEngine() {
        return reteEngine;
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new StackEntryPoint(), 21000);
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

}