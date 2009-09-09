package chaosrt.client;

import java.awt.Color;
import java.awt.Graphics;

import ssmith.util.Interval;

public class ClientSpellRadius {
	
	private static final int MAX_COLS = 20;
	
	private Color cols[] = new Color[MAX_COLS];
	private int current_col=0;
	private Interval change_col_interval = new Interval(100);
	
	public ClientSpellRadius() {
		super();
		int rgb=255;
		for(int i=0 ; i<MAX_COLS ; i++) {
			cols[i] = new Color(rgb, rgb, rgb);
			int half_cols = MAX_COLS/2; 
			if (i < half_cols) {
				rgb -= (250/half_cols);
			} else {
				rgb += (250/half_cols);
			}
			//System.out.println(rgb);
		}
	}

	
	public void draw(Graphics g, int radius, int x, int y) {
		if (change_col_interval.hitInterval()) {
			current_col++;
			if (current_col >= MAX_COLS) {
				current_col = 0;
	
			}
		}

		//System.out.println("Col:" + current_col);
		g.setColor(cols[current_col]);
		g.drawOval(x, y, radius * 2, radius * 2);
	}

}
