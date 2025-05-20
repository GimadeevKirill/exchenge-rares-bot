package com.enansin.exchenge_rates_bot.bot;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.enansin.exchenge_rates_bot.exception.ServiceException;
import com.enansin.exchenge_rates_bot.service.ExchengeRatesService;

@Component
public class ExchengeRatesBot extends TelegramLongPollingBot {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExchengeRatesBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String HELP = "/help";

    @Autowired
    private ExchengeRatesService exchangeRatesService;

	public ExchengeRatesBot(@Value("${bot.token}") String botToken) {
		super(botToken);
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
	}

	@Override
	public String getBotUsername() {
		return "ExchengeRatesBot";
	}

	private void startCommand(Long chatId, String userName) {
        String text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.
                
                Для этого воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                
                Дополнительные команды:
                /help - получение справки
                """;
        String formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }
	
	private void usdCommand(Long chatId) {
        String formattedText;
        try {
            String usd = exchangeRatesService.getUSDExchengeRate();
            String text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс доллара. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }
	
	private void eurCommand(Long chatId) {
        String formattedText;
        try {
            String usd = exchangeRatesService.getEURExchengeRate();
            String text = "Курс евро на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса евро", e);
            formattedText = "Не удалось получить текущий курс евро. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
        
	}
        
    private void helpCommand(Long chatId) {
        String text = """
                    Справочная информация по боту
                    
                    Для получения текущих курсов валют воспользуйтесь командами:
                    /usd - курс доллара
                    /eur - курс евро
                    """;
        sendMessage(chatId, text);
    }
    
    private void unknownCommand(Long chatId) {
        String text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }
    
    private void sendMessage(Long chatId, String text) {
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
