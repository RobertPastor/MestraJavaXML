package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;

import mestra.MestraFileType.MestraFileTypeEnum;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;

public class CustomTableView extends TableView {

	BufferedImage image = null;

	public CustomTableView(DefaultTableViewModel model) {

		super(model);
		this.setOpaque(false);
		try {
			// default background  image is SSS
			java.net.URL imageURL = CustomTableView.class.getResource("SSS.png");
			this.image = ImageIO.read(imageURL);
			//System.out.println("image found !!!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.getTableHeader().setBackground(Color.blue);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		final Component c = super.prepareRenderer(renderer, row, column);
		if (c instanceof JComponent){
			((JComponent) c).setOpaque(false);                    
		}
		return c;
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
	public void paintComponent(Graphics g) {
		//System.out.println("==> Custom Table View paint component ");
		
		if (image != null) {
			//System.out.println("width= " + getWidth() + " image width= " + image.getWidth());
			//System.out.println("height= " + getHeight() + " image height= " + image.getHeight());
			BufferedImage rotatedImage = rotate(image, Math.PI/4.);
			//int x = getWidth() - image.getWidth();
			//int y = getHeight() - image.getHeight();
			for (int i=0; i<(this.getHeight()/rotatedImage.getHeight())+1; i++) {
				for (int j=0 ; j<(this.getWidth()/rotatedImage.getWidth())+1; j++ ) {
					g.drawImage(rotatedImage, j*rotatedImage.getWidth(), i*rotatedImage.getHeight(), this);
				}
			}
		}
		super.paintComponent(g);
		this.validate();
		this.revalidate();
		this.repaint();
		g.dispose();
	}

	public void notifyChanges(MestraFileTypeEnum mestraFileTypeEnum) {

		String resource = "SSS.png";
		switch (mestraFileTypeEnum) {
		case SSS:
			resource = "SSS.png";
			break;
		case SRS:
			resource = "SRS.png";
			break;
		case TS:
			resource = "TS.png";
			break;
		case SDD:
			resource = "SDD.png";
			break;
		case MF:
			resource = "MF.png";
			break;
		default:
			resource = "SSS.png";
			break;
		}
		try {
			java.net.URL imageURL = CustomTableView.class.getResource(resource);
			this.image = ImageIO.read(imageURL);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.validate();
		this.revalidate();
		this.repaint();
	}

}
