#!/bin/bash

set -e

echo -e "🔨 ${BLUE}Building it${NC}"

./gradlew :genesisproduct-alpha:assemble

echo -e "🚀 ${BLUE}Deploying to $GENESIS_HOME${NC}"

./gradlew :genesisproduct-alpha:alpha-deploy:setupEnvironment

./gradlew :genesisproduct-alpha:alpha-deploy:install-auth-distribution.zip \
 :genesisproduct-alpha:alpha-deploy:install-alpha-site-specific-1.0.0-SNAPSHOT-bin.zip-distribution.zip \
 :genesisproduct-alpha:alpha-deploy:install-genesisproduct-alpha-1.0.0-SNAPSHOT-bin.zip-distribution.zip

./gradlew :genesisproduct-alpha:alpha-deploy:mon 

 # missing here ignore hooks
 # :genesisproduct-alpha:alpha-deploy:genesisInstall \ 

 # :genesisproduct-alpha:alpha-deploy:loadInitialData \
 # :genesisproduct-alpha:alpha-deploy:remap \

 # runs everything together
 # :genesisproduct-alpha:alpha-deploy:deploy-genesisproduct-alpha.zip \
 
    
