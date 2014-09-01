package org.jcamp.math;

/**
 * general topological 2D grid with linear interpolation between
 * grid points. 
 * @author Thomas Weber
 */
public class TopologicalGrid2D extends Grid2D {
    private double[][] samples;
    private Range2D.Double range = new Range2D.Double();
    /**
     */
    public TopologicalGrid2D() {
        super();
        this.samples = new double[][] {
        };
    }
    /**
     * IrregularGrid2D constructor comment.
     * @param lengthX int
     * @param lengthY int
     */
    public TopologicalGrid2D(double[][] samples, int lengthX, int lengthY) {
        this(samples, lengthX, lengthY, true);
    }
    /**
     * IrregularGrid2D constructor comment.
     * @param lengthX int
     * @param lengthY int
     */
    public TopologicalGrid2D(double[][] samples, int lengthX, int lengthY, boolean copy) {
        super(lengthX, lengthY);
        setSamples(samples, lengthX, lengthY, copy);
    }
    /**
     * calculates triangle area
     */
    private static double area(double ax, double ay, double bx, double by, double cx, double cy) {
        return 0.5 * ((ay + cy) * (cx - ax) + (cy + by) * (bx - cx) - (ay + by) * (bx - ax));
    }
    /**
     * cloning
     * @return Object
     */
    public Object clone() {
        TopologicalGrid2D grid = null;
        //	try {
        grid = (TopologicalGrid2D) super.clone();
        //	} catch (CloneNotSupportedException e) {}
        grid.range = (Range2D.Double) this.range.clone();
        grid.setSamples(this.samples, getXLength(), getYLength(), true);
        return grid;
    }
    /**
     */
    public double[] coordinateAt(double valuex, double valuey) {
        double[] grid = new double[2];
        int lengthX = getXLength();
        int lengthY = getYLength();
        double a =
            area(
                samples[0][(lengthY - 1) * lengthX],
                samples[1][(lengthY - 1) * lengthX],
                samples[0][0],
                samples[1][0],
                samples[0][lengthX - 1],
                samples[1][lengthX - 1]);
        boolean positive = (a > 0);
        grid[0] = Double.NaN;
        grid[1] = Double.NaN;
        int ix0, ix1, iy0, iy1;
        int dx = (lengthX - 1) / 2;
        int dy = (lengthX - 1) / 2;
        ix0 = dx;
        iy0 = dy;
        double pa, pb, pc, pd;
        double xa, xb, xc, xd;
        double ya, yb, yc, yd;
        double xp = valuex;
        double yp = valuey;
        double gdx, gdy;
        for (int iter = 0; iter < lengthX * lengthY; ++iter) {
            if (ix0 < 0) {
                ix0 = 0;
            }
            if (ix0 > lengthX - 2) {
                ix0 = lengthX - 2;
            }
            if (iy0 < 0) {
                iy0 = 0;
            }
            if (iy0 > lengthY - 2) {
                iy0 = lengthY - 2;
            }
            ix1 = ix0 + 1;
            iy1 = iy0 + 1;
            //System.out.println("ix0:"+ix0);
            //System.out.println("iy0:"+iy0);
            // test polynom
            xa = samples[0][iy0 * lengthX + ix0];
            ya = samples[1][iy0 * lengthX + ix0];
            xb = samples[0][iy0 * lengthX + ix1];
            yb = samples[1][iy0 * lengthX + ix1];
            xc = samples[0][iy1 * lengthX + ix1];
            yc = samples[1][iy1 * lengthX + ix1];
            xd = samples[0][iy1 * lengthX + ix0];
            yd = samples[1][iy1 * lengthX + ix0];
            // check if P within ABD
            pa = area(xp, yp, xb, yb, xd, yd);
            pb = area(xa, ya, xp, yp, xd, yd);
            pd = area(xa, ya, xb, yb, xp, yp);
            if ((positive && pa < 0) || (!positive && pa > 0)) { // ne of BD
                // check BCD
                pb = area(xp, yp, xc, yc, xd, yd);
                pc = area(xb, yb, xp, yp, xd, yd);
                pd = area(xb, yb, xc, yc, xp, yp);
                if ((positive && pc < 0) || (!positive && pc > 0)) { // sw of BD ??? maybe NaN
                    break;
                } else {
                    if ((positive && pd < 0) || (!positive && pd > 0)) { // s of BC
                        ix0 += dx;
                        dx /= 2;
                    } else {
                        if ((positive && pb < 0) || (!positive && pb > 0)) { // n of CD
                            iy0 += dy;
                            dy /= 2;
                        } else { // Bingo! within BCD
                            // reverse interpolation
                            gdx =
                                ((xc - xp) * (yb - yc) + (yp - yc) * (xb - xc))
                                    / ((xd - xc) * (yb - yc) - (yd - yc) * (xb - xc));
                            gdy =
                                ((yd - yc) * (xc - xp) + (xd - xc) * (yp - yc))
                                    / ((xb - xc) * (yd - yc) - (xd - xc) * (yb - yc));
                            grid[0] = ix1 + gdx;
                            grid[1] = iy1 + gdy;
                            break;
                        }
                    }
                }
            } else {
                if ((positive && pb < 0) || (!positive && pb > 0)) { // w of AC
                    ix0 -= dx;
                    dx /= 2;
                } else {
                    if ((positive && pd < 0) || (!positive && pb > 0)) { // s of AB
                        iy0 -= dy;
                        dy /= 2;
                    } else {
                        // bingo! triangle ABD found
                        // reverse interpolation
                        gdx =
                            ((yd - ya) * (xp - xa) + (ya - yp) * (xd - xa))
                                / ((yd - ya) * (xb - xa) + (ya - yb) * (xd - xa));
                        gdy =
                            ((xp - xa) * (yb - ya) + (ya - yp) * (xb - xa))
                                / ((xd - xa) * (yb - ya) + (ya - yd) * (xb - xa));
                        grid[0] = ix0 + gdx;
                        grid[1] = iy0 + gdy;
                        break;
                    }
                }
            }
        }
        return grid;
    }
    /**
     */
    public double[][] coordinatesAt(double[] valuex, double[] valuey) {
        int n = Math.min(valuex.length, valuey.length);
        int lengthX = getXLength();
        int lengthY = getYLength();
        double[][] grid = new double[2][n];
        double a =
            area(
                samples[0][(lengthY - 1) * lengthX],
                samples[1][(lengthY - 1) * lengthX],
                samples[0][0],
                samples[1][0],
                samples[0][lengthX - 1],
                samples[1][lengthX - 1]);
        boolean positive = (a > 0);
        mainloop : for (int i = 0; i < n; i++) {
            grid[0][i] = Double.NaN;
            grid[1][i] = Double.NaN;
            int ix0, ix1, iy0, iy1;
            int dx = (lengthX - 1) / 2;
            int dy = (lengthX - 1) / 2;
            ix0 = dx;
            iy0 = dy;
            double pa, pb, pc, pd;
            double xa, xb, xc, xd;
            double ya, yb, yc, yd;
            double xp = valuex[i];
            double yp = valuey[i];
            double gdx, gdy;
            for (int iter = 0; iter < lengthX * lengthY; ++iter) {
                if (ix0 < 0) {
                    ix0 = 0;
                }
                if (ix0 > lengthX - 2) {
                    ix0 = lengthX - 2;
                }
                if (iy0 < 0) {
                    iy0 = 0;
                }
                if (iy0 > lengthY - 2) {
                    iy0 = lengthY - 2;
                }
                ix1 = ix0 + 1;
                iy1 = iy0 + 1;
                //System.out.println("ix0:"+ix0);
                //System.out.println("iy0:"+iy0);
                // test polynom
                xa = samples[0][iy0 * lengthX + ix0];
                ya = samples[1][iy0 * lengthX + ix0];
                xb = samples[0][iy0 * lengthX + ix1];
                yb = samples[1][iy0 * lengthX + ix1];
                xc = samples[0][iy1 * lengthX + ix1];
                yc = samples[1][iy1 * lengthX + ix1];
                xd = samples[0][iy1 * lengthX + ix0];
                yd = samples[1][iy1 * lengthX + ix0];
                // check if P within ABD
                pa = area(xp, yp, xb, yb, xd, yd);
                pb = area(xa, ya, xp, yp, xd, yd);
                pd = area(xa, ya, xb, yb, xp, yp);
                if ((positive && pa < 0) || (!positive && pa > 0)) { // ne of BD
                    // check BCD
                    pb = area(xp, yp, xc, yc, xd, yd);
                    pc = area(xb, yb, xp, yp, xd, yd);
                    pd = area(xb, yb, xc, yc, xp, yp);
                    if ((positive && pc < 0) || (!positive && pc > 0)) { // sw of BD ??? maybe NaN
                        continue mainloop;
                    } else {
                        if ((positive && pd < 0) || (!positive && pd > 0)) { // s of BC
                            ix0 += dx;
                            dx /= 2;
                        } else {
                            if ((positive && pb < 0) || (!positive && pb > 0)) { // n of CD
                                iy0 += dy;
                                dy /= 2;
                            } else { // Bingo! within BCD
                                // reverse interpolation
                                gdx =
                                    ((xc - xp) * (yb - yc) + (yp - yc) * (xb - xc))
                                        / ((xd - xc) * (yb - yc) - (yd - yc) * (xb - xc));
                                gdy =
                                    ((yd - yc) * (xc - xp) + (xd - xc) * (yp - yc))
                                        / ((xb - xc) * (yd - yc) - (xd - xc) * (yb - yc));
                                grid[0][i] = ix1 + gdx;
                                grid[1][i] = iy1 + gdy;
                                continue mainloop;
                            }
                        }
                    }
                } else {
                    if ((positive && pb < 0) || (!positive && pb > 0)) { // w of AC
                        ix0 -= dx;
                        dx /= 2;
                    } else {
                        if ((positive && pd < 0) || (!positive && pb > 0)) { // s of AB
                            iy0 -= dy;
                            dy /= 2;
                        } else {
                            // bingo! triangle ABD found
                            // reverse interpolation
                            gdx =
                                ((yd - ya) * (xp - xa) + (ya - yp) * (xd - xa))
                                    / ((yd - ya) * (xb - xa) + (ya - yb) * (xd - xa));
                            gdy =
                                ((xp - xa) * (yb - ya) + (ya - yp) * (xb - xa))
                                    / ((xd - xa) * (yb - ya) + (ya - yd) * (xb - xa));
                            grid[0][i] = ix0 + gdx;
                            grid[1][i] = iy0 + gdy;
                            continue mainloop;
                        }
                    }
                }
            }
        }
        return grid;
    }
    /**
     */
    public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
        switch (index) {
            case 0 :
                return new Array1D(samples[0], false);
            case 1 :
                return new Array1D(samples[1], false);
            default :
                throw new ArrayIndexOutOfBoundsException();
        }
    }
    /**
     * gets grid range.
     * @return Range.Double
     */
    public Range.Double getRange() {
        return range;
    }
    /**
     * gets grid range.
     * @return Range2D.Double
     */
    public Range2D.Double getRange2D() {
        return range;
    }
    /**
     * @return double[][]
     */
    public double[][] getSamples() {
        return samples;
    }
    /**
     * gets array of first dimension
     * 
     * @return com.creon.math.IArray1D
     */
    public IArray1D getXArray() {
        return new Array1D(samples[0]);
    }
    /**
     * gets array of second dimension
     * 
     * @return com.creon.math.IArray1D
     */
    public IArray1D getYArray() {
        return new Array1D(samples[1]);
    }
    /**
     */
    public double[] gridPointAt(int index) {
        double[] values;
        values = new double[2];
        if (index >= 0 && index <= getLength()) {
            values[0] = samples[0][index];
            values[1] = samples[1][index];
        } else {
            values[0] = Double.NaN;
            values[1] = Double.NaN;
        }
        return values;
    }
    /**
     */
    public double[][] gridPointsAt(int[] index) {
        double[][] values;
        int length = getLength();
        values = new double[2][length];
        int n = index.length; // n grid points
        for (int i = 0; i < n; i++) {
            int index_i = index[i];
            if (index_i >= 0 && index_i <= length) {
                values[0][i] = samples[0][index_i];
                values[1][i] = samples[1][index_i];
            } else {
                values[0][i] = Double.NaN;
                values[1][i] = Double.NaN;
            }
        }
        return values;
    }
    /**
     * @param newSamples double[][]
     */
    public void setSamples(double[][] newSamples, int lengthX, int lengthY) {
        setSamples(newSamples, lengthX, lengthY, true);
    }
    /**
     * sets sample points
     */
    public void setSamples(double[][] newSamples, int lengthX, int lengthY, boolean copy) {
        int length = lengthX * lengthY;
        setLength(length);
        setXLength(lengthX);
        setYLength(lengthY);

        if (newSamples.length != length)
            throw new IllegalArgumentException("bad sample array length");
        if (copy) {
            this.samples = new double[2][];
            this.samples[0] = (double[]) newSamples[0].clone();
            this.samples[1] = (double[]) newSamples[1].clone();
        } else
            this.samples = newSamples;
        this.range.set(this.samples[0][0], this.samples[0][0], this.samples[1][0], this.samples[1][0]);
        for (int i = 1; i < length; i++) {
            double xmax = range.getXMax();
            double xmin = range.getXMin();
            double ymax = range.getYMax();
            double ymin = range.getYMin();
            if (this.samples[0][i] > xmax)
                xmax = this.samples[0][i];
            if (this.samples[1][i] > ymax)
                ymax = this.samples[1][i];
            if (this.samples[0][i] < xmin)
                xmin = this.samples[0][i];
            if (this.samples[1][i] < ymin)
                ymin = this.samples[1][i];
            this.range.set(xmin, xmax, ymin, ymax);
        }
    }
    /**
     * gets value at grid coordinates <code>gridx, gridy</code>.
     * @param gridx double x grid coordinate
     * @param gridy double y grid coordinate
     * @return double[] 
     */
    public double[] valueAt(double gridx, double gridy) {
        double value[] = new double[2];
        double gx = gridx;
        double gy = gridy;
        int lengthX = getXLength();
        int lengthY = getYLength();
        if (gx < -0.5 || gx > lengthX - 0.5 || gy < -0.5 || gy > lengthY - 0.5) { // out of bounds
            value[0] = value[1] = Double.NaN;
            return value;
        }
        // find nearest integer lesser than g
        int gx0 = (int) Math.floor(gx + 0.5);
        if (gx0 < 0)
            gx0 = 0;
        if (gx0 > lengthX - 2)
            gx0 = lengthX - 2;
        int gy0 = (int) Math.floor(gy + 0.5);
        if (gy0 < 0)
            gy0 = 0;
        if (gy0 > lengthY - 2)
            gy0 = lengthY - 2;
        // bounding rectangle:
        int lb = gy0 * lengthX + gx0;
        int rb = gy0 * lengthX + gx0 + 1;
        int lt = (gy0 + 1) * lengthX + gx0;
        int rt = (gy0 + 1) * lengthX + gx0 + 1;
        double dx = gx - gx0;
        double dy = gy - gy0;
        if (dx + dy - 1 < 0) {
            value[0] = samples[0][lb] + dx * (samples[0][rb] - samples[0][lb]) + dy * (samples[0][lt] - samples[0][lb]);
            value[1] = samples[1][lb] + dx * (samples[1][rb] - samples[1][lb]) + dy * (samples[1][lt] - samples[1][lb]);
        } else {
            dx = gx0 + 1 - gx;
            dy = gy0 + 1 - gy;
            value[0] = samples[0][rt] + dx * (samples[0][lt] - samples[0][rt]) + dy * (samples[0][rb] - samples[0][rt]);
            value[1] = samples[1][rt] + dx * (samples[1][lt] - samples[1][rt]) + dy * (samples[1][rb] - samples[1][rt]);
        }
        return value;
    }
    /**
     * gets values at grid coordinates.
     * @param gridx double[] x grid coordinates
     * @param gridy double[] y grid coordinates
     * @return double[][] 2 dim. array of values
     */
    public double[][] valuesAt(double[] gridx, double[] gridy) {
        int n = Math.min(gridx.length, gridy.length);
        int lengthX = getXLength();
        int lengthY = getYLength();
        double value[][] = new double[2][n];
        for (int i = 0; i < n; i++) {
            double gx = gridx[i];
            double gy = gridy[i];
            if (gx < -0.5 || gx > lengthX - 0.5 || gy < -0.5 || gy > lengthY - 0.5) { // out of bounds
                value[0][i] = value[1][i] = Double.NaN;
                continue;
            }
            // find nearest integer lesser than g
            int gx0 = (int) Math.floor(gx + 0.5);
            if (gx0 < 0)
                gx0 = 0;
            if (gx0 > lengthX - 2)
                gx0 = lengthX - 2;
            int gy0 = (int) Math.floor(gy + 0.5);
            if (gy0 < 0)
                gy0 = 0;
            if (gy0 > lengthY - 2)
                gy0 = lengthY - 2;
            // bounding rectangle:
            int lb = gy0 * lengthX + gx0;
            int rb = gy0 * lengthX + gx0 + 1;
            int lt = (gy0 + 1) * lengthX + gx0;
            int rt = (gy0 + 1) * lengthX + gx0 + 1;
            double dx = gx - gx0;
            double dy = gy - gy0;
            if (dx + dy - 1 < 0) {
                value[0][i] =
                    samples[0][lb] + dx * (samples[0][rb] - samples[0][lb]) + dy * (samples[0][lt] - samples[0][lb]);
                value[1][i] =
                    samples[1][lb] + dx * (samples[1][rb] - samples[1][lb]) + dy * (samples[1][lt] - samples[1][lb]);
            } else {
                dx = gx0 + 1 - gx;
                dy = gy0 + 1 - gy;
                value[0][i] =
                    samples[0][rt] + dx * (samples[0][lt] - samples[0][rt]) + dy * (samples[0][rb] - samples[0][rt]);
                value[1][i] =
                    samples[1][rt] + dx * (samples[1][lt] - samples[1][rt]) + dy * (samples[1][rb] - samples[1][rt]);
            }
        }
        return value;
    }
}
