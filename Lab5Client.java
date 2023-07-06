import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

//Project done with Matthew Simmons 
public class Lab5Client extends JFrame implements ActionListener {
	static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Lab5Client scc = new Lab5Client();
		scc.setVisible(true);
	}

	JRadioButton dieselButton;
	JRadioButton steamButton;
	ButtonGroup locomotiveGroup;

	JRadioButton cabooseButton;
	JRadioButton presidentialButton;
	JRadioButton firstClassButton;
	JRadioButton openAirButton;
	ButtonGroup seatingGroup;

	JLabel adultLabel;
	JTextField adultTF;
	JLabel childrenLabel;
	JTextField childrenTF;

	JButton calcButton;
	JLabel ansLabel;
	JTextField ansTF;

	JLabel errorLabel;

	public Lab5Client() {
		setTitle("Price Calculator");
		setBounds(100, 100, 320, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		dieselButton = new JRadioButton("Diesel");
		dieselButton.setBounds(25, 24, 100, 16);
		dieselButton.setSelected(true);
		getContentPane().add(dieselButton);

		steamButton = new JRadioButton("Steam");
		steamButton.setBounds(25, 64, 100, 16);
		getContentPane().add(steamButton);

		locomotiveGroup = new ButtonGroup();
		locomotiveGroup.add(dieselButton);
		locomotiveGroup.add(steamButton);

		cabooseButton = new JRadioButton("Caboose");
		cabooseButton.setBounds(170, 14, 100, 16);
		cabooseButton.setSelected(true);
		getContentPane().add(cabooseButton);

		presidentialButton = new JRadioButton("Presidential");
		presidentialButton.setBounds(170, 34, 140, 16);
		getContentPane().add(presidentialButton);

		firstClassButton = new JRadioButton("First Class");
		firstClassButton.setBounds(170, 54, 140, 16);
		getContentPane().add(firstClassButton);

		openAirButton = new JRadioButton("Open Air");
		openAirButton.setBounds(170, 74, 140, 16);
		getContentPane().add(openAirButton);

		seatingGroup = new ButtonGroup();
		seatingGroup.add(cabooseButton);
		seatingGroup.add(presidentialButton);
		seatingGroup.add(firstClassButton);
		seatingGroup.add(openAirButton);

		adultLabel = new JLabel("Adults");
		adultLabel.setBounds(30, 104, 50, 16);
		getContentPane().add(adultLabel);
		adultLabel.setVisible(false);

		adultTF = new JTextField();
		adultTF.setBounds(80, 104, 50, 16);
		getContentPane().add(adultTF);
		adultTF.setVisible(false);

		childrenLabel = new JLabel("Children");
		childrenLabel.setBounds(160, 104, 60, 16);
		getContentPane().add(childrenLabel);
		childrenLabel.setVisible(false);

		childrenTF = new JTextField();
		childrenTF.setBounds(225, 104, 50, 16);
		getContentPane().add(childrenTF);
		childrenTF.setVisible(false);

		calcButton = new JButton("Calculate");
		calcButton.setBounds(100, 144, 80, 16);
		getContentPane().add(calcButton);

		ansLabel = new JLabel("Answer");
		ansLabel.setBounds(80, 184, 50, 16);
		getContentPane().add(ansLabel);

		ansTF = new JTextField();
		ansTF.setBounds(150, 184, 75, 16);
		getContentPane().add(ansTF);
		ansTF.setEditable(false);

		errorLabel = new JLabel("");
		errorLabel.setBounds(40, 220, 200, 16);
		getContentPane().add(errorLabel);

		cabooseButton.addActionListener(this);
		presidentialButton.addActionListener(this);
		firstClassButton.addActionListener(this);
		openAirButton.addActionListener(this);
		calcButton.addActionListener(this);

		connectToServer();
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Diesel") || action.equals("Steam")) {
			ansTF.setText("");
			errorLabel.setText("");
		} else if (action.equals("Caboose")) {
			ansTF.setText("");
			errorLabel.setText("");
			adultLabel.setVisible(false);
			adultTF.setVisible(false);
			childrenLabel.setVisible(false);
			childrenTF.setVisible(false);
		} else if (action.equals("Presidential") || action.equals("First Class") || action.equals("Open Air")) {
			ansTF.setText("");
			errorLabel.setText("");
			adultLabel.setVisible(true);
			adultTF.setVisible(true);
			childrenLabel.setVisible(true);
			childrenTF.setVisible(true);
		} else if (action.equals("Calculate")) {
			calculate();
		}
	}

// ========================================================================

// Do not change anything above this line
// Two global variables have been defined here and you will need to add more
	final static String server = "127.0.0.1";
	final static int port = 25413;
	Socket socket;

// Then implement the following methods

// The following method connects to the ShapeCalcultorServer
	void connectToServer() {
		try {
			socket = new Socket(server, port);
		} catch (IOException e) {

			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

// The following method sends an appropriate command to the server
// Then reads the result and displays it in the answer text field
	void calculate() {

		if (socket.isClosed()) {
			connectToServer();
		}

		try {

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			String locomotiveType = dieselButton.isSelected() ? "Diesel" : "Steam";
			String seatType = null;
			int adult = 0;
			int children = 0;

			if (cabooseButton.isSelected()) {

				seatType = cabooseButton.getActionCommand();

			} else if (presidentialButton.isSelected()) {

				seatType = presidentialButton.getActionCommand();
				adult = Integer.parseInt(adultTF.getText());
				children = Integer.parseInt(childrenTF.getText());

			} else if (firstClassButton.isSelected()) {

				seatType = firstClassButton.getActionCommand();
				adult = Integer.parseInt(adultTF.getText());
				children = Integer.parseInt(childrenTF.getText());

			} else if (openAirButton.isSelected()) {
				seatType = openAirButton.getActionCommand();
				adult = Integer.parseInt(adultTF.getText());
				children = Integer.parseInt(childrenTF.getText());
			}

			writer.println(locomotiveType + " " + seatType + " " + adult + " " + children);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			double cost = Double.parseDouble(reader.readLine());
			// System.out.println("The cost of the trip is " + cost);

			ansTF.setText(Double.toString(cost));

		} catch (NumberFormatException ex) {

			errorLabel.setText("Wrong input!!");

		} catch (SocketException se) {
			connectToServer();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			errorLabel.setText("An Error Occured!");
		}

	}
}
