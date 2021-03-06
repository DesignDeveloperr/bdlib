package net.bdew.lib

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLServerStartingEvent, FMLServerStoppingEvent}
import cpw.mods.fml.common.{FMLCommonHandler, Mod}
import net.bdew.lib.multiblock.network.NetHandler
import net.bdew.lib.tooltip.TooltipHandler
import net.minecraft.command.CommandHandler
import org.apache.logging.log4j.Logger

@Mod(modid = "bdlib", name = "BD lib", version = "BDLIB_VER", modLanguage = "scala")
object BdLib {
  var log: Logger = null

  def logDebug(msg: String, args: Any*) = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*) = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*) = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*) = log.error(msg.format(args: _*), t)

  val onServerStarting = Event[FMLServerStartingEvent]
  val onServerStopping = Event[FMLServerStoppingEvent]

  @EventHandler
  def preInit(ev: FMLPreInitializationEvent) {
    log = ev.getModLog
    log.info("bdlib BDLIB_VER loaded")
    log.debug("List of loaded APIs: " + ApiReporter.APIs)
    FMLCommonHandler.instance().registerCrashCallable(ApiReporter)
    NetHandler.init()
    if (ev.getSide.isClient) {
      TooltipHandler.init()
    }
  }

  @EventHandler
  def serverStarting(event: FMLServerStartingEvent) {
    val commandHandler = event.getServer.getCommandManager.asInstanceOf[CommandHandler]
    commandHandler.registerCommand(CommandDumpRegistry)
    commandHandler.registerCommand(CommandOreDistribution)
    onServerStarting.trigger(event)
  }

  @EventHandler
  def serverStopping(event: FMLServerStoppingEvent) {
    onServerStopping.trigger(event)
  }
}
