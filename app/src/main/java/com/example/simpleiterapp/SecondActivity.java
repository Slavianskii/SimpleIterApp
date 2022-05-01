package com.example.simpleiterapp;


//import Jama.Matrix;


import Jama.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    double[] vector_B;
    double epsilon;
    int accuracy;
    double[] x_0;
    double[][] matrix;
    int dim;
    double chara;
    int current_n = 0, max_n;
    Matrix matrix_A, matrix_B, cur_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView txt = findViewById(R.id.matrix_view);
        Bundle arguments = getIntent().getExtras();


        List<Double> vct_B = new ArrayList<Double>();
        for(String item : arguments.get("vector_b").toString().split(" ")){
            vct_B.add(Double.parseDouble(item));
        }
        vector_B = new double[vct_B.size()];
        for(int i = 0; i < vct_B.size();++i){
            vector_B[i] = vct_B.get(i);
        }

        epsilon = Double.parseDouble(arguments.get("epsilon").toString());
        dim = vct_B.size();
        accuracy = String.valueOf(epsilon).split("\\.")[1].length();

        List<Double> start_x = new ArrayList<Double>();
        for(String item : arguments.get("x_0").toString().split(" ")){
            start_x.add(Double.parseDouble(item));
        }
        x_0 = new double[start_x.size()];
        for(int i = 0; i < start_x.size();++i){
            x_0[i] = start_x.get(i);
        }


        matrix = new double[start_x.size()][start_x.size()];
        int i =0;
        for(String line:arguments.get("matrix").toString().trim().split("\n")){
            int j = 0;
            for(String item:line.trim().split(" ")){
                matrix[i][j] = Double.parseDouble(item);
                j++;
            }
            i++;
        }

        /*
        for(ArrayList<Double> line:mtrx){
            int j = 0;
            for(double item : line){
                matrix[i][j] = item;
                j++;
            }
            i++;
        }*/

        start_iter();

    }
    public void mAsolve(){
        for (int i = 0; i < dim; i++) {
            vector_B[i] /= matrix[i][i];
            for (int j = 0; j < dim; j++) {
                if(j != i){
                    matrix[i][j] *= -1;
                    matrix[i][j] /= matrix[i][i];
                }
            }
            matrix[i][i] = 0.0;
        }
    }

    public String abg(){
        double alpha = -1.0;
        double beta = -1.0;
        double gamma = 0.0;
        double summ = 0.0;
        for(double[] line:matrix){
            for(double item:line){
                summ += Math.abs(item);
                gamma += Math.pow(item, 2);
            }
            if(summ > alpha) alpha = summ;
            summ = 0.0;
        }
        for(int i = 0; i < dim; i++){
            for (int j = 0; j < dim; j++) {
                summ += Math.abs(matrix[j][i]);
            }
            if(summ > beta) beta = summ;
            summ = 0.0;
        }
        chara = Math.min(alpha, Math.min(beta, gamma));
        TextView txt = findViewById(R.id.char_view);
        txt.setText("a, b, g: " + Double.toString(alpha) + ", " +
                Double.toString(beta) + ", " + Double.toString(gamma));
        if(chara == alpha){
            return "alpha";
        }
        else if(chara == beta){
            return "beta";
        }
        return "gamma";
    }

    public void start_iter(){
        mAsolve();
        String p = abg();

        TextView txt_mtr = findViewById(R.id.matrix_view);
        TextView txt_n = findViewById(R.id.iter_view);

        matrix_A = new Matrix(matrix);
        matrix_B = new Matrix(vector_B, dim);
        Matrix tmp_x_0 = new Matrix(x_0, dim);
        cur_x = matrix_A.times(tmp_x_0);
        cur_x = cur_x.plus(matrix_B);
        current_n++;
        double ro = 0.0;
        if(p == "alpha" && chara < 1.0){
            for (int i = 0; i < dim; i++) {
                double tmp = cur_x.get(i, 0) - tmp_x_0.get(i, 0);
                if(tmp > ro) ro = tmp;
            }
        }
        else if(p == "beta" && chara < 1.0){
            for (int i = 0; i < dim; i++) {
                ro += Math.abs(cur_x.get(i, 0) - tmp_x_0.get(i, 0));
            }
        }
        else if(p == "gamma" && chara < 1.0){
            for (int i = 0; i < dim; i++) {
                ro += Math.pow(cur_x.get(i, 0) - tmp_x_0.get(i, 0), 2);
            ro = Math.sqrt(ro);
            }
        }
        else{
            Toast toast = Toast.makeText(this, "Chara's are more then 1",Toast.LENGTH_LONG);
            toast.show();
            this.finish();
        }
        max_n = (int) (Math.log((epsilon * (1.0 - chara))/ro)/Math.log(chara) + 1);
        txt_n.setText("n: " + Integer.toString(current_n) + "/" + Integer.toString(max_n));
        txt_mtr.setText("x" + Integer.toString(current_n) + "\n" + MatrixToString(cur_x));
    }

    public String MatrixToString(Matrix mtr){
        String result = new String();
        result += "[ ";
        for(int i = 0; i < dim; i++){
            result += String.format("%." + Integer.toString(accuracy) + "f", mtr.get(i, 0));
            if(i + 1 != dim) result += "\n";
        }
        result += "]";
        return result;
    }



    public void clear_click(View view){
        this.finish();
    }

    public void next_click(View view){
        if(current_n < max_n){
            TextView txt_mtr = findViewById(R.id.matrix_view);
            TextView txt_n = findViewById(R.id.iter_view);

            cur_x = matrix_A.times(cur_x);
            cur_x = cur_x.plus(matrix_B);
            current_n++;

            txt_n.setText("n: " + Integer.toString(current_n) + "/" + Integer.toString(max_n));
            txt_mtr.setText("x" + Integer.toString(current_n) + "\n" + MatrixToString(cur_x));
        }
        else{
            Toast toast = Toast.makeText(this, "Iteration is over",Toast.LENGTH_LONG);
            toast.show();
        }

    }
}