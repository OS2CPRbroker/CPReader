package util.auth.unboundid.implementations;

import java.util.List;

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
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;

import conf.IConfiguration;

public class ProxyUserUnboundidAuthentication implements IAuthentication {

	private IUnboundidConnection connection;

	private final String baseDn;
	private final String authorizedGroupRdn;
	private final String authorizedAttribute;
	private final String lowerCaseAuthorizedGroupDn;
	private final String proxyUserName;
	private final String proxyPassword;
	private final String uniqueUserAttribute;
	
	@Inject
	public ProxyUserUnboundidAuthentication(
			final IConfiguration configuration,
			final IUnboundidConnection connection) {

		this.connection = connection;

		Configuration config = configuration.getConfiguration();

		baseDn = config.getString("ldap.basedn");
		authorizedGroupRdn = config.getString("ldap.authorizedgrouprdn");
		authorizedAttribute = config.getString("ldap.authorizedattribute");
		proxyUserName = config.getString("ldap.proxyusername");
		proxyPassword = config.getString("ldap.proxypassword");
		uniqueUserAttribute = config.getString("ldap.uniqueuserattribute");
		lowerCaseAuthorizedGroupDn = (authorizedGroupRdn + "," + baseDn).toLowerCase();
		play.Logger.debug("UnboundidAuthentication.constructor, basedn: " + baseDn);
		play.Logger.debug("UnboundidAuthentication.constructor, authorizedgrouprdn: " + authorizedGroupRdn);
		play.Logger.debug("UnboundidAuthentication.constructor, authorizedattribute: " + authorizedAttribute);
	}

	@Override
	public IAuthenticationResponse authentication(final String username, final String password) {

		StopWatch stopWatch = new Slf4JStopWatch("UnboundidAuthentication.authentication");

		// Try to get a connection
		LDAPConnection ldapConnection = (LDAPConnection) connection.getConnection();
		if (ldapConnection == null) {
			stopWatch.stop("UnboundidAuthentication.authentication.noConnection");
			return new AuthenticationResponse(AuthResponseType.ERROR, "Connection error");
		}

		try {
			// proxy user bindrequest
            // TODO: Why is this needed at all ??!!
			final BindRequest proxyBindRequest = new SimpleBindRequest(proxyUserName, proxyPassword);
			BindResult proxyBindResult = ldapConnection.bind(proxyBindRequest);
			
			// was the proxyuser bind succesfully?
			if (proxyBindResult.getResultCode() == ResultCode.SUCCESS) {
				play.Logger.debug("Proxyuser bind succesful");

				// search for the SAMAccountName of the username of the person trying to login
				SearchRequest proxySearchRequest = new SearchRequest(baseDn, SearchScope.SUB,
						Filter.createEqualityFilter(uniqueUserAttribute, username));
				SearchResult proxySearchResult = ldapConnection.search(proxySearchRequest);

				// was the search successful?
				if (proxySearchResult.getEntryCount() == 1) {
					play.Logger.debug("Found entry: " + proxySearchResult.getSearchEntries().get(0).getDN());
					
					final String binddn = proxySearchResult.getSearchEntries().get(0).getDN();
					final BindRequest bindRequest = new SimpleBindRequest(binddn, password);

					// trying to bind with the non-proxy user
					BindResult bindResult = ldapConnection.bind(bindRequest);

					if (bindResult.getResultCode() == ResultCode.SUCCESS) {
						play.Logger.debug("ResultCode.SUCCESS");

						// search recursively if the user is part of the authorizedgroup starting
						// the dn entry found, when looking for the user.
						boolean isMember = recursiveSearchMemberOf(proxySearchResult, ldapConnection);
						play.Logger.debug("recursiveSearchMemberOf returned:: " +isMember);
												
						if (isMember) {
                            stopWatch.stop("UnboundidAuthentication.authentication.succesful");
							return new AuthenticationResponse(AuthResponseType.SUCCESS, "login.succesful");
						}
                        else {
                            stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful");
							// didn't return any, so user isn't authorized
							return new AuthenticationResponse(AuthResponseType.INFO, "login.not_authorized");
						}
					}
                    else {
						stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful");
						// ResultCode != success
						return new AuthenticationResponse(AuthResponseType.INFO, "login.invalid_credientials");
					}
				} else {
					return new AuthenticationResponse(AuthResponseType.INFO, "login.invalid_credientials");
				}
			}
            else{
                // TODO: What shall we do if we cannot do a binding with a proxy user??
            }
		}
        catch (final LDAPException lex) {
			stopWatch.stop("UnboundidAuthentication.authentication.unsuccesful", lex.getMessage());
			play.Logger.warn(lex.toString());
			return new AuthenticationResponse(AuthResponseType.INFO, "login.unexpected_error");
		}
        finally {
			// always close the connection afterwards
			ldapConnection.close();
		}
		return null; // This should never happen
	}

	/**
	 * Helper method to recursively search starting from the current user to
	 * check he/she is member of the authorizedgroup or is member of a group
	 * that is member of authorizedgroup or is member of a group that is a
	 * group that is a member.. etc. It will keep going until all possibilities
	 * has been checked or it is succesful - which ever comes first.
	 * @param searchResult
	 * @param connection LDAP connection that has already made a bind
	 * @return true if the user has access to the application
	 * @throws LDAPSearchException
	 */
	private boolean recursiveSearchMemberOf(SearchResult searchResult, LDAPConnection connection)
			throws LDAPSearchException {
		List<SearchResultEntry> entries = searchResult.getSearchEntries();
		
		play.Logger.debug("recursiveSearchMemberOf called, entries::" + entries.size());
		for(SearchResultEntry entry : entries) {
			// is the entry memberOf the correct group
			if(entry.getDN().toLowerCase().equals(lowerCaseAuthorizedGroupDn)) {
				play.Logger.debug("SUCCESS! Entry had dn::" + entry.getDN().toLowerCase());
				return true;
			} else if(entry.hasAttribute("memberOf")) {
				play.Logger.debug("recursiveSearchMemberOf found memberOf attribute");
				String[] memberOfAttributes = entry.getAttributeValues("memberOf");
				
				for(String memberOfAttribute : memberOfAttributes) {
					play.Logger.debug("memberOfAttribute::" + memberOfAttribute.toLowerCase());

					// check if the user is direct member of 
					if(memberOfAttribute.toLowerCase().equals(lowerCaseAuthorizedGroupDn)) {
						play.Logger.debug("SUCCESS! MemberOfAttribute " + memberOfAttribute.toLowerCase() + " equal to ::" + lowerCaseAuthorizedGroupDn);
						return true;
					}
					play.Logger.debug("MemberOfAttribute " + memberOfAttribute.toLowerCase() + " NOT equal to ::" + lowerCaseAuthorizedGroupDn);
					
					// Search for the memberOfAttribute
					SearchRequest innerSearchRequest = new SearchRequest(baseDn, SearchScope.SUB,
							Filter.createEqualityFilter("member", memberOfAttribute));
					SearchResult innerSearchResult = connection.search(innerSearchRequest);
					play.Logger.debug("Calling recursiveSearchMemberOf on member="+memberOfAttribute.toLowerCase().toString());
					boolean isMemberOf = recursiveSearchMemberOf(innerSearchResult, connection);
					
					// Only return if true otherwise
					if(isMemberOf) return isMemberOf;
				} //END FOR LOOP
			}//END ELSE IF
			play.Logger.debug(" FAILURE! Entry had dn::" + entry.getDN().toLowerCase() + ", NOT equal to " + lowerCaseAuthorizedGroupDn);
		}//END FOR LOOP
		return false;
	}//END recursiveSearchMemberOf method
}
