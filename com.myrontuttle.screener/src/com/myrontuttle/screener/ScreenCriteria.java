package com.myrontuttle.screener;

public class ScreenCriteria {

	private String name;
	private String[] values;

	ScreenCriteria(String name, String[] values){
		this.name = name;
		this.values = values;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue(int index) {
		if (index < 0) {
			index = 0;
		} else if (index > values.length - 1) {
			index = values.length - 1;
		}
		
		return values[index];
	}
	
	public int getLength() {
		return values.length;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(": ");
		for (int i=0; i<values.length - 1; i++) {
			sb.append(values[i] + ", ");
		}
		sb.append(values[values.length - 1]);
		
		return sb.toString();
	}
}
