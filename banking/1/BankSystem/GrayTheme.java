import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GrayTheme extends DefaultMetalTheme {

	public String getName() {

		return "Cool Gray";
	}
	// greenish colors
	private final ColorUIResource primary1 = new ColorUIResource(225, 225, 225);
	private final ColorUIResource primary2 = new ColorUIResource(165, 165, 165);
	private final ColorUIResource primary3 = new ColorUIResource(175, 175, 175);

	protected ColorUIResource getPrimary1() { return primary1; }
	protected ColorUIResource getPrimary2() { return primary2; }
	protected ColorUIResource getPrimary3() { return primary3; }

}