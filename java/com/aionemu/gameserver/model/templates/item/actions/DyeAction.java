/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_EDIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author IceReaper
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DyeAction")
public class DyeAction extends AbstractItemAction implements HouseDyeAction
{
	@XmlAttribute(name = "color")
	protected String color;
	
	@XmlAttribute
	private Integer minutes;
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if (targetItem == null)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		return true;
	}
	
	private int getColorBGRA()
	{
		if (color.equals("no"))
		{
			return 0;
		}
		final int rgb = Integer.parseInt(color, 16);
		return 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
	}
	
	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1))
		{
			return;
		}
		if (targetItem.getItemTemplate().isItemDyePermitted())
		{
			if (getColorBGRA() == 0)
			{
				targetItem.setItemColor(0);
				targetItem.setColorExpireTime(0);
			}
			else
			{
				targetItem.setItemColor(parentItem.getItemTemplate().getTemplateId());
				if (minutes != null)
				{
					targetItem.setColorExpireTime((int) ((System.currentTimeMillis() / 1000) + (minutes * 60)));
				}
			}
			if (player.getEquipment().getEquippedItemByObjId(targetItem.getObjectId()) != null)
			{
				PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedForApparence()), true);
				player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			else
			{
				player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			ItemPacketService.updateItemAfterInfoChange(player, targetItem);
		}
	}
	
	public int getColor()
	{
		return getColorBGRA();
	}
	
	@Override
	public boolean canAct(Player player, Item parentItem, HouseObject<?> targetHouseObject)
	{
		if (targetHouseObject == null)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (color.equals("no") && (targetHouseObject.getColor() == null))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_PAINT_ERROR_CANNOTREMOVE);
			return false;
		}
		final boolean canPaint = targetHouseObject.getObjectTemplate().getCanDye();
		if (!canPaint)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_PAINT_ERROR_CANNOTPAINT);
		}
		return canPaint;
	}
	
	@Override
	public void act(Player player, Item parentItem, HouseObject<?> targetHouseObject)
	{
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1))
		{
			return;
		}
		if (color.equals("no"))
		{
			targetHouseObject.setColor(null);
		}
		else
		{
			targetHouseObject.setColor(Integer.parseInt(color, 16));
		}
		final float x = targetHouseObject.getX();
		final float y = targetHouseObject.getY();
		final float z = targetHouseObject.getZ();
		final int rotation = targetHouseObject.getRotation();
		PacketSendUtility.sendPacket(player, new SM_HOUSE_EDIT(7, 0, targetHouseObject.getObjectId()));
		PacketSendUtility.sendPacket(player, new SM_HOUSE_EDIT(5, targetHouseObject.getObjectId(), x, y, z, rotation));
		targetHouseObject.spawn();
		final int objectName = targetHouseObject.getObjectTemplate().getNameId();
		if (color.equals("no"))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_PAINT_REMOVE_SUCCEED(objectName));
		}
		else
		{
			final int paintName = parentItem.getItemTemplate().getNameId();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_PAINT_SUCCEED(objectName, paintName));
		}
	}
}