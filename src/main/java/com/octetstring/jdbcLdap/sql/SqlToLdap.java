/*     */ package com.octetstring.jdbcLdap.sql;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Stack;
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
/*     */ public class SqlToLdap
/*     */ {
/*     */   public static final String SQL_AND = "AND";
/*     */   public static final String SQL_OR = "OR";
/*     */   public static final String SQL_NOT = "NOT";
/*     */   public static final String SQL_NULL = "NULL";
/*     */   public static final String SQL_IS = "IS";
/*     */   public static final char LEFT_PAR = '(';
/*     */   public static final char RIGHT_PAR = ')';
/*     */   static final String SL_PAR = "(";
/*     */   static final String SR_PAR = ")";
/*     */   HashMap order;
/*     */   
/*     */   public SqlToLdap() {
/*  70 */     this.order = new HashMap<Object, Object>();
/*  71 */     this.order.put("NOT", new Integer(5));
/*  72 */     this.order.put("AND", new Integer(4));
/*  73 */     this.order.put("OR", new Integer(3));
/*  74 */     this.order.put(")", new Integer(1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean lastNodeGreater(Stack<Node> opps, String curr) {
/*  84 */     if (opps.isEmpty()) return false;
/*     */     
/*  86 */     Node node = opps.peek();
/*     */     
/*  88 */     return (node.type > ((Integer)this.order.get(curr)).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isCmd(String curr) {
/*  97 */     return (curr.equalsIgnoreCase(")") || curr.equalsIgnoreCase("AND") || curr.equalsIgnoreCase("OR") || curr.equalsIgnoreCase("NOT"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void procStack(Stack<Node> opps, Stack<Node> elements, int curr) {
/*     */     Node opp;
/* 109 */     if (opps.isEmpty())
/*     */       return; 
/*     */     do {
/*     */       Node l;
/* 113 */       opp = opps.pop();
/*     */       
/* 115 */       Node r = elements.pop();
/* 116 */       if (opp.type != 5 && opp.type != 0) {
/*     */         
/* 118 */         l = elements.pop();
/*     */       }
/*     */       else {
/*     */         
/* 122 */         l = null;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 127 */       opp.l = l;
/* 128 */       opp.r = r;
/*     */       
/* 130 */       if (opp.type == 0) {
/* 131 */         elements.push(opp.r);
/*     */       } else {
/*     */         
/* 134 */         elements.push(opp);
/*     */       }
/*     */     
/* 137 */     } while (opp.type > curr && !opps.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String convertToLdap(String expr, HashMap fieldMap) throws SQLException {
/* 147 */     LinkedList list = inOrder(expr, fieldMap);
/*     */     
/* 149 */     Stack<Node> elements = new Stack();
/* 150 */     Stack<Node> opps = new Stack();
/*     */ 
/*     */ 
/*     */     
/* 154 */     Iterator<String> it = list.iterator();
/* 155 */     while (it.hasNext()) {
/*     */       
/* 157 */       String curr = it.next();
/*     */       
/* 159 */       while (curr.trim().length() == 0 && it.hasNext()) {
/* 160 */         curr = it.next();
/*     */       }
/*     */ 
/*     */       
/* 164 */       String currUCase = curr.toUpperCase();
/*     */       
/* 166 */       if (curr.equalsIgnoreCase("(")) {
/* 167 */         Node node1 = new Node();
/* 168 */         node1.type = 0;
/* 169 */         node1.l = null;
/* 170 */         node1.r = null;
/* 171 */         opps.push(node1); continue;
/*     */       } 
/* 173 */       if (isCmd(currUCase)) {
/* 174 */         if (curr.equalsIgnoreCase(")")) {
/* 175 */           procStack(opps, elements, ((Integer)this.order.get(")")).intValue()); continue;
/*     */         } 
/* 177 */         if (lastNodeGreater(opps, currUCase)) {
/* 178 */           procStack(opps, elements, ((Integer)this.order.get(currUCase)).intValue());
/* 179 */           Node node2 = new Node();
/* 180 */           node2.l = null;
/* 181 */           node2.r = null;
/* 182 */           node2.type = ((Integer)this.order.get(currUCase)).intValue();
/* 183 */           opps.push(node2);
/*     */           continue;
/*     */         } 
/* 186 */         Node node1 = new Node();
/* 187 */         node1.l = null;
/* 188 */         node1.r = null;
/* 189 */         node1.type = ((Integer)this.order.get(currUCase)).intValue();
/* 190 */         opps.push(node1); continue;
/*     */       } 
/* 192 */       if (currUCase.equals("IS")) {
/* 193 */         String next = it.next();
/*     */         
/* 195 */         if (next.equalsIgnoreCase("NULL")) {
/* 196 */           Node node1 = elements.peek();
/* 197 */           if (fieldMap != null) {
/*     */             
/* 199 */             String fieldName = node1.val;
/* 200 */             String newField = (String)fieldMap.get(fieldName);
/* 201 */             if (newField != null) {
/* 202 */               node1.val = newField;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 207 */           node1.val = "!(" + node1.val + "=*)"; continue;
/* 208 */         }  if (next.equalsIgnoreCase("NOT")) {
/* 209 */           String next2 = it.next();
/* 210 */           if (!next2.equalsIgnoreCase("NULL")) {
/* 211 */             throw new SQLException("Unexpected token near IS");
/*     */           }
/*     */           
/* 214 */           Node node1 = elements.peek();
/*     */           
/* 216 */           if (fieldMap != null) {
/*     */             
/* 218 */             String fieldName = node1.val;
/* 219 */             String newField = (String)fieldMap.get(fieldName);
/* 220 */             if (newField != null) {
/* 221 */               node1.val = newField;
/*     */             }
/*     */           } 
/*     */           
/* 225 */           node1.val += "=*";
/*     */           continue;
/*     */         } 
/* 228 */         throw new SQLException("Unexpected token near IS");
/*     */       } 
/*     */ 
/*     */       
/* 232 */       Node node = new Node();
/* 233 */       node.l = null;
/* 234 */       node.r = null;
/* 235 */       node.val = curr;
/* 236 */       node.type = 6;
/* 237 */       elements.push(node);
/*     */     } 
/*     */ 
/*     */     
/* 241 */     procStack(opps, elements, -1);
/*     */     
/* 243 */     Node tree = elements.pop();
/*     */ 
/*     */ 
/*     */     
/* 247 */     StringBuffer finalExpr = new StringBuffer();
/* 248 */     tree.traverse(finalExpr);
/*     */     
/* 250 */     return finalExpr.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList inOrder(String expr, HashMap fieldMap) {
/* 260 */     LinkedList<String> list = new LinkedList();
/* 261 */     StringBuffer buf = new StringBuffer();
/*     */ 
/*     */     
/* 264 */     char[] tmp = new char[1];
/*     */     
/* 266 */     for (int i = 0; i < expr.length(); i++) {
/* 267 */       char curr = expr.charAt(i);
/*     */ 
/*     */       
/* 270 */       if (curr == '(' || curr == ')') {
/*     */         
/* 272 */         if (buf.length() != 0 && 
/* 273 */           !addToList(list, buf, fieldMap)) {
/*     */           
/* 275 */           list.add(transformToFilter(new StringBuffer(buf.toString().trim()), fieldMap));
/* 276 */           buf.setLength(0);
/*     */         } 
/*     */ 
/*     */         
/* 280 */         tmp[0] = curr;
/* 281 */         list.add(new String(tmp));
/*     */ 
/*     */       
/*     */       }
/* 285 */       else if (curr == ' ') {
/* 286 */         if (!addToList(list, buf, fieldMap)) {
/* 287 */           buf.append(curr);
/*     */         }
/*     */       } else {
/*     */         
/* 291 */         buf.append(curr);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 296 */     if (buf.length() != 0) {
/*     */       
/* 298 */       String stmp = transformToFilter(buf, fieldMap);
/* 299 */       list.add(stmp);
/*     */     } 
/*     */     
/* 302 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String transformToFilter(StringBuffer buf, HashMap fieldMap) {
/* 310 */     String stmp = buf.toString().trim();
/*     */     
/* 312 */     int like = stmp.toLowerCase().indexOf(" like ");
/* 313 */     if (like != -1) {
/* 314 */       buf.setLength(0);
/* 315 */       buf.append(stmp);
/* 316 */       System.out.println("Buff : " + like + ";" + buf.length());
/* 317 */       buf.delete(like, like + 6);
/* 318 */       buf.insert(like, '=');
/* 319 */       stmp = buf.toString();
/*     */     } 
/*     */     
/* 322 */     if (stmp.charAt(0) == '\'') {
/* 323 */       stmp = stmp.substring(1);
/*     */     }
/*     */     
/* 326 */     int equals = stmp.indexOf('=');
/* 327 */     if (equals != -1) {
/* 328 */       int quote = stmp.indexOf('\'', equals);
/* 329 */       if (quote != -1 && stmp.charAt(quote - 1) != '\\') {
/* 330 */         buf.setLength(0);
/* 331 */         buf.append(stmp);
/* 332 */         buf.delete(equals + 1, quote + 1);
/* 333 */         stmp = buf.toString();
/*     */       } 
/*     */       
/* 336 */       if (fieldMap != null) {
/* 337 */         equals = stmp.indexOf('=');
/* 338 */         String fieldName = stmp.substring(0, equals).trim();
/* 339 */         System.out.println("filedName " + fieldName);
/* 340 */         String newField = (String)fieldMap.get(fieldName);
/* 341 */         System.out.println("newfield " + newField);
/* 342 */         if (newField != null) {
/* 343 */           buf.setLength(0);
/* 344 */           buf.append(newField).append(stmp.substring(equals));
/* 345 */           stmp = buf.toString();
/* 346 */           System.out.println(stmp);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     int wc = stmp.indexOf('%');
/* 357 */     if (wc != -1) {
/* 358 */       buf.setLength(0);
/* 359 */       buf.append(stmp);
/* 360 */       if (buf.charAt(wc - 1) != '\\') {
/* 361 */         buf.setCharAt(wc, '*');
/*     */       } else {
/* 363 */         buf.deleteCharAt(wc - 1);
/* 364 */         wc--;
/*     */       } 
/*     */       
/* 367 */       wc = buf.indexOf("%", wc + 1);
/* 368 */       while (wc != -1) {
/* 369 */         if (buf.charAt(wc - 1) != '\\') {
/* 370 */           buf.setCharAt(wc, '*');
/*     */         } else {
/* 372 */           buf.deleteCharAt(wc - 1);
/* 373 */           wc--;
/*     */         } 
/* 375 */         wc = buf.indexOf("%", wc + 1);
/*     */       } 
/* 377 */       stmp = buf.toString();
/*     */     } 
/*     */     
/* 380 */     if (stmp.charAt(stmp.length() - 1) == '\'' && stmp.charAt(stmp.length() - 2) != '\\') {
/* 381 */       stmp = stmp.substring(0, stmp.length() - 1);
/*     */     }
/* 383 */     return stmp;
/*     */   }
/*     */   
/*     */   boolean addToList(LinkedList<String> list, StringBuffer buf, HashMap fieldMap) {
/* 387 */     int space = buf.toString().lastIndexOf(' ');
/* 388 */     boolean added = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     if (buf.toString().trim().equalsIgnoreCase("NOT")) {
/* 395 */       list.add(transformToFilter(buf, fieldMap));
/*     */       
/* 397 */       buf.setLength(0);
/*     */     }
/* 399 */     else if (buf.substring(space + 1).equalsIgnoreCase("AND") || buf.substring(space + 1).equalsIgnoreCase("OR") || buf.substring(space + 1).equalsIgnoreCase("NOT") || buf.substring(space + 1).equalsIgnoreCase("IS") || buf.substring(space + 1).equalsIgnoreCase("IS")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 405 */       String add = buf.substring(0, space).trim();
/* 406 */       if (add.length() != 0) {
/* 407 */         String tmp = buf.substring(0, space).trim();
/*     */         
/* 409 */         list.add(transformToFilter(new StringBuffer(tmp), fieldMap));
/*     */       } 
/*     */       
/* 412 */       list.add(buf.substring(space + 1).trim());
/*     */       
/* 414 */       buf.setLength(0);
/* 415 */       added = true;
/*     */     } 
/*     */     
/* 418 */     return added;
/*     */   }
/*     */ }


/* Location:              /Users/marcboorshtein/Downloads/jdbcLdap-1.0.0.jar!/com/octetstring/jdbcLdap/sql/SqlToLdap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */