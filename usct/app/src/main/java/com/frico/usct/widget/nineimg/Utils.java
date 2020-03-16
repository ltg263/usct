package com.frico.usct.widget.nineimg;

/**
 * @Create On 2018/4/2 19:40
 */
public class Utils {
    public static String getNameByPosition(int itemPosition, int i) {
        String name = itemPosition + "_0";
        switch (i) {
            case 0:
                name = itemPosition + "_1";
                break;
            case 1:
                name = itemPosition + "_2";
                break;
            case 2:
                name = itemPosition + "_3";
                break;
            case 3:
                name = itemPosition + "_4";
                break;
            case 4:
                name = itemPosition + "_5";
                break;
            case 5:
                name = itemPosition + "_6";
                break;
            case 6:
                name = itemPosition + "_7";
                break;
            case 7:
                name = itemPosition + "_8";
                break;
            case 8:
                name = itemPosition + "_9";
                break;
        }
        return name;
    }
}
