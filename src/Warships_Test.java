import ind.meffodiiy.telegram.bot.api.*;
import ind.meffodiiy.telegram.bot.interfaces.UpdateHandler;

class Warship_Test extends TelegramBot implements UpdateHandler {
    public static void main(String[] args){
        Warship_Test bot = new Warship_Test();
        bot.beginLongPollingSession();
    }

    public Warship_Test(){super("685559760:AAHTJiyik8QwIEOXm09UPm4D_pvfkcov3S8");}
    private boolean gamemode = false;
    private boolean setmode = true;
    private User user1;
    private User user2;
    private boolean ind_registr1 = false;
    private boolean ind_registr2 = false;
    private boolean ind_setting1 = false;
    private boolean isInd_setting2 = false;

    Warship warship1 = new Warship();
    Warship warship2 = new Warship();
    Message message1 = new Message();
    @Override
    public void onUpdate(Update update) {

        if(gamemode){
            if(setmode){
                switch (warship1.setShips(update.getCallbackQuery())){
                    case 1:
                        editMessageText(new Arguments.EditMessageText().setChatId(user1.getId()).setMessageId(message1.getMessageId()).setText("Поле").setReplyMarkup(warship1.getField(warship1.field_my)));

                        //sendMessageToUser("Поставил!", user1);
                        break;
                    case 2:
                        sendMessageToUser("Воу, тут же слишком близко корабль!", user1);

                        break;
                    case 3:
                        sendMessageToUser("Ты шо такие большие корабли строишь?!?!", user1);

                        break;
                    case 4:
                        sendMessageToUser("Занято!", user1);

                        break;
                }
                answerCallbackQuery(new Arguments.AnswerCallbackQuery().setCallbackQueryId(update.getCallbackQuery().getId()));
                //editMessageText(new Arguments.EditMessageText().setChatId(user1.getId()).setMessageId(message1.getMessageId()).setText("Поле").setReplyMarkup(warship1.getField(warship1.field_my)));
                //sendMessage(new Arguments.SendMessage().setChatId(user1.getId()).setText("Лови поле").setReplyMarkup(warship1.getField(warship1.field_my)));
                return;
            }
        }
        Message message = update.getMessage();
        if( message ==null|| message.getText().isEmpty()) return;

        switch (message.getText()){
            case "/help":
                break;
            case "/start":
                break;
            case "/game":
                if(!ind_registr1){
                    ind_registr1 = true;
                    user1 = message.getFrom();
                    gamemode = true;
                    sendMessageToUser("Расставляй кораблики)", user1);
                    message1= sendMessage(new Arguments.SendMessage().setChatId(user1.getId()).setText("Лови поле").setReplyMarkup(warship1.getField(warship1.field_my)));

                }
                break;


        }






    }
    private void sendMessageToUser(String text, User user){
        sendMessage(new Arguments.SendMessage()
                .setChatId(user.getId())
                .setText(text)
        );
    }
}
