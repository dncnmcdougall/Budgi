package GUI.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ProgressBar extends JComponent {

	private double max;
	private double min;
	private double value;
	private double target;

	private Area left;
	private Area midLowA;
	private Area midHighA;
	private Area right;

	private Color fore;
	private Color fore2;
	private Color back;
	private Color back2;

	private Color midLow;
	private Color midLow2;

	private Color midHigh;
	private Color midHigh2;

	private boolean useMid;

	private boolean reversed;

	public ProgressBar(double max, double min, double value, boolean reversed,
			Color fore, Color back, Color midHigh, Color midLow, boolean useMid) {
		super();
		this.max = max;
		this.min = min;
		this.reversed = reversed;
		this.useMid = useMid;
		if (reversed) {
			this.value = (max - value);

			this.back = fore;
			this.back2 = fore.darker();
			this.fore = back;
			this.fore2 = back.brighter();

			this.midHigh = midLow;
			this.midHigh2 = midLow.brighter();
			this.midLow = midHigh;
			this.midLow2 = midHigh.darker();

		} else {
			this.value = value;

			this.back = back;
			this.back2 = back.brighter();
			this.fore = fore;
			this.fore2 = fore.darker();

			this.midHigh = midHigh;
			this.midHigh2 = midHigh.brighter();

			this.midLow = midLow;
			this.midLow2 = midLow.darker();
		}

		init();
		update();
	}

	public ProgressBar(double max, double min, double value, boolean reversed,
			Color fore, Color back) {
		this(max, min, value, reversed, fore, back, Color.blue, Color.red,
				false);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		if (!reversed) {
			this.value = value;
		} else {
			this.value = (max - value);
		}
		update();
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		if (!reversed) {
			this.target = target;
		} else {
			this.target = (max - target);
		}
		update();
	}

	private void init() {
		setPreferredSize(new Dimension(25, 25));

		setLocale(null);

		left = new Area(fore, fore2, Area.Vertical);
		add(left);

		midLowA = new Area(midLow, midLow2, Area.Vertical);
		add(midLowA);

		midHighA = new Area(midHigh, midHigh2, Area.Vertical);
		add(midHighA);

		right = new Area(back, back2, Area.Vertical);
		add(right);
	}

	public void update() {
		int width = getWidth();
		int height = getHeight();

		double vt = (value - min) / (max - min);

		double tt = (target - min) / (max - min);

		Color borderColor = new Color(180, 180, 225);

		if (!useMid || vt == tt) {
			
			int m =(int) Math.round(width * vt);
			
			left.setBounds(5, 5, m, height - 5);
			left.setBorder(new MatteBorder(1, 1, 1, 1, borderColor));
			right.setBounds(m, 5,
					width -m, height - 5);
			right.setBorder(new MatteBorder(1, 1, 1, 1, borderColor));

			midHighA.setBounds(0, 0, 0, 0);
			midLowA.setBounds(0, 0, 0, 0);

		} else if (vt < tt) {
			
			int m =  (int) Math.round(width * vt);
			int n = (int) Math.round(width * tt);
			
			left.setBounds(5, 5, m, height - 5);
			left.setBorder(new MatteBorder(1, 1, 1, 2, borderColor));

			midHighA.setBounds(m, 5,
					n-m, height - 5);
			midHighA.setBorder(new MatteBorder(1, 0, 1, 0, borderColor));
			right.setBounds(n, 5,
					width - n, height - 5);
			right.setBorder(new MatteBorder(1, 0, 1, 1, borderColor));

			midLowA.setBounds(0, 0, 0, 0);
		} else {
			int m =  (int) Math.round(width * vt);
			int n = (int) Math.round(width * tt);
			
			left.setBounds(5, 5, n, height - 5);
			left.setBorder(new MatteBorder(1, 1, 1, 0, borderColor));
			midLowA.setBounds(n, 5,
					m-n, height - 5);
			midLowA.setBorder(new MatteBorder(1, 0, 1, 2, borderColor));
			right.setBounds(m, 5,
					width-m, height - 5);
			right.setBorder(new MatteBorder(1, 0, 1, 1, borderColor));

			midHighA.setBounds(0, 0, 0, 0);
		}
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
		update();
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
		update();
	}

	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(boolean reversed) {
		this.reversed = reversed;
		update();
	}

	public boolean isUseMid() {
		return useMid;
	}

	public void setUseMid(boolean useMid) {
		this.useMid = useMid;
		update();
	}

}
