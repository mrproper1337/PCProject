import panels.ReadPanel;
import panels.WritePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * Created by JFormDesigner on Wed Jun 03 17:52:46 EEST 2015
 */



/**
 * @author Urka
 */
public class Main extends JFrame {
    public Main() {
        initComponents();

        setContentPane(new ReadPanel());
    }

    public static void main(String[] args) {
        Main form = new Main();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }

    private void menuItem1ActionPerformed(ActionEvent e) {
        setContentPane(new ReadPanel());
        revalidate();
    }

    private void menuItem2ActionPerformed(ActionEvent e) {
        setContentPane(new WritePanel());
        revalidate();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();

        //======== this ========
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u041f\u0435\u0440\u0435\u0439\u0442\u0438");

                //---- menuItem1 ----
                menuItem1.setText("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434");
                menuItem1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuItem1ActionPerformed(e);
                    }
                });
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f");
                menuItem2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuItem2ActionPerformed(e);
                    }
                });
                menu1.add(menuItem2);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGap(0, 794, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGap(0, 461, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
