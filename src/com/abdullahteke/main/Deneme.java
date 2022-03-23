package com.abdullahteke.main;

import com.abdullahteke.controller.TimeController;

public class Deneme {

	public Deneme() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(TimeController.getManagerInstance().get5MinutesBefore());
		System.out.println(TimeController.getManagerInstance().getCurrentDateTime());
		System.out.println(TimeController.getManagerInstance().abc("1.9173644E7"));
	}

}
