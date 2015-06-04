package tableModels;

import panels.WritePanel;
import pojo.SportNormName;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SNNModel extends DefaultTableModel {
    public static List<SportNormName> snnList = ConnectHibernate.loadTable("from SportNormName");
    public SNNModel(){
        snnList = ConnectHibernate.loadTable("from SportNormName");
    }

    @Override
    public int getRowCount() {
        return snnList.size()+1;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex == 0)return "Норматив";
        else
            return "\"Модальність\" нормативу";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(snnList.size() == row)return "";
        if(column == 0)
            return snnList.get(row).getSportNormName();
        return snnList.get(row).getMarkMode()==0? "Менше - краще" : "Більше - краще";
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        SportNormName obj;
        switch(CellUtilities.getActionId(row == snnList.size(), aValue.toString().equals(""))){
            case 0:
                obj = new SportNormName();
                if(column==1){
                    obj.setMarkMode(aValue.toString().equals("Менше - краще")?0:1);
                    obj.setSportNormName("Новий норматив");
                }
                obj.setSportNormName(aValue.toString());
                obj.setMarkMode(0);
                ConnectHibernate.addToTable(obj);
                break;
            case 1:
                obj = snnList.get(row);
                ConnectHibernate.deleteFromTable(obj);
                break;
            case 2:
                obj = snnList.get(row);
                if(column==1)
                    obj.setMarkMode(aValue.toString().equals("Менше - краще")?0:1);
                else
                    obj.setSportNormName(aValue.toString());
                ConnectHibernate.updateInTable(obj);
                break;
        }
        WritePanel.updateModel();
    }
}
