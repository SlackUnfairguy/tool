package compiler;

public enum Datatype {
	TYPE_INT("I","Integer"), TYPE_BOOL("Z","Boolean"), TYPE_STRING("Ljava/lang/String;","String"),
	TYPE_VOID("V","Void/Object"), TYPE_INVALID("�","INVALID");

	private String jasminType;
	private String naturalType;
	
	private Datatype(){
		this.jasminType="";
		this.naturalType="";
	}
	
	private Datatype(String pType, String pNatural){
		this.jasminType = pType;
		this.naturalType = pNatural;
	}
	
	public static Datatype resolveType(String pString){
		Datatype type = null;
		switch(pString){
		case "int":
			type=TYPE_INT;
			break;
		case "bool":
			type=TYPE_BOOL;
			break;
		case "str":
			type=TYPE_STRING;
			break;
		case "leer":
			type=TYPE_VOID;
			break;
		default:
			type=TYPE_INVALID;
			break;
		}
		return type;
	}

	public String getJasminType() {
		return this.jasminType;
	}
	
	public String getType(){
		return this.naturalType;
	}
}