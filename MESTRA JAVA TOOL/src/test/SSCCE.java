package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SSCCE extends JPanel
{
	public SSCCE() 	{
		setLayout( new BorderLayout() );

		JTable table = new JTable(5, 5);
		table.setOpaque( false );
		DefaultTableCellRenderer renderer =
				(DefaultTableCellRenderer)table.getDefaultRenderer(Object.class);
		renderer.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane( table );
		scrollPane.setOpaque( false );
		scrollPane.getViewport().setOpaque( false );

		JPanel background = new JPanel( new BorderLayout() )
		{

			BufferedImage image = null;
			protected void getImage() {
				try {
					// default background  image is SSS
					java.net.URL imageURL = SSCCE.class.getResource("SSS.png");
					image = ImageIO.read(imageURL);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				getImage();
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		};
		background.add( scrollPane );
		add(background);
	}

	private static void createAndShowGUI() {
		JPanel panel = new JPanel();

		JFrame frame = new JFrame("SSCCE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new SSCCE());
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