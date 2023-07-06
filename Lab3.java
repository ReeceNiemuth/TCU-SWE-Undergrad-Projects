import javax.swing.*;
import javax.swing.plaf.synth.SynthSeparatorUI;
import java.awt.FileDialog;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;
public class Lab3 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private String currentSurname = "";
    private String currentGivenName = "";
    private JButton addButton;
    private JLabel addressLabel;
    private JPanel addressPanel;
    private JTextField addressTextField;
    private JPanel buttonPanel;
    private JLabel cityLabel;
    private JPanel cityStatePanel;
    private JTextField cityTextField;
    private JButton deleteButton;
    private JButton findButton;
    private JButton firstButton;
    private JLabel givenNameLabel;
    private JPanel givenNamePanel;
    private JTextField givenNameTextField;
    private JButton lastButton;
    private JButton nextButton;
    private JButton previousButton;
    private JLabel stateLabel;
    private JTextField stateTextField;
    private JLabel surnameLabel;
    private JPanel surnamePanel;
    private JTextField surnameTextField;
    private JButton updateButton;
    String bookFile = null;
    String indexFile = null;
    RandomAccessFile index;
    RandomAccessFile book;
    public Lab3() {
        setTitle("Address Book");setBounds(100, 100, 704, 239);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(5, 0));
        surnamePanel = new JPanel();
        surnameLabel = new JLabel();
        surnameTextField = new JTextField();
        givenNamePanel = new JPanel();
        givenNameLabel = new JLabel();
        givenNameTextField = new JTextField();
        addressPanel = new JPanel();
        addressLabel = new JLabel();
        addressTextField = new JTextField();
        cityStatePanel = new JPanel();
        cityLabel = new JLabel();
        cityTextField = new JTextField();
        stateLabel = new JLabel();
        stateTextField = new JTextField();
        buttonPanel = new JPanel();
        firstButton = new JButton();
        nextButton = new JButton();
        previousButton = new JButton();
        lastButton = new JButton();
        findButton = new JButton();
        addButton = new JButton();
        deleteButton = new JButton();
        updateButton = new JButton();
        surnamePanel.setName("surnamePanel");
        surnameLabel.setText("Surname");
        surnameLabel.setName("surnameLabel");
        surnamePanel.add(surnameLabel);
        surnameTextField.setColumns(45);
        surnameTextField.setText("");
        surnameTextField.setName("surnameTextField");
        surnamePanel.add(surnameTextField);
        getContentPane().add(surnamePanel);
        givenNamePanel.setName("givenNamePanel");
        givenNameLabel.setText("Given Names");givenNameLabel.setName("givenNameLabel");
        givenNamePanel.add(givenNameLabel);
        givenNameTextField.setColumns(45);
        givenNameTextField.setText("");
        givenNameTextField.setName("givenNameTextField");
        givenNamePanel.add(givenNameTextField);
        getContentPane().add(givenNamePanel);
        addressPanel.setName("addressPanel");
        addressLabel.setText("Street Address");
        addressLabel.setName("addressLabel");
        addressPanel.add(addressLabel);
        addressTextField.setColumns(45);
        addressTextField.setText("");
        addressTextField.setName("addressTextField");
        addressPanel.add(addressTextField);
        getContentPane().add(addressPanel);
        cityStatePanel.setName("cityStatePanel");
        cityLabel.setText("City");
        cityLabel.setName("cityLabel");
        cityStatePanel.add(cityLabel);
        cityTextField.setColumns(30);
        cityTextField.setText("");
        cityTextField.setName("cityTextField");
        cityStatePanel.add(cityTextField);
        stateLabel.setText("State");
        stateLabel.setName("stateLabel");
        cityStatePanel.add(stateLabel);
        stateTextField.setColumns(5);
        stateTextField.setText("");
        stateTextField.setName("stateTextField");
        cityStatePanel.add(stateTextField);
        getContentPane().add(cityStatePanel);buttonPanel.setName("buttonPanel");
        firstButton.setText("First");
        firstButton.setName("firstButton");
        firstButton.addActionListener(this);
        buttonPanel.add(firstButton);
        nextButton.setText("Next");
        nextButton.setName("nextButton");
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        previousButton.setText("Previous");
        previousButton.setName("previousButton");
        previousButton.addActionListener(this);
        buttonPanel.add(previousButton);
        lastButton.setText("Last");
        lastButton.setName("lastButton");
        lastButton.addActionListener(this);
        buttonPanel.add(lastButton);
        findButton.setText("Find");
        findButton.setName("findButton");
        findButton.addActionListener(this);
        buttonPanel.add(findButton);
        addButton.setText("Add");
//        addButton.setEnabled(false);
        addButton.setName("addButton");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);
        deleteButton.setText("Delete");
//        deleteButton.setEnabled(false);
        deleteButton.setName("deleteButton");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);
        updateButton.setText("Update");
