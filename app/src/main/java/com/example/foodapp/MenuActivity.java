package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    // Массив данных о продуктах
    private String[] productNames = {"Пицца Маргарита", "Пицца Пепперони", "Бургер", "Салат Цезарь", "Кола"};
    private double[] prices = {450.0, 550.0, 320.0, 350.0, 120.0};
    private int[] imageResources = {
            R.drawable.pizza_margarita,    // Замените на ваши PNG файлы
            R.drawable.pizza_pepperoni,
            R.drawable.burger,
            R.drawable.salad,
            R.drawable.cola
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);

        // Создание списка продуктов
        createProductList();

        // Кнопка назад
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Кнопка корзины в заголовке
        Button btnCartHeader = findViewById(R.id.btnCartHeader);
        btnCartHeader.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void createProductList() {
        LinearLayout productLayout = findViewById(R.id.productLayout);

        for (int i = 0; i < productNames.length; i++) {
            String productName = productNames[i];
            double price = prices[i];
            int imageRes = imageResources[i];

            // Создаем контейнер для продукта
            LinearLayout productItem = new LinearLayout(this);
            productItem.setOrientation(LinearLayout.HORIZONTAL);
            productItem.setPadding(30, 20, 30, 20);

            // Картинка продукта
            ImageView productImage = new ImageView(this);
            productImage.setImageResource(imageRes);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    450,  // ширина в пикселях
                    300   // высота в пикселях
            );
            productImage.setLayoutParams(imageParams);
            productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            productImage.setPadding(10, 10, 10, 10);

            // Текстовый контейнер
            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
            textLayout.setLayoutParams(textParams);
            textLayout.setPadding(20, 0, 20, 0);

            // Название продукта
            TextView productText = new TextView(this);
            productText.setText(productName);
            productText.setTextSize(18);
            productText.setTypeface(null, android.graphics.Typeface.BOLD); // Исправлено

            // Описание и цена
            TextView priceText = new TextView(this);
            priceText.setText(price + " руб.");
            priceText.setTextSize(16);
            priceText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            priceText.setPadding(0, 5, 0, 0);

            // Кнопка добавления в корзину
            Button addButton = new Button(this);
            addButton.setText("В корзину");
            addButton.setBackgroundColor(0xFF4CAF50);
            addButton.setTextColor(0xFFFFFFFF);
            addButton.setOnClickListener(v -> addToCart(productName));
            addButton.setPadding(20, 10, 20, 10);

            // Добавляем элементы в текстовый layout
            textLayout.addView(productText);
            textLayout.addView(priceText);
            textLayout.addView(addButton);

            // Добавляем картинку и текст в основной контейнер
            productItem.addView(productImage);
            productItem.addView(textLayout);

            // Добавляем разделитель
            android.view.View divider = new android.view.View(this); // Исправлено
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
            );
            dividerParams.setMargins(30, 10, 30, 10);
            divider.setLayoutParams(dividerParams);
            divider.setBackgroundColor(0xFFE0E0E0);

            productLayout.addView(productItem);
            productLayout.addView(divider);
        }
    }

    private void addToCart(String productName) {
        int currentQuantity = sharedPreferences.getInt(productName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(productName, currentQuantity + 1);
        editor.apply();
        Toast.makeText(this, productName + " добавлен в корзину!", Toast.LENGTH_SHORT).show();
    }
}