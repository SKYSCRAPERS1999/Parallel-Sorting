inputSize=""
if [ $# -gt 0 ]
then
    inputSize=$1
fi
test -e bin || mkdir bin
javac -d bin -sourcepath src src/Main.java 
java -classpath bin Main $inputSize
rm -r bin
