    #version 330 core

    in float fColorFactor;

    out vec4 fragColor;

    uniform float sysTime;

    void main()
    {
        // Colors all fragments black
        //fragColor = vec4(0, 0, 0, 1);

        float red, green, blue;
        float hueStages = 6f;
        float hueChangeSpeed = 1500f;
        float colorSlider = mod(mod(sysTime / hueChangeSpeed, 6f) + fColorFactor, hueStages);
        if(colorSlider <= 0) {
            red = 1f;
            green = blue = 0f;
        }
        else if(colorSlider > 0 && colorSlider <= 1) {
            red = 1f;
            green = colorSlider;
            blue = 0f;
        }
        else if(colorSlider > 1 && colorSlider <= 2) {
            red = 2 - colorSlider;
            green = 1f;
            blue = 0f;
        }
        else if(colorSlider > 2 && colorSlider <= 3) {
            red = 0f;
            green = 1f;
            blue = colorSlider - 2;
        }
        else if(colorSlider > 3 && colorSlider <= 4) {
            red = 0f;
            green = 4 - colorSlider;
            blue = 1f;
        }
        else if(colorSlider > 4 && colorSlider <= 5) {
            red = colorSlider - 4;
            green = 0f;
            blue = 1f;
        }
        else if(colorSlider > 5 && colorSlider <= 6) {
            red = 1f;
            green = 0f;
            blue = 6 - colorSlider;
        }

        fragColor = vec4(red, green, blue, 1);
    }