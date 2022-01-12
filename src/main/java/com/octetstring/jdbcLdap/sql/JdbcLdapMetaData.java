/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import com.octetstring.jdbcLdap.jndi.UnpackResults;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
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
/*     */ public class JdbcLdapMetaData
/*     */   implements ResultSetMetaData
/*     */ {
/*     */   String baseDN;
/*     */   private UnpackResults unpack;
/*     */   
/*     */   public JdbcLdapMetaData(UnpackResults unpack, String baseDN) {
/*  44 */     this.unpack = unpack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCatalogName(int param) throws SQLException {
/*  50 */     return (String) this.unpack.getFieldNames().get(param);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnClassName(int param) throws SQLException {
/*  55 */     return "java.lang.String";
/*     */   }
/*     */   
/*     */   public int getColumnCount() throws SQLException {
/*  59 */     return this.unpack.getFieldNames().size();
/*     */   }
/*     */   
/*     */   public int getColumnDisplaySize(int param) throws SQLException {
/*  63 */     return -1;
/*     */   }
/*     */   
/*     */   public String getColumnLabel(int param) throws SQLException {
/*  67 */     return getCatalogName(param - 1);
/*     */   }
/*     */   
/*     */   public String getColumnName(int param) throws SQLException {
/*  71 */     return getCatalogName(param - 1);
/*     */   }
/*     */   
/*     */   public int getColumnType(int param) throws SQLException {
/*  75 */     return ((Integer)this.unpack.getFieldTypes().get(param - 1)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnTypeName(int param) throws SQLException {
/*  80 */     int coltype = ((Integer)this.unpack.getFieldTypes().get(param - 1)).intValue();
/*  81 */     switch (coltype) { case 12:
/*  82 */         return "VARCHAR";
/*  83 */       case 8: return "DOUBLE";
/*  84 */       case 4: return "INTEGER";
/*  85 */       case 91: return "DATE";
/*  86 */       case 92: return "TIME";
/*  87 */       case 93: return "TIMESTAMP"; }
/*  88 */      throw new SQLException("Illegal Type");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrecision(int param) throws SQLException {
/*  94 */     return 0;
/*     */   }
/*     */   
/*     */   public int getScale(int param) throws SQLException {
/*  98 */     return 0;
/*     */   }
/*     */   
/*     */   public String getSchemaName(int param) throws SQLException {
/* 102 */     return "";
/*     */   }
/*     */   
/*     */   public String getTableName(int param) throws SQLException {
/* 106 */     return this.baseDN;
/*     */   }
/*     */   
/*     */   public boolean isAutoIncrement(int param) throws SQLException {
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCaseSensitive(int param) throws SQLException {
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCurrency(int param) throws SQLException {
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDefinitelyWritable(int param) throws SQLException {
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   public int isNullable(int param) throws SQLException {
/* 126 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(int param) throws SQLException {
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isSearchable(int param) throws SQLException {
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSigned(int param) throws SQLException {
/* 138 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isWritable(int param) throws SQLException {
/* 142 */     return false;
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
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/JdbcLdapMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */