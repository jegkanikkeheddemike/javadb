while true
do
  git pull
  mvn compile exec:java -Dexec.mainClass="javadb.networking.DBServer"
done