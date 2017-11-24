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

class PostOrderVisitor(AstVisitorBase):
    """Visit AST in post-order"""

    def __init__(self):
        super(PostOrderVisitor, self).__init__()

    def traverseProgram(self, pgm):
        if any((m.accept(self) == False) for m in pgm.modules):
            return False
        if any((d.accept(self) == False) for d in pgm.awdecls):
            return False
        if any((d.accept(self) == False) for d in pgm.ardecls):
            return False
        if any((d.accept(self) == False) for d in pgm.locdecls):
            return False
        if any((s.accept(self) == False) for s in pgm.init):
            return False
        if any((e.accept(self) == False) for e in pgm.events):
            return False
        return self.visitProgram(pgm)

    def traverseModule(self, module):
        # TODO
        return self.visitModule(module)

    def traverseDeclaration(self, decl):
        return True # TODO

    def traverseEvent(self, event):
        return True # TODO

    def traversePassStmt(self, stmt):
        return True

    def visitProgram(self, pgm):
        return True

    def visitModule(self, module):
        return True
