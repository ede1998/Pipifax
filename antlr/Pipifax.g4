grammar Pipifax;
prog: (funcdecl | vardecl)*
     ;
funcdecl: 'func' ID '('parameterlist')'type? block; 
vardecl: 'var' ID type ';'?;
type: 'int' # IntType
      | 'double' # DoubleType
      | 'string' # StringType
      | '['INT']' type # ArrayType
      ;
parameterlist: (parameter (',' parameter)*)?;
parameter: ID parameter_type;
parameter_type: type # TypeParameter
      | '*' type # ReferenceParameter
      | '*' '['']' type # ReferenceArrayParameter
      ;

block: '{' (vardecl | statement)* '}';
statements: statement 
      | block
      ;

statement: assignment ';'?
      | ifstmt
      | whilestmt
      | funccall ';'?
      | forstmt
      | switchstmt
      ;
ifstmt: 'if' expr statements elsestmt?;
elsestmt: 'else' statements;
whilestmt: 'while' expr statements;
forstmt: 'for' '(' (initassign = assignment)? ';' expr ';' (loopedassign = assignment)? ')' statements;
switchstmt: 'switch' expr '{' casestmt* defaultstmt?'}';
casestmt: 'case' expr statements;
defaultstmt: 'default' statements;
assignment: lvalue '=' expr;
lvalue: ID
      | ID ('[' expr ']')+;
expr: INT # IntLiteral
      | DOUBLE # DoubleLiteral
      | STRING # StringLiteral
      | funccall # Call
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
DOUBLE : INT ('.'INT)?('e'[+-]?INT)?;
STRING : '"'~["]*'"';
