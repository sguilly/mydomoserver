package chart;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.DataUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;

public class MinStackedBarRenderer extends StackedBarRenderer {
   
   private static final long serialVersionUID = 1L;
   
    /** A flag that controls whether the bars display values or percentages. */
    private boolean renderAsPercentages;
    
    /** A list to manage stacks in each column */
    private List<BarStackHolder> barStackHolderList = new ArrayList<BarStackHolder>();
    
    /**
     * Creates a new renderer.
     */
    public MinStackedBarRenderer() {
        super(); 
    }

    @Override
    public void drawItem(Graphics2D g2,
                         CategoryItemRendererState state,
                         Rectangle2D dataArea,
                         CategoryPlot plot,
                         CategoryAxis domainAxis,
                         ValueAxis rangeAxis,
                         CategoryDataset dataset,
                         int row,
                         int column,
                         int pass) {

        if (!isSeriesVisible(row)) {
            return;
        }

        // nothing is drawn for null values...
        Number dataValue = dataset.getValue(row, column);
        if (dataValue == null || dataValue.doubleValue() == 0.0) {
            return;
        }

        double value = dataValue.doubleValue();
        double total = 0.0;  // only needed if calculating percentages
        if (this.renderAsPercentages) {
            total = DataUtilities.calculateColumnTotal(dataset, column,
                    state.getVisibleSeriesArray());
            value = value / total;
        }

        PlotOrientation orientation = plot.getOrientation();
        double barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(),
                dataArea, plot.getDomainAxisEdge())
                - state.getBarWidth() / 2.0;

        double positiveBase = getBase();
        double negativeBase = positiveBase;

        for (int i = 0; i < row; i++) {
            Number v = dataset.getValue(i, column);
            if (v != null && isSeriesVisible(i)) {
                double d = v.doubleValue();
                if (this.renderAsPercentages) {
                    d = d / total;
                }
                if (d > 0) {
                    positiveBase = positiveBase + d;
                }
                else {
                    negativeBase = negativeBase + d;
                }
            }
        }

        double translatedBase;
        double translatedValue;
        boolean positive = (value > 0.0);
        boolean inverted = rangeAxis.isInverted();
        RectangleEdge barBase;
        if (orientation == PlotOrientation.HORIZONTAL) {
            if (positive && inverted || !positive && !inverted) {
                barBase = RectangleEdge.RIGHT;
            }
            else {
                barBase = RectangleEdge.LEFT;
            }
        }
        else {
            if (positive && !inverted || !positive && inverted) {
                barBase = RectangleEdge.BOTTOM;
            }
            else {
                barBase = RectangleEdge.TOP;
            }
        }

        RectangleEdge location = plot.getRangeAxisEdge();
        if (positive) {
            translatedBase = rangeAxis.valueToJava2D(positiveBase, dataArea,
                    location);
            translatedValue = rangeAxis.valueToJava2D(positiveBase + value,
                    dataArea, location);

        }
        else {
            translatedBase = rangeAxis.valueToJava2D(negativeBase, dataArea,
                    location);
            translatedValue = rangeAxis.valueToJava2D(negativeBase + value,
                    dataArea, location);
        }
        double barL0 = Math.min(translatedBase, translatedValue);
        double barLength = Math.max(Math.abs(translatedValue - translatedBase),
                getMinimumBarLength());
        double oriBarLength = Math.abs(translatedValue - translatedBase);
        
        addBarStackHolder(row, column, oriBarLength, value, barLength);
        
        if(barLength == getMinimumBarLength() && positive && positiveBase == 0){
           barL0 = Math.min(translatedBase - getMinimumBarLength() + oriBarLength, translatedValue - getMinimumBarLength() + oriBarLength);
        }
        else if(hasPosMinInColumn(column) && positive && positiveBase != 0){
           double posMinLength = obtainPosMinLength(column,row);

          barL0 = Math.min(translatedBase - posMinLength, translatedValue - posMinLength);
        }
        
        Rectangle2D bar = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(barL0, barW0, barLength,
                    state.getBarWidth());
        }
        else {
            bar = new Rectangle2D.Double(barW0, barL0, state.getBarWidth(),
                    barLength);
        }
        
        if (pass == 0) {
            if (getShadowsVisible()) {
                boolean pegToBase = (positive && (positiveBase == getBase()))
                        || (!positive && (negativeBase == getBase()));
                getBarPainter().paintBarShadow(g2, this, row, column, bar,
                        barBase, pegToBase);
            }
        }
        else if (pass == 1) {
            getBarPainter().paintBar(g2, this, row, column, bar, barBase);

            // add an item entity, if this information is being collected
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                addItemEntity(entities, dataset, row, column, bar);
            }
            
        }
        else if (pass == 2) {
            CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
                    column);
            if (generator != null && isItemLabelVisible(row, column)) {
                drawItemLabel(g2, dataset, row, column, plot, generator, bar,
                        (value < 0.0));
            }
        }

    }
    
    private boolean hasPosMinInColumn(int column){
       for(BarStackHolder value : this.barStackHolderList){
          if(value.getColumn() == column){
             if(value.getValue() > 0 && value.getLength() == getMinimumBarLength()){
                return true;
             }
          }
       }
       
       return false;
    }
    
    private double obtainPosMinLength(int column, int row){
       double length = 0.0;
       
       for(BarStackHolder value : this.barStackHolderList){
          if(value.getColumn() == column){
             if(value.getValue() > 0 && value.getRow() <= row && value.getLength() == getMinimumBarLength()){
                length += (getMinimumBarLength() - value.getOriLength());
             }
          }
       }
       
       return length;
    }
    
    private void addBarStackHolder(int row, int column, double oriLength, double value, double length){
       for(BarStackHolder bar : this.barStackHolderList){
          if(bar.getColumn() == column && bar.getRow() == row){
             return;
          }
       }
       
       this.barStackHolderList.add(new BarStackHolder(row, column, oriLength, value, length));
    }
}