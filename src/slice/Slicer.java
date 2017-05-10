package slice;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

public class Slicer {
	private Slicer() {
		throw new AssertionError("Tihs class should not be constructed!");
	}

	public static BufferedImage[] slice(BufferedImage img, int slices) {
		BufferedImage[] ret = new BufferedImage[slices];
		Raster r = img.getRaster();
		for (int i = 0; i < slices; i++) {
			ret[i] = copyImage(img);
			
			Graphics2D g2d = ret[i].createGraphics();
			
			g2d.setColor(new Color(255, 255, 255, 0));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			for(int j = i; j < img.getHeight(); j += slices)
				g2d.fill(new Rectangle(0, j, img.getWidth(), slices - 1));
			
			g2d.dispose();
		}

		return ret;
	}

	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public static void main(String[] args) throws Throwable {
		String inputfile = "file:///" + new File("../donald-trump.jpg").getCanonicalPath();
		String outputfile = "output%i%";
		
		BufferedImage img = ImageIO.read(new URL(inputfile));
		BufferedImage[] result = slice(img, 2);
		
		for(int i = 0; i < result.length; i++)
			ImageIO.write(result[i], "png", new File(outputfile.replace("%i%", ""+i) + ".png"));
		
	}
}