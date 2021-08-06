import syntaxtree.*;
import visitor.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        if(args.length < 1){

            System.err.println("Usage: java [MainClassName] [file1] [file2] ... [fileN]");
            System.exit(1);
        }

        for(String argument : args){

            FileInputStream fis = null;
            ST symbolTable;

            try{

                fis = new FileInputStream(argument);
                MiniJavaParser parser = new MiniJavaParser(fis);

                Goal root = parser.Goal();

                System.err.println("Program parsed successfully for program \""+argument+"\".");

                symbolTable = new ST();

                /* Calling first phase visitor. */
                firstPhaseVisitor first = new firstPhaseVisitor(symbolTable);
                root.accept(first, null);

                System.err.println("First phase visitor parsed succesfully for program \""+argument+"\".");

                /* Calling second phase visitor. */
                secondPhaseVisitor second = new secondPhaseVisitor(symbolTable);
                root.accept(second,null); 
            
                System.err.println("Second phase visitor parsed succesfully.");

                /* Offsets printing part of the program. */
                symbolTable.printOffsets();

            }catch(ParseException ex){
                System.out.println(ex.getMessage());
            }catch(FileNotFoundException ex){
                System.err.println(ex.getMessage());
            }finally{
                try{
                    if(fis != null) fis.close();
                }catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}