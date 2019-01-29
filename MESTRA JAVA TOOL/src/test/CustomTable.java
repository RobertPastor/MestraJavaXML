package test;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CustomTable extends JTable {
	
	BufferedImage image = null;

	public CustomTable(int rows, int columns) {
		super(rows, columns);
		this.setOpaque(false);
		try {
			// default background  image is SSS
			java.net.URL imageURL = CustomTable.class.getResource("SSS.png");
			image = ImageIO.read(imageURL);
			System.out.println("image found !!!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		final Component c = super.prepareRenderer(renderer, row, column);
		if (c instanceof JComponent){
			((JComponent) c).setOpaque(false);                    
		}
		return c;
	}

	@Override
	public void paint(Graphics g) {
		System.out.println(" paint ");
		//draw image in centre
		final int imageWidth = image.getWidth();
		final int imageHeight = image.getHeight();
		final int x = (this.getWidth() - imageWidth);
		final int y = (this.getHeight() - imageHeight);
		g.drawImage(image, 0, 0, null, null);
		super.paintComponent(g);
		this.validate();
		this.revalidate();
		this.repaint();
	}



private static void createAndShowGUI() {
	JPanel panel = new JPanel();

	final JScrollPane sp = new JScrollPane(new CustomTable(10, 5));
	
	JFrame frame = new JFrame("SSCCE");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(sp);
	frame.setLocationByPlatform( true );
	frame.pack();
	frame.setVisible( true );
}

public static void main(String[] args) 	{
	EventQueue.invokeLater(new Runnable()
	{
		public void run()
		{
			createAndShowGUI();
		}
	});
}


}
