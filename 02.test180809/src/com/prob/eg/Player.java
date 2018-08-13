package com.prob.eg;

public class Player {
    int money;
    int win=0, lose=0;
    Game game;

    public  Player(){
        money = 1000; // 처음 플레이어를 생성 시 시드머니 1000원 주고 시작
        game = new Game(this);
    }


    void winMoney(int amount){
        money += amount;
        win+=1;
        System.out.println(amount+"원 획득");
    }

    int loseMoney(int amount){
        if(amount<=money){
            money -= amount;
            lose+=1;
        }
        return money;
    }



}
