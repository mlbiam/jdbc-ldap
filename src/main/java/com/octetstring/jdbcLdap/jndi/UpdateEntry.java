/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import com.novell.ldap.LDAPAttribute;
/*     */ import com.novell.ldap.LDAPEntry;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.novell.ldap.LDAPModification;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryUpdateEntry;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapUpdateEntry;
/*     */ import com.octetstring.jdbcLdap.util.Pair;
/*     */ import com.octetstring.jdbcLdap.util.UpdateSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
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
/*     */ public class UpdateEntry
/*     */   implements DirectoryUpdateEntry
/*     */ {
/*     */   public int doUpdateEntryJldap(JdbcLdapUpdateEntry stmt) throws SQLException {
/*  41 */     DirectoryRetrieveResults res = (DirectoryRetrieveResults)stmt.getJDBCConnection().getImplClasses().get("RETRIEVE_RESULTS");
/*  42 */     int argPos = 0;
/*  43 */     StringBuffer dn = new StringBuffer();
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
/*  56 */     Iterator<UpdateSet> icmds = stmt.getCmds().iterator();
/*     */     
/*  58 */     LinkedList<LDAPModification> mods = new LinkedList();
/*  59 */     int paramnum = 0;
/*  60 */     while (icmds.hasNext()) {
/*  61 */       int modtype; UpdateSet us = icmds.next();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  66 */       if (us.getCmd().equalsIgnoreCase("add")) {
/*  67 */         modtype = 0;
/*     */       }
/*  69 */       else if (us.getCmd().equalsIgnoreCase("delete")) {
/*  70 */         modtype = 1;
/*     */       } else {
/*     */         
/*  73 */         modtype = 2;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       Iterator<Pair> it = us.getAttribs().iterator();
/*  82 */       int i = 0;
/*  83 */       ArrayList al = new ArrayList();
/*  84 */       while (it.hasNext()) {
/*  85 */         if (modtype == 0 || modtype == 2) {
/*  86 */           String val; Pair p = it.next();
/*  87 */           String name = p.getName();
/*     */           
/*  89 */           if (p.getValue().equals("?")) {
/*     */ 
/*     */             
/*  92 */             val = stmt.getArgVals()[paramnum];
/*  93 */             paramnum++;
/*     */           }
/*     */           else {
/*     */             
/*  97 */             val = p.getValue();
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 102 */           mods.add(new LDAPModification(modtype, new LDAPAttribute(name, val)));
/*     */         } else {
/*     */           
/* 105 */           String name = (String)it.next().getName();
/*     */           
/* 107 */           mods.add(new LDAPModification(modtype, new LDAPAttribute(name)));
/*     */         } 
/* 109 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     Object[] toCopy = mods.toArray();
/* 114 */     LDAPModification[] doMods = new LDAPModification[toCopy.length];
/* 115 */     System.arraycopy(toCopy, 0, doMods, 0, doMods.length);
/*     */     
/* 117 */     StringBuffer buf = new StringBuffer();
/*     */     
/*     */     try {
/* 120 */       int count = 0;
/* 121 */       if (stmt.getSearchScope() != 0) {
/* 122 */         LDAPSearchResults enumer = res.searchUpInsJldap((JdbcLdapSqlAbs)stmt);
/* 123 */         while (enumer.hasMore()) {
/* 124 */           LDAPEntry entry = enumer.next();
/* 125 */           buf.setLength(0);
/*     */           
/* 127 */           String name = entry.getDN();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 132 */           stmt.getConnection().modify(name, doMods);
/* 133 */           count++;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 138 */         stmt.getConnection().modify(stmt.getBaseContext(), doMods);
/* 139 */         count++;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 144 */       return count;
/*     */     }
/* 146 */     catch (LDAPException ne) {
/* 147 */       throw new SQLNamingException(ne);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/UpdateEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */