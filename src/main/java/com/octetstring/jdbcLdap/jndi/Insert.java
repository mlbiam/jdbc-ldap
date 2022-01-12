/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import com.novell.ldap.LDAPAttribute;
/*     */ import com.novell.ldap.LDAPAttributeSet;
/*     */ import com.novell.ldap.LDAPConnection;
/*     */ import com.novell.ldap.LDAPEntry;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryInsert;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapInsert;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*     */ import com.octetstring.jdbcLdap.util.Pair;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttribute;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
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
/*     */ public class Insert
/*     */   implements DirectoryInsert
/*     */ {
/*     */   public void doInsert(JdbcLdapInsert insert) throws SQLException {
/*  43 */     DirContext con = insert.getContext();
/*  44 */     Attributes atts = new BasicAttributes();
/*  45 */     SqlStore store = insert.getSqlStore();
/*  46 */     String[] fields = store.getFields();
/*  47 */     String[] vals = insert.getVals();
/*  48 */     LinkedList fieldsMap = store.getFieldsMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  54 */       Iterator<Pair> it = fieldsMap.iterator();
/*  55 */       while (it.hasNext()) {
/*  56 */         Pair p = it.next();
/*  57 */         String field = p.getName();
/*     */         
/*  59 */         if (atts.get(field) == null) {
/*     */           
/*  61 */           atts.put(new BasicAttribute(field, p.getValue()));
/*     */           continue;
/*     */         } 
/*  64 */         ((BasicAttribute)atts.get(field)).add(p.getValue());
/*     */       } 
/*     */ 
/*     */       
/*  68 */       con.createSubcontext(insert.getDistinguishedName(), atts);
/*     */     }
/*  70 */     catch (NamingException ne) {
/*  71 */       throw new SQLNamingException(ne);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doInsertJldap(JdbcLdapInsert insert) throws SQLException {
/*  79 */     LDAPConnection con = insert.getConnection();
/*  80 */     Attributes atts = new BasicAttributes();
/*  81 */     SqlStore store = insert.getSqlStore();
/*  82 */     String[] fields = store.getFields();
/*  83 */     String[] vals = insert.getVals();
/*  84 */     LinkedList fieldsMap = store.getFieldsMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     LDAPAttributeSet attribs = new LDAPAttributeSet();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  97 */       Iterator<Pair> it = fieldsMap.iterator();
/*  98 */       Set dontAdd = insert.getSqlStore().getDontAdd();
/*     */       
/* 100 */       boolean usedOC = false;
/*     */       
/* 102 */       while (it.hasNext()) {
/*     */         
/* 104 */         Pair p = it.next();
/*     */ 
/*     */ 
/*     */         
/* 108 */         if (dontAdd != null && dontAdd.contains(p.getNameUpperCase())) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 116 */         String field = p.getName();
/*     */         
/* 118 */         if (field.equalsIgnoreCase("objectClass")) {
/* 119 */           usedOC = true;
/*     */         }
/*     */         
/* 122 */         LDAPAttribute attrib = attribs.getAttribute(field);
/*     */         
/* 124 */         if (attrib == null) {
/* 125 */           attrib = new LDAPAttribute(field);
/* 126 */           attribs.add(attrib);
/*     */         } 
/*     */ 
/*     */         
/* 130 */         attrib.addValue(p.getValue());
/*     */       } 
/*     */       
/* 133 */       if (!usedOC && insert.getSqlStore().getDefOC() != null) {
/* 134 */         LDAPAttribute attrib = new LDAPAttribute("objectClass");
/* 135 */         attrib.addValue(insert.getSqlStore().getDefOC());
/* 136 */         attribs.add(attrib);
/*     */       } 
/*     */ 
/*     */       
/* 140 */       con.add(new LDAPEntry(JndiLdapConnection.getRealBase((JdbcLdapSqlAbs)insert), attribs));
/*     */     }
/* 142 */     catch (LDAPException ne) {
/* 143 */       throw new SQLNamingException(ne);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/Insert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */