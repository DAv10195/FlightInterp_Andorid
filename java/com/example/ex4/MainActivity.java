package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assign the onClick as the one to be executed once the connect button is pressed
        Button connect_button = findViewById(R.id.connect_button);
        connect_button.setOnClickListener(this);
    }
    //onClick method of the connect button
    public void onClick(View view)
    {
        EditText[] values = new EditText[]{findViewById(R.id.ip_val),findViewById(R.id.port_val)};
        UserHandler.getInstance().connect(values[0].getText().toString(),
                Integer.parseInt(values[1].getText().toString()));
        startActivity(new Intent(this, JoystickActivity.class));
    }
}
