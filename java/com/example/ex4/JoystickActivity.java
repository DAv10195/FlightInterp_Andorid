package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity implements Joystick.JoystickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }
    //method to handle OnJoystickMoved event
    public void onJoystickMoved(float xPercent, float yPercent, int source)
    {
        UserHandler handler = UserHandler.getInstance();
        handler.send_message(true, xPercent); //true is aileron
        handler.send_message(false, yPercent); //false is elevator
    }
}