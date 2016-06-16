# LoadingButton
一个带圆点进度显示的按钮
模仿windows效果

# Demo
![Sample Screenshots][1]

# Sample Usage
There are four status in LoadingButton  
Normal  ---->  1  
Process  ---->  2 ~ 99  
Complete  ----> 100  
Error  ---->  -1  

### Layout
        <com.teresazl.library.impl.LoadingButton
            android:id="@+id/loading_btn_normal"
            android:layout_width="match_parent"
            android:layout_height="48dp"
            android:text="Send"
            android:gravity="center"
            android:textColor="@android:color/white"
            android:textSize="18sp"
            app:complete_text="DONE" />

### Activity
        // normal
        normalBtn = (LoadingButton) findViewById(R.id.loading_btn_normal);
        normalBtn.setOnLoadCompleteListener(this);
        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ...
            }
        });
        
        // complete
        completeBtn = (LoadingButton) findViewById(R.id.loading_btn_complete);
        completeBtn.setProgress(100);

        // error
        errorBtn = (LoadingButton) findViewById(R.id.loading_btn_error);
        errorBtn.setProgress(-1);

        // process
        processBtn = (LoadingButton) findViewById(R.id.loading_btn_process);
        processBtn.setProgress(50);
        
### CallBack

        public class MainActivity extends AppCompatActivity implements ProcessButton.LoadCompleteListener {
            ....
            ....
            
            @Override
            public void loadComplete() {
                Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
            }
        }

[1]: https://github.com/teresazl/LoadingButton/blob/master/screenshots/loading_button.gif
