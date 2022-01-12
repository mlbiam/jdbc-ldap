/*    */ package com.octetstring.jdbcLdap.jndi;
/*    */ 
/*    */ import com.novell.ldap.LDAPAttribute;
/*    */ import com.novell.ldap.LDAPConnection;
/*    */ import com.novell.ldap.LDAPEntry;
/*    */ import com.novell.ldap.LDAPException;
/*    */ import com.novell.ldap.LDAPModification;
/*    */ import com.novell.ldap.LDAPSearchResults;
/*    */ import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
/*    */ import com.octetstring.jdbcLdap.backend.DirectoryUpdate;
/*    */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*    */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*    */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapUpdate;
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
/*    */ 
/*    */ public class Update
/*    */   implements DirectoryUpdate
/*    */ {
/*    */   public int doUpdateJldap(JdbcLdapUpdate update) throws SQLException {
/* 43 */     DirectoryRetrieveResults res = (DirectoryRetrieveResults)update.getCon().getImplClasses().get("RETRIEVE_RESULTS");
/* 44 */     LDAPConnection con = update.getConnection();
/*    */     
/* 46 */     StringBuffer buf = new StringBuffer();
/* 47 */     SqlStore store = update.getSqlStore();
/* 48 */     int count = 0;
/*    */ 
/*    */ 
/*    */     
/* 52 */     LDAPModification[] mods = new LDAPModification[(store.getFields()).length];
/* 53 */     String[] fields = store.getFields();
/* 54 */     String[] vals = update.getVals();
/*    */     
/* 56 */     for (int i = 0, m = mods.length; i < m; i++) {
/* 57 */       mods[i] = new LDAPModification(2, new LDAPAttribute(fields[i], vals[i]));
/*    */     }
/*    */     
/*    */     try {
/* 61 */       if (update.getSearchScope() != 0) {
/* 62 */         LDAPSearchResults enumer = res.searchUpInsJldap((JdbcLdapSqlAbs)update);
/*    */         
/* 64 */         while (enumer.hasMore()) {
/* 65 */           LDAPEntry seres = enumer.next();
/* 66 */           buf.setLength(0);
/*    */           
/* 68 */           String name = seres.getDN();
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 73 */           con.modify(name, mods);
/* 74 */           count++;
/*    */         } 
/*    */       } else {
/*    */         
/* 78 */         con.modify(update.getBaseContext(), mods);
/* 79 */         count++;
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 84 */       return count;
/*    */     }
/* 86 */     catch (LDAPException ne) {
/* 87 */       throw new SQLNamingException(ne);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/Update.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */