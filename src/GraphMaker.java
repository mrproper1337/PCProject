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

public class GraphMaker {

    XYDataset data;

    GraphMaker(XYDataset data){
        this.data = data;
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

}
