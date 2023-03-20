import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class BankHelp extends JInternalFrame {

	public BankHelp (String title, String filename) {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super (title, false, true, false, true);
		setSize (500, 350);

		HtmlPane html = new HtmlPane (filename);
		setContentPane (html);

		setVisible (true);

	}

}

class HtmlPane extends JScrollPane implements HyperlinkListener {

	JEditorPane html;

	public HtmlPane(String filename) {

		try {
			File f = new File (filename);
			String s = f.getAbsolutePath();
			s = "file:"+s;
			URL url = new URL(s);
			html = new JEditorPane(s);
			html.setEditable(false);
			html.addHyperlinkListener(this);
			JViewport vp = getViewport();
			vp.add(html);
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e);
		}
		catch (IOException e) {
			System.out.println("IOException: " + e);
		}

	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
	
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			linkActivated(e.getURL());
		}
	}

	protected void linkActivated(URL u) {

		Cursor c = html.getCursor();
		Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		html.setCursor(waitCursor);
		SwingUtilities.invokeLater(new PageLoader(u, c));

	}

	class PageLoader implements Runnable {

		PageLoader(URL u, Cursor c) {

			url = u;
			cursor = c;

		}

		public void run() {
	
			if (url == null) {
				html.setCursor(cursor);
				Container parent = html.getParent();
				parent.repaint();
			}
			else {
				Document doc = html.getDocument();
				try {
					html.setPage(url);
				}
				catch (IOException ioe) {
					html.setDocument(doc);
					getToolkit().beep();
				}
				finally {
					url = null;
					SwingUtilities.invokeLater(this);
				}
			}

		}

		URL url;
		Cursor cursor;

	}

}