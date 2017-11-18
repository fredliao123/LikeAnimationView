package bupt.liao.fred.likeanimationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import bupt.liao.fred.likeanimationview.view.LikeUpView;
import bupt.liao.fred.likeanimationview.view.LikeView;


public class MainActivity extends AppCompatActivity {
    EditText edNum;
    LikeUpView likeUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edNum = (EditText) findViewById(R.id.ed_num);
        likeUpView = (LikeUpView) findViewById(R.id.newLikeUpView);

        likeUpView.setLikeUpClickListener(new LikeView.LikeUpClickListener() {

            @Override
            public void LikeUpFinish() {

            }

            @Override
            public void LikeDownFinish() {

            }
        });
    }

    public void setNum(View v) {
        try {
            int num = Integer.valueOf(edNum.getText().toString().trim());
            likeUpView.setCount(num).setLikeUp(false);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Integer Only", Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}