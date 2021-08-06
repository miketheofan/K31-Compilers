import java.util.*;
import java.io.*;
import syntaxtree.*;
import visitor.GJDepthFirst;

public class LLVMvisitor extends GJDepthFirst<String,arguHelper>{

	ST symbolTable;

	/* Virtual Table for classes and their methods, contains all classes, for every class contains a hash map of methods,
	which have a string for their name and a string for their declaration code. */
	LinkedHashMap<String,LinkedHashMap<String,String>> vTable;

  /* Virtual Table for fields of classes, contains for every class all its fields. For every field it holds its name as key
  and its type. */
  LinkedHashMap<String,LinkedHashMap<String,String>> fieldsVTable;

  /* The following is a linked hash map that holds every field,parameter that belongs to any class that exists in the program. */
	LinkedHashMap<String,String> fieldRegisters;

  /* The following is a linked hash map that is used for every other variable we need in the program. */
  LinkedHashMap<String,String> registersMap;

	String file;
	int noRegister;
	int noLabel;
	String buffer;

	public LLVMvisitor(ST symbolTable,String file) throws Exception{

		this.symbolTable = symbolTable;
		this.vTable = new LinkedHashMap<String, LinkedHashMap<String, String>>();
    this.fieldsVTable = new LinkedHashMap<String,LinkedHashMap<String,String>>();
		this.fieldRegisters = new LinkedHashMap<String,String>();
    this.registersMap = new LinkedHashMap<String,String>();
		this.file = file + ".ll";
		this.noRegister = 0;
		this.noLabel = 0;

		File newFile = new File(this.file);
		
    /* If .ll file already exists, delete it. */
		if(newFile.exists()) newFile.delete();

    /* Here we create the file. */
		newFile.createNewFile();

		this.initializeVTable();
		this.startDeclarations();
	}

  /* The following function is used in order to convert all acceptable types, to LLVM types. */
	public String typeConverter(String type){

		if(type.equals("boolean")) 
			return "i1";
		else if(type.equals("int"))
			return "i32";
		else if(type.equals("int[]"))
			return "i32*";
		else
			return "i8*";
	}

  /* The following is a function that initializes both Virtual Tables. */
	public void initializeVTable(){

		int count =0;

    /* For every class that exists in the symbol table. */
		for(Map.Entry<String,classIdentifier> tempClass : this.symbolTable.classes.entrySet()){

      /* Create both tables. */
      LinkedHashMap<String,String> fields = new LinkedHashMap<String,String>();
      this.fieldsVTable.put(tempClass.getKey(),fields);

      LinkedHashMap<String,String> methods = new LinkedHashMap<String,String>();
      this.vTable.put(tempClass.getKey(),methods);

      /* Skip main Class. */
			if(count++ == 0){

				this.buffer = "@." + tempClass.getKey() + "_vtable = global [0 x i8*] []\n";
				this.emit();
				continue;

			}

      /* If current class in inherited, add his parent's data to its virtual table. */
			if(tempClass.getValue().parentClass != ""){

        this.fieldsVTable.get(tempClass.getKey()).putAll(fieldsVTable.get(tempClass.getValue().parentClass));
				this.vTable.get(tempClass.getKey()).putAll(vTable.get(tempClass.getValue().parentClass));

      }

      /* Insert all fields to fields virtual table. */
      for (Map.Entry<String,Field> tempField : tempClass.getValue().Fields.entrySet()){

        this.fieldsVTable.get(tempClass.getKey()).put(tempField.getValue().name,tempField.getValue().type);
      }
			
      /* For every method, first print out all the data that are needed for a LLVM code to run, and then insert all data
      to the methods virtual table. */
			for (Map.Entry<String,Method> tempMethod : tempClass.getValue().Methods.entrySet()){

				String buffer = "i8* bitcast (" + this.typeConverter(tempMethod.getValue().type) + " (i8*";
				
				for(Map.Entry<String,Field> tempParameter : tempMethod.getValue().parameters.entrySet())
					buffer += "," + typeConverter(tempParameter.getValue().type);

				buffer += ")* @" + tempClass.getKey() + "." + tempMethod.getKey() + " to i8*)";
        
				methods.put(tempMethod.getKey(),buffer);
			}	

			this.vTable.put(tempClass.getKey(),methods);

			this.buffer = "@." + tempClass.getKey() + "_vtable = global [" + this.vTable.get(tempClass.getKey()).size() + " x i8*] [";
			this.emit();

      int count2=0;
			for (Map.Entry<String,String> tempMethod : this.vTable.get(tempClass.getKey()).entrySet()){

        if(count2++ == 0){

          this.buffer = tempMethod.getValue();
          this.emit();

          continue;
        }

				this.buffer = "," + tempMethod.getValue();
				this.emit();
			}

			this.buffer = "]";
			this.emit();

		}
	}

