package chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class CreateJFreeChartBarColor {

	
	public static void main(String[] args) {
		
		CategoryDataset dataset = createDataset();
		
		JFreeChart freeChart = createChart(dataset);
		
		saveAsFile(freeChart, "C:\\TEMP\\bar.png", 500, 400);
	}

	
	public static void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {
		FileOutputStream out = null;
		try {
			File outFile = new File(outputPath);
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			out = new FileOutputStream(outputPath);
			
			ChartUtilities.writeChartAsPNG(out, chart, weight, height);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}

	
	public static JFreeChart createChart(CategoryDataset categoryDataset) {
		JFreeChart jfreechart = ChartFactory.createBarChart("a", 
				"b", // categoryAxisLabel
				"c", // valueAxisLabel
				categoryDataset, // dataset
				PlotOrientation.HORIZONTAL, false, // legend
				false, // tooltips
				false); // URLs

		Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

		jfreechart.setTextAntiAlias(false);
		jfreechart.setBackgroundPaint(Color.white);

		CategoryPlot plot = jfreechart.getCategoryPlot();

		
		plot.setRangeGridlinesVisible(true);
		
		plot.setRangeGridlinePaint(Color.gray);
		
		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		// vn.setAutoRangeIncludesZero(true);
		DecimalFormat df = new DecimalFormat("#0.0");
		vn.setNumberFormatOverride(df); 
		
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(labelFont);
		domainAxis.setTickLabelFont(labelFont);
		
		domainAxis.setMaximumCategoryLabelWidthRatio(6.00f);
		

		
		domainAxis.setLowerMargin(0.1);
		
		domainAxis.setUpperMargin(0.1);
		//  columnKey 
		// domainAxis.setSkipCategoryLabelsToFit(true);
		plot.setDomainAxis(domainAxis);
		
		plot.setBackgroundPaint(new Color(255, 255, 204));

		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(labelFont);
		rangeAxis.setTickLabelFont(labelFont);
		
		rangeAxis.setUpperMargin(0.15);
		
		rangeAxis.setLowerMargin(0.15);
		plot.setRangeAxis(rangeAxis);

		
		TextTitle textTitle = jfreechart.getTitle();
		textTitle.setFont(new Font("Arial", Font.PLAIN, 20));
		domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
		domainAxis.setLabelFont(new Font("Arial", Font.PLAIN, 12));
		vn.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
		vn.setLabelFont(new Font("Arial", Font.PLAIN, 12));
		// jfreechart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));
		
		CustomRenderer renderer = new CustomRenderer();
		
		renderer.setMaximumBarWidth(0.2);
		
		renderer.setMinimumBarLength(0.2);
		
		renderer.setBaseOutlinePaint(Color.BLACK);
		
		renderer.setDrawBarOutline(true);
		
		renderer.setItemMargin(0.5);
		jfreechart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		renderer.setIncludeBaseInRange(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		plot.setRenderer(renderer);
		
		plot.setForegroundAlpha(1.0f);

		
		plot.setBackgroundAlpha(0.5f);

		return jfreechart;
	}

	// CategoryDataset
	public static CategoryDataset createDataset() {
		double[][] data = new double[][] { { 25, 24, 40, 12, 33, 33 } };
		String[] rowKeys = { "" };
		String[] columnKeys = { "A", "B", "C", "D", "E", "F" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
		return dataset;
	}

}