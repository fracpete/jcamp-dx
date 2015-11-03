/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcamp.spectrum;

/**
 *
 * @author fr
 */
public interface INMRSpectrum extends ISpectrum {

	String getSolvent();

	void setSolvent(String solvent);

	Double getTemperature();

	void setTemperature(Double temperature);
}
