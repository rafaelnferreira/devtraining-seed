/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide table definition config for multi-pro-code-test.
 *
 * Modification History
 */

tables {

    table( name = "CHARLIE_TABLE", id = 2016) {
        field("ID", LONG).autoIncrement().primaryKey()
        field("NAME", STRING)
        field("COUNTRY_CODE", STRING(2))
        field("CHARLIE", INT)
    }

}