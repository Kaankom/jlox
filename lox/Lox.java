import javax.xml.transform.Source;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    // sysexits.h UNIX exit codes
    static final int EX_USAGE = 64;
    static final int EX_DATAERR = 65;
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(EX_USAGE);
        }
        if(args.length == 1) {
            runFile(args[0]);
        }
        runPrompt(); // REPL
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));


        if(hadError) System.exit(EX_DATAERR);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader userInput = new InputStreamReader(System.in);
        BufferedReader buffReader = new BufferedReader(userInput);

        while(true) {
            System.out.println("> ");
            String loxLine = buffReader.readLine();
            if(loxLine == null) break;
            run(loxLine);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }
}

