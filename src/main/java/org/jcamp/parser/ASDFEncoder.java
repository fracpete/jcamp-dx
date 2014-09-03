package org.jcamp.parser;

/**
 * Utility class for ASDF encoding of spectrum data
 * @author Thomas Weber
 */
public class ASDFEncoder {
  
  private final static char[] posSQZ = new char[] { '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
  private final static char[] negSQZ = new char[] { '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' };
  private final static char[] posDIF = new char[] { '%', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R' };
  private final static char[] negDIF = new char[] { '%', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r' };
  private final static char[] posDUP = new char[] { ' ', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 's' };
  private final static int MODE_SQZ = 0;
  private final static int MODE_DIF = 1;
  private final static int MAX_CHARS_PER_LINE = 60;
  
  /**
   * JCAMPWriter constructor comment.
   */
  private ASDFEncoder() {
  }
  /**
   * encode an integer intensity array with ASDF (ASCII Squeezed Difference Form)
   * 
   * @return String
   * @param firstx double first x value
   * @param lastx double last x value
   * @param data int[] array of y values
   */
  public static String encode(double firstx, double lastx, int[] data) {
    int mode = MODE_SQZ;
    StringBuilder line = new StringBuilder();
    StringBuilder difdup = new StringBuilder();
    int n = data.length;
    int dup = 1;
    int dif;
    int odif = 0;
    boolean checkDUP = false;
    double deltax = (lastx - firstx) / (n - 1);
    line.append(firstx);
    line.append(toSQZ(data[0]));
    for (int i = 1; i < n; i++) {
      if (line.length() > MAX_CHARS_PER_LINE) {
	// insert linefeed
	difdup.append(line).append("\r\n");
	line.setLength(0);
	// if last mode was DIF
	if (mode == MODE_DIF) {
	  // insert x-sequence check (no advance in abszissa)
	  line.append(deltax * (i - 1) + firstx);
	  // insert a  y-value check (repeat last value)
	  line.append(toSQZ(data[i - 1]));
	  mode = MODE_SQZ;
	  dup = 1;
	  checkDUP = false;
	} else {
	  // insert x-sequence check 	and actual value
	  line.append(deltax * i + firstx);
	  line.append(toSQZ(data[i]));
	  mode = MODE_SQZ;
	  dup = 1;
	  checkDUP = false;
	  continue;
	}
      }

      // calc difference
      dif = data[i] - data[i - 1];
      char[] difChars = toDIF(dif);
      char[] sqzChars = toSQZ(data[i]);

      // if difChars are longer than sqzChars switch back to SQZ-mode
      if (difChars.length > sqzChars.length) {
	// check if we have pending DUPs
	if (dup > 1) {
	  line.append(toDUP(dup));
	  dup = 1;
	}
	line.append(sqzChars);
	checkDUP = false;
	mode = MODE_SQZ;
      } else { // DIF-mode
	// check if difference has not changed
	if (checkDUP && dif == odif) {
	  dup++; // increase DUP counter but do not output 
	} else {
	  // different difference, we must check for pending DUPs
	  if (dup > 1) {
	    line.append(toDUP(dup));
	    dup = 1;
	  }
	  line.append(difChars);
	  odif = dif;
	}
	checkDUP = true;
	mode = MODE_DIF;
      }
    }
    if (dup > 1)
      line.append(toDUP(dup));
    difdup.append(line).append("\r\n");
    // if last mode was DIF
    if (mode == MODE_DIF) {
      // insert x-sequence check (no advance in abszissa)
      difdup.append(deltax * (n - 1) + firstx);
      // insert a  y-value check (repeat last value)
      difdup.append(toSQZ(data[n - 1]));
      difdup.append("\r\n");
    }

    return difdup.toString();
  }
  /**
   * DIF encoding of an integer
   * 
   * @return char[]
   * @param value int
   */
  private static char[] toDIF(int value) {
    if (value > 0) {
      char[] dif = Integer.toString(value).toCharArray();
      char first = dif[0];
      dif[0] = posDIF[first - '0'];
      return dif;
    } else if (value < 0) {
      char[] dif = Integer.toString(-value).toCharArray();
      char first = dif[0];
      dif[0] = negDIF[first - '0'];
      return dif;
    } else
      return new char[] { '%' };
  }
  /**
   * DUP encoding of an integer
   * 
   * @return char[]
   * @param value int
   */
  private static char[] toDUP(int value) {
    char[] dup = Integer.toString(value).toCharArray();
    char first = dup[0];
    dup[0] = posDUP[first - '0'];
    return dup;
  }
  /**
   * SQZ encoding of an integer
   * 
   * @return char[]
   * @param value int
   */
  private static char[] toSQZ(int value) {
    if (value > 0) {
      char[] sqz = Integer.toString(value).toCharArray();
      char first = sqz[0];
      sqz[0] = posSQZ[first - '0'];
      return sqz;
    } else if (value < 0) {
      char[] sqz = Integer.toString(-value).toCharArray();
      char first = sqz[0];
      sqz[0] = negSQZ[first - '0'];
      return sqz;
    } else
      return new char[] { '@' };
  }
}
