package uz.hakimkhon.puzzle15;

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
    int x, y, move, counter = 1;
    long timeWhenStopped = 0;
    Button emptyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        load();
        generateNumbers();
        colorButtons();
    }
    private void load(){
        for (int i = 1; i <= 16; i++){
            numbers.add(i);
        }
    }
    private void generateNumbers(){
        move = 0;
        binding.chronometer.setBase(SystemClock.elapsedRealtime());
        binding.chronometer.stop();
        binding.chronometer.start();
        do {
            Collections.shuffle(numbers);
        } while (!isSolvable(numbers));
        for (int i = 0; i < binding.gridLayout.getChildCount(); i++){
            if (numbers.get(i) == 16) {
                String tag = binding.gridLayout.getChildAt(i).getTag().toString();
                x = tag.charAt(0) - '0';
                y = tag.charAt(1) - '0';
                ((Button) binding.gridLayout.getChildAt(i)).setText("");
                emptyBtn = ((Button) binding.gridLayout.getChildAt(i));
                emptyBtn.setVisibility(View.INVISIBLE);
            }
            else {
                ((Button) binding.gridLayout.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
            }
        }
    }
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
        return Math.abs((clickedX + clickedY) - (x + y)) == 1 && Math.abs(clickedX - x) != 2 && Math.abs(clickedY - y) != 2;
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
    private void swap(int clickedX, int clickedY, Button clicked){
        String text = clicked.getText().toString();
        clicked.setText("");
        clicked.setVisibility(View.INVISIBLE);
        emptyBtn.setVisibility(View.VISIBLE);
        emptyBtn.setText(text);
        emptyBtn = clicked;
        x = clickedX;
        y = clickedY;
    }
    public void reStart(View view){
        emptyBtn.setVisibility(View.VISIBLE);
        generateNumbers();
        colorButtons();
        updateMove(move);
    }
    public void pause(View view){
        if (binding.btnPause.getText().equals("PAUSE")) {
            timeWhenStopped = binding.chronometer.getBase() - SystemClock.elapsedRealtime();
            binding.chronometer.stop();
            binding.btnPause.setText("START");
        }
        else if (binding.btnPause.getText().equals("START")) {
            binding.chronometer.start();
            binding.btnPause.setText("PAUSE");
        }
    }

    //region start_stop

    @Override
    protected void onStart() {
        binding.chronometer.stop();
        super.onStart();
    }

    @Override
    protected void onStop() {
        timeWhenStopped = binding.chronometer.getBase() - SystemClock.elapsedRealtime();
        binding.chronometer.stop();
        super.onStop();
    }


    //    public void start() {
//        setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
//        super.start();
//    }
//
//    @Override
//    public void stop() {
//        super.stop();
//        timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
//    }

//    public void reset() {
//        stop();
//        setBase(SystemClock.elapsedRealtime());
//        timeWhenStopped = 0;
//    }
    //endregion


    public void onClick(View view){
        counter = 1;
        binding.chronometer.start();
        binding.btnPause.setText("PAUSE");
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
    private void updateMove(){
        move++;
        binding.textMove.setText(String.valueOf(move));
    }
    private void updateMove(int move){
        binding.textMove.setText(String.valueOf(move));
    }
}