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

statement: assignment ';'? # AssignmentStatement
      | ifstmt # IfStatement
      | whilestmt # WhileStatement
      | funccall ';'? # FunctionCallStatement
      ;
ifstmt: 'if' expr block elsestmt?;
elsestmt: 'else' block;
whilestmt: 'while' expr block;
assignment: lvalue '=' expr;
lvalue: ID
      | ID ('[' expr ']')+;
expr: INT # IntLiteral
      | DOUBLE # DoubleLiteral
      | STRING # StringLiteral
      | funccall # Call
      | lvalue # LValueExpression
      | '(' expr ')' # Parentheses 
      | expr '+' expr # Addition
      | expr '-' expr # Subtraction
      | expr '*' expr # Multiplication
      | expr '/' expr # Division
      | expr '&&' expr # And
      | expr '||' expr # Or
      | expr '<' expr # Less
      | expr '>' expr # Greater
      | expr '<=' expr # LessOrEquals
      | expr '>=' expr # GreaterOrEquals
      | expr '==' expr # Equals
      | expr '!=' expr # NotEquals
      | '!' expr # Not
      | '-' expr # Negation
      | expr '<=>' expr # StringCompare
      ; 
funccall: ID '(' (expr (',' expr)*)? ')';

NEWLINE : [\r\n]+ -> skip;
ENDOFFILE : EOF -> skip;
COMMENT : '#' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;
WS : [ \r\t\n]+ -> skip ;
ID : LETTER (LETTER|'0'..'9')* ;
fragment LETTER : [a-zA-Z_] ;
INT     : [0-9]+ ;
DOUBLE : INT ('.'INT)?('e'[+-]?INT)?;
STRING : '"'~["]*'"';
