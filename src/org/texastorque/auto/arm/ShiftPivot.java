package org.texastorque.auto.arm;

import org.texastorque.auto.AutoCommand;
import org.texastorque.auto.AutoManager;
import org.texastorque.feedback.Feedback;

public class ShiftPivot extends AutoCommand {

	private int setpointIndex;
	private final double time;

	public ShiftPivot(int setpointIndex, int time) {
		this.setpointIndex = setpointIndex;
		this.time = time;
	}

	@Override
	public void run() {
		Feedback.getInstance().resetArmEncoders();

		input.setPTSetpoint(setpointIndex);
		AutoManager.pause(time);
	}

	@Override
	public void reset() {
		setpointIndex = 0;
	}
}