//        updateButton.setEnabled(false);
        updateButton.setName("updateButton");
        updateButton.addActionListener(this);buttonPanel.add(updateButton);
        getContentPane().add(buttonPanel);
        getFiles();
        try {
            index = new RandomAccessFile(indexFile, "r");
            book = new RandomAccessFile(bookFile, "r");
        } catch(IOException ioe) {
            System.out.println(ioe);
            System.exit(0);
        }
    }
    void getFiles() {
        FileDialog fd = new FileDialog(this, "Select the Address Book",
                FileDialog.LOAD);
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename == null)
            System.exit(0);
        bookFile = fd.getDirectory() + filename;
        fd = new FileDialog(this, "Select the Index File", FileDialog.LOAD);
        fd.setVisible(true);
        filename = fd.getFile();
        if (filename == null)
            System.exit(0);
        indexFile = fd.getDirectory() + filename;
    }
    public static void main(String[] args) {
        Lab3 window = new Lab3();
        window.setVisible(true);
    }
    /***************************************************************/
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("First")) {
            first();}
        else if(event.getActionCommand().equals("Next")) {
            next();
        }
        else if(event.getActionCommand().equals("Previous")) {
            previous();
        }
        else if(event.getActionCommand().equals("Last")) {
            last();
        }
        else if(event.getActionCommand().equals("Find")) {
            find();
        }
        else if(event.getActionCommand().equals("Add")) {
            add();
        }
        else if(event.getActionCommand().equals("Delete")) {
            delete();
        }
        else if(event.getActionCommand().equals("Update")) {
            update();
        }
    }
    //implement this method
