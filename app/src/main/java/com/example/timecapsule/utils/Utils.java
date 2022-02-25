package com.example.timecapsule.utils;

import java.util.Random;

public class Utils {

    public static int getNum(int endNum){
        if(endNum > 0){
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }
}
