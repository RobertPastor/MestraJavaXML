package FileChooser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
 
/**
<p>Attempt to improve the behaviour of the JFileChooser.  Anyone who has used the
the JFileChooser will probably remember that it lacks several useful features.
The following features have been added:

<ul>
<li>Double click to choose a file</li>
<li>Enter key to choose a file after typing the filename</li>
<li>Enter key to change to a different directory after typing the filename</li>
<li>Automatic rescanning of directories</li>
<li>A getSelectedFiles method that returns the correct selected files</li>
<li>Escape key cancels the dialog</li>
<li>Access to common GUI components, such as the OK and Cancel buttons</li>
<li>Removal of the useless Update and Help buttons in Motif L&F</li>
</ul>

<p>There are a lot more features that could be added to make the JFileChooser more
user friendly.  For example, a drop-down combo-box as the user is typing the name of
the file, a list of currently visited directories, user specified file filtering, etc.

<p>The look and feels supported are Metal, Window and Motif.  Each look and feel
puts the OK and Cancel buttons in different locations and unfortunately the JFileChooser
doesn't provide direct access to them.  Thus, for each look-and-feel the buttons must
be found.

<p>The following are known issues:  Rescanning doesn't work when in Motif L&F.  Some
L&Fs have components that don't become available until the user clicks a button.  For
example, the Metal L&F has a JTable but only when viewing in details mode.  The double
click to choose a file does not work in details mode.  There are probably more unknown
issues, but the changes made so far should make the JFileChooser easier to use.
*/
public class FileChooserFixer implements ActionListener, KeyListener, MouseListener, Runnable {
 
	final static Logger logger = Logger.getLogger(FileChooserFixer.class.getName()); 

    /*
    Had to make new buttons because when the original buttons are clicked
    they revert back to the original label text.  I.e. some programmer decided
    it would be a good idea to set the button text during an actionPerformed
    method.
    */
    private JFileChooser fileChooser = null;
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");
    private JList fileList = null;
    private JTextField filenameTextField = null;
    private ActionListener actionListener = null;
    private long rescanTime = 20000;
 
    public FileChooserFixer(JFileChooser fc, ActionListener a) {
        fileChooser = fc;
        actionListener = a;
 
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
 
        okButton.setMnemonic('O');
        cancelButton.setMnemonic('C');
 
        JButton oldOKButton = null;
        JButton oldCancelButton = null;
        JTextField[] textField = getTextFields(fc);
        JButton[] button = getButtons(fc);
        JList[] list = getLists(fc);
 
        String laf = javax.swing.UIManager.getLookAndFeel().getClass().getName();
 
        if (laf.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
            oldOKButton = button[0];
            oldCancelButton = button[1];
            filenameTextField = textField[0];
            fileList = list[0];
        }
        else if (laf.equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
            oldOKButton = button[0];
            oldCancelButton = button[1];
            filenameTextField = textField[0];
            fileList = list[0];
        }
        else if (laf.equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
            oldOKButton = button[0];
            oldCancelButton = button[2];
            button[1].setVisible(false); // hides the do-nothing 'Update' button
            button[3].setVisible(false); // hides the disabled 'Help' button
            filenameTextField = textField[1];
            fileList = list[0];
        }
 
        fix(oldOKButton, okButton);
        fix(oldCancelButton, cancelButton);
 
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        fileList.addMouseListener(this);
        addKeyListeners(fileChooser);
 
        new Thread(this).start(); // note: rescanning in Motif feel doesn't work
    }
 
    public void run() {
        logger.info( "FileChooserFixer: run");
        try {
            while (true) {
                Thread.sleep(rescanTime);
                Window w = SwingUtilities.windowForComponent(fileChooser);
                if (w != null && w.isVisible())
                    fileChooser.rescanCurrentDirectory();
            }
        } catch (Throwable err) {}
    }
 
    public long getRescanTime() {
        return this.rescanTime;
    }
 
