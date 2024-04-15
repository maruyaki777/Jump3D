package com.mario3d.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mario3d.CubeEntity.CubeEntity;
import com.mario3d.Cubes.Cube;
import com.mario3d.Entities.Entity;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldChunkPosition;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.WorldSystem.World.Chunk;

public class Collision {

    public final double dx;
    public final double dy;
    public final Entity entity;
    public boolean touch[];//-x -y -z x y z
    protected WorldPosition climbed;
    
    //dxは座標からの半径（四角形）dyは座標からの高さ
    public Collision(Entity entity, double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        this.entity = entity;
        touch = new boolean[6];
        climbed = null;
    }

    public WorldPosition calc(WorldPosition from, WorldPosition to) {
        WorldChunkPosition wcp = to.getChunkPosition();
        List<Cube> calced = new ArrayList<>();//すでに計算済みのをもう一度計算するのを防ぐため
        HashMap<WorldChunkPosition, Chunk> map = GameScene.world.map;//文を見やすく
        double[] vec = new double[] {to.x - from.x, to.y - from.y, to.z - from.z};//移動している方向を得るためのもの
        touch = new boolean[6];//接地判定の再計算のため
        RelativePosition rp = new RelativePosition(to.x - from.x, to.y - from.y, to.z - from.z);//一番距離が短いのを選択するためのもの。
        RelativePosition climb = null;
        for (int i = -1;i <= 1;i++) for (int j = -1;j <= 1;j++) {
            wcp.x += i; wcp.z += j;
            Chunk chunk = map.get(wcp);
            wcp.x -= i; wcp.z -= j;
            if (chunk == null) continue;//チャンクが存在しないなら戻る
            for (Cube cube : chunk.container) {
                if (calced.contains(cube)) continue;//計算済みなら戻る
                Result r = withCube(cube, from, to, vec);
                RelativePosition t;
                //登れるなら高い方を代入する。（当たり判定の計算は後から行うあくまで登れるかどうか）
                if (climb == null) climb = r.climb;
                else if (r.climb != null && climb.y < r.climb.y) climb = r.climb;
                t = r.rp;
                if ((rp.x - t.x) * get1(vec[0]) > 0) rp.x = t.x;//一番距離が短いのにする
                if ((rp.y - t.y) * get1(vec[1]) > 0) rp.y = t.y;
                if ((rp.z - t.z) * get1(vec[2]) > 0) rp.z = t.z;
            }
        }
        for (Entity e : GameScene.world.entities) {
            if (e.equals(this.entity)) continue;
            else if (e instanceof CubeEntity) {
                RelativePosition t = withCubeEntity((CubeEntity)e, from, to, vec);
                if ((rp.x - t.x) * get1(vec[0]) > 0) rp.x = t.x;//一番距離が短いのにする
                if ((rp.y - t.y) * get1(vec[1]) > 0) rp.y = t.y;
                if ((rp.z - t.z) * get1(vec[2]) > 0) rp.z = t.z;
            }
            else withEntity(e, from);;
        }
        WorldPosition resultpos = rp.getAbsolutePos(from);
        if (climb == null) this.climbed = null;
        else this.climbed = climb.getAbsolutePos(from);
        return resultpos;
    }

    private static int get1(double a) {
        if (a == 0) return 0;
        else if (a > 0) return 1;
        else return -1;
    }

    private boolean inrange(WorldPosition target, Cube c) {
        int r = 0;
        if (c.BasePos.x < target.x + dx && target.x - dx < c.BasePos.x + c.dx) r++;
        if (c.BasePos.y < target.y + dy && target.y < c.BasePos.y + c.dy) r++;
        if (c.BasePos.z < target.z + dx && target.z - dx < c.BasePos.z + c.dz) r++;
        if (r >= 3) return true;
        return false;
    }

    private boolean inrange(WorldPosition target, CubeEntity c) {
        int r = 0;
        if (c.pos.x + c.BasePos.x < target.x + dx && target.x - dx < c.pos.x + c.BasePos.x + c.dx) r++;
        if (c.pos.y + c.BasePos.y < target.y + dy && target.y < c.pos.y + c.BasePos.y + c.dy) r++;
        if (c.pos.z + c.BasePos.z < target.z + dx && target.z - dx < c.pos.z + c.BasePos.z + c.dz) r++;
        if (r >= 3) return true;
        return false;
    }

    private boolean inrangeXZ(WorldPosition target, Cube c) {
        int r = 0;
        if (c.BasePos.x < target.x + dx && target.x - dx < c.BasePos.x + c.dx) r++;
        if (c.BasePos.z < target.z + dx && target.z - dx < c.BasePos.z + c.dz) r++;
        if (r >= 2) return true;
        return false;
    }

