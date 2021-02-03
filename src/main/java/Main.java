import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Input the length of the secret code:");
        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);

        // secretLen
        if (!scanner.hasNextInt()) {
            System.out.printf("Error: \"%s\" isn't a valid number.", scanner.next());
            System.exit(0);
        }

        int secretLen = scanner.nextInt();
        if (secretLen > 36 || secretLen == 0) {
            System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.\n", secretLen);
            System.exit(0);
        }


        // symbolsVariations
        System.out.println("Input the number of possible symbols in the code:");
        System.out.print("> ");

        if (!scanner.hasNextInt()) {
            System.out.printf("Error: \"%s\" isn't a valid number.", scanner.next());
            System.exit(0);
        }

        int symbolsVariations = scanner.nextInt();
        if (symbolsVariations < secretLen) {
            System.out.printf("Error: it's not possible to generate " +
                    "a code with a length of %d with %d unique symbols.\n", secretLen, symbolsVariations);
            System.exit(0);
        }

        if (symbolsVariations > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }

        String secret = generateSecret(secretLen, symbolsVariations);

        System.out.println("Okay, let's start a game!");
        System.out.println("secret: " + secret);

        int turn = 0;
        int bulls;
        do {
            System.out.println("Turn: " + ++turn);
            System.out.print("> ");
            String suggestion = scanner.next();
            bulls = grader(secret, suggestion);
        } while (bulls < secret.length());


        System.out.println("Congratulations! You guessed the secret code.");
    }


    private static String generateSecret(int secretLen, int symbolsVariations) {
        ArrayList<String> symbols = getSymbols(symbolsVariations);

        Random random = new Random(1000);
        StringBuilder secret = new StringBuilder();
        do {
            String cand = symbols.get(random.nextInt(symbolsVariations));
            if (!secret.toString().contains(cand)) {
                secret.append(cand);
                if (secret.length() == secretLen) {
                    break;
                }
            }
        } while (secret.length() < secretLen);


        System.out.printf(
                "The secret is prepared: %s (%s%s).%n",
                "*".repeat(secretLen),
                String.format("0-%d", Math.min(symbols.size() - 1, 9)),
                symbols.size() > 10
                        ? String.format(", %s-%s", symbols.get(10), symbols.get(symbols.size() - 1))
                        : ""
        );

        return secret.toString();
    }


    private static ArrayList<String> getSymbols(int symbolsVariations) {
        ArrayList<String> symbols = new ArrayList<>();
        for (int i = 0; i <= Math.min(symbolsVariations, 9); i++) {
            symbols.add(Integer.toString(i));
        }

        int currentSize = symbols.size();
        for (int i = 0; i < symbolsVariations - currentSize; i++) {
            char ch = (char) ('a' + i);
            symbols.add(Character.toString(ch));
        }

        return symbols;
    }


    private static int grader(String secret, String suggestion) {
        int cows = 0;
        int bulls = 0; // if its position is also correct, then it is a bull
        int index = 0;
        for (String s : suggestion.split("")) {
            if (secret.contains(s)) {
                if (secret.indexOf(s) == index) {
                    bulls++;
                } else {
                    cows++;
                }
            }
            index++;
        }

        // Grade: 1 bull(s) and 1 cow(s). The secret code is 9305.
        // Grade: 1 cow(s). The secret code is 9305.
        // Grade: None. The secret code is 9305.

        String grade = "None";
        if (cows > 0 && bulls > 0) {
            grade = String.format("%d bull(s) and %d cow(s).", bulls, cows);
        } else if (cows > 0) {
            grade = String.format("%d cow(s).", cows);
        } else if (bulls > 0) {
            grade = String.format("%d bull(s).", bulls);
        }

        System.out.printf("Grade: %s", grade);
        System.out.println();

        return bulls;
    }
}
