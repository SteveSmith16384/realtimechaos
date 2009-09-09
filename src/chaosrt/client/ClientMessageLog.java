package chaosrt.client;

import java.util.*;
import java.awt.*;

public class ClientMessageLog {

  private ArrayList messages = new ArrayList();
  private int max;

  public ClientMessageLog(int max_messages) {
    max = max_messages;
  }

  public void addMessage(String msg, byte priority) {
    messages.add(new Message(msg, priority));
    if (messages.size() > max) {
    	messages.remove(0);
    }
  }

  public Message getMessage(int no) {
    if (no < messages.size()) {
      Message message = (Message)messages.get(no);
      return message;
    } else {
      return null;
    }
  }

  public void draw(Graphics g, int x, int y) {
	  g.setColor(Color.white);
	  for(int m=0 ; m < max ; m++) {
		  Message message = getMessage(m);
		  if (message != null) {
			  if (message.text.length() > 0) {
				  //g.setColor(GetColourFromPriority(getMessage(m).priority));
				  if (m == messages.size()-1) {
					  g.setColor(Color.WHITE);
				  /*} else if (m == messages.size()-2) {
					  g.setColor(Color.LIGHT_GRAY);*/
				  } else {
					  g.setColor(Color.LIGHT_GRAY);
				  }
				  g.drawString(message.text, x, y + (m * 20));
			  }
		  }
	  }
  }
  
/*  private static Color GetColourFromPriority(byte priority) {
	  switch (priority) {
	  case 0:
		  return Color.lightGray;
	  case 1:
		  return Color.white;
	  case 2:
		  return Color.yellow;
	  default:
		  return Color.white;
	  }
  }
  */
  //************************************************************************************

  class Message {

    public String text;
    public byte priority;

    public Message(String t, byte p) {
      text = t;
      priority = p;
    }

  }

}

