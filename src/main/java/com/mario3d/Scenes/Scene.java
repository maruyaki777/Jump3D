package com.mario3d.Scenes;

//名前をわかりやすいようにrunnableではなく独自に用意
public interface Scene {
    //Sceneの変更がない場合はnullを返す
    public Scene execute();
    //シーンの入れ替わりで最初に行われる関数
    public void init();
    //シーンの終了時に行われる関数
    default public void finish() {}
}
