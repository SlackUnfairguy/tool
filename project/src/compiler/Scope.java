package compiler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class Scope {
	
	public class UnknownVariableException extends Exception {
		private static final long serialVersionUID = 4379775796175306144L;

		public UnknownVariableException(String pMsg){
			super(pMsg);
		}
	}
	
	public class UnknownFunctionException extends Exception {
		private static final long serialVersionUID = -4601824032925547441L;
		
		public UnknownFunctionException(String pMsg){
			super(pMsg);
		}
	}
	
	private Scope parent;
	private Map<String, Variable> variables;  
	private Map<String, Function> functions;
	
	public Scope(Scope pParent){
		this.parent = pParent;
		this.variables = new HashMap<>();
		this.functions = new HashMap<>();
	}
	
	public boolean isRoot(){
		return this.parent == null;
	}

	public int defineVar(String pName, Datatype pType){
		if(!this.variables.containsKey(pName)){
			final int next = variables.size();
			variables.put(pName, new Variable(next, pType));
			return next;
		}
		else {
			System.err.println("variable redefined in same scope: " + pName);
			return -1;
		}
	}
	
	// only allow happy functions
	public boolean defineFun(String pName, Function pFunc){
		if(!this.functions.containsKey(pName)){
			functions.put(pName, pFunc);
			return true;
		}
		else {
			System.err.println("function redefined in same scope: " +  pName);
			return false;
		}
	}
	
	public Integer getVarId(String pName) throws UnknownVariableException{
		if(variables.containsKey(pName)){
			return variables.get(pName).getId();	
		}
		else {
			if(parent != null){
				return parent.getVarId(pName);
			}
			else {
				throw new UnknownVariableException("Unknown variable name: "+pName);
			}
		}
	}
	
	public String getVarLoadStatement(String pName) throws UnknownVariableException
	{
		if(variables.containsKey(pName)){
			String statement;
			
			switch(variables.get(pName).getType())
			{
				case TYPE_INT:
					statement = "iload "+ variables.get(pName).getId();
				break;
				default:
					statement = "";
				break;
			}
			return statement;
		}
		else {
			if(parent != null){
				parent.getVarId(pName);
			}
			else {
				throw new UnknownVariableException("Unknown variable name: "+pName);
			}
		}
		return "";
	}
	
	public void printInfo(){
		System.out.println("Info on "+this.toString());
		System.out.println("parent:"+this.parent);
		System.out.println("variables:"+this.variables.size());
		for(final Entry<String, Variable> currentEntry : variables.entrySet()){
			final Variable v = currentEntry.getValue();
			System.out.println(" "+currentEntry.getKey()+":"+v.getId()+","+v.getType());
		}
		System.out.println("functions:"+this.functions.size());
		for(final Entry<String,Function> currentEntry : functions.entrySet()){
			final Function f = currentEntry.getValue();
			System.out.println(" "+currentEntry.getKey()+":"+f.getDescriptor());//TODO: print code belonging to function
		}
	}

	public Function getFun(String pName) throws UnknownFunctionException {
		if(functions.containsKey(pName)){
			return functions.get(pName);
		}
		else {
			throw new UnknownFunctionException("Unknown function name: "+pName);
		}
	}
}
