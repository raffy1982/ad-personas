/**
 * Copyright (C) 2012 Vincenzo Pirrone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.kdcloud.lib.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import weka.core.DenseInstance;
import weka.core.Instances;

import com.kdcloud.lib.domain.ModalitySpecification;

public class StubClient extends BaseClient {
	
	

	public StubClient(String url) throws ParserConfigurationException {
		super(url);
	}

	public StubClient(String url, ModalitySpecification modality)
			throws ParserConfigurationException {
		super(url, modality);
	}

	@Override
	public void log(String message, Throwable thrown) {
		System.out.println(message);
		thrown.printStackTrace();
	}
	
	public static double[] readData(String file) {
		BufferedReader in = null;
		Vector<Double> sign = new Vector<Double>();
		try {
			InputStream stream = StubClient.class.getClassLoader().getResourceAsStream(file);
			in = new BufferedReader(new InputStreamReader(stream));
			String line = in.readLine(); //header
			line = in.readLine();
			while (line != null) {
				// System.out.print(line+"\t");
				// System.out.println(Double.parseDouble(line));
				sign.add(Double.parseDouble(line));
				line = in.readLine();
			}
			;
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		double[] res = new double[sign.size()];
		for (int i = 0; i < sign.size(); i++) {
			res[i] = sign.get(i);
		}
		return res;
	}

	@Override
	public Instances getData() {
		double[] values = readData("ecg_small.csv");
		log("data length: " + values.length);
		Instances data = modality.getInputSpecification().newInstances("test");
		for (int i = 0; i < values.length; i++) {
			double[] cells = { values[i] };
			data.add(new DenseInstance(1, cells));
		}
		return data;
	}

	@Override
	public String handleChoice(String parameterName, String[] choices) {
		return choices[0];
	}

	@Override
	public void report(Document view) {
		System.out.println("this is a report");
	}
	
	public static void main(String[] args) throws Exception {
//		String url = "http://localhost:8888";
		String url = "https://snapshot.kd-cloud.appspot.com";
		BaseClient kdcloud = new StubClient(url);
		kdcloud.setRepeatAllowed(false);
		kdcloud.setAuthentication("admin", "admin");
		List<ModalitySpecification> modalities = kdcloud.getModalities();
		ModalitySpecification dataFeed = modalities.get(0);
		kdcloud.setModality(dataFeed);
		kdcloud.executeModality();
		ModalitySpecification single = modalities.get(1);
		kdcloud.setModality(single);
		kdcloud.executeModality();
		ModalitySpecification global = modalities.get(2);
		kdcloud.setModality(global);
		kdcloud.executeModality();
	}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public void handleResourceException(Status status, ResourceException e) {
		System.out.println(status);
	}

}