    private boolean inrangeXZ(WorldPosition target, CubeEntity c) {
        int r = 0;
        if (c.pos.x + c.BasePos.x < target.x + dx && target.x - dx < c.pos.x + c.BasePos.x + c.dx) r++;
        if (c.pos.z + c.BasePos.z < target.z + dx && target.z - dx < c.pos.z + c.BasePos.z + c.dz) r++;
        if (r >= 2) return true;
        return false;
    }

    private Result withCube(Cube cube, WorldPosition from, WorldPosition to, double[] vec) {
        Result result;
        double xt=to.x, yt=to.y, zt=to.z;//最終的な行先計算用
        int xv=get1(vec[0]), yv=get1(vec[1]), zv=get1(vec[2]);
        int[] tar = null;
        int slot = 0;
        boolean collided = false;
        boolean climb = false;
        if (inrange(to, cube)) {
            //x
            if (xv != 0) {
                if (xv < 0) slot = 3;
                tar = cube.face[slot + 0];
                if (((xv * dx + to.x) - cube.corners[tar[0]].x) * xv >= 0 && ((xv * dx + from.x) - cube.corners[tar[0]].x) * xv <= 0) {//式簡略化必須
                    xt = cube.corners[tar[0]].x - (dx * xv);
                    touch[3 - slot] = true;
                    collided = true;
                    if (vec[1] == 0 && canclimbheight(to, cube)) climb = true;
                }
                slot = 0;
            }
            //y
            if (yv != 0) {
                if (yv < 0) slot = 3;
                tar = cube.face[slot + 1];
                double tdy = 0; if (yv > 0) tdy = dy;
                if (((yv * tdy + to.y) - cube.corners[tar[1]].y) * yv >= 0 && ((yv * tdy + from.y) - cube.corners[tar[1]].y) * yv <= 0) {//tar[i]は全部tar[0]に置き換えれる。
                    yt = cube.corners[tar[1]].y - tdy;
                    touch[4 - slot] = true;
                    collided = true;
                }
                slot = 0;
            }
            else if (to.y - 0.1 - cube.corners[cube.face[4][0]].y <= 0 && to.y + dy - cube.corners[cube.face[4][0]].y > 0) {touch[1] = true;collided = true;}
            //z
            if (zv != 0) {
                if (zv < 0) slot = 3;
                tar = cube.face[slot + 2];
                if (((zv * dx + to.z) - cube.corners[tar[2]].z) * zv >= 0 && ((zv * dx + from.z) - cube.corners[tar[2]].z) * zv <= 0) {
                    zt = cube.corners[tar[2]].z - (dx * zv);
                    touch[5 - slot] = true;
                    collided = true;
                    if (vec[1] == 0 && canclimbheight(to, cube)) climb = true;
                }
                slot = 0;
            }
        }
        else if (inrangeXZ(to, cube) && vec[1] == 0 && to.y - 0.1 - cube.corners[cube.face[4][0]].y <= 0 && to.y + dy - cube.corners[cube.face[4][0]].y > 0) {touch[1] = true;collided = true;}
        //当たっているか
        if (climb) result = new Result(new RelativePosition(xt - from.x, yt - from.y, zt - from.z), new RelativePosition(to.x - from.x, cube.BasePos.y + cube.dy - from.y + 0.01, to.z - from.z));
        else result = new Result(new RelativePosition(xt - from.x, yt - from.y, zt - from.z));
        if (collided) GameEvent.add(new CollisionEvent(cube, entity, new WorldPosition(from)));
        return result;
    }

