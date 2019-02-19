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
package system.handlers.quest.inggison;

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
 * @author Leunam
 */
public class _11070CraftyMessenger extends QuestHandler
{
	
	private static final int questId = 11070;
	private static final int[] npc_ids =
	{
		798945,
		798943,
		798949,
		798932
	};
	
	public _11070CraftyMessenger()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798945).addOnQuestStart(questId);
		for (int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
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
		if (targetId == 798945)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialogId() == 1002)
				{
					if (giveQuestItem(env, 182206859, 3))
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
		}
		if (qs == null)
		{
			return false;
		}
		
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798945)
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == 1009)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (targetId == 798943)
		{
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 0)
					{
						return sendQuestDialog(env, 1352);
					}
				}
				case STEP_TO_1:
				{
					if (var == 0)
					{
						removeQuestItem(env, 182206859, 1);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				}
			}
		}
		else if (targetId == 798949)
		{
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 1)
					{
						return sendQuestDialog(env, 1693);
					}
				}
				case STEP_TO_2:
				{
					if (var == 1)
					{
						removeQuestItem(env, 182206859, 1);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				}
			}
		}
		else if (targetId == 798932)
		{
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 2)
					{
						return sendQuestDialog(env, 2034);
					}
				}
				case STEP_TO_3:
				{
					if (var == 2)
					{
						removeQuestItem(env, 182206859, 1);
						qs.setQuestVarById(0, var + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				}
			}
		}
		return false;
	}
}
