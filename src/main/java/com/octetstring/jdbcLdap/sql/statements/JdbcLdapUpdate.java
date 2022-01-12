/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryUpdate;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.util.TableDef;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ public class JdbcLdapUpdate
/*     */   extends JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   static final char QUOTE = '"';
/*     */   static final String DEFAULT_SEARCH_FILTER = "(objectClass=*)";
/*     */   DirectoryUpdate update;
/*     */   static final String QMARK = "?";
/*     */   static final String EQUALS = "=";
/*     */   static final String UPDATE = "update";
/*     */   static final String SET = " set ";
/*     */   static final String WHERE = " where ";
/*     */   static final String COMMA = ",";
/*     */   SqlStore store;
/*     */   String[] fields;
/*     */   String[] vals;
/*     */   int[] offset;
/*     */   int border;
/*     */   
/*     */   public Object executeQuery() throws SQLException {
/*  87 */     throw new SQLException("UPDATE can not execute a query");
/*     */   }
/*     */   
/*     */   public Object executeUpdate() throws SQLException {
/*  91 */     return new Integer(this.update.doUpdateJldap(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetrieveDN() {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getSqlStore() {
/* 108 */     return this.store;
/*     */   }
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL) throws SQLException {
/*     */     Integer iscope;
/* 113 */     this.update = (DirectoryUpdate)con.getImplClasses().get("UPDATE");
/* 114 */     this.con = con;
/* 115 */     String tmpSQL = SQL.toLowerCase();
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
/* 127 */     int begin = tmpSQL.indexOf("update") + "update".length();
/* 128 */     int end = tmpSQL.indexOf(" set ");
/* 129 */     this.from = SQL.substring(begin, end).trim();
/*     */     
/* 131 */     if (con.getTableDefs().containsKey(this.from)) {
/* 132 */       this.from = ((TableDef)con.getTableDefs().get(this.from)).getScopeBase();
/*     */     }
/*     */     
/* 135 */     if (this.from.indexOf(";") != -1) {
/* 136 */       String sscope = this.from.substring(0, this.from.indexOf(";")).trim();
/*     */       
/* 138 */       iscope = (Integer)this.scopes.get(sscope);
/* 139 */       this.from = this.from.substring(this.from.indexOf(";") + 1);
/*     */     }
/*     */     else {
/*     */       
/* 143 */       iscope = (Integer)this.scopes.get(con.getSearchScope());
/*     */     } 
/*     */     
/* 146 */     if (iscope == null) {
/* 147 */       throw new SQLException("Unrecognized Search Scope");
/*     */     }
/*     */     
/* 150 */     this.scope = iscope.intValue();
/*     */ 
/*     */ 
/*     */     
/* 154 */     begin = tmpSQL.indexOf(" set ") + " set ".length();
/*     */     
/* 156 */     end = tmpSQL.indexOf(" where ");
/* 157 */     if (end == -1) {
/* 158 */       end = tmpSQL.length();
/*     */     }
/*     */     
/* 161 */     String set = SQL.substring(begin, end);
/*     */     
/* 163 */     LinkedList itok = explodeDN(set);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     this.fields = new String[itok.size()];
/* 173 */     this.vals = new String[itok.size()];
/* 174 */     this.offset = new int[itok.size()];
/* 175 */     Iterator<String> it = itok.iterator(); int j=0;
/* 176 */     for (int i = 0; it.hasNext(); i++) {
/* 177 */       String token = ((String)it.next()).trim();
/*     */ 
/*     */ 
/*     */       
/* 181 */       this.fields[i] = token.substring(0, token.indexOf("="));
/* 182 */       this.vals[i] = token.substring(token.indexOf("=") + 1);
/*     */ 
/*     */       
/* 185 */       if (this.vals[i].charAt(0) == '"' || this.vals[i].charAt(0) == '\'') {
/* 186 */         this.vals[i] = this.vals[i].substring(1, this.vals[i].length() - 1);
/*     */       }
/*     */ 
/*     */       
/* 190 */       if (this.vals[i].equals("?")) {
/* 191 */         this.offset[j++] = i;
/*     */       }
/* 193 */       else if (this.vals[i].charAt(0) == '"') {
/*     */         
/* 195 */         this.vals[i] = this.vals[i].substring(1, this.vals[i].lastIndexOf('"'));
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     this.border = j;
/*     */ 
/*     */     
/* 202 */     if (end == tmpSQL.length()) {
/*     */       
/* 204 */       this.where = "(objectClass=*)";
/* 205 */       this.border = -1;
/*     */     } else {
/*     */       
/* 208 */       begin = end + " where ".length();
/* 209 */       this.where = con.nativeSQL(sqlArgsToLdap(SQL.substring(begin).trim()));
/*     */     } 
/*     */     
/* 212 */     this.store = new SqlStore(SQL);
/* 213 */     this.store.setFields(this.fields);
/* 214 */     this.store.setDistinguishedName(this.from);
/* 215 */     this.store.setArgs((this.args != null) ? this.args.length : 0);
/* 216 */     this.store.setInsertFields(this.vals);
/* 217 */     this.store.setFieldOffset(this.offset);
/*     */     
/* 219 */     this.store.setWhere(this.where);
/* 220 */     this.store.setBorder(this.border);
/*     */     
/* 222 */     this.store.setScope(this.scope);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL, SqlStore sqlStore) throws SQLException {
/* 228 */     this.update = (DirectoryUpdate)con.getImplClasses().get("UPDATE");
/* 229 */     this.con = con;
/* 230 */     this.store = sqlStore;
/* 231 */     this.fields = this.store.getFields();
/* 232 */     this.from = this.store.getDistinguishedName();
/* 233 */     this.offset = this.store.getFieldOffset();
/* 234 */     this.where = this.store.getWhere();
/* 235 */     this.border = this.store.getBorder();
/* 236 */     this.args = new Object[this.store.getArgs()];
/* 237 */     this.vals = new String[this.fields.length];
/* 238 */     this.scope = this.store.getScope();
/* 239 */     System.arraycopy(sqlStore.getInsertFields(), 0, this.vals, 0, this.vals.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int pos, String value) throws SQLException {
/* 249 */     if (pos < this.border || this.border == -1) {
/* 250 */       if (pos < 0) throw new SQLException(Integer.toString(pos) + " out of bounds"); 
/* 251 */       this.vals[this.offset[pos]] = value;
/*     */     } else {
/*     */       
/* 254 */       this.args[pos - this.border] = value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getVals() {
/* 263 */     return this.vals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiLdapConnection getCon() {
/* 270 */     return this.con;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUpdate() {
/* 278 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapUpdate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */