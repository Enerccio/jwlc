package jwlc;

import java.util.List;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputResolutionCallback;

public class Example {

	public static void main(String[] args) throws Exception {
		new Example().run();
	}

	private JWLC wlc;

	private void relayout(Output output) {
		Size r = output.getVirtualResolution();
		if (r == null)
			return;

		List<View> views = output.getViews();
		int memb = views.size();

		int positioned = 0;
		for (int i = 0; i < memb; i++) {
			if (views.get(i).positioner.getAnchorRect() == null)
				++positioned;
		}

		boolean toggle = false;
		int y = 0;
		int n = (1 + positioned) / 2 > 1 ? (1 + positioned) / 2 : 1;
		int w = r.getW() / 2;
		int h = r.getH() / n;
		int ew = r.getW() - w * 2;
		int eh = r.getH() - h * 2;
		int j = 0;
		for (int i = 0; i < memb; i++) {
			View v = views.get(i);
			Geometry anchorRect = v.positioner.getAnchorRect();
			if (anchorRect == null) {
				Geometry g = new Geometry(toggle ? w + ew : 0, y,
						(!toggle && j == positioned - 1 ? r.getW() : (toggle ? w : w + ew)), 
						j < 2 ? h + eh : h);
				v.setGeometry(0, g);
				y = y + (!(toggle = !toggle) ? g.getSize().getH() : 0);
				++j;
			} else {
				Size sizeReq = v.positioner.getSize();
				if (sizeReq.getH() <= 0 || sizeReq.getW() <= 0) {
					Geometry current = v.getGeometry();
					sizeReq = current.getSize();
				}
				Geometry g = new Geometry(anchorRect.getOrigin(), sizeReq);
				View parent = v.getParent();
				if (parent != null) {
					Geometry parentGeometry = parent.getGeometry();
					g.getOrigin().setX(g.getOrigin().getX() + parentGeometry.getOrigin().getX());
					g.getOrigin().setY(g.getOrigin().getY() + parentGeometry.getOrigin().getY());
				}
				v.setGeometry(0, g);
			}			
		}
	}

	private void run() throws Exception {
		wlc = JWLC.getJWLCHandler();

		wlc.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(message);
			}
		});

		Output.setResolutionCallback(new OutputResolutionCallback() {

			public void onOutputResolution(Output handle, Size fromSize, Size toSize) {
				relayout(handle);
			}
		});

		wlc.init();
		wlc.run();
	}

}
