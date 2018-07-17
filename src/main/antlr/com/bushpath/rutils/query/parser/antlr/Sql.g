grammar Sql;

@header {
    package com.bushpath.rutils.query.parser.antlr;
}

// parser rules

select : 'select' FEATURES 'from' entity=TEXT (where)?;

where : 'where' expr ('and' expr)*;

expr : feature=TEXT opr=OPR value=NUMERIC;

// lexer rules

FEATURES : '*';

OPR : '<' | '<=' | '=' | '>'  | '>=';

TEXT : ( 'a'..'z' | 'A'..'Z' ) ( 'a'..'z' | 'A'..'Z' | '0'..'9' | '.' )+;

NUMERIC : ( '0'..'9' )+ ('.' ( '0'..'9' )+)?;

WHITESPACE : ( '\t' | ' ' )+ -> skip; 
