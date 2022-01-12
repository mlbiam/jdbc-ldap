/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 

/*     */ import com.novell.ldap.LDAPConnection;
/*     */ import com.novell.ldap.LDAPConstraints;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.novell.ldap.LDAPJSSESecureSocketFactory;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.novell.ldap.LDAPSocketFactory;
/*     */ import com.novell.ldap.LDAPUrl;

/*     */ import com.octetstring.jdbcLdap.sql.JdbcLdapDBMetaData;
/*     */ import com.octetstring.jdbcLdap.sql.JdbcLdapPreparedStatement;
/*     */ import com.octetstring.jdbcLdap.sql.JdbcLdapStatement;
/*     */ import com.octetstring.jdbcLdap.sql.SqlStore;
/*     */ import com.octetstring.jdbcLdap.sql.SqlToLdap;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapInsert;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSelect;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSqlAbs;
/*     */ import com.octetstring.jdbcLdap.util.AddPattern;
/*     */ import com.octetstring.jdbcLdap.util.TableDef;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
import java.sql.Array;
import java.sql.Blob;
/*     */ import java.sql.CallableStatement;
import java.sql.Clob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
import java.sql.SQLXML;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
import java.sql.Struct;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
import java.util.concurrent.Executor;

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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiLdapConnection
/*     */   implements Connection
/*     */ {
/*     */   public static final String IMPL_UPDATE_ENTRY = "UpdateEntry";
/*     */   public static final String IMPL_UPDATE = "UPDATE";
/*     */   public static final String IMPL_RETRIEVE_RESULTS = "RETRIEVE_RESULTS";
/*     */   public static final String IMPL_INSERT = "INSERT";
/*     */   public static final String IMPL_DELETE = "DELETE";
/*     */   public static final String DSML_BASE_DN = "DSML_BASE_DN";
/*     */   public static final String SPML_IMPL = "SPML_IMPL";
/*     */   public static final String SPML_BASE_DN = "SPML_BASE_DN";
/*     */   public static final String LDAP_COMMA = "\\,";
/*     */   public static final String LDAP_EQUALS = "\\=";
/*     */   public static final String LDAP_PLUS = "\\+";
/*     */   public static final String LDAP_LESS = "\\<";
/*     */   public static final String LDAP_GREATER = "\\>";
/*     */   public static final String LDAP_SEMI_COLON = "\\;";
/*     */   public static final String OBJECT_SCOPE = "objectScope";
/*     */   public static final String ONELEVEL_SCOPE = "oneLevelScope";
/*     */   public static final String SUBTREE_SCOPE = "subTreeScope";
/*     */   public static final String CONCAT_ATTS = "CONCAT_ATTS";
/*     */   public static final String EXP_ROWS = "EXP_ROWS";
/*     */   public static final String AUTHENTICATION_TYPE = "java.naming.security.authentication";
/*     */   public static final String NO_AUTHENTICATION = "none";
/*     */   public static final String SIMPLE_AUTHENTICATION = "simple";
/*     */   public static final String SEARCH_SCOPE = "SEARCH_SCOPE";
/*     */   public static final String CACHE_STATEMENTS = "CACHE_STATEMENTS";
/*     */   public static final String PRE_FETCH = "PRE_FETCH";
/*     */   public static final String SIZE_LIMIT = "SIZE_LIMIT";
/*     */   public static final String TIME_LIMIT = "TIME_LIMIT";
/*     */   public static final String NO_SOAP = "NO_SOAP";
/*     */   public static final String IGNORE_TRANSACTIONS = "IGNORE_TRANSACTIONS";
/*     */   public static final String BACKEND_PACKAGE = "BACKEND_PACKAGE";
/*     */   public static final String NO_CONNECTION = "NO_CONNECTION";
/*     */   static final String USER = "user";
/*     */   static final String PASSWORD = "password";
/*     */   static final String SECURE = "secure";
/*     */   static final int ELIM_JDBC = 5;
/*     */   static final int ELIM_JDBC_DSML = 12;
/*     */   static final int ELIM_JDBC_SPML = 12;
/*     */   HashMap statements;
/*     */   LDAPConnection con;
/*     */   Hashtable env;
/*     */   boolean cacheStatements;
/*     */   boolean expandRow = false;
/*     */   String scope;
/*     */   String baseDN;
/*     */   SqlToLdap sql2ldap;
/*     */   boolean concatAtts;
/*     */   StringBuffer tmpBuff;
/*     */   private boolean ignoreTransactions;
/*     */   boolean preFetch;
/*     */   private int size;
/*     */   private int time;
/*     */   private boolean isDsml;
/*     */   private boolean isSPML;
/*     */   private boolean noSoap;
/*     */   HashMap tables;
/*     */   private String url;
/*     */   private String user;
/*     */   private String implPackage;
/*     */   private boolean noConnection;
/*     */   HashMap implClasses;
/*     */   
/*     */   public void cacheStatement(String sql, SqlStore stmt) {
/* 208 */     if (cacheStatements()) {
/* 209 */       this.statements.put(sql, stmt);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore getCache(String sql) {
/* 217 */     return (SqlStore)this.statements.get(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConcatAtts(boolean concat) {
/* 225 */     this.concatAtts = concat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getConcatAtts() {
/* 233 */     return this.concatAtts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirContext getContext() {
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cacheStatements() {
/* 248 */     return this.cacheStatements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSearchScope() {
/* 256 */     return this.scope;
/*     */   }
/*     */   
/*     */   public String getBaseDN() {
/* 260 */     return this.baseDN;
/*     */   }
/*     */   
/*     */   public JndiLdapConnection(LDAPConnection connection) {
/* 264 */     this.statements = new HashMap<Object, Object>();
/* 265 */     this.sql2ldap = new SqlToLdap();
/* 266 */     this.env = new Hashtable<Object, Object>();
/* 267 */     this.concatAtts = false;
/* 268 */     boolean authFound = false;
/* 269 */     this.tmpBuff = new StringBuffer();
/*     */     
/* 271 */     this.con = connection;
/* 272 */     this.baseDN = "";
/* 273 */     this.cacheStatements = true;
/* 274 */     this.tables = new HashMap<Object, Object>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiLdapConnection(String url, Properties props) throws SQLException {
/* 282 */     this.url = url;
/* 283 */     this.statements = new HashMap<Object, Object>();
/* 284 */     this.sql2ldap = new SqlToLdap();
/* 285 */     this.env = new Hashtable<Object, Object>();
/* 286 */     this.concatAtts = false;
/* 287 */     boolean authFound = false;
/* 288 */     this.tmpBuff = new StringBuffer();
/* 289 */     this.ignoreTransactions = false;
/* 290 */     this.isDsml = url.startsWith("jdbc:dsml");
/* 291 */     this.isSPML = url.startsWith("jdbc:spml");
/* 292 */     String spmlImpl = null;
/* 293 */     this.noConnection = false;
/* 294 */     this.implPackage = "com.octetstring.jdbcLdap.jndi";
/*     */     
/* 296 */     Enumeration<?> en = props.propertyNames();
/*     */     
/* 298 */     this.tables = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */     
/* 302 */     String user = null, pass = null;
/*     */     
/* 304 */     this.preFetch = true;
/* 305 */     boolean secure = false;
/* 306 */     boolean isLDAP = url.startsWith("jdbc:ldap");
/*     */     
/* 308 */     this.noSoap = false;
/* 309 */     while (en.hasMoreElements()) {
/* 310 */       String prop = (String)en.nextElement();
/* 311 */       if (prop.equalsIgnoreCase("secure")) {
/* 312 */         if (props.getProperty(prop) != null && props.getProperty(prop).equalsIgnoreCase("true"))
/* 313 */           secure = true;  continue;
/*     */       } 
/* 315 */       if (prop.equalsIgnoreCase("NO_CONNECTION") && props.getProperty(prop).equalsIgnoreCase("true")) {
/* 316 */         this.noConnection = true;
/*     */       }
/*     */     } 
/*     */     
/* 320 */     en = props.propertyNames();
/*     */     
/* 322 */     if (isLDAP && !this.noConnection) {
/* 323 */       LDAPUrl ldapUrl; if (secure) {
/* 324 */         this.con = new LDAPConnection((LDAPSocketFactory)new LDAPJSSESecureSocketFactory());
/*     */       } else {
/* 326 */         this.con = new LDAPConnection();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 334 */         ldapUrl = new LDAPUrl(url.substring(5));
/* 335 */         this.con.connect(ldapUrl.getHost(), ldapUrl.getPort());
/* 336 */       } catch (MalformedURLException e1) {
/* 337 */         throw new SQLNamingException(e1);
/* 338 */       } catch (LDAPException e1) {
/* 339 */         throw new SQLNamingException(e1);
/*     */       } 
/*     */       
/* 342 */       this.baseDN = ldapUrl.getDN();
/* 343 */       if (this.baseDN == null) {
/* 344 */         this.baseDN = "";
/*     */       }
/*     */     }
/* 347 */      else if (this.noConnection) {
/* 375 */       this.con = null;
/* 376 */       this.baseDN = "";
/*     */     } 
/*     */ 
/*     */     
/* 380 */     while (en.hasMoreElements()) {
/* 381 */       String prop = (String)en.nextElement();
/*     */       
/* 383 */       if (prop.equalsIgnoreCase("user")) {
/* 384 */         user = props.getProperty(prop);
/* 385 */         this.user = user; continue;
/*     */       } 
/* 387 */       if (prop.equalsIgnoreCase("password")) {
/* 388 */         pass = props.getProperty(prop); continue;
/*     */       } 
/* 390 */       if (prop.equalsIgnoreCase("CACHE_STATEMENTS")) {
/* 391 */         this.cacheStatements = props.getProperty(prop).equalsIgnoreCase("true"); continue;
/*     */       } 
/* 393 */       if (prop.equalsIgnoreCase("EXP_ROWS")) {
/* 394 */         this.expandRow = props.getProperty(prop).equalsIgnoreCase("true"); continue;
/*     */       } 
/* 396 */       if (prop.equalsIgnoreCase("SEARCH_SCOPE")) {
/* 397 */         this.scope = props.getProperty(prop); continue;
/*     */       } 
/* 399 */       if (prop.equalsIgnoreCase("java.naming.security.authentication")) {
/*     */         continue;
/*     */       }
/*     */       
/* 403 */       if (prop.equalsIgnoreCase("CONCAT_ATTS")) {
/* 404 */         this.concatAtts = props.getProperty(prop).equalsIgnoreCase("true");
/*     */         continue;
/*     */       } 
/* 407 */       if (prop.equalsIgnoreCase("IGNORE_TRANSACTIONS")) {
/* 408 */         this.ignoreTransactions = props.getProperty(prop).equalsIgnoreCase("true"); continue;
/* 409 */       }  if (prop.equalsIgnoreCase("PRE_FETCH")) {
/* 410 */         this.preFetch = "PRE_FETCH".equalsIgnoreCase(props.getProperty(prop)); continue;
/* 411 */       }  if (prop.equalsIgnoreCase("SIZE_LIMIT")) {
/* 412 */         this.size = Integer.parseInt(props.getProperty(prop)); continue;
/*     */       } 
/* 414 */         if (prop.equalsIgnoreCase("TIME_LIMIT")) {
/* 418 */         this.time = Integer.parseInt(props.getProperty(prop));
/* 419 */         if (this.time > 0) {
/* 420 */           LDAPConstraints genconstraints = this.con.getConstraints();
/* 421 */           genconstraints.setTimeLimit(this.time);
/* 422 */           this.con.setConstraints(genconstraints);
/*     */         }  continue;
/* 424 */       }  if (prop.equalsIgnoreCase("TABLE_DEF")) {
/*     */         try {
/* 426 */           generateTables(props.getProperty(prop));
/* 427 */         } catch (FileNotFoundException e1) {
/* 428 */           throw new SQLNamingException(e1);
/* 429 */         } catch (IOException e1) {
/* 430 */           throw new SQLNamingException(e1);
/* 431 */         } catch (LDAPException e1) {
/* 432 */           throw new SQLNamingException(e1);
/*     */         }  continue;
/* 434 */       }  if (prop.equalsIgnoreCase("BACKEND_PACKAGE")) {
/* 435 */         this.implPackage = props.getProperty(prop);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 445 */       if (user != null && pass != null && !this.noConnection) this.con.bind(3, user, pass.getBytes());
/*     */     
/* 447 */     } catch (LDAPException e) {
/* 448 */       throw new SQLNamingException(e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 454 */     this.implClasses = new HashMap<Object, Object>();
/*     */     try {
/* 456 */       this.implClasses.put("DELETE", Class.forName(this.implPackage + ".Delete").newInstance());
/* 457 */       this.implClasses.put("INSERT", Class.forName(this.implPackage + ".Insert").newInstance());
/* 458 */       this.implClasses.put("RETRIEVE_RESULTS", Class.forName(this.implPackage + ".RetrieveResults").newInstance());
/* 459 */       this.implClasses.put("UPDATE", Class.forName(this.implPackage + ".Update").newInstance());
/* 460 */       this.implClasses.put("UpdateEntry", Class.forName(this.implPackage + ".UpdateEntry").newInstance());
/* 461 */     } catch (InstantiationException e) {
/*     */       
/* 463 */       e.printStackTrace();
/* 464 */     } catch (IllegalAccessException e) {
/*     */       
/* 466 */       e.printStackTrace();
/* 467 */     } catch (ClassNotFoundException e) {
/*     */       
/* 469 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int param) throws SQLException {
/* 480 */     if (!this.ignoreTransactions) {
/* 481 */       throw new SQLException("LDAP Does Not Support Transactions");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getAutoCommit() throws SQLException {
/* 486 */     return false;
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/*     */     try {
/* 491 */       if (this.con != null) this.con.disconnect(); 
/* 492 */     } catch (LDAPException e) {
/* 493 */       throw new SQLNamingException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void commit() throws SQLException {
/* 498 */     if (!this.ignoreTransactions) {
/* 499 */       throw new SQLException("LDAP Does Not Support Transactions");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 504 */     return (this.con == null || !this.con.isConnectionAlive());
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
/*     */ 
/*     */   
/*     */   public String ldapClean(String val) {
/* 518 */     this.tmpBuff.setLength(0);
/* 519 */     for (int i = 0, m = val.length(); i < m; i++) {
/* 520 */       char c = val.charAt(i);
/*     */       
/* 522 */       switch (c) { case ',':
/* 523 */           this.tmpBuff.append("\\,"); break;
/* 524 */         case '=': this.tmpBuff.append("\\="); break;
/* 525 */         case '+': this.tmpBuff.append("\\+"); break;
/* 526 */         case '<': this.tmpBuff.append("\\<"); break;
/* 527 */         case '>': this.tmpBuff.append("\\>"); break;
/* 528 */         case '#': this.tmpBuff.append("\\#"); break;
/* 529 */         case ';': this.tmpBuff.append("\\;");
/*     */           break;
/*     */         
/*     */         default:
/* 533 */           this.tmpBuff.append(c);
/*     */           break; }
/*     */ 
/*     */     
/*     */     } 
/* 538 */     System.out.println("cleaned? : " + this.tmpBuff.toString());
/*     */     
/* 540 */     return this.tmpBuff.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCatalog(String str) throws SQLException {}
/*     */   
/*     */   public Savepoint setSavepoint() throws SQLException {
/* 547 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() throws SQLException {
/* 551 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHoldability(int param) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {
/* 559 */     if (!this.ignoreTransactions) {
/* 560 */       throw new SQLException("LDAP Does Not Support Transactions");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getCatalog() throws SQLException {
/* 565 */     return this.baseDN;
/*     */   }
/*     */   
/*     */   public PreparedStatement prepareStatement(String str, int param) throws SQLException {
/* 569 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */   
/*     */   public PreparedStatement prepareStatement(String str, int param, int param2, int param3) throws SQLException {
/* 573 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */   
/*     */   public PreparedStatement prepareStatement(String str, int param, int param2) throws SQLException {
/* 577 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */   
/*     */   public void setAutoCommit(boolean param) throws SQLException {
/* 581 */     if (!this.ignoreTransactions) {
/* 582 */       throw new SQLException("LDAP Does Not Support Transactions");
/*     */     }
/*     */   }
/*     */   
/*     */   public CallableStatement prepareCall(String str) throws SQLException {
/* 587 */     return null;
/*     */   }
/*     */   
/*     */   public PreparedStatement prepareStatement(String str, int[] values) throws SQLException {
/* 591 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTypeMap(Map map) throws SQLException {}
/*     */   
/*     */   public int getHoldability() throws SQLException {
/* 598 */     return -1;
/*     */   }
/*     */   
/*     */   public Savepoint setSavepoint(String str) throws SQLException {
/* 602 */     return null;
/*     */   }
/*     */   
/*     */   public Statement createStatement(int param, int param1, int param2) throws SQLException {
/* 606 */     return (Statement)new JdbcLdapStatement(this);
/*     */   }
/*     */   
/*     */   public Statement createStatement(int param, int param1) throws SQLException {
/* 610 */     return (Statement)new JdbcLdapStatement(this);
/*     */   }
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 614 */     return (Statement)new JdbcLdapStatement(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nativeSQL(String str) throws SQLException {
/* 624 */     return this.sql2ldap.convertToLdap(str, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nativeSQL(String str, HashMap fieldMap) throws SQLException {
/* 634 */     return this.sql2ldap.convertToLdap(str, fieldMap);
/*     */   }
/*     */   
/*     */   public CallableStatement prepareCall(String str, int param, int param2) throws SQLException {
/* 638 */     throw new SQLException("LDAP Does Not Support Stored Procedures");
/*     */   }
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String str, int param, int param2, int param3) throws SQLException {
/* 643 */     throw new SQLException("LDAP Does Not Support Stored Procedures");
/*     */   }
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 647 */     return null;
/*     */   }
/*     */   
/*     */   public PreparedStatement prepareStatement(String str) throws SQLException {
/* 651 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean param) throws SQLException {}
/*     */   
/*     */   public Map getTypeMap() throws SQLException {
/* 658 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseSavepoint(Savepoint savepoint) throws SQLException {}
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String str, String[] str1) throws SQLException {
/* 666 */     return (PreparedStatement)new JdbcLdapPreparedStatement(str, this);
/*     */   }
/*     */   
/*     */   public int getTransactionIsolation() throws SQLException {
/* 670 */     return -1;
/*     */   }
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 674 */     return (DatabaseMetaData)new JdbcLdapDBMetaData(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {
/* 681 */     if (!this.ignoreTransactions) {
/* 682 */       throw new SQLException("LDAP Does Not Support Transactions");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpandRow() {
/* 690 */     return this.expandRow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpandRow(boolean b) {
/* 697 */     this.expandRow = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScope() {
/* 704 */     return this.scope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(String string) {
/* 711 */     this.scope = string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LDAPConnection getConnection() {
/* 719 */     return this.con;
/*     */   }
/*     */   
/*     */   public String getBaseContext() {
/* 723 */     return this.baseDN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRealBase(JdbcLdapSqlAbs sql) {
/*     */     String localBase;
/* 732 */     if (sql instanceof JdbcLdapInsert) {
/* 733 */       localBase = ((JdbcLdapInsert)sql).getDistinguishedName();
/*     */     } else {
/*     */       
/* 736 */       localBase = sql.getBaseContext();
/*     */     } 
/* 738 */     String base = sql.getConnectionBase();
/* 739 */     boolean appendBase = (base != null && base.trim().length() != 0 && !localBase.endsWith(base));
/* 740 */     boolean useComma = (localBase.trim().length() != 0);
/* 741 */     String useBase = appendBase ? (localBase + (useComma ? "," : "") + base) : localBase;
/* 742 */     return useBase;
/*     */   }
/*     */   
/*     */   public LDAPSearchResults search(String SQL) throws SQLException {
/* 746 */     JdbcLdapSelect search = new JdbcLdapSelect();
/* 747 */     search.init(this, SQL);
/* 748 */     return (LDAPSearchResults)search.executeQuery();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPreFetch() {
/* 756 */     return this.preFetch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreFetch(boolean preFetch) {
/* 762 */     this.preFetch = preFetch;
/*     */   }
/*     */   
/*     */   public void setMaxSizeLimit(int size) {
/* 766 */     this.size = size;
/*     */   }
/*     */   
/*     */   public void setMaxTimeLimit(int time) {
/* 770 */     this.time = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSizeLimit() {
/* 775 */     return this.size;
/*     */   }
/*     */   
/*     */   public int getMaxTimeLimit() {
/* 779 */     return this.time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDSML() {
/* 786 */     return this.isDsml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSPML() {
/* 793 */     return this.isSPML;
/*     */   }
/*     */   
/*     */   private void generateTables(String propsPath) throws FileNotFoundException, IOException, LDAPException {
/* 797 */     Properties props = new Properties();
/* 798 */     props.load(new FileInputStream(propsPath));
/*     */     
/* 800 */     int numTables = Integer.parseInt(props.getProperty("numTables"));
/*     */     
/* 802 */     for (int i = 0; i < numTables; i++) {
/* 803 */       String name = props.getProperty("table." + i + ".name");
/* 804 */       String base = props.getProperty("table." + i + ".base");
/* 805 */       String scope = props.getProperty("table." + i + ".scope");
/* 806 */       String objectClasses = props.getProperty("table." + i + ".ocs");
/*     */       
/* 808 */       StringTokenizer toker = new StringTokenizer(objectClasses, ",", false);
/*     */       
/* 810 */       String[] ocs = new String[toker.countTokens()];
/* 811 */       int j = 0;
/* 812 */       while (toker.hasMoreTokens()) {
/* 813 */         ocs[j++] = toker.nextToken();
/*     */       }
/*     */       
/* 816 */       String addPattern = props.getProperty("table." + i + ".addPattern");
/* 817 */       toker = new StringTokenizer(addPattern, "|");
/*     */       
/* 819 */       HashMap<Object, Object> addPatternMap = new HashMap<Object, Object>();
/*     */       
/* 821 */       while (toker.hasMoreTokens()) {
/* 822 */         String pattern = toker.nextToken();
/* 823 */         HashSet<String> dontAddSet = new HashSet();
/* 824 */         String defOC = null;
/* 825 */         if (pattern.indexOf('#') != -1) {
/* 826 */           String dontAdd = pattern.substring(pattern.indexOf('#') + 1);
/* 827 */           StringTokenizer tokda = new StringTokenizer(dontAdd, ",");
/*     */           
/* 829 */           while (tokda.hasMoreTokens()) {
/* 830 */             dontAddSet.add(tokda.nextToken().toUpperCase());
/*     */           }
/* 832 */           pattern = pattern.substring(0, pattern.indexOf('#'));
/*     */         } 
/*     */         
/* 835 */         if (pattern.indexOf('&') != -1) {
/* 836 */           defOC = pattern.substring(pattern.indexOf('&') + 1);
/* 837 */           pattern = pattern.substring(0, pattern.indexOf('&'));
/*     */         } 
/*     */         
/* 840 */         AddPattern pat = new AddPattern(pattern, dontAddSet, defOC);
/* 841 */         StringTokenizer tokpat = new StringTokenizer(pattern, ",");
/* 842 */         HashMap<Object, Object> curr = addPatternMap;
/* 843 */         while (tokpat.hasMoreTokens()) {
/* 844 */           String node = tokpat.nextToken();
/*     */           
/* 846 */           Object o = curr.get(node);
/* 847 */           if (o == null) {
/* 848 */             if (tokpat.hasMoreTokens()) {
/* 849 */               HashMap<Object, Object> n = new HashMap<Object, Object>();
/* 850 */               curr.put(node, n);
/* 851 */               curr = n; continue;
/*     */             } 
/* 853 */             curr.put(node, pat);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 859 */       TableDef tbl = new TableDef(name, base, scope, ocs, this.con, addPatternMap);
/* 860 */       this.tables.put(name, tbl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getURL() {
/* 869 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 876 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getTableDefs() {
/* 883 */     return this.tables;
/*     */   }
/*     */   
/*     */   public HashMap getImplClasses() {
/* 887 */     return this.implClasses;
/*     */   }
/*     */   
/*     */   public boolean isNoCon() {
/* 891 */     return this.noConnection;
/*     */   }
/*     */
@Override
public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public Clob createClob() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Blob createBlob() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public NClob createNClob() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public SQLXML createSQLXML() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public boolean isValid(int timeout) throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void setClientInfo(String name, String value) throws SQLClientInfoException {
    // TODO Auto-generated method stub
    
}
@Override
public void setClientInfo(Properties properties) throws SQLClientInfoException {
    // TODO Auto-generated method stub
    
}
@Override
public String getClientInfo(String name) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Properties getClientInfo() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public void setSchema(String schema) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public String getSchema() throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public void abort(Executor executor) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public int getNetworkTimeout() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/JndiLdapConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */