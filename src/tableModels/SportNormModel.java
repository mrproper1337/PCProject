package tableModels;

import panels.WritePanel;
import pojo.SportNorm;
import pojo.SportNormName;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SportNormModel extends DefaultTableModel {
    static List<SportNorm> sportNormList = ConnectHibernate.loadTable("from SportNorm");
    public SportNormModel(){
        sportNormList = ConnectHibernate.loadTable("from SportNorm");
    }

    @Override
    public int getRowCount() {
        return sportNormList.size()+1;
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Норматив";
            case 1:
                return "Курс";
            case 2:
                return "Стать";
            case 3:
                return "Група здоров'я";
            case 4:
                return "Відмінно";
            case 5:
                return "Добре";
            case 6:
                return "Задовільно";
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(row == sportNormList.size())return "";
        switch (column){
            case 0:
                return sportNormList.get(row).getSportNormNameId().getSportNormName();
            case 1:
                return String.valueOf(sportNormList.get(row).getCourseNorm());
            case 2:
                return sportNormList.get(row).getGenderNorm()==0?"хлопець":"дівчина";
            case 3:
                return sportNormList.get(row).getHealthGroupNorm()== 0?"звичайна":"спецгрупа";
            case 4:
                return String.valueOf(sportNormList.get(row).getExcellentMark());
            case 5:
                return String.valueOf(sportNormList.get(row).getGoodMark());
            case 6:
                return String.valueOf(sportNormList.get(row).getSatisfactorilyMark());
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch(CellUtilities.getActionId(row == sportNormList.size(), aValue.toString().equals(""))){
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
        SportNorm obj = new SportNorm();
        switch(column){
            case 0:
                for(Object s:ConnectHibernate.loadTable("from SportNormName")){
                    SportNormName sportNormName = (SportNormName)s;
                    if(sportNormName.getSportNormName().equals(aValue.toString()))
                        obj.setSportNormNameId(sportNormName);
                }
                obj.setExcellentMark(1);
                obj.setGoodMark(1);
                obj.setSatisfactorilyMark(1);
                obj.setCourseNorm(1);
                obj.setHealthGroupNorm(0);
                obj.setGenderNorm(0);
                break;

        }
        WritePanel.updateModel();
    }
    private void deleteRow(int row){
        SportNorm obj = sportNormList.get(row);
        ConnectHibernate.deleteFromTable(obj);
        WritePanel.updateModel();
    }
    private void updateRow(Object aValue, int row, int column){
        SportNorm obj = sportNormList.get(row);
        switch (column){
            case 0:
                for(Object s:ConnectHibernate.loadTable("from SportNormName")){
                    SportNormName sportNormName = (SportNormName)s;
                    if(sportNormName.getSportNormName().equals(aValue.toString()))
                        obj.setSportNormNameId(sportNormName);
                }
                break;
            case 1:
                obj.setCourseNorm(Integer.parseInt(aValue.toString()));
                break;
            case 2:
                obj.setGenderNorm(Integer.parseInt(aValue.toString()));
                break;
            case 3:
                obj.setHealthGroupNorm(Integer.parseInt(aValue.toString()));
                break;
            case 4:
                obj.setExcellentMark(Integer.parseInt(aValue.toString()));
                break;
            case 5:
                obj.setGoodMark(Integer.parseInt(aValue.toString()));
                break;
            case 6:
                obj.setSatisfactorilyMark(Integer.parseInt(aValue.toString()));
                break;
        }
        ConnectHibernate.updateInTable(obj);
        WritePanel.updateModel();
    }
}
