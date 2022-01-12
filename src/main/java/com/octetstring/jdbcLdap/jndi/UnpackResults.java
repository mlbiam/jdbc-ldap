/*     */ package com.octetstring.jdbcLdap.jndi;
/*     */ 
/*     */ import com.novell.ldap.LDAPAttribute;
/*     */ import com.novell.ldap.LDAPAttributeSet;
/*     */ import com.novell.ldap.LDAPDN;
/*     */ import com.novell.ldap.LDAPEntry;
/*     */ import com.novell.ldap.LDAPException;
/*     */ import com.novell.ldap.LDAPMessage;
/*     */ import com.novell.ldap.LDAPMessageQueue;
/*     */ import com.novell.ldap.LDAPReferralException;
/*     */ import com.novell.ldap.LDAPResponse;
/*     */ import com.novell.ldap.LDAPSearchResult;
/*     */ import com.novell.ldap.LDAPSearchResultReference;
/*     */ import com.novell.ldap.LDAPSearchResults;
/*     */ import com.novell.ldap.util.Base64;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class UnpackResults
/*     */ {
/*     */   static final String HEX_COMMA = "\\2C";
/*     */   static final String HEX_PLUS = "\\2B";
/*     */   static final String HEX_DBL_QUOTE = "\\22";
/*     */   static final String HEX_BACK_SLASH = "\\5C";
/*     */   static final String HEX_LESS = "\\3C";
/*     */   static final String HEX_MORE = "\\3E";
/*     */   static final String HEX_SEMI_COLON = "\\3B";
/*     */   static final String DN_ATT = "DN";
/*     */   JndiLdapConnection con;
/*     */   HashMap names;
/*     */   ArrayList rows;
/*     */   LDAPMessageQueue queue;
/*     */   protected boolean dn;
/*     */   protected String fromContext;
/*  76 */   static final HashMap HEX_TO_STRING = new HashMap<Object, Object>(); static {
/*  77 */     HEX_TO_STRING.put("\\2C", "\\,");
/*  78 */     HEX_TO_STRING.put("\\2B", "\\+");
/*  79 */     HEX_TO_STRING.put("\\22", "\\\"");
/*  80 */     HEX_TO_STRING.put("\\5C", "\\\\");
/*  81 */     HEX_TO_STRING.put("\\3C", "\\<");
/*  82 */     HEX_TO_STRING.put("\\3E", "\\>");
/*  83 */     HEX_TO_STRING.put("\\3B", "\\;");
/*     */   }
/*     */   protected StringBuffer buff; protected LDAPEntry entry;
/*     */   ArrayList fieldNames;
/*     */   
/*     */   public UnpackResults(JndiLdapConnection con) {
/*  89 */     this.con = con;
/*  90 */     this.names = new HashMap<Object, Object>();
/*  91 */     this.rows = new ArrayList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ArrayList fieldTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasMoreEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   private LDAPSearchResults searchResults;
/*     */ 
/*     */ 
/*     */   
/*     */   private HashMap revMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList getFieldNames() {
/* 115 */     return this.fieldNames;
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
/*     */   public ArrayList getFieldTypes() {
/* 146 */     return this.fieldTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getRows() {
/* 151 */     return this.rows;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unpackJldap(LDAPSearchResults res, boolean dn, String fromContext, String baseContext, HashMap revMap) throws SQLException {
/* 156 */     ArrayList expRows = null;
/*     */ 
/*     */     
/* 159 */     this.queue = null;
/* 160 */     this.searchResults = res;
/*     */     
/* 162 */     this.revMap = revMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 173 */     this.names.clear();
/* 174 */     this.rows.clear();
/*     */ 
/*     */ 
/*     */     
/* 178 */     LDAPEntry entry = null;
/*     */ 
/*     */ 
/*     */     
/* 182 */     buff.setLength(0);
/* 183 */     if (fromContext != null && fromContext.length() != 0)
/* 184 */       buff.append(',').append(fromContext); 
/* 185 */     if (baseContext != null && baseContext.length() != 0) {
/* 186 */       buff.append(',').append(baseContext);
/*     */     }
/* 188 */     String base = buff.toString();
/*     */     
/* 190 */     this.dn = dn;
/* 191 */     this.fromContext = fromContext;
/* 192 */     this.buff = buff;
/* 193 */     this.entry = entry;
/*     */ 
/*     */     
/* 196 */     this.fieldNames = new ArrayList();
/*     */     
/* 198 */     this.fieldTypes = new ArrayList();
/*     */ 
/*     */     
/* 201 */     this.hasMoreEntries = true;
/* 202 */     if (this.con.isPreFetch()) {
/* 203 */       int i = 0;
/* 204 */       while (moveNext(i++));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unpackJldap(LDAPMessageQueue queue, boolean dn, String fromContext, String baseContext, HashMap revMap) throws SQLException {
/* 213 */     ArrayList expRows = null;
/*     */ 
/*     */     
/* 216 */     this.revMap = revMap;
/*     */     
/* 218 */     this.queue = queue;
/* 219 */     this.searchResults = null;
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
/* 230 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 232 */     this.names.clear();
/* 233 */     this.rows.clear();
/*     */ 
/*     */ 
/*     */     
/* 237 */     LDAPEntry entry = null;
/*     */ 
/*     */ 
/*     */     
/* 241 */     buff.setLength(0);
/* 242 */     if (fromContext != null && fromContext.length() != 0)
/* 243 */       buff.append(',').append(fromContext); 
/* 244 */     if (baseContext != null && baseContext.length() != 0) {
/* 245 */       buff.append(',').append(baseContext);
/*     */     }
/* 247 */     String base = buff.toString();
/*     */     
/* 249 */     this.dn = dn;
/* 250 */     this.fromContext = fromContext;
/* 251 */     this.buff = buff;
/* 252 */     this.entry = entry;
/*     */ 
/*     */     
/* 255 */     this.fieldNames = new ArrayList();
/*     */     
/* 257 */     this.fieldTypes = new ArrayList();
/*     */ 
/*     */     
/* 260 */     this.hasMoreEntries = true;
/* 261 */     if (this.con.isPreFetch()) {
/* 262 */       int i = 0;
/* 263 */       while (moveNext(i++));
/*     */     } 
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
/*     */ 
/*     */   
/*     */   protected LDAPEntry extractEntry(boolean dn, String fromContext, StringBuffer buff, LDAPEntry entry) throws SQLNamingException {
/* 281 */     ArrayList<HashMap<Object, Object>> expRows = null;
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
/* 295 */     LDAPAttributeSet atts = entry.getAttributeSet();
/*     */     
/* 297 */     HashMap<Object, Object> row = new HashMap<Object, Object>();
/* 298 */     if (this.con.isExpandRow()) {
/* 299 */       expRows = new ArrayList();
/* 300 */       expRows.add(row);
/*     */     } 
/*     */     
/* 303 */     if (dn) {
/* 304 */       FieldStore field = (FieldStore)this.names.get("DN");
/* 305 */       if (field == null) {
/* 306 */         field = new FieldStore();
/* 307 */         field.name = "DN";
/* 308 */         this.names.put(field.name, field);
/* 309 */         this.fieldNames.add("DN");
/* 310 */         this.fieldTypes.add(new Integer(field.type));
/*     */       } 
/* 312 */       buff.setLength(0);
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
/* 326 */       row.put("DN", LDAPDN.normalize(entry.getDN()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 331 */     Object[] attribArray = atts.toArray();
/* 332 */     for (int j = 0, n = attribArray.length; j < n; j++) {
/*     */       String[] svals;
/*     */ 
/*     */ 
/*     */       
/* 337 */       LDAPAttribute attrib = (LDAPAttribute)attribArray[j];
/*     */       
/* 339 */       FieldStore field = (FieldStore)this.names.get(getFieldName(attrib.getName()));
/* 340 */       boolean existed = true;
/* 341 */       if (field == null) {
/* 342 */         field = new FieldStore();
/* 343 */         field.name = getFieldName(attrib.getName());
/* 344 */         this.names.put(field.name, field);
/* 345 */         existed = false;
/*     */       } 
/*     */       
/* 348 */       byte[] bval = attrib.getByteValue();
/* 349 */       if (bval == null) {
/* 350 */         bval = new byte[0];
/*     */       }
/* 352 */       if (Base64.isLDIFSafe(bval)) {
/* 353 */         svals = attrib.getStringValueArray();
/*     */       } else {
/* 355 */         byte[][] byteVals = attrib.getByteValueArray();
/* 356 */         svals = new String[byteVals.length];
/* 357 */         for (int i = 0, m = byteVals.length; i < m; i++) {
/* 358 */           svals[i] = Base64.encode(byteVals[i]);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 363 */       if (svals.length <= 1) {
/* 364 */         if (this.con.isExpandRow()) {
/* 365 */           String val = (svals.length != 0) ? svals[0] : "";
/* 366 */           Iterator<HashMap<Object, Object>> it = expRows.iterator();
/* 367 */           while (it.hasNext()) {
/* 368 */             field.determineType(val);
/* 369 */             row = it.next();
/* 370 */             row.put(field.name, val);
/*     */           } 
/*     */           
/* 373 */           if (!existed) {
/* 374 */             this.fieldNames.add(field.name);
/* 375 */             this.fieldTypes.add(new Integer(field.type));
/*     */           } 
/*     */         } else {
/*     */           
/* 379 */           String val = svals[0];
/* 380 */           field.determineType(val);
/* 381 */           row.put(field.name, val);
/* 382 */           if (!existed) {
/* 383 */             this.fieldNames.add(field.name);
/* 384 */             this.fieldTypes.add(new Integer(field.type));
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 389 */       } else if (this.con.getConcatAtts()) {
/* 390 */         buff.setLength(0);
/* 391 */         field.numVals = 0;
/*     */         
/* 393 */         for (int i = 0, m = svals.length; i < m; i++) {
/* 394 */           String val = svals[i];
/* 395 */           field.determineType(val);
/* 396 */           buff.append('[').append(val).append(']');
/*     */         } 
/*     */         
/* 399 */         row.put(field.name, buff.toString());
/* 400 */         if (!existed) {
/* 401 */           this.fieldNames.add(field.name);
/* 402 */           this.fieldTypes.add(new Integer(field.type));
/*     */         }
/*     */       
/* 405 */       } else if (this.con.isExpandRow()) {
/*     */         
/* 407 */         ArrayList<HashMap<Object, Object>> tmprows = new ArrayList();
/*     */         
/* 409 */         for (int i = 0, m = svals.length; i < m; i++) {
/*     */           
/* 411 */           String val = svals[i];
/* 412 */           field.determineType(val);
/* 413 */           Iterator<HashMap<Object, Object>> it = expRows.iterator();
/*     */           
/* 415 */           while (it.hasNext()) {
/* 416 */             row = it.next();
/* 417 */             row = (HashMap<Object, Object>)row.clone();
/*     */             
/* 419 */             row.put(field.name, val);
/* 420 */             tmprows.add(row);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 426 */         if (!existed) {
/* 427 */           this.fieldNames.add(field.name);
/* 428 */           this.fieldTypes.add(new Integer(field.type));
/*     */         } 
/*     */         
/* 431 */         expRows = tmprows;
/*     */       } else {
/*     */         
/* 434 */         int currNumVals = 0;
/* 435 */         int low = field.numVals;
/* 436 */         for (int i = 0, m = svals.length; i < m; i++) {
/* 437 */           buff.setLength(0);
/* 438 */           String val = svals[i];
/* 439 */           field.determineType(val);
/* 440 */           row.put(buff.append(field.name).append('_').append(currNumVals).toString(), val);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 447 */           currNumVals++;
/*     */           
/* 449 */           String fieldName = field.name + "_" + Integer.toString(currNumVals - 1);
/*     */           
/* 451 */           if (currNumVals >= low && !this.fieldNames.contains(fieldName)) {
/* 452 */             this.fieldNames.add(fieldName);
/* 453 */             this.fieldTypes.add(new Integer(field.type));
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 459 */         field.numVals = (currNumVals > field.numVals) ? currNumVals : field.numVals;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 467 */     if (this.con.isExpandRow()) {
/* 468 */       this.rows.addAll(expRows);
/*     */     } else {
/*     */       
/* 471 */       this.rows.add(row);
/*     */     } 
/* 473 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LDAPEntry getEntry(LDAPSearchResults results, String fromContext, LDAPEntry entry) throws SQLNamingException {
/*     */     try {
/* 485 */       entry = results.next();
/*     */     }
/* 487 */     catch (LDAPReferralException ref) {
/*     */ 
/*     */       
/* 490 */       String refName = "cn=Referral[" + ref.getReferrals()[0] + "]";
/* 491 */       if (entry == null) {
/*     */         
/* 493 */         if (this.con.baseDN != null && this.con.baseDN.trim().length() >= 0) {
/* 494 */           refName = refName + "," + fromContext;
/*     */         }
/*     */       } else {
/*     */         
/* 498 */         String[] parts = LDAPDN.explodeDN(entry.getDN(), false);
/* 499 */         for (int j = 1, k = parts.length; j < k; j++) {
/* 500 */           refName = refName + "," + parts[j];
/*     */         }
/*     */       } 
/* 503 */       LDAPAttribute attrib = new LDAPAttribute("ref");
/* 504 */       String[] refUrls = ref.getReferrals();
/* 505 */       for (int i = 0, m = refUrls.length; i < m; i++)
/*     */       {
/* 507 */         attrib.addValue(refUrls[i]);
/*     */       }
/*     */ 
/*     */       
/* 511 */       LDAPAttributeSet attribs = new LDAPAttributeSet();
/* 512 */       attribs.add(attrib);
/*     */       
/* 514 */       entry = new LDAPEntry(refName, attribs);
/*     */ 
/*     */     
/*     */     }
/* 518 */     catch (LDAPException e) {
/* 519 */       throw new SQLNamingException(e);
/*     */     } 
/* 521 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public String cleanDn(String dn) {
/* 526 */     StringBuffer buf = new StringBuffer(dn);
/*     */     
/* 528 */     int begin = buf.indexOf("\\");
/*     */     
/* 530 */     while (begin != -1) {
/* 531 */       String val = (String)HEX_TO_STRING.get(buf.substring(begin, begin + 3));
/* 532 */       if (val != null) {
/* 533 */         buf.replace(begin, begin + 3, val);
/*     */       }
/* 535 */       begin = buf.indexOf("\\", begin + 1);
/*     */     } 
/*     */     
/* 538 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveNext(int index) throws SQLNamingException {
/* 549 */     if (index >= this.rows.size()) {
/* 550 */       if (this.hasMoreEntries) {
/* 551 */         getNextEntry();
/*     */         
/* 553 */         return this.hasMoreEntries;
/*     */       } 
/* 555 */       return false;
/*     */     } 
/*     */     
/* 558 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getNextEntry() throws SQLNamingException {
/* 566 */     if (this.queue != null) {
/* 567 */       getNextQueue();
/*     */     } else {
/* 569 */       getNextResults();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getNextQueue() throws SQLNamingException {
/*     */     LDAPMessage message;
/*     */     try {
/* 579 */       message = this.queue.getResponse();
/* 580 */     } catch (LDAPException e) {
/* 581 */       throw new SQLNamingException(e);
/*     */     } 
/* 583 */     if (message instanceof LDAPSearchResult) {
/* 584 */       this.entry = ((LDAPSearchResult)message).getEntry();
/* 585 */       extractEntry(this.dn, this.fromContext, this.buff, this.entry);
/*     */     }
/* 587 */     else if (message instanceof LDAPSearchResultReference) {
/* 588 */       LDAPSearchResultReference ref = (LDAPSearchResultReference)message;
/*     */ 
/*     */       
/* 591 */       String refName = "cn=Referral[" + ref.getReferrals()[0] + "]";
/* 592 */       if (this.entry == null) {
/*     */         
/* 594 */         if (this.con.baseDN != null && this.con.baseDN.trim().length() >= 0) {
/* 595 */           refName = refName + "," + this.fromContext;
/*     */         }
/*     */       } else {
/*     */         
/* 599 */         String[] parts = LDAPDN.explodeDN(this.entry.getDN(), false);
/* 600 */         for (int j = 1, k = parts.length; j < k; j++) {
/* 601 */           refName = refName + "," + parts[j];
/*     */         }
/*     */       } 
/* 604 */       LDAPAttribute attrib = new LDAPAttribute("ref");
/* 605 */       String[] refUrls = ref.getReferrals();
/* 606 */       for (int i = 0, m = refUrls.length; i < m; i++)
/*     */       {
/* 608 */         attrib.addValue(refUrls[i]);
/*     */       }
/*     */ 
/*     */       
/* 612 */       LDAPAttributeSet attribs = new LDAPAttributeSet();
/* 613 */       attribs.add(attrib);
/*     */       
/* 615 */       this.entry = new LDAPEntry(refName, attribs);
/* 616 */       extractEntry(this.dn, this.fromContext, this.buff, this.entry);
/*     */     } else {
/*     */       
/* 619 */       LDAPResponse resp = (LDAPResponse)message;
/* 620 */       if (resp.getResultCode() == 0) {
/* 621 */         this.hasMoreEntries = false;
/*     */       } else {
/*     */         
/* 624 */         throw new SQLNamingException(new LDAPException(resp.getErrorMessage(), resp.getResultCode(), resp.getErrorMessage(), resp.getMatchedDN()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getNextResults() throws SQLNamingException {
/* 635 */     if (!this.searchResults.hasMore()) {
/* 636 */       this.hasMoreEntries = false;
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 641 */       this.entry = this.searchResults.next();
/*     */       
/* 643 */       if (this.con.isSPML()) {
/* 644 */         String name = this.entry.getDN();
/* 645 */         this.entry = new LDAPEntry(name + ",ou=Users," + this.con.getBaseContext(), this.entry.getAttributeSet());
/*     */       } 
/*     */       
/* 648 */       extractEntry(this.dn, this.fromContext, this.buff, this.entry);
/*     */     }
/* 650 */     catch (LDAPReferralException ref) {
/*     */ 
/*     */       
/* 653 */       String refName = "cn=Referral[" + ref.getReferrals()[0] + "]";
/* 654 */       if (this.entry == null) {
/*     */         
/* 656 */         if (this.con.baseDN != null && this.con.baseDN.trim().length() >= 0) {
/* 657 */           refName = refName + "," + this.fromContext;
/*     */         }
/*     */       } else {
/*     */         
/* 661 */         String[] parts = LDAPDN.explodeDN(this.entry.getDN(), false);
/* 662 */         for (int j = 1, k = parts.length; j < k; j++) {
/* 663 */           refName = refName + "," + parts[j];
/*     */         }
/*     */       } 
/* 666 */       LDAPAttribute attrib = new LDAPAttribute("ref");
/* 667 */       String[] refUrls = ref.getReferrals();
/* 668 */       for (int i = 0, m = refUrls.length; i < m; i++)
/*     */       {
/* 670 */         attrib.addValue(refUrls[i]);
/*     */       }
/*     */ 
/*     */       
/* 674 */       LDAPAttributeSet attribs = new LDAPAttributeSet();
/* 675 */       attribs.add(attrib);
/*     */ 
/*     */       
/* 678 */       this.entry = new LDAPEntry(refName, attribs);
/* 679 */       extractEntry(this.dn, this.fromContext, this.buff, this.entry);
/* 680 */     } catch (LDAPException ldape) {
/* 681 */       throw new SQLNamingException(ldape);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String getFieldName(String name) {
/* 687 */     if (this.revMap != null) {
/* 688 */       String nname = (String)this.revMap.get(name);
/* 689 */       if (nname != null) {
/* 690 */         return nname;
/*     */       }
/*     */     } 
/*     */     
/* 694 */     return name;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/jndi/UnpackResults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */