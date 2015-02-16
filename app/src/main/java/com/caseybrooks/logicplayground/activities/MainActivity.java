package com.caseybrooks.logicplayground.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.caseybrooks.logicplayground.R;
import com.caseybrooks.logicplayground.fragments.InteractiveFragment;


public class MainActivity extends ActionBarActivity {
	Context context;

	Toolbar tb;
	ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.context = this;

		getFragmentManager().beginTransaction()
				.add(R.id.container, (Fragment) InteractiveFragment.newInstance())
				.commit();

		tb = (Toolbar) findViewById(R.id.activity_toolbar);
		setSupportActionBar(tb);

		ab = getSupportActionBar();
		ab.setTitle("Logic PlayGround");
	}
}
