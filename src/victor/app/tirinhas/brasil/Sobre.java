package victor.app.tirinhas.brasil;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Sobre extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sobre);
		Button botao = (Button) findViewById(R.id.button1);
		botao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("market://details?id=" + getPackageName());
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(i);
					Toast.makeText(getApplicationContext(), "Obrigado!", Toast.LENGTH_LONG).show();
				}
				catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Oops, deu merda", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

}