    public void setRescanTime(long t) {
        if (t < 200)
            throw new IllegalArgumentException("Rescan time >= 200 required.");
 
        this.rescanTime = t;
    }
 
    private void addKeyListeners(Container c) {
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component d = c.getComponent(i);
            if (d instanceof Container)
                addKeyListeners((Container) d);
            d.addKeyListener(this);
        }
    }
 
    private static void fix(JButton oldButton, JButton newButton) {
        int index = getIndex(oldButton);
        Container c = oldButton.getParent();
        c.remove(index);
        c.add(newButton, index);
 
        newButton.setPreferredSize(oldButton.getPreferredSize());
        newButton.setMinimumSize(oldButton.getMinimumSize());
        newButton.setMaximumSize(oldButton.getMaximumSize());
    }
 
    private static int getIndex(Component c) {
        Container p = c.getParent();
        for (int i = 0; i < p.getComponentCount(); i++) {
            if (p.getComponent(i) == c)
                return i;
        }
        return -1;
    }
 
    public JButton getOKButton() {
        return this.okButton;
    }
 
    public JButton getCancelButton() {
        return this.cancelButton;
    }
 
    public JList getFileList() {
        return this.fileList;
    }
 
    public JTextField getFilenameTextField() {
        return  this.filenameTextField;
    }
 
    public JFileChooser getFileChooser() {
        return this.fileChooser;
    }
 
    protected JButton[] getButtons(JFileChooser fc) {
        Vector<Component> v = new Vector<Component>();
        Stack<Component> s = new Stack<Component>();
        s.push(fc);
        while (!s.isEmpty()) {
            Component c = (Component) s.pop();
 
            if (c instanceof Container) {
                Container d = (Container) c;
                for (int i = 0; i < d.getComponentCount(); i++) {
                    if (d.getComponent(i) instanceof JButton)
                        v.add(d.getComponent(i));
                    else
                        s.push(d.getComponent(i));
                }
            }
        }
 
        JButton[] arr = new JButton[v.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = (JButton) v.get(i);
 
        return arr;
    }
 
    protected JTextField[] getTextFields(JFileChooser fc) {
        
        Vector<Component> v = new Vector<Component>();
        Stack<Component> s = new Stack<Component>();
        s.push(fc);
        while (!s.isEmpty()) {
            Component c = (Component) s.pop();
 
            if (c instanceof Container) {
                Container d = (Container) c;
                for (int i = 0; i < d.getComponentCount(); i++) {
                    if (d.getComponent(i) instanceof JTextField)
                        v.add(d.getComponent(i));
                    else
                        s.push(d.getComponent(i));
                }
            }
        }
 
        JTextField[] arr = new JTextField[v.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = (JTextField) v.get(i);
 
        return arr;
    }
 
    protected JList[] getLists(JFileChooser fc) {
        logger.info( "FileChooserFixer: getLists");
        Vector<Component> v = new Vector<Component>();
        Stack<Component> s = new Stack<Component>();
        s.push(fc);
        while (!s.isEmpty()) {
            Component c = (Component) s.pop();
 
            if (c instanceof Container) {
                Container d = (Container) c;
                for (int i = 0; i < d.getComponentCount(); i++) {
                    if (d.getComponent(i) instanceof JList)
                        v.add(d.getComponent(i));
                    else
                        s.push(d.getComponent(i));
                }
            }
        }
 
        JList[] arr = new JList[v.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = (JList) v.get(i);
 
        return arr;
    }
 
    public File[] getSelectedFiles() {
        logger.info( "FileChooserFixer: getSelectedFiles");
        File[] f = fileChooser.getSelectedFiles();
        if (f.length == 0) {
            
            File file = fileChooser.getSelectedFile();
            if (file != null)
                f = new File[] { file };
        }
        return f;
    }
 
    public void mousePressed(MouseEvent evt) {
        logger.info( "FileChooserFixer: --------------------");
        logger.info( "FileChooserFixer: mouse Pressed");
        Object src = evt.getSource();
 
        if (src == fileList) {
            logger.info( "FileChooserFixer: source fileList");
            if (evt.getModifiers() != InputEvent.BUTTON1_MASK) return;
 
            int index = fileList.locationToIndex(evt.getPoint());
            if (index < 0) return;
            fileList.setSelectedIndex(index);
            File[] arr = getSelectedFiles();
 
            if (evt.getClickCount() == 2 ) {
                logger.info( "FileChooserFixer: second click");
            
                if ( (arr.length == 1) && arr[0].isFile() ) {
                    logger.info( "FileChooserFixer: file selected: "+arr[0].getAbsolutePath());
                    actionPerformed(new ActionEvent(okButton, 0, okButton.getActionCommand()));  
                }
            }
        }
    }
 
    public void mouseReleased(MouseEvent evt) {
        
    }
    
    public void mouseClicked(MouseEvent evt) {
        logger.info( "FileChooserFixer: -----------------------");
        logger.info( "FileChooserFixer: mouse clicked");
    }
    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
 
    public void keyPressed(KeyEvent evt) {
        
    	logger.info("FileChooserFixer: -----------------------");
    	logger.info("FileChooserFixer: Key Event");
        
        Object src = evt.getSource();
        int code = evt.getKeyCode();
 
        if (code == KeyEvent.VK_ESCAPE)
            actionPerformed(new ActionEvent(cancelButton, 0, cancelButton.getActionCommand()));
 
        if (src == filenameTextField) {
            if (code != KeyEvent.VK_ENTER) return;
            fileList.getSelectionModel().clearSelection();
            actionPerformed(new ActionEvent(okButton, 0, "enter"));
        }
    }
 
    public void keyReleased(KeyEvent evt) {}
    public void keyTyped(KeyEvent evt) {}
 
    public void actionPerformed(ActionEvent evt) {
    	logger.info( "FileChooserFixer action Performed!");
        Object src = evt.getSource();
 
        if (src == cancelButton) {
            if (actionListener != null)
                actionListener.actionPerformed(evt);
        }
        else if (src == okButton) {
        	logger.info( "FileChooserFixer action Performed: OK button");
            File[] selectedFiles = getSelectedFiles();
            Object obj = fileList.getSelectedValue(); // is null when no file is selected in the JList
            String text = filenameTextField.getText().trim();
 
            if (text.length() > 0 && (obj == null || selectedFiles.length == 0)) {
                File d = fileChooser.getCurrentDirectory();
                Vector<File> vec = new Vector<File>();
 
                StringTokenizer st = new StringTokenizer(text, "\"");
 
                while (st.hasMoreTokens()) {
                    String s = st.nextToken().trim();
                    if (s.length() == 0) continue;
 
                    File a = new File(s);
 
                    if (a.isAbsolute())
                        vec.add(a);
                    else
                        vec.add(new File(d, s));
                }
 
                File[] arr = new File[vec.size()];
                for (int i = 0; i < arr.length; i++)
                    arr[i] = (File) vec.get(i);
 
                selectedFiles = arr;
            }
 
            if (selectedFiles.length == 0) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
 
            if (selectedFiles.length == 1) {
                File f = selectedFiles[0];
 
                if (f.exists() && f.isDirectory() && 
                        text.length() > 0 && evt.getActionCommand().equals("enter")) {
                    fileChooser.setCurrentDirectory(f);
                    filenameTextField.setText("");
                    filenameTextField.requestFocus();
                    return;
                }
            }
 
            boolean filesOnly = (fileChooser.getFileSelectionMode() == JFileChooser.FILES_ONLY);
            boolean dirsOnly = (fileChooser.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY);
 
            if (filesOnly || dirsOnly) {
                for (int i = 0; i < selectedFiles.length; i++) {
                    File f = selectedFiles[i];
                    if (filesOnly && f.isDirectory() || dirsOnly && f.isFile()) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                }
            }
 
            fileChooser.setSelectedFiles(selectedFiles);
 
            if (actionListener != null)
                actionListener.actionPerformed(evt);
        }
    }
}
