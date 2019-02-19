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
package system.handlers.quest.fenris_fang;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _4940Decorations_Of_Pandaemonium extends QuestHandler
{
	private static final int questId = 4940;
	
	public _4940Decorations_Of_Pandaemonium()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 4939, true);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204050).addOnQuestStart(questId);
		qe.registerQuestNpc(204050).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || ((qs.getStatus() == QuestStatus.NONE) || (qs.getStatus() == QuestStatus.COMPLETE)))
		{
			if (targetId == 204050)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
				
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204050:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
						case CHECK_COLLECTED_ITEMS:
						{
							final long itemCount1 = player.getInventory().getItemCountByItemId(182207117);
							final long itemCount2 = player.getInventory().getItemCountByItemId(182207118);
							final long itemCount3 = player.getInventory().getItemCountByItemId(182207119);
							final long itemCount4 = player.getInventory().getItemCountByItemId(182207120);
							if ((itemCount1 >= 10) && (itemCount2 >= 10) && (itemCount3 >= 10) && (itemCount4 >= 10))
							{
								removeQuestItem(env, 182207117, 10);
								removeQuestItem(env, 182207118, 10);
								removeQuestItem(env, 182207119, 10);
								removeQuestItem(env, 182207120, 10);
								changeQuestStep(env, 1, 1, true);
								return sendQuestDialog(env, 5);
							}
							return sendQuestDialog(env, 10001);
						}
					}
					break;
				}
				default:
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204050)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}