import javax.swing.*;
import java.awt.event.*;

public class Lab1 extends JFrame implements ActionListener {
	static final long serialVersionUID = 1l;
	private JTextField assemblerInstruction;
	private JTextField binaryInstruction;
	private JTextField hexInstruction;
	private JLabel errorLabel;
	
	public Lab1() {
		setTitle("XDS Sigma 9");
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE ASSEMBLY LANGUAGE TEXTFIELD AND BUTTON
		assemblerInstruction = new JTextField();
		assemblerInstruction.setBounds(25, 24, 134, 28);
		getContentPane().add(assemblerInstruction);

		JLabel lblAssemblyLanguage = new JLabel("Assembly Language");
		lblAssemblyLanguage.setBounds(30, 64, 160, 16);
		getContentPane().add(lblAssemblyLanguage);

		JButton btnEncode = new JButton("Encode");
		btnEncode.setBounds(200, 25, 117, 29);
		getContentPane().add(btnEncode);
		btnEncode.addActionListener(this);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE BINARY INSTRUCTION TEXTFIELD AND BUTTON
		binaryInstruction = new JTextField();
		binaryInstruction.setBounds(25, 115, 330, 28);
		getContentPane().add(binaryInstruction);

		JLabel lblBinary = new JLabel("Binary Instruction");
		lblBinary.setBounds(30, 155, 190, 16);
		getContentPane().add(lblBinary);

		JButton btnDecode = new JButton("Decode Binary");
		btnDecode.setBounds(200, 150, 150, 29);
		getContentPane().add(btnDecode);
		btnDecode.addActionListener(this);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE HEX INSTRUCTION TEXTFIELD AND BUTTON
		hexInstruction = new JTextField();
		hexInstruction.setBounds(25, 220, 134, 28);
		getContentPane().add(hexInstruction);

		JLabel lblHexEquivalent = new JLabel("Hex Instruction");
		lblHexEquivalent.setBounds(30, 260, 131, 16);
		getContentPane().add(lblHexEquivalent);

		JButton btnDecodeHex = new JButton("Decode Hex");
		btnDecodeHex.setBounds(200, 220, 150, 29);
		getContentPane().add(btnDecodeHex);
		btnDecodeHex.addActionListener(this);		
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE LABEL TO DISPLAY ERROR MESSAGES
		errorLabel = new JLabel("");
		errorLabel.setBounds(25, 320, 280, 16);
		getContentPane().add(errorLabel);
	}

	public void actionPerformed(ActionEvent evt) {
		errorLabel.setText("");
		if (evt.getActionCommand().equals("Encode")) {
			System.out.println("Caroline is so hot");
			encode();
		} else if (evt.getActionCommand().equals("Decode Binary")) {
			decodeBin();
		} else if (evt.getActionCommand().equals("Decode Hex")) {
			decodeHex();
		}
	}

	public static void main(String[] args) {
		Lab1 window = new Lab1();
		window.setVisible(true);
   
	}

// USE THE FOLLOWING METHODS TO CREATE A STRING THAT IS THE
// BINARY OR HEX REPRESENTATION OF A SORT OR INT

// CONVERT AN INT TO 8 HEX DIGITS
	String displayIntAsHex(int x) {
		String ans="";
		for (int i=0; i<8; i++) {
			int hex = x & 15;
			char hexChar = "0123456789ABCDEF".charAt(hex);
			ans = hexChar + ans;
			x = (x >> 4);
		}
		return ans;
	}

// CONVERT AN INT TO 32 BINARY DIGITS
	String displayIntAsBinary(int x) {
		String ans="";
		for(int i=0; i<32; i++) {
			ans = (x & 1) + ans;
			x = (x >> 1);
		}
		return ans;
	}
 //Manipulates the binary to remove low order "0" bits and set to specific length
 String catBin(String bin, int length) {
		String ans="";
    long x = Integer.parseInt(bin,2); 
		for(int i=0; i<length; i++) {
			ans = (x & 1) + ans;
			x = (x >> 1);
		}
		return ans;
	}

