package com.mario3d.Entities.Utils;

public class Aspect {
    public double x;//横回転
    public double y;//上下回転

    public Aspect(double x, double y) {
        set(x, y);
    }

    public Aspect(Aspect aspect) {
        this.x = aspect.x;
        this.y = aspect.y;
    }

    public void add(double x, double y) {
        if (this.y + y > 90) y = 90 - this.y;
        else if (this.y + y < -90) y = -90 - this.y;
        set(this.x + x, this.y + y);
    }

    public void set(double x, double y) {
        short bx = 1, by = 1;
        if (x < 0) {x *= -1; bx = -1;}
        if (y < 0) {y *= -1; by = -1;}
        x += 180.0; y += 180.0;
        if (x >= 360)x %= 360;
        if (y >= 360)y %= 360;
        this.x = (x - 180) * bx; this.y = (y - 180) * by;
        if (this.x == -180) this.x *= -1; if (this.y == -180) this.y *= -1;
    }
}
