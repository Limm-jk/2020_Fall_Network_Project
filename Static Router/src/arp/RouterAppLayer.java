package staticRouting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

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

import staticRouting.ARPLayer.ARPCache;


public class RouterAppLayer extends JFrame implements BaseLayer{
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	BaseLayer UnderLayer;
	
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
    private static LayerManager m_LayerMgr = new LayerManager();
    
    private static RoutingTable routingTable = new RoutingTable();
    
    public static void main(String[] args) throws IOException {
    	
    	NILayer[] niLayer = new NILayer[2];
    	EthernetLayer[] ethernetLayer = new EthernetLayer[2];
    	ARPLayer[] arpLayer = new ARPLayer[2];
    	IPLayer[] ipLayer = new IPLayer[2];
    	
    	
    	niLayer[0] = new NILayer("NI0");
    	niLayer[1] = new NILayer("NI1");
    	
    	ethernetLayer[0] = new EthernetLayer("Ethernet0");
    	ethernetLayer[1] = new EthernetLayer("Ethernet1");
    	
    	arpLayer[0] = new ARPLayer("ARP0");
    	arpLayer[1] = new ARPLayer("ARP1");
    	
    	ipLayer[0] = new IPLayer("IP0");
    	ipLayer[1] = new IPLayer("IP1");
    	
    	RouterAppLayer routerAppLayer = new RouterAppLayer("GUI");
    	
    	m_LayerMgr.AddLayer(niLayer[0]);
    	m_LayerMgr.AddLayer(niLayer[1]);
    	m_LayerMgr.AddLayer(ethernetLayer[0]);
    	m_LayerMgr.AddLayer(ethernetLayer[1]);
    	m_LayerMgr.AddLayer(arpLayer[0]);
    	m_LayerMgr.AddLayer(arpLayer[1]);
    	m_LayerMgr.AddLayer(ipLayer[0]);
    	m_LayerMgr.AddLayer(ipLayer[1]);
    	m_LayerMgr.AddLayer(routerAppLayer);
    	
    	// port 0
    	m_LayerMgr.ConnectLayers(" NI0 ( *Ethernet0 ( *ARP0 ( *IP0 ) ) ) ");
    	// port 1
    	m_LayerMgr.ConnectLayers(" NI1 ( *Ethernet1 ( *ARP1 ( *IP1 ) ) ) ");
    	
    	// TODO : RouterAppLayer Setting
		ethernetLayer[0].setIPLayer(ipLayer[0]);
		ethernetLayer[1].setIPLayer(ipLayer[1]);
		
		ethernetLayer[0].setARPLayer(arpLayer[0]);
		ethernetLayer[1].setARPLayer(arpLayer[1]);
		
    	// IP, Mac Setting
    	ipLayer[0].setEthernetLayer(ethernetLayer[0]);
    	ipLayer[1].setEthernetLayer(ethernetLayer[1]);
    	
    	ethernetLayer[0].SetEnetSrcAddress(niLayer[0].m_pAdapterList.get(0).getHardwareAddress());
    	ethernetLayer[1].SetEnetDstAddress(niLayer[1].m_pAdapterList.get(1).getHardwareAddress());
    	
    	ipLayer[0].setSrcIp(niLayer[0].m_pAdapterList.get(0).getAddresses().get(0).getAddr().getData());
    	ipLayer[1].setSrcIp(niLayer[1].m_pAdapterList.get(1).getAddresses().get(0).getAddr().getData());
    	
    	arpLayer[0].setSrcIp(niLayer[0].m_pAdapterList.get(0).getAddresses().get(0).getAddr().getData());
    	arpLayer[1].setSrcIp(niLayer[1].m_pAdapterList.get(1).getAddresses().get(0).getAddr().getData());
    	arpLayer[0].setSrcMac(niLayer[0].m_pAdapterList.get(0).getHardwareAddress());
    	arpLayer[1].setSrcMac(niLayer[1].m_pAdapterList.get(1).getHardwareAddress());
    	
    	niLayer[0].SetAdapterNumber(0);
    	niLayer[1].SetAdapterNumber(1);
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
        RoutingAdd.addActionListener((new java.awt.event.ActionListener() {
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
        ProxyAdd.addActionListener(new java.awt.event.ActionListener() {
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
        btnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnEnd.setBounds(334, 396, 114, 42);
        contentPane.add(btnEnd);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
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
    
	public void updateARPCacheTable(ArrayList<ARPCache> cache_table) {
		// GUI에 cache table 업데이트
		//ip주소를 string으로 변환 필요
		//mac주소를 string으로 변환 필요
		//ARPTable 변수 (JTable)의 row에 cache table 업데이터
		
		// 모든 행 삭제
		if (arpModel.getRowCount() > 0) {
		    for (int i = arpModel.getRowCount() - 1; i > -1; i--) {
		        arpModel.removeRow(i);
		    }
		}
		
		//cache_table의 모든 행 추가
		Iterator<ARPCache> iter = cache_table.iterator();
    	while(iter.hasNext()) {
    		ARPCache cache = iter.next();
    		String[] row = new String[3];
    		
    		row[0] = ipByteToString(cache.ip);
    		if(cache.status == false) {
    			row[1] = "??????????";
    			row[2] = "incomplete";
    		}
    		else {
    			row[1] = macByteToString(cache.mac);
    			row[2] = "complete";
    		}
    		
    		arpModel.addRow(row);
    	}
	}
	
	
	public String macByteToString(byte[] byte_MacAddress) { //MAC Byte주소를 String으로 변환
		String MacAddress = "";
		for (int i = 0; i < 6; i++) { 
			//2자리 16진수를 대문자로, 그리고 1자리 16진수는 앞에 0을 붙임.
			MacAddress += String.format("%02X%s", byte_MacAddress[i], (i < MacAddress.length() - 1) ? "" : "");
			
			if (i != 5) {
				//2자리 16진수 자리 단위 뒤에 "-"붙여주기
				MacAddress += ":";
			}
		}
		return MacAddress;
	}

	public byte[] macStringToByte(String mac) {
		// string mac 주소는 "00:00:00:00:00:00" 형태
		byte[] ret = new byte[6];

		StringTokenizer tokens = new StringTokenizer(mac, ":");

		for (int i = 0; tokens.hasMoreElements(); i++) {

			String temp = tokens.nextToken();

			try {
				ret[i] = Byte.parseByte(temp, 16);
			} catch (NumberFormatException e) {
				int minus = (Integer.parseInt(temp, 16)) - 256;
				ret[i] = (byte) (minus);
			}
		}

		return ret;
	}
	
	public String ipByteToString(byte[] stringIP) {
		String result = "";
		for(byte raw : stringIP){
			result += raw & 0xFF;
			result += ".";
		}
		return result.substring(0, result.length()-1);		
	}
	
    @Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		// TODO Auto-generated method stub
		if (pUnderLayer == null)
			return;
		this.p_UnderLayer = pUnderLayer;
	}

	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
		// TODO Auto-generated method stub
		if (pUpperLayer == null)
			return;
		this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
		// nUpperLayerCount++;
	}

	@Override
	public String GetLayerName() {
		// TODO Auto-generated method stub
		return pLayerName;
	}

	@Override
	public BaseLayer GetUnderLayer() {
		// TODO Auto-generated method stub
		if (p_UnderLayer == null)
			return null;
		return p_UnderLayer;
	}

	@Override
	public BaseLayer GetUpperLayer(int nindex) {
		// TODO Auto-generated method stub
		if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
			return null;
		return p_aUpperLayer.get(nindex);
	}

	@Override
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		this.SetUpperLayer(pUULayer);
		pUULayer.SetUnderLayer(this);

	}


/*
package staticRouting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import staticRouting.ARPLayer.ARPCache;
import staticRouting.ARPLayer.Proxy;

public class RouterAppLayer extends JFrame implements BaseLayer{
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	BaseLayer UnderLayer;
	
	
	private DefaultTableModel arpModel, proxyModel;
	String arpModelHeader[] = {"IpAddress", "MacAddress", "Status"};
	String[][] arpModelContents = new String[0][3];
	String proxyModelHeader[] = {"IpAddress", "MacAddress"};
	String[][] proxyModelContents = new String[0][2];
	
	private static LayerManager m_LayerMgr = new LayerManager();
	
	static int adapterNumber = 0;
	
	public static void main(String[] args) throws IOException {
		NILayer niLayer = new NILayer("NI");
		EthernetLayer ethernetLayer = new EthernetLayer("Ethernet");
		ARPLayer arpLayer = new ARPLayer("ARP");
		IPLayer ipLayer = new IPLayer("IP");
		RouterAppLayer routerAppLayer = new RouterAppLayer("GUI");
		
		m_LayerMgr.AddLayer(niLayer);
		m_LayerMgr.AddLayer(ethernetLayer);
		m_LayerMgr.AddLayer(arpLayer);
		m_LayerMgr.AddLayer(ipLayer);
		m_LayerMgr.AddLayer(routerAppLayer);

		m_LayerMgr.ConnectLayers(" NI ( *Ethernet ( *ARP ( *IP ( *GUI ) ) ) )");
		
		arpLayer.setArpAppLayer(routerAppLayer);
		ipLayer.setSrcIp(InetAddress.getLocalHost().getAddress());
		arpLayer.setSrcIp(InetAddress.getLocalHost().getAddress());
		arpLayer.setSrcMac(niLayer.GetAdapterObject(adapterNumber).getHardwareAddress());
		
		niLayer.SetAdapterNumber(0);
		niLayer.Receive();
	}
	
	
	public RouterAppLayer(String pName) throws IOException {
		pLayerName = pName;
		
		
	}
	
	class proxyWindow extends JFrame{
		
	}
	
	class btnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	
	public String macByteToString(byte[] byte_MacAddress) { //MAC Byte주소를 String으로 변환
		String MacAddress = "";
		for (int i = 0; i < 6; i++) { 
			//2자리 16진수를 대문자로, 그리고 1자리 16진수는 앞에 0을 붙임.
			MacAddress += String.format("%02X%s", byte_MacAddress[i], (i < MacAddress.length() - 1) ? "" : "");
			
			if (i != 5) {
				//2자리 16진수 자리 단위 뒤에 "-"붙여주기
				MacAddress += ":";
			}
		}
		return MacAddress;
	}

	public byte[] macStringToByte(String mac) {
		// string mac 주소는 "00:00:00:00:00:00" 형태
		byte[] ret = new byte[6];

		StringTokenizer tokens = new StringTokenizer(mac, ":");

		for (int i = 0; tokens.hasMoreElements(); i++) {

			String temp = tokens.nextToken();

			try {
				ret[i] = Byte.parseByte(temp, 16);
			} catch (NumberFormatException e) {
				int minus = (Integer.parseInt(temp, 16)) - 256;
				ret[i] = (byte) (minus);
			}
		}

		return ret;
	}
	
	public String ipByteToString(byte[] stringIP) {
		String result = "";
		for(byte raw : stringIP){
			result += raw & 0xFF;
			result += ".";
		}
		return result.substring(0, result.length()-1);		
	}
	
	public void updateARPCacheTable(ArrayList<ARPCache> cache_table) {
		// GUI에 cache table 업데이트
		//ip주소를 string으로 변환 필요
		//mac주소를 string으로 변환 필요
		//ARPTable 변수 (JTable)의 row에 cache table 업데이터
		
		// 모든 행 삭제
		if (arpModel.getRowCount() > 0) {
		    for (int i = arpModel.getRowCount() - 1; i > -1; i--) {
		        arpModel.removeRow(i);
		    }
		}
		
		//cache_table의 모든 행 추가
		Iterator<ARPCache> iter = cache_table.iterator();
    	while(iter.hasNext()) {
    		ARPCache cache = iter.next();
    		String[] row = new String[3];
    		
    		row[0] = ipByteToString(cache.ip);
    		if(cache.status == false) {
    			row[1] = "??????????";
    			row[2] = "incomplete";
    		}
    		else {
    			row[1] = macByteToString(cache.mac);
    			row[2] = "complete";
    		}
    		
    		arpModel.addRow(row);
    	}
	}
	
	public void updateProxyEntry(ArrayList<Proxy> proxyEntry) {
		// GUI에 proxy Entry 업데이트
		// 모든 행 삭제
		if (proxyModel.getRowCount() > 0) {
		    for (int i = proxyModel.getRowCount() - 1; i > -1; i--) {
		        proxyModel.removeRow(i);
		    }
		}
		
		Iterator<Proxy> iter = proxyEntry.iterator();
    	while(iter.hasNext()) {
    		Proxy proxy = iter.next();
    		String[] row = new String[2];
    		
    		row[0] = ipByteToString(proxy.ip);
    		row[1] = macByteToString(proxy.mac);
    		
    		proxyModel.addRow(row);
    	}
	}
	
	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		// TODO Auto-generated method stub
		if (pUnderLayer == null)
			return;
		this.p_UnderLayer = pUnderLayer;
	}

	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
		// TODO Auto-generated method stub
		if (pUpperLayer == null)
			return;
		this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
		// nUpperLayerCount++;
	}

	@Override
	public String GetLayerName() {
		// TODO Auto-generated method stub
		return pLayerName;
	}

	@Override
	public BaseLayer GetUnderLayer() {
		// TODO Auto-generated method stub
		if (p_UnderLayer == null)
			return null;
		return p_UnderLayer;
	}

	@Override
	public BaseLayer GetUpperLayer(int nindex) {
		// TODO Auto-generated method stub
		if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
			return null;
		return p_aUpperLayer.get(nindex);
	}

	@Override
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		this.SetUpperLayer(pUULayer);
		pUULayer.SetUnderLayer(this);

	}
	*/
}