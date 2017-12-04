"""Translate Koord to Java for simulation"""

from codegen import *
import parser
from parser import myparser
from typechecker import TypeInfoVisitor


class ToJavaTranslator(object):
    """Implement translation to Java"""

    def __init__(self, code):
        self.__pgm = myparser().parse(code)

        self.wnum = parser.wnum
        self.symtab = parser.symtab

    def get_program_name(self):
        return self.__pgm.name

    def get_package_name(self):
        return self.get_program_name().lower()

    def get_app_filename(self):
        return self.get_program_name() + "App.java"

    def get_drawer_filename(self):
        return self.get_program_name() + "Drawer.java"

    def get_main_filename(self):
        return "Main.java"  # XXX Java needs the filename matches class name

    def generate_app(self):
        # visitor = TypeInfoVisitor()
        # visitor.traverseProgram(self.__pgm)

        return codeGen(self.__pgm, 0, self.symtab, self.wnum)

    def generate_main(self):
        return mainCodeGen(self.__pgm.name, self.__pgm.name + "Drawer")

    def generate_drawer(self):
        return drawCodeGen(self.__pgm.name)

    def generate_symtab(self):
        return str(self.symtab)
