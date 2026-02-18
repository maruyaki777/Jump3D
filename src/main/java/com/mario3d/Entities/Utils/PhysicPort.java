package com.mario3d.Entities.Utils;

import com.mario3d.Utils.Physic;

public interface PhysicPort {
	public default Physic getPhysic() {return null;}
}
