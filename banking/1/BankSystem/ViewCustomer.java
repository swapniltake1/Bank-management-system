import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class ViewCustomer extends JInternalFrame {

	private JPanel jpShow = new JPanel ();

	private DefaultTableModel dtmCustomer;
	private JTable tbCustomer;
	private JScrollPane jspTable;

	private int row = 0;
	private int total = 0;

	//String Type Array use to Load Records into File.
	private String rowData[][];

	private FileInputStream fis;
	private DataInputStream dis;

	ViewCustomer () {

		//super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("View All Account Holders", false, true, false, true);
		setSize (475, 280);

		jpShow.setLayout (null);

		populateArray ();

		tbCustomer = makeTable ();
		jspTable = new JScrollPane (tbCustomer);
		jspTable.setBounds (20, 20, 425, 200);

		//Adding the Table to Panel.
		jpShow.add (jspTable);

		//Adding Panel to Window.
		getContentPane().add (jpShow);

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use to load all Records from File when Window Open.
	void populateArray () {

		//String Type Array use to Load Records into File.
		String rows[][] = new String [500][6];
		try {
			fis = new FileInputStream ("Bank.dat");
			dis = new DataInputStream (fis);
			//Loop to Populate the Array.
			while (true) {
				for (int i = 0; i < 6; i++) {
					rows[row][i] = dis.readUTF ();
				}
				row++;
			}
		}
		catch (Exception ex) {
			total = row;
			rowData = new String [total][4];
			if (total == 0) {
				JOptionPane.showMessageDialog (null, "Records File is Empty.\nEnter Records to Display.",
							"BankSystem - EmptyFile", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				for (int i = 0; i < total; i++) {
					rowData[i][0] = rows[i][0];
					rowData[i][1] = rows[i][1];
					rowData[i][2] = rows[i][2] + ", " + rows[i][3] + ", " + rows[i][4];
					rowData[i][3] = rows[i][5];
				}
				try {
					dis.close();
					fis.close();
				}
				catch (Exception exp) { }
			}
		}

	}

	//Function to Create the Table and Add Data to Show.
	private JTable makeTable () {

		//String Type Array use to Give Table Column Names.
		String cols[] = {"Account No.", "Customer Name", "Opening Date", "Bank Balance"};

		dtmCustomer  = new DefaultTableModel (rowData, cols);
		tbCustomer = new JTable (dtmCustomer) {
			public boolean isCellEditable (int iRow, int iCol) {
				return false;	//Disable All Columns of Table.
			}
		};
		//Sizing the Columns of Table.
		(tbCustomer.getColumnModel().getColumn(0)).setPreferredWidth (180);
		(tbCustomer.getColumnModel().getColumn(1)).setPreferredWidth (275);
		(tbCustomer.getColumnModel().getColumn(2)).setPreferredWidth (275);
		(tbCustomer.getColumnModel().getColumn(3)).setPreferredWidth (200);
		tbCustomer.setRowHeight (20);
		tbCustomer.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		return tbCustomer;

	}

}