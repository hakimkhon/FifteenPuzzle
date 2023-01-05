package uz.hakimkhon.puzzle15;

import static uz.hakimkhon.puzzle15.R.*;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import kotlin.jvm.internal.CollectionToArray;
import uz.hakimkhon.puzzle15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Integer> numbers = new ArrayList<>();
    int x, y, counter = 1;
    Button emptyBtn;
    private ClassLoader contextInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        emptyBtn = binding.btn0;
        generateNumbers();
//        x = binding.gridLayout.getChildAt(15).getTag().toString().charAt(0) - '0';
//        y = binding.gridLayout.getChildAt(15).getTag().toString().charAt(1) - '0';
    }
    private void generateNumbers(){
        for (int i = 1; i <= 16; i++){
            numbers.add(i);
        }
//        Collections.shuffle(numbers);
        for (int i = 0; i <= binding.gridLayout.getChildCount()-1; i++){
            if (numbers.get(i) != 16) {
                ((Button) binding.gridLayout.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
            }
            else {

                // i == 16 katakchani bo'sh qoldirish
                ((Button) binding.gridLayout.getChildAt(i)).setText("");
                // bo'sh katakni tag ni topish, uni x va y ga o'zlashtirish
                emptyBtn = ((Button) binding.gridLayout.getChildAt(i));

//                binding.gridLayout.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                x = binding.gridLayout.getChildAt(i).getTag().toString().charAt(0) - '0';
                y = binding.gridLayout.getChildAt(i).getTag().toString().charAt(1) - '0';
            }
        }
        if (!isSolvable())
            generateNumbers();
    }
    private boolean isSolvable(){
        int countInversion = 0;
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < i; j++){
                if (numbers.get(j) > numbers.get(i))
                    countInversion++;
            }
        }
        return countInversion % 2 == 0;
    }
    private boolean canMove(int clickedX, int clickedY){
        return Math.abs((clickedX + clickedY) - (x + y)) == 1 && Math.abs(clickedX - x) != 2 && Math.abs(clickedY - y) != 2;
    }
    private boolean gameOver(){
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
    private void swap(int clickedX, int clickedY, Button clicked){
        String text = clicked.getText().toString();
        clicked.setText("");
        emptyBtn.setText(text);
        emptyBtn = clicked;
        x = clickedX;
        y = clickedY;
    }
    public void reStart(View view){

        Collections.shuffle(numbers);
        for (int i = 0; i <= binding.gridLayout.getChildCount()-1; i++){
            if (numbers.get(i) != 16) {
                ((Button) binding.gridLayout.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
            }
            else {

                // i == 16 katakchani bo'sh qoldirish
                ((Button) binding.gridLayout.getChildAt(i)).setText("");
                // bo'sh katakni tag ni topish, uni x va y ga o'zlashtirish
                emptyBtn = ((Button) binding.gridLayout.getChildAt(i));
//                binding.gridLayout.getChildAt(i).setBackgroundResource(R.drawable.color);
                x = binding.gridLayout.getChildAt(i).getTag().toString().charAt(0) - '0';
                y = binding.gridLayout.getChildAt(i).getTag().toString().charAt(1) - '0';
            }
        }

//                if (numbers.get(i) != 16) {
//                }
//                else ((Button) binding.gridLayout.getChildAt(i)).setText("");

    }
    public void onClick(View view){
        counter = 1;
        Button clicked = (Button) view;
        String tag = view.getTag().toString();
        int clickedX = tag.charAt(0) - '0';
        int clickedY = tag.charAt(1) - '0';
        if (canMove(clickedX, clickedY)){
            swap(clickedX,clickedY,clicked);
        }
        if (gameOver()){
            Toast.makeText(this, "Siz g'olibsiz", Toast.LENGTH_SHORT).show();
        }
    }
}