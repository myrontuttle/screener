package com.myrontuttle.screener;

import com.myrontuttle.adaptivetrader.ScreenerService;
import com.myrontuttle.adaptivetrader.ScreenCriteria;

public class SimpleScreen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ScreenerService screener = new YahooFinanceStockScreener();
		
		ScreenCriteria[] options = null;
		try {
			options = screener.getAvailableCriteria();
		} catch (Exception e) {
			System.out.println("Error getting screen criteria: " + e.getMessage());
			e.printStackTrace();
		}
		
		int[] selected = new int[options.length / 4];
		
		for (int i=0; i<selected.length; i++) {
			selected[i] = options[i].getLength() - 1;
		}
		
		try {
			if (screener.areValid(selected)) {
				String symbols[] = screener.screen(selected);
				for (String symbol : symbols) {
					System.out.println(symbol);
				}
			} else {
				System.out.println("Invalid selection");
			}
		} catch (Exception e) {
			System.out.println("Error screening for stocks: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
