package com.octetstring.jdbcLdap.sql.statements;

import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
import com.octetstring.jdbcLdap.sql.SqlStore;
import java.sql.SQLException;

public interface JdbcLdapSql {
  void init(JndiLdapConnection paramJndiLdapConnection, String paramString) throws SQLException;
  
  void init(JndiLdapConnection paramJndiLdapConnection, String paramString, SqlStore paramSqlStore) throws SQLException;
  
  Object executeQuery() throws SQLException;
  
  Object executeUpdate() throws SQLException;
  
  void setValue(int paramInt, String paramString) throws SQLException;
  
  SqlStore getSqlStore();
  
  boolean getRetrieveDN();
  
  String getFilterWithParams();
  
  boolean isUpdate();
}


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapSql.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */