package org.jcamp.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * check handler for ASDF parser that uses x and y-checks.
 * 
 * @author Thomas Weber
 */
public class ReplacingCheckHandler
  implements ASDFCheckHandler {
  
  private double firstX;
  private double lastX;
  private double deltaX;
  private double xFactor;
  private int nPoints;
  private static Log log = LogFactory.getLog(ReplacingCheckHandler.class);

  /**
   * ReplacingCheckHandler constructor comment.
   */
  public ReplacingCheckHandler(double firstX, double lastX, double xFactor, int nPoints) {
    super();
    this.firstX = firstX;
    this.lastX = lastX;
    this.xFactor = xFactor;
    this.deltaX = (lastX - firstX) / (nPoints - 1);
    this.nPoints = nPoints;
  }

  /**
   * @see com.creon.chem.jcamp.ASDFCheckHandler
   */
  public int check(ASDFParseState state) {
    // perform x check
    double checkX = xFactor * state.getCheckX();
    double realX = firstX + state.getCurrentIndex() * deltaX;
    if (Math.abs(checkX - realX) * xFactor > 0.1) {
      log.warn(
	  "x check failed in line"
	      + state.getCurrentLineNumber()
	      + ": expected x = "
	      + Double.toString(checkX)
	      + " but got "
	      + Double.toString(realX));
      int newIndex = (int) Math.floor((checkX - firstX) / deltaX + .5);
      if (newIndex < 0 || newIndex >= nPoints) {
	// x check is bad, do not modify anything
	log.error("bad x check value");
	return IGNORE;
      } else
	state.setCurrentIndex(newIndex);
      return REPLACE;
    }
    // perform y check
    double checkY = state.getCheckY();
    double realY = state.getCurrentY();
    if (checkY != realY) {
      log.warn(
	  "y check failed in line "
	      + state.getCurrentLineNumber()
	      + ": expected y = "
	      + Double.toString(checkY)
	      + " but got "
	      + Double.toString(realY));
      return REPLACE;
    }
    if (log.isDebugEnabled())
      log.debug("line " + state.getCurrentLineNumber() + ": check passed");
    return IGNORE;
  }
}