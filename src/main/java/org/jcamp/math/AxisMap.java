package org.jcamp.math;

/**
 * mapping of data to coordinate axes.
 * 
 * @author Thomas Weber
 */
public abstract class AxisMap extends DataMap {
  
  protected IInterval1D data = null;
  
  protected Grid1D grid;
  
  /** data range */
  protected Range1D.Double dataRange = new Range1D.Double(0, 1);
  
  /** real world visible range */
  protected Range1D.Double realZoomRange = new Range1D.Double(0, 1);
  
  /** full view visible range */
  protected Range1D.Double fullViewRange = new Range1D.Double(0, 1);
  
  /** visible range in grid */
  protected Range1D.Double gridZoomRange = new Range1D.Double(0, 1);
  
  protected final static double ZOOM_EPS = 1E-10;
  
  /**
   * AxisMap constructor comment.
   */
  AxisMap() {
    super();
  }
  
  /**
   * AxisMap constructor comment.
   */
  public AxisMap(IInterval1D data) {
    super();
    this.data = data;
    if (data != null) {
      if (data.getRange1D().getXWidth() > 0) {
	setDataRange(data.getRange1D());
	setFullViewRange(data.getRange1D());
      } else {
	setDataRange(data.getRange1D());
	double x = data.getRange1D().getXMax();
	setFullViewRange(new Range1D.Double(x - .5 * x, x + .5 * x));
      }
    }
  }
  
  /**
   * align tick steps for zoom range to a pleasing value.
   * @return double
   * @param s double
   */
  public static double alignTickStep(double s) {
    double fac = 1.0;
    if (s < ZOOM_EPS || Double.isNaN(s) || Double.isInfinite(s))
      return ZOOM_EPS; // prevents endless loop if s == 0.0

    while (s > 100.0) {
      s *= 0.1;
      fac *= 10.0;
    }
    while (s < 10.0) {
      s *= 10.0;
      fac *= 0.1;
    }
    if (s > 50.0) {
      if (Math.abs(s - 100.0) > Math.abs(s - 50.0))
	s = 50.0;
      else
	s = 100.0;
    } else if (s > 25.0) {
      if (Math.abs(s - 50.0) > Math.abs(s - 25.0))
	s = 25.0;
      else
	s = 50.0;
    } else if (s > 20.0) {
      if (Math.abs(s - 25.0) > Math.abs(s - 20.0))
	s = 25.0;
      else
	s = 20.0;
    } else if (s > 10.0) {
      if (Math.abs(s - 20.0) > Math.abs(s - 10.0))
	s = 20.0;
      else
	s = 10.0;
    }

    return s * fac;
  }
  
  /**
   * calculates mapping grid.
   */
  abstract protected void calcGrid();
  
  /**
   * gets data mapped.
   * @return Interval1D
   */
  public IInterval1D getData() {
    return data;
  }
  
  /**
   * gets data range.
   * @return Range1D
   */
  public Range1D getDataRange() {
    return dataRange;
  }
  
  /**
   * gets full zoom range.
   * @return Range1D.Double
   */
  public Range1D.Double getFullViewRange() {
    return fullViewRange;
  }
  
  /**
   * return axis grid.
   * @return Grid1D
   */
  public Grid1D getGrid() {
    return grid;
  }
  
  /**
   * get mapping range.
   * @return Range1D.Double
   */
  @Override
  public final Range1D.Double getMapRange() {
    return new Range1D.Double(0., 1.);
  }
  
  /**
   * gets current zoom range.
   * @return Range1D
   */
  public Range1D getZoomRange() {
    return realZoomRange;
  }
  
  /**
   * gets direction of grid values.
   * @return boolean
   */
  public boolean isAscending() {
    return getGrid().isAscending();
  }
  
  /**
   * map data points.
   */
  @Override
  public double[] map(double[] world) {
    double[] gridvalues = grid.coordinatesAt(world);
    double scale = 1.0 / gridZoomRange.getXWidth();
    for (int i = 0; i < gridvalues.length; i++) {
      gridvalues[i] -= gridZoomRange.getXMin();
      gridvalues[i] *= scale;
    }
    return gridvalues;
  }
  
  /**
   * map method comment.
   */
  @Override
  public double map(double world) {
    double value = (grid.coordinateAt(world) - gridZoomRange.getXMin()) / gridZoomRange.getXWidth();
    return value;
  }
  
  /**
   * maps complete grids.
   * @return Grid1D
   * @param grid Grid1D
   */
  public Grid1D map(Grid1D grid) {
    double[] g = grid.asArray();
    double[] h = map(g);
    return new IrregularGrid1D(h);
  }
  
  /**
   * maps range to axis.
   */
  public Range1D.Double map(Range1D.Double world) {
    return new Range1D.Double(map(world.getXMin()), map(world.getXMax()));
  }
  
  /**
   * sets zoom to full zoom range and recalculates mapping grid.
   */
  public void resetZoom() {
    realZoomRange.set(fullViewRange);
    calcGrid();
  }
  
  /**
   * maps array on axis back.
   */
  @Override
  public double[] reverseMap(double[] v) {
    double[] gridv = new double[v.length];
    double scale = gridZoomRange.getXWidth();
    for (int i = 0; i < v.length; i++) {
      gridv[i] = scale * v[i] + gridZoomRange.getXMin();
    }
    double[] values = grid.valuesAt(gridv);
    return values;
  }
  
  /**
   * maps value back.
   */
  @Override
  public double reverseMap(double v) {
    double gridv = v * gridZoomRange.getXWidth() + gridZoomRange.getXMin();
    double value = grid.valueAt(gridv);
    return value;
  }
  
  /**
   * reverse maps complete grids.
   * @return Grid1D
   * @param grid Grid1D
   */
  public Grid1D reverseMap(Grid1D grid) {
    double[] g = grid.asArray();
    double[] h = reverseMap(g);
    return new IrregularGrid1D(h);
  }
  
  /**
   * maps range back.
   */
  public Range1D.Double reverseMap(Range1D.Double v) {
    return new Range1D.Double(reverseMap(v.getXMin()), reverseMap(v.getXMax()));
  }

  /**
   * set data range to a new value and adjust nice view range.
   * @param newDataRange Range1D
   */
  private void setDataRange(Range1D newDataRange) {
    if (newDataRange != null) {
      dataRange.set(newDataRange);

      if (dataRange.getXWidth() > 0) {
	fullViewRange.set(dataRange);
	realZoomRange.set(dataRange);
      } else {
	fullViewRange.set(dataRange.getXMin() - .1, dataRange.getXMax() + .1);
	realZoomRange.set(dataRange.getXMin() - .1, dataRange.getXMax() + .1);
      }
      calcGrid();
    }
  }
  
  /**
   * sets full zoom range.
   * @param newFullViewRange Range1D
   */
  public void setFullViewRange(Range1D newFullViewRange) {
    fullViewRange.set(newFullViewRange);
    setZoomRange(newFullViewRange);
  }
  
  /**
   * sets the zoom range between x0,x1.
   * @param x0 double
   * @param x1 double
   */
  public void setZoomRange(double x0, double x1) {
    realZoomRange.set(x0, x1);
    calcGrid();
  }
  
  /**
   * sets zooming range.
   * @param zoom Range1D
   */
  public void setZoomRange(Range1D zoom) {
    realZoomRange.set(zoom);
    calcGrid();
  }
}
