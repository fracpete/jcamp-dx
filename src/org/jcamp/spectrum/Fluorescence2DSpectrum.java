package org.jcamp.spectrum;
import org.jcamp.math.Range1D;
import org.jcamp.math.Range3D;
/**
 * 2D fluorescence spectrum.
 * @author Thomas Weber
 */
public class Fluorescence2DSpectrum extends Spectrum2D {

    /**
     * FluorescenceSpectrum.
     *
     */
    public Fluorescence2DSpectrum(IOrderedDataArray1D x, IOrderedDataArray1D y, IDataArray1D z) {
        super(x, y, z);
        setFullSpectrum(true);
        adjustFullViewRange();
    }

    /**
     * adjust data ranges for pretty full spectrum view
     */
    private void adjustFullViewRange() {
        if (isFullSpectrum()) {
            Range1D.Double xrange = getXData().getRange1D();
            /*		double w = xrange.getWidth();
            		xrange.left -= 0.05 * w;
            		xrange.right += 0.05 * w; */
            setXFullViewRange(xrange);
            Range1D.Double yrange = getYData().getRange1D();
            /*		w = yrange.getWidth();
            		yrange.left -= 0.05 * w;
            		yrange.right += 0.05 * w; */
            setYFullViewRange(yrange);
            Range1D.Double zrange = getZData().getRange1D();
            /*		w = zrange.getWidth();
            		zrange.right += 0.05 * w;
            		zrange.left -= 0.05 * w; */
            setZFullViewRange(zrange);

        } else {
            /* include 0.0 in y-range and add 5% on to all borders */
            /*
            		Range1D xrange = getXData().getRange();
            		double w = xrange.getWidth();
            		xrange.left -= 0.05 * w;
            		xrange.right += 0.05 * w;
            		setFullViewRangeX(xrange);
            		Range1D yrange = getYData().getRange();
            		yrange.left = Math.min(yrange.left, 0.0);
            		yrange.right = Math.max(yrange.right, 0.0);
            		w = yrange.getWidth();
            		if (yrange.left == 0.0) {
            			yrange.right += 0.05 * w;
            		} else {
            			yrange.left -= 0.05 * w;
            		}
            		setFullViewRangeY(yrange);
            	*/
        }
    }

    /**
     * gets spectrum data range.
     */
    public Range3D.Double getDataRange() {
        return new Range3D.Double(xAxisMap.getDataRange(), yAxisMap.getDataRange(), zAxisMap.getDataRange());
    }

    /**
     * gets emission spectrum at position <code>index</code>
     * @return com.labcontrol.spectrum.FluorescenceSpectrum
     * @param index int
     */
    public FluorescenceSpectrum getEmissionSpectrum(int index) {
        throw new RuntimeException("not implemented");
    }

    /**
     * gets excitation spectrum at position <code>index</code>
     * @return com.labcontrol.spectrum.FluorescenceSpectrum
     * @param index int
     */
    public FluorescenceSpectrum getExcitationSpectrum(int index) {
        throw new RuntimeException("not implemented");
    }

    /**
     * gets spectrum ID.
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.FLUORESCENCE2D;
    }

    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof NMRSpectrum)
            return true;
        return false;
    }
}