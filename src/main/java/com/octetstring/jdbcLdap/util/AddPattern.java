/*    */ package com.octetstring.jdbcLdap.util;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddPattern
/*    */ {
/*    */   String addPattern;
/*    */   Set notToAdd;
/*    */   String defaultOC;
/*    */   
/*    */   public AddPattern(String addPattern, Set notToAdd, String defaultOC) {
/* 25 */     this.addPattern = addPattern;
/* 26 */     this.defaultOC = defaultOC;
/* 27 */     this.notToAdd = notToAdd;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAddPattern() {
/* 35 */     return this.addPattern;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDefaultOC() {
/* 41 */     return this.defaultOC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set getNotToAdd() {
/* 47 */     return this.notToAdd;
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/AddPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */