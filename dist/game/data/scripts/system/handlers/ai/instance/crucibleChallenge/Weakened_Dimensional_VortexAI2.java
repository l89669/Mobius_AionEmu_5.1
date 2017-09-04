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
package system.handlers.ai.instance.crucibleChallenge;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("weakened_dimensional_vortex")
public class Weakened_Dimensional_VortexAI2 extends AggressiveNpcAI2
{
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				SkillEngine.getInstance().getSkill(getOwner(), 19570, 46, getOwner()).useNoAnimationSkill(); // Dimensional Vortex.
				startLifeTask();
			}
		}, 1000);
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				AI2Actions.deleteOwner(Weakened_Dimensional_VortexAI2.this);
			}
		}, 15000);
	}
	
	@Override
	protected AIAnswer pollInstance(AIQuestion question)
	{
		switch (question)
		{
			case SHOULD_DECAY:
			{
				return AIAnswers.NEGATIVE;
			}
			case SHOULD_RESPAWN:
			{
				return AIAnswers.NEGATIVE;
			}
			case SHOULD_REWARD:
			{
				return AIAnswers.NEGATIVE;
			}
			default:
			{
				return null;
			}
		}
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
}