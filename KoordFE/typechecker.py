"""Type Checking for Uncertain<T>"""

from ast_base import AstVisitorBase


class TypeInfoVisitor(AstVisitorBase):
    """AST Visitor to collect type info for each expression"""

    def __init__(self):
        pass
        super.__init__()

    def visitProgram(self, pgm):
        pass  # TODO

    def visitStatement(self, stmt):
        raise NotImplementedError
