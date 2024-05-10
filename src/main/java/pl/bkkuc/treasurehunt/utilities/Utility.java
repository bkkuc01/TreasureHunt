package pl.bkkuc.treasurehunt.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class Utility {

    public static int randomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }
}
