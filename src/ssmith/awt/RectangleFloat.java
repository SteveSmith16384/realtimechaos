package ssmith.awt;

public class RectangleFloat {

	public float x, y, width, height;

	public RectangleFloat(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

    public boolean intersects(RectangleFloat r) {
	float tw = this.width;
	float th = this.height;
	float rw = r.width;
	float rh = r.height;
	if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
	    return false;
	}
	float tx = this.x;
	float ty = this.y;
	float rx = r.x;
	float ry = r.y;
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


}
