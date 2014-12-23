/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.gui.{GuiHandler, GuiProvider}
import net.bdew.lib.machine.Machine
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraft.creativetab.CreativeTabs

class MachineManager(val tuning: ConfigSection, guiHandler: GuiHandler, creativeTab: CreativeTabs) {
  def registerMachine[R <: Machine[_]](machine: R): R = {
    machine.tuning = tuning.getSection(machine.name)
    if (machine.tuning.getBoolean("Enabled")) {
      machine.enabled = true
      machine.regBlock(creativeTab)
      if (machine.isInstanceOf[GuiProvider])
        guiHandler.register(machine.asInstanceOf[GuiProvider])
    }
    return machine
  }
  def load() {}
}
