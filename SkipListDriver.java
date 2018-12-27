package sxg175130;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        SkipList<Long> skipList = new SkipList<>();
        // Initialize the timer
        Timer timer = new Timer();
        int counter = 0;
        while (!((operation = sc.next()).equals("End"))) {
            counter++;
            switch (operation) {
                case "Add": {
                    operand = sc.nextLong();
                    if(skipList.add(operand)) {
                        result = (result + 1) % modValue;
//                        System.out.println(counter+"Add: "+"1");
                    }
//                    else
//                        System.out.println(counter+"Add: "+"0");

//                    System.out.println(counter+" " +result);
                    break;
                }
                case "Ceiling": {
                    operand = sc.nextLong();
                    returnValue = skipList.ceiling(operand);
                    if (returnValue != null) {
//                        System.out.println(counter+"Ceiling: "+returnValue);

                        result = (result + returnValue) % modValue;
                    }
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "First": {
                    returnValue = skipList.first();
                    if (returnValue != null) {
//                        System.out.println(counter+"First: "+returnValue);

                        result = (result + returnValue) % modValue;
                    }
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Get": {
                    int intOperand = sc.nextInt();
                    returnValue = skipList.get(intOperand);
                    if (returnValue != null) {
//                        System.out.println(counter+"Get: "+returnValue);

                        result = (result + returnValue) % modValue;
                    }
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Last": {
                    returnValue = skipList.last();
                    if (returnValue != null) {
//                        System.out.println(counter+"Last: "+returnValue);

                        result = (result + returnValue) % modValue;
                    }
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Floor": {
                    operand = sc.nextLong();
                    returnValue = skipList.floor(operand);
                    if (returnValue != null) {
//                        System.out.println(counter+"Floor: "+returnValue);

                        result = (result + returnValue) % modValue;
                    }
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Remove": {
                    operand = sc.nextLong();
                    if (skipList.remove(operand) != null) {
//                        System.out.println(counter+"Remove: "+"1");

                        result = (result + 1) % modValue;
                    }
//                    else
//                        System.out.println(counter+"Remove: "+"0");
//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Contains":{
                    operand = sc.nextLong();
                    if (skipList.contains(operand)) {
//                        System.out.println(counter+"Contains: "+"1");

                        result = (result + 1) % modValue;
                    }
//                    else
//                        System.out.println(counter+"Contains: "+"0");

//                    System.out.println(counter+" " +result);

                    break;
                }
                case "Rebuild":{
                    try {
                        skipList.rebuild();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "log":{
                    int intOperand = sc.nextInt();
                    skipList.getLog(intOperand);
                }
                case "PL":{
//                    skipList.printList();
                    break;
                }

//                case "PS":{
//                    skipList.printSpan();
//                    break;
//                }

            }
        }

        // End Time
        timer.end();

        System.out.println();
        System.out.println(result);
        System.out.println(timer);
    }
}

