package uz.hakimkhon.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int[][][][] arr = new int[4][4][4][4];

    public void setArr(int[][][][] arr) {
        this.arr = arr;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arr[0][0][0][0] = 1;
        arr[0][0][0][1] = 2;
        arr[0][0][0][2] = 3;
        arr[0][0][0][3] = 4;
        arr[0][0][0][0] = 5;
        arr[0][0][0][0] = 6;
        arr[0][0][0][0] = 7;
        arr[0][0][0][0] = 8;
        arr[0][0][0][0] = 9;
        arr[0][0][0][0] = 10;
        arr[0][0][0][0] = 11;
        arr[0][0][0][0] = 12;
        arr[0][0][0][0] = 13;
        arr[0][0][0][0] = 14;
        arr[0][0][0][0] = 15;
    }
}