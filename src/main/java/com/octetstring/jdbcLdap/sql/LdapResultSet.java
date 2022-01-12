/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.jndi.SQLNamingException;
/*     */ import com.octetstring.jdbcLdap.jndi.UnpackResults;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.io.StringReader;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
import java.sql.NClob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
import java.sql.SQLXML;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class LdapResultSet
/*     */   implements ResultSet
/*     */ {
/*     */   ArrayList fields;
/*     */   JndiLdapConnection con;
/*     */   HashMap row;
/*     */   int pos;
/*     */   String baseDN;
/*     */   JdbcLdapStatement statement;
/*     */   int[] types;
/*     */   boolean wasNull;
/*     */   UnpackResults unpack;
/*     */   
/*     */   boolean moveToPos() throws SQLNamingException {
/*  74 */     if (this.pos >= 0 && this.unpack.moveNext(this.pos)) {
/*  75 */       this.row = (HashMap) this.unpack.getRows().get(this.pos);
/*  76 */       return (this.row != null);
/*     */     } 
/*  78 */     this.row = null;
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LdapResultSet(JndiLdapConnection con, JdbcLdapStatement statement, UnpackResults unpack, String baseDN) {
/*  90 */     this.unpack = unpack;
/*  91 */     this.con = con;
/*     */     
/*  93 */     this.baseDN = baseDN;
/*  94 */     this.statement = statement;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     this.pos = -1;
/*     */   }
/*     */   
/*     */   String getByName(String name) throws SQLException {
/* 103 */     if (this.row == null)
/* 104 */       throw new SQLException("Invalid row position"); 
/* 105 */     String val = (String)this.row.get(name);
/* 106 */     if (val == null) {
/* 107 */       this.wasNull = true;
/* 108 */       return "";
/*     */     } 
/* 110 */     this.wasNull = false;
/* 111 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   String getByNum(int id) throws SQLException {
/* 116 */     if (this.row == null) {
/* 117 */       throw new SQLException("Invalid row position");
/*     */     }
/* 119 */     String field = (String) this.unpack.getFieldNames().get(id - 1);
/*     */     
/* 121 */     if (field == null) {
/* 122 */       throw new SQLException("Field " + Integer.toString(id) + " Does not Exist");
/*     */     }
/*     */     
/* 125 */     String val = (String)this.row.get(field);
/* 126 */     if (val == null) {
/* 127 */       this.wasNull = true;
/* 128 */       return "";
/*     */     } 
/* 130 */     this.wasNull = false;
/* 131 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean absolute(int param) throws SQLException {
/* 136 */     this.pos = param;
/* 137 */     return moveToPos();
/*     */   }
/*     */   
/*     */   public void afterLast() throws SQLException {
/* 141 */     this.pos = this.unpack.getRows().size();
/* 142 */     moveToPos();
/*     */   }
/*     */   
/*     */   public void beforeFirst() throws SQLException {
/* 146 */     this.pos = -1;
/* 147 */     moveToPos();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelRowUpdates() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void deleteRow() throws SQLException {
/* 162 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public int findColumn(String str) throws SQLException {
/* 166 */     return this.unpack.getFieldNames().indexOf(str);
/*     */   }
/*     */   
/*     */   public boolean first() throws SQLException {
/* 170 */     this.pos = 0;
/* 171 */     return moveToPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public Array getArray(int param) throws SQLException {
/* 176 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public Array getArray(String str) throws SQLException {
/* 181 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getAsciiStream(int param) throws SQLException {
/* 186 */     return new StringBufferInputStream(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getAsciiStream(String str) throws SQLException {
/* 191 */     return new StringBufferInputStream(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(int param) throws SQLException {
/* 196 */     return new BigDecimal(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(String str) throws SQLException {
/* 201 */     return new BigDecimal(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(int param, int param1) throws SQLException {
/* 206 */     return new BigDecimal(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(String str, int param) throws SQLException {
/* 211 */     return new BigDecimal(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBinaryStream(int param) throws SQLException {
/* 216 */     return new ByteArrayInputStream(getByNum(param).getBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBinaryStream(String str) throws SQLException {
/* 221 */     return new ByteArrayInputStream(getByName(str).getBytes());
/*     */   }
/*     */   
/*     */   public Blob getBlob(int param) throws SQLException {
/* 225 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public Blob getBlob(String str) throws SQLException {
/* 230 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean getBoolean(int param) throws SQLException {
/* 234 */     return Boolean.getBoolean(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String str) throws SQLException {
/* 239 */     return Boolean.getBoolean(getByName(str));
/*     */   }
/*     */   
/*     */   public byte getByte(int param) throws SQLException {
/* 243 */     return Byte.parseByte(getByNum(param));
/*     */   }
/*     */   
/*     */   public byte getByte(String str) throws SQLException {
/* 247 */     return Byte.parseByte(getByName(str));
/*     */   }
/*     */   
/*     */   public byte[] getBytes(int param) throws SQLException {
/* 251 */     return getByNum(param).getBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes(String str) throws SQLException {
/* 256 */     return getByName(str).getBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Reader getCharacterStream(int param) throws SQLException {
/* 261 */     return new StringReader(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public Reader getCharacterStream(String str) throws SQLException {
/* 266 */     return new StringReader(getByName(str));
/*     */   }
/*     */   
/*     */   public Clob getClob(int param) throws SQLException {
/* 270 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public Clob getClob(String str) throws SQLException {
/* 275 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public int getConcurrency() throws SQLException {
/* 279 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public String getCursorName() throws SQLException {
/* 283 */     return "";
/*     */   }
/*     */   
/*     */   public Date getDate(int param) throws SQLException {
/* 287 */     return Date.valueOf(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getDate(String str) throws SQLException {
/* 292 */     return Date.valueOf(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getDate(int param, Calendar calendar) throws SQLException {
/* 297 */     return getDate(param);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate(String str, Calendar calendar) throws SQLException {
/* 305 */     return getDate(str);
/*     */   }
/*     */   
/*     */   public double getDouble(int param) throws SQLException {
/* 309 */     return Double.parseDouble(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(String str) throws SQLException {
/* 314 */     return Double.parseDouble(getByName(str));
/*     */   }
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 318 */     return 1;
/*     */   }
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 322 */     return 1;
/*     */   }
/*     */   
/*     */   public float getFloat(int param) throws SQLException {
/* 326 */     return Float.parseFloat(getByNum(param));
/*     */   }
/*     */   
/*     */   public float getFloat(String str) throws SQLException {
/* 330 */     return Float.parseFloat(getByName(str));
/*     */   }
/*     */   
/*     */   public int getInt(int param) throws SQLException {
/* 334 */     return Integer.parseInt(getByNum(param));
/*     */   }
/*     */   
/*     */   public int getInt(String str) throws SQLException {
/* 338 */     return Integer.parseInt(getByName(str));
/*     */   }
/*     */   
/*     */   public long getLong(int param) throws SQLException {
/* 342 */     return Long.parseLong(getByNum(param));
/*     */   }
/*     */   
/*     */   public long getLong(String str) throws SQLException {
/* 346 */     return Long.parseLong(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSetMetaData getMetaData() throws SQLException {
/* 351 */     return new JdbcLdapMetaData(this.unpack, this.baseDN);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(int param) throws SQLException {
/* 357 */     return getByNum(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(String str) throws SQLException {
/* 362 */     return getByName(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(int param, Map map) throws SQLException {
/* 367 */     return getByNum(param);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(String str, Map map) throws SQLException {
/* 373 */     return getByName(str);
/*     */   }
/*     */   
/*     */   public Ref getRef(int param) throws SQLException {
/* 377 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public Ref getRef(String str) throws SQLException {
/* 382 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public int getRow() throws SQLException {
/* 386 */     return this.pos;
/*     */   }
/*     */   
/*     */   public short getShort(int param) throws SQLException {
/* 390 */     return Short.parseShort(getByNum(param));
/*     */   }
/*     */   
/*     */   public short getShort(String str) throws SQLException {
/* 394 */     return Short.parseShort(getByName(str));
/*     */   }
/*     */   
/*     */   public Statement getStatement() throws SQLException {
/* 398 */     return this.statement;
/*     */   }
/*     */   
/*     */   public String getString(int param) throws SQLException {
/* 402 */     return getByNum(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String str) throws SQLException {
/* 407 */     return getByName(str);
/*     */   }
/*     */   
/*     */   public Time getTime(int param) throws SQLException {
/* 411 */     return Time.valueOf(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public Time getTime(String str) throws SQLException {
/* 416 */     return Time.valueOf(getByName(str));
/*     */   }
/*     */ 
/*     */   
/*     */   public Time getTime(int param, Calendar calendar) throws SQLException {
/* 421 */     return getTime(param);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Time getTime(String str, Calendar calendar) throws SQLException {
/* 428 */     return getTime(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(int param) throws SQLException {
/* 433 */     return Timestamp.valueOf(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(String str) throws SQLException {
/* 438 */     return Timestamp.valueOf(getByName(str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(int param, Calendar calendar) throws SQLException {
/* 445 */     return getTimestamp(param);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(String str, Calendar calendar) throws SQLException {
/* 452 */     return getTimestamp(str);
/*     */   }
/*     */   
/*     */   public int getType() throws SQLException {
/* 456 */     return 0;
/*     */   }
/*     */   
/*     */   public URL getURL(int param) throws SQLException {
/*     */     try {
/* 461 */       return new URL(getByNum(param));
/* 462 */     } catch (Exception e) {
/* 463 */       throw new SQLException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURL(String str) throws SQLException {
/*     */     try {
/* 470 */       return new URL(getByName(str));
/* 471 */     } catch (Exception e) {
/* 472 */       throw new SQLException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getUnicodeStream(int param) throws SQLException {
/* 478 */     return new StringBufferInputStream(getByNum(param));
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getUnicodeStream(String str) throws SQLException {
/* 483 */     return new StringBufferInputStream(getByName(str));
/*     */   }
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 487 */     return null;
/*     */   }
/*     */   
/*     */   public void insertRow() throws SQLException {
/* 491 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean isAfterLast() throws SQLException {
/* 495 */     return (this.pos > this.unpack.getRows().size() - 1);
/*     */   }
/*     */   
/*     */   public boolean isBeforeFirst() throws SQLException {
/* 499 */     return (this.pos == -1);
/*     */   }
/*     */   
/*     */   public boolean isFirst() throws SQLException {
/* 503 */     return (this.pos == 0);
/*     */   }
/*     */   
/*     */   public boolean isLast() throws SQLException {
/* 507 */     return (this.pos == this.unpack.getRows().size() - 1);
/*     */   }
/*     */   
/*     */   public boolean last() throws SQLException {
/* 511 */     this.pos = this.unpack.getRows().size() - 1;
/* 512 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveToCurrentRow() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void moveToInsertRow() throws SQLException {
/* 520 */     throw new SQLException("Not Implemented");
/*     */   }
/*     */   
/*     */   public boolean next() throws SQLException {
/* 524 */     this.pos++;
/* 525 */     return moveToPos();
/*     */   }
/*     */   
/*     */   public boolean previous() throws SQLException {
/* 529 */     this.pos--;
/* 530 */     return moveToPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshRow() throws SQLException {}
/*     */ 
/*     */   
/*     */   public boolean relative(int param) throws SQLException {
/* 538 */     this.pos += param;
/* 539 */     return moveToPos();
/*     */   }
/*     */   
/*     */   public boolean rowDeleted() throws SQLException {
/* 543 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean rowInserted() throws SQLException {
/* 547 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean rowUpdated() throws SQLException {
/* 551 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int param) throws SQLException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFetchSize(int param) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void updateArray(int param, Array array) throws SQLException {
/* 564 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateArray(String str, Array array) throws SQLException {
/* 569 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(int param, InputStream inputStream, int param2) throws SQLException {
/* 577 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(String str, InputStream inputStream, int param) throws SQLException {
/* 585 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBigDecimal(int param, BigDecimal bigDecimal) throws SQLException {
/* 590 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
/* 597 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(int param, InputStream inputStream, int param2) throws SQLException {
/* 605 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(String str, InputStream inputStream, int param) throws SQLException {
/* 613 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlob(int param, Blob blob) throws SQLException {
/* 618 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlob(String str, Blob blob) throws SQLException {
/* 623 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBoolean(int param, boolean param1) throws SQLException {
/* 628 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBoolean(String str, boolean param) throws SQLException {
/* 633 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateByte(int param, byte param1) throws SQLException {
/* 638 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateByte(String str, byte param) throws SQLException {
/* 643 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBytes(int param, byte[] values) throws SQLException {
/* 648 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBytes(String str, byte[] values) throws SQLException {
/* 653 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(int param, Reader reader, int param2) throws SQLException {
/* 661 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(String str, Reader reader, int param) throws SQLException {
/* 669 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(int param, Clob clob) throws SQLException {
/* 674 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(String str, Clob clob) throws SQLException {
/* 679 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDate(int param, Date date) throws SQLException {
/* 684 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDate(String str, Date date) throws SQLException {
/* 689 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDouble(int param, double param1) throws SQLException {
/* 694 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDouble(String str, double param) throws SQLException {
/* 699 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateFloat(int param, float param1) throws SQLException {
/* 704 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateFloat(String str, float param) throws SQLException {
/* 709 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public void updateInt(int param, int param1) throws SQLException {
/* 713 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateInt(String str, int param) throws SQLException {
/* 718 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLong(int param, long param1) throws SQLException {
/* 723 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLong(String str, long param) throws SQLException {
/* 728 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public void updateNull(int param) throws SQLException {
/* 732 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public void updateNull(String str) throws SQLException {
/* 736 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateObject(int param, Object obj) throws SQLException {
/* 741 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateObject(String str, Object obj) throws SQLException {
/* 746 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateObject(int param, Object obj, int param2) throws SQLException {
/* 751 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateObject(String str, Object obj, int param) throws SQLException {
/* 759 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRef(int param, Ref ref) throws SQLException {
/* 764 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRef(String str, Ref ref) throws SQLException {
/* 769 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public void updateRow() throws SQLException {
/* 773 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateShort(int param, short param1) throws SQLException {
/* 778 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateShort(String str, short param) throws SQLException {
/* 783 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateString(int param, String str) throws SQLException {
/* 788 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateString(String str, String str1) throws SQLException {
/* 793 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTime(int param, Time time) throws SQLException {
/* 798 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTime(String str, Time time) throws SQLException {
/* 803 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTimestamp(int param, Timestamp timestamp) throws SQLException {
/* 808 */     throw new SQLException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
/* 815 */     throw new SQLException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean wasNull() throws SQLException {
/* 819 */     return this.wasNull;
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


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/LdapResultSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */