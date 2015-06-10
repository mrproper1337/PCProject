package tableModels;

import panels.ReadPanel;
import panels.WritePanel;
import pojo.Student;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudentModel extends DefaultTableModel {
    public static List<Student> studentList = ConnectHibernate.loadTable("from Student where groupId ="+GroupModel.groupList.get(0).getGroupId());
    final boolean editable;
    public StudentModel(int groupId){
        this.editable = true;
        studentList = ConnectHibernate.loadTable("from Student where groupId = "+groupId);
    }
    public StudentModel(int groupId,int healthId,int gender){
        this.editable = false;
        studentList = ConnectHibernate.loadTable("from Student where(" +
                "groupId = "+groupId+
                " and " +
                "gender ="+gender+
                " and " +
                "healthGroup = "+healthId+
                ")");
    }

    @Override
    public int getRowCount() {
        return editable?studentList.size()+1:studentList.size();
    }

    @Override
    public int getColumnCount() {
        return editable?3:1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if(editable)
            switch(columnIndex){
                case 0:
                    return "П.І.Б.";
                case 1:
                    return "Стать";
                case 2:
                    return "Група здоров'я";

            }
        return "П.І.Б.";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(!editable)
            ReadPanel.g.setStats(studentList.get(row));
        return editable;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(row == studentList.size())return "";
        if(!editable || column == 0)return studentList.get(row).getName();
        switch (column){
            case 1:return studentList.get(row).getGender()== 0?"хлопець":"дівчина";
            case 2:return studentList.get(row).getHealthGroup()== 0?"звичайна":"спецгрупа";
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch(CellUtilities.getActionId(row == studentList.size(), aValue.toString().equals(""))){
            case 0:
                addRow(aValue,column);
                break;
            case 1:
                deleteRow(row);
                break;
            case 2:
                updateRow(aValue,row,column);
                break;
        }
    }

    private void addRow(Object aValue,int column){
        Student obj = new Student();
        switch(column){
            case 0:
                obj.setName(aValue.toString());
                obj.setGroupId(studentList.get(0).getGroupId());
                obj.setGender(0);
                obj.setHealthGroup(0);
                break;
            case 1:
                obj.setName("новий студент");
                obj.setGroupId(studentList.get(0).getGroupId());
                obj.setGender(aValue.toString().equals("хлопець")?0:1);
                obj.setHealthGroup(0);
                break;
            case 2:
                obj.setName("новий студент");
                obj.setGroupId(studentList.get(0).getGroupId());
                obj.setGender(0);
                obj.setHealthGroup(aValue.toString().equals("звичайна")?0:1);
                break;
        }
        ConnectHibernate.addToTable(obj);
        WritePanel.updateModel();
    }
    private void deleteRow(int row){
        Student obj = studentList.get(row);
        ConnectHibernate.deleteFromTable(obj);
        WritePanel.updateModel();
    }
    private void updateRow(Object aValue, int row, int column){
        Student obj = studentList.get(row);
        switch (column){
            case 0:
                obj.setName(aValue.toString());
                break;
            case 1:
                obj.setGender(aValue.toString().equals("хлопець") ? 0 : 1);
                break;
            case 2:
                obj.setHealthGroup(aValue.toString().equals("звичайна")?0:1);
        }
        ConnectHibernate.updateInTable(obj);
        WritePanel.updateModel();
    }
}
