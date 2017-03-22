package cz.upol.inf.vanusanik.jwlc.wlc;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class Modifiers {

	private int leds, mods;

	public Modifiers() {

	}

	public Modifiers(int leds, int mods) {
		this.leds = leds;
		this.mods = mods;
	}

	public int getLeds() {
		return leds;
	}

	public void setLeds(int leds) {
		this.leds = leds;
	}

	public int getMods() {
		return mods;
	}

	public void setMods(int mods) {
		this.mods = mods;
	}

	public wlc_modifiers to() {
		wlc_modifiers m = new wlc_modifiers();
		m.leds = leds;
		m.mods = mods;
		return m;
	}

	public static Modifiers from(wlc_modifiers mods) {
		return new Modifiers(mods.leds, mods.mods);
	}

	public static class wlc_modifiers extends Structure {

		public int leds, mods;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "leds", "mods" });
		}

	}
}
