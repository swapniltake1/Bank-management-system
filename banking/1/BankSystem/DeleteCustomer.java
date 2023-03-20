import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class DeleteCustomer extends JInternalFrame implements ActionListener {

	private JPanel jpDel = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal;
	private JButton btnDel, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private	int total = 0;

	//String Type Array use to Load Records From File.
	private String records[][] = new String [500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	DeleteCustomer () {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Delete Account Holder", false, true, false, true);
		setSize (350, 235);

		jpDel.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	        lbName = new JLabel ("Person Name:");
		lbName.setForeground (Color.black);
	        lbName.setBounds (15, 55, 90, 25);
		lbDate = new JLabel ("Last Transaction:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 100, 25);
		lbBal = new JLabel ("Balance:");
		lbBal.setForeground (Color.black);
		lbBal.setBounds (15, 125, 80, 25);

		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		txtName = new JTextField ();
		txtName.setEnabled (false);
		txtName.setBounds (125, 55, 200, 25);
		txtDate = new JTextField ();
		txtDate.setEnabled (false);
		txtDate.setBounds (125, 90, 200, 25);
		txtBal = new JTextField ();
		txtBal.setEnabled (false);
		txtBal.setHorizontalAlignment (JTextField.RIGHT);
		txtBal.setBounds (125, 125, 200, 25);

		//Aligning The Buttons.
		btnDel = new JButton ("Delete");
		btnDel.setBounds (20, 165, 120, 25);
		btnDel.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 165, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpDel.add (lbNo);
		jpDel.add (txtNo);
		jpDel.add (lbName);
		jpDel.add (txtName);
		jpDel.add (lbDate);
		jpDel.add (txtDate);
		jpDel.add (lbBal);
		jpDel.add (txtBal);
		jpDel.add (btnDel);
		jpDel.add (btnCancel);

		//Restricting The User Input to only Numerics in Numeric TextBoxes.
		txtNo.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);
		//Checking the Accunt No. Provided By User on Lost Focus of the TextBox.
		txtNo.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) { }
			public void focusLost (FocusEvent fe) {
				if (txtNo.getText().equals ("")) { }
				else {
					rows = 0;
					populateArray ();	//Load All Existing Records in Memory.
					findRec ();		//Finding if Account No. Already Exist or Not.
				}
			}
		}
		);

		//Adding Panel to Window.
		getContentPane().add (jpDel);

		populateArray ();	//Load All Existing Records in Memory.

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnDel) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
			else {
				deletion ();	//Confirm Deletion of Current Record.
			}
		}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dispose();
		}

	}

	//Function use to load all Records from File when Application Execute.
	void populateArray () {

		try {
			fis = new FileInputStream ("Bank.dat");
			dis = new DataInputStream (fis);
			//Loop to Populate the Array.
			while (true) {
				for (int i = 0; i < 6; i++) {
					records[rows][i] = dis.readUTF ();
				}
				rows++;
			}
		}
		catch (Exception ex) {
			total = rows;
			if (total == 0) {
				JOptionPane.showMessageDialog (null, "Records File is Empty.\nEnter Records First to Display.",
					"BankSystem - EmptyFile", JOptionPane.PLAIN_MESSAGE);
				btnEnable ();
			}
			else {
				try {
					dis.close();
					fis.close();
				}
				catch (Exception exp) { }
			}
		}

	}

	//Function use to Find Record by Matching the Contents of Records Array with ID TextBox.
	void findRec () {

		boolean found = false;
		for (int x = 0; x < total; x++) {
			if (records[x][0].equals (txtNo.getText())) {
				found = true;
				showRec (x);
				break;
			}
		}
		if (found == false) {
			String str = txtNo.getText ();
			txtClear ();
			JOptionPane.showMessageDialog (this, "Account No. " + str + " doesn't Exist.",
					"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
		}

	}

	//Function which display Record from Array onto the Form.
	void showRec (int intRec) {

		txtNo.setText (records[intRec][0]);
		txtName.setText (records[intRec][1]);
		txtDate.setText (records[intRec][2] + ", " + records[intRec][3] + ", " + records[intRec][4]);
		txtBal.setText (records[intRec][5]);
		recCount = intRec;

	}

	//Confirming the Deletion Decision made By User of Program.
	void deletion () {

		try {
			//Show a Confirmation Dialog.
		    	int reply = JOptionPane.showConfirmDialog (this,
					"Are you Sure you want to Delete\n" + txtName.getText () + " Record From BankSystem?",
					"Bank System - Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			//Check the User Selection.
			if (reply == JOptionPane.YES_OPTION) {
				delRec ();	//Delete the Selected Contents of Array.
			}
			else if (reply == JOptionPane.NO_OPTION) { }
		} 

		catch (Exception e) {}

	}

	//Function use to Delete an Element from the Array.
	void delRec () {

		try {
			if (records != null) {
				for(int i = recCount; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						records[i][r] = records[i+1][r];				
						if (records[i][r] == null) break;
					}
				}
				total = total - 1;
				deleteFile ();
			}
		}
		catch (ArrayIndexOutOfBoundsException ex) { }

	}

	//Function use to Save Records to File After Deleting the Record of User Choice.
	void deleteFile () {

		try {
			FileOutputStream fos = new FileOutputStream ("Bank.dat");
			DataOutputStream dos = new DataOutputStream (fos);
			if (records != null) {
				for (int i = 0; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						dos.writeUTF (records[i][r]);
						if (records[i][r] == null) break;
					}
				}
				JOptionPane.showMessageDialog (this, "Record has been Deleted Successfuly.",
					"BankSystem - Record Deleted", JOptionPane.PLAIN_MESSAGE);
				txtClear ();
			}
			else { }
			dos.close();
			fos.close();
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog (this, "There are Some Problem with File",
						"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}

	}

	//Function use to Clear all TextFields of Window.
	void txtClear () {

		txtNo.setText ("");
		txtName.setText ("");
		txtDate.setText ("");
		txtBal.setText ("");
		txtNo.requestFocus ();

	}

	//Function use to Lock Controls of Window.
	void btnEnable () {

		txtNo.setEnabled (false);
		btnDel.setEnabled (false);

	}

}	