package mycompany.events

import com.google.inject.Inject
import global.genesis.config.service.StandardServiceDetailProvider
import global.genesis.net.GenesisMessageClient
import global.genesis.net.config.NetworkConfiguration

class ClientProvider @Inject constructor(networkConfiguration: NetworkConfiguration) {

    private val serviceDetails = StandardServiceDetailProvider.getInstance()
        .getServiceDetails("ALPHA_GROOVY_EVENT_HANDLER")

    val client = GenesisMessageClient("localhost", serviceDetails.port, serviceDetails.isSecure, networkConfiguration)

}