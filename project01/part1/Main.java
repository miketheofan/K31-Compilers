import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		try {
			System.out.println(new Calculator(System.in).evaluate());
		}catch(IOException | ParseError e) {
			System.err.println(e.getMessage());
		}
	}
}
