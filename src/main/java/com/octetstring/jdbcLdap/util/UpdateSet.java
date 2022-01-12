/*    */ package com.octetstring.jdbcLdap.util;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class UpdateSet
/*    */ {
/*    */   String cmd;
/*    */   ArrayList attribs;
/*    */   
/*    */   public UpdateSet(String cmd, ArrayList attribs) {
/* 27 */     this.cmd = cmd;
/* 28 */     this.attribs = attribs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ArrayList getAttribs() {
/* 35 */     return this.attribs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCmd() {
/* 42 */     return this.cmd;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttribs(ArrayList list) {
/* 49 */     this.attribs = list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCmd(String i) {
/* 56 */     this.cmd = i;
/*    */   }
/*    */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/UpdateSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */