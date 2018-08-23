import ind.meffodiiy.telegram.bot.api.*;

import java.util.Arrays;


public class Warship {
    public int [][] field_my = new int[10][10]; //Матрица моего поля, для расстановки кораблей
    public int [][] field_enemy = new int[10][10]; //Матрица вражеского поля, по которому я пуляю
    public int [] ships = { 4, 3, 2, 1, 0};        // массив счетчиков на каждый тип корабля. 0- 1палуб, 1 -2палуб...
    //Пятый ноль, чтобы не было ексепшена при попытке построить 5-ти палубник


    /*Warship(){

    }*/

    // Этот монстрик отвечает за отрисовку поля
    public InlineKeyboardMarkup getField (int [][] field){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton[][]buttons = new InlineKeyboardButton[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                buttons[i][j] = new InlineKeyboardButton();
                buttons[i][j].setCallbackData(i+ ""+ j);
                if (field[i + 1][j + 1] == 0){
                    buttons[i][j].setText(" ");
                }
                if (field[i+1][j+1] == 1){
                    buttons[i][j].setText("•");
                }
                if (field[i+1][j+1] == 2){
                    buttons[i][j].setText("⬜️");
                }
                if (field[i+1][j+1] == 3){
                    buttons[i][j].setText("☒");
                }
            }
        }
        markup.setInlineKeyboard(buttons);
        return markup;
    }
    // Эта выхухоль будет отрабатывать выстрел, проверять косой ты или все же снайпер...
    public void updateField (CallbackQuery update_button, int [][] opponent_field){
        char [] position= update_button.getData().toCharArray();
        int [] xY = Arrays.stream(update_button.getData().split("")).mapToInt(Integer::parseInt).toArray();
        int x = xY[0] + 1;
        int y = xY[1] + 1;
        if (opponent_field[x][y] == 0){
            field_enemy[x][y] = 1;
        }
        if (opponent_field[x][y] == 2){
            field_enemy[x][y] = 3;
        }

    }

    // А вот это та самая Годзилла, которая расставляет кораблики и с которой постоянные траблы...
    public int setShips (CallbackQuery update_button) {
        int counter_sections = 0;
        int counter_ships_around = 0;
        int i = 0;
        int j = 0;
        int[] xY = Arrays.stream(update_button.getData().split("")).mapToInt(Integer::parseInt).toArray();
        boolean ind_ship = false;
        int x = xY[0] + 1;          //Присваиваю переменным х у координаты кнопки.
        // +1, бо поле 10х10, сделал отступ в одну клетку, чтобы не долбаться с проверкой на края
        int y = xY[1] + 1;
        if (field_my[x][y] == 0) {            //если клетка пуста
            for (int g = -1; g < 2; g++) {
                for (int z = -1; z < 2; z++) {   //Обхожу клетку по кругу с радиусом в 1 клетку
                    if (g != 0 || z != 0) {          // Избегаю проверки самой клетки
                        if (field_my[x + g][y + z] == 2) {  // Если где-то в радиусе есть кораблик
                            counter_ships_around ++;
                            i = g;
                            j = z;
                        }
                    }
                }
            }
            if (counter_ships_around == 0) {

                if (ships[0] == 0) {
                    return 3;
                } else {                                  //Ставлю однопалубник, если не нашел рядом с нажатой клеткой кораблей
                    field_my[x][y] = 2;
                    ships[0]--;
                    return 1;
                }

            }
            if(counter_ships_around == 1){
                if(i == 0 ||j == 0){       //Проверяю, находится ли корабль на углу или сбоку/сверху(false - на углу)
                    if(i == 0){
                        if(j > 0) {  //Проверяю по х или у идти дальше, чтобі посмотреть сколько уже палуб у корабля
                            for (int k = y + j; field_my[x][k] != 0; k++) {
                                counter_sections++;
                            }
                        }
                        else{
                            for (int k = y + j; field_my[x][k] != 0; k--) {
                                counter_sections++;
                            }
                        }
                    }                                               //Считаю палубы корабля
                    else{
                        if(i > 0) {
                            for (int k = x + i; field_my[k][y] != 0; k++) {
                                counter_sections++;
                            }
                        }
                        else{
                            for (int k = x + i; field_my[k][y] != 0; k--) {
                                counter_sections++;
                            }
                        }
                    }
                    if(counter_sections == 4 ||ships[counter_sections] == 0){ // Если 4х палубник или счетчик нужного корабля = 0
                        return 3;
                    }
                    else{
//                                    if (counter_sections > 1) {
                        ships[counter_sections ]--;   //отнимаю от счетчика корабля, который получается 1
                        ships[counter_sections - 1]++;  // добавляю 1 к счетчику корабля, который на "ранг" ниже того, что ставлю
                        //Бо если я сделал из 3-палубного 4-палубный, то у меня пропадет 3-палубный и появится 4-палубный
                        //Тобишь освободилась "вакансия на 3-палубный кораблик)
                        field_my[x][y] = 2;             //Обозначаю, что поставил корабль на матрице
                        return 1;
                        //                             }
//                                    else {
//                                        ships[counter_sections ]--;
//                                        field_my[x][y] = 2;
//                                        return 1;
//                                    }

                    }

                }
                else return 2;
            }
            else{
                return 2;
            }
        }

        else{
            return 4;  //Если клетка занята
        }

    }

}
