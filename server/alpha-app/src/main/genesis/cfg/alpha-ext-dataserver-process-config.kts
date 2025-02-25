process {
    
    systemDefinition {
        item(name = "DbHost", value = "jdbc:postgresql://localhost:5432/genesis?user=readonly_user&password=postgres")
        item(name = "DbNamespace", value = "org1")

    // TODO Check with Jose how it is supposed to work
//        item(name = "DictionarySource", value = "FILE")
    }

}