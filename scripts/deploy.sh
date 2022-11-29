#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/buysell-0.0.1-SNAPSHOT.jar \
    root@194.38.9.29:/home

echo 'Restart server....'

ssh -i ~/.ssh/id_rsa root@194.38.9.29 <<EOF

pgrep java | xargs kill -9
nohup java -jar buysell-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'


#кто какие порты слушает sudo lsof -nP -i | grep LISTEN