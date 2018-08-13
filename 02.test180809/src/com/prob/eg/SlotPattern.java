package com.prob.eg;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlotPattern {
    //패턴
    //1등 : 777
    //2등 : 272, 727
    //3등 : 111,222,333,444,555,666
    //4등 : 444를 제외하고, 4가 연속으로 2회 붙어있는 모든 조합(44*, *44);

    final static int sizeOfAllPatterns = 343;
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
        createThird();
        createFourth();
        createFifth();

    }

    public List<Integer> getResultSlotWithGrade(Game.Grade grade){

        switch (grade){
            case grade1:
                System.out.print("1등! ");
                return patternForFirst;

            case grade2:
                System.out.print("2등! ");
                resultSlot = getRandomSlot(patternsForSecond);
                break;

            case grade3:
                System.out.print("3등! ");
                resultSlot = getRandomSlot(patternsForThird);
                break;

            case grade4:
                System.out.print("4등! ");
                resultSlot = getRandomSlot(patternsForFourth);
                break;

            default:
                System.out.print("5등! ");
                resultSlot = getRandomSlot(patternsForFifth);
        }

        return resultSlot;
    }

    List<Integer> getRandomSlot(Set<List<Integer>> patterns){
        Object[] tempArray  =patterns.toArray();
        int idx = new Random().nextInt(patterns.size()); // 0~size-1 까지의 숫자 임의 생성
        return (List<Integer>) tempArray[idx];
    }


    void createThird(){
        // 777을 제외한 세자리가 연속된 슬롯
        ArrayList pattern = IntStream.rangeClosed(1, 7).filter(n -> n!=7).mapToObj(n -> Arrays.asList(n,n,n)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        patternsForThird.addAll(pattern);
    }


    void createFourth(){
        // 4가 두번 연속으로 나오는 슬롯 중에 444 제외
        ArrayList pattern1 = IntStream.rangeClosed(1, 7).filter(n -> n!=4).mapToObj(n -> Arrays.asList(4,4,n)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        ArrayList pattern2 = IntStream.rangeClosed(1, 7).filter(n -> n!=4).mapToObj(n -> Arrays.asList(n,4,4)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        patternsForFourth.addAll(pattern1);
        patternsForFourth.addAll(pattern2);
    }


    void createFifth(){
        patternsForFifth = (Set<List<Integer>>)((HashSet<List<Integer>>) allPatterns).clone();
        patternsForFifth.remove(Arrays.asList(patternForFirst));
        patternsForFifth.removeAll(patternsForSecond);
        patternsForFifth.removeAll(patternsForThird);
        patternsForFifth.removeAll(patternsForFourth);
    }


    static Set<List<Integer>> createAllPatterns(){
        List<Integer> list = new Random().ints(1,8).limit(3).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        allPatterns.add(list);
        return allPatterns.size() == sizeOfAllPatterns ? allPatterns : createAllPatterns();
    }



}
