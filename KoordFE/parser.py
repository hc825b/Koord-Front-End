#!/usr/bin/env python

import sys
from scanner import *
from ast import *
from codegen import *
from symtab import *

wnum = 0  # TODO remove global variables
symtab = []


def p_program(p):
    '''program : agent modules awdecls ardecls locdecls init events
    '''
    p[0] = pgmAst(p[1], p[2], p[3], p[4], p[5], p[6], p[7])


def p_agent(p):
    '''agent : AGENT CID NL'''
    p[0] = p[2]


def p_modules(p):
    '''modules : module modules
               | module
    '''
    mlist = [p[1]]
    if len(p) == 3:
        mlist += p[2]
    p[0] = mlist


def p_module(p):
    '''module : USING MODULE CID COLON NL INDENT actuatordecls sensordecls DEDENT'''
    for decl in p[7] + p[8]:
        entry = mkEntry(decl)
        entry.set_module(p[2])
        global symtab
        symtab.append(entry)

    p[0] = moduleAst(p[3], p[7], p[8])


def p_actuatordecls(p):
    '''actuatordecls : ACTUATORS COLON NL INDENT decls DEDENT
                     | ACTUATORS COLON NL INDENT passdecls DEDENT
    '''
    p[0] = p[5]


def p_sensordecls(p):
    '''sensordecls : SENSORS COLON NL INDENT decls DEDENT
                   | SENSORS COLON NL INDENT passdecls DEDENT
    '''
    p[0] = p[5]


def p_awdecls(p):
    '''awdecls : ALLWRITE COLON NL INDENT decls DEDENT
               | ALLWRITE COLON NL INDENT passdecls DEDENT'''
    # print(p[5])
    for decl in p[5]:
        # print(decl)
        decl.set_scope(MULTI_WRITER)
        global symtab
        symtab.append(mkEntry(decl))
    p[0] = p[5]


def p_ardecls(p):
    '''ardecls : ALLREAD COLON NL INDENT rvdecls DEDENT
               | ALLREAD COLON NL INDENT passdecls DEDENT'''
    # print(p[5])
    for decl in p[5]:
        # print(decl)
        decl.set_scope(MULTI_READER)
        global symtab
        symtab.append(mkEntry(decl))
    p[0] = p[5]


def p_passdecls(p):
    '''passdecls : PASS NL'''
    p[0] = []


def p_locdecls(p):
    '''locdecls : LOCAL COLON NL INDENT decls DEDENT
                | LOCAL COLON NL INDENT passdecls DEDENT'''
    for decl in p[5]:
        global symtab
        symtab.append(mkEntry(decl))
    p[0] = p[5]


def p_decls_empty(p):
    '''decls : empty'''
    p[0] = []


def p_decls_nonempty(p):
    '''decls : decl decls'''
    p[0] = [p[1]] + p[2]


def p_decl(p):
    '''decl : type varname NL'''
    p[0] = declAst(p[1], p[2])


def p_decl_init(p):
    '''decl : type varname ASGN exp NL'''
    p[0] = (declAst(p[1], p[2], p[4]))


def p_decl_map(p):
    '''decl : mapdecl NL'''
    p[0] = (p[1])


def p_mapdecl(p):
    '''mapdecl : MAP LT type COMMA type GT varname
    '''
    p[0] = []


def p_rvdecls(p):
    '''rvdecls : rvdecl rvdecls
               | empty
    '''
    p[0] = []


def p_rvdecl(p):
    '''rvdecl : type varname LBRACE owner RBRACE NL
              | type varname LBRACE owner RBRACE ASGN num NL

    '''
    p[0] = []


def p_owner(p):
    '''owner : TIMES
             | INUM'''
    p[0] = []


def p_funccall(p):
    '''funccall : varname LPAR args RPAR'''
    p[0] = funcAst(p[1], p[3])


def p_args(p):
    '''args : neargs
            | noargs
    '''
    p[0] = p[1]


def p_noargs(p):
    '''noargs : empty'''
    p[0] = []


def p_neargs(p):
    '''neargs : exp
              | exp COMMA neargs
    '''
    alist = []
    alist.append(p[1])
    if len(p) > 2:
        alist += p[3]
    p[0] = alist


def p_varnames(p):
    '''varnames : varname
                | varname COMMA varnames
    '''
    if len(p) is 2:
        p[0] = [p[1]]

    else:
        vlist = []
        vlist.append(p[1])
        vlist += p[3]
        p[0] = vlist


def p_type(p):
    '''type : numtype
            | uncertaintype
            | STRING
    '''
    p[0] = p[1]

    # print(p[0])


def p_numtype(p):
    '''numtype : INT
               | FLOAT
               | IPOS
               | BOOLEAN
    '''
    p[0] = p[1]


def p_uncertaintype(p):
    ''' uncertaintype : UNCERTAIN LT numtype GT'''
    # TODO construct AST with uncertain information
    p[0] = "u_" + p[3]


def p_init_block(p):
    '''init : INIT COLON NL INDENT stmts DEDENT'''
    p[0] = p[5]


def p_init_empty(p):
    '''init : empty'''
    p[0] = []


def p_events(p):
    '''events : event events
              | empty '''
    elist = []
    if len(p) == 3:
        elist.append(p[1])
        elist += p[2]
    p[0] = elist


def p_event(p):
    '''event : LID COLON NL INDENT PRE COLON cond NL effblock DEDENT'''
    p[0] = eventAst(p[1], p[7], p[9])


def p_effblock_stmt(p):
    '''effblock : EFF COLON stmt'''
    p[0] = [p[3]]  # List with only one element


def p_effblock_stmts(p):
    '''effblock : EFF COLON NL INDENT stmts DEDENT'''
    p[0] = p[5]


def p_cond(p):
    '''cond : logic_exp'''
    p[0] = conditionAst(p[1])


def p_stmts(p):
    '''stmts : stmt stmts
             | empty'''
    slist = []
    if len(p) > 2:
        slist.append(p[1])
        slist += p[2]
    p[0] = slist


def p_stmt(p):
    '''stmt : asgn
            | passstmt
            | funccall NL
            | modulefunccall NL
            | ATOMIC COLON NL INDENT stmts DEDENT
            | IF cond COLON NL INDENT stmts DEDENT elseblock
    '''
    if len(p) <= 3:
        p[0] = p[1]
    elif len(p) == 7:
        global wnum
        p[0] = atomicAst(wnum, p[5])
        wnum += 1
    else:
        p[0] = iteAst(p[2], p[6], p[8])


def p_modulefunccall(p):
    '''modulefunccall : CID LPAR args RPAR '''
    p[0] = mfAst(p[3])


def p_elseblock(p):
    '''elseblock : ELSE COLON NL INDENT stmts DEDENT'''
    p[0] = p[5]


def p_passstmt(p):
    '''passstmt : PASS NL'''
    p[0] = passAst()


def p_asgn(p):
    '''asgn : varname ASGN exp NL
    '''
    # print(asgnAst(p[1],p[3]))
    p[0] = asgnAst(p[1], p[3])


precedence = (
    ('left', 'OR'),
    ('left', 'AND'),
    ('right', 'NOT'),
    ('nonassoc', 'EQ', 'NEQ', 'GEQ', 'LEQ', 'GT', 'LT'),
    ('left', 'PLUS', 'MINUS'),
    ('left', 'TIMES', 'BY')
)


def p_exp(p):
    '''exp : logic_exp'''
    p[0] = p[1]


def p_logic_exp_1(p):
    '''logic_exp : rel_exp'''
    p[0] = p[1]


def p_logic_exp_2(p):
    '''logic_exp : NOT logic_exp'''
    p[0] = exprAst('logic', p[2], None, p[1])


def p_logic_exp_3(p):
    '''logic_exp : logic_exp AND logic_exp
                 | logic_exp OR logic_exp
    '''
    p[0] = exprAst('logic', p[1], p[3], p[2])


def p_rel_exp_1(p):
    '''rel_exp : arith_exp'''
    p[0] = p[1]


def p_rel_exp_2(p):
    '''rel_exp : arith_exp relop arith_exp'''
    p[0] = exprAst('rel', p[1], p[3], p[2])


def p_arith_exp_1(p):
    '''arith_exp : unary_exp'''
    p[0] = p[1]


def p_arith_exp_2(p):
    '''arith_exp : arith_exp PLUS arith_exp
                 | arith_exp TIMES arith_exp
                 | arith_exp MINUS arith_exp
                 | arith_exp BY arith_exp
    '''
    p[0] = exprAst('arith', p[1], p[3], p[2])


def p_unary_exp_1(p):
    '''unary_exp : primary_exp'''
    p[0] = p[1]


def p_unary_exp_2(p):
    '''unary_exp : PLUS unary_exp
                 | MINUS unary_exp
    '''
    p[0] = exprAst('unary', p[2], None, p[1])


def p_primary_exp_1(p):
    '''primary_exp : varname
                   | bval
                   | num
                   | funccall
    '''
    p[0] = p[1]


def p_primary_exp_2(p):
    '''primary_exp : LPAR exp RPAR'''
    p[0] = p[2]


def p_bval(p):
    '''bval : TRUE
           | FALSE
    '''
    p[0] = exprAst('bval', p[1])


def p_num_integer(p):
    '''num : INUM'''
    p[0] = exprAst('inum', p[1])


def p_num_floating(p):
    '''num : FNUM'''
    p[0] = exprAst('fnum', p[1])


def p_varname(p):
    '''varname : LID

    '''
    p[0] = exprAst('var', p[1])


def p_relop(p):
    '''relop : EQ
             | NEQ
             | GEQ
             | LEQ
             | GT
             | LT
    '''
    p[0] = p[1]


def p_empty(p):
    '''empty :'''
    pass


def p_error(p):
    print("syntax error in input on line ", p.lineno, p.type)


class myparser(object):
    def __init__(self, lexer=None):
        self.lexer = IndentLexer()
        self.parser = yacc.yacc()

    def parse(self, code):
        self.lexer.input(code)
        result = self.parser.parse(lexer=self.lexer)
        return result


class mycompiler(object):
    def __init__(self):
        self.parser = myparser()

    def compile(self, filename):
        code = open(filename, "r").read()
        pgm = (self.parser.parse(code))

        print(pgm.name)
        appname = str(pgm.name) + "App.java"
        f = open(appname, "w")
        global wnum
        global symtab
        f.write(codeGen(pgm, 0, symtab, wnum))
        f.close()
        f = open("Main.java", 'w')
        f.write(mainCodeGen(str(pgm.name), str(pgm.name) + "Drawer"))
        f.close()
        drawfile = str(pgm.name) + "Drawer.java"
        f = open(drawfile, 'w')
        f.write(drawCodeGen(str(pgm.name)))
        f.close()
        f = open(str(pgm.name) + ".symtab", "w")
        global symtab
        f.write(str(symtab))
        f.close()


if __name__ == "__main__":
    filename = sys.argv[1]
    mycompiler().compile(filename)
