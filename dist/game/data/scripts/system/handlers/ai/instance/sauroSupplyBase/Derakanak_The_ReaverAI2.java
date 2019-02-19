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
package system.handlers.ai.instance.sauroSupplyBase;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("derakanak_the_reaver")
public class Derakanak_The_ReaverAI2 extends AggressiveNpcAI2
{
	private int stage = 0;
	private boolean isStart = false;
	
	@Override
	protected void handleCreatureAggro(Creature creature)
	{
		super.handleCreatureAggro(creature);
		wakeUp();
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		beritraFavor();
	}
	
	private void beritraFavor()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 21135, 1, getOwner()).useNoAnimationSkill(); // Beritra's Favor.
	}
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		wakeUp();
	}
	
	private void wakeUp()
	{
		isStart = true;
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if ((hpPercentage <= 80) && (stage < 1))
		{
			stage1();
			stage = 1;
		}
		if ((hpPercentage <= 40) && (stage < 2))
		{
			stage1();
			stage = 2;
		}
	}
	
	void stage1()
	{
		final int delay = 45000;
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		SkillEngine.getInstance().getSkill(getOwner(), 17888, 60, getOwner()).useNoAnimationSkill(); // Fear Casting.
		scheduleDelayStage1(delay);
	}
	
	private void stage2()
	{
		final int delay = 15000;
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		SkillEngine.getInstance().getSkill(getOwner(), Rnd.get(2) == 0 ? 16918 : 16881, 60, getTarget()).useNoAnimationSkill();
		scheduleDelayStage2(delay);
	}
	
	private void scheduleDelayStage2(int delay)
	{
		if (!isStart && !isAlreadyDead())
		{
			return;
		}
		ThreadPoolManager.getInstance().schedule((Runnable) () -> stage2(), delay);
	}
	
	private void scheduleDelayStage1(int delay)
	{
		if (!isStart && !isAlreadyDead())
		{
			return;
		}
		ThreadPoolManager.getInstance().schedule((Runnable) () -> stage1(), delay);
	}
	
	@Override
	protected void handleBackHome()
	{
		super.handleBackHome();
		isStart = false;
		stage = 0;
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
		isStart = false;
		stage = 0;
	}
}