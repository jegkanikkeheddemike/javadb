while true
do
  git reset --hard
  git pull
  /usr/bin/mvn compile exec:java -Dexec.mainClass="javadb.networking.DBServer"
done