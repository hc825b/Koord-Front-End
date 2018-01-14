package testSim.apps.gototarget;

import java.util.HashMap;
import java.util.HashSet;
import edu.illinois.mitra.cyphyhouse.interfaces.MutualExclusion;
import java.util.List;
import java.util.Map;
import edu.illinois.mitra.cyphyhouse.functions.DSMMultipleAttr;
import edu.illinois.mitra.cyphyhouse.comms.RobotMessage;
import edu.illinois.mitra.cyphyhouse.gvh.GlobalVarHolder;
import edu.illinois.mitra.cyphyhouse.interfaces.LogicThread;
import edu.illinois.mitra.cyphyhouse.motion.MotionParameters;
import edu.illinois.mitra.cyphyhouse.motion.RRTNode;
import edu.illinois.mitra.cyphyhouse.motion.MotionParameters.COLAVOID_MODE_TYPE;
import edu.illinois.mitra.cyphyhouse.objects.ItemPosition;
import edu.illinois.mitra.cyphyhouse.objects.ObstacleList;
import edu.illinois.mitra.cyphyhouse.objects.PositionList;
import edu.illinois.mitra.cyphyhouse.interfaces.DSM;
import edu.illinois.mitra.cyphyhouse.functions.GroupSetMutex;

public class GototargetApp extends LogicThread {
	private static final String TAG = "Lineform App";
	private DSM dsm;

	int pid;
	private int numBots;

	private enum Stage {
		PICK, GO, WAIT
	};

	private Stage stage;

	ItemPosition target;
	ItemPosition position;

	public GototargetApp(GlobalVarHolder gvh) {
		super(gvh);
		MotionParameters.Builder settings = new MotionParameters.Builder();
		settings.COLAVOID_MODE(COLAVOID_MODE_TYPE.USE_COLBACK);
		MotionParameters param = settings.build();
		gvh.plat.moat.setParameters(param);

		pid = Integer.parseInt(name.replaceAll("[^0-9]", ""));
		numBots = gvh.id.getParticipants().size();
		dsm = new DSMMultipleAttr(gvh);
	}

	@Override
	public List<Object> callStarL() {

		// Initialize
		position = gvh.gps.getMyPosition();
		final int x = position.getX();
		final int y = position.getY();
		stage = Stage.PICK;

		while (true) {
			// Environment Turn
			if (target != null) {
				gvh.plat.moat.goTo(target);
			}

			sleep(100);

			// Program Turn
			//// Read sensors
			position = gvh.gps.getMyPosition();
			switch (stage) {
			case PICK:
				target = new ItemPosition("target", x, y);
				stage = Stage.GO;
				break;
			case GO:
				if (!(gvh.plat.moat.inMotion)) {
					stage = Stage.WAIT;
				}
				break;
			case WAIT:
				if (gvh.plat.moat.done) {
					stage = Stage.PICK;
				}
				break;
			}
		}
	}

	@Override
	protected void receive(RobotMessage m) {
	}
}
