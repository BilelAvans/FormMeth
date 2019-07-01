package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Forming.Alfabet;
import Forming.DFA;
import Forming.Grammar;
import Forming.NDFA;
import Forming.ProductionRule;
import Forming.RegulExpress;
import Forming.TransitionRule;
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
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.awt.Dimension;
import javax.swing.JList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

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
	private JTextField inputProductionRulesField;
	private JTextField userExpressTextField;
	private JTextField userTestStringTextField;

	public DFACreator() {
		initialize();
		reloadDfaItems();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 1600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1620, 852);
		frame.getContentPane().add(tabbedPane);
		
		JPanel regulExpPanel = new JPanel();
		tabbedPane.addTab("RegulExp", null, regulExpPanel, null);
		regulExpPanel.setLayout(null);
		
		
		userExpressTextField = new JTextField();
		userExpressTextField.setBounds(125, 32, 368, 20);
		regulExpPanel.add(userExpressTextField);
		userExpressTextField.setColumns(10);
		
		userTestStringTextField = new JTextField();
		userTestStringTextField.setBounds(125, 76, 368, 20);
		regulExpPanel.add(userTestStringTextField);
		userTestStringTextField.setColumns(10);
		


		
		JLabel lblExpression = new JLabel("Expression:");
		lblExpression.setBounds(10, 32, 76, 14);
		regulExpPanel.add(lblExpression);
		
		JLabel lblTestString = new JLabel("Test String");
		lblTestString.setBounds(10, 76, 76, 14);
		regulExpPanel.add(lblTestString);
		
		JLabel isValidStringLabel = new JLabel("?");
		isValidStringLabel.setBounds(445, 111, 48, 14);
		regulExpPanel.add(isValidStringLabel);

		JPanel dfaPanel = new JPanel();
		tabbedPane.addTab("DFA", null, dfaPanel, null);
		dfaPanel.setLayout(null);

		JLabel lblGenerateDfa = new JLabel("Name");
		lblGenerateDfa.setBounds(178, 15, 34, 14);
		dfaPanel.add(lblGenerateDfa);

		JLabel lblMatchString = new JLabel("match String");
		lblMatchString.setBounds(178, 40, 75, 14);
		dfaPanel.add(lblMatchString);
		
		JButton btnGenerate = new JButton("Valid String?");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get Regul Expression
				String userExpression = userExpressTextField.getText();
				
				if (userExpression.length() < 1)
					return; // Do nothing no expression.
				
				String userString = userTestStringTextField.getText();
				
				if (userString.length() < 1)
					return;
				
				var expression = new RegulExpress(userExpression);
				
				isValidStringLabel.setText(expression.isValid(userString) ? "Valid" : "Non Valid");
			}
		});
		btnGenerate.setBounds(319, 107, 101, 23);
		regulExpPanel.add(btnGenerate);

		matchStringTextField = new JTextField();
		matchStringTextField.setBounds(251, 37, 225, 20);
		dfaPanel.add(matchStringTextField);
		matchStringTextField.setColumns(10);

		dfaImagePanel.setBounds(174, 61, 1591, 866);
		dfaPanel.add(dfaImagePanel);
		dfaImagePanel.setLayout(null);

		JLabel dfaLabel_1 = new JLabel("New label");
		dfaLabel_1.setBounds(10, 11, 1560, 844);
		dfaImagePanel.add(dfaLabel_1);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Minimise");
		chckbxNewCheckBox.setBounds(581, 36, 75, 23);
		dfaPanel.add(chckbxNewCheckBox);
		
		JCheckBox chckbxReverse = new JCheckBox("Reverse");
		chckbxReverse.setBounds(662, 36, 97, 23);
		dfaPanel.add(chckbxReverse);

		JButton dfaFromMatchStringGenerateButton = new JButton("Generate");
		dfaFromMatchStringGenerateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		dfaFromMatchStringGenerateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Remove old image
				DFA dfa = DFACommandParser.ParseMultiString(matchStringTextField.getText());
				
				if (chckbxNewCheckBox.isSelected())
					dfa = dfa.minimize();
