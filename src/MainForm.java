
import pojo.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.*;

/**
 * @author Nazar Rudenko
 * https://github.com/mrproper1337
 */
public class MainForm extends JFrame {

    ConnectHibernate ch;
    ModelLoader tml;
    boolean listenerIsStopped;

    public MainForm() {
        ch = new ConnectHibernate();
        tml = new ModelLoader();
        listenerIsStopped = true;
        initComponents();
        tml.getGroupModel(false);
        tml.getGroupModel(true);
        tml.setGroupCombo();
        tml.setSNCombo();
    }
    private void applyReadFormFilter(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex() == 0)tml.getGroupModel(false);
            else tml.getStudentModel(false);
            table1.updateUI();
        }
    }
    private void applyWriteFormFilter(ItemEvent e) {
        comboBox5.setEnabled(false);
        comboBox6.setEnabled(false);

        switch(comboBox4.getSelectedIndex()){
            case 0:
                tml.getGroupModel(true);
                break;
            case 1:
                comboBox5.setEnabled(true);
                tml.getStudentModel(true);
                break;
            case 2:
                tml.getSNNameModel();
                break;
            case 3:
                tml.getSportNormModel();
                break;
            case 4:
                comboBox5.setEnabled(true);
                comboBox6.setEnabled(true);
                tml.getResultModel(tml.studentsId.get(0));
                break;
        }
        table2.updateUI();
    }
    private void applyResultFilter(ItemEvent e) {
        if(!listenerIsStopped && comboBox4.getSelectedIndex()!=1){
            if(e.getSource()==comboBox5){
                listenerIsStopped = true;
                comboBox6.removeAllItems();
                tml.studentsId.clear();
                for(Student s:(List<Student>)ch.loadTable("from Student where " +
                                "groupId = "+tml.groupsId.get(comboBox5.getSelectedIndex())
                ))
                {
                    comboBox6.addItem(s.getName());
                    tml.studentsId.add(s.getStudentId());
                }
                listenerIsStopped = false;
                tml.getResultModel(tml.studentsId.get(0));
            }
            else tml.getResultModel(tml.studentsId.get(comboBox6.getSelectedIndex()));
        }
    }
    private void applyStudentFilter(ItemEvent e) {
        if(!listenerIsStopped && comboBox4.getSelectedIndex()==1)
            tml.getStudentModel(true);
    }
    private void addNewRow(ActionEvent e) {
        switch(comboBox4.getSelectedIndex()){
            case 0:
                Group group = new Group();
                group.setGroupName("new");
                ch.addToTable(group);
                tml.getGroupModel(true);
                break;
            case 1:
                Student student = new Student();
                student.setName("new");
                student.setGroupId((Group)tml.groupList.get(comboBox5.getSelectedIndex()));
                ch.addToTable(student);
                tml.getStudentModel(true);
                break;
            case 2:
                SportNormName snn = new SportNormName();
                snn.setSportNormName("new");
                ch.addToTable(snn);
                tml.getSNNameModel();
                break;
            case 3:
                SportNorm sn = new SportNorm();
                sn.setExcellentMark(0);
                sn.setSatisfactorilyMark(0);
                sn.setGoodMark(0);
                ch.addToTable(sn);
                tml.getSportNormModel();
                break;
            case 4:
                Result result = new Result();
                result.setResult(0);
                ch.addToTable(result);
                tml.getResultModel(tml.studentsId.get(0));
                break;
        }
        table2.updateUI();
        table2.repaint(1000);
        tml.setGroupCombo();
        tml.setSNCombo();

    }
    private class ModelLoader {

        List<Integer> groupsId,snId,studentsId;
        final List groupList, sportNormNameList;
        String lastQuery,currentQuery;

        ModelLoader(){
            lastQuery = "";
            groupsId = new ArrayList<>();
            snId = new ArrayList<>();
            studentsId = new ArrayList<>();
            groupList=ch.loadTable("from Group");
            sportNormNameList =ch.loadTable("from SportNormName");
        }

        private void getGroupModel(final boolean editable){
            currentQuery="from Group";
            DefaultTableModel groups_tm;
            lastQuery = currentQuery;
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
                        return ret.getGroupName().equals("new") ? "" : ret.getGroupName();
                    }

                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Group obj =(Group)groupList.get(row);
                    obj.setGroupName(aValue.toString());
                    ch.updateInTable(obj);
                    if(aValue.toString().equals("") &&
                            JOptionPane.showConfirmDialog(null,"видалити рядок?","Поле не може бути пустим",
                                    JOptionPane.YES_NO_OPTION)==0){
                        ch.deleteFromTable(obj);
                        getGroupModel(true);
                    }
                    setGroupCombo();
                }
            };
            if(editable)table2.setModel(groups_tm);
            else table1.setModel(groups_tm);
        }
        private void getStudentModel(final boolean editable){
            int check = checkBox1.isSelected() ? 1 : 0;
            if(!editable){
                currentQuery = "from Student where(" +
                        "groupId = "+groupsId.get(comboBox1.getSelectedIndex()-1)+
                        " and " +
                        "gender ="+comboBox2.getSelectedIndex()+
                        " and " +
                        "healthGroup = "+check+
                        ")";
            }
            else currentQuery = "from Student where groupId = "+groupsId.get(comboBox5.getSelectedIndex());
            if(!lastQuery.equals(currentQuery)){
                DefaultTableModel students_tm;
                lastQuery = currentQuery;
                final java.util.List studentList=ch.loadTable(currentQuery);

                students_tm = new DefaultTableModel(){

                    @Override
                    public int getRowCount() {
                        return studentList.size();
                    }

                    @Override
                    public int getColumnCount() {
                        return 4;
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

                        }
                        return null;
                    }

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return columnIndex!=0 && editable;
                    }

                    @Override
                    public Object getValueAt(int rowIndex, int columnIndex) {
                        Student res=(pojo.Student)studentList.get(rowIndex);
                        switch(columnIndex){
                            case 0:
                                return res.getStudentId();
                            case 1:
                                if(res.getName().equals("new"))
                                    return "";
                                return res.getName();
                            case 2:
                                if(res.getGender()==0 || res.getName().equals("new"))
                                    return "хлопець";
                                else
                                    return "дівчина";
                            case 3:
                                if(res.getHealthGroup()==0 || res.getName().equals("new"))
                                    return "Звичайна";
                                else
                                    return "Спецгрупа";
                        }
                        return "";
                    }

                    @Override
                    public void setValueAt(Object aValue, int row, int column) {
                        Student obj =(Student)studentList.get(row);
                        switch(column){
                            case 1:
                                obj.setName(aValue.toString());
                                break;
                            case 2:
                                if(aValue=="хлопець")
                                    obj.setGender(0);
                                else
                                    obj.setGender(1);
                                break;
                            case 3:
                                if(aValue=="Звичайна")
                                    obj.setHealthGroup(0);
                                else
                                    obj.setHealthGroup(1);
                                break;
                        }
                        ch.updateInTable(obj);
                        if(aValue.toString().equals("") &&
                                JOptionPane.showConfirmDialog(null,"видалити рядок?","Поле не може бути пустим",
                                        JOptionPane.YES_NO_OPTION)==0){
                            ch.deleteFromTable(obj);
                            getStudentModel(true);
                        }
                    }
                };
                if(editable){
                    table2.setModel(students_tm);

                    ArrayList<String> cbItem = new ArrayList();
                    cbItem.add("хлопець");
                    cbItem.add("дівчина");
                    initInsertedCombos(table2,2,cbItem);
                    cbItem.clear();

                    cbItem.add("Звичайна");
                    cbItem.add("Спецгрупа");
                    initInsertedCombos(table2,3,cbItem);
                    cbItem.clear();
                }
                else table1.setModel(students_tm);
            }
        }
        private void getSNNameModel(){
            currentQuery="from SportNormName";
            DefaultTableModel sportNormName_tm;
            lastQuery = currentQuery;
            sportNormName_tm = new DefaultTableModel(){

                @Override
                public int getRowCount() {
                    return sportNormNameList.size();
                }

                @Override
                public int getColumnCount() {
                    return 2;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    if(columnIndex==0)return "ID";
                    else return "Норматив";
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 1;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    SportNormName ret;
                    if(columnIndex==0){
                        ret = (SportNormName)sportNormNameList.get(rowIndex);
                        return ret.getSportNormNameId();
                    }

                    else{
                        ret = (SportNormName)sportNormNameList.get(rowIndex);
                        return ret.getSportNormName().equals("new") ? "" : ret.getSportNormName();
                    }

                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    SportNormName obj =(SportNormName)sportNormNameList.get(row);
                    obj.setSportNormName(aValue.toString());
                    ch.updateInTable(obj);
                    if(aValue.toString().equals("") &&
                            JOptionPane.showConfirmDialog(null,"видалити рядок?","Поле не може бути пустим",
                                    JOptionPane.YES_NO_OPTION)==0){
                        ch.deleteFromTable(obj);
                        getSNNameModel();
                    }
                    setSNCombo();
                }
            };
            table2.setModel(sportNormName_tm);
        }
        private void getSportNormModel(){
            currentQuery="from SportNorm";
            DefaultTableModel sportNorm_tm;
            lastQuery = currentQuery;
            final List sportNormList=ch.loadTable(currentQuery);
            sportNorm_tm = new DefaultTableModel(){

                @Override
                public int getRowCount() {
                    return sportNormList.size();
                }

                @Override
                public int getColumnCount() {
                    return 8;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    switch(columnIndex) {
                        case 0:
                            return "ID";
                        case 1:
                            return "Норматив";
                        case 2:
                            return "Курс";
                        case 3:
                            return "Стать";
                        case 4:
                            return "Група здоров'я";
                        case 5:
                            return "Відмінно";
                        case 6:
                            return "Добре";
                        case 7:
                            return "Задовільно";
                    }
                    return "";
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex != 0;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    SportNorm res=(SportNorm)sportNormList.get(rowIndex);
                    boolean newRow = false;
                    if(res.getGoodMark()==0 && res.getSatisfactorilyMark()==0 && res.getExcellentMark()==0 )
                        newRow = true;

                    switch(columnIndex){
                        case 0:
                            return res.getSportNormId();
                        case 1:
                            if(newRow)return sportNormNameList.get(0);
                            return res.getSportNormNameId().getSportNormName();
                        case 2:
                            if(newRow)return 1;
                            return res.getCourseNorm();
                        case 3:
                            if(res.getGenderNorm()==0 || newRow)
                                return "хлопці";
                            else
                                return "дівчата";
                        case 4:
                            if(res.getHealthGroupNorm()==0 || newRow)
                                return "Звичайна";
                            else
                                return "Спецгрупа";
                        case 5:
                            if(newRow) return "";
                            return res.getExcellentMark();
                        case 6:
                            if(newRow) return "";
                            return res.getGoodMark();
                        case 7:
                            if(newRow) return "";
                            return res.getSatisfactorilyMark();
                    }
                    return "";
                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    SportNorm obj =(SportNorm)sportNormList.get(row);
                    switch(column){
                        case 1:
                            for(SportNormName snn:(List<SportNormName>)sportNormNameList)
                                if(snn.getSportNormName().equals(aValue))
                                    obj.setSportNormNameId(snn);
                            break;
                        case 2:
                            obj.setCourseNorm(Integer.parseInt(aValue.toString()));
                            break;
                        case 3:
                            if(aValue=="хлопці")
                                obj.setGenderNorm(0);
                            else
                                obj.setGenderNorm(1);
                            break;
                        case 4:
                            if(aValue=="Звичайна")
                                obj.setHealthGroupNorm(0);
                            else
                                obj.setHealthGroupNorm(1);
                            break;
                        case 5:
                            obj.setExcellentMark(Double.parseDouble(aValue.toString()));
                            break;
                        case 6:
                            obj.setGoodMark(Double.parseDouble(aValue.toString()));
                            break;
                        case 7:
                            obj.setSatisfactorilyMark(Double.parseDouble(aValue.toString()));
                            break;
                    }
                    ch.updateInTable(obj);
                    if(aValue.toString().equals("") &&
                            JOptionPane.showConfirmDialog(null,"видалити рядок?","Поле не може бути пустим",
                                    JOptionPane.YES_NO_OPTION)==0){
                        ch.deleteFromTable(obj);
                        getSportNormModel();
                    }
                }
            };
            table2.setModel(sportNorm_tm);

            ArrayList cbItem = new ArrayList();

            for(SportNormName snn:(List<SportNormName>)sportNormNameList)
                cbItem.add(snn.getSportNormName());
            initInsertedCombos(table2,1,cbItem,160);
            cbItem.clear();

            cbItem.add(1);
            cbItem.add(2);
            cbItem.add(3);
            cbItem.add(4);
            initInsertedCombos(table2, 2, cbItem, 40);
            cbItem.clear();

            cbItem.add("хлопці");
            cbItem.add("дівчата");
            initInsertedCombos(table2,3,cbItem,80);
            cbItem.clear();

            cbItem.add("Звичайна");
            cbItem.add("Спецгрупа");
            initInsertedCombos(table2,4,cbItem,90);
            cbItem.clear();

        }
        private void getResultModel(final int studentId){
            currentQuery="from Result where studentId = "+studentId;
            DefaultTableModel result_tm;
            final List resultList = ch.loadTable(currentQuery);
            lastQuery = currentQuery;

            Student currentStudent =(Student)ch.loadTable("from Student where studentId ="+studentId).get(0);
            final List allowedNorms = new ArrayList();
            for(SportNorm sn:(List<SportNorm>)ch.loadTable("from SportNorm where(" +
                    " genderNorm ="+currentStudent.getGender()+
                    " and "+
                    " healthGroupNorm ="+currentStudent.getHealthGroup()+
                    ")")){
                allowedNorms.add(sn);
            }

            result_tm = new DefaultTableModel(){

                @Override
                public int getRowCount() {
                    return resultList.size();
                }

                @Override
                public int getColumnCount() {
                    return 4;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    switch(columnIndex) {
                        case 0:
                            return "ID";
                        case 1:
                            return "Норматив";
                        case 2:
                            return "Курс";
                        case 3:
                            return "Результат";
                    }
                    return "";
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex != 0;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    Result res=(Result)resultList.get(rowIndex);
                    switch(columnIndex){
                        case 0:
                            return res.getResultId();
                        case 1:
                            if(res.getResult()==0)return sportNormNameList.get(0);
                            return res.getSportNormId().getSportNormNameId().getSportNormName();
                        case 2:
                            if(res.getResult()==0) return 1;
                            return res.getSportNormId().getCourseNorm();
                        case 3:
                            if(res.getResult()==0) return "";
                            return res.getResult();
                    }
                    return "";
                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Result obj =(Result)resultList.get(row);
                    switch(column){
                        case 1:
                            for(SportNorm asn:(List<SportNorm>)allowedNorms)
                                if(asn.getSportNormNameId().getSportNormName().equals(aValue) &&
                                        asn.getCourseNorm()==Integer.parseInt(getValueAt(row,2).toString()))
                                    obj.setSportNormId(asn);
                            break;
                        case 2:
                            for(SportNorm asn:(List<SportNorm>)allowedNorms)
                                if(asn.getSportNormNameId().getSportNormName().equals(getValueAt(row,1).toString()) &&
                                        asn.getCourseNorm()==Integer.parseInt(aValue.toString()))
                                    obj.setSportNormId(asn);
                            break;
                        case 3:
                            obj.setResult(Double.parseDouble(aValue.toString()));
                            break;
                    }
                    ch.updateInTable(obj);
                    if(aValue.toString().equals("") &&
                            JOptionPane.showConfirmDialog(null,"видалити рядок?","Поле не може бути пустим",
                                    JOptionPane.YES_NO_OPTION)==0){
                        ch.deleteFromTable(obj);
                        getResultModel(studentId);
                    }
                }
            };
            table2.setModel(result_tm);

            ArrayList cbItem = new ArrayList();

            for(SportNorm allowed:(List<SportNorm>)allowedNorms)
                if(!cbItem.contains(allowed.getSportNormNameId().getSportNormName()))
                    cbItem.add(allowed.getSportNormNameId().getSportNormName());
            initInsertedCombos(table2,1,cbItem,160);
            cbItem.clear();

            for(SportNorm allowed:(List<SportNorm>)allowedNorms)
                if(!cbItem.contains(allowed.getCourseNorm()))
                    cbItem.add(allowed.getCourseNorm());
            initInsertedCombos(table2,2,cbItem,40);
            cbItem.clear();

        }

        private void initInsertedCombos(JTable table,int columnIndex,ArrayList<String> rows){//after TM init
            TableColumn column;
            column = table.getColumnModel().getColumn(columnIndex);
            column.setCellRenderer(new ComboBoxCellRenderer(rows));
            column.setCellEditor(new ComboBoxCellEditor(rows));


        }
        private void initInsertedCombos(JTable table,int columnIndex,ArrayList<String> rows,int width){
            TableColumn column;
            column = table.getColumnModel().getColumn(columnIndex);
            column.setCellRenderer(new ComboBoxCellRenderer(rows,width));
            column.setCellEditor(new ComboBoxCellEditor(rows,width));
        }
        private void setGroupCombo(){
            final java.util.List groupList=ch.loadTable("from Group");
            listenerIsStopped=true;
            groupsId.clear();
            comboBox1.removeAllItems();
            comboBox5.removeAllItems();
            comboBox1.addItem("По групам");
            for(Group a:(java.util.List<Group>)groupList){
                comboBox1.addItem(a.getGroupName());
                comboBox5.addItem(a.getGroupName());
                groupsId.add(a.getGroupId());
            }
            listenerIsStopped=false;
        }
        private void setSNCombo(){
            listenerIsStopped = true;
            snId.clear();
            comboBox3.removeAllItems();
            for(SportNormName a:(java.util.List<SportNormName>)sportNormNameList){
                comboBox3.addItem(a.getSportNormName());
                snId.add(a.getSportNormNameId());
            }
            listenerIsStopped = false;

            listenerIsStopped = true;
            comboBox6.removeAllItems();
            tml.studentsId.clear();
            for(Student s:(List<Student>)ch.loadTable("from Student where groupId = "+tml.groupsId.get(0))){
                comboBox6.addItem(s.getName());
                tml.studentsId.add(s.getStudentId());
            }
            listenerIsStopped = false;
        }

        class ComboBoxPanel extends JPanel {
            ArrayList comboItems = null;
            protected JComboBox comboBox;
            public ComboBoxPanel(ArrayList comboItems) {

                this.comboItems = comboItems;
                comboBox = new JComboBox(comboItems.toArray()){
                    @Override public Dimension getPreferredSize() {
                        Dimension d = super.getPreferredSize();
                        return new Dimension(140, d.height-5);
                    }
                };
                setOpaque(true);
                add(comboBox);
            }
            public ComboBoxPanel(ArrayList comboItems, final int width) {

                this.comboItems = comboItems;
                comboBox = new JComboBox(comboItems.toArray()){
                    @Override public Dimension getPreferredSize() {
                        Dimension d = super.getPreferredSize();
                        return new Dimension(width, d.height-5);
                    }
                };
                setOpaque(true);
                add(comboBox);
            }
        }
        class ComboBoxCellRenderer extends ComboBoxPanel implements TableCellRenderer {
            public ComboBoxCellRenderer(ArrayList comboItems) {
                super(comboItems);
            }
            public ComboBoxCellRenderer(ArrayList comboItems,int width) {
                super(comboItems,width);
            }
            @Override public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                setBackground(isSelected?table.getSelectionBackground():table.getBackground());
                if(value!=null) {
                    comboBox.setSelectedItem(value);
                }
                return this;
            }
        }
        class ComboBoxCellEditor   extends ComboBoxPanel implements TableCellEditor {
            public ComboBoxCellEditor(ArrayList comboItems) {
                super(comboItems);
                comboBox.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        fireEditingStopped();
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) {
                        fireEditingStopped();
                    }
                });
            }
            public ComboBoxCellEditor(ArrayList comboItems,int width) {
                super(comboItems,width);
                comboBox.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        fireEditingStopped();
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) {
                        fireEditingStopped();
                    }
                });
            }
            @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                this.setBackground(table.getSelectionBackground());
                comboBox.setSelectedItem(value);
                return this;
            }

            //Copid from DefaultCellEditor.EditorDelegate
            @Override public Object getCellEditorValue() {
                return comboBox.getSelectedItem();
            }
            @Override public boolean shouldSelectCell(EventObject anEvent) {
                if(anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent)anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }
            @Override public boolean stopCellEditing() {
                if(comboBox.isEditable()) {
                    comboBox.actionPerformed(new ActionEvent(this, 0, ""));
                }
                fireEditingStopped();
                return true;
            }

            //Copid from AbstractCellEditor
            //protected EventListenerList listenerList = new EventListenerList();
            transient protected ChangeEvent changeEvent = null;

            @Override public boolean isCellEditable(EventObject e) {
                return true;
            }
            @Override public void  cancelCellEditing() {
                fireEditingCanceled();
            }
            @Override public void addCellEditorListener(CellEditorListener l) {
                listenerList.add(CellEditorListener.class, l);
            }
            @Override public void removeCellEditorListener(CellEditorListener l) {
                listenerList.remove(CellEditorListener.class, l);
            }
            public CellEditorListener[] getCellEditorListeners() {
                return listenerList.getListeners(CellEditorListener.class);
            }
            protected void fireEditingStopped() {
                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for(int i = listeners.length-2; i>=0; i-=2) {
                    if(listeners[i]==CellEditorListener.class) {
                        // Lazily create the event:
                        if(changeEvent == null) changeEvent = new ChangeEvent(this);
                        ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
                    }
                }
            }
            protected void fireEditingCanceled() {
                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for(int i = listeners.length-2; i>=0; i-=2) {
                    if(listeners[i]==CellEditorListener.class) {
                        // Lazily create the event:
                        if(changeEvent == null) changeEvent = new ChangeEvent(this);
                        ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
                    }
                }
            }
        }
    }
    //       U CAN'T TOUCH DIS
    //           \/\/\/

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
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
        comboBox5 = new JComboBox();
        comboBox6 = new JComboBox();

        //======== this ========
        setTitle("\u0421\u0442\u0430\u0442\u0438\u0441\u0442\u0438\u043a\u0430 \u0444\u0456\u0437\u0438\u0447\u043d\u0438\u0445 \u043f\u043e\u043a\u0430\u0437\u043d\u0438\u043a\u0456\u0432");
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    table1.setRowHeight(20);
                    scrollPane1.setViewportView(table1);
                }

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(204, 204, 255));

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .addGap(0, 476, Short.MAX_VALUE)
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
                        applyReadFormFilter(e);
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
                        applyReadFormFilter(e);
                    }
                });

                //---- checkBox1 ----
                checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
                checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                checkBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyReadFormFilter(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(scrollPane1)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(comboBox1)
                                .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addContainerGap())
                );
            }
            tabbedPane1.addTab("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434", panel1);

            //======== panel2 ========
            {

                //======== scrollPane2 ========
                {

                    //---- table2 ----
                    table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    table2.setRowHeight(28);
                    table2.setComponentPopupMenu(null);
                    table2.setRowSorter(null);
                    table2.setMinimumSize(new Dimension(15, 28));
                    table2.setAlignmentX(0.0F);
                    table2.setAlignmentY(0.0F);
                    scrollPane2.setViewportView(table2);
                }

                //---- comboBox4 ----
                comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u0413\u0440\u0443\u043f\u0438",
                    "\u0421\u0442\u0443\u0434\u0435\u043d\u0442\u0438",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u041f\u0435\u0440\u0435\u043b\u0456\u043a)",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u0412\u0438\u043c\u043e\u0433\u0438)",
                    "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438"
                }));
                comboBox4.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyWriteFormFilter(e);
                    }
                });

                //---- button1 ----
                button1.setText("\u0414\u043e\u0434\u0430\u0442\u0438 \u0440\u044f\u0434\u043e\u043a");
                button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNewRow(e);
                    }
                });

                //---- comboBox5 ----
                comboBox5.setEnabled(false);
                comboBox5.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyResultFilter(e);
                        applyStudentFilter(e);
                    }
                });

                //---- comboBox6 ----
                comboBox6.setEnabled(false);
                comboBox6.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyResultFilter(e);
                    }
                });

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(comboBox4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
                                    .addGap(223, 223, 223)
                                    .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE))
                            .addGap(23, 23, 23))
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel2Layout.createParallelGroup()
                                .addComponent(comboBox5, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button1)
                                .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                );
                panel2Layout.linkSize(SwingConstants.VERTICAL, new Component[] {button1, comboBox4, comboBox5, comboBox6});
            }
            tabbedPane1.addTab("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 936, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    JTabbedPane tabbedPane1;
    JPanel panel1;
    JScrollPane scrollPane1;
    JTable table1;
    JPanel panel3;
    JComboBox comboBox1;
    JComboBox<String> comboBox2;
    JCheckBox checkBox1;
    JComboBox comboBox3;
    JPanel panel2;
    JScrollPane scrollPane2;
    public JTable table2;
    JComboBox<String> comboBox4;
    JButton button1;
    JComboBox comboBox5;
    JComboBox comboBox6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        MainForm form = new MainForm();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
        form.setResizable(false);
    }
}
