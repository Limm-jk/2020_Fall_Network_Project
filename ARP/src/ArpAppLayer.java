package arp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import arp.ChatFileDlg.setAddressListener;

public class ArpAppLayer extends JFrame implements BaseLayer{
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	BaseLayer UnderLayer;
	
	private JPanel contentPane;
	private JTextField GARPTextField;
	private JButton ARPItemDelete;
	private JButton ARPAllDelete;
	private JTextArea ARPTextArea;
	private JTextArea ProxyTestArea;
	private JButton ProxyAdd;
	private JButton ProxyDelete;
	private JTextField IPTextField;
	private JButton ARP_IPSend;
	private JButton GARPSend;

	private static LayerManager m_LayerMgr = new LayerManager();
	
	static int adapterNumber = 0;
	
	public static void main(String[] args) throws IOException {
		NILayer niLayer = new NILayer("NI");
		EthernetLayer ethernetLayer = new EthernetLayer("Ethernet");
		ARPLayer arpLayer = new ARPLayer("ARP");
		ArpAppLayer arpAppLayer = new ArpAppLayer("GUI");
		
		m_LayerMgr.AddLayer(niLayer);
		m_LayerMgr.AddLayer(ethernetLayer);
		m_LayerMgr.AddLayer(arpLayer);
		m_LayerMgr.AddLayer(arpAppLayer);

		m_LayerMgr.ConnectLayers(" NI ( *Ethernet ( *ARP ( *GUI ) )");
		
		
	}
	
	
	public ArpAppLayer(String pName) throws IOException {
		pLayerName = pName;
		
		setTitle("TestARP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/*-----ARP Panel-----*/
		JPanel ARPPanel = new JPanel();
		ARPPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ARP Cache", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		ARPPanel.setToolTipText("");
		ARPPanel.setBounds(12, 23, 436, 353);
		contentPane.add(ARPPanel);
		ARPPanel.setLayout(null);
		
		ARPTextArea = new JTextArea();
		ARPTextArea.setEditable(false);
		ARPTextArea.setBounds(16, 22, 403, 204);
		ARPTextArea.setColumns(57);
		ARPTextArea.setRows(10);
		ARPPanel.add(ARPTextArea);
		
		JLabel lblNewLabel = new JLabel("IP주소");
		lblNewLabel.setFont(new Font("돋움", Font.BOLD, 14));
		lblNewLabel.setBounds(16, 295, 52, 36);
		ARPPanel.add(lblNewLabel);
		
		IPTextField = new JTextField();
		IPTextField.setBounds(98, 300, 202, 30);
		ARPPanel.add(IPTextField);
		IPTextField.setColumns(10);
		
		ARPItemDelete = new JButton("Item Delete");
		ARPItemDelete.setBounds(26, 236, 146, 38);
		ARPItemDelete.addActionListener(new btnListener());
		ARPPanel.add(ARPItemDelete);
		
		ARPAllDelete = new JButton("All Delete");
		ARPAllDelete.setBounds(254, 236, 146, 38);
		ARPAllDelete.addActionListener(new btnListener());
		ARPPanel.add(ARPAllDelete);
		
		ARP_IPSend = new JButton("Send");
		ARP_IPSend.setBounds(327, 303, 73, 23);
		ARP_IPSend.addActionListener(new btnListener());
		ARPPanel.add(ARP_IPSend);
		
		
		
		/*-----Proxy Panel-----*/
		JPanel ProxyPanel = new JPanel();
		ProxyPanel.setBorder(new TitledBorder(null, "Proxy ARP Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ProxyPanel.setBounds(458, 23, 414, 227);
		contentPane.add(ProxyPanel);
		ProxyPanel.setLayout(null);
		
		ProxyTestArea = new JTextArea();
		ProxyTestArea.setEditable(false);
		ProxyTestArea.setBounds(12, 22, 390, 147);
		ProxyPanel.add(ProxyTestArea);
		
		ProxyAdd = new JButton("Add");
		ProxyAdd.setBounds(22, 179, 156, 38);
		ProxyAdd.addActionListener(new btnListener());
		ProxyPanel.add(ProxyAdd);
		
		ProxyDelete = new JButton("Delete");
		ProxyDelete.setBounds(232, 179, 156, 38);
		ProxyDelete.addActionListener(new btnListener());
		ProxyPanel.add(ProxyDelete);
		
		
		/*-----GARP Panel-----*/
		JPanel GARPPanel = new JPanel();
		GARPPanel.setBorder(new TitledBorder(null, "Gratultous ARP", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GARPPanel.setBounds(460, 260, 412, 116);
		contentPane.add(GARPPanel);
		GARPPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("H/W 주소");
		lblNewLabel_1.setFont(new Font("돋움", Font.BOLD, 16));
		lblNewLabel_1.setBounds(12, 41, 74, 36);
		GARPPanel.add(lblNewLabel_1);
		
		GARPTextField = new JTextField();
		GARPTextField.setBounds(98, 41, 218, 36);
		GARPPanel.add(GARPTextField);
		GARPTextField.setColumns(10);
		
		GARPSend = new JButton("Send");
		GARPSend.setBounds(332, 41, 68, 36);
		GARPSend.addActionListener(new btnListener());
		GARPPanel.add(GARPSend);
		
		
		/*-----종료, 취소-----*/
		JButton btnEnd = new JButton("종료");
		btnEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnEnd.setBounds(334, 396, 114, 42);
		contentPane.add(btnEnd);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancel.setBounds(458, 396, 114, 42);
		contentPane.add(btnCancel);
		
		setVisible(true);
		
		
		NILayer niLayer = (NILayer)m_LayerMgr.GetLayer("NI");
		EthernetLayer ethernetLayer = (EthernetLayer)m_LayerMgr.GetLayer("Ethernet");
		ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
		
		//arp Layer에 IP와 Mac 세팅
		arpLayer.setSrcIp(InetAddress.getLocalHost().getAddress());
		arpLayer.setSrcMac(niLayer.GetAdapterObject(adapterNumber).getHardwareAddress());
		
	}
	
	class btnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/*----ARP Action-----*/
			if(e.getSource() == ARP_IPSend) {
				//IP 입력 후 send버튼 눌렀을 때
				String inputIP = IPTextField.getText().trim();
				
				InetAddress ip = null;
				try {
					ip = InetAddress.getByName(inputIP);
					byte[] dstIP = ip.getAddress();
					
					ARPLayer arpLayer = (ARPLayer)m_LayerMgr.GetLayer("ARP");
					arpLayer.setDstIp(dstIP);
					
					arpLayer.Send(new byte[0], 0);
					
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				
				IPTextField.setText("");
			}
			
			else if(e.getSource() == ARPItemDelete) {
				//ItemDelete 버튼을 눌렀을 때
			}
			
			else if(e.getSource() == ARPAllDelete) {
				//AllDelete 버튼을 눌렀을 때
			}
			
			/*----- Proxy Action -----*/
			else if(e.getSource() == ProxyAdd) {
				//Add 버튼을 눌렀을 때
			}
			
			else if(e.getSource() == ProxyDelete) {
				//Delete 버튼을 눌렀을 때
			}
			
			/*----- GARP Action -----*/
			else if(e.getSource() == GARPSend) {
				//GARP Send버튼을 눌렀을 때
			}
		}
	}
	
	public String get_MacAddress(byte[] byte_MacAddress) { //MAC Byte주소를 String으로 변환

		String MacAddress = "";
		for (int i = 0; i < 6; i++) { 
			//2자리 16진수를 대문자로, 그리고 1자리 16진수는 앞에 0을 붙임.
			MacAddress += String.format("%02X%s", byte_MacAddress[i], (i < MacAddress.length() - 1) ? "" : "");
			
			if (i != 5) {
				//2자리 16진수 자리 단위 뒤에 "-"붙여주기
				MacAddress += "-";
			}
		} 
		System.out.println("mac_address:" + MacAddress);
		return MacAddress;
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
}
