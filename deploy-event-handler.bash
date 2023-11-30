#!/bin/bash

set -e

./gradlew :genesisproduct-alpha:alpha-eventhandler:assemble

cp server/jvm/alpha-eventhandler/build/libs/alpha-eventhandler-*.jar $GENESIS_HOME/alpha/bin
cp server/jvm/alpha-script-config/src/main/resources/scripts/alpha-eventhandler.gy $GENESIS_HOME/alpha/scripts

ls -lrt $GENESIS_HOME/alpha/bin