  /* The following function returns the offset of a given method. */
  public int getMethodOffset(String className,String methodName){

    int count = 0;

    for(Map.Entry<String, String> tempMethod : this.vTable.get(className).entrySet()) {

        if(tempMethod.getKey().equals(methodName))
          return count;
          
        count++;
    }

    return -1;
  }

  public int getMethodSize(String className){
    
    for(Map.Entry<String, LinkedHashMap<String,String>> tempClass : this.vTable.entrySet()) {

        if(tempClass.getKey() == className)
          return this.vTable.get(className).size();
    }

    return -1;
  }

  /* The following function returns the offset of a field given. We return offset+8, because of the memory
  that the class' virtual table needs. */
  public int getFieldOffset(String className,String field){

    int offset =0;

    for(Map.Entry<String, String> tempField : this.fieldsVTable.get(className).entrySet()) {

      if(tempField.getKey() == field)
        return offset+8;

      if(tempField.getValue().equals("int"))
        offset+=4;
      else if(tempField.getValue().equals("boolean"))
        offset+=1;
      else  
        offset+=8;

    }

    return -1;
  }

	public void startDeclarations(){

		this.buffer = "\n\n\ndeclare i8* @calloc(i32, i32)\ndeclare i32 @printf(i8*, ...)\ndeclare void @exit(i32)\n\n@_cint = constant [4 x i8] c\"%d\\0a\\00\"\n@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\ndefine void @print_int(i32 %i) {\n\t%_str = bitcast [4 x i8]* @_cint to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n\tret void\n}\n\ndefine void @throw_oob() {\n\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str)\n\tcall void @exit(i32 1)\n\tret void\n}\n";
		this.emit();
	}

  /* The following function writes current buffer to the .ll file. */
	public void emit(){

		try{

			FileWriter writer = new FileWriter(this.file,true);

			writer.write(this.buffer);
			writer.flush();
			writer.close();

		}catch(IOException e){

			e.printStackTrace();
			System.exit(-1);
		}
	}

  /* The following function returns next available register. */
	public String getRegister(){
		return "%_" + Integer.toString(this.noRegister++);
	}

  /* The next two functions are used to return last used register. */
	public String getLastRegister(){
		return "%_" + Integer.toString(this.noRegister-2);
	}

	public String getLastRegister(int x){
		return "%_" + Integer.toString(this.noRegister-x);
	}

  /* The following function returns next available label. */
	public String getLabel(String name){
    return name + Integer.toString(this.noLabel++);
  }

	/**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public String visit(MainClass n, arguHelper argu) throws Exception {

      String className = n.f1.accept(this, argu);      
      
      this.buffer = "\ndefine i32 @main() {\n";
      this.emit();

      /* Accept all variable declarations. */
      if(n.f14.present()){

          Field tempField;

          NodeListOptional varDeclarations = n.f14;
          for (int i = 0; i < varDeclarations.size(); ++i) {
            
	 		 	      VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
            	varDecl.accept(this,new arguHelper(className,"main"));

          }
      }

      /* Accept all Statements. */
      if(n.f15.present()){

        Field tempField;

        NodeListOptional statDeclarations = n.f15;
        for (int i = 0; i < statDeclarations.size(); ++i) {
          
 		 	      Statement statDecl = (Statement) statDeclarations.elementAt(i);
          	statDecl.accept(this,new arguHelper(className,"main"));

        }
      }
     
