/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLServerStartingEvent, FMLServerStoppingEvent}
import net.bdew.lib.multiblock.network.NetHandler
import net.minecraft.command.CommandHandler
import org.apache.logging.log4j.Logger

@Mod(modid = "bdlib", name = "BD lib", version = "BDLIB_VER", modLanguage = "scala")
object BdLib {
  var log: Logger = null

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
    NetHandler.init()
  }

  @EventHandler
  def serverStarting(event: FMLServerStartingEvent) {
    val commandHandler = event.getServer.getCommandManager.asInstanceOf[CommandHandler]
    commandHandler.registerCommand(new CommandDumpRegistry)
    onServerStarting.trigger(event)
  }

  @EventHandler
  def serverStopping(event: FMLServerStoppingEvent) {
    onServerStopping.trigger(event)
  }
}
