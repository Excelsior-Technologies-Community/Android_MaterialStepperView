# **MaterialStepperView**

---
MaterialStepperView is a custom Android stepper view library for showing progress through a sequence of steps, with smooth forward animation, circle indicators, and customizable colors and titles. Works for horizontal and vertical orientations.

---

## ✨ **Features**

- Smooth forward progress animation for line and circle.

- Instant backward navigation without animation.

- Customizable step count, titles, and colors.

- Horizontal or Vertical orientation.

- Fully XML-configurable, no code changes needed for customization.



  ---

# **Preview**
---
<img src="https://github.com/S13reya/Android_MaterialStepperView/blob/stages/app/src/main/assets/demovideo.gif" height="320"/>



## ⚡ **Installation**

**Step 1:** Add JitPack repository to your root build.gradle:

```gradle
maven { url = uri("https://jitpack.io") }
```

**Step 2:** Add the dependency in your app `build.gradle` (example if hosted on JitPack):  

```gradle
dependencies {
	        implementation 'com.github.Excelsior-Technologies-Community:Android_SwipeToUp:1.0.0'

}
```
## ⚡ **attrs file**

```

<?xml version="1.0" encoding="utf-8"?>
<resources>

    <declare-styleable name="MaterialStepperView">

        <!-- Steps -->
        <attr name="stepCount" format="integer"/>
        <attr name="currentStep" format="integer"/>

        <!-- Titles -->
        <attr name="stepTitles" format="reference"/>
        <attr name="titleColor" format="color"/>
        <attr name="titleSize" format="dimension"/>

        <!-- Colors -->
        <attr name="activeColor" format="color"/>
        <attr name="completedColor" format="color"/>
        <attr name="pendingColor" format="color"/>

        <!-- Sizes -->
        <attr name="stepRadius" format="dimension"/>
        <attr name="lineThickness" format="dimension"/>

        <!-- Orientation -->
        <attr name="stepOrientation">
            <enum name="horizontal" value="0"/>
            <enum name="vertical" value="1"/>
        </attr>

        <!-- Animation -->
        <attr name="animateSteps" format="boolean"/>

    </declare-styleable>

</resources>





```

## ⚡ **Usage**

1. Add in XML

```

<com.ext.android_material_stepper_view.MaterialStepperView
    android:id="@+id/stepper"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:stepCount="4"
    app:currentStep="0"
    app:stepTitles="@array/step_titles"
    app:activeColor="@color/purple_500"
    app:completedColor="@color/green_500"
    app:pendingColor="@color/gray_400"
    app:titleColor="@color/black"
    app:stepOrientation="0" /> <!-- 0 = Horizontal, 1 = Vertical -->


```

2.Define step titles in res/values/strings.xml:

```
<string-array name="step_titles">
    <item>Login</item>
    <item>Choose Plan</item>
    <item>Payment</item>
    <item>Finish</item>
</string-array>

```

## **2. Setup in Activity**
```

val stepper = findViewById<MaterialStepperView>(R.id.stepper)

// Move to next step with smooth animation
findViewById<View>(R.id.btnNext).setOnClickListener {
    stepper.next()
}

// Move back instantly
findViewById<View>(R.id.btnBack).setOnClickListener {
    stepper.previous()
}


```






## **📄 License**

**MIT License**  
```
Copyright (c) 2025 Excelsior Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED **"AS IS"**, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```



  