      /* Main class returns 0 by default. */
      this.buffer = "\tret i32 0\n";
      this.emit();

      this.buffer = "\n}";
      this.emit();

      return null;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public String visit(TypeDeclaration n, arguHelper argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public String visit(ClassDeclaration n, arguHelper argu) throws Exception {

      String className = n.f1.accept(this, argu);

      /* Accept all method declarations. */
      if(n.f4.present()){

			  Method tempMethod;

	      NodeListOptional methodDeclarations = n.f4;
  			for (int i = 0; i < methodDeclarations.size(); ++i) {
  	 		 	
  	 		 	MethodDeclaration methDecl = (MethodDeclaration) methodDeclarations.elementAt(i);
  	 			methDecl.accept(this,new arguHelper(className,null));
  			}
		  }

      return null;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public String visit(ClassExtendsDeclaration n, arguHelper argu) throws Exception {

      String className = n.f1.accept(this, argu);

      /* Accept all method declarations. */
      if(n.f6.present()){

    		Method tempMethod;

    	  NodeListOptional methodDeclarations = n.f6;
    			
        for (int i = 0; i < methodDeclarations.size(); ++i) {
    	 		 	
    		 	MethodDeclaration methDecl = (MethodDeclaration) methodDeclarations.elementAt(i);
    			methDecl.accept(this,new arguHelper(className,null));
    		}
	    }

      return null;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, arguHelper argu) throws Exception {

      String varType = n.f0.accept(this, argu);
      String varName = n.f1.accept(this, argu);

      this.fieldRegisters.put(varName,"%"+varName);
      this.buffer = "\t%" + varName + " = alloca " + typeConverter(varType) + "\n";
      this.emit();

      this.registersMap.put("%"+varName,varType);

      return null;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public String visit(MethodDeclaration n, arguHelper argu) throws Exception {

      String methType = n.f1.accept(this, argu);
      String methName = n.f2.accept(this, argu);

      String methodNameFormated = " @" + argu.Class + "." + methName;

      this.buffer = "\n\ndefine " + typeConverter(methType) + methodNameFormated + "(i8* %this";
      this.emit(); 

      this.buffer = "";

      LinkedHashMap<String,String> parametersMap = new LinkedHashMap<String,String>();

      /* Get all the parameters of the method that are given with the declaration 
      and insert them to the linked hash map declared just above. */
      if(n.f4.present()){

          String parameters = n.f4.accept(this,new arguHelper(argu.Class,methName));
  	  	  String type,name;

  	  	  while(parameters != ""){
   
   			  type = parameters.substring(0,parameters.indexOf(" "));
  			  parameters = parameters.substring(parameters.indexOf(" ")+1);
  					
  			  if(parameters.indexOf(",") != -1){

  				  name = parameters.substring(0,parameters.indexOf(","));
  				  parameters = parameters.substring(parameters.indexOf(",")+1);

  			  }else{

  				  name = parameters.substring(0,parameters.length());
  				  parameters = "";

  			  }

  			  this.buffer += ", " + typeConverter(type) + " %." + name;
  			  this.fieldRegisters.put(name,"%"+name);

          /* Insert current parameter to parametersMap. */
  			  parametersMap.put(name,typeConverter(type));

          /* And also insert it in registers map. */
          this.registersMap.put("%"+name,type);
	  	  }
  	  }

  	  this.buffer += ") {\n";
  	  this.emit();

   	  for(Map.Entry<String,String> temp : parametersMap.entrySet()){

   	  		String parameterType = temp.getValue();
   	  		String parameterName = temp.getKey();

   	  		this.buffer = "\t%" + parameterName + " = alloca " + parameterType + "\n";
   	  		this.buffer += "\tstore " + parameterType + " %." + parameterName + ", " + parameterType + "* %" + parameterName + "\n";
   	  		this.emit();
   	  }

      /* Accept all variable declarations. */
      if(n.f7.present()){

          NodeListOptional varDeclarations = n.f7;
          for (int i = 0; i < varDeclarations.size(); ++i) {
            
	 		 	      VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
            	varDecl.accept(this,new arguHelper(argu.Class,methName));
          }
      }

      /* Accept all Statements. */
      if(n.f8.present()){

          Field tempField;

          NodeListOptional statDeclarations = n.f8;
          for (int i = 0; i < statDeclarations.size(); ++i) {
            
	 		 	      Statement statDecl = (Statement) statDeclarations.elementAt(i);
            	statDecl.accept(this,new arguHelper(argu.Class,methName));

          }
      }

      String returnExpression = n.f10.accept(this, new arguHelper(argu.Class,methName));

      this.buffer = "\tret " + this.typeConverter(methType) + " " + returnExpression + "\n";
      this.emit();

      this.buffer = "\n}";
      this.emit();

      return null;
   }

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public String visit(FormalParameterList n, arguHelper argu) throws Exception {
        String ret = n.f0.accept(this, argu); 

        if (n.f1 != null)
            ret += n.f1.accept(this, argu);

        return ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public String visit(FormalParameterTerm n, arguHelper argu) throws Exception {
        return n.f1.accept(this, argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterTail n, arguHelper argu) throws Exception {
        
        String ret = "";
        
        for ( Node node: n.f0.nodes)
            ret += "," + node.accept(this, argu);

        return ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, arguHelper argu) throws Exception{
        return n.f0.accept(this,argu)+" "+n.f1.accept(this, argu);
    }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n, arguHelper argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n, arguHelper argu) throws Exception {
      return "int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n, arguHelper argu) throws Exception {
      return "boolean";
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n, arguHelper argu) throws Exception {
      return "int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n, arguHelper argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public String visit(Block n, arguHelper argu) throws Exception {
      
      /* Accept all Statements. */
      if(n.f1.present()){

          NodeListOptional statDeclarations = n.f1;
          for (int i = 0; i < statDeclarations.size(); ++i) {
            
              Statement statDecl = (Statement) statDeclarations.elementAt(i);
              statDecl.accept(this,argu);

          }
      }

      return null;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public String visit(AssignmentStatement n, arguHelper argu) throws Exception {

      String identifier = n.f0.accept(this, argu);
      String leftType;
      String expression = n.f2.accept(this, new arguHelper(argu.Class,argu.Method,true));

      /* If identifier is a method's field. */
      if( (leftType = this.symbolTable.classes.get(argu.Class).Methods.get(argu.Method).getFieldType(identifier)) != "" ){

          /* Otherwise get his register from fieldsRegister's LinkedHasMap. */
          leftType = this.typeConverter(leftType);
          identifier = this.fieldRegisters.get(identifier);

      /* Otherwise it is a classes' field. */
      }else{

          leftType = this.typeConverter(this.symbolTable.getFieldTypeLLVM(argu.Class,identifier));

          this.buffer = "\t" + this.getRegister() + " = getelementptr i8, i8* %this , i32 " + this.getFieldOffset(argu.Class,identifier) + "\n";
          this.emit();

          identifier = this.getRegister();
          this.buffer = "\t" + identifier + " = bitcast i8* " + this.getLastRegister() + " to " + leftType + "*" + "\n";
          this.emit();
      }

      this.buffer = "\tstore " + leftType + " " + expression + ", " + leftType + "* " + identifier + "\n";
      this.emit();

      /* Copy expression's type, to identifier variable in order for the program to know its initialized type. */
      if(this.registersMap.containsKey(expression)) 
        this.registersMap.put(identifier,this.registersMap.get(expression));

      if(expression.equals("%this"))
        this.registersMap.put(identifier,argu.Class);

      return null;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public String visit(ArrayAssignmentStatement n, arguHelper argu) throws Exception {

      String identifier = n.f0.accept(this, argu);
      String tmp;

      /* If identifier is a method's field. */
      if( (tmp = this.symbolTable.classes.get(argu.Class).Methods.get(argu.Method).getFieldType(identifier)) != ""){

        String exprRegister = this.getRegister();
        this.buffer = "\t" + exprRegister + " = load i32*, i32** " + this.fieldRegisters.get(identifier) + "\n";
        this.emit();
        identifier = exprRegister; 

      }
      /* Otherwise it is a classes' field. */
      else if( (tmp = this.symbolTable.getFieldTypeLLVM(argu.Class,identifier)) != ""){

          String register; 
          String register2;

          this.buffer = "\t" + this.getRegister() + " = getelementptr i8, i8* %this , i32 " + this.getFieldOffset(argu.Class,identifier) + "\n";
          this.emit();

          register = this.getRegister();
          this.buffer = "\t" + register + " = bitcast i8* " + this.getLastRegister() + " to i32**\n";
          this.emit();

          register2 = this.getRegister();
          this.buffer = "\t" + register2 + " = load i32*, i32** " + register + "\n";
          this.emit();

          identifier = register2;

      }

      String index = n.f2.accept(this, argu);

      this.buffer = "\t" + this.getRegister() + " = load i32, i32 *" + identifier + "\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = icmp ult i32 " + index + ", " + this.getLastRegister() + "\n";
      this.emit();

      String label1 = this.getLabel("oob");
      String label2 = this.getLabel("oob");
      String label3 = this.getLabel("oob");

      this.buffer = "\tbr i1 " + this.getLastRegister(1) + ", label %" + label1 + ", label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label1 + ":\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = add i32 " + index + ", 1\n";
      this.emit();

      tmp = this.getRegister();
      this.buffer = "\t" + tmp + " = getelementptr i32, i32* " + identifier + ", i32 " + this.getLastRegister() + "\n";
      this.emit();

      String expression = n.f5.accept(this, argu);

      this.buffer = "\tstore i32 " + expression + ", i32* " + tmp + "\n";
      this.emit();

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label2 + ":\n";
      this.emit();

      this.buffer = "\tcall void @throw_oob()\n";
      this.emit();

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label3 + ":\n";
      this.emit();

      return null;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public String visit(IfStatement n, arguHelper argu) throws Exception {

      String expression = n.f2.accept(this, argu);

      String label1 = this.getLabel("if");
      String label2 = this.getLabel("if");
      String label3 = this.getLabel("if");

      this.buffer = "\tbr i1 " + expression + ", label %" + label1 + ", label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label1 + ":\n";
      this.emit();

      n.f4.accept(this,argu);

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label2 + ":\n";
      this.emit();

      n.f6.accept(this,argu);

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label3 + ":\n";
      this.emit();

      return null;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public String visit(WhileStatement n, arguHelper argu) throws Exception {
      
      String tmpLabel0 = this.getLabel("loop");
      this.buffer = "\tbr label %" + tmpLabel0 + "\n";
      this.emit(); 

      this.buffer = "\n" + tmpLabel0 + ":\n";
      this.emit();

      String expression = n.f2.accept(this,argu);

      String tmpLabel1 = this.getLabel("loop");
      String tmpLabel2 = this.getLabel("loop");

      this.buffer = "\tbr i1 " + expression + ", label %" + tmpLabel1 + ", label %" + tmpLabel2 + "\n";
      this.emit();

      this.buffer = "\n" + tmpLabel1 + ":\n";
      this.emit();

      n.f4.accept(this,argu);

      this.buffer = "\n\n\tbr label %" + tmpLabel0 + "\n";
      this.emit();

      this.buffer = "\n" + tmpLabel2 + ":\n";
      this.emit();

      return null;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public String visit(PrintStatement n, arguHelper argu) throws Exception {

      String expression = n.f2.accept(this,argu);

      this.buffer = "\n\tcall void (i32) @print_int(i32 " + expression + ")\n";
      this.emit();

      return null;
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public String visit(Expression n, arguHelper argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public String visit(AndExpression n, arguHelper argu) throws Exception {

      String clause1 = n.f0.accept(this, argu);
      
      String label0 = this.getLabel("andclause");

      this.buffer = "\tbr label %" + label0 + "\n";
      this.emit();

      this.buffer = "\n" + label0 + ":\n";
      this.emit();

      String label1 = this.getLabel("andclause");
      String label2 = this.getLabel("andclause"); 
      String label3 = this.getLabel("andclause"); 

      this.buffer = "\tbr i1 " + clause1 + ", label %" + label1 + ", label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label1 + ":\n";
      this.emit();

      String clause2 = n.f2.accept(this, argu);

      this.buffer = "\tbr label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label2 + ":\n";
      this.emit();

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit();

      this.buffer = "\n" + label3 + ":\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = phi i1 [ 0, %" + label0 + " ], [ " + clause2 + ", %" + label2 + " ]\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n, arguHelper argu) throws Exception {

      String left = n.f0.accept(this, argu);
      String right = n.f2.accept(this, argu);

      this.buffer = "\t" + this.getRegister() + " = icmp slt i32 " + left + ", " + right + "\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(PlusExpression n, arguHelper argu) throws Exception {

      String left = n.f0.accept(this, argu);
      String right = n.f2.accept(this, argu);

      String leftRegister,rightRegister; 

      this.buffer = "\t" + this.getRegister() + " = add i32 " + left + ", " + right + "\n";
      this.emit();
      
      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n, arguHelper argu) throws Exception {

      String left = n.f0.accept(this, argu);
      String right = n.f2.accept(this, argu);

      String minusRegister = this.getRegister();
      this.buffer = "\t" + minusRegister + " = sub i32 " + left + ", " + right + "\n";
      this.emit();
      
      return minusRegister;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n, arguHelper argu) throws Exception {

      String left = n.f0.accept(this, argu);
      String right = n.f2.accept(this, argu);
      
      this.buffer = "\t" + this.getRegister() + " = mul i32 " + left + ", " + right + "\n";
      this.emit();
      
      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n, arguHelper argu) throws Exception {

      String variable = n.f0.accept(this, argu);
      String index = n.f2.accept(this,argu);

      this.buffer = "\t" + this.getRegister() + " = load i32, i32* " + variable + "\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = icmp ult i32 " + index + ", " + this.getLastRegister() + "\n";
      this.emit();

      String label1 = this.getLabel("oob");
      String label2 = this.getLabel("oob");

      this.buffer = "\tbr i1 " + this.getLastRegister(1) + ", label %" + label1 + ", label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label1 + ":\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = add i32 " + index + ", 1\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = getelementptr i32, i32* " + variable + ", i32 " + this.getLastRegister() + "\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = load i32, i32* " + this.getLastRegister() + "\n";
      this.emit();

      String label3 = this.getLabel("oob");
      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit(); 

      this.buffer = "\n" + label2 + ":\n";
      this.emit();

      this.buffer = "\tcall void @throw_oob()\n";
      this.emit();

      this.buffer = "\tbr label %" + label3 + "\n";
      this.emit(); 

      this.buffer = "\n" + label3 + ":\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n, arguHelper argu) throws Exception {

      String expression = n.f0.accept(this, argu);
      
      this.buffer = "\t" + this.getRegister() + " = load i32, i32* " + expression + "\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public String visit(MessageSend n, arguHelper argu) throws Exception {

      String className = n.f0.accept(this, argu);
      String classRegister;

      this.buffer = "\t" + this.getRegister() + " = bitcast i8* " + className + " to i8***\n";
      this.emit();

      if(className == "%this")
        classRegister = argu.Class;
      /* If className is not this, then we search registersMap Linked Hash Map, in order to find the class of the register. */
      else
        classRegister = this.registersMap.get(className);

      String methodName = n.f2.accept(this, argu);

      this.buffer = "\n\t; " + classRegister + "." + methodName + " : " + this.getMethodOffset(classRegister,methodName) + "\n";
   	  this.emit();

  	  this.buffer = "\t" + this.getRegister() + " = load i8**, i8*** " + this.getLastRegister() + "\n";
  	  this.emit();

  	  this.buffer = "\t" + this.getRegister() + " = getelementptr i8*, i8** " + this.getLastRegister() + ", i32 " + this.getMethodOffset(classRegister,methodName) + "\n";
  	  this.emit();

  	  this.buffer = "\t" + this.getRegister() + " = load i8*, i8** " + this.getLastRegister() + "\n";
  	  this.emit();

  	  Method tmpMethod = this.symbolTable.getMethod(classRegister,methodName);

      String tmpType;
  	  String tmpRegister = this.getRegister();
      this.buffer = "\t" + tmpRegister + " = bitcast i8* " + this.getLastRegister() + " to " + this.typeConverter(tmpMethod.type) + " (i8*";

  	  int count=0;
  	  while( (tmpType = tmpMethod.getParameterTypeAt(count++)) != "" )
  	  	this.buffer += "," + this.typeConverter(tmpType);

  	  this.buffer += ")*\n";
  	  this.emit();

  	  int i=0;
      LinkedHashMap<Integer,String> parameterRegisters = new LinkedHashMap<Integer,String>();
      /* Get all parameters given with message send. */
      if(n.f4.present()){

	      String parameters = n.f4.accept(this,argu);
		  	String name="";

		  	while(parameters != ""){
	 				
  				if(parameters.indexOf(",") != -1){

  						name = parameters.substring(0,parameters.indexOf(","));
  						parameters = parameters.substring(parameters.indexOf(",")+1);

  				}else{

  						name = parameters.substring(0,parameters.length());
  						parameters = "";

  				}

          /* If a parameter is a method's field or parameter. */
          if(this.fieldRegisters.containsKey(name)){

            String type = this.typeConverter(this.symbolTable.classes.get(argu.Class).getFieldType(argu.Method,name));

            this.buffer = "\t" + this.getRegister() + " = load " + type + ", " + type + "* " + this.fieldRegisters.get(name) + "\n";
            this.emit();

            parameterRegisters.put(i++,this.getLastRegister(1));

          }else
            parameterRegisters.put(i++,name);

		   	}

  		}

      String callRegister = this.getRegister();
  		this.buffer = "\t" + callRegister + " = call " + this.typeConverter(tmpMethod.type) + " " + tmpRegister + "(i8* " + className;
  		count = 0;
    	while( (tmpType = tmpMethod.getParameterTypeAt(count)) != "" ){

    		this.buffer += "," + this.typeConverter(tmpType) + " " + parameterRegisters.get(count++);
      }

    	this.buffer += ")\n";
    	this.emit();

    	this.noRegister+=i;

      /* Put register in registersMap. */
      this.registersMap.put(callRegister,classRegister);
      return callRegister;
   }

   /**
     * f0 -> Expression()
     * f1 -> ExpressionTail()
     */
    public String visit(ExpressionList n, arguHelper argu) throws Exception { 
        return n.f0.accept(this,argu) + n.f1.accept(this,argu);
    }

    /**
     * f0 -> ( ExpressionTerm() )*
     */
    public String visit(ExpressionTail n, arguHelper argu) throws Exception {
        
        String ret = "";

        if(n.f0.present())
	        for ( Node node: n.f0.nodes)
	            ret += node.accept(this, argu);

        return ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionTerm n, arguHelper argu) throws Exception {
        return "," + n.f1.accept(this, argu);
    }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public String visit(PrimaryExpression n, arguHelper argu) throws Exception {

        String expression = n.f0.accept(this,argu);

        /* If expression is "true" or "false", then cast it to 1 or 0. */
        if(expression.equals("true") || expression.equals("false"))
            return expression.equals("true") ? "1" : "0";       
        /* Otherwise if expression is an identifier */
        else if(n.f0.which == 3){

            String tmpType;

            /* Check if expression is a method's field. */
            if( (tmpType = this.symbolTable.classes.get(argu.Class).Methods.get(argu.Method).getFieldType(expression)) != "" ){

                  tmpType = this.typeConverter(tmpType);

                  this.buffer = "\t" + this.getRegister() + " = load " + tmpType + ", " + tmpType + "* %" + expression + "\n";
                  this.emit();

                  if(this.registersMap.containsKey("%"+expression))
                      this.registersMap.put(this.getLastRegister(1),this.registersMap.get("%"+expression));
                  else if(this.registersMap.containsKey(this.fieldRegisters.get(expression)))
                      this.registersMap.put(this.getLastRegister(1),this.registersMap.get("%"+expression));

                  return this.getLastRegister(1);

            /* Check if expression is a field of current class, or any class object. */
            }else{

                  tmpType = this.typeConverter(this.symbolTable.getFieldTypeLLVM(argu.Class,expression));

                  this.buffer = "\t" + this.getRegister() + " = getelementptr i8, i8* %this , i32 " + this.getFieldOffset(argu.Class,expression) + "\n";
                  this.emit();

                  this.buffer = "\t" + this.getRegister() + " = bitcast i8* " + this.getLastRegister() + " to " + tmpType + "*" + "\n";
                  this.emit();

                  this.buffer = "\t" + this.getRegister() + " = load " + tmpType + ", " + tmpType + "* " + this.getLastRegister() + "\n";
                  this.emit();

                  this.registersMap.put(this.getLastRegister(1),this.symbolTable.getFieldTypeLLVM(argu.Class,expression));

                  return this.getLastRegister(1);
            }

        }else if(Objects.equals("this", expression))
            return "%this";
        else
            /* Otherwise for the remaining cases, just return the expression(IntegerLiteral, ArrayAllocationExpression, BracketExpression, AllocationExpression, any register etc.). */
            return expression;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n, arguHelper argu) throws Exception {
      return String.valueOf(n.f0);
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n, arguHelper argu) throws Exception {
      return "true";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n, arguHelper argu) throws Exception {
      return "false";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n, arguHelper argu) throws Exception {
      return n.f0.toString();
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n, arguHelper argu) throws Exception {
      return "this";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(ArrayAllocationExpression n, arguHelper argu) throws Exception {

      String size = n.f3.accept(this, argu);

      this.buffer = "\t" + this.getRegister() + " = icmp slt i32 " + size + ", 0" + "\n";
      this.emit();

      String label1 = this.getLabel("arr_alloc");
      String label2 = this.getLabel("arr_alloc");

      this.buffer = "\tbr i1 " + this.getLastRegister(1) + ", label %" + label1 + ", label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label1 + ":\n";
      this.emit();

      this.buffer = "\tcall void @throw_oob()\n";
      this.emit();

      this.buffer = "\tbr label %" + label2 + "\n";
      this.emit();

      this.buffer = "\n" + label2 + ":\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = add i32 " + size + ", 1\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = call i8* @calloc(i32 4, i32 " + this.getLastRegister() + ")\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = bitcast i8* " + this.getLastRegister() + " to i32*\n";
      this.emit();

      this.buffer = "\tstore i32 " + size + ", i32* " + this.getLastRegister(1) + "\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n, arguHelper argu) throws Exception {

      String className = n.f1.accept(this, argu);

      int classSize = this.symbolTable.classes.get(className).getClassSize();

      String tempRegister = this.getRegister();
      this.buffer = "\t" + tempRegister + " = call i8* @calloc(i32 1, i32 " + classSize + ")\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = bitcast i8* " + this.getLastRegister() + " to i8***\n";
      this.emit();

      this.buffer = "\t" + this.getRegister() + " = getelementptr [" + this.getMethodSize(className) + " x i8*], [" + this.getMethodSize(className) + " x i8*]* @." + className + "_vtable, i32 0, i32 0\n";
      this.emit();

      this.buffer = "\tstore i8** " + this.getLastRegister(1) + ", i8*** " + this.getLastRegister() + "\n";
      this.emit();

      this.registersMap.put(tempRegister,className);

      return tempRegister;
   }

   /**
    * f0 -> "!"
    * f1 -> PrimaryExpression()
    */
   public String visit(NotExpression n, arguHelper argu) throws Exception {

      String expression = n.f1.accept(this, argu);

      this.buffer = "\t" + this.getRegister() + " = xor i1 1, " + expression + "\n";
      this.emit();

      return this.getLastRegister(1);
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n, arguHelper argu) throws Exception {
      return n.f1.accept(this,argu);
   }

}