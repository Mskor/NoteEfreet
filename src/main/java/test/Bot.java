package test;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.logging.Level;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "AQAAAAAEb1x-AAWTjc5k2GqqNklrhS1aQIPFTrw";
    public static final String YANDEX_API_BASE_URI = "https://cloud-api.yandex.net/v1";
    public static final String YANDEX_API_DISK_URL = "disk";

    private static final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<String, Object>() {{
        add("Authorization", "OAuth AQAAAAAEb1x-AAWTjc5k2GqqNklrhS1aQIPFTrw");
    }};


    public Bot(DefaultBotOptions options) {
        super(options);
    }

    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(YANDEX_API_BASE_URI).path(YANDEX_API_DISK_URL);

        String response = target.request(MediaType.APPLICATION_JSON_TYPE).headers(headerMap).get(String.class);

        System.out.println(response);
    }

    /**
     * Метод для настройки сообщения и его отправки.
     *
     * @param chatId id чата
     * @param s      Строка, которую необходимот отправить в качестве сообщения.
     */
    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */

    public String getBotUsername() {
        return "NoteEfreetBot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return "798078868:AAGBbVBZmerGH45pGqeImdz1Z1_CsU0znoI";
    }

    public static void main(String[] args) {

        ApiContextInitializer.init();

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost("localhost");
        options.setProxyPort(9150);
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot(options));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}