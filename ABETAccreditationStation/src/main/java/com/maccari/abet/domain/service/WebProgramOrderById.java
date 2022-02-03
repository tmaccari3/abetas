package com.maccari.abet.domain.service;

import java.util.Comparator;

import com.maccari.abet.domain.entity.WebProgram;

public class WebProgramOrderById implements Comparator<WebProgram> {
	@Override
	public int compare(WebProgram wp1, WebProgram wp2) {
		if(wp1.getId() == wp2.getId()) {
			return 0;
		}
		else if(wp1.getId() <= wp2.getId()) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
}
