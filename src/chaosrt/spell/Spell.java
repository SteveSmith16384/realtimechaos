package chaosrt.spell;

import ssmith.lang.*;
import java.io.*;
import java.awt.*;

import chaosrt.client.TileMapView;
import chaosrt.server.ServerPlayer;

public abstract class Spell {

  public static final int CREATURE_RANGE=3 * TileMapView.SQUARE_SIZE;

  public static final int DISBELIEVE=0, ORC=1, DWARF=2, INVISIBLE=3, GOOEY_BLOB=4, WALL=5;
  public static final int TURMOIL=6, KNIGHT=7, TROLL=8, OGRE=9, MAGIC_BOW=10, WIND=11;
  public static final int RESURRECT=12, MAGIC_FIRE=13, MOUNTED_SKELETON=14, ARCHER=15;
  public static final int TELEPORT=16, HEAL=17, SUBVERSION=18, LIGHTENING_BOLT=19, FREEZE=20;
  public static final int MUMMY=21, GHOST=22, HYDRA=23, SHADOW_WOOD=24, DRAGON=25, PSIREN=26;
  public static final int VORTEX=27, CLONE_WIZARD=28, ZONE_OF_DARKNESS=29, DOPPLEGANGER=30;
  public static final int CHANGE_FORM=31, ALTER_REALITY=32;
  private static final int MAX_SPELLS=32;

  public int type;
  public int cost;
  public String name;
  public int radius=0;

  /**
   * This is required so the client can create spells just from their number.
   */
  public static Spell SpellFactory(int type) {
	  switch(type) {
	  case DISBELIEVE:
		  return new SpellDisbelieve();
	  case ORC:
		  return new SpellOrc();
	  case DWARF:
		  return new SpellDwarf();
	  case KNIGHT:
		  return new SpellKnight();
	  case TROLL:
		  return new SpellTroll();
	  case OGRE:
		  return new SpellOgre();
	  case INVISIBLE:
		  return new SpellInvisible();
	  case MAGIC_BOW:
		  return new SpellMagicBow();
	  case GOOEY_BLOB:
		  return new SpellGooeyBlob();
	  case MAGIC_FIRE:
		  return new SpellMagicFire();
	  case WALL:
		  return new SpellWall();
	  case TURMOIL:
		  return new SpellTurmoil();
	  case WIND:
		  return new SpellWind();
	  case RESURRECT:
		  return new SpellResurrect();
	  case Spell.MOUNTED_SKELETON:
		  return new SpellMountedSkeleton();
	  case Spell.ARCHER:
		  return new SpellArcher();
	  case Spell.TELEPORT:
		  return new SpellTeleport();
	  case Spell.HEAL:
		  return new SpellHeal();
	  case Spell.SUBVERSION:
		  return new SpellSubversion();
	  case Spell.LIGHTENING_BOLT:
		  return new SpellLighteningBolt();
	  case Spell.FREEZE:
		  return new SpellFreeze();
	  case Spell.GHOST:
		  return new SpellGhost();
	  case Spell.MUMMY:
		  return new SpellMummy();
	  case Spell.HYDRA:
		  return new SpellHydra();
	  case Spell.SHADOW_WOOD:
		  return new SpellShadowWood();
	  case Spell.DRAGON:
		  return new SpellDragon();
	  case Spell.PSIREN:
		  return new SpellPsiren();
	  case Spell.VORTEX:
		  return new SpellVortex();
	  case Spell.CLONE_WIZARD:
		  return new SpellCloneWizard();
	  case Spell.ZONE_OF_DARKNESS:
		  return new SpellZoneOfDarkness();
	  case Spell.DOPPLEGANGER:
		  return new SpellDoppleganger();
	  case Spell.CHANGE_FORM:
		  return new SpellChangeForm();
	  case Spell.ALTER_REALITY:
		  return new SpellAlterReality();
	  default:
		  System.err.println("Unknown spell " + type + " in spell factory.");
	  	  return SpellFactory(type);  // Try again
	  }
  }
  
  public static Spell GetRandomSpell() {
    return SpellFactory(Functions.rnd(1, MAX_SPELLS)); // Does not include disbel
  }

  protected Spell(int type, int cost, String name, int rad) {
    this.type = type;
    this.cost = cost;
    this.name = name;
    this.radius = rad;
  }

  /**
   * This can be over-ridden to choose a dif colour;
   */
  public Color getColour() {
    return Color.yellow;
  }

  /**
   * Returns whether succesful or not.
   */
  public abstract boolean cast(ServerPlayer player, int x, int y, boolean illusion) throws IOException;

  public abstract void process() throws IOException; // Run by the Server

}
