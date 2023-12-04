package mycompany.events

import com.google.inject.Inject
import com.google.inject.Provider
import global.genesis.commons.annotation.ProviderOf
import global.genesis.config.service.StandardServiceDetailProvider
import global.genesis.net.GenesisMessageClient
import global.genesis.net.config.NetworkConfiguration

@ProviderOf(type = GenesisMessageClient::class)
class ClientProvider @Inject constructor(private val networkConfiguration: NetworkConfiguration) :
    Provider<GenesisMessageClient> {

    private val serviceDetails = StandardServiceDetailProvider.getInstance()
        .getServiceDetails("ALPHA_GROOVY_EVENT_HANDLER")

    override fun get(): GenesisMessageClient {
        return GenesisMessageClient("localhost", serviceDetails.port, serviceDetails.isSecure, networkConfiguration)
    }

}