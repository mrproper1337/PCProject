import java.awt.event.*;
import pojo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/*
 * Created by JFormDesigner on Sun Nov 09 21:32:23 EET 2014
 */



/**
 * @author Ash Coopeer
 */
public class MainForm extends JFrame {

    //private DefaultTableModel students_tm,groups_tm;
    ConnectHibernate ch;
    List<Integer> groupsId,snId;
    boolean listenerIsStopped;
    public MainForm() {
        ch = new ConnectHibernate();
        groupsId=new ArrayList<>();
        snId=new ArrayList<>();
        initComponents();
        listenerIsStopped=true;
        table1.setModel(setGroupModels(false));
        table2.setModel(setGroupModels(true));
        setSportNormsModels();

    }

    private DefaultTableModel setGroupModels(final boolean editable){
        System.out.println("setGroupModels");
        DefaultTableModel groups_tm;
        final List groupList=ch.loadTable("from Group");
        groups_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return groupList.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                if(columnIndex==0)return "ID";
                else return "Група";
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                try{
                    if(columnIndex==0)
                        return Class.forName("java.lang.Integer");
                    else
                        return Class.forName("java.lang.String");

                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editable && columnIndex == 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Group ret;
                if(columnIndex==0){
                    ret = (pojo.Group)groupList.get(rowIndex);
                    return ret.getGroupId();
                }

                else{
                    ret = (pojo.Group)groupList.get(rowIndex);
                    return ret.getGroupName();
                }

            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                Group obj =(Group)groupList.get(row);
                obj.setGroupName(aValue.toString());
                ch.updateInTable(obj);
                updateUI();
            }
        };
        listenerIsStopped=true;
        groupsId.clear();
        comboBox1.removeAllItems();
        comboBox1.addItem("По групам");
        for(Group a:(List<pojo.Group>)groupList){
            comboBox1.addItem(a.getGroupName());
            groupsId.add(a.getGroupId());
        }
        listenerIsStopped=false;
        return groups_tm;
    }
    private DefaultTableModel setStudentModels(final boolean editable){
        System.out.println("setStudModels");

        DefaultTableModel students_tm;
        int check;
        if(checkBox1.isSelected())
            check=1;
        else
            check=0;
        String query = "from Student where(" +
                "groupId = "+groupsId.get(comboBox1.getSelectedIndex()-1)+
                " and " +
                "gender ="+comboBox2.getSelectedIndex()+
                " and " +
                "healthGroup = "+check+
                ")";
        System.out.println(query);
        final List studentList=ch.loadTable(query);
        students_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return studentList.size();
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch(columnIndex){
                    case 0:
                        return "ID";
                    case 1:
                        return "П.І.Б.";
                    case 2:
                        return "Стать";
                    case 3:
                        return "Група здоров'я";
                    case 4:
                        return "Група";

                }
                return null;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                try{
                    switch(columnIndex){
                        case 0:
                            return Class.forName("java.lang.Integer");
                        case 1:
                            return Class.forName("java.lang.String");
                        case 2:
                            return Class.forName("java.lang.String");
                        case 3:
                            return Class.forName("java.lang.String");
                        case 4:
                            return Class.forName("java.lang.String");

                    }
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return (columnIndex==1
                        || columnIndex==2
                        || columnIndex==3
                        || columnIndex==4) && editable;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Student res=(pojo.Student)studentList.get(rowIndex);
                switch(columnIndex){
                    case 0:
                        return res.getStudentId();
                    case 1:
                        return res.getName();
                    case 2:
                        if(!editable){
                            if(res.getGender()==0)
                                return "хлопець";
                            else
                                return "дівчина";
                        }
                        else{
                            JComboBox cb = new JComboBox();
                            cb.addItem("хлопець");
                            cb.addItem("дівчина");
                            cb.setSelectedIndex(res.getGender());
                        }
                    case 3:
                        if(res.getHealthGroup()==1)
                            return "Спецгрупа";
                        else
                            return "Звичайна";
                    case 4:
                        return res.getGroupId().getGroupName();
                }
                return null;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                Student obj =(Student)studentList.get(row);
                switch(column){
                    case 1:
                        obj.setName(aValue.toString());
                        break;
                    case 2:
                }
                ch.updateInTable(obj);
                updateUI();
            }
        };

        return  students_tm;
    }
    private void setSportNormsModels(){
        snId.clear();
        comboBox3.removeAllItems();
        System.out.println("setSNModels");
        List snList=ch.loadTable("from SportNormName");
        for(SportNormName a:(List<SportNormName>)snList){
            comboBox3.addItem(a.getSportNormName());
            snId.add(a.getSportNormNameId());
        }
    }
    private void applyFilter(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex()==0)
                table1.setModel(setGroupModels(false));
            else
                table1.setModel(setStudentModels(false));
            table1.updateUI();
        }
    }
    private void updateUI(){
        table1.setModel(setGroupModels(false));
        table2.setModel(setGroupModels(true));
        setSportNormsModels();

        table1.updateUI();
        table2.updateUI();
        comboBox1.updateUI();
        comboBox3.updateUI();
    }

    private void changeTable(ItemEvent e) {
        // TODO add your code here
    }




    //       U CAN'T TOUCH DIS
    //           \/\/\/

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ash Hisenberg
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox<>();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        comboBox4 = new JComboBox<>();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(table1);
                }

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(204, 204, 255));

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .addGap(0, 486, Short.MAX_VALUE)
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                    );
                }

                //---- comboBox1 ----
                comboBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyFilter(e);
                    }
                });

                //---- comboBox2 ----
                comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u0425\u043b\u043e\u043f\u0446\u0456",
                    "\u0414\u0456\u0432\u0447\u0430\u0442\u0430"
                }));
                comboBox2.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyFilter(e);
                    }
                });

                //---- checkBox1 ----
                checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
                checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                checkBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyFilter(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 313, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(scrollPane1)
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(comboBox1)
                                .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addComponent(comboBox3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))))
                            .addContainerGap())
                );
            }
            tabbedPane1.addTab("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434", panel1);

            //======== panel2 ========
            {

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(table2);
                }

                //---- comboBox4 ----
                comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u0413\u0440\u0443\u043f\u0438",
                    "\u0421\u0442\u0443\u0434\u0435\u043d\u0442\u0438",
                    "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438"
                }));
                comboBox4.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        changeTable(e);
                    }
                });

                //---- button1 ----
                button1.setText("\u0414\u043e\u0434\u0430\u0442\u0438 \u0440\u044f\u0434\u043e\u043a");

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 555, Short.MAX_VALUE))
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE))
                            .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(comboBox4)
                                .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            tabbedPane1.addTab("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ash Hisenberg
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel3;
    private JComboBox comboBox1;
    private JComboBox<String> comboBox2;
    private JCheckBox checkBox1;
    private JComboBox comboBox3;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JComboBox<String> comboBox4;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
