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

public class Router_Gui extends JFrame {

	private JPanel contentPane;
	private JTable table;

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
		setBounds(100, 100, 906, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel RoutingTablePanel = new JPanel();
		RoutingTablePanel.setBounds(12, 10, 457, 401);
		contentPane.add(RoutingTablePanel);
		RoutingTablePanel.setLayout(null);
		
		JLabel RTLabel = new JLabel("Static Routing Table");
		RTLabel.setHorizontalAlignment(SwingConstants.CENTER);
		RTLabel.setBounds(87, 10, 274, 40);
		RoutingTablePanel.add(RTLabel);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"New column"
			}
		));
		table.setToolTipText("");
		table.setBounds(12, 49, 433, 282);
		RoutingTablePanel.add(table);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(90, 341, 127, 50);
		RoutingTablePanel.add(btnNewButton);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(254, 341, 127, 50);
		RoutingTablePanel.add(btnDelete);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(481, 10, 397, 257);
		contentPane.add(panel_1);
	}
}
