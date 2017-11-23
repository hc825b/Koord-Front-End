#!/usr/bin/env python

import argparse
import os
import sys

from KoordFE.parser import mycompiler


def main(argv):
    cmd_parser = argparse.ArgumentParser(
        description="Generate Java code for simulation")
    cmd_parser.add_argument('krd_file', type=open,
                            metavar="<app.krd>", help="Koord program code")
    args = cmd_parser.parse_args()

    krd_compiler = mycompiler()
    krd_compiler.parse(args.krd_file.read())

    # TODO + "/src/main/java/testSim/"
    dirPath = os.path.dirname(os.path.realpath(__file__))
    app_java_name = "App.java"
#    with open(app_java_name, "w") as app_java_file:
#        app_java_file.write(krd_compiler.generate_app())

    print(krd_compiler.generate_main())
    print(krd_compiler.generate_app())
    print(krd_compiler.generate_drawer())
    print(krd_compiler.generate_symtab())


if __name__ == "__main__":
    main(sys.argv)
