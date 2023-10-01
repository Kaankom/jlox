import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",     TokenType.IF);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("or",     TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this",   TokenType.THIS);
        keywords.put("true",   TokenType.TRUE);
        keywords.put("var",    TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }



    Scanner(String source) {
        this.source = source;
    }

    /**
     * This method goes through the source code
     * and adds Tokens until it's out of character
     * @return List of Tokens
     */
    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }

        // Add last Token and return
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            // Single Character Tokens
            case '(': addToken(TokenType.LEFT_PARENTHESES); break;
            case ')': addToken(TokenType.RIGHT_PARENTHESES); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            // One or Two Character Tokens
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if(match('/')) {
                    // a single line comment
                    while(peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            // Special Characters to ignore
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            // String Literals
            case '"': handleString(); break;

            default:
                if(isDigit(c)) {
                    handleNumber();
                } else if (isAlpha(c)) {
                    handleIdentifier();
                } else {
                    Lox.error(line, "Unexpected Character: '" + c +"'.");
                }
                break;
        }
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expectedChar) {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expectedChar) return false;

        current++;
        return true;
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if(current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void handleString() {
        while(peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') line++;
            advance();
        }
        if(isAtEnd()) {
            Lox.error(line, "Unterminated String");
            return;
        }

        advance(); // to the closing "

        // Trim the surrounding quotes
        String value = source.substring(start + 1, current -1);
        addToken(TokenType.STRING, value);
    }

    private void handleNumber() {
        while(isDigit(peek())) {
            advance();
        }

        // Look for a fractional point
        if(peek() == '.' && isDigit(peekNext())) {
            // Consume the fractional point
            advance();

            while(isDigit(peek())) advance();
        }
        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void handleIdentifier() {
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
