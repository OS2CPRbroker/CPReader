package util.auth.ldap;

import java.security.GeneralSecurityException;

import javax.inject.Inject;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import util.auth.AuthResponseType;
import util.auth.IAuthResponse;
import util.auth.IAuthStrategy;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import com.unboundid.util.ssl.TrustStoreTrustManager;

public class LdapAuthenticationStrategy implements IAuthStrategy {

	private final String hostname;
	private final int port;
	private final String basedn;
	private final String groupdn;
	private final String groupfield;

	@Inject
	public LdapAuthenticationStrategy(final String newHostname,
			final int newPort, final String newBasedn, final String newGroupdn,
			final String newGroupfield) {

		hostname = newHostname;
		port = newPort;
		basedn = newBasedn;
		groupdn = newGroupdn;
		groupfield = newGroupfield;
	}

	private final LDAPConnection getConnection() {
		StopWatch stopWatch = new Slf4JStopWatch("LdapAuthenticationStrategy.getConnection");
		LDAPConnection ldapConnection = null;
		try {
			String trustStoreFile  = "/etc/java/security/jssecacerts";
			SSLUtil sslUtil = new SSLUtil(new TrustStoreTrustManager(trustStoreFile));
			//TODO Figure out this SSL connection
			//SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
		    ldapConnection = new LDAPConnection(sslUtil.createSSLSocketFactory(), hostname, port);
			//ldapConnection = new LDAPConnection(hostname, port);
		} catch (final LDAPException | GeneralSecurityException lex) {
			play.Logger.error("failed to connect to " + hostname + " "
					+ lex.getMessage());
			stopWatch.stop("LdapAuthenticationStrategy.getConnection.failed", lex.getMessage());
		} 
		
		stopWatch.stop();
		return ldapConnection;
	}

	// TODO make some sort of better report than a boolean report type
	@Override
	public final IAuthResponse authentication(final String samAccountName,
			final String password) {
		StopWatch stopWatch = new Slf4JStopWatch("LdapAuthenticationStrategy.authentication");

		// Try to get a connection
		LDAPConnection ldapConnection = getConnection();

		if (ldapConnection == null) {
			stopWatch.stop("LdapAuthenticationStrategy.authentication.noConnection");
			return new LdapAuthResponse(AuthResponseType.ERROR,
					"Connection error");
		}

		try {
			final BindRequest bindRequest = new SimpleBindRequest(samAccountName,password);
			BindResult bindResult = ldapConnection.bind(bindRequest);

			if (bindResult.getResultCode() == ResultCode.SUCCESS) {

				//TODO Change this filter to something better
				Filter filter = Filter.create("(&(objectCategory=User)(samAccountName="+ samAccountName +"))");

				SearchRequest searchRequest3 = new SearchRequest(
						basedn, SearchScope.SUB, filter);
				String distinguishedName = "";
				
				try {
					SearchResult searchResult = ldapConnection
							.search(searchRequest3);

					for (SearchResultEntry entry : searchResult
							.getSearchEntries()) {
						
						distinguishedName = entry.getAttribute("distinguishedName").getValue();
					}
				} catch (LDAPSearchException lse) {
					play.Logger.warn("The search failed.");
					play.Logger.warn(lse.getMessage());
					stopWatch.stop("LdapAuthenticationStrategy.authentication.searchFailed", lse.getMessage());
				} 

				// search and check if the user is member of the predefined group
				SearchRequest searchRequest = new SearchRequest(groupdn,
						SearchScope.SUB, Filter.createEqualityFilter(
								groupfield, distinguishedName));

				SearchResult sr = ldapConnection.search(searchRequest);

				if (sr.getEntryCount() == 1) {
					stopWatch.stop("LdapAuthenticationStrategy.authentication.succesful");
					// found one entry, so must be okay
					return new LdapAuthResponse(AuthResponseType.SUCCESS,
							"login.succesful");
				} else if (sr.getEntryCount() == 0) {
					stopWatch.stop("LdapAuthenticationStrategy.authentication.unsuccesful");
					// didn't return any, so user isn't authorized
					return new LdapAuthResponse(AuthResponseType.INFO,
							"login.not_authorized");
				} else {
					stopWatch.stop("LdapAuthenticationStrategy.authentication.unexpectedSearchResult");
					// something went wrong!
					play.Logger
							.error(this.getClass().getName()
									+ ": return unexpected multiply result - should be 0 or 1");
					return new LdapAuthResponse(AuthResponseType.ERROR,
							"login.unexpected_error");
				}
			} else {
				stopWatch.stop("LdapAuthenticationStrategy.authentication.unsuccesful");
				// ResultCode != success
				return new LdapAuthResponse(AuthResponseType.INFO,
						"login.invalid_credientials");
			}
		} catch (final LDAPException lex) {

			stopWatch.stop("LdapAuthenticationStrategy.authentication.unsuccesful", lex.getMessage());
			play.Logger.warn(lex.toString());

			return new LdapAuthResponse(AuthResponseType.INFO,
					"login.invalid_credientials");

		} finally {
			// always close the connection afterwards
			ldapConnection.close();

		}
	}
}
