import java.util.*;

class offsetHelper{

	String className;
	String name;
	int offset;

	public offsetHelper(String className,String name,int offset){

		this.className = className;
		this.name = name;
		this.offset = offset;
	}

	public String toString(){
		return this.className + "." + this.name + " : " + this.offset;
	}
}