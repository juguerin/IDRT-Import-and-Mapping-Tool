// ============================================================================
//
// Copyright (c) 2005-2012, Talend Inc.
//
// This source code has been automatically generated by_Talend Open Studio for Big Data
// CodeGenerator version 5.2.0.M4_r90320
// You can find more information about Talend products at www.talend.com.
// You may distribute this code under the terms of the GNU LGPL license
// http://www.gnu.org/licenses/lgpl.html).
//
// ============================================================================
package routines;

public class Relational {

    /**
     * ISNULL( ) Indicates when a variable is the null value.
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {Category} Relational
     * 
     * {param} Object(null)
     * 
     * {example} ISNULL(null)
     * 
     * 
     */
    public static boolean ISNULL(Object variable) {
        return variable == null;
    }

    /**
     * NOT( ) Returns the complement of the logical value of an expression.
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {Category} Relational
     * 
     * {param} boolean(true)
     * 
     * {example} NOT(false)
     */
    public static boolean NOT(boolean expression) {
        return !expression;
    }
}
