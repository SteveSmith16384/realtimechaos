package ssmith.opengl;

public class Functions {

  /**
   * This gets the angle from one point to another, in degrees.
   */
/*  public static float getAngle(float x1, float y1, float x2, float y2) {
    float x = (x1 - x2);
    float y = (y1 - y2);
    if (x>0 && y>0) {
      return (float) Math.toDegrees(Math.atan(x/y));
    } else if (x<0 && y>0) {
      return (float) Math.toDegrees(Math.atan(x/y)) + 360;
    } else if (x>0 && y<0) {
      return (float) Math.toDegrees(Math.atan(x/y)) + 180;
    } else {
      return (float) Math.toDegrees(Math.atan(x/y)) + 180;
    }
  }
*/
  public static double getAngle(double x1, double y1, double x2, double y2) {
    double x = (x1 - x2);
    double y = (y1 - y2);
    if (x>0 && y>0) {
      return Math.toDegrees(Math.atan(x/y));
    } else if (x<0 && y>0) {
      return Math.toDegrees(Math.atan(x/y)) + 360;
    } else if (x>0 && y<0) {
      return Math.toDegrees(Math.atan(x/y)) + 180;
    } else {
      return Math.toDegrees(Math.atan(x/y)) + 180;
    }
  }

  /**
   * This gets the angle from one point to another, in RADIANS.  0 rads is left I think.
   */
/*  public static double getAngle2InRadians(double x1, double y1, double x2, double y2) {
    return Math.atan2(y2-y1, x2-x1);
  }
*/
  /**
   * This gets the angle from one point to another.  0 deg is straight up.
   * This assumes 0, 0 is top left, and positive values take you south-east.
   * It will return -180 to +180.
   *
   *      180
   *       |
   *    -90.-90
   *       0
   */
  public static double getAngleInDegrees(double x1, double y1, double x2, double y2) {
    return Math.toDegrees(Math.atan2(x2-x1, y2-y1));
  }

  /**
   * This gets the angle from one point to another, in RADIANS.  0 rads is UP.
   * This assumes 0, 0 is top left, and positive values take you south-east.
   * It will return -180 to +180.
   *
   *      180
   *       |
   *    -90.-90
   *       0
   */
  public static double getAngleInRadians(double x1, double y1, double x2, double y2) {
    return Math.atan2(x2-x1, y2-y1);
  }

  //***********************************
  public static void main(String args[]) {
    System.out.println(getAngleInDegrees(0f, 0f, 0f, -1f));
    System.out.println(getAngleInDegrees(0f, 0f, 1f, -1f));
    System.out.println(getAngleInDegrees(0f, 0f, -1f, -1f));
    System.out.println(getAngleInDegrees(0f, 0f, -1f, 1f));
  }

}
