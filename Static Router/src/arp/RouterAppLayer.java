package arp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class RouterAppLayer extends JFrame{
    public int nUpperLayerCount = 0;
    public String pLayerName = null;

    private JPanel contentPane;
    private JTextField GARPTextField;
    private JButton RoutingAdd;
    private JButton RoutingDelete;
    private JTable Route_Table;
    private JTable ARPTable;
    private JTable ProxyTable;
    private JButton ProxyAdd;
    private JButton ProxyDelete;
    private JButton ArpDelete;
    private JTextField IPTextField;
    private JButton ARP_IPSend;
    private JButton GARPSend;

    private DefaultTableModel arpModel, proxyModel, routingModel;
    String arpModelHeader[] = {"IpAddress", "MacAddress", "Status"};
    String[][] arpModelContents = new String[0][3];
    String proxyModelHeader[] = {"IpAddress", "MacAddress"};
    String[][] proxyModelContents = new String[0][2];
    String routingModelHeader[] = {"Destination", "Netmask", "Gateway", "Flag", "Interface"};
    String[][] routingModelContents = new String[0][5];

    // proxy window
    private JTextField proxyEntryIp;
    private JTextField proxyEntryMac;


    static int adapterNumber = 0;

    public static void main(String[] args) throws IOException {

        RouterAppLayer routerAppLayer = new RouterAppLayer("GUI");

    }


    public RouterAppLayer(String pName) throws IOException {
        pLayerName = pName;

        setTitle("Routing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        /*-----Routing Panel-----*/
        JPanel RoutingPanel = new JPanel();
        RoutingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Routing Table", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        RoutingPanel.setToolTipText("");
        RoutingPanel.setBounds(12, 23, 436, 360);
        contentPane.add(RoutingPanel);
        RoutingPanel.setLayout(null);

        routingModel = new DefaultTableModel(routingModelContents, routingModelHeader);
        Route_Table = new JTable(routingModel);
        Route_Table.setBounds(12, 20, 412, 266);
        RoutingPanel.add(Route_Table);

        RoutingAdd = new JButton("Add");
        RoutingAdd.setBounds(26, 300, 146, 38);
        RoutingAdd.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RoutingWindow();
            }}));
        RoutingPanel.add(RoutingAdd);

        RoutingDelete = new JButton("Delete");
        RoutingDelete.setBounds(254, 300, 146, 38);
