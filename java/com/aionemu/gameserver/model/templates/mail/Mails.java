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
package com.aionemu.gameserver.model.templates.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{
	"sysMailTemplates"
})
@XmlRootElement(name = "mails")
public class Mails
{
	@XmlElement(name = "mail")
	private List<SysMail> sysMailTemplates;
	
	@XmlTransient
	private final Map<String, SysMail> sysMailByName = new HashMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (SysMail template : sysMailTemplates)
		{
			final String sysMailName = template.getName().toLowerCase();
			sysMailByName.put(sysMailName, template);
		}
		sysMailTemplates.clear();
		sysMailTemplates = null;
	}
	
	public MailTemplate getMailTemplate(String name, String eventName, Race playerRace)
	{
		final SysMail template = sysMailByName.get(name.toLowerCase());
		if (template == null)
		{
			return null;
		}
		return template.getTemplate(eventName, playerRace);
	}
	
	public int size()
	{
		return sysMailByName.values().size();
	}
}
