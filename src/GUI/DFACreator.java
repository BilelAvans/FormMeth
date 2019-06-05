package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Forming.DFA;
import Parsing.GraphwizExec;
import Parsing.ImageDisplay;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class DFACreator {

	private JFrame frame;
	private JTextField matchStringTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DFACreator window = new DFACreator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DFACreator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 880, 571);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 864, 532);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("DFA", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblGenerateDfa = new JLabel("Generate DFA");
		lblGenerateDfa.setBounds(10, 11, 85, 14);
		panel.add(lblGenerateDfa);
		
		JLabel lblMatchString = new JLabel("match String");
		lblMatchString.setBounds(33, 36, 75, 14);
		panel.add(lblMatchString);
		
		matchStringTextField = new JTextField();
		matchStringTextField.setBounds(106, 33, 96, 20);
		panel.add(matchStringTextField);
		matchStringTextField.setColumns(10);
		
		JPanel dfaImagePanel = new JPanel();
		dfaImagePanel.setBounds(350, 11, 499, 482);
		panel.add(dfaImagePanel);
		dfaImagePanel.setLayout(null);
		
		JLabel dfaLabel = new JLabel("New label");
		dfaLabel.setBounds(10, 11, 489, 471);
		dfaImagePanel.add(dfaLabel);
		
		JButton dfaFromMatchStringGenerateButton = new JButton("Generate");
		dfaFromMatchStringGenerateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Remove old image
				dfaImagePanel.remove(0);
				// Create new image
				JLabel dfaLabel = new JLabel("Empty");
				DFA dfa = DFA.GenerateDFA(matchStringTextField.getText());
				dfaLabel = GraphwizExec.openDotExeCodePanel(dfa);
				dfaLabel.setBounds(10, 11, 489, 471);
				dfaImagePanel.add(dfaLabel);
				dfaImagePanel.revalidate();
				dfaImagePanel.repaint();
			}
		});
		dfaFromMatchStringGenerateButton.setBounds(229, 32, 89, 23);
		panel.add(dfaFromMatchStringGenerateButton);
		

		
	}
}
