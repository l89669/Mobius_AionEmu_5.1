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
package system.handlers.quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author MrPoke remod By Xitanium
 */
public class _1483HarumonerksRequest extends QuestHandler
{
	private static final int questId = 1483;
	
	public _1483HarumonerksRequest()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798126).addOnQuestStart(questId);
		qe.registerQuestNpc(798126).addOnTalkEvent(questId);
		qe.registerQuestNpc(203940).addOnTalkEvent(questId);
		qe.registerQuestNpc(203944).addOnTalkEvent(questId);
		qe.registerQuestNpc(798127).addOnTalkEvent(questId);
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
		if (targetId == 798126)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 203940)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 203944)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 1))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 1693));
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 798127)
		{
			if (qs != null)
			{
				if ((env.getDialog() == QuestDialog.START_DIALOG) && (qs.getStatus() == QuestStatus.START))
				{
					return sendQuestDialog(env, 2375);
				}
				else if ((env.getDialogId() == 1009) && (qs.getStatus() != QuestStatus.COMPLETE) && (qs.getStatus() != QuestStatus.NONE))
				{
					qs.setQuestVar(3);
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
