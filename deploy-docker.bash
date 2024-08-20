#!/bin/bash

set -e

./gradlew :server:alpha-app:buildImage -Pgenesis-home=/app/run -Pssh-username=genesis -Pssh-password=genesis -Pssh-port=1337 -Pssh-host=localhost -PgenesisVersion=8.0.1 -PauthVersion=8.0.0

echo -e "\n✅ Done, run with: \n"

echo "Postgres docker bridged network:"
echo "docker run --rm -it -p 9064:9064 -e GENESIS_SYSDEF_DbHost="jdbc:postgresql://postgresdb:5432/postgres?user=postgres&password=postgres" -e GENESIS_SYSDEF_DbNamespace="alphadocker" --name alpha genesis/alpha:1.0.0-SNAPSHOT /bin/bash"

echo "Postgres local network stack:"
echo "docker run --rm -it --net host -e GENESIS_SYSDEF_DbHost="jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres" -e GENESIS_SYSDEF_DbNamespace="alphadocker"  --name alpha genesis/alpha:1.0.0-SNAPSHOT /bin/bash"

echo "H2:"
echo "docker run -p 9064:9064 -e GENESIS_SYSDEF_DbHost="jdbc:h2:file:~/genesis-local-db/alpha/h2/test;DB_CLOSE_DELAY=-1;NON_KEYWORDS=VALUE,KEY;AUTO_SERVER=TRUE" --name alpha genesis/alpha:1.0.0-SNAPSHOT"
