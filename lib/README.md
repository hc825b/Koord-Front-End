# Generate local Maven repository from jar file

These local Maven repositories are generated from given Jar files using the
following command under **root directory of the project**.

```
$ mvn install:install-file -Dfile=<path/to/name.jar> -DgroupId=lib \
-DartifactId=<name> -Dversion=0.1 -Dpackaging=jar -DlocalRepositoryPath=.
```
