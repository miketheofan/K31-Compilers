import java.util.*;

public class classIdentifier{

	public String name;
	public String parentClass;
	public LinkedHashMap<String,Field> Fields;
	public LinkedHashMap<String,Method> Methods;

	public classIdentifier(String name,String parentClass){

		this.name = name;
		this.parentClass = parentClass;
		this.Fields = new LinkedHashMap<String,Field>();
		this.Methods = new LinkedHashMap<String,Method>();
	}

	/* The following function returns whether a field given exists in this class. */
	public boolean fieldExists(String field){
		return Fields.containsKey(field);
	}

	/* The following function returns whether a method given exists in this class. */
	public boolean methodExists(String field){
		return Methods.containsKey(field);
	}

	/* The following function returns whether a method with same name and different characteristics
	as the given one exists in any of the superclasses of this class. */
	public boolean methodExistsDiff(Method method){

		for(Map.Entry<String,Method> temp : Methods.entrySet())
			if(temp.getKey().equals(method.name))
				return temp.getValue().compare(method);


		return false;
	}

	/* The following function inserts a field in this current class. */
	public void insertField(String name,Field field) throws Exception {

		if(this.fieldExists(name) == true)
			throw new Exception("Field <"+name+"> already declared in Class <"+this.name+".");
		else			
			this.Fields.put(name,field);
	}

	/* The following function inserts a method in this class. */
	public void insertMethod(String name,Method method) throws Exception{

		if(this.methodExists(name) == true)
			throw new Exception("Method <"+name+"> already declared in Class <"+this.name+".");
		else 
			this.Methods.put(name,method);
	}

	/* The following function inserts a parameter in a method given of this class. */
	public void insertParameter(String methodName,String name,Field field) throws Exception{

		for (Map.Entry<String,Method> temp : Methods.entrySet()){

			if(temp.getKey().equals(methodName)){
				temp.getValue().insertParameter(name,field);
				break;
			}
		}
	}

	/* The following function returns whether a field given exists or not. */
	public boolean search(String methodName,String name) throws Exception{

		for (Map.Entry<String,Method> temp : Methods.entrySet()){

			if(temp.getKey().equals(methodName)){
				if(temp.getValue().search(name) == true)
					return true;
				else
					break;
			}
		}

		if(fieldExists(name)) return true;

		return false;
	}

	/* The following function returns the type of a field given that belongs to a method. */
	public String getFieldType(String methodName,String name) throws Exception{

		String returnValue;

		for (Map.Entry<String,Method> temp : Methods.entrySet())
			if(temp.getKey().equals(methodName)){

				returnValue = temp.getValue().getFieldType(name);
				if(returnValue != "")
					return returnValue;
				else
					break;
			}

		if(this.fieldExists(name) == true)
			return this.Fields.get(name).type;
		else
			return "";
	}

	/* The following function returns the type of a field given. */
	public String getFieldType(String name){
		
		if(this.fieldExists(name))
			return this.Fields.get(name).type;
		else
			return "";
	}

	/* The following function returns the type of a method given. */
	public String getMethodReturnType(String name) throws Exception{

		if(!methodExists(name))
			return "";
		else
			return this.Methods.get(name).type;
	}

	/* The following function returns the number of parameters of a method given. */
	public int getNoParameters(String name) throws Exception{

		for (Map.Entry<String,Method> temp : Methods.entrySet())
			if(temp.getKey().equals(name))
				return temp.getValue().getNoParameters();

		return 0;
	}

	/* The following function returns a method, given its' name. */
	public Method getMethod(String name) throws Exception{
			return this.Methods.get(name);		
	}

	/* The following function returns the offset value of a type given. */
	public int getOffsetByType(String type){

		if(type == "int") return 4;
		else if(type == "boolean") return 1;
		else return 8;
	}

	/* The following function updates a List given, using the offsets of all fields/methods. */
	public Offset getOffsets(ST table,Offset offset,LinkedList<offsetHelper> list) throws Exception{

		int fieldsOffset = offset.fieldsOffset;
		int methodsOffset = offset.methodsOffset;

		for (Map.Entry<String,Field> temp : Fields.entrySet()){

			if(temp.getValue().type.equals("int"))
				list.add(new offsetHelper(this.name,temp.getValue().name,fieldsOffset));
			else if(temp.getValue().type.equals("boolean"))
				list.add(new offsetHelper(this.name,temp.getValue().name,fieldsOffset));
			else	
				list.add(new offsetHelper(this.name,temp.getValue().name,fieldsOffset));

			fieldsOffset += getOffsetByType(temp.getValue().type);
		}

		for (Map.Entry<String,Method> temp : Methods.entrySet()){

			if(!table.isInheritance(this,this.getMethod(temp.getKey())))	
				list.add(new offsetHelper(this.name,temp.getValue().name,methodsOffset));
			else
				continue;

			methodsOffset += 8;
		}

		return new Offset(fieldsOffset,methodsOffset);
	}

}