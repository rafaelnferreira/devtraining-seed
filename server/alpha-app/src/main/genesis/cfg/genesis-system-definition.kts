package genesis.cfg

systemDefinition {
    global {
        item(name = "DEPLOYED_PRODUCT", value = "alpha")
        
        item(name = "MqLayer", value = "ZeroMQ")
        item(name = "JmsUsername", value = "artemis")
        item(name = "JmsPassword", value = "fd47076f2ae8975f853e95a4bac6de787ec39d05095d3f87d48558a77e5a4d3f", encrypted = true)
        
        // item(name = "DbLayer", value = "FDB")
        item(name = "DbLayer", value = "SQL")
        
        item(name = "DictionarySource", value = "DB")
        item(name = "AliasSource", value = "DB")
        item(name = "MetricsEnabled", value = "true")
        item(name = "MetricsReportType", value = "SLF4J")
        item(name = "ZeroMQProxyInboundPort", value = "5001")
        item(name = "ZeroMQProxyOutboundPort", value = "5000")
        item(name = "ZeroMQConnectToLocalhostViaLoopback", value = "true")
        item(name = "GenesisKey", value = "aB3cDeF7gHiJkLmN9oPqRs1tUvWxYz21")
        item(name = "RemapEnableAutoConfirm", value = "true")

        // Consul as service registrar
        // item(name = "ClusterMode", value = "CONSUL")
        
        // postgres
        item(name = "DbHost", value = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
        
        // oracle
        //item(name = "DbHost", value = "jdbc:oracle:thin:system/Hoothoot1@localhost:1521/XE")

        // sql server
        //item(name = "DbHost", value = "jdbc:sqlserver://localhost:1433;trustServerCertificate=true;username=SA;password=Rootroot1")

        // h2
        // item(name = "DbHost", value = "jdbc:h2:file:~/genesis-local-db/alpha/h2/test;DB_CLOSE_DELAY=-1;NON_KEYWORDS=VALUE,KEY;AUTO_SERVER=TRUE")
        
        // FDB
        // item(name = "DbHost", value = "localhost")

        item(name = "DbMode", value = "VANILLA")
        item(name = "GenesisNetProtocol", value = "V2")
        item(name = "ResourcePollerTimeout", value = "5")
        item(name = "ReqRepTimeout", value = "60")
        item(name = "MetadataChronicleMapAverageKeySizeBytes", value = "128")
        item(name = "MetadataChronicleMapAverageValueSizeBytes", value = "1024")
        item(name = "MetadataChronicleMapEntriesCount", value = "512")
        item(name = "DaemonServerPort", value = "4568")
        item(name = "DaemonHealthPort", value = "4569")
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

            item(name = "DbNamespace", value = "alpha")
            item(name = "PrimaryIfSingleNode", value = "true")
            item(name = "ClusterPort", value = "6000")
            item(name = "location", value = "LO")
            item(name = "LogFramework", value = "LOG4J2")
            item(name = "LogFrameworkConfig", value = "log4j2-default.xml")
        }

    }

}
