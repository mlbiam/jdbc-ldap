/*    */ package com.octetstring.jdbcLdap.jndi;
/*    */ 
/*    */ import com.novell.ldap.LDAPConnection;
/*    */ import com.novell.ldap.LDAPEntry;
/*    */ import com.novell.ldap.LDAPException;
/*    */ import com.novell.ldap.LDAPSearchResults;
/*    */ import com.octetstring.jdbcLdap.backend.DirectoryDelete;
/*    */ import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
/*    */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*    */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapDelete;
/*    */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Delete
/*    */   implements DirectoryDelete
/*    */ {
/*    */   public int doDeleteJldap(JdbcLdapDelete del) throws SQLException {
/* 40 */     DirectoryRetrieveResults res = (DirectoryRetrieveResults)del.getCon().getImplClasses().get("RETRIEVE_RESULTS");
/* 41 */     LDAPConnection con = del.getConnection();
/*    */     
/* 43 */     StringBuffer buf = new StringBuffer();
/* 44 */     SqlStore store = del.getSqlStore();
/* 45 */     int count = 0;
/*    */     
/* 47 */     if (store.getSimple()) {
/*    */       try {
/* 49 */         con.delete(JndiLdapConnection.getRealBase((JdbcLdapSqlAbs)del));
/*    */       }
/* 51 */       catch (LDAPException ne) {
/* 52 */         throw new SQLNamingException(ne);
/*    */       } 
/*    */       
/* 55 */       return 1;
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 60 */       LDAPSearchResults enumer = res.searchUpInsJldap((JdbcLdapSqlAbs)del);
/* 61 */       while (enumer.hasMore()) {
/* 62 */         LDAPEntry entry = enumer.next();
/* 63 */         con.delete(entry.getDN());
/* 64 */         count++;
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 69 */       return count;
/*    */     }
/* 71 */     catch (LDAPException ne) {
/* 72 */       throw new SQLNamingException(ne);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/Delete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */