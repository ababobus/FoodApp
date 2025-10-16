package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);

        displayCart();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnMenuHeader = findViewById(R.id.btnMenuHeader);
        btnMenuHeader.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            displayCart();
            Toast.makeText(this, "Корзина очищена!", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayCart() {
        LinearLayout cartLayout = findViewById(R.id.cartLayout);
        cartLayout.removeAllViews();

        String[] productNames = {"Пицца Маргарита", "Пицца Пепперони", "Бургер", "Салат Цезарь", "Кола"};
        double[] prices = {450.0, 550.0, 320.0, 350.0, 120.0};

        double total = 0;
        boolean hasItems = false;

        for (int i = 0; i < productNames.length; i++) {
            String productName = productNames[i];
            double price = prices[i];

            int quantity = sharedPreferences.getInt(productName, 0);

            if (quantity > 0) {
                hasItems = true;
                double itemTotal = price * quantity;
                total += itemTotal;

                LinearLayout cartItem = createCartItem(productName, quantity, price, itemTotal);
                cartLayout.addView(cartItem);
            }
        }

        if (!hasItems) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Корзина пуста");
            emptyText.setTextSize(18);
            emptyText.setPadding(50, 50, 50, 50);
            cartLayout.addView(emptyText);
        } else {
            TextView totalText = new TextView(this);
            totalText.setText("Итого: " + total + " руб.");
            totalText.setTextSize(20);
            totalText.setTextColor(getResources().getColor(android.R.color.black));
            totalText.setPadding(50, 30, 50, 30);
            cartLayout.addView(totalText);
        }
    }

    private LinearLayout createCartItem(String productName, int quantity, double price, double itemTotal) {
        LinearLayout cartItem = new LinearLayout(this);
        cartItem.setOrientation(LinearLayout.VERTICAL);
        cartItem.setPadding(50, 20, 50, 20);
        cartItem.setBackgroundColor(0xFFF8F8F8);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView nameText = new TextView(this);
        nameText.setText(productName);
        nameText.setTextSize(16);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        nameText.setLayoutParams(nameParams);

        TextView totalText = new TextView(this);
        totalText.setText(itemTotal + " руб.");
        totalText.setTextSize(16);
        totalText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        topRow.addView(nameText);
        topRow.addView(totalText);

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setPadding(0, 10, 0, 0);

        TextView priceText = new TextView(this);
        priceText.setText(price + " руб./шт");
        priceText.setTextSize(14);
        priceText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        priceText.setLayoutParams(priceParams);

        LinearLayout quantityLayout = new LinearLayout(this);
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button btnDecrease = new Button(this);
        btnDecrease.setText("-");
        btnDecrease.setBackgroundColor(0xFFFF6B35);
        btnDecrease.setTextColor(0xFFFFFFFF);
        btnDecrease.setOnClickListener(v -> updateQuantity(productName, quantity - 1));

        TextView quantityText = new TextView(this);
        quantityText.setText(" " + quantity + " ");
        quantityText.setTextSize(16);
        quantityText.setPadding(20, 0, 20, 0);

        Button btnIncrease = new Button(this);
        btnIncrease.setText("+");
        btnIncrease.setBackgroundColor(0xFFFF6B35);
        btnIncrease.setTextColor(0xFFFFFFFF);
        btnIncrease.setOnClickListener(v -> updateQuantity(productName, quantity + 1));

        quantityLayout.addView(btnDecrease);
        quantityLayout.addView(quantityText);
        quantityLayout.addView(btnIncrease);

        bottomRow.addView(priceText);
        bottomRow.addView(quantityLayout);

        cartItem.addView(topRow);
        cartItem.addView(bottomRow);

        return cartItem;
    }

    private void updateQuantity(String productName, int newQuantity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (newQuantity <= 0) {
            editor.remove(productName);
            Toast.makeText(this, productName + " удален из корзины", Toast.LENGTH_SHORT).show();
        } else {
            editor.putInt(productName, newQuantity);
        }

        editor.apply();
        displayCart();
    }
}