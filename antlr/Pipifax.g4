grammar Pipifax;
prog:	funcdecl
      | vardecl
      | prog prog
      | EOF
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

block: '{' (vardecl | statement)* '}' NEWLINE*;

statement: assignment
      | ifstmt
      | whilestmt
      | funccall
      ;
ifstmt: 'if' expr block elsestmt?;
elsestmt: 'else' block;
whilestmt: 'while' expr block;
assignment: lvalue '=' expr ';'?;
lvalue: ID
      | ID ('[' expr ']')+;
expr: INT
      | DOUBLE
      | STRING
      | ID
      | funccall
      | ID ('[' expr ']')+
      | '(' expr ')'
      | expr '+' expr
      | expr '-' expr
      | expr '*' expr
      | expr '/' expr
      | expr '&&' expr
      | expr '||' expr
      | expr '<' expr
      | expr '>' expr
      | expr '<=' expr
      | expr '>=' expr
      | expr '==' expr
      | expr '!=' expr
      | '!' expr
      | '-' expr
      | expr '<=>' expr
      ; 
funccall: ID '(' (expr (',' expr)*)? ')';

NEWLINE : [\r\n]+ ;
COMMENT : '#' ~[\r\n]* '\r'? '\n' -> skip ;
WS : [ \r\t\n]+ -> skip ;
ID : LETTER (LETTER|'0'..'9')* ;
fragment LETTER : [a-zA-Z_] ;
INT     : [0-9]+ ;
DOUBLE : INT ('.'INT)?('e'[+-]?INT)?;
STRING : '"'~["]*'"';
