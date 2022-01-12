/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryDelete;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import java.sql.SQLException;
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
/*     */ public class JdbcLdapDelete
/*     */   extends JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   static final String FROM = " from ";
/*     */   static final String WHERE = " where ";
/*     */   static final String DELETE = "delete ";
/*     */   SqlStore store;
/*     */   String SQL;
/*     */   boolean simple;
/*     */   DirectoryDelete del;
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL) throws SQLException {
/*  67 */     this.del = (DirectoryDelete)con.getImplClasses().get("DELETE");
/*     */ 
/*     */     
/*  70 */     this.con = con;
/*     */     
/*  72 */     String tmpSQL = SQL.toLowerCase();
/*  73 */     this.SQL = SQL;
/*     */     
/*  75 */     int begin = tmpSQL.indexOf(" from ") + " from ".length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     if (tmpSQL.indexOf(" where ") == -1) {
/*     */       
/*  83 */       this.from = SQL.substring(begin);
/*  84 */       this.from = this.from.trim();
/*  85 */       this.simple = true;
/*     */     } else {
/*     */       Integer iscope;
/*  88 */       this.from = SQL.substring(begin, tmpSQL.indexOf(" where "));
/*  89 */       this.from = this.from.trim();
/*     */       
/*  91 */       begin = tmpSQL.indexOf("delete ") + "delete ".length() - 1;
/*  92 */       int end = tmpSQL.indexOf(" from ");
/*     */       
/*  94 */       String sscope = SQL.substring(begin, end).trim();
/*     */       
/*  96 */       if (sscope.trim().length() == 0) {
/*  97 */         iscope = (Integer)this.scopes.get(con.getSearchScope());
/*     */       } else {
/*  99 */         iscope = (Integer)this.scopes.get(sscope.trim());
/*     */       } 
/*     */       
/* 102 */       if (iscope == null) {
/* 103 */         throw new SQLException("Unrecognized Search Scope");
/*     */       }
/* 105 */       this.scope = iscope.intValue();
/*     */       
/* 107 */       begin = tmpSQL.indexOf(" where ") + " where ".length();
/* 108 */       this.where = SQL.substring(begin).trim();
/*     */ 
/*     */ 
/*     */       
/* 112 */       this.where = con.nativeSQL(sqlArgsToLdap(this.where));
/*     */       
/* 114 */       this.simple = false;
/*     */     } 
/*     */     
/* 117 */     this.store = new SqlStore(SQL);
/*     */     
/* 119 */     this.store.setFrom(this.from);
/* 120 */     this.store.setSimple(this.simple);
/* 121 */     this.store.setScope(this.scope);
/* 122 */     this.store.setWhere(this.where);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL, SqlStore sqlStore) throws SQLException {
/* 129 */     this.del = (DirectoryDelete)con.getImplClasses().get("DELETE");
/* 130 */     this.con = con;
/* 131 */     this.SQL = SQL;
/* 132 */     this.store = this.store;
/* 133 */     this.from = this.store.getFrom();
/* 134 */     this.simple = this.store.getSimple();
/* 135 */     this.args = new Object[this.store.getArgs()];
/* 136 */     this.where = this.store.getWhere();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object executeQuery() throws SQLException {
/* 141 */     return null;
/*     */   }
/*     */   public Object executeUpdate() throws SQLException {
/* 144 */     return new Integer(this.del.doDeleteJldap(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int pos, String value) throws SQLException {
/* 153 */     this.args[pos] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getSqlStore() {
/* 161 */     return this.store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetrieveDN() {
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiLdapConnection getCon() {
/* 175 */     return this.con;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWhere() {
/* 182 */     return this.where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUpdate() {
/* 190 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapDelete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */