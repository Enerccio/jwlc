# JWLC - Java bindings for [libwlc](https://github.com/Cloudef/wlc) 

**Currently wrapped version** - 6a05b372d89fcaa884b7bb9694b55a7935506db6

## Compiling and running examples

Compile example: `javac -cp "./jna-4.4.0.jar:./jwlc-0.0.8.jar" jwlc/Example.java`

Run example: `java -cp "./jna-4.4.0.jar:./jwlc-0.0.8.jar:./" jwlc/Example`

## Example

This is the example from libwlc's readme.md but in Java.

```java
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.ViewState;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewFocusCallback;

public class ExampleWebExample {

	public static void main(String[] args) throws Exception {
		View.setCreatedCallback(new ViewCreatedCallback() {
			
			public boolean onViewCreated(View view) {
				view.setMask(view.getOutput().getMask());
				view.bringToFront();
				view.focus();
				return true;
			}
		});
		
		View.setFocusCallback(new ViewFocusCallback() {
			
			public void onFocusChange(View view, boolean focusState) {
				view.setState(ViewState.ACTIVATED, focusState);
			}
		});
		
		JWLC.init();
		JWLC.run();
	}

}
```

For original `example.c` from libwlc, see `jwlc.Example.java` in `src/test/examples`.

Also, see package `jwlc` in `src/test/examples` for more different examples what you can do with this library.

## Main api classes

`cz.upol.inf.vanusanik.jwlc.JWLC` - main class, init and termination is handled by static methods of it.

`cz.upol.inf.vanusanik.jwlc.Compositor` - compositor callbacks

`cz.upol.inf.vanusanik.jwlc.Event` - FD and timed event handling

`cz.upol.inf.vanusanik.jwlc.Keyboard` - keyboard related methods and callbacks

`cz.upol.inf.vanusanik.jwlc.Mouse` - nouse related methods and callbacks

`cz.upol.inf.vanusanik.jwlc.Resource` - surface resources 

package `cz.upol.inf.vanusanik.jwlc.geometry` - geometry related classes (`Point`, `Size`, `Geometry`)

`cz.upol.inf.vanusanik.jwlc.render.Framebuffer` - framebuffer related methods

`cz.upol.inf.vanusanik.jwlc.wlc.Output` - output related methods and callbacks

`cz.upol.inf.vanusanik.jwlc.View` - view related methods and callbacks

package `cz.upol.inf.vanusanik.jwlc.wlc.callbacks` - all high level callback interfaces

## Documentation

See `doc` for generated javadoc.
