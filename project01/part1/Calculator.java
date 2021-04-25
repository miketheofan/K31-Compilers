import java.io.InputStream;
import java.io.IOException;
import java.lang.Math;

public class Calculator{

	private final InputStream in;
	private int lookahead; 
	
	public Calculator(InputStream in) throws IOException{
		 
		this.in = in;
		this.lookahead = in.read();
	} 
	
	private void consume(int symbol) throws IOException, ParseError{
			
		if( this.lookahead == symbol )
			this.lookahead = in.read();
		else
			throw new ParseError();
	}
	
	private boolean isDigit(int c) {
		return '0' <= c && c <= '9';
	}
	
	private int evalDigit(int c) { 
	        return c - '0';   
	}
	
	public int evaluate() throws IOException, ParseError{
		
		return expr();
	}
	
	private int expr() throws IOException, ParseError {
		
		try {
												
			return expr2(term());
			
		}catch(IOException | ParseError e) {
			throw new ParseError();
		}
	}
	
	private int term() throws IOException, ParseError{
		
		if( isDigit(this.lookahead) || this.lookahead == '(' ) {
			
			/* Zero is used in order to indicate that number function will start with previous number =0.
			This functionality is mostly used for multiple digit numbers. */
			int cond = number(0);
									
			return term2(cond);
		}
		
		throw new ParseError();
	}

	/* The following function calculates a power (**), 
	by considering all cases given in piazza post: https://piazza.com/class/kl9eav853vt37s?cid=43. */
	private int powerCalculator(int base , int exponent) throws IOException, ParseError{

		if(exponent < 0) return 0;
		else if(exponent == 0) return 1;
		else if(exponent == 1) return base;

		if(exponent%2 == 0) return (int)Math.pow(base*base,exponent/2);
		else return base*(int)Math.pow(base*base,exponent/2);
	}
	
	private int term2(int condition) throws IOException, ParseError{
		
		switch(this.lookahead) { 
		
			case '*':
				 
				consume('*');
				
				/* If not two '*' are given, then we have a parse error. */
				if(this.lookahead != '*')
					throw new ParseError();
				
				consume('*');
				
				return powerCalculator(condition,term2(number(0)));
			
			/* Default cases are used in order to take into consideration '\n','EOF' characters but also
			in order to terminate functions if wrong input is given. In that case, a parse error will be thrown
			somewhere in the program. */
			default:
				return condition;
		}		
	}
	
	private int expr2(int condition) throws IOException, ParseError{
		
		int right;
				
		switch(this.lookahead) { 
		
			case '+':
				
				consume('+');
				right = term();
				
				return expr2(term2(condition+right));
				
			case '-':
				
				consume('-');
				right = term();
				
				return expr2(term2(condition-right));
				 
			default:
				return condition; 
		} 		
	}
	
	/* Following function returns an integer when a digit is given as input.
	Previous number is used for recursive reasons, as we will se below in this
	function. */
	private int number(int prevNumber) throws IOException, ParseError{		
			
		int number =0;
		
		try {
		
			if(this.lookahead == '(') {
			 
				consume('(');
				number = expr();
				consume(')');
				
			}else { 
				
				number = evalDigit(this.lookahead);
				
				consume(this.lookahead);
			}

		}catch(IOException | ParseError e) {
			System.err.println(e.getMessage());
		}
		
		/* Following condition is used in order to throw parse error if input like: 02 is given. */	
		if(prevNumber == 0 && number == 0 && isDigit(this.lookahead))
			throw new ParseError();
		
		/* Following condition is used in order to calculate multiple digit numbers. */
		if(isDigit(this.lookahead))  			
			return mul((prevNumber*10 + number));
		else 
			return prevNumber*10 + number;
		
	}	
	
	/* The following function is used in cooperation with number function in order to calculate
	multiple digit numbers that are given as input. */
	private int mul(int prevNumber) throws IOException, ParseError{
		
		int number =0;
		
		try {
		
			number = evalDigit(this.lookahead);
			consume(this.lookahead);  
			
		}catch(IOException | ParseError e) {
			System.err.println(e.getMessage());
		}
				
		if(isDigit(this.lookahead))
			return number(prevNumber*10 + number);
		else
			return prevNumber*10 + number;
	}
}
