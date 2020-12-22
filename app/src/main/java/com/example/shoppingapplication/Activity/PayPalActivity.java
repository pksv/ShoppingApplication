package com.example.shoppingapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.shoppingapplication.Database.Order;
import com.example.shoppingapplication.Fragments.PaymentResultFragment;
import com.example.shoppingapplication.OtherClasses.PayPalConfig;
import com.example.shoppingapplication.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static com.example.shoppingapplication.Fragments.SecondCartFragment.ORDER_KEY;

public class PayPalActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String TAG = "PayPalActivity";
    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            .merchantName("Shopping Application")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        initViews();
        initButtonNavView();
        setSupportActionBar(toolbar);

        Intent intent1 = getIntent();
        final String jsonOrder = intent1.getStringExtra(ORDER_KEY);
        if (jsonOrder != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Order>() {
            }.getType();
            order = gson.fromJson(jsonOrder, type);
            if (order != null) {
                order.setPaymentMethod("PayPal");
                Intent intent = new Intent(this, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(intent);
                getPayment();
            }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    String confirmCode = String.valueOf(confirm.getPayment().toJSONObject());
                    Log.d(TAG, "onActivityResult: confirmCode = " + confirmCode);
                    success(confirmCode);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void getPayment() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String invoice = new String(array, StandardCharsets.UTF_8);
        PayPalPayment payPalPayment = new PayPalPayment(BigDecimal.valueOf(order.getTotalPrice()),
                "USD", "Test", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intentPayment = new Intent(this, PaymentActivity.class);
        intentPayment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        payPalPayment.invoiceNumber(invoice);
        intentPayment.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intentPayment, PAYPAL_REQUEST_CODE);
        onActivityResult(PAYPAL_REQUEST_CODE, Activity.RESULT_OK, intentPayment);
    }

    private void success(String code) {
        order.setSuccess(true);
        Bundle resultBundle = new Bundle();
        Gson gson = new Gson();
        resultBundle.putString(ORDER_KEY, gson.toJson(order));
        PaymentResultFragment paymentResultFragment = new PaymentResultFragment();
        paymentResultFragment.setArguments(resultBundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, paymentResultFragment);
        transaction.commit();
    }

    private void initButtonNavView() {
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    Intent searchIntent = new Intent(PayPalActivity.this, SearchActivity.class);
                    searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(searchIntent);
                    break;
                case R.id.home:
                    Intent homeIntent = new Intent(PayPalActivity.this, MainActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}