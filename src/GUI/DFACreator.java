package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Forming.NDFA;
import Parsing.DFACommandParser;
import Parsing.FileStorage;
import Parsing.GraphwizExec;
import Parsing.ImageDisplay;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.Dimension;
import javax.swing.JList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class DFACreator {

	private JFrame frame;
	private JTextField matchStringTextField;
	private JTextField nameDFATextField;
	private JPanel dfaImagePanel = new JPanel();
	
	private int _lastSelection;
	
	private NDFA _lastDFA;

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
	private JList dfaItemList = new JList();
	private JTextField grammarNameTextField;
	private JTextField alfabetTextField;
	private JTextField productionRulesTextField;
	
	public DFACreator() {
		initialize();
		reloadDfaItems();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 1800, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1774, 950);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("DFA", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblGenerateDfa = new JLabel("Name");
		lblGenerateDfa.setBounds(178, 15, 34, 14);
		panel.add(lblGenerateDfa);
		
		JLabel lblMatchString = new JLabel("match String");
		lblMatchString.setBounds(178, 40, 75, 14);
		panel.add(lblMatchString);
		
		matchStringTextField = new JTextField();
		matchStringTextField.setBounds(251, 37, 225, 20);
		panel.add(matchStringTextField);
		matchStringTextField.setColumns(10);
		
		
		dfaImagePanel.setBounds(174, 61, 1591, 866);
		panel.add(dfaImagePanel);
		dfaImagePanel.setLayout(null);
		
		JLabel dfaLabel_1 = new JLabel("New label");
		dfaLabel_1.setBounds(10, 11, 1560, 844);
		dfaImagePanel.add(dfaLabel_1);
		
		JButton dfaFromMatchStringGenerateButton = new JButton("Generate");
		dfaFromMatchStringGenerateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Remove old image
				NDFA dfa = DFACommandParser.ParseMultiString(matchStringTextField.getText());
				
				String dfaName = nameDFATextField.getText();
				dfa.setMethodName(dfaName);
				
				if (dfaName.length() > 0) {
					// Create new image
					JLabel dfaLabel = new JLabel("Empty");
					var bounds = dfaImagePanel.getBounds();
					
					if (!(dfaImagePanel.getComponents().length > 0))
						dfaImagePanel.remove(0);
					
					
					dfaLabel = GraphwizExec.generateDFAJLabel(dfa);
					dfaLabel.setBounds(bounds);
					
					if (dfaImagePanel.getComponents().length > 0)
						dfaImagePanel.remove(0);
					
					dfaImagePanel.add(dfaLabel);
					dfaImagePanel.revalidate();
					dfaImagePanel.repaint();
				} else {
					GraphwizExec.infoBox("No name present. Please fill in a filename.", "No Filename present");
				}

			}
		});
		
				dfaFromMatchStringGenerateButton.setBounds(486, 36, 89, 23);
				panel.add(dfaFromMatchStringGenerateButton);
				
				JButton btnSave = new JButton("Save");
				btnSave.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						String dfaName = nameDFATextField.getText();
						
						if (dfaName.length() > 0) {
							if (FileStorage.saveTextFile(dfaName, _lastDFA.getMethodAsGraphVizString())) {
								GraphwizExec.infoBox("Success.", "File: "+ dfaName +" saved successfully");
							}
						} else {
							GraphwizExec.infoBox("No filename present. Please fill in a filename.", "No Filename present");
						}
						
						dfaItemList.revalidate();
						dfaItemList.repaint();
						
					}
				});
				btnSave.setBounds(486, 11, 89, 23);
				panel.add(btnSave);
				
				nameDFATextField = new JTextField();
				nameDFATextField.setBounds(251, 12, 225, 20);
				panel.add(nameDFATextField);
				nameDFATextField.setColumns(10);
				this.dfaItemList.setModel(new DefaultListModel<String>());
				
				dfaItemList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (getDfaListDataModel().getSize() != 0)
							getDfaListDataModel().removeElement(_lastSelection);
						
						dfaItemList.revalidate();
						dfaItemList.repaint();
						
						System.out.println(dfaItemList.getSelectedValue().toString());
						
						// JList menu item clicked, load file
						String dfaName = dfaItemList.getSelectedValue().toString();
						_lastSelection = dfaItemList.getSelectedIndex();
						
						if (dfaName.length() > 0) {
							showDfa(dfaName);
						} else {
							GraphwizExec.infoBox("No filename present. Please fill in a filename.", "No Filename present");
						}
						
					}
				});
				
				dfaItemList.setBounds(10, 63, 154, 848);
				panel.add(dfaItemList);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Grammar", null, panel_1, null);
		panel_1.setLayout(null);
		
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(1099, 36, 89, 23);
		panel_1.add(button);
		
		grammarNameTextField = new JTextField();
		grammarNameTextField.setColumns(10);
		grammarNameTextField.setBounds(241, 12, 225, 20);
		panel_1.add(grammarNameTextField);
		
		JLabel label = new JLabel("Name");
		label.setBounds(168, 15, 34, 14);
		panel_1.add(label);
		
		JLabel lblAlfabet = new JLabel("alfabet");
		lblAlfabet.setBounds(168, 40, 75, 14);
		panel_1.add(lblAlfabet);
		
		alfabetTextField = new JTextField();
		alfabetTextField.setColumns(10);
		alfabetTextField.setBounds(241, 37, 225, 20);
		panel_1.add(alfabetTextField);
		
		JButton button_1 = new JButton("Generate");
		button_1.setBounds(1099, 65, 89, 23);
		panel_1.add(button_1);
		
		JLabel label_2 = new JLabel("New label");
		label_2.setBounds(174, 119, 1585, 792);
		panel_1.add(label_2);
		
		JList list = new JList();
		list.setBounds(0, 131, 154, 780);
		panel_1.add(list);
		
		JLabel lblPrRules = new JLabel("Pr Rules");
		lblPrRules.setBounds(168, 65, 48, 14);
		panel_1.add(lblPrRules);
		
		productionRulesTextField = new JTextField();
		productionRulesTextField.setBounds(241, 62, 225, 20);
		panel_1.add(productionRulesTextField);
		productionRulesTextField.setColumns(10);
		
		JLabel label5 = new JLabel("Start");
		label5.setBounds(493, 15, 48, 14);
		panel_1.add(label5);
		
		JTextPane startStateTextField = new JTextPane();
		startStateTextField.setBounds(530, 12, 154, 20);
		panel_1.add(startStateTextField);
		
	}
	
	private DefaultListModel<String> getDfaListDataModel() {
		return (DefaultListModel<String>)dfaItemList.getModel();
	}
	
	private void reloadDfaItems() {
		File folder = new File(System.getProperty("user.dir")+"\\Storage\\JPEG\\");
		DefaultListModel<String> model = (DefaultListModel<String>) this.dfaItemList.getModel();
		model.removeAllElements();
		
		for (File f: folder.listFiles()) {
			if (f.getName().endsWith(".gv"))
				model.addElement(f.getName());
		}
	}
	
	private void showDfa(String dfaName) {
		if (dfaName.length() < 1)
			return;
		
		NDFA dfa = NDFA.fromGraphVizStringToDFA(FileStorage.loadTextFile(dfaName), dfaName.replace(".gv", ".jpg"));
		
		
		// Create new image
		JLabel dfaLabel = new JLabel("Empty");
		var bounds = dfaImagePanel.getBounds();
		
		if (!(dfaImagePanel.getComponents().length > 0)) {
			dfaImagePanel.remove(0);
		}
		
		dfaLabel = GraphwizExec.loadDFAJLabel(dfa);
		dfaLabel.setBounds(bounds);
		
		if (dfaImagePanel.getComponents().length > 0)
			dfaImagePanel.remove(0);
		
		dfaImagePanel.add(dfaLabel);
		dfaImagePanel.revalidate();
		dfaImagePanel.repaint();
	}
}
