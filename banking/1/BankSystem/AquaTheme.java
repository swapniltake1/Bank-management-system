import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AquaTheme extends DefaultMetalTheme {

	public String getName() {

		return "Pure Aqua";

	}

	private final ColorUIResource primary1 = new ColorUIResource(102, 153, 153);
	private final ColorUIResource primary2 = new ColorUIResource(128, 192, 192);
	private final ColorUIResource primary3 = new ColorUIResource(159, 235, 235);
	private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
	private final ColorUIResource secondary3 = new ColorUIResource(160, 225, 225);

	protected ColorUIResource getPrimary1() { return primary1; }
	protected ColorUIResource getPrimary2() { return primary2; }
	protected ColorUIResource getPrimary3() { return primary3; }
	protected ColorUIResource getSecondary2() { return secondary2; }
	protected ColorUIResource getSecondary3() { return secondary3; }

}