	void encode() {
    String input = assemblerInstruction.getText();
    try{
        String op = "";
        String reg = "";
        String val = "";
        String indexreg = "";
        boolean star = false;
        String[] codes = input.split(" ");
        String[] opside = codes[0].split(",");
        String[] regside = codes[1].split(",");
        
        op = opside[0];
        reg = opside[1];
//Sets high order bit to 1 if asterisk is present
        if (regside[0].contains("*"))
        {
            val = regside[0].substring(1,regside[0].length());
            star = true;
        }
        else
        {
            val = regside[0];
        }
        
        if(regside.length == 2)
        {
            indexreg = regside[1];
        }
        else
        {
            indexreg = "0";
        }
//converts all integers from assembly input to binary        
        int regint = Integer.parseInt(reg);
        reg = displayIntAsBinary(regint);
        int valint = Integer.parseInt(val);
        val = displayIntAsBinary(valint);
        int indexregint = Integer.parseInt(indexreg);
        indexreg = displayIntAsBinary(indexregint);
//below command converts operation from assembly to binary and takes into account the asterisk
        String conversion = decodeOPbin(op, star);
        reg = catBin(reg, 4);
        val = catBin(val, 17);
        indexreg = catBin(indexreg, 3);
        String bin = conversion + "" + reg + "" + indexreg + "" + val;
        
        String p1 = "";
        String p2 = "";
        String p3 = "";
        String p4 = "";
/*.parseLong is used in lieu in the case the presence of neg #s
which will conflict with Java operation of negative integers*/ 
         
        long spaces = Long.parseLong(bin,2);
//statements build 4 different 8 bit binary sequences, then combines them
        for(int i=0; i<32; i++) { 
            if(i < 8)
            {
                p4 = (spaces & 1) + p4;
            }
            if(i < 16 && i >= 8)
            {
                p3 = (spaces & 1) + p3;
            }
            if(i < 24 && i>=16)
            {
                p2 = (spaces & 1) + p2;
            }
            if(i < 32 && i>=24 )
            {
                p1 = (spaces & 1) + p1;
            }
    			spaces = (spaces >> 1);
    		}
       
       binaryInstruction.setText(p1 + " " + p2 + " " + p3 + " " + p4);
        
        long constInput = Long.parseLong(bin,2);
        binToHex(constInput);
    }
    catch(Exception e) {
        errorLabel.setText("ERROR : Bad Assembly Input");
    }
    
	}
//takes input string removes space characters and calls binary to assembly and hex methods
	void decodeBin() {
 
    
		String input = binaryInstruction.getText();
    try{
        input = input.replaceAll("\\s", "");
        
        long constInput = 0;
        if( input.length() != 32)
        {
           errorLabel.setText("ERROR : Bad Binary Input");
        }
        else
        {
            constInput = Long.parseLong(input, 2);
            binToAssem(constInput);
            binToHex(constInput);
        }
    }
    catch(Exception e) {
        errorLabel.setText("ERROR : Bad Binary Input");
    }

	}
//receives text, splits, then converts the 4 parts into binary then outputs binary then uses binary output to convert to assembly
	void decodeHex() {
    try{
    	String input = hexInstruction.getText();
        String[] codes = input.split(" ");
        String bin1 = displayIntAsBinary(Integer.parseInt(codes[0],16));
        String bin2 = displayIntAsBinary(Integer.parseInt(codes[1],16));
        String bin3 = displayIntAsBinary(Integer.parseInt(codes[2],16));
        String bin4 = displayIntAsBinary(Integer.parseInt(codes[3],16));
        
        String binary = catBin(bin1, 8) + " " +
                                catBin(bin2, 8) + " " +
                                catBin(bin3, 8) + " " +
                                catBin(bin4, 8);
                                
        binaryInstruction.setText(binary);
        
        String nospaces = binary.replaceAll("\\s", "");
        long constInput = Long.parseLong(nospaces, 2);
        binToAssem(constInput);
    }
    catch(Exception e) {
        errorLabel.setText("ERROR : Bad Hex Input");
    }
	}
// Converts binary to hexadecimal by iterating through variable position in the given binary sequence
  void binToHex(long constInput){
        String hex1 = "";
        String hex2 = "";
        String hex3 = "";
        String hex4 = "";
        long hexInput = constInput;
        for(int i=0; i<32; i++) {
            if(i < 8)
            {
                hex1 = (hexInput & 1) + hex1;
            }
            
            if(i < 16 && i >= 8)
            {
                hex2 = (hexInput & 1) + hex2;
            }
            
            if(i < 24 && i >= 16)
            {
                hex3 = (hexInput & 1) + hex3;
            }
            
            if(i < 32 && i >= 24)
            {
                hex4 = (hexInput & 1) + hex4;
            }
            hexInput = (hexInput >> 1);
        }
//(source, position) statements build the hexadecimal interpretation of the binary input
        String hexstr1 = displayIntAsHex(Integer.parseInt(hex4,2));
        String hexstr2 = displayIntAsHex(Integer.parseInt(hex3,2));
        String hexstr3 = displayIntAsHex(Integer.parseInt(hex2,2));
        String hexstr4 = displayIntAsHex(Integer.parseInt(hex1,2));
        
        hexInstruction.setText(hexstr1.substring(hexstr1.length() - 2, (hexstr1.length())) + " " +
        hexstr2.substring(hexstr2.length() - 2, (hexstr2.length())) + " " +
        hexstr3.substring(hexstr3.length() - 2, (hexstr3.length())) + " " +
        hexstr4.substring(hexstr4.length() - 2, (hexstr4.length())));
  }
//converts the binary value to a String for the assembly equivalent
  void binToAssem(long constInput){
        String dis = "";
        String reg = "";
        String star = "";
        String index = "";
        String val = "";
        String op="";
        String opstr = "";
        long temp = constInput;
        long inpt = constInput;
        for(int i=0; i<32; i++) {
          if( i>=24 && i <31)
          {
               op = (temp & 1) + op;
               
          }
          temp = (temp >> 1);
        }
//chooses how to decode the binary based on assembly 2 letter input        
        opstr = encodeOPbin(op);
        if(opstr.equals("LI"))
        {
            for(int i=0; i<32; i++) {
              if(i < 20)
              {
                   val = (inpt & 1) + val;
              }
              else if( i >= 20 && i <24)
              {
                  reg = (inpt & 1) + reg;
              }
              else if( i>=24 && i <31)
              {
                   op = (inpt & 1) + op;
              }
              else
              {
                  star = (inpt & 1) + star;
              }
              inpt = (inpt >> 1);
        		}
            
           if(star.equals("1"))
           {
               assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " *" + Integer.parseInt(val,2));
           }
           else
           {
               assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " " + Integer.parseInt(val,2));
           }
        }
        else
        {
//If the operation is anything BUT "LI" (without brackets etc.) this loop executes
        		for(int i=0; i<32; i++) {
              if(i < 17)
              {
                   dis = (inpt & 1) + dis;
              }
              else if( i >= 17 && i < 20)
              {
                  index = (inpt & 1) + index;
                  
              }
              else if( i >= 20 && i <24)
              {
                  reg = (inpt & 1) + reg;
              }
              else if( i>=24 && i <31)
              {
                   op = (inpt & 1) + op;
              }
              else
              {
                  star = (inpt & 1) + star;
              }
              inpt = (inpt >> 1);
        		}
           
           if(star.equals("1"))
           {
               if(index.equals("000"))
               {
                 assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " *" + Integer.parseInt(dis,2));
               }
               else
                 assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " *" + Integer.parseInt(dis,2) + "," + Integer.parseInt(index,2));
           }
           else
           {
               if(index.equals("000"))
               {
                 assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " " + Integer.parseInt(dis,2));
               }
               else
                 assemblerInstruction.setText(opstr + "," + Integer.parseInt(reg,2) + " " + Integer.parseInt(dis,2) + "," + Integer.parseInt(index,2));
           }
        }  
  }
