package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main {

    private static final ProductTableFrame mainFrame = ProductTableFrame.getInstance();

    public static void main(String[] args) {
        setMenuBar();
    }

    private static void setMenuBar() {
        JMenuBar mb = new JMenuBar();

        // create a menu
        JMenu jMenu = new JMenu("Menu");
        // sub menu
        JMenuItem addProductMenu = new JMenuItem("Tambah Data");
        jMenu.add(addProductMenu);
        mb.add(jMenu);

        addProductMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductFormFrame frame = new ProductFormFrame(null);
                frame.setVisible(true);
            }
        });

        // add menubar to frame
        mainFrame.setJMenuBar(mb);
    }
}
