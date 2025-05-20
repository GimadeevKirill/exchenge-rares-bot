package com.enansin.exchenge_rates_bot.service.impl;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.enansin.exchenge_rates_bot.client.CbrClient;
import com.enansin.exchenge_rates_bot.exception.ServiceException;
import com.enansin.exchenge_rates_bot.service.ExchengeRatesService;

@Service
public class ExchengeRatesServiceImpl implements ExchengeRatesService {

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
	
	@Autowired
	private CbrClient client;

	@Override
	public String getUSDExchengeRate() throws ServiceException {
		String xml = client.getCurrencyRatesXML();
		return extractCurrencyValueFromXML(xml, USD_XPATH);
	}

	@Override
	public String getEURExchengeRate() throws ServiceException {
		String xml = client.getCurrencyRatesXML();
		return extractCurrencyValueFromXML(xml, EUR_XPATH);
	}
	
	private static String extractCurrencyValueFromXML(String xml, String xpathExpression) throws ServiceException {
		InputSource source = new InputSource(new StringReader(xml));
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			Document document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			
			return xpath.evaluate(xpathExpression, document);
		} catch (XPathExpressionException e) {
			throw new ServiceException("Не удалось распарсить XML", e);
		}
	}

}
