#!/bin/bash

set -e

./gradlew :genesisproduct-alpha:alpha-eventhandler:assemble

cp server/jvm/alpha-eventhandler/build/libs/alpha-eventhandler-*.jar $GENESIS_HOME/alpha/bin

ls -lrt $GENESIS_HOME/alpha/bin