/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import com.novell.ldap.LDAPConnection;
/*     */ import com.novell.ldap.LDAPControl;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.novell.ldap.LDAPSearchConstraints;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.novell.ldap.controls.LDAPSortControl;
/*     */ import com.novell.ldap.controls.LDAPSortKey;
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSelect;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
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
/*     */ public class RetrieveResults
/*     */   implements DirectoryRetrieveResults
/*     */ {
/*     */   public Object searchJldap(JdbcLdapSelect select) throws SQLException {
/*     */     try {
/*     */       String[] searchAttribs;
/*  47 */       LDAPConnection con = select.getConnection();
/*  48 */       String[] fields = select.getSearchAttributes();
/*  49 */       fields = (fields != null) ? fields : new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  54 */       if (fields.length == 1 && fields[0].equalsIgnoreCase("dn")) {
/*  55 */         searchAttribs = new String[] { "1.1" };
/*     */       }
/*     */       else {
/*     */         
/*  59 */         searchAttribs = fields;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  69 */       String useBase = JndiLdapConnection.getRealBase((JdbcLdapSqlAbs)select);
/*     */       
/*  71 */       String filter = select.getFilterWithParams();
/*     */ 
/*     */       
/*  74 */       LDAPSearchConstraints constraints = null;
/*     */       
/*  76 */       if (select.getJDBCConnection().getMaxSizeLimit() >= 0) {
/*  77 */         constraints = con.getSearchConstraints();
/*  78 */         constraints.setMaxResults(select.getJDBCConnection().getMaxSizeLimit());
/*     */       } 
/*     */       
/*  81 */       if (select.getJDBCConnection().getMaxTimeLimit() >= 0) {
/*  82 */         if (constraints == null) {
/*  83 */           constraints = con.getSearchConstraints();
/*     */         }
/*     */         
/*  86 */         constraints.setTimeLimit(select.getJDBCConnection().getMaxTimeLimit());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  91 */       LDAPSortKey[] keys = null;
/*     */       
/*  93 */       if (select.getSqlStore().getOrderby() != null) {
/*  94 */         keys = new LDAPSortKey[(select.getSqlStore().getOrderby()).length];
/*  95 */         for (int i = 0, m = keys.length; i < m; i++) {
/*  96 */           keys[i] = new LDAPSortKey(getFieldName(select.getSqlStore().getOrderby()[i], select.getSqlStore().getFieldMap()));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 102 */       if (select.getJDBCConnection().isDSML() || select.getJDBCConnection().isSPML()) {
/* 103 */         return con.search(useBase, select.getSearchScope(), filter, searchAttribs, false, constraints);
/*     */       }
/*     */       
/* 106 */       if (keys != null) {
/* 107 */         constraints.setControls(new LDAPControl[] { (LDAPControl)new LDAPSortControl(keys, true) });
/*     */       }
/*     */       
/* 110 */       return con.search(useBase, select.getSearchScope(), filter, searchAttribs, false, null, constraints);
/*     */     }
/* 112 */     catch (LDAPException e) {
/* 113 */       throw new SQLNamingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LDAPSearchResults searchUpInsJldap(JdbcLdapSqlAbs sql) throws SQLException {
/*     */     try {
/* 126 */       LDAPConnection con = sql.getConnection();
/*     */ 
/*     */       
/* 129 */       String useBase = JndiLdapConnection.getRealBase(sql);
/*     */       
/* 131 */       String filter = sql.getFilterWithParams();
/*     */       
/* 133 */       LDAPSearchConstraints constraints = null;
/*     */       
/* 135 */       if (sql.getJDBCConnection().getMaxSizeLimit() >= 0) {
/* 136 */         constraints = con.getSearchConstraints();
/* 137 */         constraints.setMaxResults(sql.getJDBCConnection().getMaxSizeLimit());
/*     */       } 
/*     */       
/* 140 */       if (sql.getJDBCConnection().getMaxTimeLimit() >= 0) {
/* 141 */         if (constraints == null) {
/* 142 */           constraints = con.getSearchConstraints();
/*     */         }
/*     */         
/* 145 */         constraints.setTimeLimit(sql.getJDBCConnection().getMaxTimeLimit());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       return con.search(useBase, sql.getSearchScope(), filter, new String[] { "1.1" }, false, constraints);
/*     */     }
/* 153 */     catch (LDAPException e) {
/* 154 */       throw new SQLNamingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String getFieldName(String name, HashMap revMap) {
/* 160 */     if (revMap != null) {
/* 161 */       String nname = (String)revMap.get(name);
/* 162 */       if (nname != null) {
/* 163 */         return nname;
/*     */       }
/*     */     } 
/*     */     
/* 167 */     return name;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/RetrieveResults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */