import java.util.*;

public class Method{

	public String type;
	public String name;
	public int noParameters;
	public LinkedHashMap<String,Field> parameters;
	public LinkedHashMap<String,Field> variables;

	public Method(String type,String name){

		this.type = type;
		this.name = name;
		this.noParameters = 0;
		this.parameters = new LinkedHashMap<>();
		this.variables = new LinkedHashMap<>();
	}

	/* The following function inserts a parameter in this method. */
	public void insertParameter(String name,Field field) throws Exception{

		for (Map.Entry<String,Field> temp : parameters.entrySet())
			if(temp.getKey().equals(name))
				throw new Exception("Parameter <"+name+"> already given.");

		this.noParameters++;
		this.parameters.put(name,field);
	}

	/* The following function inserts a field in this method. */
	public void insertVariable(String name,Field field) throws Exception{

		for (Map.Entry<String,Field> temp : variables.entrySet())
			if(temp.getKey().equals(name))
				throw new Exception("Variable <"+name+"> already initialized.");

		for (Map.Entry<String,Field> temp : parameters.entrySet())
			if(temp.getKey().equals(name))
				throw new Exception("Variable <"+name+"> already initialized.");

		this.variables.put(name,field);
	}

	/* The following function returns whether a field/parameter exists, given its' name. */
	public boolean search(String name) throws Exception{

		for(Map.Entry<String,Field> temp : variables.entrySet())
			if(temp.getKey().equals(name))
				return true;

		for(Map.Entry<String,Field> temp : parameters.entrySet())
			if(temp.getKey().equals(name))
				return true;

		return false;
	}

	/* The following function compares a method given with this method. */
	public boolean compare(Method method){

		if((this.parameters.isEmpty() && !method.parameters.isEmpty()) 
			|| (!this.parameters.isEmpty() && method.parameters.isEmpty()))
			return false;

		if(this.name == method.name && this.type == method.type){
			
			if(this.parameters.isEmpty() && method.parameters.isEmpty())
				return true;

			int size = this.parameters.size();

			for(int i=0;i<size;i++){

				if(!this.parameters.values().toArray()[i].toString().equals(method.parameters.values().toArray()[i].toString()))
					return false;
			}

			return true;
		}

		return false;
	}

	/* The following function returns whether a parameter exists or not in this method. */
	public boolean parameterExists(String name) throws Exception{
		return this.parameters.containsKey(name);
	}

	/* The following function returns whether a variable exists or not in this method. */
	public boolean variableExists(String name) throws Exception{
		return this.variables.containsKey(name);
	}

	/* The following function returns the type of a field given. */
	public String getFieldType(String name) throws Exception{
		
		if(this.search(name) == true)
			if(variableExists(name))
				return this.variables.get(name).type;
			else
				return this.parameters.get(name).type;
		else 
			return "";
	}

	/* The following function returns the number of parameters that exists in this method. */
	public int getNoParameters() throws Exception{
		return this.noParameters;
	}

	/* The following function returns the type of a parameter on index given. */
	public String getParameterTypeAt(int i) throws Exception{

		if(i >= this.noParameters)
			return "";

		String content = this.parameters.values().toArray()[i].toString();
		return content.substring(0,content.indexOf(" "));
	}
}