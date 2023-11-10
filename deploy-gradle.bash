#!/bin/bash

set -e
trap 'stop_spinner' ERR

get_process_uptime() {
    local pid="$1"
    local start_time=`date --date="$(ps -p $pid -o lstart=)" '+%s'`
    local current_time=$(date +%s)
    local elapsed_seconds=$(( $current_time - $start_time ))
    echo "$elapsed_seconds"
}

stop_spinner() {
    local uptime_seconds=$(get_process_uptime "$spinner_pid")
    kill $spinner_pid &>/dev/null
    echo -e "\n✅ Done in ${uptime_seconds} seconds \n"
}

spinner() {
    iterations=(⠋ ⠙ ⠹ ⠸ ⠼ ⠴ ⠦ ⠧ ⠇ ⠏)
    while true; do
        for i in "${iterations[@]}"; do
            echo -ne "\r${UNDERLINE}[${NC} $i $1 ${NC}${UNDERLINE}]${NC}"
            sleep 0.1
        done
    done
}

spinner "🔨 ${BLUE}Building" &
spinner_pid=$!

./gradlew :genesisproduct-alpha:assemble

stop_spinner

spinner "🚀 ${BLUE}Deploying" &
spinner_pid=$!

echo -e "Deploying to ${GREEN}$GENESIS_HOME${NC}"

./gradlew :genesisproduct-alpha:alpha-deploy:setupEnvironment

./gradlew :genesisproduct-alpha:alpha-deploy:install-auth-distribution.zip \
 :genesisproduct-alpha:alpha-deploy:install-alpha-site-specific-1.0.0-SNAPSHOT-bin.zip-distribution.zip \
 :genesisproduct-alpha:alpha-deploy:install-genesisproduct-alpha-1.0.0-SNAPSHOT-bin.zip-distribution.zip

genesisInstall --ignoreHooks

echo y | remap --commit

stop_spinner

 # missing here ignore hooks
 # :genesisproduct-alpha:alpha-deploy:genesisInstall \ 

 # :genesisproduct-alpha:alpha-deploy:loadInitialData \
 # :genesisproduct-alpha:alpha-deploy:remap \

 # runs everything together
 # :genesisproduct-alpha:alpha-deploy:deploy-genesisproduct-alpha.zip \
 # :genesisproduct-alpha:alpha-deploy:mon  
    
