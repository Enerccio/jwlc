package jwlc;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;

public class ExampleSimpleCallback {

	public static void main(String[] args) throws Exception {
		final JWLC wlc = JWLC.getJWLCHandler();
		
		wlc.setLoggerCallback(new LoggerCallback() {
			
			public void onLog(LogType type, String message) {
				System.out.println(type + ": " + message);
			}
		});
		
		Output.setCreatedCallback(new OutputCreatedCallback() {
			
			public boolean outputCreated(Output output) {
				System.out.println("Output id " + output);
				System.out.println(output.getViews());
				return false;
			}
		});
		
		wlc.init();
		wlc.run();
	}

}
