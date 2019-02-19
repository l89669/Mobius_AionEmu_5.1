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
package system.handlers.quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _15516Scout_The_Canyon extends QuestHandler
{
	private static final int questId = 15516;
	
	public _15516Scout_The_Canyon()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806094).addOnQuestStart(questId);
		qe.registerQuestNpc(806094).addOnTalkEvent(questId);
		qe.registerQuestNpc(241559).addOnKillEvent(questId);
		qe.registerQuestNpc(241560).addOnKillEvent(questId);
		qe.registerQuestNpc(241561).addOnKillEvent(questId);
		qe.registerQuestNpc(241562).addOnKillEvent(questId);
		qe.registerQuestNpc(241563).addOnKillEvent(questId);
		qe.registerQuestNpc(241564).addOnKillEvent(questId);
		qe.registerQuestNpc(241565).addOnKillEvent(questId);
		qe.registerQuestNpc(241566).addOnKillEvent(questId);
		qe.registerQuestNpc(241567).addOnKillEvent(questId);
		qe.registerQuestNpc(241568).addOnKillEvent(questId);
		qe.registerQuestNpc(241569).addOnKillEvent(questId);
		qe.registerQuestNpc(241570).addOnKillEvent(questId);
		qe.registerQuestNpc(241571).addOnKillEvent(questId);
		qe.registerQuestNpc(241572).addOnKillEvent(questId);
		qe.registerQuestNpc(241573).addOnKillEvent(questId);
		qe.registerQuestNpc(242007).addOnKillEvent(questId);
		qe.registerQuestNpc(242011).addOnKillEvent(questId);
		qe.registerQuestNpc(242015).addOnKillEvent(questId);
		qe.registerQuestNpc(242019).addOnKillEvent(questId);
		qe.registerQuestNpc(242023).addOnKillEvent(questId);
		qe.registerQuestNpc(242027).addOnKillEvent(questId);
		qe.registerQuestNpc(242031).addOnKillEvent(questId);
		qe.registerQuestNpc(242035).addOnKillEvent(questId);
		qe.registerQuestNpc(242039).addOnKillEvent(questId);
		qe.registerQuestNpc(242043).addOnKillEvent(questId);
		qe.registerQuestNpc(242047).addOnKillEvent(questId);
		qe.registerQuestNpc(242051).addOnKillEvent(questId);
		qe.registerQuestNpc(242055).addOnKillEvent(questId);
		qe.registerQuestNpc(242059).addOnKillEvent(questId);
		qe.registerQuestNpc(242063).addOnKillEvent(questId);
		qe.registerQuestNpc(242067).addOnKillEvent(questId);
		qe.registerQuestNpc(242071).addOnKillEvent(questId);
		qe.registerQuestNpc(242075).addOnKillEvent(questId);
		qe.registerQuestNpc(242079).addOnKillEvent(questId);
		qe.registerQuestNpc(242083).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806094)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 806094)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 60)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 60, 61, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806094)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (env.getTargetId())
			{
				case 241559:
				case 241560:
				case 241561:
				case 241562:
				case 241563:
				case 241564:
				case 241565:
				case 241566:
				case 241567:
				case 241568:
				case 241569:
				case 241570:
				case 241571:
				case 241572:
				case 241573:
				case 242007:
				case 242011:
				case 242015:
				case 242019:
				case 242023:
				case 242027:
				case 242031:
				case 242035:
				case 242039:
				case 242043:
				case 242047:
				case 242051:
				case 242055:
				case 242059:
				case 242063:
				case 242067:
				case 242071:
				case 242075:
				case 242079:
				case 242083:
				{
					if (qs.getQuestVarById(1) < 60)
					{
						qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
						updateQuestStatus(env);
					}
					if (qs.getQuestVarById(1) >= 60)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
				}
			}
		}
		return false;
	}
}