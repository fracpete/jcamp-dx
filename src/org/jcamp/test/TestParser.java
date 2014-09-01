package org.jcamp.test;

import java.io.BufferedReader;
import java.io.FileReader;

import junit.framework.TestCase;

import org.jcamp.parser.JCAMPReader;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.MassSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.Spectrum;

public class TestParser extends TestCase
{
	
    public void testSpinworks() throws Exception{

        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/spinworks.dx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();


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

        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/1567755.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();


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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/cpd01.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();


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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/bug1054.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();


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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/bug1054withoutspace.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();


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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/mzdiv-813_c.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
    
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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/mzdiv-813_c.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
    
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
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(new FileReader("testdata/ir_floats.jdx"));
      char[] buf = new char[1024];
      int numRead=0;
      while((numRead=reader.read(buf)) != -1){
          String readData = String.valueOf(buf, 0, numRead);
          fileData.append(readData);
          buf = new char[1024];
      }
      reader.close();
  
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
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader("testdata/jcamp60.jdx"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();

        Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(fileData.toString());
        assertTrue(jcampSpectrum instanceof NMRSpectrum);
    }

}