//            RoutingDelete.addActionListener(new btnListener());
        RoutingPanel.add(RoutingDelete);


        /*-----ARP Panel-----*/
        JPanel ARPPanel = new JPanel();
        ARPPanel.setBorder(new TitledBorder(null, "ARP Cache Table", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        ARPPanel.setBounds(458, 23, 414, 180);
        contentPane.add(ARPPanel);
        ARPPanel.setLayout(null);

        arpModel = new DefaultTableModel(arpModelContents, arpModelHeader);
        ARPTable = new JTable(arpModel);
        ARPTable.setBounds(12, 21, 390, 111);
        ARPPanel.add(ARPTable);

        ArpDelete = new JButton("Delete");
        ArpDelete.setBounds(139, 139, 136, 30);
//            ProxyDelete.addActionListener(new btnListener());
        ARPPanel.add(ArpDelete);

        /*-----Proxy Panel-----*/
        JPanel ProxyPanel = new JPanel();
        ProxyPanel.setBorder(new TitledBorder(null, "Proxy ARP Table", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        ProxyPanel.setBounds(458, 203, 414, 180);
        contentPane.add(ProxyPanel);
        ProxyPanel.setLayout(null);

        proxyModel = new DefaultTableModel(proxyModelContents, proxyModelHeader);
        ProxyTable = new JTable(proxyModel);
        ProxyTable.setBounds(12, 21, 390, 111);
        ProxyPanel.add(ProxyTable);

        ProxyAdd = new JButton("Add");
        ProxyAdd.setBounds(32, 139, 136, 30);
        ProxyAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new proxyWindow();
            }});
        ProxyPanel.add(ProxyAdd);

        ProxyDelete = new JButton("Delete");
        ProxyDelete.setBounds(242, 139, 136, 30);
//            ProxyDelete.addActionListener(new btnListener());
        ProxyPanel.add(ProxyDelete);

        /*-----Exit / Cancel-----*/
        JButton btnEnd = new JButton("Exit");
        btnEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnEnd.setBounds(334, 396, 114, 42);
        contentPane.add(btnEnd);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnCancel.setBounds(458, 396, 114, 42);
        contentPane.add(btnCancel);

        setVisible(true);
    }

    class proxyWindow extends JFrame{
        public proxyWindow() {
            setTitle("Proxy ARP");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 492, 295);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(null);

            JLabel ProxyEntryIPLabel = new JLabel("IP Input");
            ProxyEntryIPLabel.setFont(new Font("font", Font.BOLD, 20));
            ProxyEntryIPLabel.setBounds(86, 64, 74, 34);
            contentPane.add(ProxyEntryIPLabel);

            JLabel proxyEntryEthernetLabel = new JLabel("Ethernet Input");
            proxyEntryEthernetLabel.setFont(new Font("font", Font.BOLD, 20));
            proxyEntryEthernetLabel.setBounds(31, 112, 129, 34);
            contentPane.add(proxyEntryEthernetLabel);

            proxyEntryIp = new JTextField();
            proxyEntryIp.setBounds(172, 64, 223, 30);
            contentPane.add(proxyEntryIp);
            proxyEntryIp.setColumns(10);

            proxyEntryMac = new JTextField();
            proxyEntryMac.setColumns(10);
            proxyEntryMac.setBounds(172, 117, 223, 30);
            contentPane.add(proxyEntryMac);

            JButton proxyEntryAddBtn = new JButton("Add");
            proxyEntryAddBtn.setBounds(104, 191, 97, 23);
            contentPane.add(proxyEntryAddBtn);
            proxyEntryAddBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //entry
                    String inputIp = proxyEntryIp.getText().trim();
                    String inputMac = proxyEntryMac.getText().trim();

                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getByName(inputIp);
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                    byte[] byteIp = ip.getAddress();

//                        byte[] byteMac = macStringToByte(inputMac);
//                        ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//
//                        arpLayer.addProxy(byteIp, byteMac);

                    setVisible(false);
                }
            });

            JButton proxyEntryCancelBtn = new JButton("Cancel");
            proxyEntryCancelBtn.setBounds(266, 191, 97, 23);
            contentPane.add(proxyEntryCancelBtn);
            proxyEntryCancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });

            setVisible(true);
        }
    }

    class RoutingWindow extends JFrame{
        public RoutingWindow() {
            setTitle("Routing Table Add");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 442, 325);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(null);

            JLabel DestinationLabel = new JLabel("Destination");
            DestinationLabel.setFont(new Font("font", Font.BOLD, 20));
            DestinationLabel.setBounds(20, 20, 200, 34);
            contentPane.add(DestinationLabel);

            proxyEntryIp = new JTextField();
            proxyEntryIp.setBounds(172, 22, 223, 30);
            contentPane.add(proxyEntryIp);
            proxyEntryIp.setColumns(10);

            JLabel NetmaskLabel = new JLabel("Netmask");
            NetmaskLabel.setFont(new Font("font", Font.BOLD, 20));
            NetmaskLabel.setBounds(20, 60, 200, 34);
            contentPane.add(NetmaskLabel);

            proxyEntryMac = new JTextField();
            proxyEntryMac.setColumns(10);
            proxyEntryMac.setBounds(172, 62, 223, 30);
            contentPane.add(proxyEntryMac);

            JLabel GatewayLabel = new JLabel("Gateway");
            GatewayLabel.setFont(new Font("font", Font.BOLD, 20));
            GatewayLabel.setBounds(20, 100, 200, 34);
            contentPane.add(GatewayLabel);

            proxyEntryIp = new JTextField();
            proxyEntryIp.setBounds(172, 102, 223, 30);
            contentPane.add(proxyEntryIp);
            proxyEntryIp.setColumns(10);

            JLabel FlagLabel = new JLabel("Flag");
            FlagLabel.setFont(new Font("font", Font.BOLD, 20));
            FlagLabel.setBounds(20, 140, 200, 34);
            contentPane.add(FlagLabel);

            JCheckBox Up_Flag = new JCheckBox("UP");
            Up_Flag.setBounds(172, 142, 60, 30);
            contentPane.add(Up_Flag);

            JCheckBox Gate_Flag = new JCheckBox("Gateway");
            Gate_Flag.setBounds(242, 142, 90, 30);
            contentPane.add(Gate_Flag);

            JCheckBox Host_Flag = new JCheckBox("Host");
            Host_Flag.setBounds(342, 142, 70, 30);
            contentPane.add(Host_Flag);

            JLabel InterfaceLabel = new JLabel("Interface");
            InterfaceLabel.setFont(new Font("font", Font.BOLD, 20));
            InterfaceLabel.setBounds(20, 180, 200, 34);
            contentPane.add(InterfaceLabel);

            JComboBox InterfaceBox = new JComboBox();
            InterfaceBox.setBounds(172, 182, 223, 30);
            contentPane.add(InterfaceBox);
            proxyEntryIp.setColumns(10);

            JButton proxyEntryAddBtn = new JButton("Add");
            proxyEntryAddBtn.setBounds(64, 240, 97, 23);
            contentPane.add(proxyEntryAddBtn);
            proxyEntryAddBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //entry
                    String inputIp = proxyEntryIp.getText().trim();
                    String inputMac = proxyEntryMac.getText().trim();

                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getByName(inputIp);
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                    byte[] byteIp = ip.getAddress();

//                        byte[] byteMac = macStringToByte(inputMac);
//                        ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//
//                        arpLayer.addProxy(byteIp, byteMac);

                    setVisible(false);
                }
            });

            JButton proxyEntryCancelBtn = new JButton("Cancel");
            proxyEntryCancelBtn.setBounds(266, 240, 97, 23);
            contentPane.add(proxyEntryCancelBtn);
            proxyEntryCancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });

            setVisible(true);
        }
    }

