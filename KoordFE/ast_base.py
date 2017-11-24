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
    def visitProgram(self, pgm):
        pass

    @abc.abstractmethod
    def visitStatement(self, stmt):
        pass
