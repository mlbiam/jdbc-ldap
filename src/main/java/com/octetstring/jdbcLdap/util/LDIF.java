/*     */ package com.octetstring.jdbcLdap.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class LDIF
/*     */ {
/*     */   public static final String DN = "dn";
/*     */   public static final String SEP = ": ";
/*     */   public static final String BIN_SEP = ":: ";
/*     */   LinkedList ldif;
/*     */   boolean debug;
/*     */   
/*     */   public LDIF() {
/*  43 */     this.ldif = new LinkedList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LDIF(String ldif, boolean debug) throws Exception {
/*  51 */     this.debug = debug;
/*  52 */     StringReader r = new StringReader(ldif);
/*  53 */     BufferedReader in = new BufferedReader(r);
/*  54 */     String attr = null, val = null;
/*     */ 
/*     */ 
/*     */     
/*  58 */     this.ldif = new LinkedList();
/*     */     
/*  60 */     HashMap<Object, Object> entry = null;
/*     */ 
/*     */     
/*     */     String line;
/*     */ 
/*     */     
/*  66 */     while ((line = in.readLine()) != null) {
/*     */       
/*  68 */       if (line.trim().length() != 0) {
/*     */ 
/*     */         
/*     */         try {
/*  72 */           attr = line.substring(0, line.indexOf(": ")).trim();
/*  73 */           val = line.substring(line.indexOf(": ") + ": ".length());
/*  74 */         } catch (Exception e) {
/*     */           
/*  76 */           e.printStackTrace(System.out);
/*  77 */           System.out.println("Error on line : " + line);
/*  78 */           System.exit(1);
/*     */         } 
/*     */ 
/*     */         
/*  82 */         if (attr.equalsIgnoreCase("dn")) {
/*  83 */           entry = new HashMap<Object, Object>();
/*  84 */           this.ldif.add(new Entry(val, entry));
/*     */           continue;
/*     */         } 
/*  87 */         LinkedList<String> attrib = (LinkedList)entry.get(attr);
/*     */ 
/*     */         
/*  90 */         if (attrib == null) {
/*  91 */           attrib = new LinkedList();
/*  92 */           entry.put(attr, attrib);
/*     */         } 
/*     */         
/*  95 */         attrib.add(val);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LDIF(ResultSet rs, String dnField, boolean debug) throws Exception {
/* 107 */     this.debug = debug;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.ldif = new LinkedList();
/*     */     
/* 114 */     HashMap<Object, Object> entry = null;
/*     */ 
/*     */ 
/*     */     
/* 118 */     while (rs.next()) {
/*     */       
/* 120 */       entry = new HashMap<Object, Object>();
/*     */       
/* 122 */       String dn = rs.getString(dnField);
/* 123 */       this.ldif.add(new Entry(dn, entry));
/*     */       
/* 125 */       ResultSetMetaData rsmd = rs.getMetaData();
/* 126 */       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
/*     */         
/* 128 */         String attr = rsmd.getColumnName(i);
/*     */ 
/*     */         
/* 131 */         String val = rs.getString(attr);
/* 132 */         if (!attr.equals(dnField) && val.trim().length() != 0) {
/*     */ 
/*     */ 
/*     */           
/* 136 */           LinkedList<String> attrib = new LinkedList();
/* 137 */           entry.put(attr, attrib);
/*     */ 
/*     */           
/* 140 */           if (val.charAt(0) == '[' && val.charAt(val.length() - 1) == ']') {
/*     */             
/* 142 */             StringTokenizer tok = new StringTokenizer(val.substring(1, val.length() - 1), "][", false);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 147 */             while (tok.hasMoreTokens()) {
/* 148 */               attrib.add(tok.nextToken());
/*     */             }
/*     */           } else {
/*     */             
/* 152 */             attrib.add(val);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean compareLdif(LDIF o, LDIF diffLdif) {
/* 160 */     if (!(o instanceof LDIF)) {
/* 161 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     LinkedList ldif1 = (LinkedList)this.ldif.clone();
/* 167 */     LinkedList<Entry> ldif2 = (LinkedList)o.ldif.clone();
/* 168 */     LinkedList<Entry> diff = new LinkedList();
/*     */     
/* 170 */     boolean match = true;
/* 171 */     boolean equals = true;
/*     */     
/* 173 */     if (this.debug) {
/*     */       
/* 175 */       System.out.println("ldif 1: " + ldif1.size());
/* 176 */       System.out.println("ldif 2: " + ldif2.size());
/*     */     } 
/*     */     
/* 179 */     Entry ent2 = null;
/* 180 */     String dn = null;
/* 181 */     Iterator<Entry> entries2 = null;
/* 182 */     Iterator<Entry> entries = ldif1.iterator();
/* 183 */     boolean found = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     while (entries.hasNext()) {
/* 191 */       Entry ent = entries.next();
/* 192 */       dn = ent.getDn();
/*     */       
/* 194 */       entries2 = ldif2.iterator();
/* 195 */       found = false;
/* 196 */       match = true;
/* 197 */       while (entries2.hasNext()) {
/* 198 */         ent2 = entries2.next();
/*     */         
/* 200 */         if (ent2.getDn().equalsIgnoreCase(dn)) {
/* 201 */           found = true;
/*     */           
/* 203 */           ldif2.remove(ent2);
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 210 */       if (!found) {
/* 211 */         System.out.println("DN " + dn + " not found");
/* 212 */         match = false;
/*     */       } 
/*     */       
/* 215 */       HashMap attribs = ent.getAtts();
/* 216 */       HashMap attribs2 = null;
/* 217 */       if (found) attribs2 = (HashMap)ent2.getAtts().clone();
/*     */       
/* 219 */       Iterator<String> attribsKeys = attribs.keySet().iterator();
/* 220 */       while (found && attribsKeys.hasNext()) {
/* 221 */         String attrib1 = attribsKeys.next();
/* 222 */         if (this.debug) {
/* 223 */           System.out.println("Looking for : " + attrib1);
/*     */         }
/*     */         
/* 226 */         LinkedList ts1 = (LinkedList)attribs.get(attrib1);
/* 227 */         Iterator<String> itat = attribs2.keySet().iterator();
/* 228 */         LinkedList ts2 = null;
/*     */         
/* 230 */         while (itat.hasNext()) {
/* 231 */           String tmpval = itat.next();
/* 232 */           if (tmpval.equalsIgnoreCase(attrib1)) {
/* 233 */             ts2 = (LinkedList)attribs2.remove(tmpval);
/* 234 */             ts2 = (LinkedList)ts2.clone();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 239 */         if (ts2 == null) {
/* 240 */           match = false;
/* 241 */           System.out.println("FAILED : " + dn);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 246 */         Iterator<String> tsi = ts1.iterator();
/* 247 */         while (tsi.hasNext()) {
/* 248 */           String val = tsi.next();
/* 249 */           if (this.debug) {
/*     */             
/* 251 */             System.out.println("\tAttib : " + attrib1);
/* 252 */             System.out.println("\tVal : " + val);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 257 */           found = false;
/* 258 */           Iterator<String> it = ts2.iterator();
/* 259 */           while (it.hasNext()) {
/* 260 */             String val2 = it.next();
/* 261 */             if (val2.equalsIgnoreCase(val)) {
/* 262 */               found = true;
/* 263 */               it.remove();
/* 264 */               tsi.remove();
/*     */               break;
/*     */             } 
/*     */           } 
/* 268 */           if (this.debug) System.out.println("\tContains : " + found); 
/* 269 */           if (!found) {
/* 270 */             System.out.println("FAILED : " + dn);
/*     */             
/* 272 */             match = false;
/*     */           } 
/*     */         } 
/*     */         
/* 276 */         if (this.debug) {
/* 277 */           System.out.println("ts2.size() : " + ts2.size());
/* 278 */           System.out.println("ts1.size() : " + ts1.size());
/*     */         } 
/* 280 */         if (ts2.size() != 0) {
/* 281 */           System.out.println("FAILED : " + dn);
/* 282 */           match = false;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 287 */       if (this.debug)
/* 288 */         System.out.println("attribs2.size() : " + ((attribs2 != null) ? Integer.toString(attribs2.size()) : "null")); 
/* 289 */       if (attribs2 == null || attribs2.size() != 0) {
/* 290 */         System.out.println("FAILED : " + dn);
/* 291 */         match = false;
/*     */       } 
/*     */       
/* 294 */       if (!match) {
/* 295 */         System.out.println("FAILED : " + dn);
/* 296 */         equals = false;
/* 297 */         diff.add(ent);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 302 */     if (this.debug)
/* 303 */       System.out.println("ldif2.size() : " + ldif2.size()); 
/* 304 */     if (ldif2.size() != 0) {
/* 305 */       entries2 = ldif2.iterator();
/* 306 */       while (entries2.hasNext()) {
/* 307 */         ent2 = entries2.next();
/* 308 */         System.out.println("FAILED : " + ((dn != null) ? dn : ""));
/* 309 */         diff.add(ent2);
/*     */       } 
/* 311 */       equals = false;
/*     */     } 
/*     */     
/* 314 */     if (!equals) {
/* 315 */       diffLdif.ldif = diff;
/*     */     }
/* 317 */     return equals;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 323 */     StringBuffer ldif = new StringBuffer();
/*     */ 
/*     */ 
/*     */     
/* 327 */     Iterator<Entry> entries = this.ldif.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     while (entries.hasNext()) {
/* 338 */       Entry ent = entries.next();
/* 339 */       String dn = ent.getDn();
/* 340 */       ldif.append("dn: ").append(dn).append('\n');
/* 341 */       HashMap entry = ent.getAtts();
/*     */       
/* 343 */       Iterator<String> atts = entry.keySet().iterator();
/* 344 */       while (atts.hasNext()) {
/* 345 */         String attrib = atts.next();
/*     */         
/* 347 */         LinkedList ts = (LinkedList)entry.get(attrib);
/* 348 */         Iterator vals = ts.iterator();
/* 349 */         while (vals.hasNext())
/*     */         {
/* 351 */           ldif.append(attrib).append(": ").append(vals.next()).append('\n');
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 356 */       ldif.append('\n');
/*     */     } 
/*     */     
/* 359 */     return ldif.toString();
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/util/LDIF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */