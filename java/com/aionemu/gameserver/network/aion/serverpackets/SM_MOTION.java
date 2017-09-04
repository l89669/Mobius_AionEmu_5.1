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

import java.util.Collection;
import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_MOTION extends AionServerPacket
{
	byte action;
	short motionId;
	int remainingTime;
	int playerId;
	Map<Integer, Motion> activeMotions;
	Collection<Motion> motions;
	byte type;
	
	public SM_MOTION(Collection<Motion> motions)
	{
		action = 1;
		this.motions = motions;
	}
	
	public SM_MOTION(short motionId, int remainingTime)
	{
		action = 2;
		this.motionId = motionId;
		this.remainingTime = remainingTime;
	}
	
	public SM_MOTION(short motionId, byte type)
	{
		action = 5;
		this.motionId = motionId;
		this.type = type;
	}
	
	public SM_MOTION(short motionId)
	{
		action = 6;
		this.motionId = motionId;
	}
	
	public SM_MOTION(int playerId, Map<Integer, Motion> activeMotions)
	{
		action = 7;
		this.playerId = playerId;
		this.activeMotions = activeMotions;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(action);
		switch (action)
		{
			case 1:
			{
				writeH(motions.size());
				for (Motion motion : motions)
				{
					writeH(motion.getId());
					writeD(motion.getRemainingTime());
					writeC(motion.isActive() ? 1 : 0);
				}
				break;
			}
			case 2:
			{
				writeH(motionId);
				writeD(remainingTime);
				break;
			}
			case 5:
			{
				writeH(motionId);
				writeC(type);
				break;
			}
			case 6:
			{
				writeH(motionId);
				break;
			}
			case 7:
			{
				writeD(playerId);
				for (int i = 1; i < 6; i++)
				{
					final Motion motion = activeMotions.get(i);
					if (motion == null)
					{
						writeH(0);
					}
					else
					{
						writeH(motion.getId());
					}
				}
				break;
			}
		}
	}
}