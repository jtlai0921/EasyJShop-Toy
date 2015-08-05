package cc.openhome.frame;

import cc.openhome.img.ImageMementoManager;
import cc.openhome.menu.SavableFileFilter;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ImageInternalFrame extends JInternalFrame {
    private JFileChooser saveFileChooser;

    private MainFrame mainFrame;
    private CanvasComponent canvas;

    public CanvasComponent getCanvas() {
        return canvas;
    }

    public ImageInternalFrame(MainFrame mainFrame, String title, Image image) {
        super(title, true, true, true, true);

        this.mainFrame = mainFrame;
        
        initComponents(mainFrame, image);
        initEventListeners();
    }

    private void initComponents(MainFrame mainFrame1, Image image) {
        canvas = new CanvasComponent(image, mainFrame1);
        saveFileChooser = new JFileChooser();
        saveFileChooser.addChoosableFileFilter(new SavableFileFilter());
        setFrameIcon(mainFrame1.getIcon());
        mainFrame1.getMementoManagers().put(canvas, new ImageMementoManager());
        JPanel panel = new JPanel();
        canvas.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(canvas);
        JScrollPane scrollPanel = new JScrollPane(panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPanel);
    }

    private void initEventListeners() {
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameOpened(InternalFrameEvent e) {
                mainFrame.updateMenuStatus();
                mainFrame.getEditMenu().setEditInfo(getCanvas());
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                saveOrNot();
            }

            public void internalFrameClosed(InternalFrameEvent e) {
                mainFrame.updateMenuStatus();
            }

            public void internalFrameIconified(InternalFrameEvent e) {
                try {
                    setSelected(false);
                    mainFrame.updateMenuStatus();
                } catch (PropertyVetoException ex) {
                    throw new RuntimeException(ex);
                }
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
                mainFrame.updateMenuStatus();
            }

            public void internalFrameActivated(InternalFrameEvent e) {
                mainFrame.updateMenuStatus();
            }
        });
    }

    private void saveOrNot() throws HeadlessException {
        deIconified();
        if (getTitle().startsWith("*")) {
            int option = JOptionPane.showOptionDialog(null,
                    getTitle().substring(1) + " is unsaved, save?", "save?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, mainFrame.smallLogo, null, null);
            if (option == JOptionPane.YES_OPTION) {
                saveImageFile();
            }
        }
        close();
    }

    public void deIconified() {
        try {
            setIcon(false);
            setSelected(true);
        } catch (PropertyVetoException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void close() {
        mainFrame.getMementoManagers().remove(getCanvas());
        setVisible(false);
        dispose();
    }

    public void saveImageFile() {
        if (title.endsWith("untitled")) {
            saveImageFileAs();
        } else if (title.startsWith("*")) {
            saveTo(new File(title));
        }
    }

    public void saveImageFileAs() {
        if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = getSelectedFile();
            if (file.exists()) {
                confirmOverwrite(file);
            } else {
                saveTo(file);
            }
        }
    }

    private void confirmOverwrite(File file) throws HeadlessException {
        int option = JOptionPane.showOptionDialog(null,
                file.toString() + " exists, overwrite?", "overwrite?", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, mainFrame.smallLogo, null, null);
        switch (option) {
            case JOptionPane.YES_OPTION: // overwrite
                saveTo(file);
                break;
            case JOptionPane.NO_OPTION:
                saveImageFileAs();
                break;
        }
    }

    private File getSelectedFile() {
        File file = saveFileChooser.getSelectedFile();
        String filename = file.toString();
        String lowerCaseFilename = filename.toLowerCase();
        // the default extension filename is 'jpg'.
        if (!lowerCaseFilename.endsWith(".jpg") && !lowerCaseFilename.endsWith(".png")) {
            filename = filename + ".jpg";
            file = new File(filename);
        }
        return file;
    }

    private void saveTo(File file) {
        saveImage(createImage(), file);
        setTitle(file.toString());
    }

    private BufferedImage createImage() {
        Image image = getCanvas().getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        return bufferedImage;
    }

    private void saveImage(BufferedImage bufferedImage, File file) {
        String filename = file.toString();
        try {
            ImageIO.write(bufferedImage, filename.substring(filename.lastIndexOf('.') + 1), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() {
        try {
            pack();
            setVisible(true);
            setSelected(true);
            setMaximum(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}