grammar Pipifax;
prog:	funcdecl
      | vardecl
      | prog prog
      ;
funcdecl: 'func' ID '('parameter')'type? block; 
vardecl: 'var' ID type ';'?;
type: 'int'
      | 'double'
      | 'string'
      | '['INT']' type
      ;

parameter: ID parameter_type
      | parameter ',' parameter
      ;
parameter_type: type
      | '*' type
      | '*' '['']' type
      ;

block: '{' (vardecl | statement)* '}';

statement: assignment ';'?
      | ifstmt
      | whilestmt
      | funccall ';'?
      ;
ifstmt: 'if' expr block elsestmt?;
elsestmt: 'else' block;
whilestmt: 'while' expr block;
assignment: lvalue '=' expr;
lvalue: ID
      | ID ('[' expr ']')+;
expr: INT # Literal
      | DOUBLE # Literal
      | STRING # Literal
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
COMMENT : '#' ~[\r\n]* '\r'? '\n' -> skip ;
WS : [ \r\t\n]+ -> skip ;
ID : LETTER (LETTER|'0'..'9')* ;
fragment LETTER : [a-zA-Z_] ;
INT     : [0-9]+ ;
DOUBLE : INT ('.'INT)?('e'[+-]?INT)?;
STRING : '"'~["]*'"';
