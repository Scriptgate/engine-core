package net.scriptgate.engine.util;

public class ArrayUtil {

    public static void flipVertically(int[] input, int lineWidth, int numberOfLines)
    {
        int[] tempLine = new int[lineWidth];
        int half = numberOfLines / 2;

        for (int i = 0; i < half; ++i)
        {
            //copy current line to temp
            System.arraycopy(input, i * lineWidth, tempLine, 0, lineWidth);
            //copy bottom line to top
            System.arraycopy(input, (numberOfLines - 1 - i) * lineWidth, input, i * lineWidth, lineWidth);
            //copy temp to bottom
            System.arraycopy(tempLine, 0, input, (numberOfLines - 1 - i) * lineWidth, lineWidth);
        }
    }

}
