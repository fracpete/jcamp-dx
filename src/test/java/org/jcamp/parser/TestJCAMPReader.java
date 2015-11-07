/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jcamp.VisitorJCAMPBlock;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.ISpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestJCAMPReader {

	static void iterateAll(JCAMPBlock b, VisitorJCAMPBlock visitor) {
		visitor.transform(b);
		for (JCAMPBlock bb : b.getChildBlocks()) {
			iterateAll(bb, visitor);
		}
	}

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
	public final void testAccessPeakList01() throws IOException, JCAMPException {
		ISpectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata/1567755.jdx"));
		assertNotNull(s);
		assertTrue(s.hasPeakTable());
		assertNotNull(s.getPeakTable());
		assertEquals(54, s.getPeakTable().length);
	}

	@Test
	public final void testAccessPeakList02() throws IOException, JCAMPException {
		JCAMPBlock b = new JCAMPBlock(FileUtils.readFileToString(
				new File("src/test/resources/testdata2/2-MAPB.jcamp")));
		assertNotNull(b);
		iterateAll(b, new VisitorJCAMPBlock() {

			@Override
			public Void transform(JCAMPBlock element, int cnt) {
				// 4th block contains peak table
				if (cnt == 4) {
					try {
						ISpectrum s = JCAMPReader.getInstance().createSpectrum(
								element);
						assertNotNull(s);
						assertNotNull(s.getPeakTable());
					} catch (JCAMPException e) {
						throw new RuntimeException(e);
					}
				}
				return null;
			}
		});
	}

	@Test
	public final void testAccessPeakList03() throws IOException, JCAMPException {
		JCAMPBlock b = new JCAMPBlock(
				FileUtils.readFileToString(new File(
								"src/test/resources/testdata2/S2015_1275_1 3-fpm_mit_Struc.jcamp")));
		assertNotNull(b);
		iterateAll(b, new VisitorJCAMPBlock() {

			@Override
			public Void transform(JCAMPBlock element, int cnt) {
				// 4th block contains peak table
				if (cnt == 4) {
					try {
						ISpectrum s = JCAMPReader.getInstance().createSpectrum(
								element);
						assertNotNull(s);
						assertNotNull(s.getPeakTable());
					} catch (JCAMPException e) {
						throw new RuntimeException(e);
					}
				}
				return null;
			}
		});

	}

	@Test
	public final void testAccessPeakList04() throws IOException, JCAMPException {
		JCAMPBlock b = new JCAMPBlock(
				FileUtils.readFileToString(new File(
								"src/test/resources/testdata_BLAAF/BLAAF41h-a.jdx")));
		assertNotNull(b);
		iterateAll(b, new VisitorJCAMPBlock() {

			@Override
			public Void transform(JCAMPBlock element, int cnt) {
				// 3th block contains peak table
				if (cnt == 3) {
					try {
						ISpectrum s = JCAMPReader.getInstance().createSpectrum(
								element);
						assertNotNull(s);
						assertNotNull(s.getPeakTable());
					} catch (JCAMPException e) {
						throw new RuntimeException(e);
					}
				}
				return null;
			}
		});

	}

	@Ignore
	@Test
	public final void testCreateSpectrum01() throws IOException, JCAMPException {
		ISpectrum s = JCAMPReader
				.getInstance()
				.createSpectrum(
						new File(
								"src/test/resources/testdata2/S2014_5533_94 AM-1220_aceton-HSQC.jcamp"));
		assertNotNull(s);

	}

	@Test
	public final void testCreateSpectrum02() throws IOException, JCAMPException {
		ISpectrum s = JCAMPReader
				.getInstance()
				.createSpectrum(
						new File(
								"src/test/resources/testdata_BLAAF/BLAAF41h-a.jdx"));
		assertNotNull(s);
		assertEquals(NMRSpectrum.class, s.getClass());
		NMRSpectrum ns = (NMRSpectrum) s;
		assertNotNull(ns.getXData());
		assertNotNull(ns.getYData());

	}

	@Test
	public final void testCreateSpectrumInconsitentNumberOfHeaderColumns01()
			throws IOException, JCAMPException {
		ISpectrum s = JCAMPReader
				.getInstance()
				.createSpectrum(
						new File(
								"src/test/resources/testdata_BLAAF/BLAAF172ripac.jdx"));
		assertNotNull(s);
	}

	@Test
	public final void testCreateSpectrumInconsitentNumberOfHeaderColumns02()
			throws IOException, JCAMPException {
		ISpectrum s = JCAMPReader.getInstance().createSpectrum(
				new File("src/test/resources/testdata_LEV/LEVLA2hri.jdx"));
		assertNotNull(s);
	}

	@Test
	public final void testCreateSpectrumInfrared01() throws IOException,
			JCAMPException {
		ISpectrum s = JCAMPReader
				.getInstance()
				.createSpectrum(
						new File(
								"src/test/resources/testdata2/PE1800.DX"));
		assertNotNull(s);
		assertEquals(IRSpectrum.class, s.getClass());
		assertNotNull(s.getXData());
		assertNotNull(s.getYData());
	}

	@Test
	public final void testWrongSpectrumType01() throws IOException,
			JCAMPException {
		JCAMPBlock b = new JCAMPBlock(FileUtils.readFileToString(new File(
				"src/test/resources/testdata_BLAAF/BLAAF172rpac.jdx")));
		assertNotNull(b);
		iterateAll(b, new VisitorJCAMPBlock() {
			@Override
			public Void transform(JCAMPBlock element, int cnt) {
				// third block contains peak table
				if (cnt == 2) {
					try {
						ISpectrum s = JCAMPReader.getInstance().createSpectrum(
								element);
						assertNotNull(s);
						assertNotNull(s.getPeakTable());
					} catch (JCAMPException e) {
						throw new RuntimeException(e);
					}
				}
				return null;
			}

		});
	}
}
