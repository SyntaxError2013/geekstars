package in.syntaxerror.jogging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HomeActivity extends Activity implements OnClickListener {

	private Button okButton = null;
	private EditText weightTextField = null;
	Editable weight;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        okButton = (Button) findViewById(R.id.weightButton);
        weightTextField = (EditText) findViewById(R.id.weightTextField);
        
        weight= weightTextField.getText(); 
        
        
        okButton.setOnClickListener(this);
        
        
    }

	@Override
	public void onClick(View v) {
		
		String x=weight.toString();
		  
        if(!x.isEmpty()){
			Intent i= new Intent(HomeActivity.this,MapActivity.class);
			 i.putExtra("weight", ""+weight); 
			startActivity(i);}
        
        else
     	   Toast.makeText(getApplicationContext(), "Weight field is empty, please fill it before proceeding forward", Toast.LENGTH_LONG).show();
		
	}


}
