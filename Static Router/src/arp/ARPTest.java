package staticRouting;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTable;

public class ARPTest extends JFrame {

	private JPanel contentPane;
	private JTextField GARPTextField;
	private JButton ARPItemDelete;
	private JButton ARPAllDelete;
	private JButton ProxyAdd;
	private JButton ProxyDelete;
	private JTextField IPTextField;
	private JButton ARP_IPSend;
	private JButton GARPSend;
	private JTable ARPTable;
	private JTable ProxyTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ARPTest frame = new ARPTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ARPTest() {
		setTitle("TestARP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel ARPPanel = new JPanel();
		ARPPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ARP Cache", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		ARPPanel.setToolTipText("");
		ARPPanel.setBounds(12, 23, 436, 353);
		contentPane.add(ARPPanel);
		ARPPanel.setLayout(null);
		
		ARPItemDelete = new JButton("Item Delete");
		ARPItemDelete.setBounds(26, 236, 146, 38);
		ARPItemDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		ARPPanel.add(ARPItemDelete);
		
		ARPAllDelete = new JButton("All Delete");
		ARPAllDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		ARPAllDelete.setBounds(254, 236, 146, 38);
		ARPPanel.add(ARPAllDelete);
		
		JLabel lblNewLabel = new JLabel("IP주소");
		lblNewLabel.setFont(new Font("돋움", Font.BOLD, 14));
		lblNewLabel.setBounds(16, 295, 52, 36);
		ARPPanel.add(lblNewLabel);
		
		IPTextField = new JTextField();
		IPTextField.setBounds(98, 300, 202, 30);
		ARPPanel.add(IPTextField);
		IPTextField.setColumns(10);
		
		ARP_IPSend = new JButton("Send");
		ARP_IPSend.setBounds(327, 303, 73, 23);
		ARPPanel.add(ARP_IPSend);
		
		ARPTable = new JTable();
		ARPTable.setBounds(12, 20, 412, 206);
		ARPPanel.add(ARPTable);
		
		JPanel ProxyPanel = new JPanel();
		ProxyPanel.setBorder(new TitledBorder(null, "Proxy ARP Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ProxyPanel.setBounds(458, 23, 414, 227);
		contentPane.add(ProxyPanel);
		ProxyPanel.setLayout(null);
		
		ProxyAdd = new JButton("Add");
		ProxyAdd.setBounds(22, 179, 156, 38);
		ProxyPanel.add(ProxyAdd);
		
		ProxyDelete = new JButton("Delete");
		ProxyDelete.setBounds(232, 179, 156, 38);
		ProxyPanel.add(ProxyDelete);
		
		ProxyTable = new JTable();
		ProxyTable.setBounds(12, 21, 390, 151);
		ProxyPanel.add(ProxyTable);
		
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
		GARPPanel.add(GARPSend);
		
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
		
	}
}
