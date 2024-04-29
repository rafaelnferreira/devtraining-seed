package genesis.cfg

systemDefinition {
    global {
        item(name = "DEPLOYED_PRODUCT", value = "alpha")
        item(name = "GenerateDatabaseRepositories", value = "false")
        item(name = "MqLayer", value = "ZeroMQ")
        item(name = "DbLayer", value = "SQL")
        item(name = "DictionarySource", value = "DB")
        item(name = "AliasSource", value = "DB")
        item(name = "MetricsEnabled", value = "false")
        item(name = "ZeroMQProxyInboundPort", value = "5001")
        item(name = "ZeroMQProxyOutboundPort", value = "5000")
        
        // postgres
        item(name = "DbHost", value = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
        
        // oracle
        //item(name = "DbHost", value = "jdbc:oracle:thin:system/Hoothoot1@localhost:1521/XE")

        // sql server
        //item(name = "DbHost", value = "jdbc:sqlserver://localhost:1433;trustServerCertificate=true;username=SA;password=Rootroot1")
        
        item(name = "DbMode", value = "VANILLA")
        item(name = "GenesisNetProtocol", value = "V2")
        item(name = "ResourcePollerTimeout", value = "5")
        item(name = "ReqRepTimeout", value = "60")
        item(name = "MetadataChronicleMapAverageKeySizeBytes", value = "128")
        item(name = "MetadataChronicleMapAverageValueSizeBytes", value = "1024")
        item(name = "MetadataChronicleMapEntriesCount", value = "512")
        item(name = "DaemonServerPort", value = "4568")
        item(name = "DbSqlConnectionPoolSize", value = "4")
        item(
            name = "JVM_OPTIONS",
            value = "-XX:MaxHeapFreeRatio=70 -XX:MinHeapFreeRatio=30 -XX:+UseG1GC -XX:+UseStringDeduplication -XX:OnOutOfMemoryError=\"handleOutOfMemoryError.sh %p\" -Djava.net.preferIPv4Stack=true"
        )
    }

    systems {

        system(name = "DEV") {

            hosts {
                host(LOCAL_HOST)
            }

            item(name = "DbNamespace", value = "alphalegacy")
            item(name = "ClusterPort", value = "6000")
            item(name = "location", value = "LO")
            item(name = "LogFramework", value = "LOG4J2")
            item(name = "LogFrameworkConfig", value = "log4j2-default.xml")
        }

    }

}
