package com.enansin.exchenge_rates_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.enansin.exchenge_rates_bot.bot.ExchengeRatesBot;

import okhttp3.OkHttpClient;

@Configuration
public class ExchengeRatesBotConfig {

	@Bean
	public TelegramBotsApi telegramBotsApi(ExchengeRatesBot exchengeRatesBot) throws TelegramApiException {
		TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(exchengeRatesBot);
		return api;
	}
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
	}
}
