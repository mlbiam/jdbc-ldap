package com.octetstring.jdbcLdap.backend;

import com.octetstring.jdbcLdap.sql.statements.JdbcLdapUpdate;
import java.sql.SQLException;

public interface DirectoryUpdate {
  int doUpdateJldap(JdbcLdapUpdate paramJdbcLdapUpdate) throws SQLException;
}


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/backend/DirectoryUpdate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */