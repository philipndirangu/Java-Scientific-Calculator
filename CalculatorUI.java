import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

//Class Declaration
public class CalculatorUI extends JFrame implements ActionListener {
    //Extending JFrame from Java Swing to allow for use of features i.e Window Creation
    protected DecimalFormat df = new DecimalFormat("#,###.00");
    protected String[] symbols = {
            "(", ")", "π", "e", "!", "c", "+/-", "%", "÷",
            "x²", "x³", "xʸ", "eˣ", "10ˣ", "7", "8", "9", "x",
            "1/x", "log₁₀", "²√x", "∛x", "ʸ√ₓ", "4", "5", "6", "-",
            "ln", "sin", "cos", "tan", "Rad", "3", "2", "1", "+",
            "x⁻¹", "sinh", "cosh", "tanh", ".", "0", "del", "MOD", "="
    };
    protected int operator = 0; //Type of operator selected, later used by performRegularOperation to det which operation to use
    protected boolean sinSelected = false;
    protected boolean cosSelected = false;
    protected boolean tanSelected = false;
    protected boolean sinhSelected = false;
    protected boolean coshSelected = false;
    protected boolean tanhSelected = false;
    protected double constantValue = 0;
    protected JPanel panel = new JPanel(new BorderLayout(0, 0));
    protected JPanel btnPanel = new JPanel(new GridLayout(5, 9, 2, 2));
    protected JButton[] btns = new JButton[symbols.length];
    protected JTextArea screen = new JTextArea(5, 9);
    protected double firstNum = 0, secondNum = 0;
    protected JTextField calculatingTf = new JTextField(9);
    public CalculatorUI() {
        init();
    } //Access  Modifier is Public. Can be accessed from other classes (instances can also be made from other classes)
    public void init() { //Method the constructor is calling on. Contains actual GUI
        setTitle("Calculator");
        screen.setFont(new Font("San Francisco Display Light", Font.PLAIN, 20));
        screen.setBackground(Color.BLACK);
        panel.setBackground(Color.BLACK);
        btnPanel.setBackground(Color.BLACK);
        screen.setForeground(Color.WHITE);

        screen.setFocusable(false); //Unable to Use the Keyboard

        for (int i = 0; i < symbols.length; i++) {
            btns[i] = new JButton(symbols[i]);
            btns[i].setOpaque(true);
            btns[i].setBorderPainted(false);
            btns[i].setForeground(Color.WHITE);
            btns[i].setFont(new Font("San Francisco Display Light", Font.PLAIN, 20));
            btns[i].addActionListener(this);
            btnPanel.add(btns[i]);

            if (symbols[i].matches("[0-8]")) {
                btns[i].setBackground(Color.GRAY);
            } else if (symbols[i].equals("9")) {
                btns[i].setBackground(Color.GRAY);
            } else if (symbols[i].equals(".")) {
                btns[i].setBackground(new Color(64, 64, 64)); // Decimal "." is (new Color(64, 64, 64))
            } else if (symbols[i].equals("del") || symbols[i].equals("MOD")) {
                btns[i].setBackground(Color.GRAY);
            } else if (symbols[i].matches("[÷x+-=]")) {
                btns[i].setBackground(new Color(240, 160, 0));
            } else {
                btns[i].setBackground(new Color(64, 64, 64));
            }
        }

        add(panel);
        panel.add(screen, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);
        panel.add(calculatingTf, BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); //Resizes
        setLocationRelativeTo(null); //Sets JFrame centered to the screen
        setVisible(true); // Makes frame Visible
    }

