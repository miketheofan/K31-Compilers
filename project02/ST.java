import java.util.*;

public class ST{

	public LinkedHashMap<String,classIdentifier> classes;

	public ST(){
		classes = new LinkedHashMap<String,classIdentifier>();
	}

	/* Following function returns whether a class exist in symbol table or not. */
	public boolean classExists(String name){
		return classes.containsKey(name);
	}

	/* Following function checks whether we have circular inheritance in our class definitions
	or not.
	** This function will probably never be called, but because of the following post in 
	piazza I thought I should check for this edge case, even though the grammar probably
	prevents it: 
	https://piazza.com/class/kl9eav853vt37s?cid=81
	**
	*/
	public void checkForCircular(classIdentifier identif) throws Exception{

		String parent = identif.parentClass;
		classIdentifier tempClass;

		while(parent != ""){

			if(parent == identif.name)
				throw new Exception("ClassCircularityError");

			for (Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(parent)){

					tempClass = temp.getValue();
					parent = tempClass.parentClass;
					break;
				}
		}
	}

	/* The following function inserts a class in symbol table. */
	public void insertClass(String name,classIdentifier identif) throws Exception{
		
		this.checkForCircular(identif);

		if(this.classExists(name) == true)
			throw new Exception("Class name <"+name+"> already declared.");
		else			
			this.classes.put(name,identif);
	}

	/* The following function inserts a field inside a class of this symbol table. */
	public void insertField(String className,String name,Field field) throws Exception{

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
			if(temp.getKey().equals(className)){
				temp.getValue().insertField(name,field);
				break;
			}
	}

	/* The following function inserts a method inside a class of this symbol table. */
	public void insertMethod(String className,String name,Method method) throws Exception{

		if(className.equals(name)) 
			throw new Exception("There should not be a constructor of class <"+className+">.");

		String current="" , parent = "";
		Map.Entry<String,classIdentifier> currentNode = null;

		for (Map.Entry<String,classIdentifier> temp : classes.entrySet()){

			if(temp.getKey().equals(className)){
			
				if(temp.getValue().methodExistsDiff(method))
					throw new Exception("Method name: <"+name+"> already declared in Class <"+temp.getKey()+">.");
				else{

					currentNode = temp;
					current = temp.getKey();
					parent = temp.getValue().parentClass;
					break;
				}
			}
		}

		switch(checkInSuperclassesMethod(parent,method)){

			case 0:
				currentNode.getValue().insertMethod(name,method);
				break;
			case -1:
				throw new Exception("Method name: <"+name+"> is not overriden correctly.");
			case 1:
				currentNode.getValue().insertMethod(name,method);
				break;
		}
	}	

	/* The following function returns a method of a class in this symbol table. */
	public Method getMethod(String className,String methodName) throws Exception{

		for (Map.Entry<String,classIdentifier> temp : classes.entrySet())

			if(temp.getKey().equals(className))
				if(temp.getValue().methodExists(methodName))
					return temp.getValue().getMethod(methodName);
				else{
					if(this.checkInSuperclassesMethod(temp.getValue().parentClass,methodName) != 1)						
						throw new Exception("Method <"+methodName+"> is not declared.");
					
					return this.getMethod(temp.getValue().parentClass,methodName);
				}

		return null;
	}

	/* The following function inserts a parameter inside a method given. */
	public void insertParameter(String className,String methodName,String name,Field field) throws Exception{

		for (Map.Entry<String,classIdentifier> temp : classes.entrySet()){

			if(temp.getKey().equals(className)){
				temp.getValue().insertParameter(methodName,name,field);
				break;
			}
		}
	}

	/* The following function checks if field exists in class,method given. If not, it throws an exception. */
	public void searchField(String className,String methodName,String name) throws Exception{

		String parentClass = "";

		for (Map.Entry<String,classIdentifier> temp : classes.entrySet()){

			if(temp.getKey().equals(className)){
				
				if(temp.getValue().search(methodName,name) == true)
					return;
				else{

					parentClass = temp.getValue().parentClass;
					break;
				}
			}
		}

		checkInSuperclassesField(parentClass,name);
	}

	/* The following function checks if a field exists in class given, or any of its superclasses. */
	public void checkInSuperclassesField(String parent,String field) throws Exception{

		String currentParent = parent;

		while(currentParent != ""){

			for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(currentParent)){

					if(temp.getValue().fieldExists(field))
						return;
					currentParent = temp.getValue().parentClass;
					break;
				}
		}

