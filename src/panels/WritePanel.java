/*
 * Created by JFormDesigner on Wed Jun 03 18:04:03 EEST 2015
 */

package panels;

import pojo.Group;
import pojo.SportNormName;
import pojo.Student;
import tableModels.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author Urka
 */
public class WritePanel extends JPanel {

    static boolean readyToListen = false;

    public WritePanel() {
        initComponents();
        table1.setModel(new GroupModel(true));
        initCombos(0);

        for(Object g: ConnectHibernate.loadTable("from Group")){
            String st = ((Group)g).getGroupName();
            comboBox2.addItem(st);
        }
        for(Object g: ConnectHibernate.loadTable("from Student where groupId ="+GroupModel.groupList.
                get(0).getGroupId())){
            String st = ((Student)g).getName();
            comboBox3.addItem(st);
        }
        setReadyToListen(true);
    }

    private void comboBox1ItemStateChanged(ItemEvent e) {
        if(isReadyToListen())updateModel();
    }

    private void comboBox2ItemStateChanged(ItemEvent e) {
        if(isReadyToListen()){
            setReadyToListen(false);
            if(comboBox1.getSelectedIndex()==1)
                table1.setModel(new StudentModel(
                        GroupModel.groupList.get(comboBox2.getSelectedIndex()).getGroupId()));
            if(comboBox1.getSelectedIndex()==4){
                try{
                    table1.setModel(new ResultModel(((Student)ConnectHibernate.loadTable("from Student where groupId = "+
                            GroupModel.groupList.get(comboBox2.getSelectedIndex()).getGroupId()).get(0)).getStudentId()));
                    comboBox3.removeAllItems();
                    for(Object g: ConnectHibernate.loadTable("from Student where groupId ="+GroupModel.groupList.
                            get(comboBox2.getSelectedIndex()).getGroupId())){
                        String st = ((Student)g).getName();
                        comboBox3.addItem(st);
                    }
                }catch (IndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null,"В групі немає студентів, щоб додати\nстудентів перейдіть в таблицю групи");
                }
            }
            initCombos(comboBox1.getSelectedIndex());
            setReadyToListen(true);
        }
    }

    private void comboBox3ItemStateChanged(ItemEvent e) {
        if(isReadyToListen()){
            setReadyToListen(false);
            table1.setModel(new ResultModel(((Student)ConnectHibernate.loadTable("from Student where groupId = "+
                    GroupModel.groupList.get(comboBox2.getSelectedIndex()).getGroupId()).
                    get(comboBox3.getSelectedIndex())).
                    getStudentId()));
            initCombos(comboBox1.getSelectedIndex());
            setReadyToListen(true);
        }
    }

    public static void updateModel(){
        comboBox2.setVisible(false);
        comboBox3.setVisible(false);
        setReadyToListen(false);
        switch(comboBox1.getSelectedIndex()){
            case 0:
                table1.setModel(new GroupModel(true));
                comboBox2.removeAllItems();
                for(Object g: ConnectHibernate.loadTable("from Group")){
                    String st = ((Group)g).getGroupName();
                    comboBox2.addItem(st);
                }
                break;
            case 1:
                comboBox2.setVisible(true);
                table1.setModel(new StudentModel(
                        GroupModel.groupList.get(comboBox2.getSelectedIndex()).getGroupId()));
                break;
            case 2:
                table1.setModel(new SNNModel());
                break;
            case 3:
                table1.setModel(new SportNormModel());
                break;
            case 4:
                comboBox2.setVisible(true);
                comboBox3.setVisible(true);
                comboBox3.removeAllItems();
                for(Object g: ConnectHibernate.loadTable("from Student where groupId ="+GroupModel.groupList.get(0).getGroupId())){
                    String st = ((Student)g).getName();
                    comboBox3.addItem(st);
                }
                table1.setModel(new ResultModel(((Student)ConnectHibernate.loadTable("from Student where groupId = "+
                        GroupModel.groupList.get(0).getGroupId()).get(0)).getStudentId()));
                break;
        }
        initCombos(comboBox1.getSelectedIndex());
        setReadyToListen(true);
    }

    private static void initCombos(int tableId){
        ArrayList<String> genders,health,sn,courses,mode;

        genders = new ArrayList<>();
        genders.add("хлопець");
        genders.add("дівчина");
        genders.add("");

        health = new ArrayList<>();
        health.add("звичайна");
        health.add("спецгрупа");
        health.add("");

        mode = new ArrayList<>();
        mode.add("Менше - краще");
        mode.add("Більше - краще");
        mode.add("");

        sn = new ArrayList<>();
        for(Object o:ConnectHibernate.loadTable("from SportNormName")){
            SportNormName snn = (SportNormName)o;
            sn.add(snn.getSportNormName());
        }
        sn.add("");

        courses = new ArrayList<>();
        courses.add("1");
        courses.add("2");
        courses.add("3");
        courses.add("4");
        courses.add("");
        switch (tableId){
            case 1:
                CellUtilities.initInsertedCombos(table1, 1,genders, 165);
                CellUtilities.initInsertedCombos(table1, 2,health, 165);
                break;
            case 2:
                CellUtilities.initInsertedCombos(table1, 1, mode, 140);
                break;
            case 3:

                CellUtilities.initInsertedCombos(table1, 0,sn, 165);
                CellUtilities.initInsertedCombos(table1, 1,courses,40);
                CellUtilities.initInsertedCombos(table1, 2,genders,120);
                CellUtilities.initInsertedCombos(table1, 3,health,120);
                break;
            case 4:
                CellUtilities.initInsertedCombos(table1, 0,sn,165);
                CellUtilities.initInsertedCombos(table1, 1,courses,40);
                break;
        }
    }

    public static boolean isReadyToListen() {
        return readyToListen;
    }

    public static void setReadyToListen(boolean readyToListen) {
        WritePanel.readyToListen = readyToListen;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        comboBox1 = new JComboBox<>();
        comboBox2 = new JComboBox();
        comboBox3 = new JComboBox();

        //======== this ========

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setRowHeight(20);
            scrollPane1.setViewportView(table1);
        }

        //---- comboBox1 ----
        comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
            "\u0413\u0440\u0443\u043f\u0438",
            "\u0421\u0442\u0443\u0434\u0435\u043d\u0442\u0438",
            "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u041d\u0430\u0437\u0432\u0438)",
            "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u0412\u0438\u043c\u043e\u0433\u0438)",
            "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438"
        }));
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox1ItemStateChanged(e);
            }
        });

        //---- comboBox2 ----
        comboBox2.setVisible(false);
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox2ItemStateChanged(e);
            }
        });

        //---- comboBox3 ----
        comboBox3.setVisible(false);
        comboBox3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox3ItemStateChanged(e);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(106, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(comboBox1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBox2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBox3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private static JTable table1;
    private static JComboBox<String> comboBox1;
    private static JComboBox comboBox2;
    private static JComboBox comboBox3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
