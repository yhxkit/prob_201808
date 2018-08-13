package com.prob.eg;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Game {
    Player player;
    SlotPattern pattern;
    final static int percentage = 100;

    public Game(Player player){
        this.player = player;
        pattern = new SlotPattern(this);
        this.start(player.money);
        System.out.println("   총 "+player.lose+"번 베팅하여 "+player.win+"번 획득 "+player.money);

    }


    int bettingCheck(int money) {
        int betting = Integer.parseInt(input("판돈 입력~(판돈:0원 이상 보유액 이하 )[현재 "+money+" 시드머니 보유]"));
        return  money >= betting ? betting : bettingCheck(money);
    }

    void start(int money){
        int betting = bettingCheck(player.money);
        player.loseMoney(betting);

        Grade grade = probability();

        if(grade==Grade.grade1){
            player.winMoney(betting*4);
        }else if(grade==Grade.grade2){
            player.winMoney(betting*3);
        }else if(grade==Grade.grade3){
            player.winMoney(betting*2);
        }else if(grade==Grade.grade4){
            player.winMoney(betting);
        }


        System.out.println("슬롯 번호 "+this.pattern.getResultSlotWithGrade(grade));

        if(player.money>0) {start(player.money);}else{stop();}
    } //start end

    void stop() {
        System.out.println("Game over");

    }


    enum Grade { grade1, grade2, grade3, grade4, grade5 }

    Grade probability () {
        int prob = getRandom(percentage);
        Grade grade;

        if (prob == 1) {
            grade = Grade.grade1;
        } else if (prob >= 2 && prob <= 3) {
            grade = Grade.grade2;
        } else if (prob >= 4 && prob <= 8) {
            grade = Grade.grade3;
        } else if (prob >= 8 && prob <= 17) {
            grade = Grade.grade4;
        } else {
            grade = Grade.grade5;
        }
        return grade;
    } // probability end

    int getRandom(int num){
        int result;
        result = (int) (Math.random() * num + 1);
        return result;
    } //getRandom end

    String input (String msg) {
        System.out.println(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    } //input end





}