//builds displayable binary representation, and takes the asterisk into account
  String decodeOPbin(String op, boolean star) {
  
      String ret = "";
      
      if(star)
      {
        
        if(op.equals("LW")){
            ret = "10110010";
        }
        else if(op.equals("AW")) {
            ret = "10110000";
        }
        else if(op.equals("LI")) {
            ret = "10100010";
        }
        else if(op.equals("STW")) {
            ret = "10110101 ";
        }
        else
        {
            ret = "no op";
        }
      }
      else
      {
        
        if(op.equals("LW")){
            ret = "00110010";
        }
        else if(op.equals("AW")) {
            ret = "00110000";
        }
        else if(op.equals("LI")) {
            ret = "00100010";
        }
        else if(op.equals("STW")) {
            ret = "00110101 ";
        }
        else
        {
            ret = "no op";
        }
      }
      return ret;
  }
//sets binary input to the respected assembly language
  String encodeOPbin(String op) {
  
      String ret = "";
      
        if(op.equals("0110010")){
            ret = "LW";
            
        }
        else if(op.equals("0110000")) {
;
            ret = "AW";
        }
        else if(op.equals("0100010")) {
            ret = "LI";
        }
        else if(op.equals("0110101")) {

            ret = "STW";
        }
        else
        {
            ret = "no op";
        }
      
      
      return ret;
  }
}