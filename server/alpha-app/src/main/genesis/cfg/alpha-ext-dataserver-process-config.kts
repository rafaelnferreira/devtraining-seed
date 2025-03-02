process {
    
    systemDefinition {
        // connects with read only user
        item(name = "DbHost", value = "jdbc:postgresql://localhost:5432/genesis?user=readonly_user&password=postgres")
        // on the tenant schema
        item(name = "DbNamespace", value = "org1")
        // that doesn't have dictionary in the database
        item(name = "DictionarySource", value = "FILE")
        
        // do not use db alias store to deserialize messages
        item(name = "MqLayerAliasStrategy", value = "Simple")
    }

}