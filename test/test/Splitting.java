package test;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Splitting {
    public static void main(String[] args) {
        String toSplit = "ABCDEFG|HIJKLMNOP|QRSTUV|WX|YZ";
        String[] data = toSplit.split(Pattern.quote("|"));
        System.out.println(Arrays.toString(data));
    }
}