/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.io.File;
import java.io.IOException;

import org.jcamp.spectrum.Spectrum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestJCAMPReader {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCreateSpectrumInconsitentNumberOfHeaderColumns01()
			throws IOException, JCAMPException {
		Spectrum s = JCAMPReader
				.getInstance()
				.createSpectrum(
						new File(
								"src/test/resources/testdata_BLAAF/BLAAF172ripac.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum01() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF172rpac.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum02() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-a.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum03() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-b.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum04() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-c.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum05() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-d.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum06() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-e.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum07() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h-own.jdx"));
		System.out.println(s);
	}

	@Ignore
	// TODO: Cannot read binary
	@Test
	public final void testCreateSpectrum08() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF41h.esp"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum09() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_BLAAF/BLAAF42h.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrum10() throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_LEV/LEVLA2h.jdx"));
		System.out.println(s);
	}

	@Test
	public final void testCreateSpectrumInconsitentNumberOfHeaderColumns02()
			throws IOException, JCAMPException {
		Spectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_LEV/LEVLA2hri.jdx"));
		System.out.println(s);
	}

}
