package lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    /**
     * We don't check if the Key is already in the Map.
     * This is on purpose.
     * This way we can redefine global variables, since it  allows
     * for better UserExperience in the REPL
     */
    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if(values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        // Walk up the chain recursive
        if(enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if(values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        // Walk up the chain recursive
        if(enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
