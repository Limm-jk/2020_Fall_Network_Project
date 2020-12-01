package arp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoutingTable {
    private ArrayList<RoutingRow> table = new ArrayList<>();

    public void add(RoutingRow r){
        table.add(r);
    }
    public RoutingRow FindRow(byte[] destination){
        RoutingRow ans = null;

        for (int i = 0; i < table.size(); i++) {
            RoutingRow row = table.get(i);
            if(Arrays.equals(destination, row.destination)){
                ans = row;
            }
        }
        return ans;
    }
    public void remove(byte[] destination){
        RoutingRow routingRow;
        for (int i = 0; i < table.size(); i++) {
            routingRow = table.get(i);
            if(Arrays.equals(destination, routingRow.destination)){
                table.remove(i);
                break;
            }
        }
    }

    public RoutingRow Routing(byte[] destination){
        //flags[0] = Up, flags[1] = Gateway, flags[2] = Host
        if(table.size() == 0) return null;

        return null;
    }

    public class RoutingRow{
        private byte[] destination;
        private int netmask;
        private byte[] gateway;
        private boolean[] flags; //flags[0] = Up, flags[1] = Gateway, flags[2] = Host
        private String interfaceName;
        int metric;

//        Constructor
        public RoutingRow(byte[] destination, int netmask, byte[] gateway, boolean[] flags, String interfaceName, int metric) {
            this.destination = destination;
            this.netmask = netmask;
            this.gateway = gateway;
            this.flags = flags;
            this.interfaceName = interfaceName;
            this.metric = metric;
        }

        //        Getter & Setter
//        Des Getter
        public byte[] getDestination() {
            return destination;
        }

        public void setDestination(byte[] destination) {
            this.destination = destination;
        }

//        Make Getter
        public int getNetmask() {
            return netmask;
        }

        public void setNetmask(int netmask) {
            this.netmask = netmask;
        }

//        Gate getter
        public byte[] getGateway() {
            return gateway;
        }

        public void setGateway(byte[] gateway) {
            this.gateway = gateway;
        }

//        Flag Getter
        public boolean[] getFlags() {
            return flags;
        }

        public void setFlags(boolean[] flags) {
            this.flags = flags;
        }

//        interface Getter
        public String getInterfaceName() {
            return interfaceName;
        }

        public void setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
        }




    }
}
