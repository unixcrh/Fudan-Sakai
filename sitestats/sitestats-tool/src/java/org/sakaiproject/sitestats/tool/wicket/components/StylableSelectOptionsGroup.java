/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/java/org/sakaiproject/sitestats/tool/wicket/components/StylableSelectOptionsGroup.java $
 * $Id: StylableSelectOptionsGroup.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
 *
 * Copyright (c) 2006-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.sitestats.tool.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;

public class StylableSelectOptionsGroup extends Border {
	private static final long	serialVersionUID	= 1L;

	public StylableSelectOptionsGroup(String id, IModel model, IModel style) {
		super(id);
		WebMarkupContainer optgroup = new WebMarkupContainer("optgroup");
		optgroup.add(new AttributeModifier("label", true, model));
		if(style != null && !"null".equals((String) style.getObject())) {
			optgroup.add(new AttributeModifier("style", true, style));
		}
		add(optgroup);
		optgroup.add(getBodyContainer());
	}

}
