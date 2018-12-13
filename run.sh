test -e build/ || mkdir build/
javac -d build $(find src -name "*java")
java -ea -classpath build Main
rm -rf build/
