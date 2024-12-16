package util;

public class MathUtil {

    public static int clamp(int value, int minInclusive, int maxInclusive)
    {
        if(value < minInclusive)
            value = minInclusive;
        else if(value > maxInclusive)
            value = maxInclusive;

        return value;
    }

    public static float clamp(float value, float minInclusive, float maxInclusive)
    {
        if(value < minInclusive)
            value = minInclusive;
        else if(value > maxInclusive)
            value = maxInclusive;

        return value;
    }

}
