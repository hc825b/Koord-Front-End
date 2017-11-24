"""Abstract base class for AST and visitor"""

import abc


class AstBase(object):
    """Abstract base class for AST"""
    __metaclass__ = abc.ABCMeta

    def __init__(self):
        pass

    @abc.abstractmethod
    def __repr__(self):
        pass

    @abc.abstractmethod
    def get_type(self):
        """Get type of current AST"""
        pass

    @abc.abstractmethod
    def accept(self, visitor):
        """Call visit function for this node according to type"""
        pass


class AstVisitorBase(object):
    """Abstract base class to traverse AST"""
    __metaclass__ = abc.ABCMeta

    def __init__(self):
        pass

    @abc.abstractmethod
    def traverseProgram(self, pgm):
        pass

    @abc.abstractmethod
    def traverseModule(self, module):
        pass

    @abc.abstractmethod
    def traverseDeclaration(self, decl):
        pass

    @abc.abstractmethod
    def traverseEvent(self, event):
        pass

    @abc.abstractmethod
    def traversePassStmt(self, stmt):
        pass

    @abc.abstractmethod
    def traverseOrExpr(self, expr):
        pass

    @abc.abstractmethod
    def traverseAndExpr(self, expr):
        pass

    @abc.abstractmethod
    def traverseNotExpr(self, expr):
        pass

class PostOrderVisitor(AstVisitorBase):
    """Visit AST in post-order"""

    def __init__(self):
        super(PostOrderVisitor, self).__init__()

    def traverseProgram(self, pgm):
        if any((not m.accept(self)) for m in pgm.modules):
            return False
        if any((not d.accept(self)) for d in pgm.awdecls):
            return False
        if any((not d.accept(self)) for d in pgm.ardecls):
            return False
        if any((not d.accept(self)) for d in pgm.locdecls):
            return False
        if any((not s.accept(self)) for s in pgm.init):
            return False
        if any((not e.accept(self)) for e in pgm.events):
            return False
        return self.visitProgram(pgm)

    def traverseModule(self, module):
        if any((not d.accept(self)) for d in module.actuatordecls):
            return False
        if any((not d.accept(self)) for d in module.sensordecls):
            return False
        return self.visitModule(module)

    def traverseDeclaration(self, decl):
        return True # TODO

    def traverseEvent(self, event):
        if not event.pre.accept(self):
            return False
        if any((not s.accept(self)) for s in event.eff):
            return False
        return True

    def traversePassStmt(self, stmt):
        return True

    def traverseOrExpr(self, expr):
        if not expr.lexp.accept(self):
            return False
        if not expr.rexp.accept(self):
            return False
        return self.visitOrExpr(expr)

    def traverseAndExpr(self, expr):
        if not expr.lexp.accept(self):
            return False
        if not expr.rexp.accept(self):
            return False
        return self.visitAndExpr(expr)

    def traverseNotExpr(self, expr):
        if not expr.lexp.accept(self):
            return False
        return self.visitNotExpr(expr)

    def visitProgram(self, pgm):
        return True

    def visitModule(self, module):
        return True

    def visitOrExpr(self, expr):
        return True

    def visitAndExpr(self, expr):
        return True

    def visitNotExpr(self, expr):
        return True
