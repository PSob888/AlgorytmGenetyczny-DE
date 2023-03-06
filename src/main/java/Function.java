import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function {
    private String fun;
    private double min;
    private double max;
    public Function(String fun, double min, double max) {
        this.fun = fun;
        this.min = min;
        this.max = max;
    }

    public String getFun() {
        return fun;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double eval(double x)
    {
        Expression e = new ExpressionBuilder(fun).variables("x").build().setVariable("x",x);
        return e.evaluate();
    }

    public double eval(double x,double y)
    {
        Expression e = new ExpressionBuilder(fun).variables("x","y").build().setVariable("x",x).setVariable("y",y);
        return e.evaluate();
    }
}
