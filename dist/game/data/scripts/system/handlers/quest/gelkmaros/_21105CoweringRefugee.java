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
package system.handlers.quest.gelkmaros;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Cheatkiller
 */
public class _21105CoweringRefugee extends QuestHandler
{
	
	private static final int questId = 21105;
	
	public _21105CoweringRefugee()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799276).addOnQuestStart(questId);
		qe.registerQuestNpc(799276).addOnTalkEvent(questId);
		qe.registerQuestNpc(700812).addOnTalkEvent(questId);
		qe.registerQuestNpc(799366).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 799276)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env, 182207857, 1);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 799366)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (dialog == QuestDialog.SET_REWARD)
				{
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().onDelete();
					removeQuestItem(env, 182207857, 1);
					return defaultCloseDialog(env, 0, 1, true, false);
				}
			}
			else if (targetId == 700812)
			{
				final Npc npc = (Npc) env.getVisibleObject();
				final int npcId[] =
				{
					799366,
					216086
				};
				QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), npcId[Rnd.get(0, npcId.length - 1)], npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799276)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
