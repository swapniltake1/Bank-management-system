import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class WithdrawMoney extends JInternalFrame implements ActionListener {

	private JPanel jpWith = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbWithdraw;
	private JTextField txtNo, txtName, txtWithdraw;
	private JComboBox cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private	int total = 0;
	private	int curr;
	private	int withdraw;

	//String Type Array use to Load Records From File.
	private String records[][] = new String [500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	WithdrawMoney () {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Withdraw Money", false, true, false, true);
		setSize (335, 235);

		jpWith.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	        lbName = new JLabel ("Person Name:");
		lbName.setForeground (Color.black);
	        lbName.setBounds (15, 55, 80, 25);
		lbDate = new JLabel ("With. Date:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 80, 25);
		lbWithdraw = new JLabel ("With. Amount:");
		lbWithdraw.setForeground (Color.black);
		lbWithdraw.setBounds (15, 125, 80, 25);

		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
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
		txtNo.setBounds (105, 20, 205, 25);

		txtName = new JTextField ();
		txtName.setEnabled (false);
		txtName.setBounds (105, 55, 205, 25);
		txtWithdraw = new JTextField ();
		txtWithdraw.setHorizontalAlignment (JTextField.RIGHT);
		txtWithdraw.setBounds (105, 125, 205, 25);

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
		txtWithdraw.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);

		//Creating Date Option.
		String Months[] = {"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"};
		cboMonth = new JComboBox (Months);
		cboDay = new JComboBox ();
		cboYear = new JComboBox ();
		for (int i = 1; i <= 31; i++) {
			String days = "" + i;
			cboDay.addItem (days);
		}
		for (int i = 2000; i <= 2015; i++) {
			String years = "" + i;
			cboYear.addItem (years);
		}

		//Aligning The Date Option Controls.
		cboMonth.setBounds (105, 90, 92, 25);
		cboDay.setBounds (202, 90, 43, 25);
		cboYear.setBounds (250, 90, 60, 25);

		//Aligning The Buttons.
		btnSave = new JButton ("Save");
		btnSave.setBounds (20, 165, 120, 25);
		btnSave.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (185, 165, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpWith.add (lbNo);
		jpWith.add (txtNo);
		jpWith.add (lbName);
		jpWith.add (txtName);
		jpWith.add (lbDate);
		jpWith.add (cboMonth);
		jpWith.add (cboDay);
		jpWith.add (cboYear);
		jpWith.add (lbWithdraw);
		jpWith.add (txtWithdraw);
		jpWith.add (btnSave);
		jpWith.add (btnCancel);

		//Adding Panel to Window.
		getContentPane().add (jpWith);

		populateArray ();	//Load All Existing Records in Memory.

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
			else if (txtWithdraw.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Withdraw Amount.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtWithdraw.requestFocus ();
			}
			else {
				withdraw = Integer.parseInt (txtWithdraw.getText ());
				if (curr == 0) {
					JOptionPane.showMessageDialog (this, txtName.getText () + " doesn't have any Amount in Balance.",
							"BankSystem - EmptyAccount", JOptionPane.PLAIN_MESSAGE);
					txtClear ();
				}
				else if (withdraw > curr) {
					JOptionPane.showMessageDialog (this, "Withdraw Amount can't greater than Actual Balance.",
							"BankSystem - Large Amount", JOptionPane.PLAIN_MESSAGE);
					txtWithdraw.setText ("");
					txtWithdraw.requestFocus ();
				}
				else {
					editRec ();	//Update the Contents of Array.
				}
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
	public void showRec (int intRec) {

		txtNo.setText (records[intRec][0]);
		txtName.setText (records[intRec][1]);
		curr = Integer.parseInt (records[intRec][5]);
		recCount = intRec;

	}

	//Function use to Clear all TextFields of Window.
	void txtClear () {

		txtNo.setText ("");
		txtName.setText ("");
		txtWithdraw.setText ("");
		txtNo.requestFocus ();

	}

	//Function use to Edit an Element's Value of the Array.
	public void editRec () {

		records[recCount][0] = txtNo.getText ();
		records[recCount][1] = txtName.getText ();
		records[recCount][2] = "" + cboMonth.getSelectedItem ();
		records[recCount][3] = "" + cboDay.getSelectedItem ();
		records[recCount][4] = "" + cboYear.getSelectedItem ();
		records[recCount][5] = "" + (curr - withdraw);
		editFile ();	//Save This Array to File.
	
	}

	//Function use to Save Records to File After editing the Record of User Choice.
	public void editFile () {

		try {
			FileOutputStream fos = new FileOutputStream ("Bank.dat");
			DataOutputStream dos = new DataOutputStream (fos);
			if (records != null) {
				for (int i = 0; i < total; i++) {
					for (int c = 0; c < 6; c++) {
						dos.writeUTF (records[i][c]);
						if (records[i][c] == null) break;
					}
				}
				JOptionPane.showMessageDialog (this, "The File is Updated Successfully",
						"BankSystem - Record Saved", JOptionPane.PLAIN_MESSAGE);
				txtClear ();
				dos.close();
				fos.close();
			}
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog (this, "There are Some Problem with File",
					"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}
	
	}

	//Function use to Lock all Buttons of Window.
	void btnEnable () {

		txtNo.setEnabled (false);
		cboMonth.setEnabled (false);
		cboDay.setEnabled (false);
		cboYear.setEnabled (false);
		txtWithdraw.setEnabled (false);
		btnSave.setEnabled (false);

	}

}	