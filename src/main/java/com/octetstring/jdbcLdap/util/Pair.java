/*    */ package com.octetstring.jdbcLdap.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Pair
/*    */ {
/*    */   String name;
/*    */   String value;
/*    */   String nameUcase;
/*    */   
/*    */   public Pair(String name, String value) {
/* 27 */     this.name = name;
/* 28 */     this.nameUcase = name.toUpperCase();
/* 29 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getNameUpperCase() {
/* 33 */     return this.nameUcase;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 48 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String string) {
/* 55 */     this.name = string;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(String string) {
/* 62 */     this.value = string;
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/Pair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */