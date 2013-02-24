package com.myrontuttle.screener;

import com.myrontuttle.adaptivetrader.ScreenerService;
import com.myrontuttle.adaptivetrader.AvailableScreenCriteria;
import com.myrontuttle.adaptivetrader.SelectedScreenCriteria;

public class SimpleScreen {

	public final static int MAX_SYMBOLS = 20;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ScreenerService screener = new YahooFinanceStockScreener();
		
		AvailableScreenCriteria[] availableCriteria = null;
		try {
			availableCriteria = screener.getAvailableCriteria();
		} catch (Exception e) {
			System.out.println("Error getting screen criteria: " + e.getMessage());
			e.printStackTrace();
		}
		
		SelectedScreenCriteria[] selected = new SelectedScreenCriteria[4];
		
		for (int i=0; i<selected.length; i++) {
			selected[i] = new SelectedScreenCriteria(
								availableCriteria[i].getName(),
								availableCriteria[i].getAcceptedValue(0),
								null);
		}
		
		try {
			String symbols[] = screener.screen(selected, MAX_SYMBOLS);
			for (String symbol : symbols) {
				System.out.println(symbol);
			}
		} catch (Exception e) {
			System.out.println("Error screening for stocks: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
