package com.ext.android_materialstepperview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ext.android_material_stepper_view.MaterialStepperView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepper = findViewById<MaterialStepperView>(R.id.stepper)

        // Next button
        findViewById<View>(R.id.btnNext).setOnClickListener {
            stepper.next()
        }

        // Back button (no animation, just jump)
        findViewById<View>(R.id.btnBack).setOnClickListener {
            stepper.previous()
        }
    }
}
