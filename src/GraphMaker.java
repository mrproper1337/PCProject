import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pojo.*;

import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by mrproper on 16.01.2015.
 */
public class GraphMaker {

    JPanel panel;
    ModelLoader ml;
    ConnectHibernate ch;

    GraphMaker(JPanel panel,ModelLoader ml){
        this.panel = panel;
        this.ml = ml;
        this.ch = ml.ch;
    }


    private JFreeChart createChart(XYSeriesCollection paramXYDataset, String func) {
        JFreeChart localJFreeChart = ChartFactory.createXYLineChart(func, "Курс", "Результат", paramXYDataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();

        //x and y lines
        localXYPlot.setDomainZeroBaselineVisible(false);
        localXYPlot.setRangeZeroBaselineVisible(false);

        localXYPlot.getDomainAxis().setLowerMargin(0.0D);
        localXYPlot.getDomainAxis().setUpperMargin(0.0D);

        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);

        XYLineAndShapeRenderer localXYLineAndShapeRenderer = (XYLineAndShapeRenderer) localXYPlot.getRenderer();
        localXYLineAndShapeRenderer.setLegendLine(new Rectangle2D.Double(-4.0D, -3.0D, 8.0D, 6.0D));
        return localJFreeChart;
    }
    private  int getMark(SportNorm sn,double result){
        int mark = 2;
        if(sn.getSportNormNameId().getMarkMode() == 0){
            if(result <= sn.getExcellentMark())
                mark = 5;
            else if(result <= sn.getGoodMark())
                mark = 4;
            else if(result <= sn.getSatisfactorilyMark())
                mark = 3;
        }
        else{
            if(result >= sn.getExcellentMark())
                mark = 5;
            else if(result >= sn.getGoodMark())
                mark = 4;
            else if(result >= sn.getSatisfactorilyMark())
                mark = 3;
        }
        return mark;
    }
    private void makeStatGraph(Group currGroup){
        double[] averageResults = new double[4];
        List currStGr = ch.loadTable("from Student where(" +
                "groupId = "+currGroup.getGroupId()+
                " and " +
                "gender ="+ml.gender+
                " and " +
                "healthGroup = "+ ml.health+
                ")");

        List currSP = ch.loadTable("from SportNorm where " +
                "(sportNormNameId =" +ml.snId.get(comboBox3.getSelectedIndex())+
                " and " +
                "genderNorm =" +comboBox2.getSelectedIndex()+
                " and " +
                "healthGroupNorm =" +check+
                ")");
        int iii;
        String studQr = "",spQr = "";
        iii = 0;
        for(Student st:(List<Student>)currStGr){
            if(iii==0)
                studQr = (" studentId =" + st.getStudentId());
            else
                studQr += (" or studentId =" + st.getStudentId());
            iii++;
        }
        iii = 0;
        for(SportNorm sp:(List<SportNorm>)currSP){
            if(iii==0)
                spQr = (" sportNormId =" + sp.getSportNormId());
            else
                spQr += (" or sportNormId =" + sp.getSportNormId());
            iii++;
        }


        int[] rc = new int[4];
        if(studQr!="" && spQr!="")
            for(Result res:(List<Result>)ch.loadTable("from Result where " +
                            " ("+studQr+")" +
                            " and " +
                            " ("+spQr+")"
            )){
                switch(res.getSportNormId().getCourseNorm()){
                    case 1:
                        averageResults[0] += res.getResult();
                        rc[0]++;
                        break;
                    case 2:
                        averageResults[1] += res.getResult();
                        rc[1]++;
                        break;
                    case 3:
                        averageResults[2] += res.getResult();
                        rc[2]++;
                        break;
                    case 4:
                        averageResults[3] += res.getResult();
                        rc[3]++;
                        break;
                }
            }

        for(int i = 0;i<4;i++)
            if(rc[i]!=0)
                averageResults[i]/=rc[i];

        XYSeries groupSeries = new XYSeries("Середні показники "+currGroup.getGroupName());
        SportNormName spncurr = (SportNormName)ch.loadTable("from SportNormName").get(comboBox3.getSelectedIndex());
        String results = "Середні результати по курсам : ";
        String marks = "Середні оцінки по курсам : ";
        for(int i = 1;i<=4;i++){
            groupSeries.add(i,spncurr.getMarkMode()==0?averageResults[i-1]*-1:averageResults[i-1]);
            SportNorm resNorm = null;
            for(SportNorm sp:(List<SportNorm>)currSP)
                if(sp.getCourseNorm() == i)
                    resNorm = sp;
            results += "    "+i+"-й : "+(averageResults[i-1]==0?"н/а":averageResults[i-1]);
            marks +=   "       "+(averageResults[i-1]==0?"н/а":getMark(resNorm,averageResults[i-1]));
        }


        XYDataset data = new XYSeriesCollection(groupSeries);
        JFreeChart chart = createChart((XYSeriesCollection)data,"Середні показники "+currGroup.getGroupName());
        panel3.removeAll();
        label1.setText(results);
        label2.setText(marks);
        panel3.add(new ChartPanel(chart));
        panel3.revalidate();
    }
    private void makeStatGraph(Student currStudent){
        double[] normResults = new double[4];
        int check = checkBox1.isSelected() ? 1 : 0;
        List currSP = ch.loadTable("from SportNorm where " +
                "(sportNormNameId =" +tml.snId.get(comboBox3.getSelectedIndex())+
                " and " +
                "genderNorm =" +comboBox2.getSelectedIndex()+
                " and " +
                "healthGroupNorm =" +check+
                ")");
        System.out.println(currSP.size()+"   hui");
        int iii;
        String spQr = "";
        iii = 0;
        for(SportNorm sp:(List<SportNorm>)currSP){
            if(iii==0)
                spQr = (" sportNormId =" + sp.getSportNormId());
            else
                spQr += (" or sportNormId =" + sp.getSportNormId());
            iii++;
        }
        if(!spQr.equals(""))
            for(Result res:(List<Result>)ch.loadTable("from Result where studentId ="+currStudent.getStudentId()+" and( "+spQr+" )")){
                switch(res.getSportNormId().getCourseNorm()){
                    case 1:
                        normResults[0] = res.getResult();
                        break;
                    case 2:
                        normResults[1] = res.getResult();
                        break;
                    case 3:
                        normResults[2] = res.getResult();
                        break;
                    case 4:
                        normResults[3] = res.getResult();
                        break;
                }
            }
        XYSeries studSeries = new XYSeries("Показники : "+currStudent.getName());
        SportNormName  spncurr = (SportNormName)ch.loadTable("from SportNormName").get(comboBox3.getSelectedIndex());
        String results = "Результати по курсам : ";
        String marks = "Оцінки по курсам : ";
        for(int i = 1;i<=4;i++){
            studSeries.add(i, spncurr.getMarkMode() == 0 ? normResults[i - 1] * -1 : normResults[i - 1]);
            SportNorm resNorm = null;
            for(SportNorm sp:(List<SportNorm>)currSP)
                if(sp.getCourseNorm() == i)
                    resNorm = sp;
            results += "    "+i+"-й : "+(normResults[i-1]==0?"н/а":normResults[i-1]);
            marks +=   "       "+(normResults[i-1]==0?"н/а":getMark(resNorm,normResults[i-1]));
        }


        XYDataset data = new XYSeriesCollection(studSeries);
        JFreeChart chart = createChart((XYSeriesCollection)data,"Показники : "+currStudent.getName());
        panel3.removeAll();
        label1.setText(results);
        label2.setText(marks);
        panel3.add(new ChartPanel(chart));
        panel3.revalidate();
    }
}
