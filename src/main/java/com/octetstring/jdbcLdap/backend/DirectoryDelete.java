package com.octetstring.jdbcLdap.backend;

import com.octetstring.jdbcLdap.sql.statements.JdbcLdapDelete;
import java.sql.SQLException;

public interface DirectoryDelete {
  int doDeleteJldap(JdbcLdapDelete paramJdbcLdapDelete) throws SQLException;
}


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/backend/DirectoryDelete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */