/*
Ariful Islam
CISC 4331
Spring 2020
Final Project

*/

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

public class Interpreter  {

    private String s;
    private int currIndex;
    private char inputToken;
    
    // Create a HashMap object called sp
    private HashMap<String, Integer> sp = new HashMap<String, Integer>(); 
        void expEvaluator(String s) {
        this.s = s.replaceAll("\\s", "");
        currIndex = 0;
        nextToken();
    }

      /*
      void nextToken() {
        char c;
        if (!s.endsWith(";"))
        throw new RuntimeException();
               */

    void nextToken() { // Pushes the specified token back into the scanner's
        char c;
        if (!s.endsWith(";"))
        throw new RuntimeException("Missing ';' exptected token");  // through the expectation
        c = s.charAt(currIndex++);
        inputToken = c;
    }
       // find the missing parenthesis 
    void match(char token) {
        if (inputToken == token) {
            nextToken();
        } else {
            throw new RuntimeException("Missing Parenthesis"); 
        }
    }


     // missing expexted token
    int inprt() {
        int x = exp();
        if (inputToken == ';') {
            return x;
        } else {
            throw new RuntimeException("Missing ';' expected token ");
        }
    }


     // set up the identifier
    public void run(Scanner fs) {
        while (fs.hasNextLine()) {
            expEvaluator(fs.nextLine());
            assignment();
        }
    }

    void assignment() {
        String var = identifier();
        int operand = inprt();
        sp.put(var, operand);
        System.out.println(var + " = " + operand);

    }

    int exp() {
        int x = term();
        while (inputToken == '+' || inputToken == '-') {
            char op = inputToken;
            nextToken();
            int y = term();
            x = apply(op, x, y);
        }
        return x;
    }
   // loop for inputToken and nestToken
    int term() {
        int x = factor();
        while (inputToken == '*' || inputToken == '/') {
            char op = inputToken;
            nextToken();
            int y = factor();
            x = apply(op, x, y);
        }
        return x;
    }

    int factor() {
        int x = 0;
        String temp = String.valueOf(inputToken);

        if (sp.containsKey(temp)) {
            x = sp.get(temp).intValue();
            nextToken();
            return x;
        } else if (inputToken == '(') {
            nextToken();
            x = exp();
            match(')');
            return x;
        } else if (inputToken == '-') {
            nextToken();
            x = factor();
            return -x;
        } else if (inputToken == '+') {
            nextToken();
            x = factor();
            return x;
        } else if (inputToken == '0') {
            nextToken();
            if (Character.isDigit(inputToken))
                throw new RuntimeException("ERROR !! Invalid value");
            return 0;
        }
        temp = "";

        while (Character.isDigit(inputToken)) {
            temp += inputToken;
            nextToken();
        }

        return Integer.parseInt(temp);

    }

    String identifier() {
        StringBuilder sb = new StringBuilder();

        if (Character.isLetter(inputToken))
            sb.append(inputToken);
        else
            throw new RuntimeException("Invalid variable name");
        nextToken();

        while (Character.isLetter(inputToken) || inputToken == '_' || Character.isDigit(inputToken)) {
            sb.append(inputToken);
            nextToken();
        }
        if (inputToken != '=')
            throw new RuntimeException("Not an assignment statement");
        nextToken();
        return sb.toString();
    }

    static int apply(char op, int x, int y) {
        int z = 0;
        switch (op) {
            case '+':
                z = x + y;
                break;
            case '-':
                z = x - y;
                break;
            case '*':
                z = x * y;
                break;
            case '/':
                z = x / y;
                break;
        }
        return z;
    }

    public static void main(String[] args) {

        try {
            Scanner fileObj = new Scanner(new FileInputStream(args[0]));
            Interpreter  expEval = new Interpreter ();
            expEval.run(fileObj);

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
    }
}