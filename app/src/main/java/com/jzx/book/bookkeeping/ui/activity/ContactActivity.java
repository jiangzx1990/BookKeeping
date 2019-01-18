package com.jzx.book.bookkeeping.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.dao.Contact;
import com.jzx.book.bookkeeping.db.ContactOperator;
import com.jzx.book.bookkeeping.ui.adapter.ContactAdapter;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;
import com.jzx.book.bookkeeping.ui.dialog.AddContactDialog;
import com.jzx.book.bookkeeping.utils.RegUtils;

import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener {
    public static final String CHOOSE_CONTACT_B = "chooseContact";
    public static final String CONTACT_ID_L = "contactId";
    public static final String CONTACT_NAME_S = "contactName";

    private boolean chooseContact;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private List<com.jzx.book.bookkeeping.dao.Contact> mData;
    private ContactAdapter adapter;
    @Override
    public int providerLayoutRes() {
        return R.layout.activity_contact_manager;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        final Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        chooseContact = getIntent().getBooleanExtra(CHOOSE_CONTACT_B,false);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
        recyclerView.addItemDecoration(new VerticalDecoration(0xFFDDDDDD,1));

        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = ContactOperator.getAllContacts();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        adapter = new ContactAdapter(mData);
                        adapter.setOnAdapterClickListener(new BaseAdapter.AdapterClickListener<Contact>(){
                            @Override
                            public void onClicked(int action, int listPosition, Contact contact) {
                                switch (action){
                                    case BaseAdapter.AdapterClickListener.ACTION_ITEM_CLICKED:
                                        if(chooseContact){
                                            Intent intent = getIntent();
                                            intent.putExtra(CONTACT_ID_L,contact.getId());
                                            intent.putExtra(CONTACT_NAME_S,contact.getName());
                                            setResult(RESULT_OK,intent);
                                            finish();
                                        }
                                        break;
                                    case BaseAdapter.AdapterClickListener.ACTION_ITEM_LONG_CLICKED:
                                        if (RegUtils.isCellphoneSimple(contact.getContact_tell())){
                                            showCallDialog(contact.getContact_tell());
                                        }
                                        break;
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private void showCallDialog(final String cellphone) {
        new AlertDialog.Builder(this)
                .setTitle("拨号")
                .setMessage("给TA打个电话？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + cellphone));
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            startActivity(intent);
                        } else {
                            Snackbar.make(recyclerView, "鹅，找不到拨打电话应用", Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .create().show();

    }

    private AddContactDialog addDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                //添加联系人
                if(addDialog == null){
                    addDialog = new AddContactDialog(this, new AddContactDialog.OnAddContactEnsure() {
                        @Override
                        public void onEnsureClick(final String name, final String tell) {
                            boolean check = true;
                            if(name.isEmpty()){
                                check = false;
                                addDialog.showNameError("联系人姓名不能为空");
                            }else{
                                addDialog.hideNameError();
                            }
                            if(!tell.isEmpty()){
                                if(RegUtils.isCellphoneSimple(tell)){
                                    addDialog.hideTellError();
                                }else{
                                    check = false;
                                    addDialog.showTellError("联系人电话号码错误");
                                }
                            }else{
                                addDialog.hideTellError();
                            }
                            if(check){
                                addDialog.dismiss();
                                showLoadingDialog();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Contact c = ContactOperator.queryContactByName(name);
                                        if(c != null){
                                            //已经存在
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dismissLoadingDialog();
                                                    Snackbar.make(recyclerView,"联系人["+name+"]已存在",Snackbar.LENGTH_LONG).show();
                                                }
                                            });
                                        }else{
                                            final Contact contact = ContactOperator.addContact(name, tell);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dismissLoadingDialog();
                                                    if(contact == null){
                                                        Snackbar.make(recyclerView,"添加失败，请稍后重试",Snackbar.LENGTH_LONG).show();
                                                    }else{
                                                        mData.add(contact);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }
                        }
                    });
                }
                addDialog.show();
                break;
        }
    }
}
