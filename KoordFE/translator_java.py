"""Translate Koord to Java for simulation"""

from codegen import *
import parser
from parser import myparser
from typechecker import TypeInfoVisitor


class ToJavaTranslator(object):
    """Implement translation to Java"""

    def __init__(self, code):
        self.pgm = myparser().parse(code)

        self.wnum = parser.wnum
        self.symtab = parser.symtab

    def generate_app(self):
        # visitor = TypeInfoVisitor()
        # visitor.traverseProgram(self.pgm)

        return codeGen(self.pgm, 0, self.symtab, self.wnum)

    def generate_main(self):
        return mainCodeGen(self.pgm.name, self.pgm.name + "Drawer")

    def generate_drawer(self):
        return drawCodeGen(self.pgm.name)

    def generate_symtab(self):
        return str(self.symtab)
