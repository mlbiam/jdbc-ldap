/*     */ package com.octetstring.jdbcLdap.util;
/*     */ 
/*     */ import com.novell.ldap.LDAPAttributeSchema;
/*     */ import com.novell.ldap.LDAPConnection;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.novell.ldap.LDAPObjectClassSchema;
/*     */ import com.novell.ldap.LDAPSchema;
/*     */ import java.lang.reflect.Field;
/*     */ import java.sql.Types;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public class TableDef
/*     */ {
/*     */   String dn;
/*     */   String scope;
/*     */   String combined;
/*     */   ArrayList metadata;
/*     */   HashMap attrMetaData;
/*     */   private String name;
/*     */   private static final String synbase = "1.3.6.1.4.1.1466.115.121.1.";
/*     */   private static final String adSynBase = "1.2.840.113556.1.4.90";
/*  52 */   static HashMap syntaxToSQL = new HashMap<Object, Object>(); static {
/*  53 */     syntaxToSQL.put("name", "VARCHAR");
/*  54 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.3", "VARCHAR");
/*  55 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.5", "BINARY");
/*  56 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.6", "VARCHAR");
/*  57 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.7", "VARCHAR");
/*  58 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.8", "BINARY");
/*  59 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.9", "BINARY");
/*  60 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.10", "BINARY");
/*  61 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.11", "VARCHAR");
/*  62 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.12", "VARCHAR");
/*  63 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.14", "VARCHAR");
/*  64 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.15", "VARCHAR");
/*  65 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.16", "VARCHAR");
/*  66 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.17", "VARCHAR");
/*  67 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.21", "VARCHAR");
/*  68 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.22", "VARCHAR");
/*  69 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.23", "BINARY");
/*  70 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.24", "VARCHAR");
/*  71 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.25", "VARCHAR");
/*  72 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.26", "VARCHAR");
/*  73 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.27", "INTEGER");
/*  74 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.28", "BINARY");
/*  75 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.30", "VARCHAR");
/*  76 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.31", "VARCHAR");
/*  77 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.33", "VARCHAR");
/*  78 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.34", "VARCHAR");
/*  79 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.35", "VARCHAR");
/*  80 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.36", "INTEGER");
/*  81 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.37", "VARCHAR");
/*  82 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.38", "VARCHAR");
/*  83 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.39", "VARCHAR");
/*  84 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.40", "BINARY");
/*     */     
/*  86 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.41", "VARCHAR");
/*  87 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.43", "VARCHAR");
/*  88 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.44", "VARCHAR");
/*  89 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.50", "VARCHAR");
/*  90 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.51", "VARCHAR");
/*  91 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.53", "VARCHAR");
/*  92 */     syntaxToSQL.put("1.3.6.1.4.1.1466.115.121.1.54", "VARCHAR");
/*     */ 
/*     */     
/*  95 */     syntaxToSQL.put("1.2.840.113556.1.4.903", "BINARY");
/*  96 */     syntaxToSQL.put("1.2.840.113556.1.4.905", "VARCHAR");
/*  97 */     syntaxToSQL.put("1.2.840.113556.1.4.906", "INTEGER");
/*  98 */     syntaxToSQL.put("1.2.840.113556.1.4.907", "BINARY");
/*     */   }
/*     */   private HashMap addPatterns;
/*     */   public TableDef(String name, String dn, String scope, String[] objectClasses, LDAPConnection ldapcon, HashMap addPatternMap) throws LDAPException {
/* 102 */     this.name = name;
/* 103 */     this.dn = dn;
/* 104 */     this.scope = scope;
/* 105 */     this.combined = scope + ";" + dn;
/* 106 */     this.attrMetaData = new HashMap<Object, Object>();
/* 107 */     LDAPSchema schema = ldapcon.fetchSchema(ldapcon.getSchemaDN());
/*     */ 
/*     */ 
/*     */     
/* 111 */     HashSet<String> proced = new HashSet();
/* 112 */     HashSet procedAttribs = new HashSet();
/*     */     
/* 114 */     proced.add("top");
/*     */     
/* 116 */     int index = 0;
/*     */     
/* 118 */     this.metadata = new ArrayList();
/*     */     
/* 120 */     for (int i = 0, m = objectClasses.length; i < m; i++) {
/* 121 */       String oc = objectClasses[i];
/* 122 */       if (!proced.contains(oc)) {
/* 123 */         LDAPObjectClassSchema ocSchema = schema.getObjectClassSchema(oc);
/*     */ 
/*     */ 
/*     */         
/* 127 */         index = extractObjectClass(schema, proced, procedAttribs, index, oc, ocSchema);
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     this.addPatterns = addPatternMap;
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
/*     */   private int extractObjectClass(LDAPSchema schema, HashSet<String> proced, HashSet procedAttribs, int index, String oc, LDAPObjectClassSchema ocSchema) {
/* 145 */     String[] sups = ocSchema.getSuperiors();
/*     */     
/* 147 */     for (int j = 0, n = sups.length; j < n; j++) {
/* 148 */       String sup = sups[j];
/* 149 */       if (!proced.contains(sup)) {
/* 150 */         index = extractObjectClass(schema, proced, procedAttribs, index, sup, schema.getObjectClassSchema(sup));
/* 151 */         proced.add(sup);
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     index = addObjectClass(oc, this.metadata, schema, index, procedAttribs);
/*     */     
/* 157 */     proced.add(oc);
/*     */     
/* 159 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int addObjectClass(String oc, ArrayList<LinkedHashMap<Object, Object>> table, LDAPSchema schema, int index, Set<String> procedAttribs) {
/* 165 */     LDAPObjectClassSchema ocSchema = schema.getObjectClassSchema(oc);
/*     */     
/* 167 */     String[] attribs = ocSchema.getOptionalAttributes();
/*     */     
/* 169 */     if (attribs != null) {
/* 170 */       for (int i = 0, m = attribs.length; i < m; i++) {
/* 171 */         LinkedHashMap<Object, Object> row = new LinkedHashMap<Object, Object>();
/* 172 */         LDAPAttributeSchema attribSchema = schema.getAttributeSchema(attribs[i]);
/*     */         
/* 174 */         String name = attribSchema.getNames()[0];
/*     */         
/* 176 */         if (attribSchema != null && !procedAttribs.contains(attribSchema.getNames()[0])) {
/*     */ 
/*     */ 
/*     */           
/* 180 */           row.put("TABLE_CAT", null);
/* 181 */           row.put("TABLE_SCHEM", null);
/* 182 */           row.put("TABLE_NAME", this.name);
/* 183 */           row.put("COLUMN_NAME", attribSchema.getNames()[0]);
/* 184 */           row.put("DATA_TYPE", new Integer(getType(attribSchema.getSyntaxString())));
/* 185 */           row.put("TYPE_NAME", getTypeName(attribSchema.getSyntaxString()));
/* 186 */           row.put("COLUMN_SIZE", new Integer(255));
/* 187 */           row.put("BUFFER_LENGTH", new Integer(0));
/* 188 */           row.put("DECIMAL_DIGITS", new Integer(10));
/* 189 */           row.put("NUM_PREC_RADIX", new Integer(10));
/* 190 */           row.put("NULLABLE", "columnNullable");
/* 191 */           row.put("REMARKS", attribSchema.getDescription());
/* 192 */           row.put("COLUMN_DEF", null);
/* 193 */           row.put("SQL_DATA_TYPE", new Integer(0));
/* 194 */           row.put("SQL_DATETIME_SUB", new Integer(0));
/* 195 */           row.put("CHAR_OCTET_LENGTH", new Integer(255));
/* 196 */           row.put("ORDINAL_POSITION", new Integer(++index));
/* 197 */           row.put("IS_NULLABLE", "YES");
/* 198 */           row.put("SCOPE_CATALOG", null);
/* 199 */           row.put("SCOPE_TABLE", null);
/* 200 */           row.put("SCOPE_DATA_TYPE", null);
/*     */           
/* 202 */           table.add(row);
/* 203 */           this.attrMetaData.put(attribSchema.getNames()[0], row);
/* 204 */           procedAttribs.add(attribSchema.getNames()[0]);
/*     */         } 
/*     */       } 
/*     */     }
/* 208 */     attribs = ocSchema.getRequiredAttributes();
/*     */     
/* 210 */     if (attribs != null) {
/* 211 */       for (int i = 0, m = attribs.length; i < m; i++) {
/* 212 */         LinkedHashMap<Object, Object> row = new LinkedHashMap<Object, Object>();
/* 213 */         LDAPAttributeSchema attribSchema = schema.getAttributeSchema(attribs[i]);
/* 214 */         String name = attribSchema.getNames()[0];
/*     */         
/* 216 */         if (attribSchema != null && !procedAttribs.contains(attribSchema.getNames()[0])) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 221 */           row.put("TABLE_CAT", null);
/* 222 */           row.put("TABLE_SCHEM", null);
/* 223 */           row.put("TABLE_NAME", this.name);
/* 224 */           row.put("COLUMN_NAME", attribSchema.getNames()[0]);
/* 225 */           row.put("DATA_TYPE", new Integer(getType(attribSchema.getSyntaxString())));
/* 226 */           row.put("TYPE_NAME", getTypeName(attribSchema.getSyntaxString()));
/* 227 */           row.put("COLUMN_SIZE", new Integer(255));
/* 228 */           row.put("BUFFER_LENGTH", new Integer(0));
/* 229 */           row.put("DECIMAL_DIGITS", new Integer(10));
/* 230 */           row.put("NUM_PREC_RADIX", new Integer(10));
/* 231 */           row.put("NULLABLE", "columnNoNulls ");
/* 232 */           row.put("REMARKS", attribSchema.getDescription());
/* 233 */           row.put("COLUMN_DEF", null);
/* 234 */           row.put("SQL_DATA_TYPE", new Integer(0));
/* 235 */           row.put("SQL_DATETIME_SUB", new Integer(0));
/* 236 */           row.put("CHAR_OCTET_LENGTH", new Integer(255));
/* 237 */           row.put("ORDINAL_POSITION", new Integer(++index));
/* 238 */           row.put("IS_NULLABLE", "NO");
/* 239 */           row.put("SCOPE_CATALOG", null);
/* 240 */           row.put("SCOPE_TABLE", null);
/* 241 */           row.put("SCOPE_DATA_TYPE", null);
/*     */           
/* 243 */           this.attrMetaData.put(attribSchema.getNames()[0], row);
/* 244 */           table.add(row);
/* 245 */           procedAttribs.add(attribSchema.getNames()[0]);
/*     */         } 
/*     */       } 
/*     */     }
/* 249 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getTypeName(String syntaxString) {
/* 257 */     int index = syntaxString.indexOf('{');
/* 258 */     if (index != -1) {
/* 259 */       syntaxString = syntaxString.substring(0, index);
/*     */     }
/* 261 */     return (String)syntaxToSQL.get(syntaxString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getType(String syntaxString) {
/* 269 */     Field[] fields = Types.class.getFields();
/*     */     
/* 271 */     String name = getTypeName(syntaxString);
/*     */     
/* 273 */     if (name == null) {
/* 274 */       return -1;
/*     */     }
/*     */     
/* 277 */     for (int i = 0, m = fields.length; i < m; i++) {
/* 278 */       if (fields[i].getName().equals(name)) {
/*     */         try {
/* 280 */           return fields[i].getInt(null);
/* 281 */         } catch (IllegalArgumentException e) {
/*     */           
/* 283 */           e.printStackTrace();
/* 284 */           return -1;
/* 285 */         } catch (IllegalAccessException e) {
/*     */           
/* 287 */           e.printStackTrace();
/* 288 */           return -1;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 293 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList getTable() {
/* 300 */     return this.metadata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 307 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScopeBase() {
/* 314 */     return this.combined;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap getAddPatterns() {
/* 321 */     return this.addPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBase() {
/* 328 */     return this.dn;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/TableDef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */