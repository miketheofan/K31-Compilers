public class Field{

	public String name;
	public String type;

	public Field(String type, String name){

		this.name = name;
		this.type = type;
	}

	public String toString(){
		return this.type + " " + this.name;
	}
}