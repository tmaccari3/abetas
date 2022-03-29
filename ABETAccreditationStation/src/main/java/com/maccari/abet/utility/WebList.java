package com.maccari.abet.utility;

import java.util.ArrayList;

/*
 * WebList.java 
 * Author: Thomas Maccari
 * 
 * Description: This class inherits from the ArrayList class via composition for 
 * the purpose of defining a custom toString method for ease of displaying on 
 * a webpage.
 * 
 */

public class WebList<T> {
	private ArrayList<T> list;
	
	public WebList() {
		this.list = new ArrayList<T>();
	}
	
	public WebList(ArrayList<T> list) {
		this.list = list;
	}
	
	public void add(T item) {
		list.add(item);
	}
	
	public void remove(int index) {
		list.remove(index);
	}
	
	public void remove(T item) {
		list.remove(item);
	}
	
	public T get(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public String toString() {
		if(list.size() == 0) {
			return "N/A";
		}
		
		String result = "";
		int iter;
		for(iter = 0; iter < list.size() - 1; iter++) {
			result += list.get(iter).toString() + ", ";
		}
		T item = list.get(iter);
		if(item != null) {
			result += item.toString();
		}
		
		return result;
	}
	
	public ArrayList<T> getList() {
		return list;
	}
}
