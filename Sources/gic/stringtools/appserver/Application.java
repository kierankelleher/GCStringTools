package gic.stringtools.appserver;

import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	public static void main(String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		ERXApplication.log.info("Welcome to " + name() + " !");
		
        // So that the application.woa URL returns Main without creating a
        // session
        // we use the DA request handle instead of the component request handler
        // by default.
        setDefaultRequestHandler(requestHandlerForKey(directActionRequestHandlerKey()));
        
		/* ** put your initialization code in here ** */
		setAllowsConcurrentRequestHandling(true);
		
	}
}
