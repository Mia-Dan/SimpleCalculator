import java.math.BigDecimal;  
import java.math.MathContext;  
import java.math.RoundingMode;  //除法保留
import java.util.ArrayList;  
import java.util.LinkedList;  
import java.util.List;  
import java.util.StringTokenizer;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
    
public class Calculator{  
    private final static String OPSTART = "(";   
    private final static String OPEND = ")"; 
    private static final int PRECISION =10;      //精度 
          
    private String expressionRaw;      //最原始的四则运算式  
    private String expressionInited;      //经过初始化处理后的四则运算式  
    private RoundingMode roundingMode=RoundingMode.HALF_UP;      //取舍模式
    private MathContext mc;                                   //精度上下文
    private List<String> expList = new ArrayList<String>();   //四则运算解析
    private List<String> rpnList = new ArrayList<String>();     //存放逆波兰表达式
    
    //constructor   
    public Calculator(String expressionRaw) {  
        init(expressionRaw);  
    }      

    public void init(String expressionRaw){   
        this.mc = new MathContext(PRECISION,roundingMode);  
        this.expressionInited = initExpress(expressionRaw);  
        //将数字字符分组成数字,如'+''3''2''-''1'分成'+''32''-''1'.  
        StringTokenizer st = new StringTokenizer(this.expressionInited,"+-*/()",true);  
        while(st.hasMoreElements()){  
            this.expList.add(st.nextElement().toString().trim());  
        }        
        this.rpnList = initRPN(this.expList);    
    }  

    //去除空白字符, 和在负号'-'前加'0'. 便于后面的StringTokenizer.   
    private static String initExpress(String exp){  
        String reStr = null;  
        reStr = exp.replaceAll("\\s", "");  
        if(reStr.startsWith("-")){  
            reStr = "0"+reStr;  
        }  
        reStr = reStr.replaceAll("\\(\\-", "(0-");  
        return reStr;  
    }  
      
    //是否是整数或是浮点数,但默认-05.15这种也认为是正确的格式
    private boolean isNumber(String str){  
        Pattern p = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");		            
        Matcher m = p.matcher(str);  
        boolean isNumber = m.matches();  
        return isNumber;  
    }  
      
    //设置优先级顺序()设置与否无所谓 
    private int precedence(String str){  
        char sign = str.charAt(0);  
        switch(sign){  
            case '+':  case '-':  return 1;  
            case '*':  case '/':  return 2;  
            case '(':  case ')':  return 0;   
            default:  return 0;        
        }  
    }  
      
    //转变为逆波兰表达式 
    public List<String> initRPN(List<String> strList){  
        List<String> returnList = new ArrayList<String>();  //用来存放操作符的栈  
        Stack stack = new Stack();  
        int length = strList.size();  
        for(int i=0;i<length;i++ ){  
            String str = strList.get(i);  
            if(isNumber(str)){  
                returnList.add(str);  
            }else{  
                if(str.equals(OPSTART)){  //'('直接入栈  
                    stack.push(str);  
                }else if(str.equals(OPEND)){  //')'进行出栈操作，直到栈为空或者遇到第一个左括号     
                    while (!stack.isEmpty()) { //将栈顶字符串做出栈操作     
                        String tempC = stack.pop();     
                        if (!tempC.equals(OPSTART)) { //如果不是左括号'('，则将字符串直接放到逆波兰链表的最后     
                            returnList.add(tempC);     
                        }else{ //如果是左括号'('，退出循环操作    
                             break;     
                        }     
                    }     
                }else{  
                    if (stack.isEmpty()) {  
                        //如果栈内为空, 将当前字符串直接压栈     
                        stack.push(str);     
                    }else{  //栈不空,比较运算符优先级顺序  
                        if(precedence(stack.top())>=precedence(str)){  
                            //如果栈顶元素优先级大于当前元素优先级则  
                            while(!stack.isEmpty() && precedence(stack.top())>=precedence(str)){  
                                returnList.add(stack.pop());  
                            }  
                        }  
                        stack.push(str);  
                    }  
                }  
            }  
        }  
        //如果栈不为空，则将栈中所有元素出栈放到逆波兰链表的最后     
        while (!stack.isEmpty()) {  
            returnList.add(stack.pop());  
        }  
        return returnList;  
    }  
      
    //计算逆波兰表达式 
    public String caculate(List<String> rpnList){  
        Stack numberStack = new Stack();     
          
        int length=rpnList.size();     
        for(int i=0;i<length;i++){     
            String temp=rpnList.get(i);     
            if(isNumber(temp)){     
                numberStack.push(temp);     
            }else{     
                BigDecimal tempNumber1 = new BigDecimal(numberStack.pop(),this.mc);                       
                BigDecimal tempNumber2 = new BigDecimal(numberStack.pop(),this.mc);     
                BigDecimal tempNumber = new BigDecimal("0",this.mc); 
                
                if(temp.equals("+")){     
                    tempNumber=tempNumber2.add(tempNumber1);     
                }else if(temp.equals("-")){     
                    tempNumber=tempNumber2.subtract(tempNumber1);     
                }else if(temp.equals("*")){     
                    tempNumber=tempNumber2.multiply(tempNumber1);     
                }else if(temp.equals("/")){     
                    tempNumber=tempNumber2.divide(tempNumber1, PRECISION, roundingMode);     
                }    
                numberStack.push(tempNumber.toString());      
            }     
        }       
        return numberStack.pop();    
    }  

    public String caculate(){  
        return caculate(this.rpnList);  
    }
    
    // 栈 
    private class Stack {  
          
        LinkedList<String> stackList = new LinkedList<String>();  
     
        public Stack() {}  
     
        // 入栈
        public void push(String expression) {  
            stackList.addLast(expression);  
        }  
      
        // 出栈
        public String pop() {  
            return stackList.removeLast();  
        }  
     
        // 栈顶元素 
        public String top() {  
            return stackList.getLast();  
        }  
     
        // 栈是否为空  
        public boolean isEmpty() {  
            return stackList.isEmpty();  
        }  
    }  
} 

