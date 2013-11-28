/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{TileDataSlots, UpdateKind, DataSlotVal}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack

case class DataSlotItemStack(name: String, parent: TileDataSlots) extends DataSlotVal[ItemStack] {
  var cval: ItemStack = null

  override def update(v: ItemStack) = {
    if (!ItemStack.areItemStacksEqual(v, cval)) {
      cval = if (v == null) null else v.copy()
      parent.dataSlotChanged(this)
    }
  }

  def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    val tag = new NBTTagCompound()
    if (cval != null) cval.writeToNBT(tag)
    t.setCompoundTag(name, tag)
  }
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = cval = ItemStack.loadItemStackFromNBT(t.getCompoundTag(name))
}
