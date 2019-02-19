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
package system.handlers.quest.morheim;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Nephis and AU quest helper Team
 */
public class _2393TheLoveOfAFather extends QuestHandler
{
	
	private static final int questId = 2393;
	
	public _2393TheLoveOfAFather()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestItem(182204162, questId);
		qe.registerQuestNpc(204343).addOnQuestStart(questId);
		qe.registerQuestNpc(204343).addOnTalkEvent(questId);
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		
		if (id != 182204162)
		{
			return HandlerResult.UNKNOWN;
		}
		if (!player.isInsideZone(ZoneName.get("DF2_ITEMUSEAREA_Q2393")))
		{
			return HandlerResult.UNKNOWN;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			player.getInventory().decreaseByObjectId(itemObjId, 1);
			giveQuestItem(env, 182204163, 1);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}, 3000);
		return HandlerResult.SUCCESS;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204343)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				else if (env.getDialogId() == 1002)
				{
					if (giveQuestItem(env, 182204162, 1))
					{
						return sendQuestStartDialog(env);
					}
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
			
			else if (qs.getStatus() == QuestStatus.REWARD)
			{
				if ((env.getDialog() == QuestDialog.START_DIALOG) && (qs.getStatus() == QuestStatus.REWARD))
				{
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == 1009)
				{
					qs.setQuestVar(2);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
