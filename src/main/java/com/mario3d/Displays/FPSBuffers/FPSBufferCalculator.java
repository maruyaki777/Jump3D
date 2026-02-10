package com.mario3d.Displays.FPSBuffers;

@FunctionalInterface
public interface FPSBufferCalculator<T> {
    public T buffer_calc(T older, T newer, T result, float ratio);
}