		throw new Exception("Variable <"+field+"> not initialized.");
	}

	/* The following function checks if a method exists in class given, or any of its superclasses. */
	public int checkInSuperclassesMethod(String parent,Method method) throws Exception{

		String currentParent = parent;

		while(currentParent != ""){

			for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(currentParent)){

					if(temp.getValue().methodExists(method.name))
						if(!temp.getValue().methodExistsDiff(method))
							return -1;
						else return 1;

					currentParent = temp.getValue().parentClass;
					break;
				}
		}

		return 0;
	}

	/* The difference of this functions with the above, is that this one gets as a parameter the name of the method
    ,as the above one gets a Method class object. The difference between the above function and this one is that 
    in this one we can just use the name of the method(for cases where we are searching the Method itslef). */
    public int checkInSuperclassesMethod(String parent,String method) throws Exception{

        String currentParent = parent;

        while(currentParent != ""){

            for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
                if(temp.getKey().equals(currentParent)){

                    if(temp.getValue().methodExists(method))
                        return 1;

                    currentParent = temp.getValue().parentClass;
                    break;
                }
        }

        return 0;
    }


	/* The following function returns whether a type is acceptable or not. */
	public boolean checkType(String type) throws Exception{

		if( Objects.equals(type,"boolean") || Objects.equals(type,"int[]") || Objects.equals(type,"int") )
			return true;

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(type))
					return true;

		return false;	
	}

	/* The following function returns whether a type given belongs to the basic types or not. */
	public boolean isBasicType(String type) throws Exception{

		if( type.equals("boolean") 
				|| type.equals("int[]") 
				|| type.equals("int") )
			return true;

		return false;
	}

	/* The following function checks if return type given, matches return expression's type (by finding what the last type is). */
	public void checkReturnExpression(String className,String methodName,String returnType,String returnExpr) throws Exception{

		if(returnExpr.equals(returnType))
			return;

		String returnExprType = getFieldType(className,methodName,returnExpr);

		if(returnType.equals(returnExprType))
			return;

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
			if(temp.getKey().equals(returnExpr))
				if(temp.getValue().parentClass == returnType)
					return;

		throw new Exception("Return type is not matched for method <"+methodName+">.");
	}

	/* The following function returns the type of a field given. */
	public String getFieldType(String className,String methodName,String name) throws Exception{

		if(Objects.equals(name,"int")) return "int";
		if(Objects.equals(name,"boolean")) return "boolean";
		if(Objects.equals(name,"int[]")) return "int[]";

		String parentClass = "";

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
			if(temp.getKey().equals(className)){

				String returnValue =  temp.getValue().getFieldType(methodName,name);
				if(returnValue != "")
					return returnValue;
				else{

					parentClass = temp.getValue().parentClass;
					break;
				}
			}

		return getFieldTypeSuperClasses(parentClass,name);
	}

	/* The following function check for the type of a field given, in a class given and its superclasses. */
	public String getFieldTypeSuperClasses(String parent,String name) throws Exception{

		String currentParent = parent;

		while(currentParent != "")
			for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(currentParent)){

					String returnValue = temp.getValue().getFieldType(name);
					if(returnValue != "")
						return returnValue;
					else{
						currentParent = temp.getValue().parentClass;
						break;
					}
				}

		return "";
	}

	/* The following returns the type of a method given. */
	public String getMethodReturnType(String className,String methodName) throws Exception{

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(className)){

					String ret = temp.getValue().getMethodReturnType(methodName);

					if(ret.equals("")){
						if(!returnIfMethodExists(className,methodName)){
							if(checkInSuperclassesMethod(temp.getValue().parentClass,getMethod(className,methodName)) != 1)
								throw new Exception("Method <"+methodName+"> is not declared in Class <"+temp.getKey()+">.");
						
						return this.getMethodReturnType(temp.getValue().parentClass,methodName);
						}
					}else
						return ret;
				}

		return "";
	}

	/* The following function returns whether a method given exists or not. */
	public boolean returnIfMethodExists(String className,String methodName) throws Exception{

		for (Map.Entry<String,classIdentifier> temp : classes.entrySet())

			if(temp.getKey().equals(className))
				if(temp.getValue().methodExists(methodName))
					return true;
				else
					if(this.checkInSuperclassesMethod(temp.getValue().parentClass,getMethod(className,methodName)) != 1)
						return false;

		return false;
	}

	/* The following function returns the number of parameters of a method given. */
	public int getNoParameters(String className,String methodName) throws Exception{

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(className))
					return temp.getValue().getNoParameters(methodName);

		return 0;
	}

	/* The following function checks if parent's class name given, is a superclass of child's class name. */
	public boolean isParent(String parent,String child) throws Exception{

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet())
				if(temp.getKey().equals(child)){
					if(temp.getValue().parentClass.equals(parent))
						return true;
					else
						return this.isParent(parent,temp.getValue().parentClass);
				}

		return false;
	}

	/* The following function checks if method given is inherited by any superclass. */
	public boolean isInheritance(classIdentifier Class,Method method) throws Exception{

		if(this.checkInSuperclassesMethod(Class.parentClass,method) == 1)
			return true;

		return false;
	}

	/* The following function prints the offsets of all fields/methods of all classes that exist
	in symbol table. */
	public void printOffsets() throws Exception{

		System.out.println("------------OFFSETS------------");

		int counter =0;
		String previousClass = null;
		Offset tempOffset = new Offset(0,0);
		LinkedList<offsetHelper> list = new LinkedList<>();

		for(Map.Entry<String,classIdentifier> temp : classes.entrySet()){

			if(!temp.getValue().parentClass.equals(previousClass))
				tempOffset = new Offset(0,0);
			
			if(counter++ != 0)
				tempOffset = temp.getValue().getOffsets(this,tempOffset,list);

			previousClass = temp.getKey();
		}
		
        for(offsetHelper helper : list)
        	System.out.println(helper);
	}
}
