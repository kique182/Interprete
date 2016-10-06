package paquete;
import java_cup.runtime.*;
import java.io.Reader;

%%

%class AnalizadorLexico

%line
%column
%cup


FECHA = ("30"|"31"|[1-2][0-9]|0[1-9])\/(0?[1-9]|1[012])\/[0-9]{4}
NUMERO = "0"|[1-9]|[1-9]+[0-9]+
CADENA = [a-zA-Z\_]+
COMILLA = \"
ESPACIO_BLANCO = [ \n\r\t]

%%

//Operadores de álgebra relacional
SEL         { return new java_cup.runtime.Symbol(sym.SEL,new Token(yytext(),yyline, yycolumn,"SEL","Relacion"));}
PRO         { return new java_cup.runtime.Symbol(sym.PRO,new Token(yytext(),yyline, yycolumn,"PRO","Relacion"));}
UNI         { return new java_cup.runtime.Symbol(sym.UNI,new Token(yytext(),yyline, yycolumn,"UNI","Relacion"));}
DIF         { return new java_cup.runtime.Symbol(sym.DIF,new Token(yytext(),yyline, yycolumn,"DIF","Relacion"));}
PROC        { return new java_cup.runtime.Symbol(sym.PROC,new Token(yytext(),yyline, yycolumn,"PROC","Relacion"));}
INT         { return new java_cup.runtime.Symbol(sym.INT,new Token(yytext(),yyline, yycolumn,"INT","Relacion"));}

//Opreradores Lógico
AND         { return new java_cup.runtime.Symbol(sym.AND,new Token(yytext(),yyline, yycolumn,"AND","booleano"));}
NOT         { return new java_cup.runtime.Symbol(sym.NOT,new Token(yytext(),yyline, yycolumn,"NOT","booleano"));}
OR          { return new java_cup.runtime.Symbol(sym.OR,new Token(yytext(),yyline, yycolumn,"OR","booleano"));}

//Opreradores Comparación
"="         { return new java_cup.runtime.Symbol(sym.EQ,new Token(yytext(),yyline, yycolumn,"EQ","booleano"));}
"!="        { return new java_cup.runtime.Symbol(sym.DIFERENTE,new Token(yytext(),yyline, yycolumn,"DIFERENTE","booleano"));}
">"         { return new java_cup.runtime.Symbol(sym.MAYOR,new Token(yytext(),yyline, yycolumn,"MAYOR","booleano"));}
"<"         { return new java_cup.runtime.Symbol(sym.MENOR,new Token(yytext(),yyline, yycolumn,"MENOR","booleano"));}
">="        { return new java_cup.runtime.Symbol(sym.MAYORIGUAL,new Token(yytext(),yyline, yycolumn,"MAYORIGUAL","booleano"));}
"<="        { return new java_cup.runtime.Symbol(sym.MENORIGUAL,new Token(yytext(),yyline, yycolumn,"MENORIGUAL","booleano"));}

//extras
"("         { return new java_cup.runtime.Symbol(sym.IPAREN,new Token(yytext(),yyline, yycolumn,"IPAREN"));}
")"         { return new java_cup.runtime.Symbol(sym.DPAREN,new Token(yytext(),yyline, yycolumn,"DPAREN"));}
","         { return new java_cup.runtime.Symbol(sym.COMA,new Token(yytext(),yyline, yycolumn,"COMA"));}
"."         { return new java_cup.runtime.Symbol(sym.PUNTO,new Token(yytext(),yyline, yycolumn,"PUNTO"));}


//Operadores matemáticos
"+"         { return new java_cup.runtime.Symbol(sym.SUMA,new Token(yytext(),yyline, yycolumn,"SUMA","numero"));}
"-"         { return new java_cup.runtime.Symbol(sym.RESTA,new Token(yytext(),yyline, yycolumn,"RESTA","numero"));}
"*"         { return new java_cup.runtime.Symbol(sym.MULTI,new Token(yytext(),yyline, yycolumn,"MULTI","numero"));}
"/"         { return new java_cup.runtime.Symbol(sym.DIVI,new Token(yytext(),yyline, yycolumn,"DIVI","numero"));}


//Otros
{FECHA}     { return new java_cup.runtime.Symbol(sym.FECHA,new Token(yytext(),yyline, yycolumn,"FECHA","fecha"));}
{NUMERO}  { return new java_cup.runtime.Symbol(sym.NUMERO,new Token(yytext(),yyline, yycolumn,"NUMERO","numero"));}
{CADENA}   { return new java_cup.runtime.Symbol(sym.CADENA,new Token(yytext(),yyline, yycolumn,"CADENA"));}
{COMILLA}   { return new java_cup.runtime.Symbol(sym.COMILLA,new Token(yytext(),yyline, yycolumn,"COMILLA"));}
{ESPACIO_BLANCO}    {}
.           {  System.err.println("Caracter ilegal, no se reconoce '"+yytext()+"' en ["+yyline+","+yycolumn+"]");}