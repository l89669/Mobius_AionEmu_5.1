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
package system.handlers.ai.worlds.enshar;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.CreatureEventHandler;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("shademender")
public class ShademenderAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
		CreatureEventHandler.onCreatureSee(this, creature);
		if (creature instanceof Player)
		{
			final Player player = (Player) creature;
			if (!creature.getEffectController().hasAbnormalEffect(20664))
			{ // Conqueror's Passion.
				if (player.getCommonData().getRace() == Race.ASMODIANS)
				{
					SkillEngine.getInstance().getSkill(getOwner(), 20664, 1, creature).useNoAnimationSkill(); // Conqueror's Passion.
				}
			}
		}
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		switch (getNpcId())
		{
			case 219880:
			case 219886:
			case 219893:
			case 219899:
			case 219905:
			case 219912:
			case 219919:
			case 219925:
			{
				conquerorPassion();
				break;
			}
		}
	}
	
	private void conquerorPassion()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 20665, 1, getOwner()).useNoAnimationSkill(); // Conqueror's Passion.
	}
}