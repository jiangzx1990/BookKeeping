package com.jzx.book.bookkeeping.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.ui.dialog.Loading;

/**
 * Created by Jzx on 2019/1/15
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int NO_LAYOUT = -1;

    public abstract int providerLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutRes = providerLayoutRes();
        if(layoutRes!=NO_LAYOUT){
            setContentView(layoutRes);
            Toolbar toolbar = findViewById(R.id.toolBar);
            if(toolbar != null){
                setSupportActionBar(toolbar);
            }

        }
        initView(savedInstanceState);
    }

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    private Loading loading;
    protected void showLoadingDialog(){
        showLoadingDialog(getString(R.string.app_text_loading));
    }

    protected void showLoadingDialog(@NonNull CharSequence text){
        if(loading == null){
            loading = new Loading(this);
        }
        loading.setLoadingText(text);
        if(!isFinishing() && !loading.isShowing()){
            loading.show();
        }
    }

    protected void dismissLoadingDialog(){
        if(loading!=null &&loading.isShowing()&&!isFinishing()){
            loading.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
