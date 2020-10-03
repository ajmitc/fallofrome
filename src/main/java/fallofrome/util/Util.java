package fallofrome.util;

import java.util.Date;
import java.util.Random;

public class Util {
    private static final Random GEN = new Random(new Date().getTime());

    public static int roll(int numDice){
        int total = 0;
        for (int i = 0; i < numDice; ++i)
            total += (1 + GEN.nextInt(6));
        return total;
    }

    public static int roll(){
        return roll(1);
    }

    private Util(){}
}
