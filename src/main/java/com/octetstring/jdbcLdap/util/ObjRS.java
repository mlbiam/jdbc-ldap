/*      */ package com.octetstring.jdbcLdap.util;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
import java.sql.NClob;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
import java.sql.SQLXML;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjRS
/*      */   implements ResultSet, Serializable
/*      */ {
/*      */   public static final transient int IT_ITERATOR = 0;
/*      */   public static final transient int IT_ARRAY = 1;
/*      */   public static final transient int IT_SINGLE = 2;
/*      */   public static final transient int EXT_REF = 0;
/*      */   public static final transient int EXT_HASH = 1;
/*      */   public static final transient int EXT_ARRAY = 2;
/*      */   HashMap nameMap;
/*      */   Collection c;
/*      */   Iterator it;
/*      */   HashMap props;
/*      */   boolean isFirst;
/*      */   int itType;
/*      */   int extType;
/*      */   Object[] metaData;
/*      */   Object o;
/*      */   Object[] lines;
/*      */   String[][] table;
/*      */   String[] fieldNames;
/*      */   int curr;
/*      */   boolean wasNull;
/*      */   
/*      */   private void refGetFields(Object o) {
/*   69 */     LinkedList<String> l = new LinkedList();
/*   70 */     this.nameMap = new HashMap<Object, Object>();
/*      */ 
/*      */     
/*   73 */     Method[] meths = o.getClass().getMethods();
/*   74 */     for (int i = 0, m = meths.length; i < m; i++) {
/*   75 */       String name = meths[i].getName();
/*   76 */       if (name.substring(0, 3).equals("get") && (meths[i].getParameterTypes()).length == 0 && !name.equals("getClass")) {
/*   77 */         name = name.substring(3);
/*   78 */         this.nameMap.put(name.toLowerCase(), meths[i]);
/*   79 */         l.add(name);
/*      */       } 
/*      */     } 
/*      */     
/*   83 */     this.fieldNames = new String[l.size()];
/*      */     
/*   85 */     l.toArray(this.fieldNames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void hashGetFields(Object o) {
/*   92 */     LinkedList<String> l = new LinkedList();
/*      */     
/*   94 */     Iterator it = ((Map)o).keySet().iterator();
/*   95 */     while (it.hasNext()) {
/*   96 */       l.add(it.next().toString().toLowerCase());
/*      */     }
/*      */     
/*   99 */     this.fieldNames = new String[l.size()];
/*  100 */     l.toArray(this.fieldNames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjRS(Collection c) {
/*  108 */     if (c == null) {
/*  109 */       this.wasNull = true;
/*  110 */       this.fieldNames = new String[0];
/*      */       return;
/*      */     } 
/*  113 */     this.wasNull = false;
/*  114 */     this.c = c;
/*  115 */     this.it = c.iterator();
/*  116 */     this.isFirst = true;
/*  117 */     this.itType = 0;
/*      */     
/*  119 */     if (this.it.hasNext()) {
/*  120 */       this.o = this.it.next();
/*  121 */       if (this.o instanceof Map) {
/*  122 */         this.extType = 1;
/*  123 */         hashGetFields(this.o);
/*      */       } else {
/*      */         
/*  126 */         this.extType = 0;
/*  127 */         refGetFields(this.o);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public ObjRS(Object[] ar) {
/*  133 */     if (ar == null) {
/*  134 */       this.wasNull = true;
/*  135 */       this.fieldNames = new String[0];
/*      */       return;
/*      */     } 
/*  138 */     this.wasNull = false;
/*  139 */     this.lines = ar;
/*  140 */     this.itType = 1;
/*  141 */     this.isFirst = true;
/*      */     
/*  143 */     this.o = this.lines[0];
/*  144 */     if (this.o instanceof Map) {
/*  145 */       this.extType = 1;
/*  146 */       hashGetFields(this.o);
/*      */     } else {
/*      */       
/*  149 */       this.extType = 0;
/*  150 */       refGetFields(this.o);
/*      */     } 
/*      */     
/*  153 */     this.curr = 0;
/*      */   }
/*      */   
/*      */   public ObjRS(String[][] ar, String[] fieldNames) {
/*  157 */     this.table = ar;
/*  158 */     this.itType = 1;
/*  159 */     this.extType = 2;
/*  160 */     this.isFirst = true;
/*  161 */     this.fieldNames = fieldNames;
/*      */     
/*  163 */     this.nameMap = new HashMap<Object, Object>();
/*  164 */     for (int i = 0, m = fieldNames.length; i < m; i++) {
/*  165 */       this.nameMap.put(fieldNames[i], new Integer(i));
/*      */     }
/*      */     
/*  168 */     this.curr = 0;
/*      */   }
/*      */   
/*      */   public ObjRS(Object o) {
/*  172 */     if (o == null) {
/*  173 */       this.wasNull = true;
/*  174 */       this.fieldNames = new String[0];
/*      */       return;
/*      */     } 
/*  177 */     this.wasNull = false;
/*  178 */     this.o = o;
/*  179 */     if (o instanceof Map) {
/*  180 */       this.extType = 1;
/*  181 */       hashGetFields(o);
/*      */     } else {
/*      */       
/*  184 */       this.extType = 0;
/*  185 */       refGetFields(o);
/*      */     } 
/*  187 */     this.isFirst = true;
/*  188 */     this.itType = 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean next() throws SQLException {
/*  197 */     if (this.wasNull) {
/*  198 */       return false;
/*      */     }
/*  200 */     switch (this.itType) { case 0:
/*  201 */         if (this.isFirst) {
/*  202 */           this.isFirst = false;
/*  203 */           if (this.o == null) {
/*  204 */             return false;
/*      */           }
/*      */           
/*  207 */           return true;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  212 */         if (this.it.hasNext()) {
/*  213 */           this.o = this.it.next();
/*  214 */           return true;
/*      */         } 
/*      */         
/*  217 */         return false;
/*      */ 
/*      */       
/*      */       case 1:
/*  221 */         if (this.lines != null) {
/*  222 */           if (this.isFirst) {
/*  223 */             this.isFirst = false;
/*  224 */             if (this.lines != null) {
/*  225 */               return (this.lines.length != 0);
/*      */             }
/*      */             
/*  228 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*  232 */           if (this.curr < this.lines.length - 1) {
/*  233 */             this.curr++;
/*  234 */             this.o = this.lines[this.curr];
/*  235 */             return true;
/*      */           } 
/*      */           
/*  238 */           return false;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  243 */         if (this.isFirst) {
/*  244 */           this.isFirst = false;
/*  245 */           if (this.table != null) {
/*  246 */             return (this.table.length != 0);
/*      */           }
/*      */           
/*  249 */           return false;
/*      */         } 
/*      */ 
/*      */         
/*  253 */         if (this.curr < this.table.length - 1) {
/*  254 */           this.curr++;
/*  255 */           return true;
/*      */         } 
/*      */         
/*  258 */         return false;
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*  263 */         if (this.isFirst) {
/*  264 */           this.isFirst = false;
/*  265 */           return true;
/*      */         } 
/*      */         
/*  268 */         return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  274 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String columnName) throws SQLException {
/*      */     Object val;
/*  281 */     if (this.wasNull) {
/*  282 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  286 */     switch (this.extType) {
/*      */       
/*      */       case 0:
/*      */         
/*      */         try {
/*  291 */           Object tmp = ((Method)this.nameMap.get(columnName)).invoke(this.o, new Object[0]);
/*  292 */           if (tmp != null) {
/*  293 */             return tmp.toString();
/*      */           }
/*      */           
/*  296 */           return "";
/*      */ 
/*      */         
/*      */         }
/*  300 */         catch (Exception e) {
/*      */           
/*  302 */           throw new SQLException(e.toString());
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*  308 */         val = ((Map)this.o).get(columnName);
/*  309 */         if (val == null) {
/*  310 */           return null;
/*      */         }
/*  312 */         return val.toString();
/*      */       
/*      */       case 2:
/*  315 */         return this.table[this.curr][((Integer)this.nameMap.get(columnName)).intValue()];
/*      */     } 
/*      */     
/*  318 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/*  331 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(int columnIndex) throws SQLException {
/*  338 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int columnIndex) throws SQLException {
/*  345 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(int columnIndex) throws SQLException {
/*  352 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int columnIndex) throws SQLException {
/*  359 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int columnIndex) throws SQLException {
/*  366 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(int columnIndex) throws SQLException {
/*  373 */     return 0L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int columnIndex) throws SQLException {
/*  380 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(int columnIndex) throws SQLException {
/*  387 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
/*  396 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int columnIndex) throws SQLException {
/*  403 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int columnIndex) throws SQLException {
/*  410 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int columnIndex) throws SQLException {
/*  417 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int columnIndex) throws SQLException {
/*  424 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getAsciiStream(int columnIndex) throws SQLException {
/*  431 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getUnicodeStream(int columnIndex) throws SQLException {
/*  439 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(int columnIndex) throws SQLException {
/*  446 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String columnName) throws SQLException {
/*  454 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String columnName) throws SQLException {
/*  461 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String columnName) throws SQLException {
/*  468 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String columnName) throws SQLException {
/*  475 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String columnName) throws SQLException {
/*  482 */     return 0L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String columnName) throws SQLException {
/*  489 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String columnName) throws SQLException {
/*  496 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
/*  505 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(String columnName) throws SQLException {
/*  512 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String columnName) throws SQLException {
/*  519 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String columnName) throws SQLException {
/*  526 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String columnName) throws SQLException {
/*  533 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getAsciiStream(String columnName) throws SQLException {
/*  540 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getUnicodeStream(String columnName) throws SQLException {
/*  549 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(String columnName) throws SQLException {
/*  556 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  563 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCursorName() throws SQLException {
/*  576 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*  583 */     return new ObjRsMetaData(this.fieldNames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(int columnIndex) throws SQLException {
/*  590 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String columnName) throws SQLException {
/*  597 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int findColumn(String columnName) throws SQLException {
/*  604 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(int columnIndex) throws SQLException {
/*  611 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(String columnName) throws SQLException {
/*  618 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
/*  625 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String columnName) throws SQLException {
/*  632 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBeforeFirst() throws SQLException {
/*  639 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAfterLast() throws SQLException {
/*  646 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFirst() throws SQLException {
/*  653 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLast() throws SQLException {
/*  660 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beforeFirst() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void afterLast() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean first() throws SQLException {
/*  679 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean last() throws SQLException {
/*  686 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRow() throws SQLException {
/*  693 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean absolute(int row) throws SQLException {
/*  700 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean relative(int rows) throws SQLException {
/*  707 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean previous() throws SQLException {
/*  714 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFetchDirection(int direction) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  727 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFetchSize(int rows) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFetchSize() throws SQLException {
/*  740 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getType() throws SQLException {
/*  747 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getConcurrency() throws SQLException {
/*  754 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowUpdated() throws SQLException {
/*  761 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowInserted() throws SQLException {
/*  768 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowDeleted() throws SQLException {
/*  775 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateNull(int columnIndex) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBoolean(int columnIndex, boolean x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateByte(int columnIndex, byte x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateShort(int columnIndex, short x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateInt(int columnIndex, int x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateLong(int columnIndex, long x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFloat(int columnIndex, float x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDouble(int columnIndex, double x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateString(int columnIndex, String x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBytes(int columnIndex, byte[] x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDate(int columnIndex, Date x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateTime(int columnIndex, Time x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateObject(int columnIndex, Object x, int scale) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateObject(int columnIndex, Object x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateNull(String columnName) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBoolean(String columnName, boolean x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateByte(String columnName, byte x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateShort(String columnName, short x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateInt(String columnName, int x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateLong(String columnName, long x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFloat(String columnName, float x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDouble(String columnName, double x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateString(String columnName, String x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBytes(String columnName, byte[] x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDate(String columnName, Date x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateTime(String columnName, Time x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateTimestamp(String columnName, Timestamp x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateObject(String columnName, Object x, int scale) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateObject(String columnName, Object x) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void refreshRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cancelRowUpdates() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveToInsertRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveToCurrentRow() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement getStatement() throws SQLException {
/* 1071 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(int i, Map map) throws SQLException {
/* 1078 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ref getRef(int i) throws SQLException {
/* 1085 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Blob getBlob(int i) throws SQLException {
/* 1092 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Clob getClob(int i) throws SQLException {
/* 1099 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Array getArray(int i) throws SQLException {
/* 1106 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String colName, Map map) throws SQLException {
/* 1113 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ref getRef(String colName) throws SQLException {
/* 1120 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Blob getBlob(String colName) throws SQLException {
/* 1127 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Clob getClob(String colName) throws SQLException {
/* 1134 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Array getArray(String colName) throws SQLException {
/* 1141 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int columnIndex, Calendar cal) throws SQLException {
/* 1148 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String columnName, Calendar cal) throws SQLException {
/* 1155 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int columnIndex, Calendar cal) throws SQLException {
/* 1162 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String columnName, Calendar cal) throws SQLException {
/* 1169 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
/* 1177 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
/* 1185 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(int columnIndex) throws SQLException {
/* 1192 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(String columnName) throws SQLException {
/* 1199 */     return null;
/*      */   }
/*      */   
/*      */   public void updateRef(int columnIndex, Ref x) throws SQLException {}
/*      */   
/*      */   public void updateRef(String columnName, Ref x) throws SQLException {}
/*      */   
/*      */   public void updateBlob(int columnIndex, Blob x) throws SQLException {}
/*      */   
/*      */   public void updateBlob(String columnName, Blob x) throws SQLException {}
/*      */   
/*      */   public void updateClob(int columnIndex, Clob x) throws SQLException {}
/*      */   
/*      */   public void updateClob(String columnName, Clob x) throws SQLException {}
/*      */   
/*      */   public void updateArray(int columnIndex, Array x) throws SQLException {}
/*      */   
/*      */   public void updateArray(String columnName, Array x) throws SQLException {}
/*      */
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
public RowId getRowId(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public RowId getRowId(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public void updateRowId(int columnIndex, RowId x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateRowId(String columnLabel, RowId x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public int getHoldability() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
}
@Override
public boolean isClosed() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void updateNString(int columnIndex, String nString) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNString(String columnLabel, String nString) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public NClob getNClob(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public NClob getNClob(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public SQLXML getSQLXML(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public SQLXML getSQLXML(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public String getNString(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public String getNString(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Reader getNCharacterStream(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public Reader getNCharacterStream(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateClob(int columnIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateClob(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    // TODO Auto-generated method stub
    return null;
}
@Override
public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    // TODO Auto-generated method stub
    return null;
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/ObjRS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */