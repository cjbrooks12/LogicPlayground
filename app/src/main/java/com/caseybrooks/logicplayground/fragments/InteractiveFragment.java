package com.caseybrooks.logicplayground.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.caseybrooks.logicplayground.R;
import com.caseybrooks.logicplayground.evaluator.Identifier;
import com.caseybrooks.logicplayground.evaluator.Node;
import com.caseybrooks.logicplayground.evaluator.Parser;
import com.caseybrooks.logicplayground.views.TreeView;

import java.util.ArrayList;
import java.util.Set;

public class InteractiveFragment extends Fragment {

	EditText enterExpression;
	Button parseExpressionButton;

	GridView toggleButtonList;
	ToggleAdapter toggleAdapter;

	Node root;
	boolean output;
	FrameLayout fab;
	TextView fab_text;

	TreeView treeview;

	Context context;

	public static InteractiveFragment newInstance() {
		InteractiveFragment fragment = new InteractiveFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public InteractiveFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_interactive, container, false);

		this.context = getActivity();

		setHasOptionsMenu(true);

		treeview = (TreeView) view.findViewById(R.id.treeview);

		fab = (FrameLayout) view.findViewById(R.id.fab);
		fab_text = (TextView) view.findViewById(R.id.fab_text);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				evaluateExpression();
			}
		});

		toggleButtonList = (GridView) view.findViewById(R.id.toggleButtonList);
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

		enterExpression = (EditText) view.findViewById(R.id.editText);
		parseExpressionButton = (Button) view.findViewById(R.id.parseExpressionButton);
		parseExpressionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String expression = enterExpression.getText().toString();
				if(expression.length() > 0) {
					Parser parser = new Parser(expression);
					root = parser.parse();

					if(root != null) {
						setup();
						evaluateExpression();
					}
				}
			}
		});

		return view;
	}

	public void setup() {
		Set<Identifier> identifiers = root.getAllIdentifiers();
		toggleAdapter.clear();

		for(Identifier identifier : identifiers) {

			ToggleButton toggle = new ToggleButton(context);
			toggle.setText(identifier.getName());
			toggle.setTextOff(identifier.getName());
			toggle.setTextOn(identifier.getName());
			toggle.setFocusable(false);
			toggle.setFocusableInTouchMode(false);
			toggle.setClickable(false);

			float density = getResources().getDisplayMetrics().density;
			toggle.setPadding((int)(8*density), 0, (int)(8*density), 0);

			toggleAdapter.addItem(toggle);
		}

		evaluateExpression();
	}

	public void evaluateExpression() {
		root.evaluate();
		treeview.setTree(root);

		enterExpression.setText(root.getName());
		setOutput(root.value);
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
