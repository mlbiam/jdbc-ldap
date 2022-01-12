/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import com.novell.ldap.LDAPMessageQueue;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.jndi.UnpackResults;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapDelete;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapInsert;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSelect;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSql;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapUpdate;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapUpdateEntry;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdbcLdapStatement
/*     */   implements Statement
/*     */ {
/*     */   static final String SELECT = "select";
/*     */   static final int SELECT_LEN = 6;
/*     */   static final String INSERT = "insert";
/*     */   static final int INSERT_LEN = 6;
/*     */   static final String DELETE = "delete";
/*     */   static final int DELETE_LEN = 6;
/*     */   static final String UPDATE = "update";
/*     */   static final int UPDATE_LEN = 6;
/*     */   static final String UPDATE_ENTRY = "update entry";
/*     */   static final int UPDATE_ENTRY_LEN = 12;
/*     */   LinkedList statements;
/*     */   UnpackResults res;
/*     */   LdapResultSet rs;
/*     */   JdbcLdapSql stmt;
/*     */   JndiLdapConnection con;
/*     */   int maxResults;
/*     */   int timeOut;
/*     */   Object[] results;
/*     */   
/*     */   void loadSQL(String sql) throws SQLException {
/*  99 */     String sqll = sql.toLowerCase().trim();
/* 100 */     SqlStore sqlStore = this.con.getCache(sql);
/*     */     
/* 102 */     if (sqll.substring(0, 6).equals("select")) {
/* 103 */       this.stmt = (JdbcLdapSql)new JdbcLdapSelect();
/* 104 */     } else if (sqll.substring(0, 6).equals("insert")) {
/* 105 */       this.stmt = (JdbcLdapSql)new JdbcLdapInsert();
/* 106 */     } else if (sqll.substring(0, 12).equals("update entry")) {
/* 107 */       this.stmt = (JdbcLdapSql)new JdbcLdapUpdateEntry();
/*     */     }
/* 109 */     else if (sqll.substring(0, 6).equals("delete")) {
/* 110 */       this.stmt = (JdbcLdapSql)new JdbcLdapDelete();
/* 111 */     } else if (sqll.substring(0, 6).equals("update")) {
/* 112 */       this.stmt = (JdbcLdapSql)new JdbcLdapUpdate();
/*     */     } else {
/* 114 */       throw new SQLException("Opperation not suported");
/*     */     } 
/*     */     
/* 117 */     if (sqlStore == null) {
/* 118 */       this.stmt.init(this.con, sql);
/*     */       
/* 120 */       if (this.con.cacheStatements())
/* 121 */         this.con.cacheStatement(sql, this.stmt.getSqlStore()); 
/*     */     } else {
/* 123 */       this.stmt.init(this.con, sql, sqlStore);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbcLdapStatement(JndiLdapConnection con) {
/* 132 */     this.con = con;
/* 133 */     this.maxResults = -1;
/* 134 */     this.timeOut = -1;
/* 135 */     this.res = new UnpackResults(con);
/* 136 */     this.statements = new LinkedList();
/*     */   }
/*     */   
/*     */   public void addBatch(String str) throws SQLException {
/* 140 */     loadSQL(str);
/* 141 */     if (!this.stmt.isUpdate())
/* 142 */       throw new SQLException(str + " is not an update"); 
/* 143 */     this.statements.add(this.stmt);
/*     */   }
/*     */   
/*     */   public void cancel() throws SQLException {
/* 147 */     throw new SQLException("not implemented");
/*     */   }
/*     */   
/*     */   public void clearBatch() throws SQLException {
/* 151 */     this.statements.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {}
/*     */   
/*     */   public boolean execute(String str) throws SQLException {
/* 161 */     executeQuery(str);
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String str, int param) throws SQLException {
/* 167 */     executeQuery(str);
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String str, int[] values) throws SQLException {
/* 173 */     executeQuery(str);
/* 174 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String str, String[] str1) throws SQLException {
/* 179 */     executeQuery(str);
/* 180 */     return true;
/*     */   }
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/* 184 */     int[] batch = new int[this.statements.size()];
/*     */     
/* 186 */     Iterator<JdbcLdapSql> it = this.statements.iterator();
/* 187 */     int i = 0;
/* 188 */     while (it.hasNext()) {
/* 189 */       this.stmt = it.next();
/* 190 */       batch[i] = ((Integer)this.stmt.executeUpdate()).intValue();
/* 191 */       i++;
/*     */     } 
/*     */     
/* 194 */     return batch;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String str) throws SQLException {
/* 199 */     loadSQL(str);
/* 200 */     if (this.con.isDSML() || this.con.isSPML() || this.con.isNoCon()) {
/* 201 */       this.res.unpackJldap((LDAPSearchResults)this.stmt.executeQuery(), this.stmt.getRetrieveDN(), this.stmt.getSqlStore().getFrom(), this.con.getBaseDN(), this.stmt.getSqlStore().getRevFieldMap());
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 207 */       this.res.unpackJldap((LDAPMessageQueue)this.stmt.executeQuery(), this.stmt.getRetrieveDN(), this.stmt.getSqlStore().getFrom(), this.con.getBaseDN(), this.stmt.getSqlStore().getRevFieldMap());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     this.rs = new LdapResultSet(this.con, this, this.res, ((JdbcLdapSelect)this.stmt).getBaseContext());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     return this.rs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String str) throws SQLException {
/* 228 */     loadSQL(str);
/*     */     
/* 230 */     return ((Integer)this.stmt.executeUpdate()).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String str, int param) throws SQLException {
/* 236 */     return executeUpdate(str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String str, int[] values) throws SQLException {
/* 242 */     return executeUpdate(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String str, String[] str1) throws SQLException {
/* 247 */     return executeUpdate(str);
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 251 */     return (Connection)this.con;
/*     */   }
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 255 */     return -1;
/*     */   }
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 259 */     return -1;
/*     */   }
/*     */   
/*     */   public ResultSet getGeneratedKeys() throws SQLException {
/* 263 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public int getMaxFieldSize() throws SQLException {
/* 267 */     return -1;
/*     */   }
/*     */   
/*     */   public int getMaxRows() throws SQLException {
/* 271 */     return this.maxResults;
/*     */   }
/*     */   
/*     */   public boolean getMoreResults() throws SQLException {
/* 275 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getMoreResults(int param) throws SQLException {
/* 279 */     return true;
/*     */   }
/*     */   
/*     */   public int getQueryTimeout() throws SQLException {
/* 283 */     return this.timeOut;
/*     */   }
/*     */   
/*     */   public ResultSet getResultSet() throws SQLException {
/* 287 */     return this.rs;
/*     */   }
/*     */   
/*     */   public int getResultSetConcurrency() throws SQLException {
/* 291 */     return -1;
/*     */   }
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 295 */     return -1;
/*     */   }
/*     */   
/*     */   public int getResultSetType() throws SQLException {
/* 299 */     return -1;
/*     */   }
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/* 303 */     return -1;
/*     */   }
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorName(String str) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setEscapeProcessing(boolean param) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int param) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setFetchSize(int param) throws SQLException {
/* 322 */     this.maxResults = param;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxFieldSize(int param) throws SQLException {}
/*     */   
/*     */   public void setMaxRows(int param) throws SQLException {
/* 329 */     this.maxResults = param;
/*     */   }
/*     */   
/*     */   public void setQueryTimeout(int param) throws SQLException {
/* 333 */     this.timeOut = param;
/*     */   }
/*     */
@Override
public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public boolean isClosed() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void setPoolable(boolean poolable) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public boolean isPoolable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void closeOnCompletion() throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public boolean isCloseOnCompletion() throws SQLException {
    // TODO Auto-generated method stub
    return false;
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/JdbcLdapStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */