/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import com.novell.ldap.LDAPMessageQueue;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
/*     */ import com.octetstring.jdbcLdap.sql.statements.JdbcLdapSelect;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
import java.sql.NClob;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
import java.sql.RowId;
/*     */ import java.sql.SQLException;
import java.sql.SQLXML;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
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
/*     */ public class JdbcLdapPreparedStatement
/*     */   extends JdbcLdapStatement
/*     */   implements PreparedStatement
/*     */ {
/*     */   public JdbcLdapPreparedStatement(String sql, JndiLdapConnection con) throws SQLException {
/*  47 */     super(con);
/*  48 */     loadSQL(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void loadSQL(SqlStore store) throws SQLException {
/*  57 */     loadSQL(store.getSQL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setVal(int pos, String val) throws SQLException {
/*  68 */     this.stmt.setValue(pos - 1, val);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnicodeStream(int param, InputStream inputStream, int param2) throws SQLException {
/*  74 */     char[] c = new char[param2];
/*     */     
/*     */     try {
/*  77 */       BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
/*  78 */       in.read(c, 0, param2);
/*     */       
/*  80 */       in.close();
/*     */     }
/*  82 */     catch (IOException e) {
/*  83 */       throw new SQLException(e.toString());
/*     */     } 
/*  85 */     setVal(param, String.valueOf(c));
/*     */   }
/*     */   
/*     */   public void setTime(int param, Time time) throws SQLException {
/*  89 */     setVal(param, time.toString());
/*     */   }
/*     */   
/*     */   public void setBigDecimal(int param, BigDecimal bigDecimal) throws SQLException {
/*  93 */     setVal(param, bigDecimal.toString());
/*     */   }
/*     */   
/*     */   public boolean execute() throws SQLException {
/*  97 */     if (this.stmt.isUpdate()) {
/*  98 */       executeUpdate();
/*  99 */       return false;
/*     */     } 
/*     */     
/* 102 */     executeQuery();
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setURL(int param, URL uRL) throws SQLException {
/* 108 */     setVal(param, uRL.toString());
/*     */   }
/*     */   
/*     */   public void setAsciiStream(int param, InputStream inputStream, int param2) throws SQLException {
/* 112 */     char[] c = new char[param2];
/*     */     try {
/* 114 */       BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
/*     */       
/* 116 */       in.read(c, 0, param2);
/*     */       
/* 118 */       in.close();
/*     */     }
/* 120 */     catch (IOException e) {
/* 121 */       throw new SQLException(e.toString());
/*     */     } 
/* 123 */     setVal(param, String.valueOf(c));
/*     */   }
/*     */   
/*     */   public void setByte(int param, byte param1) throws SQLException {
/* 127 */     setVal(param, Byte.toString(param1));
/*     */   }
/*     */   
/*     */   public void setDouble(int param, double param1) throws SQLException {
/* 131 */     setVal(param, Double.toString(param1));
/*     */   }
/*     */   
/*     */   public void setLong(int param, long param1) throws SQLException {
/* 135 */     setVal(param, Long.toString(param1));
/*     */   }
/*     */   
/*     */   public void setDate(int param, Date date) throws SQLException {
/* 139 */     setVal(param, date.toString());
/*     */   }
/*     */   
/*     */   public void setBinaryStream(int param, InputStream inputStream, int param2) throws SQLException {
/* 143 */     char[] c = new char[param2];
/*     */     
/*     */     try {
/* 146 */       BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
/* 147 */       in.read(c, 0, param2);
/*     */       
/* 149 */       in.close();
/*     */     }
/* 151 */     catch (IOException e) {
/* 152 */       throw new SQLException(e.toString());
/*     */     } 
/* 154 */     setVal(param, String.valueOf(c));
/*     */   }
/*     */   
/*     */   public ParameterMetaData getParameterMetaData() throws SQLException {
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public void setTime(int param, Time time, Calendar calendar) throws SQLException {
/* 162 */     setVal(param, time.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlob(int param, Blob blob) throws SQLException {}
/*     */   
/*     */   public ResultSet executeQuery() throws SQLException {
/* 169 */     System.out.println("Is No Con : " + this.con.isNoCon());
/* 170 */     if (this.con.isDSML() || this.con.isSPML() || this.con.isNoCon()) {
/* 171 */       this.res.unpackJldap((LDAPSearchResults)this.stmt.executeQuery(), this.stmt.getRetrieveDN(), this.stmt.getSqlStore().getFrom(), this.con.getBaseDN(), this.stmt.getSqlStore().getRevFieldMap());
/*     */     } else {
/* 173 */       this.res.unpackJldap((LDAPMessageQueue)this.stmt.executeQuery(), this.stmt.getRetrieveDN(), this.stmt.getSqlStore().getFrom(), this.con.getBaseDN(), this.stmt.getSqlStore().getRevFieldMap());
/*     */     } 
/*     */ 
/*     */     
/* 177 */     this.rs = new LdapResultSet(this.con, this, this.res, ((JdbcLdapSelect)this.stmt).getBaseContext());
/* 178 */     return this.rs;
/*     */   }
/*     */   
/*     */   public void setCharacterStream(int param, Reader reader, int param2) throws SQLException {
/* 182 */     char[] c = new char[param2];
/*     */     
/*     */     try {
/* 185 */       BufferedReader in = new BufferedReader(reader);
/* 186 */       in.read(c, 0, param2);
/*     */       
/* 188 */       in.close();
/*     */     }
/* 190 */     catch (IOException e) {
/* 191 */       throw new SQLException(e.toString());
/*     */     } 
/* 193 */     setVal(param, String.valueOf(c));
/*     */   }
/*     */   
/*     */   public ResultSetMetaData getMetaData() throws SQLException {
/* 197 */     return this.rs.getMetaData();
/*     */   }
/*     */   
/*     */   public void setTimestamp(int param, Timestamp timestamp, Calendar calendar) throws SQLException {
/* 201 */     setVal(param, timestamp.toString());
/*     */   }
/*     */   
/*     */   public void setObject(int param, Object obj, int param2, int param3) throws SQLException {
/* 205 */     setVal(param, obj.toString());
/*     */   }
/*     */   
/*     */   public void setObject(int param, Object obj, int param2) throws SQLException {
/* 209 */     setVal(param, obj.toString());
/*     */   }
/*     */   
/*     */   public void setObject(int param, Object obj) throws SQLException {
/* 213 */     setVal(param, obj.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRef(int param, Ref ref) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setArray(int param, Array array) throws SQLException {}
/*     */   
/*     */   public void setTimestamp(int param, Timestamp timestamp) throws SQLException {
/* 223 */     setVal(param, timestamp.toString());
/*     */   }
/*     */   
/*     */   public void setInt(int param, int param1) throws SQLException {
/* 227 */     setVal(param, Integer.toString(param1));
/*     */   }
/*     */   
/*     */   public void setBytes(int param, byte[] values) throws SQLException {
/* 231 */     setCharacterStream(param, new InputStreamReader(new ByteArrayInputStream(values)), values.length);
/*     */   }
/*     */   
/*     */   public void setShort(int param, short param1) throws SQLException {
/* 235 */     setVal(param, Short.toString(param1));
/*     */   }
/*     */   
/*     */   public void setFloat(int param, float param1) throws SQLException {
/* 239 */     setVal(param, Float.toString(param1));
/*     */   }
/*     */   
/*     */   public void setBoolean(int param, boolean param1) throws SQLException {
/* 243 */     setVal(param, Boolean.toString(param1));
/*     */   }
/*     */   
/*     */   public void setDate(int param, Date date, Calendar calendar) throws SQLException {
/* 247 */     setVal(param, date.toString());
/*     */   }
/*     */   
/*     */   public int executeUpdate() throws SQLException {
/* 251 */     return ((Integer)this.stmt.executeUpdate()).intValue();
/*     */   }
/*     */   
/*     */   public void setString(int param, String str) throws SQLException {
/* 255 */     setVal(param, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClob(int param, Clob clob) throws SQLException {}
/*     */   
/*     */   public void addBatch() throws SQLException {
/* 262 */     this.statements.add(this.stmt);
/* 263 */     loadSQL(this.stmt.getSqlStore());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearParameters() throws SQLException {}
/*     */   
/*     */   public void setNull(int param, int param1) throws SQLException {
/* 270 */     setVal(param, (String)null);
/*     */   }
/*     */   
/*     */   public void setNull(int param, int param1, String str) throws SQLException {
/* 274 */     setVal(param, (String)null);
/*     */   }
/*     */
@Override
public boolean isClosed() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void setPoolable(boolean poolable) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public boolean isPoolable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
@Override
public void closeOnCompletion() throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public boolean isCloseOnCompletion() throws SQLException {
    // TODO Auto-generated method stub
    return false;
}
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
public void setRowId(int parameterIndex, RowId x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNString(int parameterIndex, String value) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNClob(int parameterIndex, NClob value) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setClob(int parameterIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    // TODO Auto-generated method stub
    
}
@Override
public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
    
} }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/JdbcLdapPreparedStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */