package chaosrt.server;

import ssmith.lang.*;
import java.io.*;
import chaosrt.Server;

public abstract class ServerPerson extends ServerObject {

  public double lightening_timer=0, frozen_timer=0;

  public ServerPerson(String obj_name, int type, double x, double y, int side, boolean illusion) throws IOException {
    super(obj_name, type, x, y, side, true);
    this.illusion = illusion;
  }

  public void process() throws IOException {
		if (this.invisible_timer > 0) {
			this.invisible_timer--;
			if (this.invisible_timer == 0) {
				this.show();
				this.sendInvisible();
			}
		}
		if (this.lightening_timer > 0) {
			this.lightening_timer--;
			if (this.lightening_timer == 0) {
				this.can_shoot = false;
			}
		}
		if (this.frozen_timer > 0) {
			this.frozen_timer--;
			if (this.frozen_timer == 0) {
				this.sendFrozenToAll();
			}
		}

		super.process();
		if (this.can_shoot) {
			super.checkShotTarget();
		}
		checkDestTarget();
		if (this.frozen_timer == 0) {
			super.moveToDest();
		}
	}

	public boolean canTraverse(int type) {
		if (type == Server.GRASS || type == Server.ROAD || type == Server.MUD) {
			return true;
		} else {
			return false;
		}
	}

	public boolean collidedWith(ServerObject other) {
		if (this.rect.intersects(other.rect)) {
			return true;
		}
		return false;
	}

	/**
	 * Make sure attackers and defenders have equal chance, as troops will only
	 * attack if they are walking into the enemy.
	 */
	public void hasCollidedWith(ServerObject o) throws IOException {
		if (o instanceof ServerPerson) {
			if (this.side != o.side) {
				this.combat(o);
			}
		}
	}

	private void combat(ServerObject o) throws IOException {
		// Make each other each other's targets so they are "engaged in combat"
		this.dest_target = o;
		o.dest_target = this;

		S2CCommunication.sendAttackFlashToAll(this, o);
		int our_damage = 0, their_damage = 0;
		// We attack other first
		if (this.undead == true || o.undead == false
				|| Server.use_undead == false) {
			int att_pow = Functions.rnd(0, this.att);
			int def_pow = Functions.rnd(0, o.def);
			if (att_pow > def_pow) {
				our_damage = att_pow - def_pow;
				//System.out.println(this.name + " did " + (att_pow-def_pow) + " damage.");
			}
		} else {
			S2CCommunication.sendUndeadFlashToAll(this);
		}

		// Now they attack us
		if (this.undead == false || o.undead == true
				|| Server.use_undead == false) {
			int def_pow = Functions.rnd(0, this.def);
			int att_pow = Functions.rnd(0, o.att);
			if (att_pow > def_pow) {
				their_damage = att_pow - def_pow;
				//System.out.println(o.name + " did " + (att_pow-def_pow) + " damage.");
			}
		} else {
			S2CCommunication.sendUndeadFlashToAll(this);
		}

		o.damaged(our_damage, true);
		this.damaged(their_damage, true);
	}

	/**
	 * This overrides the function in ServerObject to take into account invisibles.
	 */
	protected void showToPlayer(int p) throws IOException {
		if (this.side != Server.players[p].side && this.invisible_timer > 0) {
			// Do not show if invisible
			return;
		} else {
			super.showToPlayer(p);
		}
	}

	public void sendFrozenToAll() throws IOException {
		for (byte p = 0; p < Server.players.length; p++) {
			Server.net_server.getConnection(p).getOutput().writeByte(
					Server.FROZEN_IB);
			Server.net_server.getConnection(p).getOutput().writeInt(this.no);
			Server.net_server.getConnection(p).getOutput().writeBoolean(
					this.frozen_timer > 0);
		}
	}

	public void sendInvisible() throws IOException {
		for (byte p = 0; p < Server.players.length; p++) {
			Server.net_server.getConnection(p).getOutput().writeByte(
					Server.INVISIBLE_IB);
			Server.net_server.getConnection(p).getOutput().writeInt(this.no);
			Server.net_server.getConnection(p).getOutput().writeBoolean(
					this.invisible_timer > 0);
		}
	}

	// Call this if the thing is killed.
	public void killed(boolean corpse) throws IOException {
		// Create corpse
		if (this.undead == false && corpse == true) {
			new ServerCorpse(this);
		}
		super.killed(false);
	}

	protected String getUpdateText() {
		return "" + this.current_health;
	}

}