    private RelativePosition withCubeEntity(CubeEntity cube, WorldPosition from, WorldPosition to, double[] vec) {
        RelativePosition result;
        double xt=to.x, yt=to.y, zt=to.z;//最終的な行先計算用
        int xv=get1(vec[0] - (cube.pos.x - cube.oldpos.x)), yv=get1(vec[1] - (cube.pos.y - cube.oldpos.y)), zv=get1(vec[2] - (cube.pos.y - cube.oldpos.y));
        int[] tar = null;
        int slot = 0;
        boolean collided = false;
        if (inrange(to, cube)) {
            //x
            if (xv != 0) {
                if (xv < 0) slot = 3;
                tar = cube.face[slot + 0];
                if (((xv * dx + to.x) - (cube.corners[tar[0]].x + cube.pos.x)) * xv >= 0 && ((xv * dx + from.x) - (cube.corners[tar[0]].x + cube.pos.x)) * xv <= 0) {//式簡略化必須
                    xt = cube.corners[tar[0]].x + cube.pos.x - (dx * xv);
                    touch[3 - slot] = true;
                    collided = true;
                }
                slot = 0;
            }
            //y
            if (yv != 0) {
                if (yv < 0) slot = 3;
                tar = cube.face[slot + 1];
                double tdy = 0; if (yv > 0) tdy = dy;
                if (((yv * tdy + to.y) - (cube.corners[tar[1]].y + cube.pos.y)) * yv >= 0 && ((yv * tdy + from.y) - (cube.corners[tar[1]].y + cube.pos.y)) * yv <= 0) {//tar[i]は全部tar[0]に置き換えれる。
                    yt = cube.corners[tar[1]].y + cube.pos.y - tdy;
                    touch[4 - slot] = true;
                    collided = true;
                }
                slot = 0;
            }
            else if (to.y - 0.1 - (cube.corners[cube.face[4][0]].y + cube.pos.y) <= 0 && to.y + dy - (cube.corners[cube.face[4][0]].y + cube.pos.y) > 0) {touch[1] = true;}
            //z
            if (zv != 0) {
                if (zv < 0) slot = 3;
                tar = cube.face[slot + 2];
                if (((zv * dx + to.z) - (cube.corners[tar[2]].z + cube.pos.z)) * zv >= 0 && ((zv * dx + from.z) - (cube.corners[tar[2]].z + cube.pos.z)) * zv <= 0) {
                    zt = cube.corners[tar[2]].z + cube.pos.z - (dx * zv);
                    touch[5 - slot] = true;
                    collided = true;
                }
                slot = 0;
            }
        }
        else if (inrangeXZ(to, cube) && vec[1] == 0 && to.y - 0.1 - (cube.corners[cube.face[4][0]].y + cube.pos.y) <= 0 && to.y + dy - (cube.corners[cube.face[4][0]].y + cube.pos.y) > 0) {touch[1] = true;collided = true;}
        result = new RelativePosition(xt - from.x, yt - from.y, zt - from.z);
        if (collided) GameEvent.add(new CollisionEvent(cube, entity, new WorldPosition(from)));
        return result;
    }

    private void withEntity(Entity e, WorldPosition pos) {
        if (e.collision == null) return;
        Collision ec = e.collision;
        WorldPosition epos = e.pos;
        if (epos.x - ec.dx <= pos.x + this.dx && epos.x + ec.dx >= pos.x - this.dx &&
            epos.y <= pos.y + this.dy && epos.y + ec.dy >= pos.y &&
            epos.z - ec.dx <= pos.z + this.dx && epos.z + ec.dx >= pos.z - this.dx) {
            
            GameEvent.add(new CollisionEvent(e, entity, new WorldPosition(pos)));
        }
    }

    private boolean canclimbheight(WorldPosition playerpos, Cube cube) {
        final double ratio = 4;
        if (playerpos.y + (this.dy / ratio) > cube.BasePos.y + cube.dy) return true;
        return false;
    }

    private static class Result {
        public final RelativePosition rp;
        public final RelativePosition climb;

        public Result(RelativePosition rp) {
            this.rp = rp;
            this.climb = null;
        }

        public Result(RelativePosition rp, RelativePosition climb) {
            this.rp = rp;
            this.climb = climb;
        }
    }

    /*public static void main(String[] args) {
        Collision c = new Collision(null, 0.5, 2);
        CubeEntity cube = new Lift(new WorldPosition(2, 0, 0));
        RelativePosition rp = c.withCubeEntity(cube, new WorldPosition(2, 1, 0), new WorldPosition(2, -0.5, 0), new double[]{0, -1.1, 0});
        System.out.println(cube.corners[cube.face[3][0]].x + cube.pos.x);
        System.out.printf("%f  %f  %f\n", rp.x, rp.y, rp.z);
    }*/




    //=================
    //ここから段差処理
    //=================

    //返り値は登れる場合はそのWorldPosition。登れない場合はnull。
    public WorldPosition calc_up_stair(WorldPosition from) {
        WorldPosition stairpos = climbed, result = null, climbedpos, fromtmp;
        boolean[] touchbuf = this.touch;
        if (climbed == null) return null;
        fromtmp = new WorldPosition(from);
        fromtmp.y = stairpos.y;
        RelativePosition check = new RelativePosition(stairpos.x - fromtmp.x, stairpos.y - fromtmp.y, stairpos.z - fromtmp.z);
        if (check.getAbsolutePos(fromtmp).equals(climbedpos = calc(fromtmp, stairpos))) result = stairpos;
        else if (climbedpos.x == stairpos.x) result = new WorldPosition(stairpos.x, stairpos.y, climbedpos.z);
        else if (climbedpos.z == stairpos.z) result = new WorldPosition(climbedpos.x, stairpos.y, stairpos.z);
        if (result == null) {this.touch = touchbuf;return null;}
        boolean[] touchbuf_climb = this.touch;
        climbed = stairpos;
        fromtmp.x = stairpos.x; fromtmp.y = from.y - dy; fromtmp.z = stairpos.z;
        check.x = 0; check.y = stairpos.y - fromtmp.y; check.z = 0;
        if (check.getAbsolutePos(fromtmp).equals(calc(fromtmp, stairpos)) == false) {result = null;this.touch = touchbuf;}
        else {this.touch = touchbuf_climb;}
        climbed = stairpos;
        return result;
    }
}
