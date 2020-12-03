package staticRouting;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Router_Gui extends JFrame {

	private JPanel contentPane;
	private JTable RoutingTable;
	private JTable ARPTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Router_Gui frame = new Router_Gui();
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
	public Router_Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 906, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel RoutingTablePanel = new JPanel();
		RoutingTablePanel.setBounds(12, 10, 457, 401);
		contentPane.add(RoutingTablePanel);
		RoutingTablePanel.setLayout(null);
		
		JLabel RoutingTableLabel = new JLabel("Static Routing Table");
		RoutingTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		RoutingTableLabel.setBounds(87, 10, 274, 40);
		RoutingTablePanel.add(RoutingTableLabel);
		
		RoutingTable = new JTable();
		RoutingTable.setToolTipText("");
		RoutingTable.setBounds(12, 49, 433, 282);
		RoutingTablePanel.add(RoutingTable);
		
		JButton RoutingAddBtn = new JButton("Add");
		RoutingAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		RoutingAddBtn.setBounds(90, 341, 127, 50);
		RoutingTablePanel.add(RoutingAddBtn);
		
		JButton RoutingDeleteBtn = new JButton("Delete");
		RoutingDeleteBtn.setBounds(254, 341, 127, 50);
		RoutingTablePanel.add(RoutingDeleteBtn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(481, 10, 397, 401);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		ARPTable = new JTable();
		ARPTable.setBounds(12, 32, 373, 301);
		panel_1.add(ARPTable);
		
		JLabel ARPTableLabel = new JLabel("ARP Cache Table");
		ARPTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ARPTableLabel.setBounds(135, 10, 124, 15);
		panel_1.add(ARPTableLabel);
		
		JButton ARPDeleteBtn = new JButton("Delete");
		ARPDeleteBtn.setBounds(135, 341, 127, 50);
		panel_1.add(ARPDeleteBtn);
	}
}
