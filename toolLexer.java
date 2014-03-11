// Generated from tool.g4 by ANTLR 4.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class toolLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__10=1, T__9=2, T__8=3, T__7=4, T__6=5, T__5=6, T__4=7, T__3=8, T__2=9, 
		T__1=10, T__0=11, L_PAREN=12, R_PAREN=13, R_C_BRACE=14, L_C_BRACE=15, 
		SEMICOLON=16, COMMA=17, ASSIGN_TO=18, CAT=19, INVERT=20, MAIN=21, IF=22, 
		ELSEIF=23, ELSE=24, DO_WHILE=25, WHILE=26, DEFINE=27, TYPE_INT=28, TYPE_BOOL=29, 
		TYPE_STRING=30, TYPE_VOID=31, NAME=32, STRING=33, BOOLEAN=34, NUMBER=35, 
		COMMENT=36, WS=37;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'||'", "'>'", "'-'", "'*'", "'/'", "'<'", "'=='", "'>='", "'!='", "'<='", 
		"'&&'", "'('", "')'", "'}'", "'{'", "';'", "','", "'='", "'+'", "'!'", 
		"'_haupt'", "'_wenn'", "'_oder_dies'", "'_sonst'", "'_solange_bis'", "'_schleife'", 
		"'_definiere'", "'int'", "'bool'", "'str'", "'leer'", "NAME", "STRING", 
		"BOOLEAN", "NUMBER", "COMMENT", "WS"
	};
	public static final String[] ruleNames = {
		"T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", 
		"T__1", "T__0", "L_PAREN", "R_PAREN", "R_C_BRACE", "L_C_BRACE", "SEMICOLON", 
		"COMMA", "ASSIGN_TO", "CAT", "INVERT", "MAIN", "IF", "ELSEIF", "ELSE", 
		"DO_WHILE", "WHILE", "DEFINE", "TYPE_INT", "TYPE_BOOL", "TYPE_STRING", 
		"TYPE_VOID", "NAME", "STRING", "BOOLEAN", "NUMBER", "COMMENT", "WS"
	};


	public toolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "tool.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 35: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 36: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1: skip();  break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\'\u0108\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37"+
		"\3 \3 \3 \3 \3 \3!\6!\u00d0\n!\r!\16!\u00d1\3\"\3\"\7\"\u00d6\n\"\f\""+
		"\16\"\u00d9\13\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\5#\u00e8\n#"+
		"\3$\3$\7$\u00ec\n$\f$\16$\u00ef\13$\3$\5$\u00f2\n$\3%\3%\3%\3%\7%\u00f8"+
		"\n%\f%\16%\u00fb\13%\3%\3%\3%\3%\3%\3&\6&\u0103\n&\r&\16&\u0104\3&\3&"+
		"\4\u00d7\u00f9\'\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23"+
		"\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1"+
		"\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67\35\19\36\1"+
		";\37\1= \1?!\1A\"\1C#\1E$\1G%\1I&\2K\'\3\3\2\6\4\2C\\c|\3\2\63;\3\2\62"+
		";\5\2\13\f\17\17\"\"\u010e\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2"+
		"\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67"+
		"\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2"+
		"\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\3M\3\2\2\2\5P\3\2\2\2"+
		"\7R\3\2\2\2\tT\3\2\2\2\13V\3\2\2\2\rX\3\2\2\2\17Z\3\2\2\2\21]\3\2\2\2"+
		"\23`\3\2\2\2\25c\3\2\2\2\27f\3\2\2\2\31i\3\2\2\2\33k\3\2\2\2\35m\3\2\2"+
		"\2\37o\3\2\2\2!q\3\2\2\2#s\3\2\2\2%u\3\2\2\2\'w\3\2\2\2)y\3\2\2\2+{\3"+
		"\2\2\2-\u0082\3\2\2\2/\u0088\3\2\2\2\61\u0093\3\2\2\2\63\u009a\3\2\2\2"+
		"\65\u00a7\3\2\2\2\67\u00b1\3\2\2\29\u00bc\3\2\2\2;\u00c0\3\2\2\2=\u00c5"+
		"\3\2\2\2?\u00c9\3\2\2\2A\u00cf\3\2\2\2C\u00d3\3\2\2\2E\u00e7\3\2\2\2G"+
		"\u00f1\3\2\2\2I\u00f3\3\2\2\2K\u0102\3\2\2\2MN\7~\2\2NO\7~\2\2O\4\3\2"+
		"\2\2PQ\7@\2\2Q\6\3\2\2\2RS\7/\2\2S\b\3\2\2\2TU\7,\2\2U\n\3\2\2\2VW\7\61"+
		"\2\2W\f\3\2\2\2XY\7>\2\2Y\16\3\2\2\2Z[\7?\2\2[\\\7?\2\2\\\20\3\2\2\2]"+
		"^\7@\2\2^_\7?\2\2_\22\3\2\2\2`a\7#\2\2ab\7?\2\2b\24\3\2\2\2cd\7>\2\2d"+
		"e\7?\2\2e\26\3\2\2\2fg\7(\2\2gh\7(\2\2h\30\3\2\2\2ij\7*\2\2j\32\3\2\2"+
		"\2kl\7+\2\2l\34\3\2\2\2mn\7\177\2\2n\36\3\2\2\2op\7}\2\2p \3\2\2\2qr\7"+
		"=\2\2r\"\3\2\2\2st\7.\2\2t$\3\2\2\2uv\7?\2\2v&\3\2\2\2wx\7-\2\2x(\3\2"+
		"\2\2yz\7#\2\2z*\3\2\2\2{|\7a\2\2|}\7j\2\2}~\7c\2\2~\177\7w\2\2\177\u0080"+
		"\7r\2\2\u0080\u0081\7v\2\2\u0081,\3\2\2\2\u0082\u0083\7a\2\2\u0083\u0084"+
		"\7y\2\2\u0084\u0085\7g\2\2\u0085\u0086\7p\2\2\u0086\u0087\7p\2\2\u0087"+
		".\3\2\2\2\u0088\u0089\7a\2\2\u0089\u008a\7q\2\2\u008a\u008b\7f\2\2\u008b"+
		"\u008c\7g\2\2\u008c\u008d\7t\2\2\u008d\u008e\7a\2\2\u008e\u008f\7f\2\2"+
		"\u008f\u0090\7k\2\2\u0090\u0091\7g\2\2\u0091\u0092\7u\2\2\u0092\60\3\2"+
		"\2\2\u0093\u0094\7a\2\2\u0094\u0095\7u\2\2\u0095\u0096\7q\2\2\u0096\u0097"+
		"\7p\2\2\u0097\u0098\7u\2\2\u0098\u0099\7v\2\2\u0099\62\3\2\2\2\u009a\u009b"+
		"\7a\2\2\u009b\u009c\7u\2\2\u009c\u009d\7q\2\2\u009d\u009e\7n\2\2\u009e"+
		"\u009f\7c\2\2\u009f\u00a0\7p\2\2\u00a0\u00a1\7i\2\2\u00a1\u00a2\7g\2\2"+
		"\u00a2\u00a3\7a\2\2\u00a3\u00a4\7d\2\2\u00a4\u00a5\7k\2\2\u00a5\u00a6"+
		"\7u\2\2\u00a6\64\3\2\2\2\u00a7\u00a8\7a\2\2\u00a8\u00a9\7u\2\2\u00a9\u00aa"+
		"\7e\2\2\u00aa\u00ab\7j\2\2\u00ab\u00ac\7n\2\2\u00ac\u00ad\7g\2\2\u00ad"+
		"\u00ae\7k\2\2\u00ae\u00af\7h\2\2\u00af\u00b0\7g\2\2\u00b0\66\3\2\2\2\u00b1"+
		"\u00b2\7a\2\2\u00b2\u00b3\7f\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7h\2\2"+
		"\u00b5\u00b6\7k\2\2\u00b6\u00b7\7p\2\2\u00b7\u00b8\7k\2\2\u00b8\u00b9"+
		"\7g\2\2\u00b9\u00ba\7t\2\2\u00ba\u00bb\7g\2\2\u00bb8\3\2\2\2\u00bc\u00bd"+
		"\7k\2\2\u00bd\u00be\7p\2\2\u00be\u00bf\7v\2\2\u00bf:\3\2\2\2\u00c0\u00c1"+
		"\7d\2\2\u00c1\u00c2\7q\2\2\u00c2\u00c3\7q\2\2\u00c3\u00c4\7n\2\2\u00c4"+
		"<\3\2\2\2\u00c5\u00c6\7u\2\2\u00c6\u00c7\7v\2\2\u00c7\u00c8\7t\2\2\u00c8"+
		">\3\2\2\2\u00c9\u00ca\7n\2\2\u00ca\u00cb\7g\2\2\u00cb\u00cc\7g\2\2\u00cc"+
		"\u00cd\7t\2\2\u00cd@\3\2\2\2\u00ce\u00d0\t\2\2\2\u00cf\u00ce\3\2\2\2\u00d0"+
		"\u00d1\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2B\3\2\2\2"+
		"\u00d3\u00d7\7$\2\2\u00d4\u00d6\13\2\2\2\u00d5\u00d4\3\2\2\2\u00d6\u00d9"+
		"\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8\u00da\3\2\2\2\u00d9"+
		"\u00d7\3\2\2\2\u00da\u00db\7$\2\2\u00dbD\3\2\2\2\u00dc\u00dd\7a\2\2\u00dd"+
		"\u00de\7v\2\2\u00de\u00df\7t\2\2\u00df\u00e0\7w\2\2\u00e0\u00e8\7g\2\2"+
		"\u00e1\u00e2\7a\2\2\u00e2\u00e3\7h\2\2\u00e3\u00e4\7c\2\2\u00e4\u00e5"+
		"\7n\2\2\u00e5\u00e6\7u\2\2\u00e6\u00e8\7g\2\2\u00e7\u00dc\3\2\2\2\u00e7"+
		"\u00e1\3\2\2\2\u00e8F\3\2\2\2\u00e9\u00ed\t\3\2\2\u00ea\u00ec\t\4\2\2"+
		"\u00eb\u00ea\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee"+
		"\3\2\2\2\u00ee\u00f2\3\2\2\2\u00ef\u00ed\3\2\2\2\u00f0\u00f2\7\62\2\2"+
		"\u00f1\u00e9\3\2\2\2\u00f1\u00f0\3\2\2\2\u00f2H\3\2\2\2\u00f3\u00f4\7"+
		"\61\2\2\u00f4\u00f5\7,\2\2\u00f5\u00f9\3\2\2\2\u00f6\u00f8\13\2\2\2\u00f7"+
		"\u00f6\3\2\2\2\u00f8\u00fb\3\2\2\2\u00f9\u00fa\3\2\2\2\u00f9\u00f7\3\2"+
		"\2\2\u00fa\u00fc\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fc\u00fd\7,\2\2\u00fd"+
		"\u00fe\7\61\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0100\b%\2\2\u0100J\3\2\2\2"+
		"\u0101\u0103\t\5\2\2\u0102\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0102"+
		"\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107\b&\3\2\u0107"+
		"L\3\2\2\2\n\2\u00d1\u00d7\u00e7\u00ed\u00f1\u00f9\u0104";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}