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

./gradlew :server:assemble

stop_spinner

spinner "🚀 ${BLUE}Deploying" &
spinner_pid=$!

echo -e "Deploying to ${GREEN}$GENESIS_HOME${NC}"

./gradlew :server:alpha-app:setupEnvironment

./gradlew :server:alpha-app:install-auth-distribution.zip \
 :server:alpha-app:install-alpha-site-specific-1.0.0-SNAPSHOT-bin-distribution.zip \
 :server:alpha-app:install-genesisproduct-alpha-1.0.0-SNAPSHOT-bin-distribution.zip

# Patching scripts for OSX
patch -ruN -d $GENESIS_HOME < OSX_Changes_to_unix_scripts_.patch

# Sync custom scripts
rsync -r custom/ $GENESIS_HOME

# Install without calling hooks
genesisInstall --ignoreHooks

# Remap and data import
echo "y\ny" | remap --commit --dataLoad

# Pre compiling scripts so the heap requirements are lower, issues are caught earlier and boostrap is quicker
preCompileScripts

stop_spinner

 # missing here ignore hooks
 # :server:alpha-app:genesisInstall \ 

 # :server:alpha-app:loadInitialData \
 # :server:alpha-app:remap \

 # runs everything together
 # :server:alpha-app:deploy-genesisproduct-alpha.zip \
 # :server:alpha-app:mon  
    
