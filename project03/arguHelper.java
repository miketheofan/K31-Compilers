import java.util.*;

public class arguHelper{

	String Class;
	String Method;
	boolean Assignment;

	public arguHelper(String Class,String Method,boolean Assignment){

		this.Class = Class;
		this.Method = Method;
		this.Assignment = Assignment;
	}

	public arguHelper(String Class,String Method){

		this.Class = Class;
		this.Method = Method;
		this.Assignment = false;
	}
}