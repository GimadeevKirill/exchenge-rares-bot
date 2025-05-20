package com.enansin.exchenge_rates_bot.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enansin.exchenge_rates_bot.exception.ServiceException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Component
public class CbrClient {

	@Autowired
	private OkHttpClient client;
	
	@Value("${cbr.currency.rates.xml.url}")
	private String url;
	
	public String getCurrencyRatesXML() throws ServiceException {
		Request request = new Request.Builder()
				.url(url)
				.build();
		
		try (Response response = client.newCall(request).execute();) {
			ResponseBody body = response.body();
			return body == null ? null : body.string();
		} catch (IOException e) {
			throw new ServiceException("Ошибка получения курсов валют", e);	
		}
	}
}