    public void actionPerformed(ActionEvent e) { //Method checks button pressed and Updates UI, Switch Statement.
        String cmd = e.getActionCommand().toString();
        try {
            switch (cmd) {
                case ".":
                    if (!screen.getText().contains(".")) {
                        screen.append(cmd);
                    }
                    break;
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    screen.append(cmd);
                    break;
                case "÷":
                case "x":
                case "-":
                case "+":
                case "%":
                case "+/-":
                case "c":
                    performRegularOperation(cmd);
                    break;
                case "sin":
                case "sinh":
                case "cos":
                case "cosh":
                case "tan":
                case "tanh":
                    selectTrigonometricFunction(cmd); //some functions delegated to other methods
                    break;
                case "=":
                    performCalculation();
                    break;
                case "π":
                    constantValue = Math.PI;
                    screen.setText(Double.toString(constantValue));
                    break;
                case "e":
                    constantValue = Math.E;
                    screen.setText(Double.toString(constantValue));
                    break;
                case "log₁₀":
                    performLogarithm();
                    break;
                case "x²":
                    double squareValue = Double.parseDouble(screen.getText());
                    double squareResult = Math.pow(squareValue, 2);
                    screen.setText(Double.toString(squareResult));
                    break;
                case "x³":
                    double cubeValue = Double.parseDouble(screen.getText());
                    double cubeResult = Math.pow(cubeValue, 3);
                    screen.setText(Double.toString(cubeResult));
                    break;
                case "xʸ":
                    firstNum = Double.parseDouble(screen.getText());
                    operator = 5;
                    screen.setText("");
                    break;
                case "10ˣ":
                    double powTenValue = Double.parseDouble(screen.getText());
                    double powTenResult = Math.pow(10, powTenValue);
                    screen.setText(Double.toString(powTenResult));
                    break;
                case "eˣ":
                    double expValue = Double.parseDouble(screen.getText());
                    double expResult = Math.exp(expValue);
                    screen.setText(Double.toString(expResult));
                    break;
                case "x⁻¹":
                    performXInverse();
                    break;
                case "1/x":
                    double reciprocalValue = Double.parseDouble(screen.getText());
                    double reciprocalResult = 1 / reciprocalValue;
                    screen.setText(Double.toString(reciprocalResult));
                    break;
                case "²√x":
                    double sqrtValue = Double.parseDouble(screen.getText());
                    double sqrtResult = Math.sqrt(sqrtValue);
                    screen.setText(Double.toString(sqrtResult));
                    break;
                case "∛x":
                    double cbrtValue = Double.parseDouble(screen.getText());
                    double cbrtResult = Math.cbrt(cbrtValue);
                    screen.setText(Double.toString(cbrtResult));
                    break;
                case "ʸ√ₓ":
                    double yRootValue = Double.parseDouble(screen.getText());
                    double yRootResult = Math.pow(firstNum, 1 / yRootValue);
                    screen.setText(Double.toString(yRootResult));
                    calculatingTf.setText(firstNum + "√" + yRootValue + " = " + yRootResult);
                    break;
                case "!":
                    performFactorial();
                    break;
                case "MOD":
                    performModuloOperation();
                    break;
                default:
            }
        }
        catch(Exception ex){
            screen.setText("Error");
        }
    }

    private void performRegularOperation(String cmd) { //first checks if screen is empty, if not, it captures the first number on screen, sets operator and clears the screen for the next number
        if (!screen.getText().isEmpty()) {
            switch (cmd) {
                case "÷":
                case "x":
                case "-":
                case "+":
                    firstNum = Double.parseDouble(screen.getText());
                    operator = switch (cmd) {
                        case "÷" -> 1;
                        case "x" -> 2;
                        case "-" -> 3;
                        case "+" -> 4;
                        default -> 0;
                    };
                    screen.setText("");
                    break;
                case "del":
                    String currentText = screen.getText();
                    if (!currentText.isEmpty()) {
                        screen.setText(currentText.substring(0, currentText.length() - 1));
                    }
                    break;
                case "+/-":
                    double value = Double.parseDouble(screen.getText());
                    value *= -1;
                    screen.setText(String.valueOf(value));
                    break;
                case "%":
                    double percentageValue = Double.parseDouble(screen.getText());
                    percentageValue /= 100;
                    screen.setText(String.valueOf(percentageValue));
                    break;
                case "c":
                    screen.setText("");
                    break;
            }
        }
    }

    private void selectTrigonometricFunction(String cmd) {
        switch (cmd) {
            case "sin":
                sinSelected = true;
                break;
            case "sinh":
                sinhSelected = true;
                break;
            case "cos":
                cosSelected = true;
                break;
            case "cosh":
                coshSelected = true;
                break;
            case "tan":
                tanSelected = true;
                break;
            case "tanh":
                tanhSelected = true;
                break;
            default:
        }
        screen.setText("");
        calculatingTf.setText(cmd);
    }

