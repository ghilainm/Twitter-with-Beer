package JavaGui;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public static Image DELETE_BUTTON = null;
	public static Image DELETE_BUTTON_SELECTED = null;
	public final static int DELETE_BUTTON_HEIGHT = 30;
	public final static int DELETE_BUTTON_WIDTH = 30;
	
	public static void InitImageLoader(){
		File f = new File("cross.gif");
		try {
			DELETE_BUTTON = ImageIO.read(f);
			f = new File("crossSelected.gif");
			DELETE_BUTTON_SELECTED = ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
