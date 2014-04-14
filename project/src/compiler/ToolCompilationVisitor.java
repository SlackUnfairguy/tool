package compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;

import compiler.Operator.OperandException;
import compiler.Scope.RedefinitionException;
import compiler.Scope.UnknownNameException;
import generated.*;
import generated.ToolParser.CodeContext;
import generated.ToolParser.ExprContext;
import generated.ToolParser.ParameterContext;
import generated.ToolParser.ProductContext;

public class ToolCompilationVisitor extends ToolBaseVisitor<String> {

	private TokenStream tokenStream;
	private String applicationName;
	private Scope currentScope;
	private Map<String, String> reservedFunctions;

	private final static String seperator = "#";

	public ToolCompilationVisitor(TokenStream pTS) {
		super();
		this.tokenStream = pTS;
		this.applicationName = "Default";
		this.currentScope = new Scope(null, this.applicationName);
		this.reservedFunctions = new HashMap<String, String>() {
			private static final long serialVersionUID = -1000729011127015471L;
			{
				put("return", "return");
				put("sprich", "getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" + "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
				put("toStr", "getstatic java/lang/Integer Ljava/lang/Integer;" + "\n" + "invokevirtual java/lang/Integer/toStr(Ljava/lang/Integer;)L");
			}
		};
	}

	private void printError(String pError, ParserRuleContext ctx) {
		System.err.printf("ERROR (line %d): %s" + System.lineSeparator(), tokenStream.get(ctx.getSourceInterval().a).getLine(), pError);
	}

	@Override
	public String visitStringFactorString(@NotNull ToolParser.StringFactorStringContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitBooleanFactorString(@NotNull ToolParser.BooleanFactorStringContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitAssignTo(@NotNull ToolParser.AssignToContext ctx) {
		// TODO: Bring it to work
		try {
			final String loadValue = visit(ctx.value);

			return loadValue + "\n" + this.currentScope.getVarStoreInstruction(ctx.variableName.getText()) + "\n";
		} catch (UnknownNameException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitExprBoolean(@NotNull ToolParser.ExprBooleanContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitIntegerFactorFunctionCall(@NotNull ToolParser.IntegerFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitBooleanFactorFunctionCall(@NotNull ToolParser.BooleanFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitVariableName(@NotNull ToolParser.VariableNameContext ctx) {
		return ctx.name.getText();
	}

	@Override
	public String visitCodeFunctionCall(@NotNull ToolParser.CodeFunctionCallContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitCodeAssignment(@NotNull ToolParser.CodeAssignmentContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitCodeVariableDefinition(@NotNull ToolParser.CodeVariableDefinitionContext ctx) {
		return visit(ctx.instruction);
	}

	@Override
	public String visitStringFactorParenthesis(@NotNull ToolParser.StringFactorParenthesisContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitWhile(@NotNull ToolParser.WhileContext ctx) {
		final String cond = visit(ctx.condition);
		final String safeBegin = LabelCounter.createSafeName("begin_code");
		final String safeEnd = LabelCounter.createSafeName("end_code");

		String code = "";
		if (ctx.instructions != null) {
			for (ToolParser.CodeContext instr : ctx.instructions) {
				code += visit(instr);
			}
		}

		String complete = "";
		complete += safeBegin + ":" + "\n";
		complete += cond + "\n";
		try {
			complete += Operator.OP_EQ.compileOperator(new Datatype[] { Datatype.TYPE_BOOL, Datatype.TYPE_BOOL }) + "\n";
		} catch (OperandException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
		}

		complete += "ifeq " + safeEnd + "\n";
		complete += code;
		complete += "goto " + safeBegin + "\n";
		complete += safeEnd + ":" + "\n";
		return complete;
	}

	@Override
	public String visitFunctionCall(@NotNull ToolParser.FunctionCallContext ctx) {
		if (reservedFunctions.containsKey(ctx.fn_name.getText())) {

			String functionCall = reservedFunctions.get(ctx.fn_name.getText()) + "\n";
			String parameters = null;

			if (ctx.parameters != null) {
				parameters = visit(ctx.parameters) + "\n";
			}

			return parameters + functionCall;

		} else {
			String invocation = "invokevirtual " + this.applicationName + "/" + ctx.fn_name.getText();
			Function called;
			try {
				called = this.currentScope.getFun(ctx.fn_name.getText());

				invocation += called.getDescriptor() + "\n";

				if (ctx.parameters != null) {
					invocation = visit(ctx.parameters) + "\n" + invocation;
				}

				return invocation;

			} catch (UnknownNameException e) {
				printError(e.getMessage(), ctx);
				System.exit(-1);
				return "";
			}
		}
	}

	@Override
	public String visitCodeControllStructure(@NotNull ToolParser.CodeControllStructureContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBooleanFactorParenthesis(@NotNull ToolParser.BooleanFactorParenthesisContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitIf(@NotNull ToolParser.IfContext ctx) {
		String cond = visit(ctx.if_condition);

		String instructions = "";
		if (ctx.if_instructions != null) {
			for (CodeContext cc : ctx.if_instructions) {
				instructions += visit(cc);
			}
		}

		return cond + instructions + "\n";
	}

	@Override
	public String visitElseIf(@NotNull ToolParser.ElseIfContext ctx) {
		String cond = visit(ctx.elif_condition);
		String instructions = "";
		if (ctx.elif_instructions != null) {
			for (ToolParser.CodeContext cc : ctx.elif_instructions) {
				instructions += visit(cc);
			}
		}
		return cond + instructions + "\n";
	}

	@Override
	public String visitFunctionDefinitionParameters(@NotNull ToolParser.FunctionDefinitionParametersContext ctx) {

		// Split param string (name:type)
		String param = visit(ctx.param);

		if (ctx.remainder != null) {
			for (ParameterContext ec : ctx.remainder) {
				param += "," + visit(ec);
			}
		}

		return param;
	}

	@Override
	public String visitExprInteger(@NotNull ToolParser.ExprIntegerContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitExprVariableName(@NotNull ToolParser.ExprVariableNameContext ctx) {
		return visit(ctx.e);
	}

	@Override
	public String visitExprFunctionName(@NotNull ToolParser.ExprFunctionNameContext ctx) {
		return visit(ctx.e);
	}

	/*
	 * @Override public String visitProductCalc(@NotNull
	 * ToolParser.ProductCalcContext ctx) { String result = ""; String left =
	 * visit(ctx.int_factor()); result += left;
	 * 
	 * if (ctx.operator != null && ctx.right != null) { int i = 0; for
	 * (ProductContext pc : ctx.right) { System.err.println("op statement");
	 * result += ctx.operator.get(i).getText(); result += visit(pc); i++; } }
	 * 
	 * // System.out.println("Productcalculation: "+result); return result; }
	 */

	@Override
	public String visitIntegerAddition(@NotNull ToolParser.IntegerAdditionContext ctx) {
		try {
			return visit(ctx.left) + "\n" + Operator.OP_ADD.compileOperator(new Datatype[] { Datatype.TYPE_INT, Datatype.TYPE_INT }) + visit(ctx.right);
		} catch (OperandException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitIntegerSubtraction(@NotNull ToolParser.IntegerSubtractionContext ctx) {
		try {
			return visit(ctx.left) + "\n" + Operator.OP_SUB.compileOperator(new Datatype[] { Datatype.TYPE_INT, Datatype.TYPE_INT }) + visit(ctx.right);
		} catch (OperandException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitIntegerProductFactor(@NotNull ToolParser.IntegerProductFactorContext ctx) {
		return visit(ctx.left);
	}

	@Override
	public String visitIntegerMultiplication(@NotNull ToolParser.IntegerMultiplicationContext ctx) {
		try {
			return visit(ctx.left) + "\n" + Operator.OP_MUL.compileOperator(new Datatype[] { Datatype.TYPE_INT, Datatype.TYPE_INT }) + visit(ctx.right);
		} catch (OperandException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitIntegerDivision(@NotNull ToolParser.IntegerDivisionContext ctx) {
		try {
			return visit(ctx.left) + "\n" + Operator.OP_DIV.compileOperator(new Datatype[] { Datatype.TYPE_INT, Datatype.TYPE_INT }) + visit(ctx.right);
		} catch (OperandException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitIntegerFactorVariableName(@NotNull ToolParser.IntegerFactorVariableNameContext ctx) {
		try {
			return this.currentScope.getVarLoadInstruction(ctx.factor.getText()) + "\n";
		} catch (UnknownNameException e) {
			printError(e.getMessage(), ctx);
			System.exit(-1);
			return "";
		}
	}

	@Override
	public String visitIntegerFactor(@NotNull ToolParser.IntegerFactorContext ctx) {
		return "ldc "+ctx.factor.getText()+"\n";
	}

	@Override
	public String visitFunctionDataType(@NotNull ToolParser.FunctionDataTypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBooleanFactorInverted(@NotNull ToolParser.BooleanFactorInvertedContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitStringExpression(@NotNull ToolParser.StringExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	protected String aggregateResult(String aggregate, String nextResult) {
		String result = "";
		if (aggregate != null) {
			result += aggregate + "\n";
		}
		if (nextResult != null) {
			result += nextResult + "\n";
		}
		return result;
	}

	@Override
	public String visitVariableDefinition(@NotNull ToolParser.VariableDefinitionContext ctx) {
		String value = "";
		if (ctx.value != null) {
			value = visit(ctx.value);
		}

		final Datatype type = Datatype.resolveType(ctx.type.getText());
		String definition = "";
		try {
			currentScope.defineVar(ctx.variableName.getText(), type);
			if (currentScope.isRoot()) {
				definition = ".field static " + ctx.variableName.getText() + " " + type.getJasminType() + "\n";
				if (value != null) {
					definition += ToolCompilationVisitor.seperator;
				}
			}

			definition += "ldc " + value + "\n";
			try {
				definition += currentScope.getVarStoreInstruction(ctx.variableName.getText());
			} catch (UnknownNameException e) {
				printError(e.getMessage(), ctx);
				System.exit(-1);
			}
		} catch (RedefinitionException e1) {
			printError(e1.getMessage(), ctx);
		}
		return definition;
	}

	@Override
	public String visitParameterDefinition(@NotNull ToolParser.ParameterDefinitionContext ctx) {
		// System.out.println("Parameter: "+ctx.name.getText()+ToolCompilationVisitor.seperator+ctx.type.getText());
		return ctx.name.getText() + ToolCompilationVisitor.seperator + ctx.type.getText();
	}

	@Override
	public String visitBooleanFactorBoolean(@NotNull ToolParser.BooleanFactorBooleanContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitBooleanFactorInt(@NotNull ToolParser.BooleanFactorIntContext ctx) {
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
	public String visitBooleanFactorVariableName(@NotNull ToolParser.BooleanFactorVariableNameContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitDataType(@NotNull ToolParser.DataTypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFunctionDefinition(@NotNull ToolParser.FunctionDefinitionContext ctx) {
		Datatype returnType = Datatype.resolveType(ctx.type.getText());
		String functionName = ctx.fn_name.getText();

		String[] parameters;
		LinkedList<String> paramNames = new LinkedList<>();
		LinkedList<Datatype> paramTypes = new LinkedList<>();

		if (ctx.parameter_list != null) {
			parameters = visit(ctx.parameter_list).split(",");

			for (String param : parameters) {
				String[] t = param.split(ToolCompilationVisitor.seperator);

				paramNames.add(t[0]);
				paramTypes.add(Datatype.resolveType(t[1]));
			}
		}

		Function function = new Function(functionName, returnType, paramNames, paramTypes);
		String code = "";
		try {
			currentScope.defineFun(functionName, function);
			currentScope = new Scope(currentScope, this.applicationName);

			if (ctx.instructions.size() > 0) {
				for (ToolParser.CodeContext cctx : ctx.instructions) {
					code += visit(cctx) + "\n";
				}
			}
			currentScope = currentScope.getParent();
		} catch (RedefinitionException e) {
			printError(e.getMessage(), ctx);
		}

		return function.createFunctionStatement(code);
	}

	@Override
	public String visitFunctionCallParameters(@NotNull ToolParser.FunctionCallParametersContext ctx) {

		// Split param string (name:type)
		String param = visit(ctx.param);

		if (ctx.remainder != null) {
			for (ExprContext ec : ctx.remainder) {
				param += "," + visit(ec);
			}
		}

		return param;
	}

	/*
	 * @Override public String visitBooleanExpression(@NotNull
	 * ToolParser.BooleanExpressionContext ctx) { String left = visit(ctx.left);
	 * String result = left; if (ctx.operator != null && ctx.right != null) {
	 * Iterator<Token> op = ctx.operator.iterator(); for
	 * (ToolParser.Bool_exprContext expr : ctx.right) { result =
	 * Datatype.compare(result, op.next().getText(), visit(expr)); } }
	 * 
	 * switch (result) { case "_true": result = "1"; break; default: result =
	 * "0"; break; } return result; }
	 */

	@Override
	public String visitIntegerFactorParenthesis(@NotNull ToolParser.IntegerFactorParenthesisContext ctx) {
		return ctx.factor.getText();
	}

	@Override
	public String visitStringFactorFunctionCall(@NotNull ToolParser.StringFactorFunctionCallContext ctx) {
		return visit(ctx.factor);
	}

	@Override
	public String visitProgram(@NotNull ToolParser.ProgramContext ctx) {
		String staticInitializerBlock = "";
		String definition = "";
		if (ctx.before != null) {
			for (ToolParser.DefContext cb : ctx.before) {
				String complete[] = visit(cb).split(ToolCompilationVisitor.seperator);
				definition += complete[0];
				if (complete.length == 2) {
					staticInitializerBlock += complete[1];
				}
			}
		}

		if (ctx.after != null) {
			for (ToolParser.DefContext ca : ctx.after) {
				String complete[] = visit(ca).split(ToolCompilationVisitor.seperator);
				definition += complete[0];
				if (complete.length == 2) {
					staticInitializerBlock += complete[1];
				}
			}
		}

		String result = ".class " + applicationName + "\n" + ".super java/lang/Object" + "\n" + definition + "\n";
		if (staticInitializerBlock.length() > 0) {
			result += ".method static public <clinit>()V" + "\n";
			result += ".limit stack 100" + "\n";
			result += staticInitializerBlock + "return " + "\n";
			result += ".end method" + "\n";
		}
		result += visit(ctx.m) + "\n";

		return result;
	}
}
