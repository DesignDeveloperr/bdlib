/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.bdew.lib.tile.TileExtended
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity

import scala.collection.mutable

trait TileDataSlots extends TileExtended {
  val dataSlots = mutable.HashMap.empty[String, DataSlot]
  var lastChange = 0L
  var lastGuiPacket = 0L

  final val TRACE = false

  def doSave(kind: UpdateKind.Value, t: NBTTagCompound) {
    if (kind == UpdateKind.GUI)
      t.setLong("BDLib_TS", getWorldObj.getTotalWorldTime)
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      s.save(t, kind)
      if (TRACE) printf("%s: %s S=> %s\n".format(kind, n, t.getTag(n)))
    }
  }

  def doLoad(kind: UpdateKind.Value, t: NBTTagCompound) {
    if (kind == UpdateKind.GUI) {
      val ts = t.getLong("BDLib_TS")
      if (ts > lastGuiPacket) {
        lastGuiPacket = ts
      } else {
        return
      }
    }
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      if (TRACE) printf("%s: %s L<= %s\n".format(kind, n, t.getTag(n)))
      s.load(t, kind)
      if (kind == UpdateKind.WORLD && s.updateKind.contains(UpdateKind.RENDER))
        getWorldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord)
    }
  }

  persistSave.listen(doSave(UpdateKind.SAVE, _))
  persistLoad.listen(doLoad(UpdateKind.SAVE, _))
  sendClientUpdate.listen(doSave(UpdateKind.WORLD, _))
  handleClientUpdate.listen(doLoad(UpdateKind.WORLD, _))

  def dataSlotChanged(slot: DataSlot) {
    if (getWorldObj != null) {
      if (slot.updateKind.contains(UpdateKind.GUI))
        lastChange = getWorldObj.getTotalWorldTime
      if (slot.updateKind.contains(UpdateKind.WORLD))
        getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      if (slot.updateKind.contains(UpdateKind.SAVE))
        getWorldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this)
    }
  }

  def getDataSlotPacket: Packet = {
    val tag = new NBTTagCompound()
    doSave(UpdateKind.GUI, tag)
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, ACT_GUI, tag)
  }

  override protected def extDataPacket(id: Int, data: NBTTagCompound) {
    if (id == ACT_GUI)
      doLoad(UpdateKind.GUI, data)
    super.extDataPacket(id, data)
  }
}
