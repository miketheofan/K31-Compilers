import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Objects;

class firstPhaseVisitor extends GJDepthFirst<String, arguHelper>{

	public ST symbolTable;

	public firstPhaseVisitor(ST symbolTable){
		this.symbolTable = symbolTable;
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
        
        String classname = n.f1.accept(this, argu);
        /* Create new class, and insert it in symbol table. This certain one will be the main class. */
        classIdentifier identif = new classIdentifier(classname,"");
        this.symbolTable.insertClass(classname,identif);

        /* Create new method, and insert it in the symbol table correctly. */
        Method tempMethod = new Method("void","main");
        this.symbolTable.insertMethod(classname,"main",tempMethod);

        /* Add argument name as the first and only field of main method. */
        String argument = n.f11.accept(this, argu);
        Field parameter = new Field("String[]",argument);
        tempMethod.insertParameter(argument,parameter);

        /* Get all the fields that belong to the main method, and correctly insert them to the symbol table. */
        if(n.f14.present()){

            Field tempField;

            NodeListOptional varDeclarations = n.f14;
            for (int i = 0; i < varDeclarations.size(); ++i) {
                
                VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
                
                String varType = varDecl.f0.accept(this,new arguHelper(classname,"main"));
                String varName = varDecl.f1.accept(this,new arguHelper(classname,"main"));
                                
                tempField = new Field(varType,varName);   
                this.symbolTable.classes.get(classname).Methods.get("main").insertVariable(varName,tempField);
            }
        }

        /* Get all statements that exist inside the main method. Error chech them correctly, using their
         corresponding visit() function. */
        if(n.f15.present()){

            NodeListOptional statements = n.f15;
            for (int i = 0; i < statements.size(); ++i) {
                
                Statement statement = (Statement) statements.elementAt(i);              
                statement.accept(this,new arguHelper(classname,"main"));
            }
        }

        return null;
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
        
        /* Insert class name in symbol table. */
        String classname = n.f1.accept(this, argu);
        classIdentifier identif = new classIdentifier(classname,"");
        this.symbolTable.insertClass(classname,identif);
		
        /* Insert all fields that are declared at the beginning of the class' declaration in the symbol table. */
		if(n.f3.present()){

			Field tempField;

	        NodeListOptional varDeclarations = n.f3;
			for (int i = 0; i < varDeclarations.size(); ++i) {
	 		 	
	 		 	VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
	  			
	  			String varType = varDecl.f0.accept(this,argu);
	  			String varName = varDecl.f1.accept(this,argu);
	  				  			
	  			tempField = new Field(varType,varName);			
				this.symbolTable.insertField(classname,varName,tempField);
			}
		}

        /* Insert all the methods of this certain class in the symbol table. */
        if(n.f4.present()){

			Method tempMethod;

	        NodeListOptional methodDeclarations = n.f4;
			for (int i = 0; i < methodDeclarations.size(); ++i) {
	 		 	
	 		 	MethodDeclaration methDecl = (MethodDeclaration) methodDeclarations.elementAt(i);
	  			
	  			String methName = methDecl.f2.accept(this,argu);
	  				  						
				String test = methDecl.accept(this,new arguHelper(classname,methName));
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
        
        /* Same exact functionality as the above function, but this time we also chech if "extends" class exists. */

		String parentName = n.f3.accept(this, argu);
        if(!this.symbolTable.classExists(parentName))
            throw new Exception("Extended Class <"+parentName+"> is not declared.");

        String classname = n.f1.accept(this, argu);
        /* We also create a class this time declaring parentName field of class as parentName variable read just above. */ 
        classIdentifier identif = new classIdentifier(classname,parentName);
        this.symbolTable.insertClass(classname,identif);
		
		if(n.f5.present()){

			Field tempField;

	        NodeListOptional varDeclarations = n.f5;
			for (int i = 0; i < varDeclarations.size(); ++i) {
	 		 	
	 		 	VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
	  			
	  			String varType = varDecl.f0.accept(this,argu);
	  			String varName = varDecl.f1.accept(this,argu);
	  				  			
	  			tempField = new Field(varType,varName);			
				this.symbolTable.insertField(classname,varName,tempField);
			}
		}

		if(n.f6.present()){

			Method tempMethod;

	        NodeListOptional methodDeclarations = n.f6;
			for (int i = 0; i < methodDeclarations.size(); ++i) {
	 		 	
	 		 	MethodDeclaration methDecl = (MethodDeclaration) methodDeclarations.elementAt(i);
	  			
	  			String methName = methDecl.f2.accept(this,argu);
 	  			
                String test = methDecl.accept(this,new arguHelper(classname,methName));
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
        
        String type = n.f0.accept(this,argu);
        String identifier = n.f1.accept(this, argu);

        return type + " " + identifier;
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

        Method tempMethod = new Method(methType,methName);     

        /* Get all the parameters of the method that are given with the declaration. */
        if(n.f4.present()){

	        String parameters = n.f4.accept(this,argu);
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

				Field tempField = new Field(type,name);
                tempMethod.insertParameter(name,tempField);
		  	}
		}
        
        /* Insert method in symbol table. */
        this.symbolTable.insertMethod(argu.Class,methName,tempMethod);

        /* Get all fields of this certain method. */
        if(n.f7.present()){

			Field tempField;

	        NodeListOptional varDeclarations = n.f7;
			for (int i = 0; i < varDeclarations.size(); ++i) {
	 		 	
	 		 	VarDeclaration varDecl = (VarDeclaration) varDeclarations.elementAt(i);
	  			
	  			String varType = varDecl.f0.accept(this,argu);
	  			String varName = varDecl.f1.accept(this,argu);
	  				  			
	  			tempField = new Field(varType,varName);		
				this.symbolTable.classes.get(argu.Class).Methods.get(methName).insertVariable(varName,tempField);
			}
		}

        /* Get all statements of this certain method. */
        if(n.f8.present()){

	        NodeListOptional statements = n.f8;
			for (int i = 0; i < statements.size(); ++i) {
	 		 	
	 		 	Statement statement = (Statement) statements.elementAt(i);	  			
	  			statement.accept(this,argu);
			}
		}

        String myName5 = n.f10.accept(this, argu);

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
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String visit(AssignmentStatement n, arguHelper argu) throws Exception{
        
        String name = n.f0.accept(this, argu);
        return name;
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
    public String visit(ArrayAssignmentStatement n, arguHelper argu) throws Exception{

        String name = n.f0.accept(this, argu);
        return name;
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
        
        String exprType = n.f2.accept(this, argu);
        return exprType;
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

    public String visit(ArrayType n, arguHelper argu) {
        return "int[]";
    }

    public String visit(BooleanType n, arguHelper argu) {
        return "boolean";
    }

    public String visit(IntegerType n, arguHelper argu) {
        return "int";
    }

    public String visit(Identifier n, arguHelper argu) {
        return n.f0.toString();
    }
}