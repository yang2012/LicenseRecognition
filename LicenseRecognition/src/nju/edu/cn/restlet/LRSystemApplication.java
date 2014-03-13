package nju.edu.cn.restlet;

import nju.edu.cn.LicenseRecognitionResource;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class LRSystemApplication extends Application {

	/**
	 * Creates a root Router to dispatch call to server resources.
	 */
	@Override
	public Restlet createInboundRoot() {
		Router defaultRouter = new Router(getContext());
		defaultRouter.attach("/", LicenseRecognitionResource.class);

		return defaultRouter;
	}

}