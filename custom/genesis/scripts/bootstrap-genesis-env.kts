
import global.genesis.clustersupport.DefaultClusterManager
import global.genesis.clustersupport.service.ServiceDiscovery
import global.genesis.clustersupport.service.ServiceModuleBuilder
import global.genesis.commons.config.GenesisConfigurationException
import global.genesis.commons.guice.LazySingletonProvider
import global.genesis.commons.script.EvaluatorPool
import global.genesis.commons.standards.GenesisPaths
import global.genesis.commons.standards.SystemDefinitionConfigFields
import global.genesis.commons.standards.SystemDefinitionConfigFields.GenesisNetProtocol
import global.genesis.commons.standards.SystemDefinitionConfigFields.MessageSerialization
import global.genesis.config.dsl.host.GenesisScriptHost
import global.genesis.config.service.StandardServiceDetailProvider
import global.genesis.config.system.SystemDefinitionResolver
import global.genesis.config.system.SystemDefinitionService
import global.genesis.db.rx.RxDb
import global.genesis.environment.scripts.ScriptsUtils
import global.genesis.net.ClientConnectionsManager
import global.genesis.net.config.NetworkConfiguration
import global.genesis.process.GenesisScriptConfig
import java.io.File
import java.net.InetAddress
import kotlin.script.experimental.api.constructorArgs

fun createNetworkConfig(sysDef: SystemDefinitionService): NetworkConfiguration {
    val networkConfiguration = NetworkConfiguration()
    // Add protocol
    val protocol: Any? = sysDef.getItem(SystemDefinitionConfigFields.GENESIS_NET_PROTOCOL)
    if (protocol != null) {
        networkConfiguration.protocol = GenesisNetProtocol.valueOf(protocol.toString())
    }
    // Add message serialization
    val messageSerialization: Any? = sysDef.getItem(SystemDefinitionConfigFields.MESSAGE_SERIALIZATION)
    if (messageSerialization != null) {
        networkConfiguration.messageSerialization = MessageSerialization.valueOf(messageSerialization.toString())
    }
    return networkConfiguration
}

ScriptsUtils.initScript()

val fileName = "GenesisShell"

val sysDef = SystemDefinitionResolver.resolveService(GenesisPaths.genesisHome())
val provider = StandardServiceDetailProvider()
val serviceModule = ServiceModuleBuilder.buildServiceModule(
    fileName,
    InetAddress.getLocalHost().hostName,
    sysDef,
    provider,
    createNetworkConfig(sysDef),
    DefaultClusterManager(),
    LazySingletonProvider { ClientConnectionsManager(provider) }
)
val genesisScriptConfig = GenesisScriptConfig(
    fileName,
    additionalModules = listOf(serviceModule),
    serviceDetailProvider = provider
)
val rxDb = try {
    genesisScriptConfig.getObject(RxDb::class.java)
} catch (e: Exception) {
    throw GenesisConfigurationException("Error while initialising database", e)
}

//val serviceDiscovery = genesisScriptConfig.getObject<ServiceDiscovery>()
