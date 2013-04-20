package chart;

import java.awt.Color;
import java.awt.Paint;

public class CustomRenderer extends org.jfree.chart.renderer.category.BarRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 784630226449158436L;
	private Paint[] colors;

	private String[] colorValues = { "#AFD8F8", "#F6BD0F", "#8BBA00", "#FF8E46", "#008E8E", "#D64646" };

	public CustomRenderer() {
		colors = new Paint[colorValues.length];
		for (int i = 0; i < colorValues.length; i++) {
			colors[i] = Color.decode(colorValues[i]);
		}
	}


	public Paint getItemPaint(int i, int j) {
		return colors[j % colors.length];
	}
}
