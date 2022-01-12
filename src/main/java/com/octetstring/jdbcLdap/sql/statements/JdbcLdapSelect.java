/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.util.TableDef;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class JdbcLdapSelect
/*     */   extends JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   static final String DEFAULT_SEARCH_FILTER = "(objectClass=*)";
/*     */   public static final String FROM_SCOPE = ";";
/*     */   static final String SELECT = "select";
/*     */   static final int SELECT_SIZE = 6;
/*     */   static final String FROM = "from";
/*     */   static final int FROM_SIZE = 4;
/*     */   static final String WHERE = "where";
/*     */   static final int WHERE_SIZE = 5;
/*     */   static final String WILDCARD = "*";
/*     */   static final String DN_FIELD = "DN";
/*     */   DirectoryRetrieveResults search;
/*     */   String sql;
/*     */   String[] fields;
/* 118 */   int maxRows = -1;
/*     */   
/*     */   SqlStore sqlStore;
/*     */   boolean retreiveDN;
/*     */   
/*     */   public Object executeQuery() throws SQLException {
/* 124 */     return this.search.searchJldap(this);
/*     */   }
/*     */   String[] sortBy; private HashMap fieldMap; private HashMap revFieldMap;
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL, SqlStore sqlStore) throws SQLException {
/* 129 */     this.search = (DirectoryRetrieveResults)con.getImplClasses().get("RETRIEVE_RESULTS");
/* 130 */     this.con = con;
/* 131 */     this.sql = this.sql;
/* 132 */     this.sqlStore = sqlStore;
/* 133 */     this.where = sqlStore.getWhere();
/* 134 */     this.from = sqlStore.getFrom();
/* 135 */     this.fields = sqlStore.getFields();
/* 136 */     this.retreiveDN = sqlStore.getDN();
/* 137 */     this.args = new Object[sqlStore.getArgs()];
/* 138 */     this.scope = sqlStore.getScope();
/* 139 */     this.sortBy = sqlStore.getOrderby();
/* 140 */     this.fieldMap = sqlStore.getFieldMap();
/* 141 */     this.revFieldMap = sqlStore.getRevFieldMap();
/*     */   }
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL) throws SQLException {
/*     */     Integer scope;
/* 146 */     this.search = (DirectoryRetrieveResults)con.getImplClasses().get("RETRIEVE_RESULTS");
/* 147 */     this.con = con;
/* 148 */     this.retreiveDN = false;
/*     */     
/* 150 */     SQL = SQL.trim();
/* 151 */     String sql = SQL.toLowerCase();
/*     */ 
/*     */     
/* 154 */     boolean whereFound = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     int begin = sql.indexOf("select") + 6;
/* 162 */     int end = sql.indexOf("from");
/*     */     
/* 164 */     String fields = SQL.substring(begin, end);
/* 165 */     fields = fields.trim();
/*     */     
/* 167 */     if (!fields.equalsIgnoreCase("*")) {
/* 168 */       StringTokenizer toker = new StringTokenizer(fields, ",");
/* 169 */       this.fields = new String[toker.countTokens()];
/* 170 */       int i = 0;
/* 171 */       while (toker.hasMoreTokens()) {
/* 172 */         this.fields[i] = toker.nextToken().trim();
/*     */         
/* 174 */         String fieldLcase = this.fields[i].toLowerCase();
/* 175 */         int beginas = fieldLcase.indexOf(" as ");
/* 176 */         if (beginas != -1) {
/* 177 */           String fieldName = this.fields[i].substring(0, beginas).trim();
/* 178 */           String asName = this.fields[i].substring(beginas + 4).trim();
/* 179 */           this.fields[i] = fieldName;
/* 180 */           if (this.fieldMap == null) {
/* 181 */             this.fieldMap = new HashMap<Object, Object>();
/* 182 */             this.revFieldMap = new HashMap<Object, Object>();
/*     */           } 
/* 184 */           this.fieldMap.put(asName, fieldName);
/* 185 */           this.revFieldMap.put(fieldName, asName);
/*     */         } 
/*     */         
/* 188 */         if (this.fields[i].equalsIgnoreCase("DN")) this.retreiveDN = true; 
/* 189 */         i++;
/*     */       } 
/*     */     } else {
/*     */       
/* 193 */       this.fields = new String[0];
/* 194 */       this.retreiveDN = true;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     begin = sql.indexOf("from", end) + 4;
/* 199 */     end = sql.indexOf("where", begin);
/*     */ 
/*     */     
/* 202 */     if (end != -1) {
/* 203 */       this.from = SQL.substring(begin, end).trim();
/* 204 */       whereFound = true;
/*     */     } else {
/*     */       
/* 207 */       end = sql.indexOf(" order by ", begin);
/* 208 */       if (end != -1) {
/* 209 */         this.from = SQL.substring(begin, end).trim();
/* 210 */         procOrderBy(SQL, end);
/*     */       } else {
/* 212 */         this.from = SQL.substring(begin).trim();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 218 */     if (con.getTableDefs().containsKey(this.from.trim())) {
/*     */       
/* 220 */       TableDef table = (TableDef)con.getTableDefs().get(this.from.trim());
/* 221 */       this.from = table.getScopeBase();
/*     */     } 
/*     */ 
/*     */     
/* 225 */     end = this.from.indexOf(";");
/* 226 */     if (end == -1) {
/* 227 */       scope = (Integer)this.scopes.get(con.getSearchScope());
/*     */     } else {
/*     */       
/* 230 */       scope = (Integer)this.scopes.get(this.from.substring(0, end).trim());
/* 231 */       this.from = this.from.substring(end + 1).trim();
/*     */     } 
/*     */     
/* 234 */     if (scope == null) {
/* 235 */       throw new SQLException("Scope not recognized");
/*     */     }
/*     */     
/* 238 */     this.scope = scope.intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     if (whereFound) {
/* 245 */       begin = sql.indexOf("where", end) + 5;
/* 246 */       end = sql.indexOf(" order by ", begin);
/* 247 */       if (end != -1) {
/* 248 */         this.where = con.nativeSQL(sqlArgsToLdap(SQL.substring(begin, end).trim()), this.fieldMap);
/* 249 */         procOrderBy(SQL, end);
/*     */       } else {
/* 251 */         this.where = con.nativeSQL(sqlArgsToLdap(SQL.substring(begin).trim()), this.fieldMap);
/*     */       } 
/*     */     } else {
/*     */       
/* 255 */       this.where = "(objectClass=*)";
/*     */     } 
/*     */     
/* 258 */     System.out.println("Sort by : " + this.sortBy);
/*     */     
/* 260 */     this.sqlStore = new SqlStore(SQL);
/* 261 */     this.sqlStore.setOrderby(this.sortBy);
/* 262 */     this.sqlStore.setWhere(this.where);
/* 263 */     this.sqlStore.setFrom(this.from);
/* 264 */     this.sqlStore.setFields(this.fields);
/* 265 */     this.sqlStore.setDN(this.retreiveDN);
/* 266 */     this.sqlStore.setScope(this.scope);
/* 267 */     this.sqlStore.setFieldMap(this.fieldMap);
/* 268 */     this.sqlStore.setRevFieldMap(this.revFieldMap);
/* 269 */     this.sqlStore.setArgs((this.args != null) ? this.args.length : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void procOrderBy(String SQL, int end) {
/* 280 */     String order = SQL.substring(end + 10).trim();
/* 281 */     StringTokenizer toker = new StringTokenizer(order, ",");
/* 282 */     this.sortBy = new String[toker.countTokens()];
/* 283 */     for (int i = 0, m = this.sortBy.length; i < m; i++) {
/* 284 */       this.sortBy[i] = toker.nextToken();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSearchString() {
/* 292 */     return this.where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSearchAttributes() {
/* 299 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxRecords(int rec) {
/* 311 */     this.maxRows = rec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRecords() {
/* 319 */     return this.maxRows;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArgs() {
/* 330 */     return this.args;
/*     */   }
/*     */   
/*     */   public Object executeUpdate() throws SQLException {
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int pos, String value) throws SQLException {
/* 343 */     if (pos < 0 || pos > this.args.length) throw new SQLException(Integer.toString(pos) + " out of bounds"); 
/* 344 */     this.args[pos] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getSqlStore() {
/* 352 */     return this.sqlStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetrieveDN() {
/* 359 */     return this.retreiveDN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUpdate() {
/* 367 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapSelect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */