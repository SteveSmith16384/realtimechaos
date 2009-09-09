package ssmith.awt;

public class RectangleDouble {

	public double x, y, width, height;

	public RectangleDouble(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

    public boolean intersects(RectangleDouble r) {
	double tw = this.width;
	double th = this.height;
	double rw = r.width;
	double rh = r.height;
	if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
	    return false;
	}
	double tx = this.x;
	double ty = this.y;
	double rx = r.x;
	double ry = r.y;
	rw += rx;
	rh += ry;
	tw += tx;
	th += ty;
	//      overflow || intersect
	return ((rw < rx || rw > tx) &&
		(rh < ry || rh > ty) &&
		(tw < tx || tw > rx) &&
		(th < ty || th > ry));

    }

    public boolean contains(int X, int Y) {
      double w = this.width;
      double h = this.height;

      // Note: if either dimension is zero, tests below must return false...
      double x = this.x;
      double y = this.y;
      if (X < x || Y < y) {
	  return false;
      }
      w += x;
      h += y;
      //    overflow || intersect
      return ((w < x || w > X) &&
	      (h < y || h > Y));
    }


}
