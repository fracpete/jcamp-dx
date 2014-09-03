package org.jcamp.math;

/**
 * generic axis mapping. uses an irregular grid.
 * 
 * @author Thomas Weber
 */
public class IrregularAxisMap
  extends AxisMap {

  private IrregularGrid1D preferedGrid;
  
  /**
   * @param data IInterval1D
   * @param grid IrregularGrid1D
   */
  public IrregularAxisMap(IInterval1D data, IrregularGrid1D grid) {
    super(data);
    preferedGrid = grid;
    Range1D range = grid.getRange1D();
    setFullViewRange(range);
    resetZoom();
  }
  
  /**
   * calcGrid method comment.
   */
  @Override
  protected void calcGrid() {
    double zoomMin = realZoomRange.getXMin();
    double zoomMax = realZoomRange.getXMax();
    if (realZoomRange.equals(fullViewRange)) { // use linear grid for zooming in and out
      resetZoom();
    } else {
      if (preferedGrid.isAscending()) {
	double left = Math.floor(preferedGrid.coordinateAt(zoomMin));
	double right = Math.ceil(preferedGrid.coordinateAt(zoomMax));
	int n = (int) (right - left) + 1;
	if (n < 20 && n > 2) {
	  double[] gridArray = new double[n];
	  for (int i = 0; i < n; i++)
	    gridArray[i] = preferedGrid.valueAt((int) left + i);
	  grid = new IrregularGrid1D(gridArray);
	  realZoomRange.set(grid.getRange1D());
	  gridZoomRange.set(grid.coordinateAt(zoomMin), grid.coordinateAt(zoomMax));
	} else {
	  double gridStep = alignTickStep(realZoomRange.getXWidth() / 5);
	  double gridStart = Math.floor((zoomMin) / gridStep) * gridStep;
	  double gridEnd = Math.ceil((zoomMax) / gridStep) * gridStep;
	  grid = new LinearGrid1D(gridStart, gridEnd, gridStep);
	  gridZoomRange.set(grid.coordinateAt(zoomMin), grid.coordinateAt(zoomMax));
	}
      } else {
	double left = Math.floor(preferedGrid.coordinateAt(zoomMax));
	double right = Math.ceil(preferedGrid.coordinateAt(zoomMin));
	int n = (int) (right - left) + 1;
	if (n < 20 && n > 2) {
	  double[] gridArray = new double[n];
	  for (int i = 0; i < n; i++)
	    gridArray[i] = preferedGrid.valueAt((int) left + i);
	  grid = new IrregularGrid1D(gridArray);
	  realZoomRange.set(grid.getRange1D());
	  gridZoomRange.set(grid.coordinateAt(zoomMin), grid.coordinateAt(zoomMax));
	} else {
	  double gridStep = alignTickStep(realZoomRange.getXWidth() / 5);
	  double gridStart = Math.floor((zoomMin) / gridStep) * gridStep;
	  double gridEnd = Math.ceil((zoomMax) / gridStep) * gridStep;
	  grid = new LinearGrid1D(gridEnd, gridStart, -gridStep);
	  gridZoomRange.set(grid.coordinateAt(zoomMin), grid.coordinateAt(zoomMax));
	}
      }
    }
  }
  
  /**
   */
  @Override
  public void resetZoom() {
    if (preferedGrid != null) {
      realZoomRange.set(preferedGrid.getRange1D());
      grid = preferedGrid;
      gridZoomRange.set(grid.coordinateAt(realZoomRange.getXMin()), grid.coordinateAt(realZoomRange.getXMax()));
    }
  }
}
