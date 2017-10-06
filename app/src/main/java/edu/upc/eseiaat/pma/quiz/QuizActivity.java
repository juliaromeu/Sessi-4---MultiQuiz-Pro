package edu.upc.eseiaat.pma.quiz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class QuizActivity extends AppCompatActivity {

    private int ids_answers[]={
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    //Correct answer será un campo
    private int correct_answer, current_question;
    private String[] all_questions;
    private boolean[] answer_is_correct;
    private int[] answer;
    private TextView text_question;
    private RadioGroup group;
    private Button btn_next, btn_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Campos:
        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup)findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        all_questions=getResources().getStringArray(R.array.all_questions);
        startOver();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();

                if(current_question < all_questions.length-1) {
                    current_question++;
                    showQuestion();
                } else {
                    Check_results();

                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                if (current_question>0){
                    current_question--;
                    showQuestion();
                }
            }
        });

    }

    private void startOver() {
        answer_is_correct = new boolean[all_questions.length];
        answer = new int[all_questions.length];
        for (int i=0; i<answer.length; i++){
            answer[i] = -1;

        }
        current_question = 0;
        showQuestion();
    }

    private void Check_results() {
        int correctas = 0, incorrectas = 0, nocontestadas=0;
        for (int i=0; i<all_questions.length; i++) {
            if (answer_is_correct[i]) correctas++;
            else if (answer[i]==-1) nocontestadas++;
            else incorrectas++;
        }

        //TODO: Permitir traducción de este texto:
        String message =
                String.format("Correctas: %d\nIncorrectas: %d\nNo contestadas: %d\n",
                        correctas, incorrectas, nocontestadas);

        //Constructor de un cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Qué queremos en el cuadro de diálogo
        builder.setTitle(R.string.results);
        builder.setMessage(message);
        //Evitar poder tirar atrás en el último cuadro de diálogo
        builder.setCancelable(false);
        //Positive Button (OK)
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });
        //Negative Button (Cancelar-Acabar)
        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Borrar respuestas y volver a la pregunta 0
                startOver();

            }
        });

        //Crear el cuadro de diálogo
        builder.create().show();

    }

    private void checkAnswer() {
        int id = group.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < ids_answers.length; i++) {
            if (ids_answers[i] == id) {
                ans = i;
            }
        }
        answer_is_correct[current_question]=(ans == correct_answer);
        answer[current_question] = ans;
    }

    private void showQuestion() {
        String q= all_questions[current_question];
        //Partimos un string en trozos (las respuestas)
        String[] parts=q.split(";");

        group.clearCheck();

        text_question.setText(parts[0]);
        for (int i=0;i<ids_answers.length; i++){
            RadioButton rb=(RadioButton) findViewById(ids_answers[i]);
            //Para saber cual es la respuesta correcta
            String ans=parts[i+1];
            //Chart At position 0: miramos la primera letra de la respuesta
            //Mira si es correcta
            if(ans.charAt(0)=='*'){
                correct_answer=i;
                ans=ans.substring(1);
            }
            //Mira si está marcada
            rb.setText(ans);
            if (answer[current_question] ==i){
            rb.setChecked(true);
            }
        }

        if (current_question == 0) {
            btn_prev.setVisibility(View.GONE);
        } else {
            btn_prev.setVisibility(View.VISIBLE);
        }
        if(/*última pregunta*/ current_question == all_questions.length-1){
            btn_next.setText(R.string.finish);
        } else {
            btn_next.setText(R.string.next);
        }
    }
}