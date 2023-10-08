package lox;

// Prints the AST in the Reverse Polish Notation
class RPNPrinter implements Expr.Visitor<String>{
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    private String parenthesize(String operator, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        for(Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(" ").append(operator);

        return builder.toString();
    }
}
