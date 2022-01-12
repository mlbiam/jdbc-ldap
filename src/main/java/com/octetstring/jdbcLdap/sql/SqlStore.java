/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SqlStore
/*     */ {
/*     */   String sql;
/*     */   String where;
/*     */   String from;
/*     */   boolean simple;
/*     */   String[] fields;
/*     */   boolean getDN;
/*     */   int scope;
/*     */   int numArgs;
/*     */   String[] insertFields;
/*     */   String dn;
/*     */   int[] fieldOffset;
/*     */   int border;
/*     */   LinkedList fieldsMap;
/*     */   String[] dnfields;
/*     */   String command;
/*     */   LinkedList attribs;
/*     */   LinkedList cmds;
/*     */   ArrayList offsetList;
/*     */   String[] orderby;
/*     */   HashMap fieldMap;
/*     */   HashMap revFieldMap;
/*     */   private Set dontAdd;
/*     */   private String defOC;
/*     */   
/*     */   public String[] getOrderby() {
/*  97 */     return this.orderby;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrderby(String[] orderby) {
/* 103 */     this.orderby = orderby;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStore(String sql) {
/* 110 */     this.sql = sql;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWhere(String where) {
/* 119 */     this.where = where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWhere() {
/* 127 */     return this.where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String from) {
/* 135 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFrom() {
/* 143 */     return this.from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(String[] fields) {
/* 151 */     this.fields = fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getFields() {
/* 159 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDN(boolean dn) {
/* 167 */     this.getDN = dn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDN() {
/* 175 */     return this.getDN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDistinguishedName(String dn) {
/* 183 */     this.dn = dn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDistinguishedName() {
/* 191 */     return this.dn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(int scope) {
/* 199 */     this.scope = scope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScope() {
/* 207 */     return this.scope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgs(int args) {
/* 215 */     this.numArgs = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArgs() {
/* 223 */     return this.numArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInsertFields(String[] fields) {
/* 231 */     this.insertFields = fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getInsertFields() {
/* 239 */     return this.insertFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getFieldOffset() {
/* 247 */     return this.fieldOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldOffset(int[] offset) {
/* 255 */     this.fieldOffset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimple(boolean simple) {
/* 263 */     this.simple = simple;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getSimple() {
/* 271 */     return this.simple;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorder(int border) {
/* 279 */     this.border = border;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBorder() {
/* 287 */     return this.border;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSQL() {
/* 295 */     return this.sql;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldsMap(LinkedList fieldsMap) {
/* 303 */     this.fieldsMap = fieldsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList getFieldsMap() {
/* 311 */     return this.fieldsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDnFields(String[] dnFields) {
/* 319 */     this.dnfields = dnFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDnFields() {
/* 327 */     return this.dnfields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommand() {
/* 335 */     return this.command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String cmd) {
/* 343 */     this.command = cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList getAttribs() {
/* 350 */     return this.attribs;
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
/*     */   public void setAttribs(LinkedList list) {
/* 363 */     this.attribs = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList getCmds() {
/* 373 */     return this.cmds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCmds(LinkedList list) {
/* 380 */     this.cmds = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList getOffsetList() {
/* 387 */     return this.offsetList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffsetList(ArrayList offsetList) {
/* 394 */     this.offsetList = offsetList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap getFieldMap() {
/* 401 */     return this.fieldMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldMap(HashMap fieldMap) {
/* 407 */     this.fieldMap = fieldMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap getRevFieldMap() {
/* 413 */     return this.revFieldMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRevFieldMap(HashMap revFieldMap) {
/* 419 */     this.revFieldMap = revFieldMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDontAdd(Set set) {
/* 425 */     this.dontAdd = set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getDontAdd() {
/* 432 */     return this.dontAdd;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultOC(String string) {
/* 438 */     this.defOC = string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefOC() {
/* 445 */     return this.defOC;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/SqlStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */