/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.novell.ldap.LDAPConnection;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
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
/*     */ public abstract class JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   static final int OBJECT_SCOPE = 0;
/*     */   static final int ONELEVEL_SCOPE = 1;
/*     */   static final int SUBTREE_SCOPE = 2;
/*     */   HashMap scopes;
/*     */   String where;
/*     */   Object[] args;
/*     */   JndiLdapConnection con;
/*     */   String from;
/*     */   int queryTimeOut;
/*     */   int scope;
/*     */   
/*     */   public abstract void init(JndiLdapConnection paramJndiLdapConnection, String paramString) throws SQLException;
/*     */   
/*     */   public abstract void init(JndiLdapConnection paramJndiLdapConnection, String paramString, SqlStore paramSqlStore) throws SQLException;
/*     */   
/*     */   public abstract Object executeQuery() throws SQLException;
/*     */   
/*     */   public abstract Object executeUpdate() throws SQLException;
/*     */   
/*     */   public abstract void setValue(int paramInt, String paramString) throws SQLException;
/*     */   
/*     */   public abstract SqlStore getSqlStore();
/*     */   
/*     */   public abstract boolean getRetrieveDN();
/*     */   
/*     */   public JdbcLdapSqlAbs() {
/*  95 */     this.scopes = new HashMap<Object, Object>();
/*     */     
/*  97 */     this.scopes.put("objectScope", new Integer(0));
/*  98 */     this.scopes.put("oneLevelScope", new Integer(1));
/*  99 */     this.scopes.put("subTreeScope", new Integer(2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String sqlArgsToLdap(String where) {
/* 109 */     StringBuffer buff = new StringBuffer(where);
/* 110 */     StringBuffer arg = new StringBuffer();
/* 111 */     int num = 0;
/*     */     
/* 113 */     int curr = where.indexOf("?");
/*     */     
/* 115 */     if (curr == -1) {
/*     */       
/* 117 */       this.args = new Object[0];
/* 118 */       return where;
/*     */     } 
/*     */     
/* 121 */     while (curr != -1) {
/* 122 */       if (buff.charAt(curr - 1) != '\\') {
/* 123 */         arg.setLength(0);
/* 124 */         buff.replace(curr, curr + 1, arg.append("{").append(num).append("}").toString());
/* 125 */         num++;
/*     */       } else {
/*     */         
/* 128 */         buff.deleteCharAt(curr - 1);
/* 129 */         curr++;
/*     */       } 
/* 131 */       curr = buff.toString().indexOf('?', curr + 1);
/*     */     } 
/* 133 */     this.args = new Object[num];
/* 134 */     return buff.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilterWithParams() {
/* 141 */     if (this.where == null) return null;
/*     */     
/* 143 */     if (this.args == null) this.args = new Object[0];
/*     */     
/* 145 */     int m = this.args.length;
/* 146 */     StringBuffer buf = new StringBuffer(this.where);
/* 147 */     StringBuffer search = new StringBuffer();
/*     */     
/* 149 */     for (int i = 0; i < m; i++) {
/* 150 */       search.setLength(0);
/* 151 */       search.append("{").append(i).append("}");
/* 152 */       int begin = buf.toString().indexOf(search.toString());
/*     */       
/* 154 */       if (begin != -1) {
/*     */         
/* 156 */         int end = begin + search.length();
/* 157 */         buf.replace(begin, end, this.args[i].toString());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirContext getContext() {
/* 172 */     return this.con.getContext();
/*     */   }
/*     */   
/*     */   public LDAPConnection getConnection() {
/* 176 */     return this.con.getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBaseContext() {
/* 183 */     return this.from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeOut(int time) {
/* 191 */     this.queryTimeOut = time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTimeOut() {
/* 199 */     return this.queryTimeOut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSearchScope() {
/* 206 */     return this.scope;
/*     */   }
/*     */   
/*     */   protected LinkedList explodeDN(String dn) {
/* 210 */     LinkedList<String> rdnComponents = new LinkedList();
/*     */     
/* 212 */     String dnstr = dn.toString();
/*     */     
/* 214 */     boolean inquotes = false;
/* 215 */     boolean escaped = false;
/*     */     
/* 217 */     int currentStart = 0;
/* 218 */     char current = Character.MIN_VALUE;
/* 219 */     for (int i = 0; i < dnstr.length(); i++) {
/* 220 */       current = dnstr.charAt(i);
/* 221 */       if (current == '\\') {
/* 222 */         escaped = !escaped;
/* 223 */       } else if ((current == '"' || current == '\'') && !escaped) {
/* 224 */         inquotes = !inquotes;
/* 225 */       } else if ((current == ',' || current == ';') && !escaped && !inquotes) {
/*     */         
/* 227 */         String currdn = dnstr.substring(currentStart, i).trim();
/* 228 */         if (currdn.endsWith("\\") && dnstr.charAt(i - 1) == ' ') {
/* 229 */           currdn = currdn + " ";
/*     */         }
/* 231 */         if (currdn.length() > 0) {
/* 232 */           rdnComponents.add(currdn);
/*     */         }
/*     */         
/* 235 */         currentStart = i + 1;
/*     */       } else {
/* 237 */         escaped = false;
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     if (dnstr.length() > currentStart) {
/* 242 */       String currdn = dnstr.substring(currentStart, dnstr.length()).trim();
/*     */       
/* 244 */       if (currdn.length() > 0) {
/* 245 */         rdnComponents.add(currdn);
/*     */       }
/*     */     } 
/* 248 */     return rdnComponents;
/*     */   }
/*     */   
/*     */   public String getConnectionBase() {
/* 252 */     return this.con.getBaseContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public JndiLdapConnection getJDBCConnection() {
/* 257 */     return this.con;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapSqlAbs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */