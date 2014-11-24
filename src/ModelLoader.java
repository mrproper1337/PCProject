import pojo.*;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ModelLoader {

    ConnectHibernate ch;
    MainForm form;
    List<Integer> groupsId,snId;
    String lastQuery,currentQuery;

    ModelLoader(MainForm form, ConnectHibernate ch){
        this.form = form;
        this.ch = ch;
        lastQuery = "";
        groupsId=new ArrayList<>();
        snId=new ArrayList<>();
    }

    public void getGroupModel(final boolean editable){
        currentQuery="from Group";
        if(!lastQuery.equals(currentQuery)){
            System.out.println("getGroupModel");
            DefaultTableModel groups_tm;
            lastQuery = currentQuery;
            final List groupList=ch.loadTable(currentQuery);
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
                        return ret.getGroupName();
                    }

                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Group obj =(Group)groupList.get(row);
                    obj.setGroupName(aValue.toString());
                    ch.updateInTable(obj);
                }
            };
            if(editable)form.table2.setModel(groups_tm);
            else form.table1.setModel(groups_tm);
        }
    }
    public void getStudentModel(final boolean editable){
        int check = form.checkBox1.isSelected() ? 1 : 0;
        if(!editable){
            currentQuery = "from Student where(" +
                    "groupId = "+groupsId.get(form.comboBox1.getSelectedIndex()-1)+
                    " and " +
                    "gender ="+form.comboBox2.getSelectedIndex()+
                    " and " +
                    "healthGroup = "+check+
                    ")";
        }
        else currentQuery = "from Student";
        if(!lastQuery.equals(currentQuery)){
            System.out.println("setStudModels");
            DefaultTableModel students_tm;
            lastQuery = currentQuery;
            final List studentList=ch.loadTable(currentQuery);
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
                            return res.getName();
                        case 2:
                            if(!editable){
                                if(res.getGender()==0)
                                    return "хлопець";
                                else
                                    return "дівчина";
                            }
                            else break;
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
                }
            };
            if(editable){
                form.table2.setModel(students_tm);
                TableColumn genderColumn = form.table2.getColumnModel().getColumn(2);
                genderColumn.setCellRenderer(new ComboBoxCellRenderer());
                genderColumn.setCellEditor(new ComboBoxCellEditor());

            }
            else form.table1.setModel(students_tm);
        }
    }
    public void setGroupCombo(){
        final List groupList=ch.loadTable("from Group");
        form.listenerIsStopped=true;
        groupsId.clear();
        form.comboBox1.removeAllItems();
        form.comboBox1.addItem("По групам");
        for(Group a:(List<pojo.Group>)groupList){
            form.comboBox1.addItem(a.getGroupName());
            groupsId.add(a.getGroupId());
        }
        form.listenerIsStopped=false;
    }
    public void setSNCombo(){
        snId.clear();
        form.comboBox3.removeAllItems();
        System.out.println("setSNModels");
        List snList=ch.loadTable("from SportNormName");
        for(SportNormName a:(List<SportNormName>)snList){
            form.comboBox3.addItem(a.getSportNormName());
            snId.add(a.getSportNormNameId());
        }
    }

    class ComboBoxPanel extends JPanel {
        private String[] m = new String[] {"хлопець","дівчина"};
        protected JComboBox<String> comboBox = new JComboBox<String>(m) {
            @Override public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(40, d.height);
            }
        };
        public ComboBoxPanel() {
            super();
            setOpaque(true);
            comboBox.setEditable(true);
            add(comboBox);
        }
    }
    class ComboBoxCellRenderer extends ComboBoxPanel implements TableCellRenderer {
        public ComboBoxCellRenderer() {
            super();
            setName("Table.cellRenderer");
        }
        @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setBackground(isSelected?table.getSelectionBackground()
                    :table.getBackground());
            if(value!=null) {
                comboBox.setSelectedItem(value);
            }
            return this;
        }
    }
    class ComboBoxCellEditor   extends ComboBoxPanel implements TableCellEditor {
        public ComboBoxCellEditor() {
            super();
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
        @Override public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
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
