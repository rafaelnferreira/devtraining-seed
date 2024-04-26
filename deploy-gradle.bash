#!/bin/bash

set -e
trap 'stop_spinner' ERR

get_process_uptime() {
    local pid="$1"
    local start_time=`date -j -f "%a %d %b %T %Y" "$(ps -p $pid -o lstart=)" '+%s'`
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
 :genesisproduct-alpha:alpha-deploy:install-alpha-site-specific-1.0.0-SNAPSHOT-bin-distribution.zip \
 :genesisproduct-alpha:alpha-deploy:install-genesisproduct-alpha-1.0.0-SNAPSHOT-bin-distribution.zip

# Patching scripts for OSX
patch -ruN -d $GENESIS_HOME < OSX_Changes_to_unix_scripts_.patch

# Sync custom scripts
#rsync -r custom/ $GENESIS_HOME

genesisInstall --ignoreHooks

echo y | remap --commit

# SendIt

#./gradlew $(./gradlew :genesisproduct-alpha:tasks | grep SendIt- | cut -d' ' -f1 | awk '{print ":genesisproduct-alpha:"$1}' | paste -s -d ' ' - )

stop_spinner

 # missing here ignore hooks
 # :genesisproduct-alpha:genesisInstall \ 

 # :genesisproduct-alpha:loadInitialData \
 # :genesisproduct-alpha:remap \

 # runs everything together
 # :genesisproduct-alpha:deploy-genesisproduct-alpha.zip \
 # :genesisproduct-alpha:mon  
    
