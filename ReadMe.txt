Run the Program by:
    sh run.sh [ InputSize ]

Description:
    This bash script will test all my sorting algorithms and output information to info{i}[_Inputsize].txt, and sorted array to order{i}[_InputSize].txt, where i is the algorithm's index. The [_Inputsize] suffix will be generated when InputSize is given.
    
    InputSize: An optional number. If omited, the origin input of "random.txt" is used. Else we will use random generated number array which size is InputSize 

Eg:
    sh run.sh // Origin input
    sh run.sh 10000000 // Input size of 1e7
