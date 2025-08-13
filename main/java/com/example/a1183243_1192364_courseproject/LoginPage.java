package com.example.a1183243_1192364_courseproject;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.AnticipateOvershootInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finish();
            return;
        }

        // Create a vertical LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(32, 64, 32, 32);
        linearLayout.setBackgroundColor(Color.parseColor("#F0F8FF")); // Light blue background

        // App Title
        TextView titleTextView = new TextView(this);
        titleTextView.setText("TODO APP");
        titleTextView.setTextSize(24);
        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextView.setTextColor(Color.parseColor("#4682B4")); // Steel Blue
        titleTextView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.bottomMargin = 64;
        titleTextView.setLayoutParams(titleParams);
        titleTextView.setAlpha(0f); // Start invisible for animation

        // Login Button
        Button logInButton = createStyledButton("LOG IN", ContextCompat.getColor(this, android.R.color.white));
        logInButton.setAlpha(0f); // Start invisible for animation
        logInButton.setOnClickListener(v -> {
            // Redirect to LoginActivity to enter credentials
            startActivity(new Intent(LoginPage.this, LoginActivity.class));
        });

        // Signup Button
        Button signUpButton = createStyledButton("SIGN UP", ContextCompat.getColor(this, android.R.color.white));
        signUpButton.setAlpha(0f); // Start invisible for animation
        signUpButton.setOnClickListener(v -> {
            // Redirect to SignupActivity for registration
            startActivity(new Intent(LoginPage.this, SignupActivity.class));
        });

        // Add views to layout
        linearLayout.addView(titleTextView);

        // Add some spacing between buttons
        View spacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                32
        );
        spacer.setLayoutParams(spacerParams);

        linearLayout.addView(logInButton);
        linearLayout.addView(spacer);
        linearLayout.addView(signUpButton);
        setContentView(linearLayout);

        // Animate the views
        postLayoutAnimation(titleTextView, logInButton, signUpButton);
    }

    private Button createStyledButton(String text, int textColor) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextColor(textColor);
        button.setTextSize(16);

        // Custom button style
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_background));
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add some elevation for depth
        button.setElevation(8);

        return button;
    }

    private void postLayoutAnimation(View... views) {
        // Ensure the layout is fully drawn before starting animations
        views[0].post(() -> {
            // Animate each view with a staggered entry
            for (int i = 0; i < views.length; i++) {
                // Scale and Fade in Animation
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(views[i], "scaleX", 0.5f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(views[i], "scaleY", 0.5f, 1f);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(views[i], "alpha", 0f, 1f);

                // Translate Animation (slide up)
                ObjectAnimator translateY = ObjectAnimator.ofFloat(views[i], "translationY", 100f, 0f);

                // Animator Set
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY, fadeIn, translateY);

                // Use different interpolators for more dynamic feel
                animatorSet.setInterpolator(new AnticipateOvershootInterpolator());

                // Stagger the animations
                animatorSet.setStartDelay(i * 200);
                animatorSet.setDuration(800);
                animatorSet.start();
            }
        });
    }
}

//public class LoginPage extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Check if the user is already logged in
//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            // If logged in, redirect to MainActivity
//            startActivity(new Intent(LoginPage.this, MainActivity.class));
//            finish(); // Close the LoginPage
//            return;
//        }
//
//        // Setup the login/signup UI programmatically
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        ));
//
//        // Login Button
//        Button logInButton = new Button(this);
//        logInButton.setText("LOG IN");
//        logInButton.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        logInButton.setOnClickListener(v -> {
//            // Redirect to LoginActivity to enter credentials
//            startActivity(new Intent(LoginPage.this, LoginActivity.class));
//        });
//
//        // Signup Button
//        Button signUpButton = new Button(this);
//        signUpButton.setText("SIGN UP");
//        signUpButton.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        signUpButton.setOnClickListener(v -> {
//            // Redirect to SignupActivity for registration
//            startActivity(new Intent(LoginPage.this, SignupActivity.class));
//        });
//
//        // Add buttons to layout
//        linearLayout.addView(logInButton);
//        linearLayout.addView(signUpButton);
//        setContentView(linearLayout);
//    }
//}
