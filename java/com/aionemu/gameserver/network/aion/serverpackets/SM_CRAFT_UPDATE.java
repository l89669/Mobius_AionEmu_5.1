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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_CRAFT_UPDATE extends AionServerPacket
{
	private final int skillId;
	private final int itemId;
	private final int action;
	private final int success;
	private final int failure;
	private final int nameId;
	private final int executionDelay = 700;
	private int executionPeriod = 1200;
	
	public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action)
	{
		this.action = action;
		this.skillId = skillId;
		itemId = item.getTemplateId();
		this.success = success;
		this.failure = failure;
		nameId = item.getNameId();
		// Aether Morphing.
		if (skillId == 40009)
		{
			executionPeriod = 3000;
		}
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeH(skillId);
		writeC(action);
		writeD(itemId);
		switch (action)
		{
			case 0:
			{
				writeD(success);
				writeD(failure);
				writeD(0);
				writeD(1200);
				writeD(1330048);
				writeH(0x24);
				writeD(nameId);
				writeH(0);
				break;
			}
			case 1:
			case 2:
			case 5:
			{
				writeD(success);
				writeD(failure);
				writeD(executionDelay);
				writeD(executionPeriod);
				writeD(0);
				writeH(0);
				break;
			}
			case 3:
			{
				writeD(success);
				writeD(failure);
				writeD(0);
				writeD(executionPeriod);
				writeD(1330048);
				writeH(0x24);
				writeD(nameId);
				writeH(0);
				break;
			}
			case 4:
			{
				writeD(success);
				writeD(failure);
				writeD(0);
				writeD(0);
				writeD(1330051);
				writeH(0);
				break;
			}
			case 6:
			{
				writeD(success);
				writeD(failure);
				writeD(executionDelay);
				writeD(executionPeriod);
				writeD(1330050);
				writeH(0x24);
				writeD(nameId);
				writeH(0);
				break;
			}
			case 7:
			{
				writeD(success);
				writeD(failure);
				writeD(0);
				writeD(executionPeriod);
				writeD(1330050);
				writeH(0x24);
				writeD(nameId);
				writeH(0);
				break;
			}
		}
	}
}