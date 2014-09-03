package org.jcamp.math;

/**
 * linear mapping with reversed axis direction.
 * 
 * @author Thomas Weber
 */
public class ReversedLinearAxisMap
  extends AxisMap {
  
  /**
   */
  public ReversedLinearAxisMap(IInterval1D data) {
    super(data);
  }
  /**
   */
  public ReversedLinearAxisMap(IInterval1D data, Range1D fullViewRange) {
    super(data);
    setFullViewRange(fullViewRange);
    calcGrid();
  }
  /**
   */
  @Override
  protected void calcGrid() {
    if (realZoomRange.getXWidth() > 0) {
      double gridStep = alignTickStep(realZoomRange.getXWidth() / 5);
      double gridStart = Math.ceil((realZoomRange.getXMax()) / gridStep) * gridStep;
      double gridEnd = Math.floor((realZoomRange.getXMin()) / gridStep) * gridStep;
      grid = new LinearGrid1D(gridStart, gridEnd, -gridStep);
      gridZoomRange.set(grid.coordinateAt(realZoomRange.getXMax()), grid.coordinateAt(realZoomRange.getXMin()));
    }
  }
  
  /**
   * @return Grid1D
   * @param grid Grid1D
   */
  @Override
  public Grid1D map(Grid1D grid) {
    if (grid instanceof LinearGrid1D)
      return new LinearGrid1D(
	  map(((LinearGrid1D) grid).getStart()),
	  map(((LinearGrid1D) grid).getEnd()),
	  grid.getLength());
    else
      return super.map(grid);
  }
}
