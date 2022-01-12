/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.novell.ldap.LDAPDN;
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryInsert;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.util.AddPattern;
/*     */ import com.octetstring.jdbcLdap.util.Pair;
/*     */ import com.octetstring.jdbcLdap.util.TableDef;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdbcLdapInsert
/*     */   extends JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   static final String INSERT_INTO = "insert into";
/*     */   static final char LPAR = '(';
/*     */   static final char RPAR = ')';
/*     */   static final String QMARK = "?";
/*     */   static final String COMMA = ",";
/*     */   static final String EQUALS = "=";
/*     */   static final char QUOTE = '"';
/*     */   String dn;
/*     */   String[] fields;
/*     */   String[] dnFields;
/*     */   LinkedList fieldsMap;
/*     */   String sql;
/*     */   SqlStore store;
/*     */   String[] vals;
/*     */   int[] offset;
/*     */   DirectoryInsert insert;
/*     */   private Set dontAdd;
/*     */   private String defOC;
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL) throws SQLException {
/* 105 */     this.con = con;
/* 106 */     this.insert = (DirectoryInsert)con.getImplClasses().get("INSERT");
/*     */ 
/*     */     
/* 109 */     String tmpSQL = SQL.toLowerCase();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     this.con = con;
/*     */ 
/*     */     
/* 119 */     int begin = tmpSQL.indexOf("insert into");
/* 120 */     begin += "insert into".length();
/* 121 */     int end = tmpSQL.indexOf('(');
/*     */     
/* 123 */     String tmp = SQL.substring(begin, end);
/* 124 */     this.dn = tmp.trim();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     TableDef def = null;
/* 130 */     if (con != null) {
/* 131 */       def = (TableDef)con.getTableDefs().get(this.dn);
/*     */     }
/* 133 */     if (def == null) {
/* 134 */       LinkedList linkedList = explodeDN(this.dn);
/* 135 */       parseCtx(linkedList);
/*     */     } 
/*     */     
/* 138 */     this.fieldsMap = new LinkedList();
/*     */ 
/*     */     
/* 141 */     begin = end + 1;
/* 142 */     end = tmpSQL.indexOf(')', begin);
/* 143 */     tmp = SQL.substring(begin, end);
/*     */     
/* 145 */     StringTokenizer tok = new StringTokenizer(tmp, ",", false);
/* 146 */     this.fields = new String[tok.countTokens()];
/*     */     
/* 148 */     HashMap addPattern = null;
/*     */     
/* 150 */     if (def != null) {
/* 151 */       addPattern = def.getAddPatterns();
/*     */     }
/*     */     int i;
/* 154 */     for (i = 0; tok.hasMoreTokens(); i++) {
/* 155 */       this.fields[i] = tok.nextToken();
/* 156 */       if (addPattern != null) {
/* 157 */         Object o = addPattern.get(this.fields[i]);
/* 158 */         if (o != null) {
/* 159 */           if (o instanceof HashMap) {
/* 160 */             addPattern = (HashMap)o;
/*     */           } else {
/* 162 */             AddPattern pat = (AddPattern)o;
/* 163 */             this.dn = pat.getAddPattern() + "," + def.getBase();
/* 164 */             this.dontAdd = pat.getNotToAdd();
/* 165 */             this.defOC = pat.getDefaultOC();
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 172 */     if (def != null) {
/* 173 */       LinkedList linkedList = explodeDN(this.dn);
/* 174 */       parseCtx(linkedList);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 179 */     begin = end + 1;
/* 180 */     begin = tmpSQL.indexOf('(', begin) + 1;
/* 181 */     end = tmpSQL.indexOf(')', begin);
/*     */     
/* 183 */     tmp = SQL.substring(begin, end);
/*     */ 
/*     */     
/* 186 */     LinkedList ltoks = explodeDN(tmp);
/* 187 */     this.vals = new String[ltoks.size()];
/* 188 */     this.offset = new int[ltoks.size()];
/* 189 */     Iterator<String> it = ltoks.iterator();
/*     */     int j;
/* 191 */     for (i = 0, j = 0; it.hasNext(); i++) {
/* 192 */       this.vals[i] = it.next();
/*     */ 
/*     */       
/* 195 */       if (this.vals[i].charAt(0) == '"' || this.vals[i].charAt(0) == '\'') {
/* 196 */         this.vals[i] = this.vals[i].substring(1, this.vals[i].length() - 1);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       if (this.vals[i].equals("?")) {
/*     */         
/* 205 */         this.offset[j++] = i;
/*     */ 
/*     */       
/*     */       }
/* 209 */       else if (this.vals[i].charAt(0) == '"' || this.vals[i].charAt(0) == '\'') {
/* 210 */         this.vals[i] = this.vals[i].substring(1, this.vals[i].length() - 2);
/*     */       } 
/*     */ 
/*     */       
/* 214 */       this.fieldsMap.add(new Pair(this.fields[i], this.vals[i]));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     this.store = new SqlStore(SQL);
/* 223 */     this.store.setFields(this.fields);
/* 224 */     this.store.setDistinguishedName(this.dn);
/* 225 */     this.store.setArgs(this.vals.length);
/* 226 */     this.store.setInsertFields(this.vals);
/* 227 */     this.store.setFieldOffset(this.offset);
/* 228 */     this.store.setDnFields(this.dnFields);
/* 229 */     this.store.setFieldsMap(this.fieldsMap);
/* 230 */     this.store.setDontAdd(this.dontAdd);
/* 231 */     this.store.setDefaultOC(this.defOC);
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
/*     */   private void parseCtx(LinkedList ltoks) {
/* 243 */     this.dnFields = new String[ltoks.size()];
/* 244 */     Iterator<String> it = ltoks.iterator();
/* 245 */     int i = 0;
/* 246 */     while (it.hasNext()) {
/* 247 */       this.dnFields[i] = it.next();
/*     */ 
/*     */       
/* 250 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL, SqlStore sqlStore) throws SQLException {
/* 257 */     this.insert = (DirectoryInsert)con.getImplClasses().get("INSERT");
/* 258 */     this.con = con;
/* 259 */     this.sql = SQL;
/* 260 */     this.store = sqlStore;
/* 261 */     this.fields = sqlStore.getFields();
/* 262 */     this.dn = this.store.getDistinguishedName();
/* 263 */     this.vals = new String[sqlStore.getArgs()];
/* 264 */     System.arraycopy(sqlStore.getInsertFields(), 0, this.vals, 0, this.vals.length);
/* 265 */     this.offset = sqlStore.getFieldOffset();
/* 266 */     this.dnFields = sqlStore.getDnFields();
/* 267 */     this.fieldsMap = new LinkedList();
/* 268 */     this.fieldsMap.addAll(this.store.getFieldsMap());
/* 269 */     this.dontAdd = this.store.getDontAdd();
/* 270 */     this.defOC = this.store.getDefOC();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object executeQuery() throws SQLException {
/* 275 */     return null;
/*     */   }
/*     */   
/*     */   public Object executeUpdate() throws SQLException {
/* 279 */     this.insert.doInsertJldap(this);
/* 280 */     return new Integer(1);
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
/*     */   public void setValue(int pos, String value) throws SQLException {
/* 292 */     ((Pair)this.fieldsMap.get(this.offset[pos])).setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getSqlStore() {
/* 301 */     return this.store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetrieveDN() {
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirContext getContext() {
/* 316 */     return this.con.getContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getVals() {
/* 324 */     return this.vals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUpdate() {
/* 332 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDistinguishedName() {
/* 340 */     StringBuffer fdn = new StringBuffer();
/*     */     
/* 342 */     HashMap<Object, Object> track = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */     
/* 346 */     Object[] fields = this.fieldsMap.toArray();
/*     */     
/* 348 */     for (int i = 0; i < this.dnFields.length; i++) {
/* 349 */       if (this.dnFields[i].indexOf('=') != -1) {
/*     */         
/* 351 */         fdn.append(this.dnFields[i]).append(",");
/*     */       } else {
/* 353 */         int start; String val = "";
/* 354 */         if (track.containsKey(this.dnFields[i])) {
/* 355 */           start = ((Integer)track.get(this.dnFields[i])).intValue() + 1;
/*     */         } else {
/*     */           
/* 358 */           start = 0;
/*     */         } 
/*     */         
/* 361 */         for (int j = start, m = fields.length; j < m; j++) {
/* 362 */           if (((Pair)fields[j]).getName().equalsIgnoreCase(this.dnFields[i])) {
/* 363 */             track.put(this.dnFields[i], new Integer(j));
/* 364 */             val = ((Pair)fields[j]).getValue();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 370 */         String tmpv = LDAPDN.escapeRDN(this.dnFields[i] + "=" + val);
/*     */         
/* 372 */         fdn.append(tmpv).append(",");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 378 */     String finalDN = fdn.toString();
/*     */     
/* 380 */     return finalDN.toString().substring(0, finalDN.length() - 1);
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapInsert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */