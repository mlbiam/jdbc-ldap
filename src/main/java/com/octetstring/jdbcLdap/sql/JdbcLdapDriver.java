/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdbcLdapDriver
/*     */   implements Driver
/*     */ {
/*     */   public static final String URL_ID = "jdbc:ldap";
/*     */   public static final String DSML_URL_ID = "jdbc:dsml";
/*     */   public static final String SPML_URL_ID = "jdbc:spml";
/*     */   public static final int MAJOR_VERSION = 0;
/*     */   public static final int MINOR_VERSION = 99;
/*     */   public static final boolean JDBC_IV = false;
/*     */   public static final String PARAM_DELIM = ":=";
/*     */   
/*     */   static {
/*     */     try {
/*  56 */       DriverManager.registerDriver(new JdbcLdapDriver());
/*     */     }
/*  58 */     catch (SQLException e) {
/*     */       
/*  60 */       e.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbcLdapDriver() throws SQLException {
/*  67 */     DriverManager.registerDriver(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptsURL(String str) throws SQLException {
/*  75 */     return (str.substring(0, 9).equalsIgnoreCase("jdbc:ldap") || str.substring(0, 9).equalsIgnoreCase("jdbc:dsml") || str.substring(0, 9).equalsIgnoreCase("jdbc:spml"));
/*     */   }
/*     */   
/*     */   public Connection connect(String str, Properties properties) throws SQLException {
/*  79 */     if (!acceptsURL(str))
/*     */     {
/*  81 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     int seperator = str.indexOf("?");
/*  87 */     if (seperator != -1) {
/*  88 */       String props = str.substring(seperator + 1);
/*     */       
/*  90 */       StringTokenizer toker = new StringTokenizer(props, "&", false);
/*  91 */       while (toker.hasMoreTokens()) {
/*  92 */         String token = toker.nextToken();
/*  93 */         String prop = token.substring(0, token.indexOf(":="));
/*  94 */         String val = token.substring(token.indexOf(":=") + ":=".length());
/*  95 */         properties.setProperty(prop, val);
/*     */       } 
/*     */ 
/*     */       
/*  99 */       return (Connection)new JndiLdapConnection(str.substring(0, seperator), properties);
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return (Connection)new JndiLdapConnection(str, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 108 */     return 0;
/*     */   }
/*     */   
/*     */   public int getMinorVersion() {
/* 112 */     return 99;
/*     */   }
/*     */   
/*     */   public DriverPropertyInfo[] getPropertyInfo(String str, Properties properties) throws SQLException {
/* 116 */     DriverPropertyInfo[] props = new DriverPropertyInfo[5];
/* 117 */     props[0] = new DriverPropertyInfo("user", "Security Principal");
/* 118 */     props[1] = new DriverPropertyInfo("password", "Security Credentials");
/* 119 */     props[2] = new DriverPropertyInfo("java.naming.security.authentication", "Authentication type - simple, none or SASL type");
/* 120 */     props[3] = new DriverPropertyInfo("SEARCH_SCOPE", "The Search scope");
/* 121 */     props[4] = new DriverPropertyInfo("CACHE_STATEMENT", "true or false, wether or not statements should be cached");
/*     */     
/* 123 */     return props;
/*     */   }
/*     */   
/*     */   public boolean jdbcCompliant() {
/* 127 */     return false;
/*     */   }
/*     */
@Override
public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    // TODO Auto-generated method stub
    return null;
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/JdbcLdapDriver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */