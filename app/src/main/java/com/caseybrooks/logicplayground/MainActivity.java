package com.caseybrooks.logicplayground;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.caseybrooks.logicplayground.evaluator.Identifier;
import com.caseybrooks.logicplayground.evaluator.Node;
import com.caseybrooks.logicplayground.evaluator.Parser;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

	EditText enterExpression;
	Button parseExpressionButton;

	ListView toggleButtonList;
	ToggleAdapter toggleAdapter;

	Node root;
	boolean output;
	FrameLayout fab;
	TextView fab_text;

	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.context = this;

		fab = (FrameLayout) findViewById(R.id.fab);
		fab_text = (TextView) findViewById(R.id.fab_text);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				evaluateExpression();
			}
		});

		toggleButtonList = (ListView) findViewById(R.id.toggleButtonList);
		toggleAdapter = new ToggleAdapter(new ArrayList<ToggleButton>());
		toggleButtonList.setAdapter(toggleAdapter);
		toggleButtonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToggleButton button = (ToggleButton) view;
				button.toggle();

				Node identifier = root.find(button.getTextOn().toString());
				if(identifier != null) {
					identifier.value = button.isChecked();

					evaluateExpression();
				}
				else {
					Toast.makeText(context, button.getTextOn().toString() + " not found in expression", Toast.LENGTH_SHORT).show();
				}
			}
		});

		enterExpression = (EditText) findViewById(R.id.editText);
		parseExpressionButton = (Button) findViewById(R.id.parseExpressionButton);
		parseExpressionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String expression = enterExpression.getText().toString();
				if(expression.length() > 0) {
					Parser parser = new Parser(expression);
					root = parser.parse();

					if(root == null) {
						Toast.makeText(getApplicationContext(), "Cannot parse string, not formatted correctly", Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(), "String was parsed correctly", Toast.LENGTH_SHORT).show();
						setup();
					}
				}
			}
		});
//		setup();
	}

	public void setup() {
		Set<Identifier> identifiers = root.getAllIdentifiers();
		toggleAdapter.clear();

		for(Identifier identifier : identifiers) {

			ToggleButton toggle = new ToggleButton(this);
			toggle.setText(identifier.getName());
			toggle.setTextOff(identifier.getName());
			toggle.setTextOn(identifier.getName());
			toggle.setFocusable(false);
			toggle.setFocusableInTouchMode(false);
			toggle.setClickable(false);

			toggleAdapter.addItem(toggle);
		}

		enterExpression.setText(root.getName());


//		setInput("a");
//		setInput("b");
//		setInput("c");
//		setInput("d");
//		setInput("e");
//		setInput("f");
//		setInput("g");
//		setInput("h");
//
//		Operator a_and_b = new Operator(Node.Type.NAND, new Identifier("a"),  new Identifier("b"));
//		Operator c_and_d = new Operator(Node.Type.NOR, new Identifier("c"),  new Identifier("d"));
//		Operator e_and_f = new Operator(Node.Type.XOR, new Identifier("e"),  new Identifier("f"));
//		Operator g_and_h = new Operator(Node.Type.XNOR, new Identifier("g"),  new Identifier("h"));
//
//		Operator left = new Operator(Node.Type.OR, a_and_b, c_and_d);
//		Operator right = new Operator(Node.Type.OR, e_and_f, g_and_h);
//		root = new Operator(Node.Type.OR, left, right).evaluate();
	}

	public void evaluateExpression() {
		Node y = root.evaluate();

		enterExpression.setText(y.getName());
		setOutput(y.value);
	}

	public class ToggleAdapter extends BaseAdapter {
		ArrayList<ToggleButton> buttons;

		public ToggleAdapter(ArrayList<ToggleButton> buttons) {
			this.buttons = buttons;
		}

		public void addItem(ToggleButton tb) {
			buttons.add(tb);
			notifyDataSetChanged();
		}

		public void clear() {
			buttons.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return buttons.size();
		}

		@Override
		public Object getItem(int position) {
			return buttons.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return buttons.get(position);
		}
	}

//	public void setInput(String name) {
//			String input = name.toLowerCase();
//
//			ToggleButton toggle = new ToggleButton(this);
//			toggle.setText(input);
//			toggle.setTextOff(input);
//			toggle.setTextOn(input);
//			toggle.setFocusable(false);
//			toggle.setFocusableInTouchMode(false);
//			toggle.setClickable(false);
//
//			toggleAdapter.addItem(toggle);
//	}

	public void setOutput(boolean state) {
		output = state;
		if(state) {
			fab.setBackgroundResource(R.drawable.circle_on);
			fab_text.setText("1");
		}
		else {
			fab.setBackgroundResource(R.drawable.circle_off);
			fab_text.setText("0");
		}
	}
}
