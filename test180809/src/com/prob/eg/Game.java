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

        this.start();
    }

    void start(){

        //재귀로 시드머니가 떨어지면 탈출하는 조건의 함수 새로 짜기... 메서드 정의도 새로 해야하지 않을까?
        Stream.iterate(0, o -> Integer.parseInt(input("판돈 입력~(0원 이상 보유액 미만 / 현재 "+player.money+" 시드머니 보유)" )))
                .filter(betting -> betting>=0 && betting<= player.money)
                .map(betting -> new int[]{betting, player.loseMoney(betting)})
                .map( moneys -> {
                    Grade grade = probability();

                    if(grade==Grade.grade1){
                        player.winMoney(moneys[0]*4);
                    }else if(grade==Grade.grade2){
                        player.winMoney(moneys[0]*3);
                    }else if(grade==Grade.grade3){
                        player.winMoney(moneys[0]*2);
                    }else if(grade==Grade.grade4){
                        player.winMoney(moneys[0]);
                    }
                    System.out.println("슬롯 번호 "+this.pattern.getResultSlotWithGrade(grade));
                    moneys[1]=player.money;
                    //return moneys[1]+" 출력값~ 보유액";
                    return "";

                }).forEach(System.out::println);

        System.out.println("Game over");
        System.out.println("   총 "+player.win+"번 획득 "+player.lose+"번 잃었습니다.");
    } //start end


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
