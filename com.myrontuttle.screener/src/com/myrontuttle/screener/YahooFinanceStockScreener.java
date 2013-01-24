package com.myrontuttle.screener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

public class YahooFinanceStockScreener implements Screener {
    final static WebClient webClient;

	public final static String SCHEME = "http";
	public final static String HOST = "screener.finance.yahoo.com";
	public final static String STOCK_QUERY_PATH = "/stocks.html";
	public final static String STOCK_RESULT_PATH = "/b";

	public final static List<NameValuePair> BASE_PARAMS = new ArrayList<NameValuePair>();
	
    static {
        webClient = new WebClient();
        webClient.setJavaScriptEnabled(false);
        webClient.setCssEnabled(false);

		BASE_PARAMS.add(new BasicNameValuePair("vw", "1"));
		BASE_PARAMS.add(new BasicNameValuePair("db", "stocks"));
		// &vw=1&db=stocks
	}

    private static YahooFinanceStockScreener screener;
    
	private ArrayList<ScreenCriteria> criteria;
	
	private YahooFinanceStockScreener() {
		String URI = buildURI(null, STOCK_QUERY_PATH).toString();
        try {
    	    final HtmlPage page = webClient.getPage(URI);
    	    this.criteria = getSelections(page);
        } catch (Exception e) {
        	System.out.println("Problem connecting to " + URI);
            e.printStackTrace();
            this.criteria = null;
        }
	}

	public static synchronized YahooFinanceStockScreener getScreener() {
		if (screener != null) {
			return screener;
		}
		screener = new YahooFinanceStockScreener();
		return screener;
	}

	@Override
	public ArrayList<ScreenCriteria> getAvailableCriteria() {
		return criteria;
	}
	
	private static ArrayList<ScreenCriteria> getSelections(HtmlPage page) {
		ArrayList<ScreenCriteria> selections = new ArrayList<ScreenCriteria>(10);
		
		DomNodeList<HtmlElement> selects = page.getElementsByTagName("select");
		for (HtmlElement select : selects) {
			ArrayList<String> values = new ArrayList<String>(5);
			
			HtmlSelect sel = (HtmlSelect) select;
			for (HtmlOption option : sel.getOptions()) {
				values.add(option.getValueAttribute());
			}
			if (!sel.getNameAttribute().equals("vw")) {
				String[] valuesArray = new String[values.size()];
				values.toArray(valuesArray);
				selections.add(
						new ScreenCriteria(sel.getNameAttribute(), valuesArray));
			}
		}
		
		return selections;
	}

	@Override
	public List<String> screen(int[] selectedCriteria) {
			List<NameValuePair> selected = 
				new ArrayList<NameValuePair>(criteria.size());
			
			for (int i=0; i<selectedCriteria.length; i++) {
				selected.add(
					new BasicNameValuePair(
						criteria.get(i).getName(), 
						criteria.get(i).getValue(selectedCriteria[i])));
			}
			
			return screenForSymbols(selected);
	}
	
	static ArrayList<String> screenForSymbols(List<NameValuePair> selected) {

		if (selected == null) {
        	System.out.println("Error: No criteria selected");
        	return null;
		}
		selected.addAll(BASE_PARAMS);
		String URI = buildURI(selected, STOCK_RESULT_PATH).toString();
		
        try {
    	    final HtmlPage page = webClient.getPage(URI);
    		return getSymbols(page);
        } catch (Exception e) {
        	System.out.println("Problem connecting to " + URI);
            e.printStackTrace();
            return null;
        }
	}

	private static URI buildURI(List<NameValuePair> parameters, String path) {
		String query;
		if (parameters != null) {
			query = URLEncodedUtils.format(parameters, "UTF-8");
		} else {
			query = null;
		}
		try {
			URI uri = URIUtils.createURI(SCHEME, HOST, -1, path, query, null);
			return uri;
		} catch (URISyntaxException e) {
			System.out.println("Could not create URI with scheme: " + SCHEME +
					", host: " + HOST + ", path: " + path + ", and query: " + query);
			e.printStackTrace();
			return null;
		}
	}
	
	private static ArrayList<String> getSymbols(HtmlPage page) {
		ArrayList<String> symbols = new ArrayList<String>(10);
		
		final DomNodeList<HtmlElement> tables = page.getElementsByTagName("table");
		for (final HtmlElement el : tables) {
			if (el.getAttribute("border").equals("1")) {
				HtmlTable table = (HtmlTable) el;
				
				for (int i=1; i<table.getRows().size(); i++) {
				    symbols.add(table.getRow(i).getCell(0).asText());
				}
			}
			
		}
		return symbols;
	}

	@Override
	public boolean areValid(int[] selectedCriteria) {
		for (int i=0; i<criteria.size(); i++) {
			if (selectedCriteria[i] < 0 || 
					selectedCriteria[i] >= criteria.get(i).getLength()) {
				return false;
			}
		}
		return true;
	}
}