//        class btnListener implements ActionListener{
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                /*----ARP Action-----*/
//                if(e.getSource() == ARP_IPSend) {
//                    //IP �엯�젰 �썑 send踰꾪듉 �닃���쓣 �븣
//                    String inputIP = IPTextField.getText().trim();
//
//                    InetAddress ip = null;
//                    try {
//                        ip = InetAddress.getByName(inputIP);
//                        byte[] dstIP = ip.getAddress();
//
//                        ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//                        arpLayer.setDstIp(dstIP);
//
//                        arpLayer.Send(new byte[0], 0);
//
//                    } catch (UnknownHostException e1) {
//                        e1.printStackTrace();
//                    }
//
//                    IPTextField.setText("");
//                }
//
//                else if(e.getSource() == ARPItemDelete) {
//                    //ItemDelete 踰꾪듉�쓣 �닃���쓣 �븣
//                    ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//                    int selectRow = ARPTable.getSelectedRow();
//                    if(selectRow == -1) {
//                        return;
//                    }
//                    else {
//                        String ipAddress = arpModel.getValueAt(selectRow, 0).toString();
//                        InetAddress ip = null;
//                        try {
//                            ip = InetAddress.getByName(ipAddress);
//                        } catch (UnknownHostException e1) {
//                            // TODO �옄�룞 �깮�꽦�맂 catch 釉붾줉
//                            e1.printStackTrace();
//                        }
//                        byte[] byteIp = ip.getAddress();
//                        arpLayer.cacheRemove(byteIp);
//                    }
//                }
//
//                else if(e.getSource() == ARPAllDelete) {
//                    //AllDelete 踰꾪듉�쓣 �닃���쓣 �븣
//                    ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//                    arpLayer.cacheRemoveAll();
//                }
//
//                /*----- Proxy Action -----*/
//                else if(e.getSource() == ProxyAdd) {
//                    //Add 踰꾪듉�쓣 �닃���쓣 �븣
//                    new proxyWindow();
//                }
//
//                else if(e.getSource() == ProxyDelete) {
//                    //Delete 踰꾪듉�쓣 �닃���쓣 �븣
//                    ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//
//                    int selectRow = ProxyTable.getSelectedRow();
//                    if(selectRow == -1) {
//                        return;
//                    }
//                    else {
//                        String ipAddress = proxyModel.getValueAt(selectRow, 0).toString();
//                        InetAddress ip = null;
//                        try {
//                            ip = InetAddress.getByName(ipAddress);
//                        } catch (UnknownHostException e1) {
//                            // TODO �옄�룞 �깮�꽦�맂 catch 釉붾줉
//                            e1.printStackTrace();
//                        }
//                        byte[] byteIp = ip.getAddress();
//                        arpLayer.proxyRemove(byteIp);
//                    }
//                }
//
//                /*----- GARP Action -----*/
//                else if(e.getSource() == GARPSend) {
//                    //GARP Send踰꾪듉�쓣 �닃���쓣 �븣
//                    byte[] mac = macStringToByte(GARPTextField.getText());
//
//                    ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
//                    EthernetLayer ethernetLayer = (EthernetLayer)m_LayerMgr.GetLayer("Ethernet");
//                    arpLayer.setSrcMac(mac);
//                    try {
//                        arpLayer.setDstIp(InetAddress.getLocalHost().getAddress());
//                    } catch (UnknownHostException e1) {
//                        e1.printStackTrace();
//                    }
//                    ethernetLayer.SetEnetSrcAddress(mac);
//
//                    // send
//                    arpLayer.Send(new byte[0], 0);
//                    GARPTextField.setText("");
//                }
//            }
//        }
//
//        public String macByteToString(byte[] byte_MacAddress) { //MAC Byte二쇱냼瑜� String�쑝濡� 蹂��솚
//            String MacAddress = "";
//            for (int i = 0; i < 6; i++) {
//                //2�옄由� 16吏꾩닔瑜� ��臾몄옄濡�, 洹몃━怨� 1�옄由� 16吏꾩닔�뒗 �븵�뿉 0�쓣 遺숈엫.
//                MacAddress += String.format("%02X%s", byte_MacAddress[i], (i < MacAddress.length() - 1) ? "" : "");
//
//                if (i != 5) {
//                    //2�옄由� 16吏꾩닔 �옄由� �떒�쐞 �뮘�뿉 "-"遺숈뿬二쇨린
//                    MacAddress += ":";
//                }
//            }
//            return MacAddress;
//        }
//
//        public byte[] macStringToByte(String mac) {
//            // string mac 二쇱냼�뒗 "00:00:00:00:00:00" �삎�깭
//            byte[] ret = new byte[6];
//
//            StringTokenizer tokens = new StringTokenizer(mac, ":");
//
//            for (int i = 0; tokens.hasMoreElements(); i++) {
//
//                String temp = tokens.nextToken();
//
//                try {
//                    ret[i] = Byte.parseByte(temp, 16);
//                } catch (NumberFormatException e) {
//                    int minus = (Integer.parseInt(temp, 16)) - 256;
//                    ret[i] = (byte) (minus);
//                }
//            }
//
//            return ret;
//        }
//
//        public String ipByteToString(byte[] stringIP) {
//            String result = "";
//            for(byte raw : stringIP){
//                result += raw & 0xFF;
//                result += ".";
//            }
//            return result.substring(0, result.length()-1);
//        }
//
//        public void updateARPCacheTable(ArrayList<ARPCache> cache_table) {
//            // GUI�뿉 cache table �뾽�뜲�씠�듃
//            //ip二쇱냼瑜� string�쑝濡� 蹂��솚 �븘�슂
//            //mac二쇱냼瑜� string�쑝濡� 蹂��솚 �븘�슂
//            //ARPTable 蹂��닔 (JTable)�쓽 row�뿉 cache table �뾽�뜲�씠�꽣
//
//            // 紐⑤뱺 �뻾 �궘�젣
//            if (arpModel.getRowCount() > 0) {
//                for (int i = arpModel.getRowCount() - 1; i > -1; i--) {
//                    arpModel.removeRow(i);
//                }
//            }
//
//            //cache_table�쓽 紐⑤뱺 �뻾 異붽�
//            Iterator<ARPCache> iter = cache_table.iterator();
//            while(iter.hasNext()) {
//                ARPCache cache = iter.next();
//                String[] row = new String[3];
//
//                row[0] = ipByteToString(cache.ip);
//                if(cache.status == false) {
//                    row[1] = "??????????";
//                    row[2] = "incomplete";
//                }
//                else {
//                    row[1] = macByteToString(cache.mac);
//                    row[2] = "complete";
//                }
//
//                arpModel.addRow(row);
//            }
//        }
//
//        public void updateProxyEntry(ArrayList<Proxy> proxyEntry) {
//            // GUI�뿉 proxy Entry �뾽�뜲�씠�듃
//            // 紐⑤뱺 �뻾 �궘�젣
//            if (proxyModel.getRowCount() > 0) {
//                for (int i = proxyModel.getRowCount() - 1; i > -1; i--) {
//                    proxyModel.removeRow(i);
//                }
//            }
//
//            Iterator<Proxy> iter = proxyEntry.iterator();
//            while(iter.hasNext()) {
//                Proxy proxy = iter.next();
//                String[] row = new String[2];
//
//                row[0] = ipByteToString(proxy.ip);
//                row[1] = macByteToString(proxy.mac);
//
//                proxyModel.addRow(row);
//            }
//        }
//
//        @Override
//        public void SetUnderLayer(BaseLayer pUnderLayer) {
//            // TODO Auto-generated method stub
//            if (pUnderLayer == null)
//                return;
//            this.p_UnderLayer = pUnderLayer;
//        }
//
//        @Override
//        public void SetUpperLayer(BaseLayer pUpperLayer) {
//            // TODO Auto-generated method stub
//            if (pUpperLayer == null)
//                return;
//            this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
//            // nUpperLayerCount++;
//        }
//
//        @Override
//        public String GetLayerName() {
//            // TODO Auto-generated method stub
//            return pLayerName;
//        }
//
//        @Override
//        public BaseLayer GetUnderLayer() {
//            // TODO Auto-generated method stub
//            if (p_UnderLayer == null)
//                return null;
//            return p_UnderLayer;
//        }
//
//        @Override
//        public BaseLayer GetUpperLayer(int nindex) {
//            // TODO Auto-generated method stub
//            if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
//                return null;
//            return p_aUpperLayer.get(nindex);
//        }
//
//        @Override
//        public void SetUpperUnderLayer(BaseLayer pUULayer) {
//            this.SetUpperLayer(pUULayer);
//            pUULayer.SetUnderLayer(this);
//
//        }
}

