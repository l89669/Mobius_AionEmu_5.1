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
package com.aionemu.gameserver.ai2.handler;

import java.util.Collections;

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
public class AggroEventHandler
{
	
	/**
	 * @param npcAI
	 * @param myTarget
	 */
	public static void onAggro(NpcAI2 npcAI, Creature myTarget)
	{
		final Npc owner = npcAI.getOwner();
		// TODO move out?
		if ((myTarget.getAdminNeutral() == 1) || (myTarget.getAdminNeutral() == 3) || (myTarget.getAdminEnmity() == 1) || (myTarget.getAdminEnmity() == 3))
		{
			return;
		}
		
		PacketSendUtility.broadcastPacket(owner, new SM_ATTACK(owner, myTarget, 0, 633, 0, Collections.singletonList(new AttackResult(0, AttackStatus.NORMALHIT))));
		
		ThreadPoolManager.getInstance().schedule(new AggroNotifier(owner, myTarget, true), 500);
	}
	
	public static boolean onCreatureNeedsSupport(NpcAI2 npcAI, Creature notMyTarget)
	{
		final Npc owner = npcAI.getOwner();
		if (notMyTarget.isSupportFrom(owner) && MathUtil.isInRange(owner, notMyTarget, owner.getAggroRange()) && GeoService.getInstance().canSee(owner, notMyTarget))
		{
			final VisibleObject myTarget = notMyTarget.getTarget();
			if ((myTarget != null) && (myTarget instanceof Creature))
			{
				final Creature targetCreature = (Creature) myTarget;
				
				PacketSendUtility.broadcastPacket(owner, new SM_ATTACK(owner, targetCreature, 0, 633, 0, Collections.singletonList(new AttackResult(0, AttackStatus.NORMALHIT))));
				ThreadPoolManager.getInstance().schedule(new AggroNotifier(owner, targetCreature, false), 500);
				return true;
			}
		}
		return false;
	}
	
	public static boolean onGuardAgainstAttacker(NpcAI2 npcAI, Creature attacker)
	{
		final Npc owner = npcAI.getOwner();
		final TribeClass tribe = owner.getTribe();
		if (!tribe.isGuard() && (owner.getObjectTemplate().getNpcTemplateType() != NpcTemplateType.GUARD))
		{
			return false;
		}
		final VisibleObject target = attacker.getTarget();
		if ((target != null) && (target instanceof Player))
		{
			final Player playerTarget = (Player) target;
			if (!owner.isEnemy(playerTarget) && owner.isEnemy(attacker) && MathUtil.isInRange(owner, playerTarget, owner.getAggroRange()) && GeoService.getInstance().canSee(owner, attacker))
			{
				owner.getAggroList().startHate(attacker);
				return true;
			}
		}
		return false;
	}
	
	private static final class AggroNotifier implements Runnable
	{
		
		private Npc aggressive;
		private Creature target;
		private final boolean broadcast;
		
		AggroNotifier(Npc aggressive, Creature target, boolean broadcast)
		{
			this.aggressive = aggressive;
			this.target = target;
			this.broadcast = broadcast;
		}
		
		@Override
		public void run()
		{
			aggressive.getAggroList().addHate(target, 1);
			if (broadcast)
			{
				aggressive.getKnownList().doOnAllNpcs(object -> object.getAi2().onCreatureEvent(AIEventType.CREATURE_NEEDS_SUPPORT, aggressive));
			}
			aggressive = null;
			target = null;
		}
		
	}
	
}
