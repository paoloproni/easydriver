/*
 * EasyDriver is a library that let a programmer build queries easier
 * than using plain JDBC.
 * Copyright (C) 2011 Paolo Proni
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.byteliberi.easydriver.expressions;

import org.byteliberi.easydriver.ExpressionAPI;
import java.util.LinkedList;
import java.util.List;
import org.byteliberi.easydriver.TableField;

/**
 * A <code>DualOperator</code> is a logical or mathematical operator
 * which manages two data. For example: =, <>, AND, OR etc...
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class DualOperator implements ExpressionAPI {

	/**
	 * String representation of what lays at the left of the operator.
	 */
	private String left;
    
	/**
	 * String representation of the operator.
	 */
	private String operator;
	
	/**
	 * String representation of what lays at the right of the operator.
	 */
	private String right;
    
	/**
	 * JDBC placeholder for a parameter
	 */
	private final static String PARAM_PLACEHOLDER = "?";
	
	/**
	 * If it is true, the query string part will be enclosed between ( and )
	 */
	private boolean parentheses = false;

	/**
	 * List of the parameters which are included in this operator
	 */
	protected List<TableField<?>> parameterManagerList = new LinkedList<TableField<?>>();

	/**
	 * Creates a new instance of this class.
	 * @param field Field that lays at the left side of the operator
	 * @param operator String operator
	 */
	public DualOperator(final TableField<?> field, final String operator) {
		this(field, operator, true);
	}
	
	public DualOperator(final TableField<?> field, final String operator, final boolean useCompleteName) {
		if (useCompleteName)
			this.left = field.getCompleteName();
		else
			this.left = field.getName();
		this.operator = operator;
		this.right = PARAM_PLACEHOLDER;
		this.parameterManagerList.add(field);
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param field Field that lays at the left side of the operator.
	 * @param operator String operator.
	 * @param right String that lays at the left side of the operator
	 */
	public DualOperator(final TableField<?> field, final String operator, final String right) {
		this.left = field.getCompleteName();
		this.operator = operator;
		this.right = right;
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param left String which lays at the left side of the operator.
	 * @param operator String operator.
	 * @param right String which lays at the right side of the operator.
	 */
	public DualOperator(final String left, final String operator, final String right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	/**
	 * Creates a new instance of this class.
	 * @param left Expression which lays at the left side of the operator.
	 * @param operator String operator.
	 * @param right Expression which lays at the right side of the operator.
	 */
	public DualOperator(final ExpressionAPI left, final String operator, final ExpressionAPI right) {
		this.left = left.createString();
		this.operator = operator;
		this.right = right.createString();
	}
	
	protected void setLeft(final String leftPart) {
		this.left = leftPart;
	}
	
	protected void setOperator(final String operatorPart) {
		this.operator = operatorPart;
	}
	
	protected void setRight(final String rightPart) {
		this.right = rightPart;
	}

//	/**
//	 * Creates a new instance of this class.
//	 * @param left String which lays at the left side of the operator.
//	 * @param operator String operator.
//	 * @param right String which lays at the right side of the operator.
//	 * @param parameters Parameter list
//	 */
//	private DualOperator(final String left, final String operator, final String right,
//						final List<TableField<?>> parameters) {
//		this.left = left;
//		this.operator = operator;
//		this.right = right;
//		this.parameterManagerList = parameters;
//	}

	@Override
	public String createString() {
		final StringBuilder sb = new StringBuilder(this.left.length() + this.operator.length() + this.right.length());
		if (this.parentheses)
			sb.append('(').append(this.left).append(") ")
				.append(this.operator)
				.append(" (").append(this.right).append(')');
		else
			sb.append(this.left).append(this.operator).append(this.right);
		return sb.toString();
	}

	/**
	 * Getter of the parameter list.
	*/
	public List<TableField<?>> getParameters() {
		return this.parameterManagerList;
	}

	/**
	* Checks if the operator expression has to be included between ( and ).
	* @return true if the expression is inside ( and )
	*/
	public final boolean isParentheses() {
		return this.parentheses;
	}

	/**
	* Set if the operator expression has to be included between ( and ).
	* @param parentheses true if the expression is inside ( and )
	*/
	public final void setParentheses(final boolean parentheses) {
		this.parentheses = parentheses;
	}

//	/**
//	* This inner class is used to make a Builder pattern, in order to
//	* create a DualOperator without incongruencies.
//	* @author Paolo Proni
//	* @since 1.0
//	* @version 1.0
//	*/
//	public class Builder {
//		/**
//		 * String representation of what lays at the left of the operator.
//		 */
//		private String left;
//
//		/**
//		 * String representation of the operator.
//		 */
//		private String operator;
//
//		/**
//		 * String representation of what lays at the right of the operator.
//		 */
//		private String right;
//
//		/**
//		 * List of the parameters which are included in this operator
//		 */
//		private List<TableField<?>> parameters = new LinkedList<TableField<?>>();
//
//		/**
//		 * Set what stays at the left of the operator.
//		 * @param field which lays at the left side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setLeft(TableField<?> field) {
//			this.left = field.getCompleteName();
//			return this;
//		}
//
//		/**
//		 * Set what stays at the left of the operator.
//		 * @param value which lays at the left side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setLeft(String value) {
//			this.left = value;
//			return this;
//		}
//
//		/**
//		 * Set what stays at the left of the operator.
//		 * @param value which lays at the left side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setLeft(ExpressionAPI value) {
//			this.left = value.createString();
//			return this;
//		}
//
//		/**
//		 * Set what stays at the right of the operator.
//		 * @param field which lays at the right side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setRight(TableField<?> field) {
//			this.right = field.getCompleteName();
//			return this;
//		}
//
//		/**
//		 * Set what stays at the right of the operator.
//		 * @param value which lays at the right side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setRight(String value) {
//			this.right = value;
//			return this;
//		}
//
//		/**
//		 * Set what stays at the right of the operator.
//		 * @param value which lays at the right side of the operator.
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setRight(ExpressionAPI value) {
//			this.right = value.createString();
//			return this;
//		}
//
//		/**
//		 * Set what stays at the right of the operator.
//		 * @param operator
//		 * @return Builder object that let the caller to continue to build the
//		 * DualOperator with subsequent calls.
//		 */
//		public Builder setOperator(String operator) {
//			this.operator = operator;
//			return this;
//		}
//
//		/**
//		 * Creates a new instance of <pre>DualOperator</pre> class.
//		 * @return Newly created instance of the DualOperator
//		 */
//		public DualOperator build() {
//			return new DualOperator(this.left, this.operator, this.right, this.parameters);
//		}
//	}
}
