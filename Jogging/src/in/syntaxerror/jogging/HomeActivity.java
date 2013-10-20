package in.syntaxerror.jogging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends Activity implements OnClickListener {

	private Button okButton = null;
	private EditText weightTextField = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        okButton = (Button) findViewById(R.id.weightButton);
        weightTextField = (EditText) findViewById(R.id.weightTextField);
        
        okButton.setOnClickListener(this);
        
        
    }

	@Override
	public void onClick(View v) {
		
		String weightString = weightTextField.getEditableText().toString();
		
		if(weightString == null || weightString.equals("")) return;
		
		Intent mapActivity = new Intent(HomeActivity.this,MapActivity.class);
		
		startActivity(mapActivity);
		
	}


}
