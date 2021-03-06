/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/chat/branches/sakai-2.8.1/chat-api/api/src/java/org/sakaiproject/chat2/model/ChatMessage.java $
 * $Id: ChatMessage.java 61133 2009-04-24 19:25:44Z stephen.marquard@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
 
package org.sakaiproject.chat2.model;


import java.util.Date;
import java.util.Stack;

import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.ResourceProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author andersjb
 *
 */
public class ChatMessage implements Entity {

   private String id;
   private ChatChannel chatChannel;
   private String owner;
   private Date messageDate;
   private String body;
   private String migratedMessageId;

   
   public ChatMessage() {
   }
   
   static String urlRegexp = "https?://[\\S]+";
   static String bodyRegexp = ".*"+urlRegexp+".*";
   static String parenRegexp = "[\\s+(]";
   static String cleanRegexp = "\\s+";
   static String firstRegexp = "^";
   
   static String href_1  = "<a target=\"_new_\" href=\"";
   static String href_1b = " "+href_1;
   static String href_1regexp = ".*"+href_1+".*";
   static String href_2 = "\">";
   static String href_3 = "</a> ";
   
   /** Get body of chat message
    **/
   public String getBody() {
      return body;
   }
     
   /** Set body of chat message, adding html anchor tags as appropriate
    **/
   public void setBody(String body) {
      String formattedBody = body;
      
      // create hyperlinks from urls
      if ( formattedBody.matches(bodyRegexp) )
      {
         // split on spaces, parenthesis, and backslashes
         String[] parts = formattedBody.split("[\\s()\\\\]");
      
         for (int i=0; i<parts.length; i++)
         {
            if ( parts[i].matches(urlRegexp) )
            {
            	StringBuilder href = new StringBuilder(href_1b);
               href.append(parts[i]);
               href.append(href_2);
               href.append(parts[i]);
               href.append(href_3);
					
					// replace url that's preceded by space(s)
               formattedBody = 
						formattedBody.replaceFirst( cleanRegexp+parts[i], 
                                              href.toString() );
					// replace url that's preceded by parenthesis
               formattedBody = 
						formattedBody.replaceFirst( parenRegexp+parts[i], 
                                              "("+href.toString() );
					// replace url that's first in text
               formattedBody = 
						formattedBody.replaceFirst( firstRegexp+parts[i], 
                                              href.toString() );
            }
         }
      }
      this.body = formattedBody;
   }

   public ChatChannel getChatChannel() {
      return chatChannel;
   }
   public void setChatChannel(ChatChannel chatChannel) {
      this.chatChannel = chatChannel;
   }
   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public Date getMessageDate() {
      return messageDate;
   }
   public void setMessageDate(Date messageDate) {
      this.messageDate = messageDate;
   }
   public String getOwner() {
      return owner;
   }
   public void setOwner(String owner) {
      this.owner = owner;
   }
   
   
   /**
    * Serialize the resource into XML, adding an element to the doc under the top of the stack element.
    * 
    * @param doc
    *        The DOM doc to contain the XML (or null for a string return).
    * @param stack
    *        The DOM elements, the top of which is the containing element of the new "resource" element.
    * @return The newly added element.
    */
   @SuppressWarnings("unchecked")
   public Element toXml(Document doc, Stack stack)
   {
      //I don't think this will get called since chat messages don't appear to be archived. - chmaurer
      
      Element message = doc.createElement("message");

      if (stack.isEmpty())
      {
         doc.appendChild(message);
      }
      else
      {
         ((Element) stack.peek()).appendChild(message);
      }

      stack.push(message);

      //m_header.toXml(doc, stack);

      //FormattedText.encodeFormattedTextAttribute(message, "body", getBody());
      message.setAttribute("body", getBody());
      message.setAttribute("owner", getOwner());
      message.setAttribute("messageDate", Long.toString(getMessageDate().getTime()));
      message.setAttribute("channelId", getChatChannel().getId());

      /*
       * // Note: the old way to set the body - CDATA is too sensitive to the characters within -ggolden Element body = doc.createElement("body"); message.appendChild(body); body.appendChild(doc.createCDATASection(getBody()));
       */

      // properties
      //m_properties.toXml(doc, stack);

      stack.pop();

      return message;

   } // toXml

   /* (non-Javadoc)
    * @see org.sakaiproject.entity.api.Entity#getProperties()
    */
   public ResourceProperties getProperties() {
      // TODO Auto-generated method stub
      return null;
   }

   /* (non-Javadoc)
    * @see org.sakaiproject.entity.api.Entity#getReference()
    */
   public String getReference() {
      return ChatManager.REFERENCE_ROOT + Entity.SEPARATOR + ChatManager.REF_TYPE_MESSAGE + Entity.SEPARATOR + chatChannel.getContext() + Entity.SEPARATOR
      + chatChannel.getId() + Entity.SEPARATOR + id;
   }

   /* (non-Javadoc)
    * @see org.sakaiproject.entity.api.Entity#getReference(java.lang.String)
    */
   public String getReference(String rootProperty) {
      return getReference();
   }

   /* (non-Javadoc)
    * @see org.sakaiproject.entity.api.Entity#getUrl()
    */
   public String getUrl() {
      return ServerConfigurationService.getAccessUrl() + getReference();
   }

   /* (non-Javadoc)
    * @see org.sakaiproject.entity.api.Entity#getUrl(java.lang.String)
    */
   public String getUrl(String rootProperty) {
      return getUrl();
   }

   public String getMigratedMessageId() {
      return migratedMessageId;
   }

   public void setMigratedMessageId(String migratedMessageId) {
      this.migratedMessageId = migratedMessageId;
   }
   
}
