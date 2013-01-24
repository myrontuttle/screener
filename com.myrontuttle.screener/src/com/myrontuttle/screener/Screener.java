/**
 * 
 */
package com.myrontuttle.screener;

import java.util.List;

/**
 * Screens financial instruments with a variety of features
 * @author Myron Tuttle
 */
public interface Screener {
    /**
     * Shows the criteria available to screen against.
     * @return A list of available screener options.
     */
	List<ScreenCriteria> getAvailableCriteria();
	
	/**
	 * Given a set of criteria to screen for, returns the financial instruments
	 * matching them.
	 * @param An array of the selected criteria used to screen instruments
	 * @return A list of financial instruments
	 */
	List<String> screen(int[] selectedCriteria);
	
	/**
	 * Checks a set of selected criteria to determine if they're valid for this
	 * screener's available criteria.
	 * @param An array of the selected criteria used to screen instruments
	 * @return true if the selected criteria are valid
	 */
	boolean areValid(int[] selectedCriteria);
}
