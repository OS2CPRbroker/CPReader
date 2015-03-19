package util.auth.unboundid.implementations;

import javax.inject.Inject;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import play.Configuration;
import util.auth.AuthResponseType;
import util.auth.AuthenticationResponse;
import util.auth.IAuthentication;
import util.auth.IAuthenticationResponse;
import util.auth.unboundid.IUnboundidConnection;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;

import conf.IConfiguration;

// This class is not used anywhere, and is simply the same as ProxyUserUnboundidAuthentication
public class UnboundidAuthentication implements IAuthentication {
	
	private IUnboundidConnection connection;
	
	private final String basedn;
	private final String userattribute;
	private final String usergrouprdn;
	private final String authorizedgrouprdn;
	private final String authorizedattribute;

	
	@Inject
	public UnboundidAuthentication(final IConfiguration configuration,
			final IUnboundidConnection connection) {

		this.connection = connection;
			
		Configuration config = configuration.getConfiguration();
		
		basedn  = config.getString("ldap.basedn");
		usergrouprdn = config.getString("ldap.usergrouprdn");
		userattribute = config.getString("ldap.userattribute");
		authorizedgrouprdn = config.getString("ldap.authorizedgrouprdn");
		authorizedattribute = config.getString("ldap.authorizedattribute");
		
		play.Logger.debug("UnboundidAuthentication.constructor, basedn: " + basedn);
		play.Logger.debug("UnboundidAuthentication.constructor, usergrouprdn: " + usergrouprdn);
		play.Logger.debug("UnboundidAuthentication.constructor, userattribute: " + userattribute);
		play.Logger.debug("UnboundidAuthentication.constructor, authorizedgrouprdn: " + authorizedgrouprdn);
		play.Logger.debug("UnboundidAuthentication.constructor, authorizedattribute: " + authorizedattribute);

	}

	@Override
	public IAuthenticationResponse authentication(final String username,
			final String password) {
		StopWatch stopWatch = new Slf4JStopWatch("UnboundidAuthentication.authentication");

		// Try to get a connection
		LDAPConnection ldapConnection = (LDAPConnection) connection.getConnection();

		if (ldapConnection == null) {
			stopWatch.stop("UnboundidAuthentication.authentication.noConnection");
			return new AuthenticationResponse(AuthResponseType.ERROR,
					"Connection error");
		}

		try {
			final String binddn = userattribute + "=" +
								  username + "," +
								  usergrouprdn + "," +
								  basedn;
			play.Logger.debug("Created binddn: " + binddn);
					
			final BindRequest bindRequest = new SimpleBindRequest(binddn ,password);
			
			BindResult bindResult = ldapConnection.bind(bindRequest);

			if (bindResult.getResultCode() == ResultCode.SUCCESS) {
				play.Logger.debug("ResultCode.SUCCESS");

				// search and check if the user is member of the predefined group
				SearchRequest searchRequest = new SearchRequest(authorizedgrouprdn + ", " + basedn,
						SearchScope.SUB, Filter.createEqualityFilter(
								authorizedattribute, binddn));
				
				SearchResult sr = ldapConnection.search(searchRequest);
				
				if (sr.getEntryCount() == 1) {
					play.Logger.debug("Got 1 result");
					stopWatch.stop("UnboundidAuthentication.authentication.succesful");
					// found one entry, so must be okay
					return new AuthenticationResponse(AuthResponseType.SUCCESS,
							"login.succesful");
				} else if (sr.getEntryCount() == 0) {
					play.Logger.debug("Got 0 results");
					stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful");
					// didn't return any, so user isn't authorized
					return new AuthenticationResponse(AuthResponseType.INFO,
							"login.not_authorized");
				} else {
					stopWatch.stop("UnboundidAuthentication.authentication.unexpectedSearchResult");
					// something went wrong!
					play.Logger
							.error(this.getClass().getName()
									+ ": return unexpected multiply result - should be 0 or 1");
					return new AuthenticationResponse(AuthResponseType.ERROR,
							"login.unexpected_error");
				}
			} else {
				stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful");
				// ResultCode != success
				return new AuthenticationResponse(AuthResponseType.INFO,
						"login.invalid_credientials");
			}
		} catch (final LDAPException lex) {

			stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful", lex.getMessage());
			play.Logger.warn(lex.toString());

			return new AuthenticationResponse(AuthResponseType.INFO,
					"login.invalid_credientials");

		} finally {
			// always close the connection afterwards
			ldapConnection.close();

		}
	}

}
