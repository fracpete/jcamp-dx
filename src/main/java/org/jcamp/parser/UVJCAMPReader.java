package org.jcamp.parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.UVSpectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
/**
 * adapter between UV spectrum class and JCAMPReader.
 * @author Thomas Weber
 */
public class UVJCAMPReader extends CommonSpectrumJCAMPReader implements ISpectrumJCAMPReader {
    private final static String CRLF = "\r\n";
    private static Log log = LogFactory.getLog(UVJCAMPReader.class);

    /**
     * UVJCAMPAdapter constructor comment.
     */
    protected UVJCAMPReader() {
        super();
    }

    /**
     * read UV full spectrum.
     * 
     * @return com.creon.chem.spectrum.UVSpectrum
     * @param block com.creon.chem.jcamp.JCAMPBlock
     */
    private UVSpectrum createFS(JCAMPBlock block) throws JCAMPException {
        UVSpectrum spectrum;
        Unit xUnit = getXUnits(block);
        if (xUnit == null)
            xUnit = CommonUnit.nanometerWavelength;
        Unit yUnit = getYUnits(block);
        if (yUnit == null)
            yUnit = CommonUnit.absorbance;
        double xFactor = getXFactor(block);
        double yFactor = getYFactor(block);
        int nPoints = getNPoints(block);
        if (block.getDataRecord("XYDATA") != null) {
            double firstX = getFirstX(block);
            double lastX = getLastX(block);
            double[] intensities = getXYData(block, firstX, lastX, nPoints, xFactor, yFactor);
            if (intensities.length != nPoints)
                throw new JCAMPException("incorrect ##NPOINTS= or bad ##XYDATA=");
            IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints, xUnit);
            IDataArray1D y = new ArrayData(intensities, yUnit);
            spectrum = new UVSpectrum(x, y, true);
        } else if (block.getDataRecord("XYPOINTS") != null) {
            double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
            IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
            IDataArray1D y = new ArrayData(xy[1], yUnit);
            spectrum = new UVSpectrum(x, y, false);
        } else
            throw new JCAMPException("missing data: ##XYDATA= or ##XYPOINTS= required.");
        return spectrum;
    }

    /**
     * create UV peak table (peak spectrum) from JCAMPBlock.
     * 
     * @return UVSpectrum
     * @param block JCAMPBlock
     * @exception JCAMPException exception thrown if parsing fails.
     */
    private UVSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
        UVSpectrum spectrum = null;
        Unit xUnit = getXUnits(block);
        if (xUnit == null)
            xUnit = CommonUnit.nanometerWavelength;
        Unit yUnit = getYUnits(block);
        if (yUnit == null)
            yUnit = CommonUnit.intensity;
        double xFactor = getXFactor(block);
        double yFactor = getYFactor(block);
        int nPoints = getNPoints(block);
        Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
        Peak1D[] peaks = (Peak1D[]) tables[0];
        if (peaks.length != nPoints) {
            block.getErrorHandler().error(
                "incorrect ##NPOINTS=: expected "
                    + Integer.toString(nPoints)
                    + " but got "
                    + Integer.toString(peaks.length));
            nPoints = peaks.length;
        }
        double[][] xy = peakTableToPeakSpectrum(peaks);
        IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
        IDataArray1D y = new ArrayData(xy[1], yUnit);
        spectrum = new UVSpectrum(x, y, false);
        spectrum.setPeakTable(peaks);
        if (tables.length > 1) {
            spectrum.setPatternTable((Pattern[]) tables[1]);
            if (tables.length > 2)
                spectrum.setAssignments((Assignment[]) tables[2]);
        }
        return spectrum;
    }

    /**
     * createSpectrum method comment.
     */
    public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
        if (block.getSpectrumID() != ISpectrumIdentifier.UV)
            block.getErrorHandler().fatal("JCAMP reader adapter missmatch");
        boolean isFID = false;
        UVSpectrum spectrum = null;
        JCAMPDataRecord ldrDataType = block.getDataRecord("DATATYPE");
        String dataType = ldrDataType.getContent().toUpperCase();
        JCAMPBlock.Type type = block.getType();
        if (type.equals(JCAMPBlock.Type.FULLSPECTRUM))
            spectrum = createFS(block);
        else if (type.equals(JCAMPBlock.Type.PEAKTABLE))
            spectrum = createPeakTable(block);
        else if (type.equals(JCAMPBlock.Type.ASSIGNMENT))
            spectrum = createPeakTable(block);
        else // never reached
            block.getErrorHandler().fatal("illegal block type");
        setNotes(block, spectrum);
        return spectrum;
    }
}