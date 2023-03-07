while True
do
  git pull
  mvn compile exec:java -Dexec.mainClass="javadb.networking.DBServer"
done