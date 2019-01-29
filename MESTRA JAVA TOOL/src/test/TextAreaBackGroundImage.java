package test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class TextAreaBackGroundImage extends JTextArea {

	private BufferedImage image = null;

    public TextAreaBackGroundImage() {
        super(20, 20);
        setOpaque(false);
        try {
            image = ImageIO.read(TextAreaBackGroundImage.this.getClass().getResource("SRS.png"));
            image = ImageIO.read(TextAreaBackGroundImage.this.getClass().getResource("SSS.png"));
            image = ImageIO.read(TextAreaBackGroundImage.this.getClass().getResource("SDD.png"));
            //System.out.println("image found!!!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (image != null) {
            //int x = getWidth() - image.getWidth();
            //int y = getHeight() - image.getHeight();
            BufferedImage rotatedImage = rotate(image, Math.PI/4.);
            //System.out.println("width of the panel= " + this.getWidth() + " rotated image width= " + rotatedImage.getWidth());
            //System.out.println("rotated image height= " + rotatedImage.getHeight());
            
            for (int i=0; i<(this.getHeight()/rotatedImage.getHeight())+1; i++) {
            	//System.out.println("number of rows= " + (this.getHeight()/rotatedImage.getHeight()));
            	for (int j=0; j<(this.getWidth()/rotatedImage.getWidth())+1; j++) {
            		//System.out.println("number of columns= "+ (this.getWidth()/rotatedImage.getWidth()));
            		g2d.drawImage(rotatedImage, j*rotatedImage.getWidth(), i*rotatedImage.getHeight(), this); 
            	}
            }
        }
        super.paintComponent(g2d);
        this.validate();
        this.revalidate();
        this.repaint();
        g2d.dispose();
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TextAreaBackGroundImage());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    //The standard main method.
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createAndShowGUI();
            }
        });
    }

}
