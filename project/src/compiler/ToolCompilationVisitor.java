package compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;

import compiler.Operator.OperandException;
import compiler.Scope.UnknownFunctionException;
import compiler.Scope.UnknownVariableException;
import generated.*;
import generated.ToolParser.CodeContext;
import generated.ToolParser.ExprContext;
import generated.ToolParser.ParameterContext;
import generated.ToolParser.ProductContext;

public class ToolCompilationVisitor extends ToolBaseVisitor<String> {

	private String applicationName = "Default";
	private Scope currentScope = new Scope(null, this.applicationName);
	private final static String seperator = "#";
	
	@SuppressWarnings("serial")
	private Map<String,String> reservedFunctions = new HashMap<String,String>(){{
		put("return","return");
		put("sprich","getstatic java/lang/System/out Ljava/io/PrintStream;"+"\n"+"invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
	}};

	@Override
	public String visitStringFactorString(
			@NotNull ToolParser.StringFactorStringContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitBooleanFactorString(
			@NotNull ToolParser.BooleanFactorStringContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitAssignTo(@NotNull ToolParser.AssignToContext ctx) {
		//TODO: Bring it to work
		try {
			final String loadValue = visit(ctx.value);
	
			return loadValue + "\n" + this.currentScope.getVarStoreInstruction(ctx.variableName.getText()) + "\n";
		} catch (UnknownVariableException e) {
			System.err.println("Unknown variable name: "+ctx.variableName.getText());
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitExprBoolean(@NotNull ToolParser.ExprBooleanContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitIntegerFactorFunctionCall(
			@NotNull ToolParser.IntegerFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override 
	public String visitBooleanFactorFunctionCall(
			@NotNull ToolParser.BooleanFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitVariableName(@NotNull ToolParser.VariableNameContext ctx) {
		return ctx.name.getText();
	}

	@Override
	public String visitCodeFunctionCall(
			@NotNull ToolParser.CodeFunctionCallContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitCodeAssignment(
			@NotNull ToolParser.CodeAssignmentContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitCodeVariableDefinition(
			@NotNull ToolParser.CodeVariableDefinitionContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitStringFactorParenthesis(
			@NotNull ToolParser.StringFactorParenthesisContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitWhile(@NotNull ToolParser.WhileContext ctx) {
		String cond = visit(ctx.condition);
		String code = "";
		if(ctx.instructions != null){
			for( ToolParser.CodeContext instr : ctx.instructions){
				code += visit(instr);
			}
		}
		String complete = cond+"\n";
		try {
			complete += Operator.OP_EQ.compileOperator(new Datatype[]{Datatype.TYPE_BOOL, Datatype.TYPE_BOOL})+"\n";
		} catch (OperandException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		//TODO: label generation with scope
		final String safeBegin = LabelCounter.createSafeName("begin_code");
		final String safeEnd = LabelCounter.createSafeName("end_code");
		complete += "ifnonnull "+ safeBegin +"\n";
		complete += "goto "+ safeEnd +"\n";
		complete += safeBegin + ":"+"\n";
		complete += code;
		complete += safeEnd+":"+"\n";
		return complete;
	}

	@Override
	public String visitFunctionCall(@NotNull ToolParser.FunctionCallContext ctx) {
		if(reservedFunctions.containsKey(ctx.fn_name.getText())){
			return reservedFunctions.get(ctx.fn_name.getText())+"\n";
		}
		else {
			String invocation = "invokevirtual "+this.applicationName+"/"+ctx.fn_name.getText();
			Function called;
			try {
				called = this.currentScope.getFun(ctx.fn_name.getText());

				invocation += called.getDescriptor() + "\n";
				
				if(ctx.parameters != null){
					invocation = visit(ctx.parameters) + "\n" + invocation;
				}
				
				return invocation;
				
			} catch (UnknownFunctionException e) {
				System.err.println("Unknown function: "+ctx.fn_name.getText());
				System.exit(-1);
				return "";
			}	
		}
	}

	@Override
	public String visitCodeControllStructure(
			@NotNull ToolParser.CodeControllStructureContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBooleanFactorParenthesis(
			@NotNull ToolParser.BooleanFactorParenthesisContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitIf(@NotNull ToolParser.IfContext ctx) {
		String cond = visit(ctx.if_condition);

				
		
		String instructions = "";
		for(CodeContext cc:ctx.if_instructions)
		{
			instructions += visit(cc);
		}
		
		
		return visitChildren(ctx);
	}

	@Override
	public String visitFunctionDefinitionParameters(
			@NotNull ToolParser.FunctionDefinitionParametersContext ctx) {
				
		//Split param string (name:type)
		String param = visit(ctx.param);		
		
		if(ctx.remainder != null)
		{
			for(ParameterContext ec : ctx.remainder) {					
				param += ","+visit(ec);	
			}
		}

		return param;
	}

	@Override
	public String visitExprInteger(@NotNull ToolParser.ExprIntegerContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitExprVariableName(
			@NotNull ToolParser.ExprVariableNameContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitExprFunctionName(
			@NotNull ToolParser.ExprFunctionNameContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitProductCalc(@NotNull ToolParser.ProductCalcContext ctx) {
		String result = "";
		String left = visit(ctx.int_factor());
		result += left;
		
		if(ctx.operator != null && ctx.right != null)
		{
			int i = 0;
			for(ProductContext pc : ctx.right)
			{
				System.err.println("op statement");
				result += ctx.operator.get(i).getText();
				result += visit(pc);
				i++;
			}
		}
		
		//System.out.println("Productcalculation: "+result);
		return result;
	}

	@Override
	public String visitFunctionDataType(
			@NotNull ToolParser.FunctionDataTypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBooleanFactorInverted(
			@NotNull ToolParser.BooleanFactorInvertedContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitStringExpression(
			@NotNull ToolParser.StringExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitIntegerFactorVariableName(@NotNull ToolParser.IntegerFactorVariableNameContext ctx) {
		try {
			return this.currentScope.getVarLoadInstruction(ctx.factor.getText())+"\n";
		} catch (UnknownVariableException e) {
			System.err.println("Unknown variable name: "+ctx.factor.getText());
			System.exit(-1);
			return "";
		}
	}
	
	@Override
	protected String aggregateResult(String aggregate, String nextResult) {
		String result = "";
		if(aggregate != null){
			result += aggregate + "\n";
		}
		if(nextResult != null){
			result += nextResult + "\n";
		}
		return result;
	}

	@Override
	public String visitVariableDefinition(
			@NotNull ToolParser.VariableDefinitionContext ctx) {
		String value = "";
		if(ctx.value != null){
			value = visit(ctx.value);
		}
		
		final Datatype type = Datatype.resolveType(ctx.type.getText());
		
		int newId = currentScope.defineVar(ctx.variableName.getText(), type);
		
		String definition = "";
		if(currentScope.isRoot()){
			definition = ".field static " + ctx.variableName.getText() + " " + type.getJasminType() + "\n";
			if(value != null){
				definition += ToolCompilationVisitor.seperator;
			}
		}
		
		definition += "ldc "+value+"\n";
		try {
			definition += currentScope.getVarStoreInstruction(ctx.variableName.getText());
		} catch (UnknownVariableException e) {
			System.err.println("Definition of variable failed: "+ctx.variableName.getText());
			System.exit(-1);
		}
		return definition;
	}

	@Override
	public String visitParameterDefinition(
			@NotNull ToolParser.ParameterDefinitionContext ctx) {
		//System.out.println("Parameter: "+ctx.name.getText()+ToolCompilationVisitor.seperator+ctx.type.getText());
		return ctx.name.getText()+ToolCompilationVisitor.seperator+ctx.type.getText();
	}

	@Override
	public String visitBooleanFactorBoolean(
			@NotNull ToolParser.BooleanFactorBooleanContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitBooleanFactorInt(
			@NotNull ToolParser.BooleanFactorIntContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitDoWhile(@NotNull ToolParser.DoWhileContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitMainFunction(@NotNull ToolParser.MainFunctionContext ctx) {
		this.currentScope = new Scope(currentScope, this.applicationName);
        String mainStuff = ".method public static main([Ljava/lang/String;)V\n.limit stack 100\n";
        for (ToolParser.CodeContext c : ctx.instructions) {
            mainStuff += visit(c);
        }
        mainStuff += "\nreturn\n.end method";
        this.currentScope = this.currentScope.getParent();
		return mainStuff;
	}

	@Override
	public String visitExprString(@NotNull ToolParser.ExprStringContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBooleanFactorVariableName(
			@NotNull ToolParser.BooleanFactorVariableNameContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitIntegerFactor(
			@NotNull ToolParser.IntegerFactorContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitDataType(@NotNull ToolParser.DataTypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFunctionDefinition(
			@NotNull ToolParser.FunctionDefinitionContext ctx) {
		Datatype returnType = Datatype.resolveType(ctx.type.getText());
		String functionName = ctx.fn_name.getText();
		
		String[] parameters;
		LinkedList<String> paramNames= new LinkedList<>();
		LinkedList<Datatype> paramTypes= new LinkedList<>();
		
		if(ctx.parameter_list != null)
		{
			parameters = visit(ctx.parameter_list).split(",");
			
			for(String param : parameters)
			{
				String[] t = param.split(ToolCompilationVisitor.seperator);
				
				paramNames.add(t[0]);
				paramTypes.add(Datatype.resolveType(t[1]));
			}
		}						
		
		Function function = new Function(functionName, returnType, paramNames, paramTypes);
		currentScope.defineFun(functionName, function);
		
		currentScope = new Scope(currentScope, this.applicationName);
		String code = "";
		if(ctx.instructions.size()>0){
			for(ToolParser.CodeContext cctx : ctx.instructions){
				code += visit(cctx)+"\n";
			}
		}
		currentScope = currentScope.getParent();
		return function.createFunctionStatement(code);
	}

	@Override
	public String visitIntegerExpression(
			@NotNull ToolParser.IntegerExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFunctionCallParameters(
			@NotNull ToolParser.FunctionCallParametersContext ctx) {	
		
		//Split param string (name:type)
		String param = visit(ctx.param);		

		if(ctx.remainder != null)
		{
			for(ExprContext ec : ctx.remainder) {					
				param += ","+visit(ec);	
				//System.out.print("..."+visit(ec));
			}
		}
		
		return param;
	}

	@Override
	public String visitBooleanExpression(
			@NotNull ToolParser.BooleanExpressionContext ctx) {
		String left = visit(ctx.left);
		String result = left;
		if(ctx.operator != null && ctx.right != null){
			Iterator<Token> op = ctx.operator.iterator();
			for(ToolParser.Bool_exprContext expr : ctx.right){
				result = Datatype.compare(result, op.next().getText(), visit(expr) );
			}
		}
		
		switch(result){
		case "_true":
			result = "1";
			break;
		default:
			result = "0";
			break;
		}
		return result;
	}

	@Override
	public String visitIntegerFactorParenthesis(
			@NotNull ToolParser.IntegerFactorParenthesisContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitStringFactorFunctionCall(
			@NotNull ToolParser.StringFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitProgram(@NotNull ToolParser.ProgramContext ctx) {
		String staticInitializerBlock = "";
		String definition = "";
		if (ctx.before != null) {
			for(ToolParser.DefContext cb : ctx.before){
				 String complete[] = visit(cb).split(ToolCompilationVisitor.seperator);
				 definition += complete[0];
				 if(complete.length == 2){
					 staticInitializerBlock += complete[1];
				 }
			}
		}
		
		if (ctx.after != null) {
			for(ToolParser.DefContext ca : ctx.after){
				 String complete[] = visit(ca).split(ToolCompilationVisitor.seperator);
				 definition += complete[0];
				 if(complete.length == 2){
					 staticInitializerBlock += complete[1];
				 }
			}
		}
		
		String result = ".class " + applicationName + "\n" + ".super java/lang/Object" + "\n" + definition + "\n";
		if(staticInitializerBlock.length()>0){
			result += ".method static public <clinit>()V" +"\n";
			result += ".limit stack 100" + "\n";
			result += staticInitializerBlock + "return " + "\n";
			result += ".end method" + "\n";
		}
		result += visit(ctx.m) + "\n";
		
		return result;
	}
}
