package com.mario3d.Displays.FPSBuffers;

import com.mario3d.GameManager;

public class FPSBufferStream<T> {
    private final FPSBufferInput<T> source;
    private final FPSBufferCalculator<T> buffercalc;
    private static final int gameTick = 40;
    private int last_input_fps;
    private int last_fps;
    public FPSBufferStream(FPSBufferCalculator<T> buffercalc, FPSBufferInput<T> source, int fps, T buffer_result) {
        this.buffercalc = buffercalc;
        this.source = source;
        last_tick = GameManager.getLatestTick();
        last_fps = fps;
        last_input_fps = fps;
        max_clock = (last_fps + gameTick - 1) / gameTick;
        inner_clock = max_clock;
        result = buffer_result;
        reset();
    }

    private T old;
    private T latest;
    private T result;
    private long last_tick;
    private int inner_clock;
    private int max_clock;
    public T read() {
        //情報が更新されていた場合
        long nowtick = GameManager.getLatestTick();
        if (nowtick != last_tick) {
            old = latest;
            latest = source.input();
            if (last_input_fps != last_fps) {
                last_fps = last_input_fps;
                max_clock = (last_fps + gameTick - 1) / gameTick;
            }
            inner_clock = 0;
            last_tick = nowtick;
            return buffercalc.buffer_calc(old, latest, result, 0);
        }
        if (inner_clock < max_clock) inner_clock++;
        result = buffercalc.buffer_calc(old, latest, result, (float)inner_clock / max_clock);
        return result;
    }

    public void setLastFPS(int fps) {last_input_fps = fps;}
    public void reset() {
        old = source.input();
        latest = old;
    }
}
