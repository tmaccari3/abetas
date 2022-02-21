package com.maccari.abet.utility;

import java.util.Comparator;

import com.maccari.abet.domain.entity.Program;

public class ProgramComparator implements Comparator<Program>{

	@Override
	public int compare(Program p1, Program p2) {
		if(p1.getId() == p2.getId()) {
			return 0;
		}
		else {
			return compareByChar(p1.getName(), p2.getName());
		}
	}

	private int compareByChar(String str1, String str2) {
		if(str1.compareTo(str2) <= -1) {
			return -1;
		}
		else if (str1.compareTo(str2) >= 1) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