    private void performCalculation() {//
        if (!screen.getText().isEmpty()) {
            secondNum = Double.parseDouble(screen.getText());
            if (operator == 1 && secondNum == 0) {
                screen.setText("Error");
                calculatingTf.setText("Cannot divide by zero");
                return;  // Exit the method to prevent further calculations
            }
            if (sinSelected || cosSelected || tanSelected || sinhSelected || coshSelected || tanhSelected) {
                double num = Double.parseDouble(screen.getText());
                double result = 0;
                if (sinSelected) {
                    result = Math.sin(Math.toRadians(num));
                } else if (sinhSelected) {
                    result = Math.sinh(num);
                } else if (cosSelected) {
                    result = Math.cos(Math.toRadians(num));
                } else if (coshSelected) {
                    result = Math.cosh(num);
                } else if (tanSelected) {
                    result = Math.tan(Math.toRadians(num));
                } else if (tanhSelected) {
                    result = Math.tanh(num);
                }
                screen.setText(String.valueOf(result));
                calculatingTf.setText(calculatingTf.getText() + "(" + num + ") = " + result);

                sinSelected = false;
                sinhSelected = false;
                cosSelected = false;
                coshSelected = false;
                tanSelected = false;
                tanhSelected = false;
            } else {
                switch (operator) {
                    case 1 -> {
                        screen.setText(String.valueOf(firstNum / secondNum));
                        calculatingTf.setText(String.valueOf(firstNum + "÷" + secondNum + "=" + (df.format(firstNum / secondNum))));
                    }
                    case 2 -> {
                        screen.setText(String.valueOf(firstNum * secondNum));
                        calculatingTf.setText(String.valueOf(firstNum + "x" + secondNum + "=" + (df.format(firstNum * secondNum))));
                    }
                    case 3 -> {
                        screen.setText(String.valueOf(firstNum - secondNum));
                        calculatingTf.setText(String.valueOf(firstNum + "-" + secondNum + "=" + (df.format(firstNum - secondNum))));
                    }
                    case 4 -> {
                        screen.setText(String.valueOf(firstNum + secondNum));
                        calculatingTf.setText(String.valueOf(firstNum + "+" + secondNum + "=" + (df.format(firstNum + secondNum))));
                    }
                    case 5 -> {
                        double result = Math.pow(firstNum, secondNum);
                        screen.setText(String.valueOf(result));
                        calculatingTf.setText(String.valueOf(firstNum + "^" + secondNum + "=" + result));
                    }
                    case 6 -> {
                        double result = customModuloOperation(firstNum, secondNum);
                        screen.setText(String.valueOf(result));
                        calculatingTf.setText(String.valueOf(firstNum + " MOD " + secondNum + "=" + result));
                    }
                    default -> {
                    }
                }
            }
        }
    }
    private void performFactorial() {
        if (!screen.getText().isEmpty()) {
            int num = Integer.parseInt(screen.getText());
            long result = factorial(num);
            screen.setText(String.valueOf(result));
            calculatingTf.setText(num + "! = " + result);
        }
    }
    private long factorial(int n) {
        if(n < 0) throw new RuntimeException("No negative factorial");
        if (n == 0 || n == 1) {
            return 1;
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    private void performModuloOperation() {
        if (!screen.getText().isEmpty()) {
            firstNum = Double.parseDouble(screen.getText());
            operator = 6;
            screen.setText("");
        }
    }
    private void performLogarithm() {
        if (!screen.getText().isEmpty()) {
            double logValue = Double.parseDouble(screen.getText());
            if (logValue > 0) {
                double lnResult = Math.log(logValue);
                double log10Result = Math.log10(logValue);


                screen.setText("ln(" + logValue + ") = " + lnResult + "\nlog(" + logValue + ") = " + log10Result);
                calculatingTf.setText("ln(" + logValue + ") = " + lnResult);
            } else {
                screen.setText("Invalid input");
            }
        }
    }

    private void performXInverse() {
        if (!screen.getText().isEmpty()) {
            double xValue = Double.parseDouble(screen.getText());
            double inverseResult;

            if (xValue != 0) {
                inverseResult = 1 / xValue;
                screen.setText(String.valueOf(inverseResult));
                calculatingTf.setText("1 / " + xValue + " = " + inverseResult);
            } else {
                screen.setText("Cannot divide by zero");
            }
        }
    }

    private double customModuloOperation(double a, double b) {
        return a % b;
    }
    public static void main(String[] args) {
        new CalculatorUI();
    }
}
