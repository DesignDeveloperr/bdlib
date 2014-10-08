/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import org.lwjgl.input.Keyboard

/**
 * Misc functions and shortcuts for clientside code
 */
object Client {
  def minecraft = Minecraft.getMinecraft
  def fontRenederer = Minecraft.getMinecraft.fontRenderer
  def renderEngine = Minecraft.getMinecraft.renderEngine
  def world = Minecraft.getMinecraft.theWorld
  def player = Minecraft.getMinecraft.thePlayer
  def textureManager = Minecraft.getMinecraft.getTextureManager

  def blockMissingIcon = textureManager.getTexture(TextureMap.locationBlocksTexture).asInstanceOf[TextureMap].getAtlasSprite("missingno")
  def itemMissingIcon = textureManager.getTexture(TextureMap.locationItemsTexture).asInstanceOf[TextureMap].getAtlasSprite("missingno")

  def shiftDown: Boolean = {
    return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)
  }

}
