package com.caseybrooks.logicplayground.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.caseybrooks.logicplayground.R;
import com.caseybrooks.logicplayground.evaluator.Node;
import com.caseybrooks.logicplayground.evaluator.Operator;

import java.util.LinkedList;
import java.util.Queue;

public class TreeView extends View {
	Node root;

	float density;
	Paint mFalsePaint;
	Paint mTruePaint;
	Paint mTextPaint;

	public TreeView(Context context) {
		super(context);
		init();
	}

	public TreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void setTree(Node rootNode) {
		root = rootNode;
		root.level = 0;

		this.invalidate();
	}

	private void init() {
		density = getResources().getDisplayMetrics().density;

		mFalsePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFalsePaint.setColor(getResources().getColor(R.color.button_false));

		mTruePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTruePaint.setColor(getResources().getColor(R.color.button_true));

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(getResources().getColor(R.color.abc_primary_text_material_light));
		mTextPaint.setTextSize(density*16);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int maxLevels = 0;
		int maxWidth = 0;

		int level = 1;
		int rowIndex = 1;

		Queue<Node> queue = new LinkedList<>();
		if (root != null) {
			root.level = 1;
			queue.clear();
			queue.add(root);
			while(!queue.isEmpty()) {
				Node node = queue.remove();

				int currentLevel = node.level;
				if(currentLevel != level) {
					level = currentLevel;
					rowIndex = 1;
				}

				maxWidth = (rowIndex > maxWidth) ? rowIndex : maxWidth;

				rowIndex++;

				if (node.type != Node.Type.IDENTIFIER) {
					Operator innerNode = (Operator) node;
					if(innerNode.left != null) {
						innerNode.left.level = innerNode.level + 1;
						maxLevels = (innerNode.left.level > maxLevels) ? innerNode.left.level : maxLevels;
						queue.add(innerNode.left);
					}
					if(innerNode.right != null) {
						innerNode.right.level = innerNode.level + 1;
						maxLevels = (innerNode.right.level > maxLevels) ? innerNode.right.level : maxLevels;
						queue.add(innerNode.right);
					}
				}
			}
		}

		int width = (int)(density*(maxWidth)*72 - density*8) + getPaddingLeft() + (2*getPaddingRight());
		int height = (int)(density*(maxLevels)*72 - density*8) + getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int level = 0;
		int rowIndex = 0;
		Queue<Node> queue = new LinkedList<>();

		if (root != null) {
			root.level = 0;
			queue.clear();
			queue.add(root);
			while(!queue.isEmpty()) {
				Node node = queue.remove();

				int currentLevel = node.level;
				if(currentLevel != level) {
					level = currentLevel;
					rowIndex = 0;
				}

				float posX = density*rowIndex*72 + density*32 + getPaddingLeft();
				float posY = density*level*72 + density*32 + getPaddingTop();
				float radius = (node.type == Node.Type.IDENTIFIER) ? density*32 : density*24;

				rowIndex++;

				//draw node
				if(node.value) {
					canvas.drawCircle(posX, posY, radius, mTruePaint);
				}
				else {
					canvas.drawCircle(posX, posY, radius, mFalsePaint);
				}

				String nodeText = "";

				switch(node.type) {
				case IDENTIFIER:	nodeText = node.getName(); break;
				case NOT: 			nodeText = "NOT"; break;
				case AND: 			nodeText = "AND"; break;
				case NAND: 			nodeText = "NAND"; break;
				case OR: 			nodeText = "OR"; break;
				case NOR: 			nodeText = "NOR"; break;
				case XOR: 			nodeText = "XOR"; break;
				case XNOR: 			nodeText = "XNOR"; break;
				}

				canvas.drawText(nodeText, posX, posY, mTextPaint);

				if (node.type != Node.Type.IDENTIFIER) {
					Operator innerNode = (Operator) node;
					if(innerNode.left != null) {
						innerNode.left.level = innerNode.level + 1;
						queue.add(innerNode.left);
					}
					if(innerNode.right != null) {
						innerNode.right.level = innerNode.level + 1;
						queue.add(innerNode.right);
					}
				}
			}
		}

	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// Try for a width based on our minimum
//		int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//		int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//
//		// Whatever the width ends up being, ask for a height that would let the pie
//		// get as big as it can
//		int minh = MeasureSpec.getSize(w) - (int)(root.getHeight()*(getResources().getDisplayMetrics().density)) + getPaddingBottom() + getPaddingTop();
//		int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)(root.getHeight()*(getResources().getDisplayMetrics().density)), heightMeasureSpec, 0);
//
//		setMeasuredDimension(w, h);
//	}
}
