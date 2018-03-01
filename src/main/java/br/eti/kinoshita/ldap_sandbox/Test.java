package br.eti.kinoshita.ldap_sandbox;

import javax.mail.internet.InternetAddress;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;

public class Test {

    public static void main(String[] args) throws Exception {
        String realEmail = "zoidberg@planetexpress.com";
        //String fakeEmail = asciify("))(notmyemail@anotherexample.com,");
        String fakeEmail = ")(mail<=zzzzzzzzzzzzzzzz@zplanetexpress.com";
        String email = String.format("%s%s", realEmail, fakeEmail);
        String filter = "(&(mail=" + email + ")(objectClass=*))";
        System.out.println(filter);
        final String host = "localhost";
        final int port    = 1389;
        final String base = "ou=people,dc=planetexpress,dc=com"; // "dc=planetexpress,dc=com";
        final String user = "cn=admin,dc=planetexpress,dc=com";
        final String pass = "GoodNewsEveryone";
        LDAPConnection connection = new LDAPConnection(host, port, user, pass);

        SearchResult entries = connection.search(base, SearchScope.SUB, filter);
        if (entries.getSearchEntries().size() == 0) {
            //throw new RuntimeException("No LDAP search results!");
            System.out.println("No LDAP search results!");
        }
        for (SearchResultEntry entry : entries.getSearchEntries()) {
            LDAPEntry ldapEntry = new LDAPEntry(entry);
            String cn = ldapEntry.getAttribute("cn").toString();
            System.out.println(cn);
        }

        connection.close();
        InternetAddress.parse(email);
        System.out.println("OK!");
    }

    private static String asciify(String origin) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < origin.length(); i++) {
            sb.append("%" + (int) origin.charAt(i));
        }
        return sb.toString();
    }
}
