import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Lab2 extends JFrame implements ActionListener {
	JButton open = new JButton("Next Program");
	JTextArea result = new JTextArea(20,40);
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	
	public Lab2() {
		setLayout(new java.awt.FlowLayout());
		setSize(500,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(open); open.addActionListener(this);
		scroller.getViewport().add(result);
		add(scroller);
		add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		result.setText("");	//clear TextArea for next program
		errors.setText("");
		processProgram();
	}
	
	public static void main(String[] args) {
		Lab2 display = new Lab2();
		display.setVisible(true);
	}
	
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}

	private void processProgram() {
	    // Get file path of the program from user
	    String filePath = getFileName();
	    if (filePath == null) {
	        errors.setText("No program selected");
	        return;
	    }

	    try (Scanner scanner = new Scanner(new File(filePath))) {
	        //stores variables and their values
	        Map<String, Double> variables = new HashMap<>();

	        //stores program statements
	        List<String> statements = new ArrayList<>();

	        // Read each statement in the program and add to statements list
	        while (scanner.hasNextLine()) {
	            String statement = scanner.nextLine();
	            if (!statement.isBlank()) {
	                statements.add(statement);
	            }
	        }

	        // Execute the program statements
	        int lineNumber = 0;
	        while (lineNumber < statements.size()) {
	            String statement = statements.get(lineNumber);
	            // Check if statement is a print statement
	          if (statement.startsWith("PRINT")) {
	                String variableName = statement.substring(5).trim();

	                // Check if variable is in the map
	                if (variables.containsKey(variableName)) {
	                    double value = variables.get(variableName);
	                    result.append(String.format("%.2f\n", value));
	                } else {
	                    errors.setText("Invalid");
	                    return;
	                }
	            }
	         // Check if statement is a conditional statement
	            else if (statement.startsWith("IF")) {
	                try {
	                    List<String> parts = Arrays.asList(statement.split(" "));
	                    if (parts.size() < 6) {
	                        errors.setText("Invalid IF Command");
	                    }
	                    String variableName = parts.get(1);
	                    double value = evaluateExpression(parts.get(3), variables);
	                    if (variables.containsKey(variableName) && variables.get(variableName) == value) {
	                    	if (parts.get(5).equals("END")) {
	                        	if (parts.size() == 6) {
	                        		errors.setText("End of Program");
	                        		break;
	                        	}
	                        }
	                    	
	                    	if (parts.get(5).equals("PRINT")) {
	                            if (parts.size() == 7) {
	                            	String varToPrint = parts.get(6);
	                                result.append(String.format("%.2f\n", variables.get(varToPrint)));
	                            } else {
	                                errors.setText("Invalid Command");
	                            }
	                        }
	                    	if (parts.get(5).equals("GOTO")) {
	                            try {
	                                if (parts.size() == 7) {
	                                    int lineNum = Integer.parseInt(parts.get(6)) - 2;
	                                    if (lineNum >= 0 && lineNum < statements.size()) {
	                                        lineNumber = lineNum;
	                                    }
	                                } else {
	                                    errors.setText("Invalid line number in GOTO statement");
	                                    return;
	                                }
	                            } catch (NumberFormatException e) {
	                                errors.setText("Invalid GOTO statement");
	                                return;
	                            }
	                        }
	                    	if(parts.get(6).equals("=")) {
	                    		String varToAssign = parts.get(5);
	                            double assignValue = evaluateExpression(parts.get(7), variables);
	                            variables.put(varToAssign, assignValue);
	                    	}
	                    	
	                     }
	                    
	                } catch (ArrayIndexOutOfBoundsException e) {
	                    errors.setText("Invalid or Incomplete IF Statement");
	                }
	            }
	            // Check if the statement is a variable assignment
	          
	            else if (statement.contains("=")) {
	            	try {
	                List<String> parts = Arrays.asList(statement.split("="));
	                String variableName = parts.get(0).trim();
	                String expression = parts.get(1).trim();

	                // Evaluate the expression and store the result in the variable
	                double value = evaluateExpression(expression, variables);
	                variables.put(variableName, value);
	            	} catch (IllegalArgumentException e) {
	            		errors.setText("Variable input error");
	            	}
	            } 
	            	
	         // Check if the statement is a GOTO statement
	            else if (statement.startsWith("GOTO")) {
	                List<String> parts = Arrays.asList(statement.split(" "));
	                 try {
	                    int lineNum = Integer.parseInt(parts.get(1)) - 2;
	                   if (lineNum >= 0 && lineNum < statements.size()) {
	                        lineNumber = lineNum;
	                        lineNum++;
	                    } else {
	                        errors.setText("Invalid line number in GOTO statement");
	                        return;
	                    }
	                } catch (NumberFormatException e) {
	                    errors.setText("Invalid line number in GOTO statement");
	                    return;
	                }
	            }
	            // Check if the statement is the END statement
	            else if (statement.equals("END")) {
	            	errors.setText("End of Program");
	                break;
	            }
	            // If the statement is invalid
	            else {
	                errors.setText("Invalid Statement");
	                return;
	           }

	            // Move to the next statement
	            lineNumber++;
	        }
	    } catch (FileNotFoundException e) {
	        errors.setText("Program file not found");
	    }
	}

	private static double evaluateExpression(String expression, Map<String, Double> variables) throws IllegalArgumentException {
	    List<String> tokens = Arrays.asList(expression.split(" "));
	    double result = 0.0;
	    String operator = "+";

	    for (String token : tokens) {
	        if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
	            operator = token;
	        } else {
	            double value;
	            if (variables.containsKey(token)) {
	                value = variables.get(token);
	            } else {
	                try {
	                    value = Double.parseDouble(token);
	                } catch (NumberFormatException e) {
	                    throw new IllegalArgumentException("Undefined variable: " + token);
	                }
	            }

	            switch (operator) {
	                case "+":
	                    result += value;
	                    break;
	                case "-":
	                    result -= value;
	                    break;
	                case "*":
	                    result *= value;
	                    break;
	                case "/":
	                    if (value == 0.0) {
	                        throw new IllegalArgumentException("Division by zero");
	                    }
	                    result /= value;
	                    break;
	            }
	        }
	    }

	    return result;
	}

}