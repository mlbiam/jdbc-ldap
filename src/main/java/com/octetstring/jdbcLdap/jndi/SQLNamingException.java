/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import com.novell.ldap.LDAPException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.MalformedURLException;
/*     */ import java.sql.SQLException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLNamingException
/*     */   extends SQLException
/*     */ {
/*     */   Exception e;
/*     */   
/*     */   public SQLNamingException(NamingException e) {
/*  43 */     this.e = e;
/*     */   }
/*     */   
/*     */   public SQLNamingException(LDAPException e) {
/*  47 */     this.e = (Exception)e;
/*     */   }
/*     */   
/*     */   public SQLNamingException(MalformedURLException e) {
/*  51 */     this.e = e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLNamingException(Exception e1) {
/*  59 */     this.e = e1;
/*     */   }
/*     */   
/*     */   protected Object clone() throws CloneNotSupportedException {
/*  63 */     return super.clone();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*  67 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable fillInStackTrace() {
/*  77 */     return super.fillInStackTrace();
/*     */   }
/*     */   
/*     */   public Throwable getCause() {
/*  81 */     return this.e.getCause();
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/*  85 */     return this.e.getLocalizedMessage();
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  89 */     return this.e.getMessage();
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStackTrace() {
/*  93 */     return this.e.getStackTrace();
/*     */   }
/*     */   
/*     */   public Throwable initCause(Throwable throwable) {
/*  97 */     return this.e.initCause(throwable);
/*     */   }
/*     */   
/*     */   public void printStackTrace() {
/* 101 */     this.e.printStackTrace();
/*     */   }
/*     */   
/*     */   public void printStackTrace(PrintStream printStream) {
/* 105 */     this.e.printStackTrace(printStream);
/*     */   }
/*     */   
/*     */   public void printStackTrace(PrintWriter printWriter) {
/* 109 */     this.e.printStackTrace(printWriter);
/*     */   }
/*     */   
/*     */   public void setStackTrace(StackTraceElement[] stackTraceElement) {
/* 113 */     this.e.setStackTrace(stackTraceElement);
/*     */   }
/*     */   
/*     */   public int getErrorCode() {
/* 117 */     if (this.e instanceof LDAPException) {
/* 118 */       return ((LDAPException)this.e).getResultCode();
/*     */     }
/* 120 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLException getNextException() {
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public String getSQLState() {
/* 129 */     return "ERROR";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNextException(SQLException sQLException) {}
/*     */ 
/*     */   
/*     */   public Exception getNamingException() {
/* 137 */     return this.e;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/SQLNamingException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */