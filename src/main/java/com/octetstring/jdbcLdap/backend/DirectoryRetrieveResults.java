package com.octetstring.jdbcLdap.backend;

import com.novell.ldap.LDAPSearchResults;
import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSelect;
import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
import java.sql.SQLException;

public interface DirectoryRetrieveResults {
  Object searchJldap(JdbcLdapSelect paramJdbcLdapSelect) throws SQLException;
  
  LDAPSearchResults searchUpInsJldap(JdbcLdapSqlAbs paramJdbcLdapSqlAbs) throws SQLException;
}


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/backend/DirectoryRetrieveResults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */