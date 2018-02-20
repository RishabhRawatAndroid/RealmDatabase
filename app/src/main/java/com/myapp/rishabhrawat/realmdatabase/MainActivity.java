package com.myapp.rishabhrawat.realmdatabase;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//my own Toast Library package name
import com.myapp.rishabhrawat.toastlogutil.ToastLogUtil;

//this is real data package
import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.name) EditText name;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.gmail) EditText email;
    @BindView(R.id.address) EditText address;

    @BindView(R.id.showdata) TextView text;

    @BindView(R.id.insert) Button insert;
    @BindView(R.id.update) Button update;
    @BindView(R.id.delete) Button delete;
    @BindView(R.id.retrive) Button reterive;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();


        //insert the data to the realm database
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text.setText("");
                int check = check_empty_string(name.getText().toString(), phone.getText().toString(), email.getText().toString(), address.getText().toString(), "Please enter all details...");
                if (check == 0) {
                    clearData();
                } else {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            //second argument represent the primary key
                            UserInformation information = realm.createObject(UserInformation.class, phone.getText().toString());
                            information.setName(name.getText().toString());
                            information.setGmail(email.getText().toString());
                            information.setAddress(address.getText().toString());
                            realm.copyToRealm(information);

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "Successfully inserted data..", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(MainActivity.this, "Something wrong please try again later..", Toast.LENGTH_SHORT).show();
                            ToastLogUtil.errorlog(error.toString());
                            ToastLogUtil.errorlog(error.getMessage());
                            clearData();
                        }
                    });

                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("");
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        int check = check_empty_string(name.getText().toString(), phone.getText().toString(), email.getText().toString(), address.getText().toString(), "Please enter all details...");
                        if (check == 0) {
                            clearData();
                        } else {
                            RealmResults<UserInformation> query = realm.where(UserInformation.class).equalTo("phone", phone.getText().toString()).findAll();
                            //if query not gives any result then query.size() return give 0 value
                            if (query.size() == 0) {
                                ToastLogUtil.toastmessage(MainActivity.this, "You entered wrong information or might be your entered phone no not matches existing information");
                            } else {
                                for (UserInformation info : query) {
                                    info.setName(name.getText().toString());
                                  //  info.setPhone(phone.getText().toString());
                                    info.setGmail(email.getText().toString());
                                    info.setAddress(address.getText().toString());
                                    realm.copyToRealm(info);
                                }
                                ToastLogUtil.toastmessage(MainActivity.this, "Successfully updated data");
                            }
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        clearData();
                        ToastLogUtil.toastmessage(MainActivity.this, "Your Information Updated Successfully..");

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        clearData();
                        ToastLogUtil.toastmessage(MainActivity.this, "Your Information not Updated something wrong...");
                        ToastLogUtil.errorlog(error.toString());
                    }
                });

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("");
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    clearData();
                    ToastLogUtil.toastmessage(MainActivity.this, "Please mention the phone number you want to delete");
                } else {

                    RealmResults<UserInformation> query = realm.where(UserInformation.class).equalTo("phone", phone.getText().toString()).findAllAsync();
                    if (query.size() == 0) {
                         ToastLogUtil.toastmessage(MainActivity.this,"Your phone no not matches in our database phone no..");
                    } else {
                        realm.beginTransaction();
                        query.deleteAllFromRealm();
                        realm.commitTransaction();
                        clearData();
                        ToastLogUtil.toastmessage(MainActivity.this, "Delete data successfully...");
                    }
                }
            }
        });


        reterive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text.setText("");
                if (TextUtils.isEmpty(name.getText().toString()) && TextUtils.isEmpty(phone.getText().toString()) && TextUtils.isEmpty(email.getText().toString()) && TextUtils.isEmpty(address.getText().toString())) {
                    RealmResults<UserInformation> query = realm.where(UserInformation.class).findAllAsync();
                    for (UserInformation info : query) {
                        text.append(info.getName() + "\n");
                        text.append(info.getPhone() + "\n");
                        text.append(info.getGmail() + "\n");
                        text.append(info.getAddress() + "\n");
                    }
                    clearData();
                    ToastLogUtil.toastmessage(MainActivity.this,"Reterive all data successfully...");
                } else {
                    RealmResults<UserInformation> query = realm.where(UserInformation.class).equalTo("phone", phone.getText().toString()).findAll();
                    for (UserInformation info : query) {
                        text.append(info.getName() + "\n");
                        text.append(info.getPhone() + "\n");
                        text.append(info.getGmail() + "\n");
                        text.append(info.getAddress() + "\n");
                        ToastLogUtil.toastmessage(MainActivity.this,"Reterive "+info.getPhone()+ " data successfully...");
                    }
                }
            }
        });


    }


    private void clearData() {
        name.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
    }

    private int check_empty_string(String name, String phone, String email, String address, String message) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(message);
            builder.setTitle("Important message");
            builder.setIcon(R.drawable.warn);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
