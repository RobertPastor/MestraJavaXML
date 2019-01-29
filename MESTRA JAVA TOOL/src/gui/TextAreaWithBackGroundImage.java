package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

public class TextAreaWithBackGroundImage extends JTextArea {

	private BufferedImage image = null;

	public TextAreaWithBackGroundImage(int rows, int columns) {
		super(rows, columns);
		try {
			// default background  image is SSS
			java.net.URL imageURL = TextAreaWithBackGroundImage.class.getResource("SSS.png");
			image = ImageIO.read(imageURL);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
	
	private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
	
	public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int w = image.getWidth();
        int h = image.getHeight();
        int neww = (int) Math.floor(w*cos+h*sin);
        int newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (image != null) {
			//System.out.println("width= " + getWidth() + " image width= " + image.getWidth());
			//System.out.println("height= " + getHeight() + " image height= " + image.getHeight());
			BufferedImage rotatedImage = rotate(image, Math.PI/4.);
			//int x = getWidth() - image.getWidth();
			//int y = getHeight() - image.getHeight();
			for (int i=0; i<(this.getHeight()/rotatedImage.getHeight())+1; i++) {
				for (int j=0 ; j<(this.getWidth()/rotatedImage.getWidth())+1; j++ ) {
					g2d.drawImage(rotatedImage, j*rotatedImage.getWidth(), i*rotatedImage.getHeight(), this);
				}
			}
		}
		super.paintComponent(g2d);
		this.validate();this.revalidate();this.repaint();
		g2d.dispose();
	}


	public void notifyChanges(String selectedFileType) {
		
		String resource = "SSS.png";
		switch (selectedFileType) {
		case "SSS":
			resource = "SSS.png";
			break;
		case "SRS":
			resource = "SRS.png";
			break;
		case "TS":
			resource = "TS.png";
			break;
		case "SDD":
			resource = "SDD.png";
			break;
		case "MF":
			resource = "MF.png";
			break;
		default:
			resource = "SSS.png";
			break;
		}
		try {
			java.net.URL imageURL = TextAreaWithBackGroundImage.class.getResource(resource);
			this.image = ImageIO.read(imageURL);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.validate();
		this.revalidate();
		this.repaint();
	}

}
