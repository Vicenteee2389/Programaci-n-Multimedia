package com.example.barapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity {

    private EditText etTotalCuenta;
    private CheckBox cbPropina;
    private SeekBar sbPropina;
    private RadioGroup rgPago;
    private RatingBar ratingBar;
    private Button btnCalcular;
    private TextView tvResultado;

    private int porcentajePropina = 10; // valor inicial de SeekBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular vistas
        etTotalCuenta = findViewById(R.id.etTotalCuenta);
        cbPropina = findViewById(R.id.cbPropina);
        sbPropina = findViewById(R.id.sbPropina);
        rgPago = findViewById(R.id.rgPago);
        ratingBar = findViewById(R.id.ratingBar);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);

        // Configurar SeekBar
        sbPropina.setProgress(porcentajePropina);
        sbPropina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentajePropina = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Botón calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularTotal();
            }
        });
    }

    private void calcularTotal() {
        String totalCuentaStr = etTotalCuenta.getText().toString().trim();

        // Validar que haya un valor
        if (TextUtils.isEmpty(totalCuentaStr)) {
            tvResultado.setText("Por favor, ingrese el valor de la cuenta.");
            return;
        }

        double totalCuenta;

        // Validar formato numérico
        try {
            totalCuenta = Double.parseDouble(totalCuentaStr);
        } catch (NumberFormatException e) {
            tvResultado.setText("Ingrese un valor numérico válido.");
            return;
        }

        // Validar valor positivo
        if (totalCuenta <= 0) {
            tvResultado.setText("El valor de la cuenta debe ser mayor que 0.");
            return;
        }

        // Calcular propina si está seleccionada
        double propina = 0;
        if (cbPropina.isChecked()) {
            propina = totalCuenta * porcentajePropina / 100.0;
        }

        double totalFinal = totalCuenta + propina;

        // Obtener método de pago
        int selectedId = rgPago.getCheckedRadioButtonId();
        String metodoPago = "No seleccionado";
        if (selectedId != -1) {
            RadioButton rbSeleccionado = findViewById(selectedId);
            metodoPago = rbSeleccionado.getText().toString();
        }

        // Obtener calificación
        float calificacion = ratingBar.getRating();

        // Mostrar resultado
        String resultado = String.format(
                "Total de la cuenta: $%.2f\nPropina: $%.2f (%d%%)\nMétodo de pago: %s\nCalificación del servicio: %.1f estrellas",
                totalFinal, propina, porcentajePropina, metodoPago, calificacion
        );

        tvResultado.setText(resultado);
    }
}
