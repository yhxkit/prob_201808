package com.prob.eg;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlotPattern {
    //패턴
    //1등 : 777
    //2등 : 272, 727
    //3등 : 111,222,333,444,555,666
    //4등 : 444를 제외하고, 4가 연속으로 2회 붙어있는 모든 조합(44*, *44);

    //패턴 구현....구현...ㅜㅜ..... 패턴 다 나눠놓고 인덱스 값을 랜덤으로 해서 반환해줄까... 근데 그 나누는게 너무 힘들어..
    //그렇다고 매번 랜덤한 값 꺼내서 자기 제외 모든 등수에 부합하지 않을 경우에만 반환하기에도 조건 걸기가 너무 복잡해지고

    //1. 랜덤 슬롯을 꺼낸다.
    //2. 그레이드에 따라 랜덤 슬롯이 그 그레이드에 맞는지 체크한다.
    //3. 안맞으면 반복한다.
    //4. 맞으면 내보낸다.

    final static int sizeOfAllPatterns = 343;
    final static int sizeOfPatternFourth = 12;

    static Set<List<Integer>> allPatterns = new HashSet<>();
    static List<Integer> patternForFirst = Arrays.asList(7,7,7);
    static Set<List<Integer>> patternsForSecond = new HashSet<>(Arrays.asList(Arrays.asList(2,7,2),Arrays.asList(7,2,7)));
    static Set<List<Integer>> patternsForThird = new HashSet<>();
    static Set<List<Integer>> patternsForFourth = new HashSet<>();
    static Set<List<Integer>> patternsForFifth = new HashSet<>();

    static List<Integer> resultSlot;

    Game game;

    public SlotPattern(Game game){
        this.game = game;
        createAllPatterns();
    }

    public List<Integer> getResultSlotWithGrade(Game.Grade grade){

        resultSlot = getRandomSlot();

        switch (grade){
            case grade1:
                break;
            case grade2:
                break;
            case grade3:
                break;
            case grade4:
                break;
            default:
        }
        return resultSlot;
    }

    List<Integer> getRandomSlot(){
        return new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }




    void getFifth(){
        patternsForFifth = (Set<List<Integer>>)((HashSet<List<Integer>>) allPatterns).clone();
        patternsForFifth.removeAll(patternForFirst);
        patternsForFifth.removeAll(patternsForSecond);
        patternsForFifth.removeAll(patternsForThird);
        patternsForFifth.removeAll(patternsForFourth);

        //resultSlot = new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        //return resultSlot.get(0)==resultSlot.get(1) && resultSlot.get(1)==resultSlot.get(2) && resultSlot != patternForFirst ? resultSlot : createThird();

    }


//    boolean createThird(){ //create 아니고 그냥 get 이잖아...
//        resultSlot = getRandomSlot();
//       return resultSlot.get(0)==resultSlot.get(1) && resultSlot.get(1)==resultSlot.get(2) && resultSlot != patternForFirst ? patternsForThird.add(resultSlot) : createThird();
//    }

    void createThird(){
     //  List<Integer> thirds = new ArrayList<>();
//        IntStream.rangeClosed(1,7).flatMap().forEach();
    //    patternsForThird.parallelStream().flatMap(IntStream.rangeClosed(1,7).forEach((Integer n) -> {patternsForThird.add(n);}));
    }


    void createFourth(){

    }



    static Set<List<Integer>> createAllPatterns(){
        List<Integer> list = new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        allPatterns.add(list);
        return allPatterns.size() == sizeOfAllPatterns ? allPatterns : createAllPatterns();
    }



}
