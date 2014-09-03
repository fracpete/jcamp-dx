package org.jcamp.math;

/**
 * generic 1-dimensional grid.
 * linear interpolation between grid samples.
 * 
 * @author Thomas Weber
 */
public class IrregularGrid1D
  extends Grid1D {
  
  /** for serialization. */
  private static final long serialVersionUID = -3347468188186084124L;

  private double[] samples;
  
  private boolean ascending;
  
  private Range1D.Double range = new Range1D.Double();
  
  /**
   * default ctor.
   */
  public IrregularGrid1D() {
    this.samples = new double[] {
    };
  }
  
  /**
   * IrregularGrid1D constructor comment.
   * @param length int
   */
  public IrregularGrid1D(double[] samples) {
    this(samples, true);
  }
  
  /**
   * IrregularGrid1D internal copy constructor.
   */
  IrregularGrid1D(double[] samples, Range1D range, boolean ascending) {
    super(samples.length);
    this.samples = (double[]) samples.clone();
    this.range.set(range);
    this.ascending = ascending;
  }
  
  /**
   * IrregularGrid1D constructor comment.
   */
  public IrregularGrid1D(double[] samples, boolean copy) {
    super(samples.length);
    setSamples(samples, copy);
  }
  
  /**
   * IrregularGrid1D constructor comment.
   * @param length int
   */
  public IrregularGrid1D(IOrderedArray1D samples) {
    this(samples.toArray(), false);
  }
  
  /**
   * resampling to wider or narrower grid range (extrapolation)
   * @param length int
   */
  public IrregularGrid1D(IrregularGrid1D from, Range1D range) {
    super();
    double gleft = from.coordinateAt(range.getXMinAsDouble());
    double gright = from.coordinateAt(range.getXMaxAsDouble());
    int length = 0;
    if (from.ascending) {
      double l = Math.floor(gleft);
      double r = Math.ceil(gright);
      length = (int) (r - l) + 1;
      this.samples = new double[length];
      for (int i = 0; i < length; i++) {
	this.samples[i] = from.valueAt((int) l + i);
      }
    } else {
      double l = Math.floor(gright);
      double r = Math.ceil(gleft);
      length = (int) (r - l) + 1;
      this.samples = new double[length];
      for (int i = 0; i < length; i++) {
	this.samples[i] = from.valueAt((int) l + i);
      }
    }
    setLength(length);
    if (length < 2)
      throw new RuntimeException("samples do not form a valid grid");
    this.range.set(samples[0], samples[0]);
    this.ascending = (samples[length - 1] > samples[0]);
    // check for strict monotony
    if (this.ascending) {
      for (int i = 1; i < length; i++) {
	if (this.samples[i] < this.range.getXMin())
	  this.range.set(this.samples[i], this.range.getXMax());
	if (this.samples[i] > this.range.getXMax())
	  this.range.set(this.range.getXMin(), samples[i]);
	if (samples[i] <= samples[i - 1]) {
	  throw new RuntimeException("samples do not form a valid grid");
	}
      }
    } else {
      for (int i = 1; i < length; i++) {
	if (this.samples[i] < this.range.getXMin())
	  this.range.set(this.samples[i], this.range.getXMax());
	if (this.samples[i] > this.range.getXMax())
	  this.range.set(this.range.getXMin(), this.samples[i]);
	if (this.samples[i] >= this.samples[i - 1]) {
	  throw new RuntimeException("samples do not form a valid grid");
	}
      }
    }
  }
  
  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    return new IrregularGrid1D(samples, range, ascending);
  }
  
  /**
   * 
   */
  @Override
  public double coordinateAt(double x) {
    double grid;
    if (Double.isNaN(x))
      grid = Double.NaN;
    else {
      int left = 0;
      int right = getLength() - 1;
      int mid;
      double sleft = samples[left];
      double sright = samples[right];
      if (ascending) {
	if (x < sleft) { // extrapolate
	  grid = left + (x - sleft) / (samples[left + 1] - sleft);
	  return grid;
	} else if (x > sright) { // extrapolate
	  grid = right + (x - sright) / (sright - samples[right - 1]);
	  return grid;
	}
	while (right - left > 1) {
	  mid = (right + left) / 2;
	  double sx = samples[mid];
	  if (x > sx)
	    left = mid;
	  else if (x < sx)
	    right = mid;
	  else {
	    left = mid;
	    break;
	  }
	}
	grid = left + (x - samples[left]) / (samples[left + 1] - samples[left]);
      } else {
	if (x < sright) { // extrapolate
	  grid = right + (sright - x) / (samples[right - 1] - sright);
	  return grid;
	} else if (x > sleft) { // extrapolate
	  grid = left + (sleft - x) / (sleft - samples[left + 1]);
	  return grid;
	}
	while (right - left > 1) {
	  mid = (right + left) / 2;
	  double sx = samples[mid];
	  if (x < sx)
	    left = mid;
	  else if (x > sx)
	    right = mid;
	  else {
	    left = mid;
	    break;
	  }
	}
	grid = left + (samples[left] - x) / (samples[left] - samples[left + 1]);
      }
    }
    return grid;
  }
  
  /**
   * 
   */
  @Override
  public double[] coordinatesAt(double[] value) {
    int n = value.length;
    double[] grid = new double[n];
    for (int i = 0; i < n; i++) {
      double x = value[i];
      if (Double.isNaN(x))
	grid[i] = Double.NaN;
      else {
	int left = 0;
	int right = getLength() - 1;
	int mid;
	double sleft = samples[left];
	double sright = samples[right];
	if (ascending) {
	  if (x < sleft) { // extrapolate
	    grid[i] = left + (x - sleft) / (samples[left + 1] - sleft);
	  continue;
	  } else if (x > sright) { // extrapolate
	    grid[i] = right + (x - sright) / (sright - samples[right - 1]);
	    continue;
	  }
	  while (right - left > 1) {
	    mid = (right + left) / 2;
	    double sx = samples[mid];
	    if (x > sx)
	      left = mid;
	    else if (x < sx)
	      right = mid;
	    else {
	      left = mid;
	      break;
	    }
	  }
	  grid[i] = left + (x - samples[left]) / (samples[left + 1] - samples[left]);
	} else {
	  if (x < sright) { // extrapolate
	    grid[i] = right + (sright - x) / (samples[right - 1] - sright);
	    continue;
	  } else if (x > sleft) { // extrapolate
	    grid[i] = left + (sleft - x) / (sleft - samples[left + 1]);
	    continue;
	  }
	  while (right - left > 1) {
	    mid = (right + left) / 2;
	    double sx = samples[mid];
	    if (x < sx)
	      left = mid;
	    else if (x > sx)
	      right = mid;
	    else {
	      left = mid;
	      break;
	    }
	  }
	  grid[i] = left + (samples[left] - x) / (samples[left] - samples[left + 1]);
	}
      }
    }
    return grid;
  }
  
  /**
   * getArray method comment.
   */
  @Override
  public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    if (index == 0)
      return this;
    else
      throw new ArrayIndexOutOfBoundsException();
  }
  
  /**
   * gets range of grid.
   */
  @Override
  public Range.Double getRange() {
    return (Range.Double) range.clone();
  }
  
  /**
   * gets range of grid.
   */
  @Override
  public Range1D.Double getRange1D() {
    return (Range1D.Double) range.clone();
  }
  
  /**
   * Insert the method's description here.
   * Creation date: (01/06/00 16:51:44)
   * @return double
   */
  public double[] getSamples() {
    return samples;
  }
  
  /**
   * flag if grid has ascending values
   * @return boolean
   */
  @Override
  public boolean isAscending() {
    return ascending;
  }
  
  /**
   * getValue method comment.
   */
  @Override
  public double pointAt(int index) {
    int length = getLength();
    if (index >= 0 && index < length) {
      return samples[index];
    } else if (index < 0) { // extrapolation
      return samples[0] + index * (samples[1] - samples[0]);
    } else { // (index>=length)
      return samples[length - 1] + (index - length + 1) * (samples[length - 1] - samples[length - 2]);
    }
  }
  
  public void scale(double amount) {
    for (int i = 0; i < samples.length; i++)
      samples[i] *= amount;
    range = new Range1D.Double(range.getXMin() * amount, range.getXMax() * amount);
    if (amount < 0)
      ascending = !ascending;
  }
  
  /**
   * set sample array.
   * @param newSamples double[]
   */
  public void setSamples(double[] newSamples) {
    setSamples(newSamples, true);
  }
  
  /**
   * sets the grid array.
   * @param newSamples double[]
   * @param copy boolean
   */
  public void setSamples(double[] newSamples, boolean copy) {
    this.samples = newSamples;
    if (copy) {
      this.samples = (double[]) newSamples.clone();
    } else
      this.samples = newSamples;
    this.range.set(samples[0], samples[0]);
    int length = getLength();
    ascending = (samples[length - 1] > samples[0]);
    // check for strict monotony
    if (ascending) {
      for (int i = 1; i < length; i++) {
	if (samples[i] < range.getXMin())
	  range.set(samples[i], range.getXMax());
	if (samples[i] > range.getXMax())
	  range.set(range.getXMin(), samples[i]);
	if (samples[i] <= samples[i - 1]) {
	  throw new RuntimeException("samples do not form a valid grid");
	}
      }
    } else {
      for (int i = 1; i < length; i++) {
	if (samples[i] < range.getXMin())
	  range.set(samples[i], range.getXMax());
	if (samples[i] > range.getXMax())
	  range.set(range.getXMin(), samples[i]);
	if (samples[i] >= samples[i - 1]) {
	  throw new RuntimeException("samples do not form a valid grid");
	}
      }
    }
  }
  
  /**
   * convert grid to double array.
   * 
   * @return double[]
   */
  public double[] toArray() {
    double[] a = new double[samples.length];
    System.arraycopy(samples, 0, a, 0, samples.length);
    return a;
  }
  
  public void translate(double amount) {
    for (int i = 0; i < samples.length; i++)
      samples[i] += amount;
    range.translate(amount);
  }
  
  /**
   * getValueFromGrid method comment.
   */
  @Override
  public double valueAt(double gx) {
    double value;
    int length = getLength();
    if (gx < 0) {
      double dx = samples[1] - samples[0];
      value = samples[0] + dx * gx;
    } else if (gx > length - 1) {
      double dx = samples[length - 1] - samples[length - 2];
      value = samples[length - 1] + dx * (gx - length + 1);
    } else {
      // find nearest integer lesser than g
      int gx0 = (int) Math.floor(gx + 0.5);
      if (gx0 < 0)
	gx0 = 0;
      if (gx0 > length - 2)
	gx0 = length - 2;
      double dx = gx - gx0;
      value = (1 - dx) * samples[gx0] + dx * samples[gx0 + 1];
    }
    return value;
  }
  
  /**
   * getValueFromGrid method comment.
   */
  @Override
  public double[] valuesAt(double[] grid) {
    int n = grid.length;
    double[] value = new double[n];
    int length = getLength();
    for (int i = 0; i < n; i++) {
      double gx = grid[i];
      if (gx < 0) {
	double dx = samples[1] - samples[0];
	value[i] = samples[0] + dx * gx;
      } else if (gx > length - 1) {
	double dx = samples[length - 1] - samples[length - 2];
	value[i] = samples[length - 1] + dx * (gx - length + 1);
      } else {
	// find nearest integer lesser than g
	int gx0 = (int) Math.floor(gx + 0.5);
	if (gx0 < 0)
	  gx0 = 0;
	if (gx0 > length - 2)
	  gx0 = length - 2;
	double dx = gx - gx0;
	value[i] = (1 - dx) * samples[gx0] + dx * samples[gx0 + 1];
      }
    }
    return value;
  }
}
