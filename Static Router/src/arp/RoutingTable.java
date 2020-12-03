package staticRouting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RoutingTable {
    private ArrayList<RoutingRow> table = new ArrayList<>();

    public RoutingRow getRoutingTableRow(byte[] destination, byte[] netmask, byte[] gateway, boolean[] flags, String interfaceName, int metric){
        return new RoutingRow(destination, netmask, gateway, flags, interfaceName, metric);
    }
    
    public void add(RoutingRow r){
        table.add(r);
//      Sort by Netmask -> Longest Prefix
        Collections.sort(table);
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
        byte[] bytes = new byte[4];

//        맨 위부터 찾아서 걸리면 그게 longest prefix임
        for(RoutingRow row : table){
            for (int i = 0; i < 4; i++) {
                bytes[i] = (byte) (row.getNetmask()[i] & destination[i]);
            }
            if(Arrays.equals(bytes, row.destination)) {
                return row;
            }
        }
        return table.get(table.size() - 1);
    }


    public class RoutingRow implements Comparable<RoutingRow>{
        private byte[] destination;
        private byte[] netmask;
        private int CountNetmask;
        private byte[] gateway;
        private boolean[] flags; //flags[0] = Up, flags[1] = Gateway, flags[2] = Host
        private String interfaceName;
        int metric;

//        Comparable
//        Sort by Netmask -> Longest Prefix
        @Override
        public int compareTo(RoutingRow row) {
            return Integer.compare(row.getCountNetmask(), this.getCountNetmask());
        }

//        Lonest Prefix를 위한 1의 갯수
//        거꾸로 연산했지만... 문제 없을듯?
        public int CountOne(byte[] netmask){
            int count = 0;
            for(byte i : netmask){
                System.out.print(i);
                for(int q = 0; q<8; q ++){
                    if ((i & 1) == 1){
                        count ++;

                    }
                    i = (byte) (i >> 1);
                }
            }
            return count;
        }

//        Constructor
        public RoutingRow(byte[] destination, byte[] netmask, byte[] gateway, boolean[] flags, String interfaceName, int metric) {
            this.destination = destination;
            this.netmask = netmask;
            this.CountNetmask = CountOne(netmask);
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

//        Mask Getter
        public byte[] getNetmask() {
            return netmask;
        }

        public void setNetmask(byte[] netmask) {
            this.netmask = netmask;
            this.CountNetmask = CountOne(netmask);
        }

//        mask counter getter
        public int getCountNetmask() {
            return CountNetmask;
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