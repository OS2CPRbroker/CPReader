CPReader
=====================================
This is a web-application for making searches and looking up information from an instance of CPR Broker.

## Installation
- Clone the git repository from https://github.com/OS2CPRbroker/CPReader
- Checkout the branch of the  latest version. 
    - As of 31/3/2017, this is: *release_2.0_rc0*
- Register a website in IIS that points to the root folder of the cloned repo

Although the website is developed in ASP.NET MVC, it uses standard ASP.NET security that is used in WebForms.

## Access control

### Windows authentication

This mode will use real windows authentication with browser asking for credentials. Access control based on ASP.NET access control model. Does not require a Windows client.

1. Create new site in IIS manager, pointing to *[cpreader_site_folder]\IISSite*
    - If the machine is member of a domain, make sure the site's application pool is running using a domain account
2. Make sure Windows authentication is installed, and that it is the only authentication method that is enabled
3. Make sure that the application pool has *Managed Pipeline Mode = Integrated*
4. Comment out this line: *bind(IAuthentication.class).to(TestAuthenticationStrategy.class);*
    - In *[cpreader_site_folder]\app\Global.java*, uncomment the line activating Windows authentication: 
*bind(IAuthentication.class).to(WindowsAuthenticationStrategy.class);*
5. Follow the guide below, knowing that the web.config file is at: [cpreader_site_folder]\IISSite\Web.config 

#### For test environments

If this happens: https://www.daniweb.com/web-development/aspnet/threads/333289/asp-net-with-activedirectory-authentication. Then this is the reason (due to cloned machine with the same SID as domain controller): https://communities.vmware.com/message/914328.

**Solution**: http://oasysadmin.com/2012/02/27/generate-a-new-sid-on-windows-server-2008-and-windows-7/

### LDAP

This method will ask the user for credentials through a web page. This is unsecure if the site is not accessed via SSL because the credentials will be sent as clear text
1. Adjust the LDAP configuration properties in *[cpreader_site_folder]\conf\application.conf*
2. *In [cpreader_site_folder]\app\Global.java*, uncomment the lines activating LDAP authentication
3. Comment out this line: 
*bind(IAuthentication.class).to(TestAuthenticationStrategy.class);*

## Using integrated Windows auhentication

### Access control

- In the web.config file of the website, under *authorization*, add, allow or deny entries as per the sample given in the file.

- If the site's name is not identical to the machine's name, add the site's host name(including full domain name) to the registry as described here http://support.microsoft.com/kb/896861

*HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Lsa\MSV1_0
BackConnectionHostNames*


### Trust & security zones

In order to avoid the browser prompting the users to enter their credentials, make sure that the website is in Internet Explorer's 'Intranet zone'.

For address auto complete to work in internet explorer, add the site dawa.aws.dk to the list of trusted sites.
