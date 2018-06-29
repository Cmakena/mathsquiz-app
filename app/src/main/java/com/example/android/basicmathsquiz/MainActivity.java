package com.example.android.basicmathsquiz;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //score is a variable to track quiz scores
    int score = 0;

    //Array of answers, answer for question one is answers[1-1], answer for question 2 is answers[2-1] etc
    int[] answers = {9,5,4,10,11,5,4,3,11,4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cast the answer choices so we can be able to hide one and show the other
        final RadioGroup rg = (RadioGroup) findViewById(R.id.radio);
        final EditText et = (EditText) findViewById(R.id.answer);
        final LinearLayout rch = (LinearLayout) findViewById(R.id.checkoxes);

        //Here, we make the app to show only checkboxes on startup( first question uses checkboxes)
        rg.setVisibility(View.GONE);
        et.setVisibility(View.GONE);
        rch.setVisibility(View.VISIBLE);
    }

    public void questionRouter(View v){
        //cast the question number
        TextView questionNumber = (TextView) findViewById(R.id.question_number);
        int qNumber = Integer.parseInt(questionNumber.getText().toString());
        // answer initialization
        int userAnswer = 0;
        // cast Edit text field
        EditText answerValue = (EditText) findViewById(R.id.answer);

        // Radio group
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio);
        // The check boxes
        LinearLayout rch = (LinearLayout) findViewById(R.id.checkoxes);

        // Individual checkbox checking
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkbox_one);
        boolean cb1 = checkbox1.isChecked();
        CheckBox checkbox2 = (CheckBox) findViewById(R.id.checkbox_two);
        boolean cb2 = checkbox2.isChecked();
        CheckBox checkbox3 = (CheckBox) findViewById(R.id.checkbox_three);
        boolean cb3 = checkbox3.isChecked();
        CheckBox checkbox4 = (CheckBox) findViewById(R.id.checkbox_four);
        boolean cb4 = checkbox4.isChecked();

        // Radio button checking
        RadioButton radio1 = (RadioButton) findViewById(R.id.radio_one);
        boolean rb1 = radio1.isChecked();
        RadioButton radio2 = (RadioButton) findViewById(R.id.radio_two);
        boolean rb2 = radio2.isChecked();
        RadioButton radio3 = (RadioButton) findViewById(R.id.radio_three);
        boolean rb3 = radio3.isChecked();
        RadioButton radio4 = (RadioButton) findViewById(R.id.radio_four);
        boolean rb4 = radio4.isChecked();

        // Questions 1,2,and 6 uses checkboxes, radio , radio respectively, so get the value of Edit text if not these numbers
        if (qNumber!=1 & qNumber!=2 & qNumber!=6){
            // Find the user answer, If null we assume no input and it defaults to 0
            if (answerValue.getText().toString().matches(""))
                userAnswer = 0;
            else userAnswer = Integer.parseInt(answerValue.getText().toString());
        }
        // If it's question 1, we check if checkbox 1 and 3 are the only selected options
        if (qNumber ==1){
            if (cb2 | cb4)
                userAnswer = 0;
            else if (cb1&cb3)
                userAnswer = 9;
        }
        // if it's question 2 or 6, the answer is 5 which is radio three
        if (qNumber == 2 | qNumber==6){
            if (rb3)
                userAnswer = 5;
            else userAnswer = 0;
        }
        //find the question text by casting the view
        TextView question = (TextView) findViewById(R.id.question);
        // Application context
        Context context = getApplicationContext();
        // while the question number is less than 10,record the score, change the question text, answer value and question number
        if (qNumber<10) {
            if (verifyAnswer(qNumber,userAnswer)){
                score += 2;
                Toast toast = Toast.makeText(context, "Correct +2", Toast.LENGTH_SHORT);
                View view = toast.getView();
                TextView text = (TextView) view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#FF25B308"));
                toast.show();
            }
            else if(!verifyAnswer(qNumber, userAnswer)){
                Toast toast = Toast.makeText(context, "Wrong", Toast.LENGTH_SHORT);
                View view = toast.getView();
                TextView text = (TextView) view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#FFD40416"));
                toast.show();
            }
            Log.i("MainActivity",""+nextQuestion(qNumber+1));
            question.setText(nextQuestion(qNumber+1));
            questionNumber.setText(""+(qNumber+1));
            // If the question number is 1 or five, we expect the next question to be answered with radioButtons,
            // so hide checkboxes and editText
            if (qNumber == 1 | qNumber ==5){
                rch.setVisibility(View.GONE);
                answerValue.setVisibility(View.GONE);
                rg.setVisibility(View.VISIBLE);
            }
            if (qNumber == 6){
                rg.clearCheck();
                rg.setVisibility(View.GONE);
                answerValue.setVisibility(View.VISIBLE);
            }
            if (qNumber == 2){
                answerValue.setVisibility(View.VISIBLE);
                rg.clearCheck();
                rg.setVisibility(View.GONE);
            }
            answerValue.setText("");
            answerValue.setHint("Your Answer");
            return;
        }
        if (qNumber==10){
            if (verifyAnswer(qNumber,userAnswer)){
                score += 2;
            }
            questionNumber.setText("");
            String result = "Your score is "+score;
            if (score >= 15){
                result = result+". \n Congratulations, this is fantastic";
                Toast toast = Toast.makeText(context, "Score "+score+". Fantastic!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                result = result+". \n Practice more, practice makes perfect";
                Toast toast = Toast.makeText(context, "Score "+score+". Practice more", Toast.LENGTH_SHORT);
                toast.show();
            }

            question.setText(result);
            answerValue.setVisibility(View.GONE);
            //Hide the submit button
            Button btn = (Button) findViewById(R.id.submitBtn);
            btn.setVisibility(View.GONE);
        }

    }

    /**
     * Verifies if the answer is correct
     *
     * @param questionNumber is the question number asked
     * @param answerGiven is the answer the user gave
     * @return boolean
     */
    private boolean verifyAnswer(int questionNumber, int answerGiven){
        return answers[questionNumber-1] == answerGiven;
    }

    /**
     * This method helps find the next question
     * @param questionNumber the number of the question we want
     * @return the question we want
     */
    private String nextQuestion(int questionNumber){
        String question ="";
        switch(questionNumber){
            case 1: question = "What is the value of x in this equation 3x = 27. Select all that applies";
                return question;
            case 2: question = "What is the square root of 25";
                return question;
            case 3: question = "Solve for x, 4x = 16";
                return question;
            case 4: question = "Solve for x, 2x = 20";
                return question;
            case 5: question = "Solve for a, 2a-2 = 20";
                return question;
            case 6: question = "What's the absolute value of -5";
                return question;
            case 7: question = "Find x if 4x-2 =14";
                return question;
            case 8: question = "Solve for y, 3y = 9";
                return question;
            case 9: question = "Solve for y, 2y+3 = 25";
                return question;
            case 10: question = "What's the square root of 16";
                return question;
        }
        return question;
    }
}
