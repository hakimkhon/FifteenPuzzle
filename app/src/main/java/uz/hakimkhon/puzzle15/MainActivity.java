package uz.hakimkhon.puzzle15;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uz.hakimkhon.puzzle15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Integer> numbers = new ArrayList<>();
    int emptyBtnIndexX, emptyBtnIndexY, move = 0, counter = 1;
    long pauseOffset;
    boolean running = true;
    Button emptyBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadNumbers();
        generateNumbers();
        colorButtons();

    }
    private void loadNumbers(){
        for (int i = 1; i <= 16; i++){
            numbers.add(i);
        }
    }
    private void generateNumbers(){
        //dastur ishga tushganda va reset bosilganda urinishlar va vaqtni 0 ga qaytarish
        move = 0;
        binding.chronometer.setBase(SystemClock.elapsedRealtime());
        binding.chronometer.stop();
        binding.chronometer.start();
        do {
            Collections.shuffle(numbers);
        } while (!isSolvable(numbers));
        for (int i = 0; i < binding.gridLayout.getChildCount(); i++){
            if (numbers.get(i) == 16) {
                //tag 16 ga teng bo'lganda bush btnni indeskini topamiz va textni "" ga tenglaymiz
                String tag = binding.gridLayout.getChildAt(i).getTag().toString();
                emptyBtnIndexX = tag.charAt(0) - '0';
                emptyBtnIndexY = tag.charAt(1) - '0';
                ((Button) binding.gridLayout.getChildAt(i)).setText("");
                emptyBtn = ((Button) binding.gridLayout.getChildAt(i));
                emptyBtn.setVisibility(View.INVISIBLE);
            }
            else {
                ((Button) binding.gridLayout.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
            }
        }
    }
    // yutsa bo'lishligini tekshirish
    private boolean isSolvable(List<Integer> numbers){
        int counter = 0;
        for (int i = 0; i < numbers.size(); i++){
            if (numbers.get(i) == 16){
                counter += (i / 4 + 1);
                continue;
            }
            for (int j = i + 1; j < numbers.size(); j++){
                if (numbers.get(i) > numbers.get(j))
                    counter++;
            }
        }
        return counter % 2 == 0;
    }
    private boolean canMove(int clickedX, int clickedY){
//        binding.btnRestart.setBackgroundColor(Color.parseColor("#7FFFD4"));
        return Math.abs((clickedX + clickedY) - (emptyBtnIndexX + emptyBtnIndexY)) == 1 &&
               Math.abs(clickedX - emptyBtnIndexX) != 2 && Math.abs(clickedY - emptyBtnIndexY) != 2;
    }
    private boolean gameOver(){
        colorButtons();
        for (int i = 0; i <= 15; i++){
            Button checker = (Button) binding.gridLayout.getChildAt(i);
            if (checker.getText().toString().isEmpty()) break;
            if (Integer.parseInt(checker.getText().toString()) != counter)
                return false;
            else counter++;
            if (counter == 16)
                return true;
        }
        return false;
    }
    private void colorButtons(){
        for (int i = 0; i <= 15; i++){
            Button checker = (Button) binding.gridLayout.getChildAt(i);
            if (checker.getText().toString().equals(String.valueOf(i+1))){
                binding.gridLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#ADFF2F"));
            }
            else
                binding.gridLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#FAEBD7"));
        }
    }
    //region swap
    private void swap(int clickedX, int clickedY, Button clicked){
        String text = clicked.getText().toString();
        clicked.setText("");
        clicked.setVisibility(View.INVISIBLE);
        emptyBtn.setVisibility(View.VISIBLE);
        emptyBtn.setText(text);
        emptyBtn = clicked;
        emptyBtnIndexX = clickedX;
        emptyBtnIndexY = clickedY;
    }
    //endregion
    //region start_stop_reset
    public void reStart(View view){
        emptyBtn.setVisibility(View.VISIBLE);
        generateNumbers();
        colorButtons();
        updateMove(move);
    }
    @SuppressLint("SetTextI18n")
    public void pauseAndStart(View view) {
        if (running) {
            binding.btnPause.setText("start");
            running = false;
            onStop();
            binding.btnPause.setBackgroundColor(Color.parseColor("#F08080"));
        } else {
            binding.btnPause.setText("pause");
            binding.btnPause.setBackgroundColor(Color.parseColor("#7FFFD4"));
            running = true;
            onStart();
        }
    }

    @Override
    protected void onStop() {
        binding.chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - binding.chronometer.getBase();
        super.onStop();
    }

    @Override
    protected void onStart() {
        binding.chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        binding.chronometer.start();
        super.onStart();
    }

    //endregion
    public void onClickSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //region onClick
    @SuppressLint("SetTextI18n")
    public void onClick(View view){
        if (!running){
            binding.btnPause.setBackgroundColor(Color.parseColor("#7FFFD4"));
            binding.chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            binding.chronometer.start();
        }
        counter = 1;
        binding.btnPause.setText("pause");
        Button clicked = (Button) view;
        String tag = view.getTag().toString();
        int clickedX = tag.charAt(0) - '0';
        int clickedY = tag.charAt(1) - '0';
        if (canMove(clickedX, clickedY)){
            updateMove();
            swap(clickedX,clickedY,clicked);
        }
        if (gameOver()){
            Toast.makeText(this, "Siz g'olibsiz", Toast.LENGTH_SHORT).show();
            binding.chronometer.stop();
        }
    }
    //endregion
    //region update
    private void updateMove(){
        move++;
        binding.textMove.setText("Move: " + move);
    }
    private void updateMove(int move){
        binding.textMove.setText("Move: " + move);
    }
    //endregion
}