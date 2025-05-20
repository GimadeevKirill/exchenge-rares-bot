package com.enansin.exchenge_rates_bot.service;

import com.enansin.exchenge_rates_bot.exception.ServiceException;

public interface ExchengeRatesService {
	
	String getUSDExchengeRate() throws ServiceException;
	
	String getEURExchengeRate() throws ServiceException;

}
