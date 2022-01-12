/*     */ package com.octetstring.jdbcLdap.sql.statements;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.backend.DirectoryUpdateEntry;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.util.Pair;
/*     */ import com.octetstring.jdbcLdap.util.TableDef;
/*     */ import com.octetstring.jdbcLdap.util.UpdateSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */ public class JdbcLdapUpdateEntry
/*     */   extends JdbcLdapSqlAbs
/*     */   implements JdbcLdapSql
/*     */ {
/*     */   public static final String UPDATE_ENTRY = "update entry";
/*     */   public static final String DELETE = "delete";
/*     */   public static final String ADD = "add";
/*     */   public static final String REPLACE = "replace";
/*     */   public static final String WHERE = " where ";
/*     */   public static final String DO = " do ";
/*     */   public static final String SET = " set ";
/*     */   public static final String QMARK = "?";
/*     */   public static final String SEMI_COLON = ";";
/*     */   String cmd;
/*     */   LinkedList cmds;
/*     */   int numArgs;
/*     */   String[] argVals;
/*     */   SqlStore sqlStore;
/*     */   ArrayList offset;
/*     */   LinkedList attribs;
/*     */   private DirectoryUpdateEntry ue;
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL) throws SQLException {
/* 100 */     this.ue = (DirectoryUpdateEntry)con.getImplClasses().get("UpdateEntry");
/*     */ 
/*     */     
/* 103 */     int paramcount = 0;
/*     */     
/* 105 */     this.cmds = new LinkedList();
/*     */ 
/*     */     
/* 108 */     boolean hasWhere = true;
/*     */ 
/*     */     
/* 111 */     String lsql = SQL.toLowerCase();
/* 112 */     int begin = lsql.indexOf("update entry") + "update entry".length();
/* 113 */     int end = lsql.indexOf(" do ");
/* 114 */     String dn = SQL.substring(begin, end).trim();
/*     */     
/* 116 */     if (con.getTableDefs().containsKey(dn)) {
/* 117 */       dn = ((TableDef)con.getTableDefs().get(dn)).getScopeBase();
/*     */     }
/*     */ 
/*     */     
/* 121 */     this.where = null;
/*     */     
/* 123 */     int semi = dn.indexOf(";");
/*     */     
/* 125 */     if (semi != -1) {
/* 126 */       String sscope = dn.substring(0, semi);
/* 127 */       dn = dn.substring(semi + 1);
/* 128 */       Integer iscope = (Integer)this.scopes.get(sscope);
/* 129 */       if (iscope != null) {
/* 130 */         this.scope = iscope.intValue();
/*     */       } else {
/*     */         
/* 133 */         iscope = (Integer)this.scopes.get(con.getScope());
/* 134 */         if (iscope == null) {
/* 135 */           throw new SQLException("Invalid search scope : " + con.getScope());
/*     */         }
/* 137 */         this.scope = iscope.intValue();
/*     */       } 
/*     */     } else {
/*     */       
/* 141 */       Integer iscope = (Integer)this.scopes.get(con.getScope());
/* 142 */       if (iscope == null) {
/* 143 */         throw new SQLException("Invalid search scope : " + con.getScope());
/*     */       }
/* 145 */       this.scope = iscope.intValue();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     begin = end;
/* 154 */     end = lsql.indexOf(" where ");
/* 155 */     if (end == -1) {
/* 156 */       hasWhere = false;
/* 157 */       end = lsql.length();
/*     */     } 
/*     */     
/* 160 */     int whereIndex = end;
/*     */     
/* 162 */     String cmdSQL = SQL.substring(begin, end);
/* 163 */     String lCmdSql = cmdSQL.toLowerCase();
/*     */     
/* 165 */     boolean ok = true;
/* 166 */     begin = 0;
/* 167 */     this.offset = new ArrayList();
/* 168 */     int params = 0;
/* 169 */     while (ok) {
/*     */ 
/*     */ 
/*     */       
/* 173 */       begin = lCmdSql.indexOf(" do ", begin) + " do ".length();
/*     */       
/* 175 */       end = lCmdSql.indexOf(" set ", begin);
/*     */       
/* 177 */       String cmd = cmdSQL.substring(begin, end).trim();
/*     */ 
/*     */       
/* 180 */       begin = lCmdSql.indexOf(" set ", end) + " set ".length();
/*     */ 
/*     */       
/* 183 */       end = lCmdSql.indexOf(" do ", begin);
/* 184 */       if (end == -1) {
/* 185 */         ok = false;
/* 186 */         end = lCmdSql.length();
/*     */       } 
/*     */ 
/*     */       
/* 190 */       String attribs = cmdSQL.substring(begin, end);
/*     */ 
/*     */       
/* 193 */       if (ok) {
/* 194 */         begin = end;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 199 */       ArrayList<Pair> attribList = new ArrayList(5);
/* 200 */       this.cmds.add(new UpdateSet(cmd, attribList));
/* 201 */       if (cmd.equalsIgnoreCase("add") || cmd.equalsIgnoreCase("replace")) {
/*     */ 
/*     */         
/* 204 */         LinkedList attribsExploded = explodeDN(attribs);
/*     */         
/* 206 */         Iterator<String> itoker = attribsExploded.iterator();
/* 207 */         while (itoker.hasNext()) {
/*     */           
/* 209 */           String attr = itoker.next();
/*     */           
/* 211 */           String attribName = attr.substring(0, attr.indexOf("="));
/* 212 */           String attribValue = attr.substring(attr.indexOf("=") + 1);
/*     */           
/* 214 */           if (attribValue.charAt(0) == '"' || attribValue.charAt(0) == '\'') {
/* 215 */             attribValue = attribValue.substring(1, attribValue.length() - 1);
/*     */           }
/*     */           
/* 218 */           if (attribValue.trim().equals("?")) {
/* 219 */             this.offset.add(new Integer(params++));
/*     */           }
/*     */           
/* 222 */           attribList.add(new Pair(attribName.trim(), attribValue));
/*     */         } 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 230 */       StringTokenizer toker = new StringTokenizer(attribs, ",", false);
/* 231 */       while (toker.hasMoreTokens()) {
/* 232 */         String attr = toker.nextToken();
/* 233 */         if (attr.indexOf('=') != -1) {
/* 234 */           String attribName = attr.substring(0, attr.indexOf("="));
/* 235 */           String attribValue = attr.substring(attr.indexOf("=") + 1);
/* 236 */           attribList.add(new Pair(attribName.trim(), attribValue.trim())); continue;
/*     */         } 
/* 238 */         attribList.add(new Pair(attr.trim(),""));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     if (hasWhere) {
/* 248 */       begin = whereIndex + " where ".length();
/* 249 */       this.where = con.nativeSQL(sqlArgsToLdap(SQL.substring(begin).trim()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 254 */     this.sqlStore = new SqlStore(SQL);
/* 255 */     this.sqlStore.setDistinguishedName(dn);
/*     */ 
/*     */ 
/*     */     
/* 259 */     this.numArgs = this.offset.size();
/*     */     
/* 261 */     this.argVals = new String[this.numArgs];
/* 262 */     this.from = dn;
/* 263 */     this.sqlStore.setScope(this.scope);
/*     */     
/* 265 */     this.sqlStore.setCommand(this.cmd);
/* 266 */     this.sqlStore.setArgs(this.numArgs);
/* 267 */     this.sqlStore.setAttribs(this.attribs);
/*     */     
/* 269 */     this.sqlStore.setCmds(this.cmds);
/* 270 */     this.sqlStore.setOffsetList(this.offset);
/* 271 */     if (this.where == null || this.where.trim().length() == 0) {
/* 272 */       this.where = "(objectClass=*)";
/*     */     }
/*     */     
/* 275 */     this.sqlStore.setWhere(this.where);
/* 276 */     this.con = con;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JndiLdapConnection con, String SQL, SqlStore sqlStore) throws SQLException {
/* 285 */     this.ue = (DirectoryUpdateEntry)con.getImplClasses().get("UpdateEntry");
/* 286 */     this.con = con;
/* 287 */     this.sqlStore = sqlStore;
/*     */     
/* 289 */     this.numArgs = sqlStore.getArgs();
/* 290 */     this.attribs = sqlStore.getAttribs();
/*     */     
/* 292 */     this.cmd = sqlStore.getCommand();
/* 293 */     this.cmds = sqlStore.getCmds();
/* 294 */     this.argVals = new String[this.numArgs];
/* 295 */     this.offset = sqlStore.getOffsetList();
/* 296 */     this.argVals = new String[this.offset.size()];
/* 297 */     this.where = sqlStore.getWhere();
/* 298 */     this.scope = sqlStore.getScope();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object executeQuery() throws SQLException {
/* 306 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object executeUpdate() throws SQLException {
/* 314 */     return new Integer(this.ue.doUpdateEntryJldap(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int pos, String value) throws SQLException {
/* 321 */     if (pos < this.argVals.length) {
/* 322 */       this.argVals[pos] = value;
/*     */     } else {
/*     */       
/* 325 */       this.args[pos - this.argVals.length] = value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getSqlStore() {
/* 334 */     return this.sqlStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetrieveDN() {
/* 342 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUpdate() {
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getArgVals() {
/* 357 */     return this.argVals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList getAttribs() {
/* 364 */     return this.attribs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCmd() {
/* 371 */     return this.cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumArgs() {
/* 380 */     return this.numArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList getCmds() {
/* 388 */     return this.cmds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCmds(LinkedList list) {
/* 395 */     this.cmds = list;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/statements/JdbcLdapUpdateEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */