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
package com.kdcloud.lib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instances;
public class DataSpecification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static class Column implements Serializable {
		private static final long serialVersionUID = 1L;
		
		String name;
		DataType type;
		InputSource source;
		
		public String toString() {
			String returnString = "COLUMN -> ";
			if (name != null) returnString += " name: " + name;
			if (type != null) returnString += " - type: " + type;
			if (source != null) returnString += " - source: " + source;
			return  returnString;
		}
	}
	
	public enum DataType {
		DOUBLE, TIMESTAMP, INTEGER;
		
		@Override
		public String toString() {
			switch (this) {
			case DOUBLE: return "DOUBLE";
			case TIMESTAMP: return "TIMESTAMP";
			case INTEGER: return "INTEGER";
			default: return super.toString();
			}
		}
		
		public static DataType getByString(String s) {
			if (s != null) {
				if (s.equalsIgnoreCase("DOUBLE")) return DOUBLE;
				else if (s.equalsIgnoreCase("TIMESTAMP")) return DOUBLE;
				else if (s.equalsIgnoreCase("INTEGER")) return INTEGER;
			}
			return null;
		}
	}
	
	public enum InputSource {
		HEARTBEAT, CLOCK;
		
		@Override
		public String toString() {
			switch (this) {
			case HEARTBEAT: return "HEARTBEAT";
			case CLOCK: return "CLOCK";
			default: return super.toString();
			}
		}
		
		public static InputSource getByString(String s) {
			if (s != null) {
				if (s.equalsIgnoreCase("HEARTBEAT")) return HEARTBEAT;
				else if (s.equalsIgnoreCase("CLOCK")) return CLOCK;
			}
			return null;
		}
	}
	
	List<Column> columns = new LinkedList<DataSpecification.Column>();
	
	public void addColumn(String name, DataType type, InputSource source){
		Column c = new Column();
		c.name = name;
		c.type = type;
		c.source = source;
		columns.add(c);
	}
	
	
	String view;
	
	public Instances newInstances(String relationalName) {
		return new Instances(relationalName, getAttrInfo(), 1000);
	}
	
	public boolean matchingSpecification(Instances instances) {
		return new Instances("", getAttrInfo(), 0).equalHeaders(instances);
	}
	
	public ArrayList<Attribute> getAttrInfo() {
		ArrayList<Attribute> info = new ArrayList<Attribute>(columns.size());
		for (Column c : columns) {
			info.add(new Attribute(c.name));
		}
		return info;
	}
	
	public void setView(String v) {
		this.view = v;
	}

	public String getView() {
		return view;
	}

	public static Instances newInstances(String name, int columns) {
		ArrayList<Attribute> info = new ArrayList<Attribute>(columns);
		for (int i = 0; i < columns; i++) {
			info.add(new Attribute("attr" + i));
		}
		return new Instances(name, info, 0);
	}

	public List<InputSource> sources() {
		LinkedList<InputSource> list = new LinkedList<DataSpecification.InputSource>();
		for (Column c : columns)
			list.add(c.source);
		return list;
	}
	
	public String toString() {
		String returnString = "DATASPECIFICATION -> ";
		if (view != null) returnString += "view: " + view;
		if (columns != null) returnString += " - columns: " + columns;
		return  returnString;  
	}

}
