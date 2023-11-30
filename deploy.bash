#/bin/bash

set -e

unzip -o ./server/jvm/alpha-site-specific/build/distributions/alpha-site-specific-1.0.0-SNAPSHOT-bin.zip -d $GENESIS_HOME
unzip -o ./server/jvm/build/dependencies/genesis-distribution-*-bin.zip -d $GENESIS_HOME
unzip -o ./server/jvm/build/dependencies/auth-distribution-*-bin.zip -d $GENESIS_HOME
unzip -o ./server/jvm/alpha-distribution/build/distributions/genesisproduct-alpha-1.0.0-SNAPSHOT-bin.zip -d $GENESIS_HOME
#unzip -o ./client/web/build/distributions/web-distribution.zip -d $GENESIS_HOME

genesisInstall --ignoreHooks

echo y | remap --commit
