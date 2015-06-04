package tableModels;

import panels.ReadPanel;
import panels.WritePanel;
import pojo.Group;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GroupModel extends DefaultTableModel{
    public static List<Group> groupList = ConnectHibernate.loadTable("from Group");
    final boolean editable;
    public GroupModel(boolean editable){
        this.editable = editable;
        groupList = ConnectHibernate.loadTable("from Group");
    }

    @Override
    public int getRowCount() {
        return editable?groupList.size()+1:groupList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return "Назва групи";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(!editable)
            ReadPanel.g.setStats(groupList.get(row));
        return editable;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(groupList.size() == row)return "";
        return groupList.get(row).getGroupName();
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Group obj;
        switch(CellUtilities.getActionId(row == groupList.size(), aValue.toString().equals(""))){
            case 0:
                obj = new Group();
                obj.setGroupName(aValue.toString());
                ConnectHibernate.addToTable(obj);
                break;
            case 1:
                obj = groupList.get(row);
                ConnectHibernate.deleteFromTable(obj);
                break;
            case 2:
                obj = groupList.get(row);
                obj.setGroupName(aValue.toString());
                ConnectHibernate.updateInTable(obj);
                break;
        }
        WritePanel.updateModel();
    }
}
