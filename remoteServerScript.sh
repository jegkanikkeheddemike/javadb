while true
do
  git reset --hard
  git pull
  mvn compile exec:java -Dexec.mainClass="javadb.networking.DBServer"
done