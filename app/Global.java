/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * Beemen Beshara
 * Søren Kirkegård
 *
 * The code is currently governed by OS2 - Offentligt digitaliserings-
 * fællesskab / http://www.os2web.dk .
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

import javax.inject.Singleton;

import controllers.Search;
import play.Application;
import play.GlobalSettings;
import play.Play;
import util.Converters;
import util.addresses.DawaAddressParser;
import util.addresses.IAddressParser;
import util.addresses.RegexAddressParser;
import util.auth.IAuthentication;
import util.auth.Secured;
import util.auth.TestAuthenticationStrategy;
import util.auth.WindowsAuthenticationStrategy;
import util.auth.unboundid.implementations.ProxyUserUnboundidAuthentication;
import util.cprbroker.ICprBrokerAccessor;
import util.cprbroker.jaxws.JaxWsCprBroker;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

import conf.IConfiguration;
import conf.PlayConfiguration;
import dk.magenta.cprbrokersoapfactory.CPRBrokerSOAPFactory;

/**
 * Global is the place to hook into the application lifecycle, which makes it
 * the place to manage dependency injection. Here Google Guice's injector is
 * placed in the constructor. It is kept 'in the club', due to the nature of
 * the getControllerInstance method, which is called for all managed routes.
 * 
 * Routes can be tagged as managed by placing a @ infront of the controller,
 * which they should manage. A such method can not be static, so remember to
 * make it non-static.
 *
 * @author Beemen Beshara
 * @author Søren Kirkegård
 *
 */
public class Global extends GlobalSettings {
	public final Injector injector;

	public Global() {
		super();

		// Initialization of dependency injection
		injector = Guice.createInjector(new AbstractModule() {
	        @Override
	        protected void configure() {      	
	
	        	// Bind all configurations to a singleton of the play configuration
	        	bind(IConfiguration.class).to(PlayConfiguration.class).in(Singleton.class);
	        	
	        	// Use these for LDAP authentication
	        	//bind(IAuthentication.class).to(ProxyUserUnboundidAuthentication.class);
		        //bind(IUnboundidConnection.class).to(UnboundidConnection.class);

				// Use this for test/test authentication
	        	//bind(IAuthentication.class).to(TestAuthenticationStrategy.class);

				// Use this for Windows authentication
				bind(IAuthentication.class).to(WindowsAuthenticationStrategy.class);

                //bind(IAddressParser.class).to(DawaAddressParser.class);
				bind(IAddressParser.class).to(RegexAddressParser.class);

	            bind(ICprBrokerAccessor.class)
            	.toProvider(new Provider<ICprBrokerAccessor>() {
                    @Override
                    public ICprBrokerAccessor get() {
                        play.Configuration config = Play.application().configuration();

                        return new JaxWsCprBroker(config, new CPRBrokerSOAPFactory());
                    }
                });
	        }
        });

	}
	
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		// validate needed components
		// UnboundidLdapAuthentication.validate();
		JaxWsCprBroker.validate(app.configuration());

		// Manual injection for Secured.Class.authenticationStrategy
        Secured.authenticationStrategy = injector.getInstance(IAuthentication.class);
        Converters.AddressParser = injector.getInstance(IAddressParser.class);
		Search.config = injector.getInstance(IConfiguration.class);
	}
	
	@Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }
}
