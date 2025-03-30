package com.mario3d;

import java.util.LinkedList;
import java.util.Queue;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.mario3d.Displays.SceneManager;
import com.mario3d.Displays.Textures;

public class Display implements GLEventListener{

    public static final int default_width = 1485;
    public static final int default_height = 810;
    public static boolean finishedinit = false;
    public static int target_fps = 140;

    protected FPSAnimator animator;
    protected GLU glu;
    protected GLWindow glWindow;
    public int width;
    public int height;
    private Queue<Float> lastbuffps = new LinkedList<>();

    public Display() {
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        glu = new GLU();
        System.setProperty("newt.window.icons", "assets/textures/misc/player2D.png, assets/textures/misc/player2D.png");
        GLWindow glWindow = GLWindow.create(caps);
        this.glWindow = glWindow;
        glWindow.setTitle("Jump3D");
        glWindow.setSize(default_width, default_height);
        glWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent event) {
            	if (!GameManager.isGameError()) System.exit(0);
            }
        });
        glWindow.addGLEventListener(this);
        animator = new FPSAnimator(target_fps);
        animator.add(glWindow);
        animator.start();
        glWindow.setVisible(true);
    }
    
    public void setFPS(int fps) {
        animator.setFPS(fps);
        synchronized (this) {
            lastbuffps = new LinkedList<>();
        }
    }
    public float getFPS() {return fps_realtime;}
    public float getAverageFPS() {return fps_average;}
    private long last_time;
    private long last_ave_time;
    private float fps_average = 0;
    private float fps_realtime = 0.1f;

    private static final double fovy = 60.0;

    @Override
    public void init(GLAutoDrawable	drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1f);
        SceneManager.init();
        Textures.init();
        finishedinit = true;
        last_time = 0;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //GL2 gl = drawable.getGL().getGL2();
        this.width = width;
        this.height = height;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        /*fps計算 */
        long nowtime = System.currentTimeMillis();
        float deltatime = nowtime - last_time != 0 ? nowtime - last_time : 0.1f;
        fps_realtime = 1000.0f / deltatime;
        last_time = nowtime;
        synchronized (this) {
            lastbuffps.add(Float.valueOf(fps_realtime));
            if (nowtime - last_ave_time > 1000) {
            	last_ave_time = nowtime;
            	float t = 0;
            	int s = lastbuffps.size();
            	for (int i = 0;i < s;i++) t += lastbuffps.poll().floatValue();
            	t /= s;
            	fps_average = t;
            }
        }
        /*ここまで */

        try {
        	GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            if (!finishedinit) return;
            //long start = System.currentTimeMillis();
            SceneManager.display(gl);
        }
        catch (Exception e) {
        	e.printStackTrace();
        	GameManager.error(e, Display.class);
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (animator != null) animator.stop();
    }

    //2Dの描写を行う前に実行する
    public void display2D(GL2 gl) {
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
    }

    //3Dの描写を行う前に実行する
    public void display3D(GL2 gl) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(fovy, (double) this.width / (double) this.height, 0.1, 300.0);
        glu.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display3D(GL2 gl, double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(fovy, (double) this.width / (double) this.height, 0.1, 300.0);
        glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0.0f, 1.0f, 0.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
