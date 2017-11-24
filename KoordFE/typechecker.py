"""Type Checking for Uncertain<T>"""

from ast_base import PostOrderVisitor


class TypeInfoVisitor(PostOrderVisitor):
    """AST Visitor to collect type info for each expression"""

    def __init__(self):
        pass
        super(TypeInfoVisitor, self).__init__()

    def visitProgram(self, pgm):
        # TODO
        return True
