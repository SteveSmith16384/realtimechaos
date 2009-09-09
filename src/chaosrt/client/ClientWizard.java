package chaosrt.client;

import java.awt.*;
import java.io.*;
import chaosrt.Client;
import chaosrt.Server;
import chaosrt.spell.Spell;

public class ClientWizard extends ClientObject {
	
	public String name;
	private ClientSpellRadius circle = new ClientSpellRadius(); 

  public ClientWizard(Client client, int no, int side) throws IOException {
    super(client, Server.WIZARD, no, side);
    this.filename = "wizard"+side;
  }

  public void draw(Graphics g) {
	  super.draw(g);
	  // Draw radius
	  if (this.side == Client.side) {
		  Spell spl = (Spell) Client.spellbook.get(Client.curr_spell);
		  if (spl.radius > 0) {
			  int x = this.rect.x + (this.rect.width / 2) - Client.view.x_pos - spl.radius;
			  int y = this.rect.y + (this.rect.height / 2) - Client.view.y_pos - spl.radius;
			  /*g.setColor(Color.white);
			  g.drawOval(x, y, spl.radius * 2, spl.radius * 2);*/
			  circle.draw(g, spl.radius, x, y);
		  }
	  }
	  
  }
  
  protected void manualDraw(Graphics g, int x, int y) {
	  // Draw name
	  if (name != null) {
		  g.setColor(Client.GetPlayerColour(side));
		  g.drawString(name, x, y+40);
	  }
  }
}
