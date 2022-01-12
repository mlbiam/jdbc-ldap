/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import java.sql.Date;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldStore
/*     */ {
/*     */   static final String DECIMAL = ".";
/*     */   static final String DASH = "-";
/*     */   static final String COLON = ":";
/*     */   String name;
/*     */   int numVals;
/*     */   int type;
/*     */   boolean determined;
/*     */   
/*     */   public FieldStore() {
/*  40 */     this.numVals = 0;
/*  41 */     this.determined = false;
/*     */   }
/*     */   
/*     */   public FieldStore(String name, int vals) {
/*  45 */     this.name = name;
/*  46 */     this.numVals = vals;
/*  47 */     this.determined = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  54 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumVals() {
/*  61 */     return this.numVals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/*  69 */     if (this.determined) {
/*  70 */       return this.type;
/*     */     }
/*     */     
/*  73 */     return 12;
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
/*     */   
/*     */   public void determineType(String val) {
/*  88 */     if (this.determined || val == null || val.length() == 0) {
/*     */       return;
/*     */     }
/*  91 */     if (Character.isDigit(val.charAt(0))) {
/*     */       
/*  93 */       mightNumeric(val);
/*     */     }
/*     */     else {
/*     */       
/*  97 */       this.type = 12;
/*  98 */       this.determined = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mightNumeric(String val) {
/* 106 */     int pos1 = val.indexOf("-");
/* 107 */     int pos2 = val.indexOf(":");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (pos1 != -1 || pos2 != -1) {
/* 113 */       mightDateTime(val, pos1, pos2);
/*     */     
/*     */     }
/* 116 */     else if (val.indexOf(".") != -1) {
/*     */       
/*     */       try {
/* 119 */         Double.valueOf(val);
/* 120 */         this.type = 8;
/* 121 */         this.determined = true;
/*     */       }
/* 123 */       catch (Exception e) {
/*     */         
/* 125 */         this.type = 12;
/* 126 */         this.determined = true;
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 133 */         long ltmp = Long.parseLong(val);
/* 134 */         this.type = 4;
/* 135 */         this.determined = true;
/*     */       }
/* 137 */       catch (Exception e) {
/*     */         
/* 139 */         this.type = 12;
/* 140 */         this.determined = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mightDateTime(String val, int pos1, int pos2) {
/* 149 */     if (pos1 != -1 && pos2 != -1 && pos1 < pos2) {
/*     */       
/*     */       try {
/* 152 */         Timestamp.valueOf(val);
/* 153 */         this.type = 93;
/* 154 */         this.determined = true;
/*     */       }
/* 156 */       catch (IllegalArgumentException e) {
/*     */         
/* 158 */         this.type = 12;
/* 159 */         this.determined = true;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 164 */     else if (pos1 != -1 || pos2 != -1) {
/* 165 */       if (pos1 == -1) {
/*     */         
/*     */         try {
/* 168 */           Time.valueOf(val);
/* 169 */           this.type = 92;
/* 170 */           this.determined = true;
/*     */         }
/* 172 */         catch (IllegalArgumentException e) {
/*     */           
/* 174 */           this.type = 12;
/* 175 */           this.determined = true;
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 181 */           Date.valueOf(val);
/* 182 */           this.type = 91;
/* 183 */           this.determined = true;
/*     */         }
/* 185 */         catch (IllegalArgumentException e) {
/*     */           
/* 187 */           this.type = 12;
/* 188 */           this.determined = true;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 193 */       this.type = 12;
/* 194 */       this.determined = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/FieldStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */