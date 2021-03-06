grammar Tool;

start: (before += def)* m=main (after += def)* #program;

main: MAIN L_PAREN R_PAREN L_C_BRACE (instructions += code)* R_C_BRACE#mainFunction;

def: variableDef=var_def SEMICOLON #defVariableDef
	| functionDef=func_def #defFunctionDef
	;

code: instruction=var_def SEMICOLON #codeVariableDefinition
	| instruction=assignment SEMICOLON #codeAssignment
	| fn_name=NAME L_PAREN parameters=func_call_params?  R_PAREN SEMICOLON #codeFunctionCall
	| instruction=contr_structure #codeControllStructure
	;

contr_structure: IF L_PAREN if_condition=bool_expr R_PAREN L_C_BRACE (if_instructions += code)* R_C_BRACE ( elifs += elif_structure )* elseStructure=else_structure? #if
		| DO_WHILE L_C_BRACE (instructions += code)* R_C_BRACE L_PAREN condition R_PAREN SEMICOLON #doWhile
		| WHILE L_PAREN while_condition=bool_expr R_PAREN L_C_BRACE (instructions += code)* R_C_BRACE #while
		;

condition: bool_expr #cond;

elif_structure: ELSEIF L_PAREN elif_condition=bool_expr R_PAREN L_C_BRACE (elif_instructions += code)* R_C_BRACE #elseIf;

else_structure: ELSE L_C_BRACE (else_instructions += code)* R_C_BRACE #else;

assignment: variableName=NAME ASSIGN_TO value=expr #assignTo;

expr: e=var_name #exprVariableName
	| e=func_call #exprFunctionName
	| e=int_expr #exprInteger
	| e=str_expr #exprString
	| e=bool_expr #exprBoolean
	; 

var_name: name=NAME #variableName;

int_expr: left=int_expr '+' right=product #integerAddition
	| left=int_expr '-' right=product #integerSubtraction
	| left=product #integerProduct
	;

product: left=product '*' right=int_factor #integerMultiplication
	| left=product '/' right=int_factor #integerDivision
	| left=int_factor #integerProductFactor
	;

int_factor: L_PAREN factor=int_expr R_PAREN #integerFactorParenthesis
	| factor=func_call #integerFactorFunctionCall
	| factor=var_name #integerFactorVariableName
	| factor=NUMBER #integerFactor
	;
	
bool_expr: left=int_expr '<' right=int_expr #booleanLower
	| left=int_expr '>' right=int_expr #booleanGreater
	| left=int_expr '<=' right=int_expr #booleanLE
	| left=int_expr '>=' right=int_expr #booleanGE
	| left=bool_factor '==' right=bool_expr #booleanEqual
	| left=bool_factor '!=' right=bool_expr #booleanUnequal
	| left=bool_factor '||' right=bool_expr #booleanOr
	| left=bool_factor '&&' right=bool_expr #booleanAnd
	| left=bool_factor #booleanFactor
	;

bool_factor: L_PAREN factor=bool_expr R_PAREN #booleanFactorParenthesis
		| INVERT factor=bool_expr #booleanFactorInverted
		| factor=func_call #booleanFactorFunctionCall
		| factor=int_expr #booleanFactorInt
		| factor=str_expr #booleanFactorString
		| factor=var_name #booleanFactorVariableName
		| factor=BOOLEAN #booleanFactorBoolean;

str_expr: left=str_factor ( CAT right+=str_factor)* #stringExpression;

str_factor: L_PAREN factor=str_expr R_PAREN #stringFactorParenthesis
		| factor=func_call #stringFactorFunctionCall
		| factor=var_name #stringFactorVariableName
		| factor=STRING #stringFactorString;

var_def: type=var_data_type  variableName=NAME (ASSIGN_TO value=expr)? #variableDefinition; 

var_data_type: type=TYPE_INT #dataType
		| type=TYPE_BOOL #dataType
		| type=TYPE_STRING #dataType
		;

func_data_type:	var_data_type #functionDataType 
		| type=TYPE_VOID #functionDataType
		;

func_def: DEFINE type=func_data_type fn_name=NAME L_PAREN parameter_list=func_def_params? R_PAREN L_C_BRACE (instructions += code)* R_C_BRACE #functionDefinition;

func_def_params: param=parameter ( COMMA remainder+=parameter )*#functionDefinitionParameters;

func_call: fn_name=NAME L_PAREN parameters=func_call_params?  R_PAREN #functionCall;

func_call_params: param=expr ( COMMA remainder+=expr )* #functionCallParameters;

parameter: type=var_data_type name=NAME#parameterDefinition;

L_PAREN: '(';
R_PAREN: ')';
R_C_BRACE: '}';
L_C_BRACE: '{';
SEMICOLON: ';';
COMMA: ',';
ASSIGN_TO: '=';
CAT: '+';

INVERT: '!';

MAIN: '_haupt';

IF: '_wenn';
ELSEIF: '_oder_dies';
ELSE: '_sonst';

DO_WHILE: '_solange_bis';
WHILE: '_schleife';

DEFINE: '_definiere';

TYPE_INT: 'int';
TYPE_BOOL: 'bool';
TYPE_STRING: 'str';

/* for functions */
TYPE_VOID: 'leer';

NAME: [a-zA-Z]+;
STRING: '"' .*? '"'; 
BOOLEAN: '_true' | '_false';
NUMBER: [1-9][0-9]* | '0';

COMMENT: '/*' .*? '*/' -> skip;
WS: [ \r\t\n]+ -> skip;
