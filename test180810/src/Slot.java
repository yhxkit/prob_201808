import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.System.exit;
import static java.lang.System.setOut;
//루프 스트림으로 변환하기~ equals~~

class Player{
    int money;
    int win=0, lose=0;
    Game game;

    public  Player(){
        money = 1000; // 처음 플레이어를 생성 시 시드머니 1000원 주고 시작
        game = new Game();
    }

    void getMoney(int amount){
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

class Game{
    final static List<Integer> symbols = Arrays.asList(1,2,3,4,5,6,7);
    List<Integer> fifth;

    final static List<Integer> first = Arrays.asList(7,7,7);
    final static List<Integer> second = Arrays.asList(7,7,2);
    final static List<Integer> third = Arrays.asList(7,7,3);
    final static List<List<Integer>> fourth = Arrays.asList(Arrays.asList(7,4,7), Arrays.asList(4,7,4));

    List<Integer> showSlotNumbers(int grade){
        List<Integer> result;

        switch (grade){
            case 1: result = first;
                break;
            case 2:result = second;
                break;
            case 3:result = third;
                break;
            case 4:result = fourth.get((int)(Math.random()*2));
                break;
            default:result = makeFifth();
        }
        return result;
    }


    List<Integer> makeFifth(){
        fifth = new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        boolean tf = new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll).equals(fourth);
        if(tf) {System.out.println("여기인지?"+tf+"5등인데 4등");}

        if(equals(fifth)){
            fifth = Arrays.asList(0,0,0);// 일단 없는 걸로 해뒀는데 아무 5등 코드..
        }
        return fifth;
    }


    boolean equals(List<Integer> fifth){
        if(fifth.equals(first)|| fifth.equals(second) || fifth.equals(third) || fifth.equals(fourth.get(0)) || fifth.equals(fourth.get(1))){
            return true;
        }
        return false;
    }

}


public class Slot {
//    슬롯머신을 만들어보자
//
//
//1. 슬롯머신에는 시드머니가 1000원이 있다. 가지고 있는 시드 머니 내에서 판돈을 걸 수 있는데, 계속 걸고 플레이 하면서 시드머니가 다 떨어지면 게임이 끝난다. 돈이 다 떨어지면 다시 시작하겠냐고 물어보고, 원하면 게임을 다시 시작할 수 있다.
//2. 슬롯머신의 심볼은 총 7가지이고, 패턴에는 5가지가 있다. 1등, 2등, 3등은 각각 1개씩고 4등은 2개 패턴을 가진다. 1등부터 시작해서 상금으로 판돈의 4배, 3배, 2배를 받을 수 있고 4등은 패턴이 2개인데 원금을 돌려받을 수 있다.
//4. 심볼: 1,2,3,4,5,6,7
//5. 패턴은 마음대로 하여 총 1,2,3,4등을 만든다.
//6. 1등 확률은 1% 2등 확률은 2% 3등 확률은 5% 4등 확률은 10%

    public static void main(String[] args) {
        Player one = new Player();
        int once=0;

//        while (one.money>=once) {
//
//            System.out.println("(1회 "+once+" 시드머니 / 현재 보유액 :"+one.money+")");
//            once = Integer.parseInt(input("판돈을 입력하세요"));
//            if(once>one.money || once<0){
//                System.out.println("판돈은 0원 이상, 보유액 이하의 금액만 걸 수 있습니다");
//                once =0;
//                continue;
//            }
//
//            one.loseMoney(once);
//
//            switch (probability()){
//                case 1:
//                    System.out.println(one.game.showSlotNumbers(1));
//                    one.getMoney(once*4);
//                    break;
//                case 2:
//                    System.out.println(one.game.showSlotNumbers(2));
//                    one.getMoney(once*3);
//                    break;
//                case 3:
//                    System.out.println(one.game.showSlotNumbers(3));
//                    one.getMoney(once*2);
//                    break;
//                case 4:
//                    System.out.println(one.game.showSlotNumbers(4));
//                    one.getMoney(once);
//                    break;
//                default:
//                    System.out.println(one.game.showSlotNumbers(5));
//
//            }
//        }





//        Stream.iterate(0, n -> one.money)
//                .limit(10)
//                .forEach( money -> );





/*        Stream.generate(() -> input("판돈을 얼마 거시겠습니까?"))
                .map(o -> Integer.parseInt(o))
                .filter(m -> m > 0)
                .forEach(s -> {
                    System.out.println(s);
                    if (s <= 0) {
                        exit(0);
                    }
                });*/


        Stream.iterate(0, o -> Integer.parseInt(input("판돈 입력~(0원 이상 보유액 미만 / 현재 "+one.money+" 시드머니 보유)" )))
                .filter(on -> on>0 && on<= one.money)
                .map(on -> { return new int[]{on, one.loseMoney(on)};})
                .map( moneys -> {
                    int pro = probability();
                    if(pro == 1){
                        one.getMoney(moneys[0]*4);
                        moneys[1]=one.money;
                        System.out.println(one.game.showSlotNumbers(1));
                    }else if(pro == 2){
                        one.getMoney(moneys[0]*3);
                        moneys[1]=one.money;
                        System.out.println(one.game.showSlotNumbers(2));
                    }else if(pro == 3){
                        one.getMoney(moneys[0]*2);
                        moneys[1]=one.money;
                        System.out.println(one.game.showSlotNumbers(3));
                    }else if(pro == 4){
                        one.getMoney(moneys[0]);
                        moneys[1]=one.money;
                        System.out.println(one.game.showSlotNumbers(4));
                    }else{
                        System.out.println(one.game.showSlotNumbers(5));
                    }

                    return moneys[1]+" 출력값~ 보유액";

                })
                .forEach(System.out::println);



        System.out.println("Game over");
        System.out.println("   총 "+one.win+"번 획득 "+one.lose+"번 잃었습니다.");

    }



    public static int getRandom(int num){
        int result;
        result = (int) (Math.random() * num + 1);
        return result;
    }

    static int probability () {

        int prob = getRandom(100);
        int grade;

        if (prob == 1) {
            grade = 1;
        } else if (prob >= 2 && prob <= 3) {
            grade = 2;
        } else if (prob >= 4 && prob <= 8) {
            grade = 3;
        } else if (prob >= 8 && prob <= 17) {
            grade = 4;
        } else {
            grade = 5;
        }

        return grade;
    }


    static String input (String msg) {

        System.out.println(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

}