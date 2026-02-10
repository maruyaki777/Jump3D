package com.mario3d.Events;

import com.mario3d.Entities.Entity;

public class MonsterDamageEvent extends GameEvent{
    public final Entity entity;

    /*
     * 0 -> 雑魚ダメージ　プレイヤーしか死なない
     * 1 -> ダメージ　一般的な敵なら死ぬ
     * 2 -> 大ダメージ（爆発ダメージ）　ほとんどの敵なら死ぬ
     * 3 -> 即死　どんなエンティティも死ぬ
     */
    public final int level;
    public MonsterDamageEvent(GameEventListener destination, Entity entity, int level) {
        super(GameEvent.EventType.MonsterDamage, destination);
        this.entity = entity;
        this.level = level;
    }
}
