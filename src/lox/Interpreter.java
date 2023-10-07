package lox;

class Interpreter implements Expr.Visitor<Object> {

    void interpret(Expr expression) {
        try{
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                checkZeroDivision(expr.operator, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case PLUS:
                if(left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if(left instanceof String) {
                    return left + stringify(right);
                }
                // TODO: there is still a NullPointerException when the right operand is "nil"
                if(left == null) {
                    throw new RuntimeError(expr.operator, "Operand can't be run on nil values");
                }
                throw new RuntimeError(expr.operator, "Operand must be two numbers or two strings.");
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
        }
        return null;
    }

    /**
     * Recursively evaluates the subexpression and returns it
     */
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    /**
     * The literal Value is already produced while parsing
     * So we can just return the value from the AST Node
     */
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
        }
        return null;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if(object == null) return false;
        if(object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        // to avoid NullPointerExceptions check for null values
        if(a == null && b == null) return true;
        if(a == null) return false;
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if(operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if(left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Both Operands must be a number.");
    }

    private void checkZeroDivision(Token operator, Object right) {
        if((double)right == 0) {
            throw new RuntimeError(operator, "You can't divide by 0");
        }
    }

    private String stringify(Object object) {
        if(object == null) return "nil";

        if (object instanceof Double) {
            String numberString = object.toString();
            // all lox numbers are java doubles
            if(numberString.endsWith(".0")) {
                // represent java floating point numbers as integers
                numberString = numberString.substring(0, numberString.length() - 2);
            }
            return numberString;
        }
        return object.toString();
    }
}