//you may add additional methods as needed
//do not change any of the code that I have written
//other than to activate buttons for extra credit
    public long recursiveBinarySearch(String searchValue, String names, long start, long end) {
        try {
            if (start > end) {
                // We have searched the entire range and not found the value
                return -1;
            }
            int mid = (int) ((start + end)  / 2);
            index.seek(mid*8L);
            long indexVar = index.readLong();

            // Read the value at the midpoint
            book.seek(indexVar);
            String value = book.readUTF();
            String givenNames = book.readUTF();

            // Compare the value at the midpoint to the search value
            int comparison = value.compareTo(searchValue);
            int comparison2 = givenNames.compareTo(names);


            if (comparison == 0 && comparison2 == 0) {
                // We have found the value in the index file
                return mid;
            }
            else if (comparison < 0 || (comparison == 0 && comparison2 < 0)){
                // The search value is greater than the midpoint, search the right half

                return recursiveBinarySearch(searchValue, names, mid+1, end);
            } else {
                // The search value is less than the midpoint, search the left half
                return recursiveBinarySearch(searchValue, names, start, mid - 1);

            }
        } catch (IOException e) {
            System.out.println(e);
            return -1;
        }
    }


    private void clearFields() {
        surnameTextField.setText("");
        givenNameTextField.setText("");
        addressTextField.setText("");
        cityTextField.setText("");
        stateTextField.setText("");
    }
    private void displayEntry(String currentSurname2, String currentGivenName2, String
            address, String city, String state) {
        surnameTextField.setText(currentSurname2);
        givenNameTextField.setText(currentGivenName2);
        addressTextField.setText(address);
        cityTextField.setText(city);
        stateTextField.setText(state);
    }
    //displays first entry in files
    public void first() {
        try {
            index.seek(0);
            long indexVar = index.readLong();
            book.seek(indexVar);
            currentSurname = book.readUTF();
            currentGivenName = book.readUTF();
            String address = book.readUTF();
            String city = book.readUTF();
            String state = book.readUTF();
            displayEntry(currentSurname, currentGivenName, address, city, state);
        } catch (IOException e) {
            System.out.println(e);
        }
    }//changes the value of any new information on selected user

    public void update() {
        try (RandomAccessFile book = new RandomAccessFile(bookFile, "rw");
             RandomAccessFile index = new RandomAccessFile(indexFile, "rw")) {
            String surname = surnameTextField.getText();
            String givenName = givenNameTextField.getText();
            // Search for the record in the index file
            long COUNTER = 0;
            index.seek(0);
            while (index.getFilePointer() < index.length()) {
                long indexVar = index.readLong();
                COUNTER += 1;
            }
            long position = recursiveBinarySearch(surname, givenName, 0, COUNTER);
            if (position == -1) {
                // I was just experimenting with JOptionPane for errors here
                JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update the record in the book file
            index.seek(position * 8L);
            long indexVar = index.readLong();
            book.seek(indexVar);
            book.writeUTF(surname);
            book.writeUTF(givenName);
            book.writeUTF(addressTextField.getText());
            book.writeUTF(cityTextField.getText());
            book.writeUTF(stateTextField.getText());

            // Update the GUI and clear the text fields
            clearFields();
            //I have only implemented this because its new to me and I thought it was cool
            JOptionPane.showMessageDialog(this, "Record updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //deletes user from files
    public void delete() {
        try (RandomAccessFile book = new RandomAccessFile(bookFile, "rw");
             RandomAccessFile index = new RandomAccessFile(indexFile, "rw")) {
            String surname = surnameTextField.getText();
            String givenName = givenNameTextField.getText();
            // Search for the record in the index file
            long COUNTER =  0;
            index.seek(0);
            while (index.getFilePointer() < index.length()) {
                long indexVar = index.readLong();
                COUNTER += 1;
            }
            long position = recursiveBinarySearch( surname, givenName,0,  COUNTER);
            if (position == -1) {
                // Record not found, display error message
                JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Delete the record from the book file
            index.seek(position * 8L);
            long indexVar = index.readLong();
            book.seek(indexVar);
            book.writeUTF("");
            book.writeUTF("");
            book.writeUTF("");
            book.writeUTF("");
            book.writeUTF("");

            // Delete the index entry
            byte[] buffer = new byte[(int)(index.length() - (position + 1) * 8)];
            index.seek((position + 1) * 8L);
            index.read(buffer);
            index.seek(position * 8L);
            index.write(buffer);

            // Truncate the index file
            index.setLength(index.length() - 8);

            // Update the GUI and clear the text fields
            clearFields();
            JOptionPane.showMessageDialog(this, "Record deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //adds user data to files
    public void add() {
        try (RandomAccessFile book = new RandomAccessFile(bookFile, "rw");
             RandomAccessFile index = new RandomAccessFile(indexFile, "rw")) {
            String surname = surnameTextField.getText();
            String givenName = givenNameTextField.getText();
            String address = addressTextField.getText();
            String city = cityTextField.getText();
            String state = stateTextField.getText();

            // Write user details to the end of the book file
            book.seek(book.length());
            long bookVar = book.getFilePointer();
            book.writeUTF(surname);
            book.writeUTF(givenName);
            book.writeUTF(address);
            book.writeUTF(city);
            book.writeUTF(state);

            // Write index entry to the end of the index file
            index.seek(index.length());
            index.writeLong(bookVar);

            // Update the GUI and clear the text fields
            displayEntry(surname, givenName, address, city, state);
            clearFields();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //finds and displays user data with given information
    public void find() {
        try {
            String surname = surnameTextField.getText();
            String givenName = givenNameTextField.getText();
            long COUNTER =  0;
            index.seek(0);
            while (index.getFilePointer() < index.length()) {
                long indexVar = index.readLong();
                COUNTER += 1;
            }
            long position = recursiveBinarySearch(surname,givenName ,0, COUNTER);
            if(position != -1){
                index.seek(position);
                long indexVar = index.readLong();
                book.seek(indexVar);
                currentSurname = book.readUTF();
                currentGivenName = book.readUTF();
                String address = book.readUTF();
                String city = book.readUTF();
                String state = book.readUTF();
                if (currentSurname.equalsIgnoreCase(surname) &&
                        currentGivenName.equalsIgnoreCase(givenName)) {
                    displayEntry(currentSurname, currentGivenName, address, city, state);
                } else {
                    clearFields();
                }

            }
            if (position == -1) {
                // Record not found, display error message
                JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //displays last entry in files
    public void last() {
        try {
            long lastIndex = index.length() - Long.BYTES;
            index.seek(lastIndex);
            long indexVar = index.readLong();
            book.seek(indexVar);
            currentSurname = book.readUTF();
            currentGivenName = book.readUTF();
            String address = book.readUTF();
            String city = book.readUTF();
            String state = book.readUTF();
            displayEntry(currentSurname, currentGivenName, address, city, state);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //goes to previous entry based on current entry
    public void previous() {
        try {

            if (currentSurname.equals("") || currentGivenName.equals("")) {
                first();
                return;
            }
            long currentIndex = index.getFilePointer();
            if (currentIndex == Long.BYTES) {
                return;
            }

            long indexVar = index.readLong();
            long previousIndex = index.getFilePointer() - 2 * Long.BYTES;
            index.seek(previousIndex);
            // Print the indexVar

            book.seek(indexVar);
            currentSurname = book.readUTF();
            currentGivenName = book.readUTF();
            String address = book.readUTF();
            String city = book.readUTF();
            String state = book.readUTF();
            displayEntry(currentSurname, currentGivenName, address, city, state);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //goes to next entry based on current entry
    public void next() {
        try {
            long currentIndex = index.getFilePointer();
            if (currentIndex == index.length() - Long.BYTES) {
                // We are at the end of the index file
                return;
            }

            long indexVar = index.readLong();
            // Print the indexVar

            book.seek(indexVar);
            currentSurname = book.readUTF();
            currentGivenName = book.readUTF();
            String address = book.readUTF();
            String city = book.readUTF();
            String state = book.readUTF();
            displayEntry(currentSurname, currentGivenName, address, city, state);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}