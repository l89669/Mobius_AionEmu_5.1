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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillLauncherEffect")
public class SkillLauncherEffect extends EffectTemplate
{
	@XmlAttribute(name = "skill_id")
	protected int skillId;
	
	@Override
	public void applyEffect(Effect effect)
	{
		// TODO figure out what value does
		final SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		final Effect e = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), 0);
		e.initialize();
		e.applyEffect();
	}
	
	public int getLaunchSkillId()
	{
		return skillId;
	}
}
