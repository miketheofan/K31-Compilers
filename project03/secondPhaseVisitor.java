import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.Objects;

class secondPhaseVisitor extends GJDepthFirst<String, arguHelper>{

	public ST symbolTable;

	public secondPhaseVisitor(ST symbolTable){
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

    	String classname = n.f1.accept(this,argu);
    	n.f11.accept(this,argu);
    	n.f14.accept(this,new arguHelper(classname,"main")); 
    	n.f15.accept(this,new arguHelper(classname,"main"));

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
        
        String className = n.f1.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, new arguHelper(className, ""));
        
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
        n.f3.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, new arguHelper(className, ""));

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public String visit(VarDeclaration n,arguHelper argu) throws Exception {

        String type = n.f0.accept(this, argu);
        /* Check if type of variable is acceptable. */
        /* Acceptable types are "int", "boolean", "int[]" and any Class' object. */
        if(this.symbolTable.checkType(type) == false)
            throw new Exception("Type <"+type+"> is not acceptable."); 

        String name = n.f1.accept(this, argu);

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
        
        n.f0.accept(this, argu);
        
        String returnType = n.f1.accept(this, argu);
        String methName = n.f2.accept(this, argu);

        /* Check if return type of method is acceptable. */
        if(symbolTable.checkType(returnType) == false)
            throw new Exception("Return type <"+returnType+"> is not acceptable.");

        n.f4.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, new arguHelper(argu.Class, methName));

        n.f9.accept(this, argu);

        /* Check if return expression's type matches return type. */
        String returnExpr = n.f10.accept(this, new arguHelper(argu.Class, methName));
        this.symbolTable.checkReturnExpression(argu.Class,methName,returnType,returnExpr);

        return null;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public String visit(FormalParameterList n, arguHelper argu) throws Exception {
        return n.f0.accept(this,argu)+n.f1.accept(this,argu);
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public String visit(FormalParameter n, arguHelper argu) throws Exception {

    	String parameterType = n.f0.accept(this, argu);
        if(!symbolTable.checkType(parameterType))
            throw new Exception("Parameter type <"+parameterType+"> is not acceptable.");
        String parameterName = n.f1.accept(this, argu);

        return parameterType+" "+parameterName;
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public String visit(FormalParameterTail n, arguHelper argu) throws Exception {
        
        String ret = "";
        
        for ( Node node: n.f0.nodes) {
            ret += "," + node.accept(this, argu);
        }

        return ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterTerm n, arguHelper argu) throws Exception {        
        return ","+n.f1.accept(this, argu);
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
        return n.f0.toString() + n.f1.toString() + n.f2.toString();
    }

    /**
    * f0 -> "boolean"
    */
    public String visit(BooleanType n, arguHelper argu) throws Exception {
        return n.f0.toString();
    }

    /**
     * f0 -> "int"
     */
    public String visit(IntegerType n, arguHelper argu) throws Exception {
        return n.f0.toString();
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
        return n.f0.accept(this,argu).toString();
    }

    /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    public String visit(AndExpression n, arguHelper argu) throws Exception {
        
        String left = n.f0.accept(this,argu);
        String right = n.f2.accept(this,argu);

        /* Check if clauses outside of '&&' are booleans. */
        if (!Objects.equals("boolean",left) || !Objects.equals("boolean",right))
            throw new Exception("Error in And Expression between types <"+left+"> and <"+right+">.");

        return "boolean";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, arguHelper argu) throws Exception {
        
        String left = n.f0.accept(this,argu);
        String right = n.f2.accept(this,argu);

        /* Check if clauses outside of '<' are booleans. */
       	if (!Objects.equals("int",left) || !Objects.equals("int",right))
            throw new Exception("Error in Compare Expression between types <"+left+"> and <"+right+">.");

        return "boolean";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, arguHelper argu) throws Exception {
        
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        /* Check if clauses outside of '+' are booleans. */
        if (!Objects.equals("int",left) || !Objects.equals("int",right))
            throw new Exception("Error in Plus Expression between types <"+left+"> and <"+right+">.");

        return "int";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, arguHelper argu) throws Exception {
        
        String left = n.f0.accept(this,argu);
        String right = n.f2.accept(this,argu);

        /* Check if clauses outside of '-' are booleans. */
        if (!Objects.equals("int",left) || !Objects.equals("int",right))
            throw new Exception("Error in Minus Expression between types <"+left+"> and <"+right+">.");

        return "int";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n,arguHelper argu) throws Exception {
        
        String left = n.f0.accept(this,argu);
        String right = n.f2.accept(this,argu);
        
        /* Check if clauses outside of '*' are booleans. */
        if (!Objects.equals("int", left) || !Objects.equals("int", right) )
        	throw new Exception("Error in Times Expression between types <"+left+"> and <"+right+">.");

        return "int";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, arguHelper argu) throws Exception {

    	/* Check if variable in which lookup is called, is type int[]. */
        String varType = n.f0.accept(this, argu);
        if(!Objects.equals(varType,"int[]"))
        	throw new Exception("Array Lookup must be on a variable of type int[], given type is <" + varType + ">.");

        /* Check if index inside '[]' is type int. */
        String indexType = n.f2.accept(this, argu);
        if(!Objects.equals("int", indexType)) {
            throw new Exception("Array's index <" + indexType + "> is not an integer");
        }

        return "int";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, arguHelper argu) throws Exception {
        
        /* Check if variable in which .length is called, is type int[]. */
        String exprType = n.f0.accept(this, argu);
        if(!Objects.equals("int[]", exprType)) {
            throw new Exception("Array Length must be on a variable of type int[].\'" + exprType + "\' must be an integer.");
        }

        n.f1.accept(this, argu);
        n.f2.accept(this, argu);

        return "int";
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
        
        String exprType = n.f0.accept(this, argu);

        /* Chech whether primary expression is a class' object or not. */
        if(this.symbolTable.isBasicType(exprType) == true)
            throw new Exception("Message send must be called on a Class object.");

        /* Check if method called exists in class given above, or in anyone of its' superclasses. */
        String methName = n.f2.accept(this, argu);
       	if(this.symbolTable.getMethod(exprType,methName) == null)
            throw new Exception("Method <"+methName+"> is not declared");

       	Method tempMethod = this.symbolTable.getMethod(exprType,methName);

        String methType = symbolTable.getMethodReturnType(exprType,methName);

        /* Chech each and every one of the parameters given, and compare them with the prototype of the method. */
        if(n.f4.present()){

        	int i=0;

	        String parameters = n.f4.accept(this,argu);
		  	String type="",name="";
		  	String typeExpected = "";

		  	while(parameters != ""){
	 				
				if(parameters.indexOf(",") != -1){

						name = parameters.substring(0,parameters.indexOf(","));
						parameters = parameters.substring(parameters.indexOf(",")+1);
				}else{

						name = parameters.substring(0,parameters.length());
						parameters = "";
				}
				
				if(!this.symbolTable.checkType(name))
					name = this.symbolTable.getFieldType(argu.Class,argu.Method,name);				

				typeExpected = tempMethod.getParameterTypeAt(i++);

				if(!typeExpected.equals(name))
					if(!this.symbolTable.isParent(typeExpected,name))
						throw new Exception("Wrong arguments given for method call <" + tempMethod.name+">.Expected type <" + typeExpected+"> and got type <" + name+">.");
		   	}

		   	if(i < tempMethod.getNoParameters())
				throw new Exception("Cannot call method <" + tempMethod.name + "> missing arguments.");			

		}else{
			if(tempMethod.getNoParameters() != 0)
				throw new Exception("Cannot call method <" + tempMethod.name + "> without arguments.");
		}

        return methType;
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
    *       | BracketExpression()
    */
    public String visit(PrimaryExpression n, arguHelper argu) throws Exception {
        
        String expression = n.f0.accept(this, argu);

        /* Chech if expression's type is acceptable. */
        if (Objects.equals("int", expression) 
        		|| Objects.equals("boolean", expression)
            	|| Objects.equals("int[]", expression))
            return expression;
        else if(n.f0.which == 3){

            if(symbolTable.classExists(expression))
                return expression;

           symbolTable.searchField(argu.Class,argu.Method,expression);
           return this.symbolTable.getFieldType(argu.Class,argu.Method,expression);
        }else if(Objects.equals("this", expression))
            return argu.Class;
        else
            /* Otherwise for the remaining cases, just return the expression(ArrayAllocationExpression, BracketExpression, AllocationExpression). */
            return expression;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, arguHelper argu) throws Exception {
        return argu.Class;
    }

    /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(ArrayAllocationExpression n,arguHelper argu) throws Exception {
      
      String expression = n.f3.accept(this,argu);

      /* Check if index inside '[]' is an integer. */
      if(!Objects.equals(expression,"int"))
      		throw new Exception("Index inside new int[] must be an integer.");
   
      return "int[]";
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n,arguHelper argu) throws Exception {

      String identifier = n.f1.accept(this,argu);

      /* Check if class exists. */
      if(!this.symbolTable.classExists(identifier))
      	throw new Exception("Class <"+identifier+"> is not declared.");

      return identifier;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, arguHelper argu) throws Exception {
        return n.f1.accept(this, argu).toString();
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, arguHelper argu) throws Exception {
        n.f0.accept(this, argu);

        /* Check if clause is a boolean. */
        String clauseType = n.f1.accept(this, argu);
        if(!Objects.equals("boolean", clauseType)) {
            throw new Exception("There must be a boolean expression after <!>.");
        }

        return "boolean";
    }

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n,arguHelper argu) throws Exception {
        return "int";
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n,arguHelper argu) throws Exception {
        return "boolean";
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n,arguHelper argu) throws Exception {
        return "boolean";
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n,arguHelper argu) throws Exception {
        return n.f0.toString();
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
    public String visit(AssignmentStatement n, arguHelper argu) throws Exception {

    	/* Check if variable given as identifier exists. */
        String leftName = n.f0.accept(this, argu);
        this.symbolTable.searchField(argu.Class,argu.Method,leftName);        
        String leftType = symbolTable.getFieldType(argu.Class,argu.Method,leftName);

        String rightType = n.f2.accept(this, argu);

        /* And then make sure both types matches. */
        if(!leftType.equals(rightType))
        	if(!this.symbolTable.isBasicType(leftType)){
        		if(this.symbolTable.isParent(leftType,rightType))
        			return null;
        		else
	          		throw new Exception("Assignment values must match.Error on <"+leftType+"> = <"+rightType+">.");
      		}else
                throw new Exception("Assignment values must match.Error on <"+leftType+"> = <"+rightType+">.");

        if(leftType.equals("String[]") || rightType.equals("String[]"))
            throw new Exception("Cannot use variables of type <String[]>.");

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

    	/* Check if variable given as identifier exists in symbol table. */
        String arrayName = n.f0.accept(this, argu);
        this.symbolTable.searchField(argu.Class,argu.Method,arrayName);
        
        String leftType = symbolTable.getFieldType(argu.Class,argu.Method,arrayName);
        if(!Objects.equals(leftType, "int[]"))
            throw new Exception("Array assignment variable must be of type int[]");

        /* Then check if index is an integer. */
        String index = n.f2.accept(this, argu);
        if(!Objects.equals("int", index))
            throw new Exception("Index inside <"+arrayName+"> must be an integer.");

        /* Finally check if both types matches. */
        String rightType = n.f5.accept(this, argu);
        if (!Objects.equals(rightType, "int"))
            throw new Exception("Array assignment values must match.Error on <"+leftType+"> = <"+rightType+"\">.");

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

    	/* Check if expression is a boolean. */
        String expression = n.f2.accept(this, argu);
        if(!Objects.equals("boolean", expression))
            throw new Exception("Expression inside if statement must be boolean.");

        n.f4.accept(this, argu);
        n.f6.accept(this, argu);

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
        
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);

    	/* Check if expression is a boolean. */
        String expression = n.f2.accept(this, argu);
        if(!Objects.equals("boolean", expression))
            throw new Exception("Expression inside while statement must be boolean.");

        n.f4.accept(this, argu);
        return null;
    }

    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n,arguHelper argu) throws Exception {

    	/* Check if expression is an integer, or function call that returns integer. */
        String expression = n.f2.accept(this, argu);
        if (Objects.equals("int", expression) == false || !expression.equals("int"))
            throw new Exception("Expression inside System.out.println() statement must be an integer.");

        return null;
    }
}