//				if (chckbxReverse.isSelected())
//					dfa = dfa.reverse();

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
		dfaPanel.add(dfaFromMatchStringGenerateButton);

		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String dfaName = nameDFATextField.getText();

				if (dfaName.length() > 0) {
					if (FileStorage.saveTextFile(dfaName, _lastDFA.getMethodAsGraphVizString())) {
						GraphwizExec.infoBox("Success.", "File: " + dfaName + " saved successfully");
					}
				} else {
					GraphwizExec.infoBox("No filename present. Please fill in a filename.", "No Filename present");
				}

				dfaItemList.revalidate();
				dfaItemList.repaint();

			}
		});
		btnSave.setBounds(486, 11, 89, 23);
		dfaPanel.add(btnSave);

		nameDFATextField = new JTextField();
		nameDFATextField.setBounds(251, 12, 225, 20);
		dfaPanel.add(nameDFATextField);
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
		dfaPanel.add(dfaItemList);
		

		JPanel grammarPanel = new JPanel();
		tabbedPane.addTab("Grammar", null, grammarPanel, null);
		grammarPanel.setLayout(null);

		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(1099, 36, 89, 23);
		grammarPanel.add(button);

		grammarNameTextField = new JTextField();
		grammarNameTextField.setColumns(10);
		grammarNameTextField.setBounds(241, 12, 225, 20);
		grammarPanel.add(grammarNameTextField);

		JLabel label = new JLabel("Name");
		label.setBounds(168, 15, 34, 14);
		grammarPanel.add(label);

		JLabel lblAlfabet = new JLabel("alfabet");
		lblAlfabet.setBounds(168, 40, 75, 14);
		grammarPanel.add(lblAlfabet);

		alfabetTextField = new JTextField();
		alfabetTextField.setColumns(10);
		alfabetTextField.setBounds(241, 37, 225, 20);
		grammarPanel.add(alfabetTextField);

		inputProductionRulesField = new JTextField();
		inputProductionRulesField.setBounds(241, 62, 225, 20);
		grammarPanel.add(inputProductionRulesField);
		inputProductionRulesField.setColumns(10);

		JList list = new JList();
		list.setBounds(0, 131, 154, 780);
		grammarPanel.add(list);

		JLabel lblPrRules = new JLabel("Pr Rules");
		lblPrRules.setBounds(168, 65, 48, 14);
		grammarPanel.add(lblPrRules);

		JLabel label5 = new JLabel("Start");
		label5.setBounds(493, 15, 48, 14);
		grammarPanel.add(label5);

		JTextPane startStateTextField = new JTextPane();
		startStateTextField.setBounds(530, 12, 154, 20);
		grammarPanel.add(startStateTextField);

		JTextArea productionRulesTextArea = new JTextArea();
		productionRulesTextArea.setBounds(168, 127, 1321, 629);
		grammarPanel.add(productionRulesTextArea);

		JButton button_1 = new JButton("Generate");
		button_1.setBounds(1099, 65, 89, 23);
		grammarPanel.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Create Grammar
				var alfabet = new Alfabet(alfabetTextField.getText().split(" "));

				String[] prodRules = inputProductionRulesField.getText().split(",");
				ArrayList<ProductionRule> rules = new ArrayList<ProductionRule>();

				for (String pr : prodRules) {
					String[] args = pr.split(" ");

					ProductionRule rule = null;

					if (!(rules == null) && !rules.contains(rule)) {
						rule = new ProductionRule(args[0], false);
						rules.add(rule);
					} else {
						rule = rules.stream().filter(ru -> ru.getFrom().equals(args[0])).findAny().get();
					}

					String sign = "";
					String endSymbol = "";
					System.out.println(args[1]);
					for (int counter = 0; counter < args[2].length(); counter++) {

						if (Character.isUpperCase(args[2].charAt(counter))) {
							endSymbol += args[2].charAt(counter);
						} else {
							sign += args[2].charAt(counter);
						}
					}

					if (!sign.isBlank() && !endSymbol.isEmpty())
						rule.addTransitionRule(new TransitionRule<String>(sign, endSymbol));

					if (pr.contains("|"))
						rule.setEndState(true);
				}

				Grammar grammar = new Grammar(alfabet, "S", rules.stream().toArray(ProductionRule[]::new));
				productionRulesTextArea.setText(grammar.toGrammarString());
			}
		});

	}

	private DefaultListModel<String> getDfaListDataModel() {
		return (DefaultListModel<String>) dfaItemList.getModel();
	}

	private void reloadDfaItems() {
		File folder = new File(System.getProperty("user.dir") + "\\Storage\\JPEG\\");
		DefaultListModel<String> model = (DefaultListModel<String>) this.dfaItemList.getModel();
		model.removeAllElements();

		for (File f : folder.listFiles()) {
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
