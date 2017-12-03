#!/usr/bin/env python

import argparse
import re
import os
import sys

from KoordFE.translator_java import ToJavaTranslator


def main(argv):
    cmd_parser = argparse.ArgumentParser(
        description="Generate Java code for simulation")
    cmd_parser.add_argument('krd_file', type=open,
                            metavar="<app.krd>", help="Koord program code")
    args = cmd_parser.parse_args()

    krd_compiler = ToJavaTranslator(args.krd_file.read())

    pkg_name = krd_compiler.get_package_name()

    project_root_path = os.path.dirname(os.path.realpath(__file__))
    simulate_path = "src/main/java/testSim/apps/" + pkg_name  # relative
    simulate_path = os.path.join(project_root_path, simulate_path)  # absolute

    if not os.path.exists(simulate_path):
        os.mkdir(simulate_path, 0775)

    # Write App.java file
    app_java_name = krd_compiler.get_app_filename()
    app_java_path = os.path.join(simulate_path, app_java_name)
    with open(app_java_path, "w") as app_java_file:
        app_java_file.write(krd_compiler.generate_app())

    # Write Drawer.java file
    drawer_java_name = krd_compiler.get_drawer_filename()
    drawer_java_path = os.path.join(simulate_path, drawer_java_name)
    with open(drawer_java_path, "w") as drawer_java_file:
        drawer_java_file.write(krd_compiler.generate_drawer())

    # Write Main.java file
    main_java_name = krd_compiler.get_main_filename()
    main_java_path = os.path.join(simulate_path, main_java_name)
    with open(main_java_path, "w") as main_java_file:
        main_java_file.write(krd_compiler.generate_main())


if __name__ == "__main__":
    main(sys.argv)
