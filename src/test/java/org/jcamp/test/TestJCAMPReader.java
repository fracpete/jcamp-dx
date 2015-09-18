package org.jcamp.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.jcamp.parser.JCAMPReader;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.MassSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.Spectrum;

public class TestJCAMPReader extends TestCase {

  /**
   * Returns the tmp directory.
   *
   * @return		the tmp directory
   */
  public String getTmpDirectory() {
    return System.getProperty("java.io.tmpdir");
  }

  /**
   * Returns the location in the tmp directory for given resource.
   *
   * @param resource	the resource (path in project) to get the tmp location for
   * @return		the tmp location
   * @see		#getTmpDirectory()
   */
  public String getTmpLocationFromResource(String resource) {
    String	result;
    File	file;

    file   = new File(resource);
    result = getTmpDirectory() + File.separator + file.getName();

    return result;
  }

  /**
   * Copies the given resource to the tmp directory.
   *
   * @param resource	the resource (path in project) to copy
   * @return		false if copying failed
   * @see		#getTmpLocationFromResource(String)
   */
  public boolean copyResourceToTmp(String resource) {
    boolean			result;
    BufferedInputStream	input;
    BufferedOutputStream	output;
    byte[]			buffer;
    int			read;

    input    = null;
    output   = null;

    try {
      input  = new BufferedInputStream(ClassLoader.getSystemResourceAsStream(resource));
      output = new BufferedOutputStream(new FileOutputStream(getTmpLocationFromResource(resource)));
      buffer = new byte[1024];
      while ((read = input.read(buffer)) != -1) {
	output.write(buffer, 0, read);
	if (read < buffer.length)
	  break;
      }
      result = true;
    }
    catch (IOException e) {
      if (e.getMessage().equals("Stream closed"))
	System.err.println("Resource '" + resource + "' not available?");
      e.printStackTrace();
      result = false;
    }
    catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    if (input != null) {
      try {
	input.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
    if (output != null) {
      try {
	output.close();
      }
      catch (Exception e) {
	// ignored
      }
    }

    return result;
  }

  /**
   * Removes the file from the tmp directory.
   *
   * @param filename	the file in the tmp directory to delete (no path!)
   * @return		true if deleting succeeded or file not present
   */
  public boolean deleteFileFromTmp(String filename) {
    boolean	result;
    File	file;

    result = true;
    file   = new File(getTmpDirectory() + File.separator + filename);
    if (file.exists())
      result = file.delete();

    return result;
  }

  /**
   * Called by JUnit before each test method.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    copyResourceToTmp("testdata/spinworks.dx");
    copyResourceToTmp("testdata/1567755.jdx");
    copyResourceToTmp("testdata/cpd01.jdx");
    copyResourceToTmp("testdata/bug1054.jdx");
    copyResourceToTmp("testdata/bug1054withoutspace.jdx");
    copyResourceToTmp("testdata/mzdiv-813_c.jdx");
    copyResourceToTmp("testdata/ir_floats.jdx");
    copyResourceToTmp("testdata/jcamp60.jdx");
    copyResourceToTmp("testdata/doubleformat.jdx");
  }

  /**
   * Called by JUnit after each test method.
   *
   * @throws Exception	if tear-down fails
   */
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    deleteFileFromTmp("spinworks.dx");
    deleteFileFromTmp("1567755.jdx");
    deleteFileFromTmp("cpd01.jdx");
    deleteFileFromTmp("bug1054.jdx");
    deleteFileFromTmp("bug1054withoutspace.jdx");
    deleteFileFromTmp("mzdiv-813_c.jdx");
    deleteFileFromTmp("ir_floats.jdx");
    deleteFileFromTmp("jcamp60.jdx");
    deleteFileFromTmp("doubleformat.jdx");
  }

  /**
   * Reads the file into a StringBuilder.
   * 
   * @param file	the file to read
   * @return		the content
   * @throws Exception	if reading of file fails
   */
  protected StringBuilder readFile(String file) throws Exception {
    StringBuilder fileData = new StringBuilder(1000);
    BufferedReader reader = new BufferedReader(new FileReader(getTmpDirectory() + File.separator + file));
    char[] buf = new char[1024];
    int numRead=0;
    while((numRead=reader.read(buf)) != -1){
      String readData = String.valueOf(buf, 0, numRead);
      fileData.append(readData);
      buf = new char[1024];
    }
    reader.close();
    return fileData;
  }
  
  public void testSpinworks() throws Exception{
    StringBuilder fileData = readFile("spinworks.dx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof NMRSpectrum)) {
      throw new Exception("Spectrum in file is not an NMR spectrum!");
    }
    NMRSpectrum nmrspectrum = (NMRSpectrum) jcampSpectrum;
    if (nmrspectrum.hasPeakTable()) {
      assertEquals(nmrspectrum.getPeakTable().length,16384);
    }
  }

  public void testMoreThan49Peaks() throws Exception{
    StringBuilder fileData = readFile("1567755.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof MassSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    MassSpectrum massspectrum = (MassSpectrum) jcampSpectrum;
    assertTrue(massspectrum.hasPeakTable());
    if (massspectrum.hasPeakTable()) {
      assertEquals(massspectrum.getPeakTable().length,54);
    }
  }

  public void testPMR() throws Exception{
    StringBuilder fileData = readFile("cpd01.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof MassSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    MassSpectrum massspectrum = (MassSpectrum) jcampSpectrum;
    assertTrue(massspectrum.hasPeakTable());
    if (massspectrum.hasPeakTable()) {
      assertEquals(25,massspectrum.getPeakTable().length);
    }

  }

  public void testBugBioclipse1054() throws Exception{
    StringBuilder fileData = readFile("bug1054.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof MassSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    MassSpectrum massspectrum = (MassSpectrum) jcampSpectrum;
    assertTrue(massspectrum.hasPeakTable());
    if (massspectrum.hasPeakTable()) {
      assertEquals(65,massspectrum.getPeakTable().length);
    }
  }

  public void testBugBioclipse1054withoutspace() throws Exception{
    StringBuilder fileData = readFile("bug1054withoutspace.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof MassSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    MassSpectrum massspectrum = (MassSpectrum) jcampSpectrum;
    assertTrue(massspectrum.hasPeakTable());
    if (massspectrum.hasPeakTable()) {
      assertEquals(65,massspectrum.getPeakTable().length);
    }
  }

  public void testMzdiv813() throws Exception{
    StringBuilder fileData = readFile("mzdiv-813_c.jdx");
    //we should get the same values when reading twice
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof NMRSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    NMRSpectrum nmrspectrum = (NMRSpectrum) jcampSpectrum;
    double firstvalue = nmrspectrum.getXData().toArray()[0];
    jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof NMRSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    nmrspectrum = (NMRSpectrum) jcampSpectrum;
    assertEquals(firstvalue,nmrspectrum.getXData().toArray()[0],.1);
  }

  public void testMzdiv813Relaxed() throws Exception{
    StringBuilder fileData = readFile("mzdiv-813_c.jdx");
    //we should get the same values when reading twice
    Spectrum jcampSpectrum = JCAMPReader.getInstance(true, JCAMPReader.RELAXED).createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof NMRSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    NMRSpectrum nmrspectrum = (NMRSpectrum) jcampSpectrum;
    double firstvalue = nmrspectrum.getXData().toArray()[0];
    jcampSpectrum = JCAMPReader.getInstance(true, JCAMPReader.RELAXED).createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof NMRSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    nmrspectrum = (NMRSpectrum) jcampSpectrum;
    assertEquals(firstvalue,nmrspectrum.getXData().toArray()[0],.1);
  }

  public void testIR_floats() throws Exception{
    StringBuilder fileData = readFile("ir_floats.jdx");
    //we should get the same values when reading twice
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof IRSpectrum)) {
      throw new Exception("Spectrum in file is not an ir spectrum!");
    }
    IRSpectrum irSpectrum = (IRSpectrum) jcampSpectrum;
    assertEquals(457.0,irSpectrum.getXData().toArray()[0],.1);
    assertEquals(0.04846820945765196,irSpectrum.getYData().toArray()[0],.1);
    assertEquals(0.04832256265669936,irSpectrum.getYData().toArray()[1],.1);
    assertEquals(0.04822549191827488,irSpectrum.getYData().toArray()[2],.1);
    assertEquals(0.04812844287163563,irSpectrum.getYData().toArray()[3],.1);
    assertEquals(0.04798290995257375,irSpectrum.getYData().toArray()[4],.1);

    jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof IRSpectrum)) {
      throw new Exception("Spectrum in file is not an ir spectrum!");
    }
    irSpectrum = (IRSpectrum) jcampSpectrum;
    assertEquals(457.0,irSpectrum.getXData().toArray()[0],.1);
    assertEquals(0.04846820945765196,irSpectrum.getYData().toArray()[0],.1);
    assertEquals(0.04832256265669936,irSpectrum.getYData().toArray()[1],.1);
    assertEquals(0.04822549191827488,irSpectrum.getYData().toArray()[2],.1);
    assertEquals(0.04812844287163563,irSpectrum.getYData().toArray()[3],.1);
    assertEquals(0.04798290995257375,irSpectrum.getYData().toArray()[4],.1);
  }

  public void testBugJcamp60() throws Exception{
    StringBuilder fileData = readFile("jcamp60.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    assertTrue(jcampSpectrum instanceof NMRSpectrum);
  }

  public void testDoubleFormat() throws Exception {
    StringBuilder fileData = readFile("doubleformat.jdx");
    Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
    if (!(jcampSpectrum instanceof MassSpectrum)) {
      throw new Exception("Spectrum in file is not an mass spectrum!");
    }
    MassSpectrum massspectrum = (MassSpectrum) jcampSpectrum;
    assertTrue(massspectrum.hasPeakTable());
    if (massspectrum.hasPeakTable()) {
      assertEquals(massspectrum.getPeakTable().length,54);
    }
  }
}
