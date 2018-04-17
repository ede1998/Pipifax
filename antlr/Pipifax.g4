grammar Pipifax;
prog: includedecl* (funcdecl | vardecl | struct)*;
includedecl: '<' 'include' STRING '>';
funcdecl: 'func' ID '('parameterlist')'type? block; 
vardecl: 'var' ID type ('=' expr)? ';'?;
struct: 'struct' ID '{' memberdecl+ '}';
memberdecl: ID type';'?;
type: 'int' # IntType
      | 'double' # DoubleType
      | 'string' # StringType
      | '['INT']' type # ArrayType
      | ID # CustomType
      ;
parameterlist: (parameter (',' parameter)*)?;
parameter: ID parameter_type;
parameter_type: type # TypeParameter
      | '*' type # ReferenceParameter
      | '*' '['']' type # ReferenceArrayParameter
      ;
block: '{' (vardecl | statement)* '}';
statements: statement # StatementSingle
      | block # StatementBlock
      ;

statement: assignment ';'? # StatementAssignment
      | ifstmt # StatementIf
      | whilestmt # StatementWhile
      | funccall ';'? # StatementCall
      | forstmt # StatementFor
      | switchstmt # StatementSwitch
      ;
ifstmt: 'if' expr statements elsestmt?;
elsestmt: 'else' statements;
whilestmt: 'while' expr statements;
forstmt: 'for' '(' (initassign = assignment)? ';' expr ';' (loopedassign = assignment)? ')' statements;
switchstmt: 'switch' expr '{' casestmt* defaultstmt?'}';
casestmt: 'case' expr statements;
defaultstmt: 'default' statements;
assignment: lvalue '=' expr # Assign
      | lvalue '+=' expr # AdditionAssign
      | lvalue '-=' expr # SubtractionAssign
      | lvalue '*=' expr # MultiplicationAssign
      | lvalue '/=' expr # DivisionAssign
      | lvalue '&=' expr # AndAssign
      | lvalue '|=' expr # OrAssign
      | lvalue '%=' expr # ModuloAssign
      | lvalue '...=' expr # StringAssign
      ;
lvalue: ID # VarAccess 
      | lvalue '[' expr ']' # ArrayAccess
      | lvalue '.' ID # StructAccess
      | funccall # FunctionAccess
      ; 
expr: INT # IntLiteral
      | DOUBLE # DoubleLiteral
      | STRING # StringLiteral
      | lvalue # LValueExpression
      | '(' expr ')' # Parentheses
      | '-' expr # Negation
      | INTCASTOP expr # IntCast
      | DOUBLECASTOP expr # DoubleCast
      | expr '*' expr # Multiplication
      | expr '/' expr # Division
      | expr '%' expr # Modulo
      | expr '+' expr # Addition
      | expr '-' expr # Subtraction
      | expr '...' expr # StringConcat
      | expr '<' expr # Less
      | expr '>' expr # Greater
      | expr '<=' expr # LessOrEquals
      | expr '>=' expr # GreaterOrEquals
      | expr '==' expr # Equals
      | expr '!=' expr # NotEquals
      | expr '<=>' expr # StringCompare
      | '!' expr # Not
      | expr '&&' expr # And
      | expr '||' expr # Or
      ;
funccall: ID '(' (expr (',' expr)*)? ')';

NEWLINE : [\r\n]+ -> skip;
ENDOFFILE : EOF -> skip;
COMMENT : '#' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;
WS : [ \r\t\n]+ -> skip ;
ID : LETTER (LETTER|'0'..'9')* ;
DOUBLECASTOP: '(' WS* 'double' WS* ')';
INTCASTOP: '(' WS* 'int' WS* ')';
fragment LETTER : [a-zA-Z_] ;
INT     : [0-9]+ ;
DOUBLE : INT ('.'INT?)?('e'[+-]?INT)?;
STRING : '"'~["]*'"';
