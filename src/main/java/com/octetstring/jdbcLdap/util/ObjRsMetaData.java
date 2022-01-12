/*    */ package com.octetstring.jdbcLdap.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjRsMetaData
/*    */   extends RSMetaData
/*    */   implements Serializable
/*    */ {
/*    */   String[] fieldNames;
/*    */   
/*    */   public ObjRsMetaData(String[] fieldNames) {
/* 17 */     this.fieldNames = fieldNames;
/*    */   }
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
/*    */   public int getColumnCount() throws SQLException {
/* 31 */     return (this.fieldNames != null) ? this.fieldNames.length : 0;
/*    */   }
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
/*    */   public String getColumnName(int column) throws SQLException {
/* 46 */     return this.fieldNames[column - 1];
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/ObjRsMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */