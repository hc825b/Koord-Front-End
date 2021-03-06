"""Abstract Syntax Tree implementation"""

from ast_base import AstBase
from symtab import *
LOCAL = -1
MULTI_WRITER = -2
MULTI_READER = -3
CONTROLLER = -4

pgmtype = 'pgm'
decltype = 'decl'
rvdecltype = 'rvdecl'
inittype = 'init'
evnttype = 'evnt'
moduletype = 'mdl'
functype = 'func'
atomictype = 'atom'


class pgmAst(AstBase):
    """AST for Program"""

    def __init__(self, name, modules, awdecls, ardecls, locdecls, init, events):
        self.name = name
        self.modules = modules
        self.awdecls = awdecls
        self.ardecls = ardecls
        self.locdecls = locdecls
        self.init = init
        self.events = events

    def __repr__(self):
        name_str = self.name
        module_str = ""
        for module in self.modules:
            module_str += str(module) + "\n"
        awdecl_str = "allwrite:\n"
        for decl in self.awdecls:
            awdecl_str += str(decl) + "\n"
        ardecl_str = "allread:\n"
        for decl in self.ardecls:
            ardecl_str += str(decl) + "\n"
        locdecl_str = "local:\n"
        for decl in self.locdecls:
            locdecl_str += str(decl) + "\n"
        event_str = ""
        for event in self.events:
            event_str += str(event) + "\n"

        return (name_str + "\n" + module_str + "\n" + awdecl_str + "\n" + ardecl_str + "\n" + locdecl_str + str(self.init) + event_str)

    def getflags(self):
        """Get list of module names and check if shared variables exists"""
        modules = []
        hasShared = False
        if self.modules == []:
            pass
        else:
            for module in self.modules:
                modules.append(module.getName())
        if self.awdecls == []:
            pass
        else:
            hasShared = True
        if self.ardecls == []:
            pass
        else:
            hasShared = True
        return (modules, hasShared)

    def get_type(self):
        return pgmtype

    def accept(self, visitor):
        return visitor.traverseProgram(self)


class mfAst(AstBase):
    def __init__(self, modfunc, args):
        self.modfunc = modfunc
        self.args = args

    def __repr__(self):
        # modname = "s"#str(self.modfunc)[(str(self.modfunc)).index('.'):-1]
        m = str(self.modfunc) + "("
        if len(self.args) == 0:
            m += ")"
        else:
            for i in range(len(self.args) - 1):
                m += str(self.args[i]) + ", "
            m += str(self.args[-1]) + ")"
        return(m)

    def get_type(self):
        return moduletype  # TODO Why returning moduletype?

    def accept(self, visitor):
        raise NotImplementedError # TODO


class funcAst(AstBase):
    def __init__(self, name, args):
        self.name = name
        self.args = args

    def __repr__(self):
        m = str(self.name) + "("
        if len(self.args) == 0:
            m += ")"
        else:
            for i in range(len(self.args) - 1):
                m += str(self.args[i]) + ", "
            m += str(self.args[-1]) + ")"
        return(m)

    def get_type(self):
        return functype

    def accept(self, visitor):
        raise NotImplementedError # TODO


class moduleAst(AstBase):
    def __init__(self, name, actuatordecls, sensordecls):
        self.name = name
        self.actuatordecls = actuatordecls
        self.sensordecls = sensordecls

    def __repr__(self):
        name_str = str(self.name)
        actuator_str = "actuators :" + "\n"
        for decl in self.actuatordecls:
            actuator_str += str(decl) + "\n"
        sensor_str = "sensors :" + "\n"
        for decl in self.sensordecls:
            sensor_str += str(decl) + "\n"
        return name_str + ":\n" + actuator_str + sensor_str

    def getName(self):
        return self.name

    def get_type(self):
        return moduletype

    def accept(self, visitor):
        return visitor.traverseModule(self)


class initAst(AstBase):
    def __init__(self, stmts):
        self.stmts = stmts

    def __repr__(self):
        init_str = "init:\n"
        for stmt in self.stmts:
            init_str += str(stmt) + "\n"
        return init_str

    def get_type(self):
        return inittype

    def accept(self, visitor):
        raise NotImplementedError # TODO


class stmtAst(AstBase):
    def __init__(self, stype):
        self.stype = stype

    def get_type(self):
        return self.stype

    def accept(self, visitor):
        raise NotImplementedError


class atomicAst(stmtAst):
    def __init__(self, wnum, stmts):
        self.stype = atomictype
        self.wnum = wnum
        self.stmts = stmts

    def __repr__(self):
        s = "atomic:"
        for stmt in self.stmts:
            s += str(stmt)
        return s

    def get_type(self):
        return atomictype

    def accept(self, visitor):
        raise NotImplementedError # TODO


class asgnAst(stmtAst):
    def __init__(self, lvar, rexp):
        self.stype = 'asgn'
        self.lvar = lvar
        self.rexp = rexp

    def __repr__(self):
        return str(self.lvar) + " = " + str(self.rexp)

    def accept(self, visitor):
        raise NotImplementedError # TODO


class passAst(stmtAst):
    def __init__(self):
        pass

    def __repr__(self):
        return ";"

    def get_type(self):
        return 'pass'

    def accept(self, visitor):
        return visitor.traversePassStmt(self)


class iteAst(stmtAst):
    def __init__(self, cond, t, e):
        self.stype = 'ite'
        self.cond = cond
        self.t = t
        self.e = e

    def __repr__(self):
        s = "if " + str(self.cond) + "\n"
        for stmt in self.t:
            s += str(stmt)
        s += "else"
        for stmt in self.e:
            s += str(stmt)
        return s

    def accept(self, visitor):
        raise NotImplementedError # TODO


class eventAst(AstBase):
    def __init__(self, name, pre, eff):
        self.name = name
        self.pre = pre
        self.eff = eff

    def __repr__(self):

        pre_str = "pre:\n" + str(self.pre) + "\n"
        eff_str = "eff:\n"
        for stmt in self.eff:
            eff_str += str(stmt) + "\n"
        return (self.name + ":\n" + pre_str + eff_str)

    def get_type(self):
        return evnttype

    def accept(self, visitor):
        return visitor.traverseEvent(self)


class conditionAst(AstBase):
    def __init__(self, logicexp):
        self.exp = logicexp

    def __repr__(self):
        return "(" + str(self.exp) + ")"

    def get_type(self):
        return 'condition'

    def accept(self, visitor):
        raise NotImplementedError # TODO


class exprAst(AstBase):
    def __init__(self, etype, lexp, rexp=None, op=None):
        self.etype = etype
        self.lexp = lexp
        self.rexp = rexp
        self.op = op

    def __repr__(self):
        if self.op is None:
            return str(self.lexp)
        if self.rexp is None:
            return str(self.lexp) + str(self.op)
        if self.lexp is None:
            return str(self.op) + str(self.rexp)
        else:
            return "( " + str(self.lexp) + " " + str(self.op) + " " + str(self.rexp) + " )"

    def get_type(self):
        return self.etype

    def accept(self, visitor):
        raise NotImplementedError # TODO


class declAst(AstBase):
    def __init__(self, dtype, varname, value=None, scope=LOCAL):
        self.scope = scope
        self.dtype = dtype
        self.varname = varname
        self.value = value

    def __repr__(self):
        dtype_str = str(self.dtype) + " "
        varname_str = str(self.varname) + " "
        value_str = ""
        if self.value is not None:
            value_str = "= " + str(self.value)
        return (dtype_str + varname_str + value_str)

    def get_scope(self):
        return self.scope

    def set_scope(self, scope):
        self.scope = scope

    def get_type(self):
        return decltype

    def accept(self, visitor):
        return visitor.traverseDeclaration(self)


class mapAst(AstBase):
    def __init__(self, t1, t2, varname, scope=LOCAL):
        self.scope = scope
        self.t1 = t1
        self.t2 = t2
        self.varname = varname
        self.scope = scope

    def __repr__(self):
        dtype_str = "final Map <" + str(self.t1) + " , "
        dtype_str += str(self.t2) + "> "
        varname_str = str(self.varname) + " "
        value_str = "= " + "new HashMap<" + \
            str(self.t1) + "," + str(self.t2) + ">()"
        return (dtype_str + varname_str + value_str)

    def get_scope(self):
        return self.scope

    def set_scope(self, scope):
        self.scope = scope

    def get_type(self):
        return 'map'

    def accept(self, visitor):
        raise NotImplementedError # TODO


class rvdeclAst(AstBase):

    def __init__(self, dtype, varname, owner, value=None, scope=LOCAL):
        self.scope = scope
        self.dtype = dtype
        self.varname = varname
        self.owner = owner
        self.value = value

    def __repr__(self):
        dtype_str = str(self.dtype) + " "
        varname_str = str(self.varname) + " "
        owner_str = "[" + str(self.owner) + "] "
        value_str = ""
        if self.value is not None:
            value_str = "= " + str(self.value)
        return (dtype_str + varname_str + owner_str + value_str)

    def get_scope(self):
        return self.scope

    def set_scope(self, scope):
        self.scope = scope

    def get_type(self):
        return rvdecltype

    def accept(self, visitor):
        raise NotImplementedError # TODO


def mkEntry(decl):
    return symEntry(decl.dtype, decl.varname, decl.scope)


def isAst(ast):
    try:
        t = ast.get_type()
        return True
    except:
        return False
