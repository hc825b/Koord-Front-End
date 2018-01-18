package testSim.apps.uncertaingototarget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.apache.log4j.Logger;

import edu.illinois.mitra.cyphyhouse.interfaces.LogicThread;
import testSim.draw.Drawer;

public class GototargetDrawer extends Drawer {
    private Stroke stroke = new BasicStroke(8);
    private Color selectColor = new Color(0,0,255,100);
    private int prev_error = 0;
    private static org.apache.log4j.Logger log = Logger.getLogger(GototargetDrawer.class);
    @Override
    public void draw(LogicThread lt, Graphics2D g) {
        GototargetApp app = (GototargetApp) lt;
        g.setColor(Color.RED);
        g.setColor(selectColor);
        g.setStroke(stroke);
        if(app.error > 0 && app.error != prev_error)
        {
        	prev_error = app.error;
        	log.info("Error:"+String.valueOf(app.error));
        }
        //g.drawString("current total "+String.valueOf(app.currentTotal),100+10*app.robotIndex,150);
    }
}